/*
 Copyright 2022-2023 engilyin

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
package com.engilyin.usefularticles.services.storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.ChecksumAlgorithm;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.CompletedMultipartUpload;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadResponse;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;

@Slf4j
@Service
public class S3Uploader {

    private final S3AsyncClient s3AsyncClient;

    private final String bucketName;

    public S3Uploader(BucketAttachmentConfigProperties attachmentConfig) {
        AwsCredentialsProvider creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getBucketName()));

        Region region = Region.of(attachmentConfig.getBucketRegion());
        this.s3AsyncClient = S3AsyncClient.builder().credentialsProvider(creds).region(region).build();
        this.bucketName = attachmentConfig.getBucketName();
    }

    // Inspired by https://www.baeldung.com/java-aws-s3-reactive
    // The minimal multipart upload size is 5MB, even if your total size is 100MB,
    // each individual multipart upload (other than the last one) can't be smaller
    // than 5MB.
    public Mono<Boolean> putObjectMultipart(String key, MediaType mediaType, Flux<DataBuffer> dataBuffer) {

        key = "jen.mp3";
        CompletableFuture<CreateMultipartUploadResponse> uploadRequest = s3AsyncClient
                .createMultipartUpload(CreateMultipartUploadRequest.builder()
                        .key(key)
                        //.overrideConfiguration(cfg -> cfg.signer(Aws4Signer.create()))
                        // .contentType(mediaType.toString())
                        .checksumAlgorithm(ChecksumAlgorithm.SHA256)
                        .bucket(bucketName)
                        .build());
        UploadState uploadState = new UploadState(key);
        return Mono.fromFuture(uploadRequest).publishOn(Schedulers.parallel()).flatMapMany(response -> {
            uploadState.uploadId = response.uploadId();
            return dataBuffer;
        }).doOnError(e -> {
            log.debug("Error on start of multipart upload request {}", e);
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
                .flatMap(buffer -> uploadPart(uploadState, buffer))
                .onBackpressureBuffer()
                .reduce(uploadState, (state, completedPart) -> {
                    state.completedParts.put(completedPart.partNumber(), completedPart);
                    return state;
                })
                .flatMap(this::completeUpload)
                .doOnError(e -> log.error("Failed to proceed with multipart upload.", e))
                .then(Mono.just(true));
    }

    private DataBuffer concatBuffers(List<DataBuffer> buffers) {
        return buffers.get(0).factory().join(buffers);
    }

    private Mono<CompletedPart> uploadPart(UploadState uploadState, DataBuffer buffer) {
        final int partNumber = uploadState.partCounter.getAndIncrement();
        CompletableFuture<UploadPartResponse> request = s3AsyncClient.uploadPart(UploadPartRequest.builder()
                .bucket(bucketName)
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

    private Mono<CompleteMultipartUploadResponse> completeUpload(UploadState state) {
        CompletedMultipartUpload multipartUpload = CompletedMultipartUpload.builder()
                .parts(state.completedParts.values())
                .build();
        return Mono.fromFuture(s3AsyncClient.completeMultipartUpload(CompleteMultipartUploadRequest.builder()
                .bucket(bucketName)
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
