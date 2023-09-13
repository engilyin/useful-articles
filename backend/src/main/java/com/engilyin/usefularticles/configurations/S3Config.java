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
package com.engilyin.usefularticles.configurations;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.client.config.ClientAsyncConfiguration;
import software.amazon.awssdk.core.client.config.SdkAdvancedAsyncClientOption;
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient;
import software.amazon.awssdk.http.nio.netty.SdkEventLoopGroup;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;

@Configuration
@ConditionalOnProperty(prefix = "articles.attachment", name = "storage", havingValue = "s3")
@Slf4j
public class S3Config {

    @Bean
    public S3AsyncClient s3AsyncClient(BucketAttachmentConfigProperties attachmentConfig,
            AwsCredentialsProvider credsProvider) {
        log.info("Creating the async client for S3 bucket storage. The backet name: {}, region: {}", attachmentConfig.getBucketName(),
                attachmentConfig.getBucketRegion());
        System.setProperty("ENABLE_S3_SIGV4_SYSTEM_PROPERTY", "true");
        
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

    @Bean
    public AwsCredentialsProvider credentialsProvider(BucketAttachmentConfigProperties attachmentConfig) {
        return StaticCredentialsProvider.create(
                AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getSecretAccessKey()));
                        
                        
                        //attachmentConfig.getAccessKeyId(), attachmentConfig.getSecretAccessKey()));
//        AwsBasicCredentials.create(attachmentConfig.getAccessKeyId(), attachmentConfig.getBucketName()));
    }
//    private String createEndpointString(S3ClientProperties props) {
//      StringBuilder endpoint = new StringBuilder();
//      endpoint.append(props.isTls() ? "https://" : "http://");
//      endpoint.append(props.getHost());
//      if (props.getPort() != null) {
//        endpoint.append(":");
//        endpoint.append(props.getPort());
//      }
//      return endpoint.toString();
//    }

}
