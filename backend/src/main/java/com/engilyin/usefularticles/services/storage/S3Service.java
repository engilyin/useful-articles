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
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.reactivestreams.Subscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;

import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.core.async.ResponsePublisher;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Service
@ConditionalOnProperty(prefix = "articles.attachment", name = "storage", havingValue = "s3")
public class S3Service {
    private final S3AsyncClient s3Client;

    public S3Service(BucketAttachmentConfigProperties attachmentConfig) {
        AwsCredentialsProvider creds = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getBucketName()));

        Region region = Region.of(attachmentConfig.getBucketRegion());
        this.s3Client = S3AsyncClient.builder().credentialsProvider(creds).region(region).build();
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> receiveObject(String backetName, String objectKey) {
        return s3Client.getObject(builder -> builder.bucket(backetName).key(objectKey),
                AsyncResponseTransformer.toPublisher());
    }

    public CompletableFuture<ResponsePublisher<GetObjectResponse>> receiveObjectPart(String backetName,
            String objectKey,
            long objectSize,
            long from,
            long fragmentSize) {
        long readTo = Math.min(from + fragmentSize, objectSize) - 1;
        String range = "bytes=" + from + "-" + readTo;
        return s3Client.getObject(builder -> builder.bucket(backetName).key(objectKey).range(range),
                AsyncResponseTransformer.toPublisher());
    }
    
    public void t(String bucketName, String objectKey) {
        s3Client.putObject(builder -> builder.bucket(bucketName).key(objectKey), new AsyncRequestBody() {
            
            @Override
            public void subscribe(Subscriber<? super ByteBuffer> s) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public Optional<Long> contentLength() {
                // TODO Auto-generated method stub
                return null;
            }
        };
    }

    public Mono<Long> objectSize(String bucketName, String objectKey) {
        CompletableFuture<Long> result = s3Client.headObject(builder -> builder.bucket(bucketName).key(objectKey))
                .thenApply(headObjectResponse -> headObjectResponse.contentLength());
        return Mono.fromFuture(result);
    }
}
