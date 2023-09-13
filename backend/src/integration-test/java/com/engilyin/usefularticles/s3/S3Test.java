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
import java.util.Properties;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;
import com.engilyin.usefularticles.utils.AppPropertiesLoader;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
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
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Slf4j
public class S3Test {

    Properties appProperties;

    @BeforeEach
    public void loadAppProperties() {
        this.appProperties = new AppPropertiesLoader().properties("application.properties");
    }

    @Test
    public void simpleS3Test() throws IOException, InterruptedException {

//        Region region = Region.US_EAST_1;
//
//        AwsCredentialsProvider creds = StaticCredentialsProvider
//                .create(AwsBasicCredentials.create(appProperties.getProperty("articles.attachment.access-key-id"),
//                        appProperties.getProperty("articles.attachment.secret-access-key")));

        BucketAttachmentConfigProperties props = createBucketAttachmentConfigProperties(appProperties);
        var s3AsyncClient =  createS3AsyncClient(props, credentialsProvider(props ));
                //S3AsyncClient.builder().credentialsProvider(creds).region(region).build();

        var path = Paths.get("D:\\tmp\\Jenny.mp3");

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(props.getBucketName()) //appProperties.getProperty("articles.attachment.bucket-name"))
                .contentLength(path.toFile().length())
                .key("Jenny.mp3")
                .build();

        var fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        var fileFlux = DataBufferUtils
                .readAsynchronousFileChannel(() -> fileChannel, new DefaultDataBufferFactory(), 4096)
                .map(b -> {
                    var r = b.toByteBuffer();
                    DataBufferUtils.release(b);
                    return r;
                });

        CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(objectRequest,
                // AsyncRequestBody.fromFile(Paths.get("D:\\tmp\\Jenny.mp3"))
                AsyncRequestBody.fromPublisher(fileFlux));
//        future.whenComplete((resp, err) -> {
//            try {
//                if (resp != null) {
//                    System.out.println("Object uploaded. Details: " + resp);
//                } else {
//                    // Handle error.
//                    err.printStackTrace();
//                }
//            } finally {
//                // Only close the client when you are completely done with it.
//                s3AsyncClient.close();
//            }
//        });
//
//        future.join();

        Mono.fromFuture(future).doOnError(this::handleError).map(this::checkResult).then(Mono.empty()).subscribe();

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
        log.info("Creating the async client for S3 bucket storage. The backet name: {}, region: {}", attachmentConfig.getBucketName(),
                attachmentConfig.getBucketRegion());
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
}
