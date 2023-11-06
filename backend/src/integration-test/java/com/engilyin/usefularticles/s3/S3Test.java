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

import com.engilyin.usefularticles.utils.AppPropertiesLoader;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.async.AsyncRequestBody;
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

        Region region = Region.US_EAST_1;

        AwsCredentialsProvider creds = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(appProperties.getProperty("articles.attachment.access-key-id"),
                        appProperties.getProperty("articles.attachment.secret-access-key")));

        var s3AsyncClient = S3AsyncClient.builder().credentialsProvider(creds).region(region).build();

        var path = Paths.get("D:\\tmp\\Jenny.mp3");

        PutObjectRequest objectRequest = PutObjectRequest.builder()
                .bucket(appProperties.getProperty("articles.attachment.bucket-name"))
                .contentLength(path.toFile().length())
                .key("Jenny.mp3")
                .build();

        var fileChannel = AsynchronousFileChannel.open(path, StandardOpenOption.READ);

        var fileFlux = Flux.from(DataBufferUtils
                .readAsynchronousFileChannel(() -> fileChannel, new DefaultDataBufferFactory(), 4096)
                .map(b -> {
                    var r = b.toByteBuffer();
                    DataBufferUtils.release(b);
                    return r;
                }));

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

    private void handleError(Throwable throwable1) {

        log.error("Problem to put on S3", throwable1);
    }

    private boolean checkResult(PutObjectResponse putObjectResponse) {
        log.debug("Got response from S3: {}", putObjectResponse);

        return putObjectResponse.sdkHttpResponse().isSuccessful();
    }
}
