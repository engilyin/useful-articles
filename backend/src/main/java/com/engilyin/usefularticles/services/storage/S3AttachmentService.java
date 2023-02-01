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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(prefix = "articles.attachment", name = "storage", havingValue = "s3")
@Slf4j
public class S3AttachmentService implements AttachmentService {

    private final BucketAttachmentConfigProperties attachmentConfigProperties;

    private final S3Service s3Service;

    public S3AttachmentService(BucketAttachmentConfigProperties attachmentConfigProperties, S3Service s3Service) {
        this.attachmentConfigProperties = attachmentConfigProperties;
        this.s3Service = s3Service;
        log.info("Using S3 bucket storage. The backet name: {}, region: {}", attachmentConfigProperties.getBucketName(),
                attachmentConfigProperties.getBucketRegion());

    }

    @Override
    public Mono<Boolean> save(String filename, long contentLength, Flux<DataBuffer> contentBuffers) {

        Flux<ByteBuffer> buffers = Flux.from(contentBuffers.map(db -> {
            log.debug("Next part: {}", db);
            var buf = db.toByteBuffer();
            DataBufferUtils.release(db);
           // DataBufferUtils.releaseConsumer();
            return buf;
        }));

        return s3Service.uploadBuffers(attachmentConfigProperties.getBucketName(), filename, contentLength, buffers);
    }

}
