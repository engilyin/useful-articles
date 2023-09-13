/*
 Copyright 2022 engilyin

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

      https://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package com.engilyin.usefularticles.s3;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;
import com.engilyin.usefularticles.utils.AppPropertiesLoader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.client.config.ClientAsyncConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.SdkEventLoopGroup;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

@Slf4j
public class S3MultipartTest {

    Properties appProperties;

    @BeforeEach
    public void loadAppProperties() {
        this.appProperties = new AppPropertiesLoader().properties("application.properties");
    }

    @Test
    public void simpleS3Test() throws IOException, InterruptedException {

        BucketAttachmentConfigProperties props = createBucketAttachmentConfigProperties(appProperties);
        var s3AsyncClient = createS3AsyncClient(props, credentialsProvider(props));

        var path = Paths.get("D:\\tmp\\Jenny.mp3");

        var fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        var dataBuffer = DataBufferUtils.readAsynchronousFileChannel(() -> fileChannel, new DefaultDataBufferFactory(),
                4096);

        CompletableFuture<CreateMultipartUploadResponse> uploadRequest = s3AsyncClient
                .createMultipartUpload(CreateMultipartUploadRequest.builder()
                        .key("Jenny.mp3")
                        // .contentType(mediaType.toString())
                        .bucket(props.getBucketName())
                        .build());
        UploadState uploadState = new UploadState("Jenny.mp3");
        Mono<Boolean> r = Mono.fromFuture(uploadRequest).publishOn(Schedulers.parallel()).flatMapMany(response -> {
            uploadState.uploadId = response.uploadId();
            return dataBuffer;
        }).bufferUntil(buffer -> {
            uploadState.buffered.getAndAdd(buffer.readableByteCount());
            if (uploadState.buffered.get() >= 5242880) {
                uploadState.buffered.set(0);
                return true;
            } else {
                return false;
            }
        })
                .map(this::concatBuffers)
                .flatMap(buffer -> uploadPart(s3AsyncClient, props, uploadState, buffer))
                .onBackpressureBuffer()
                .reduce(uploadState, (state, completedPart) -> {
                    state.completedParts.put(completedPart.partNumber(), completedPart);
                    return state;
                })
                .flatMap(v -> completeUpload(s3AsyncClient, props, v))
                .doOnError(e -> log.error("Failed to proceed with multipart upload.", e))
                .then(Mono.just(true));

        r.subscribe(v -> log.info("S3 Uploaded by parts: {}", v));
        Thread.sleep(20000);
    }

    private BucketAttachmentConfigProperties createBucketAttachmentConfigProperties(Properties props) {
        var r = new BucketAttachmentConfigProperties();
        r.setAccessKeyId(appProperties.getProperty("articles.attachment.access-key-id"));
        r.setSecretAccessKey(appProperties.getProperty("articles.attachment.secret-access-key"));
        r.setBaseFolder(appProperties.getProperty("articles.attachment.base-folder"));
        r.setBucketName(appProperties.getProperty("articles.attachment.bucket-name"));
        r.setBucketRegion(appProperties.getProperty("articles.attachment.bucket-region"));
        return r;
    }

    private void handleError(Throwable throwable1) {

        log.error("Problem to put on S3", throwable1);
    }

    private boolean checkResult(PutObjectResponse putObjectResponse) {
        log.debug("Got response from S3: {}", putObjectResponse);

        return putObjectResponse.sdkHttpResponse().isSuccessful();
    }

    S3AsyncClient createS3AsyncClient(BucketAttachmentConfigProperties attachmentConfig,
            AwsCredentialsProvider credsProvider) {
        log.info("Creating the async client for S3 bucket storage. The backet name: {}, region: {}",
                attachmentConfig.getBucketName(), attachmentConfig.getBucketRegion());
        NettyNioAsyncHttpClient.Builder httpClientBuilder = NettyNioAsyncHttpClient.builder()
//              .connectionMaxIdleTime(props.getIdleConnectionTimeout())
//              .connectionTimeout(props.getConnectTimeout())
//              .readTimeout(props.getReadTimeout())
//              .writeTimeout(props.getWriteTimeout())
//              .maxConcurrency(props.getMaxConcurrentRequests())
//              .maxPendingConnectionAcquires(props.getMaxQueuedRequests())
                .eventLoopGroupBuilder(SdkEventLoopGroup.builder().numberOfThreads(5));// props.getEventLoopGroupThreadCount()));

        ClientAsyncConfiguration clientConfiguration = ClientAsyncConfiguration.builder()
                // since all the code is non-blocking, we can execute the completed futures in
                // the same event loop threads that send the requests
                .advancedOption(SdkAdvancedAsyncClientOption.FUTURE_COMPLETION_EXECUTOR, Runnable::run)
                .build();
//      String endpoint = createEndpointString(props);
//      log.info("Setting S3 client endpoint override to {}", endpoint);

        Region region = Region.of(attachmentConfig.getBucketRegion());

        return S3AsyncClient.builder()
                // .endpointOverride(URI.create(endpoint))
                .httpClientBuilder(httpClientBuilder)
                .credentialsProvider(credsProvider)
                .asyncConfiguration(clientConfiguration)
                .region(region)
                .build();
    }

    AwsCredentialsProvider credentialsProvider(BucketAttachmentConfigProperties attachmentConfig) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getSecretAccessKey()));
//        AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getBucketName()));
    }

    private DataBuffer concatBuffers(List<DataBuffer> buffers) {
        return buffers.get(0).factory().join(buffers);
    }

    private Mono<CompletedPart> uploadPart(S3AsyncClient s3AsyncClient, BucketAttachmentConfigProperties props, UploadState uploadState, DataBuffer buffer) {
        final int partNumber = uploadState.partCounter.getAndIncrement();
        CompletableFuture<UploadPartResponse> request = s3AsyncClient.uploadPart(UploadPartRequest.builder()
                .bucket(props.getBucketName())
                .key(uploadState.fileKey)
                .partNumber(partNumber)
                .uploadId(uploadState.uploadId)
                .contentLength((long) buffer.capacity())
                .build(), AsyncRequestBody.fromByteBuffer(buffer.asByteBuffer()));
        return Mono.fromFuture(request)
                .publishOn(Schedulers.parallel())
                .map(uploadPartResult -> CompletedPart.builder()
                        .eTag(uploadPartResult.eTag())
                        .partNumber(partNumber)
                        .build())
                .retryWhen(Retry.max(3))
                .doOnNext(r -> DataBufferUtils.release(buffer));
    }

    private Mono<CompleteMultipartUploadResponse> completeUpload(S3AsyncClient s3AsyncClient, BucketAttachmentConfigProperties props, UploadState state) {
        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
                .parts(state.completedParts.values())
                .build();
        return Mono.fromFuture(s3AsyncClient.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(props.getBucketName())
                .uploadId(state.uploadId)
                .multipartUpload(multipartUpload)
                .key(state.fileKey)
                .build())).publishOn(Schedulers.parallel());

    }

    @RequiredArgsConstructor
    static class UploadState {
        final String fileKey;
        String uploadId;
        AtomicInteger partCounter = new AtomicInteger(1);
        AtomicInteger buffered = new AtomicInteger(0);
        Map<Integer, CompletedPart> completedParts = new ConcurrentHashMap<>();
    }
}
