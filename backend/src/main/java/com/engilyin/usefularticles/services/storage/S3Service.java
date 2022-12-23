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
package com.engilyin.usefularticles.services.storage;

import java.nio.ByteBuffer;
import java.util.concurrent.CompletableFuture;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@ConditionalOnProperty(prefix = "articles.attachment", name = "storage", havingValue = "s3")
@Slf4j
public class S3Service {
    private final S3AsyncClient s3AsyncClient;

    private final ObjectMapper objectMapper;

    public S3Service(BucketAttachmentConfigProperties attachmentConfig, ObjectMapper objectMapper) {
        AwsCredentialsProvider creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getBucketName()));

        Region region = Region.of(attachmentConfig.getBucketRegion());
        this.s3AsyncClient = S3AsyncClient.builder().credentialsProvider(creds).region(region).build();
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> receiveObject(String backetName, String objectKey) {
        return s3AsyncClient.getObject(builder -> builder.bucket(backetName).key(objectKey),
                AsyncResponseTransformer.toPublisher());
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> receiveObjectPart(String backetName,
            String objectKey,
            long objectSize,
            long from,
            long fragmentSize) {
        long readTo = Math.min(from + fragmentSize, objectSize) - 1;
        String range = "bytes=" + from + "-" + readTo;
        return s3AsyncClient.getObject(builder -> builder.bucket(backetName).key(objectKey).range(range),
                AsyncResponseTransformer.toPublisher());
    }

    public Mono<Long> objectSize(String bucketName, String objectKey) {
        CompletableFuture<Long> result = s3AsyncClient.headObject(builder -> builder.bucket(bucketName).key(objectKey))
                .thenApply(headObjectResponse -> headObjectResponse.contentLength());
        return Mono.fromFuture(result);
    }

    public Mono<Boolean> uploadBuffers(String bucketName,
            String objectKey,
            long contentLength,
            Flux<ByteBuffer> buffers) {
        CompletableFuture<PutObjectResponse> future = s3AsyncClient.putObject(PutObjectRequest.builder()
                .bucket(bucketName)
                .contentLength(106268L)//contentLength)
                .key("test")//objectKey)
                .contentType(MediaType.APPLICATION_OCTET_STREAM.toString())
                .build(), AsyncRequestBody.fromPublisher(buffers));

        return Mono.fromFuture(future).doOnError(this::handleError).map(this::checkResult);
    }

    private boolean checkResult(PutObjectResponse putObjectResponse) {
        String result;
        try {
            result = objectMapper.writeValueAsString(putObjectResponse);
            log.debug("Got response from S3: {}", result);
        } catch (JsonProcessingException e) {
            log.error("Unable to read S3 response", e);
        }

        return putObjectResponse.sdkHttpResponse().isSuccessful();
    }

    private void handleError(Throwable throwable1) {

        log.error("Problem to put on S3", throwable1);
    }
}
