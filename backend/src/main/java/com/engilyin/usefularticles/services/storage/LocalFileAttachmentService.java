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

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.configurations.FileAttachmentConfigProperties;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(prefix = "articles.attachment", name = "storage", havingValue = "file")
@Slf4j
public class LocalFileAttachmentService implements AttachmentService {

    private final FileAttachmentConfigProperties attachmentConfigProperties;

    public LocalFileAttachmentService(FileAttachmentConfigProperties attachmentConfigProperties) {
        this.attachmentConfigProperties = attachmentConfigProperties;
        log.info("Using local file storage. Base path: {}", attachmentConfigProperties.getBaseFolder());
    }

    @Override
    public Mono<Boolean> save(String filename, long contentLength, Flux<DataBuffer> contentBuffers) {
        // handle file upload
        Path filePath = Paths.get(attachmentConfigProperties.getBaseFolder() + filename);
        log.debug("Uploading file at: {}", filePath);

        AsynchronousFileChannel asynchronousFileChannel = createChannel(filePath);

        return DataBufferUtils.write(contentBuffers.map(p -> {
            log.debug("Next part: {}", p);
            return p;
            
        }), asynchronousFileChannel)
                .doOnNext(DataBufferUtils.releaseConsumer())
                .doAfterTerminate(() -> closeUploadedFile(asynchronousFileChannel, filePath))
                .reduce(true, (n, r) -> true);

    }

    private AsynchronousFileChannel createChannel(Path filePath) {
        try {
            Files.createDirectories(filePath.getParent());
            return AsynchronousFileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        } catch (IOException e) {
            log.error("Unable to create the attached file", e);
            throw new RuntimeException();
        }
    }

    private void closeUploadedFile(AsynchronousFileChannel asynchronousFileChannel, Path filePath) {
        try {
            asynchronousFileChannel.close();
        } catch (IOException ignored) {
            throw new RuntimeException("Unable to close uploaded file: " + filePath);
        }
    }

}
