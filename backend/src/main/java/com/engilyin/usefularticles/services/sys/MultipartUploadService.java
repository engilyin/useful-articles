/*
 Copyright 2022-2025 engilyin

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
package com.engilyin.usefularticles.services.sys;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePartEvent;
import org.springframework.http.codec.multipart.FormPartEvent;
import org.springframework.http.codec.multipart.PartEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Service
@Slf4j
public class MultipartUploadService {

    public <T extends MultipartDataAccumulator<?>> Mono<T> loadData(Flux<PartEvent> rawPartEvents, T accumulator) {
        return rawPartEvents
                .windowUntil(PartEvent::isLast)
                .concatMap(p -> p.switchOnFirst((signal, partEvents) -> handlePart(signal, partEvents, accumulator)))
                .then(Mono.just(accumulator));
    }

    private Publisher<PartEvent> handlePart(
            Signal<? extends PartEvent> signal, Flux<PartEvent> partEvents, MultipartDataAccumulator<?> accumulator) {
        if (signal.hasValue()) {
            PartEvent event = signal.get();
            if (event instanceof FormPartEvent formEvent) {
                accumulator.pushField(formEvent.name(), formEvent.value());
                return Mono.empty();
            } else if (event instanceof FilePartEvent fileEvent) {
                String filename = fileEvent.filename();
                Flux<DataBuffer> contents = partEvents.map(PartEvent::content);

                // handle file upload
                Path filePath = Paths.get(accumulator.generateFilename(filename));
                log.debug("Uploading file at: {}", filePath);
                AsynchronousFileChannel asynchronousFileChannel;
                try {
                    Files.createDirectories(filePath.getParent());
                    asynchronousFileChannel =
                            AsynchronousFileChannel.open(filePath, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
                } catch (IOException e) {
                    log.error("Unable to create the attached file", e);
                    throw new RuntimeException();
                }
                return DataBufferUtils.write(contents, asynchronousFileChannel)
                        .doOnNext(DataBufferUtils.releaseConsumer())
                        .doAfterTerminate(() -> closeUploadedFile(asynchronousFileChannel, filePath))
                        .then(Mono.empty());

            } else {
                return Mono.error(new RuntimeException("Unexpected event: " + event));
            }
        } else {
            return partEvents; // either complete or error signal
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
