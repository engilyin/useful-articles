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

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.BucketAttachmentConfigProperties;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

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

        Optional<Sinks.Many<ByteBuffer>> emptySink = Optional.empty();

        return contentBuffers.zipWith(initS3Sink(filename, contentLength)).doOnNext(b -> {
            log.debug("Sending buffer: {}", b);
            b.getT2().tryEmitNext(b.getT1().toByteBuffer());
        })
                .doOnNext(b -> DataBufferUtils.releaseConsumer())
                .reduce(emptySink, (n, r) -> Optional.of(r.getT2()))
                .map(sink -> {
                    if (sink.isPresent()) {
                        sink.get().tryEmitComplete();
                    }
                    return true;
                });

    }

    private Mono<Sinks.Many<ByteBuffer>> initS3Sink(String filename, long contentLength) {
        Sinks.Many<ByteBuffer> sink = Sinks.many().unicast().onBackpressureBuffer();
        s3Service
                .uploadBuffers(attachmentConfigProperties.getBucketName(), filename, contentLength, sink.asFlux().log());
                //.subscribe(r -> log.debug("The data transmitted onto the S3 bucket as: {}", filename));
        return Mono.just(sink);
    }

}
