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
package com.engilyin.usefularticles.services.sys;

import org.reactivestreams.Publisher;
import org.springframework.http.codec.multipart.FilePartEvent;
import org.springframework.http.codec.multipart.FormPartEvent;
import org.springframework.http.codec.multipart.PartEvent;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.services.storage.AttachmentService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Signal;

@Service
@RequiredArgsConstructor
@Slf4j
public class MultipartUploadService {

    private final AttachmentService attachmentService;

    public <T extends MultipartDataAccumulator<?>> Mono<T> loadData(Flux<PartEvent> rawPartEvents, T accumulator) {
        return rawPartEvents.windowUntil(PartEvent::isLast)
                .concatMap(p -> p.switchOnFirst((signal, partEvents) -> handlePart(signal, partEvents, accumulator)))
                .then(Mono.just(accumulator));
    }

    private Publisher<PartEvent> handlePart(Signal<? extends PartEvent> signal,
            Flux<PartEvent> partEvents,
            MultipartDataAccumulator<?> accumulator) {
        if (signal.hasValue()) {
            PartEvent event = signal.get();
            if (event instanceof FormPartEvent formEvent) {
                accumulator.pushField(formEvent.name(), formEvent.value());
                return Mono.empty();
            } else if (event instanceof FilePartEvent fileEvent) {
                log.debug("!!! FilePartEvent!");
                String filename = accumulator.generateFilename(fileEvent.filename());
                long fileSize = accumulator.attachmentSize();
//                return attachmentService.save(filename, fileSize, partEvents.map(PartEvent::content))
//                        .then(Mono.empty());
                attachmentService.save(filename, fileSize, partEvents.map(PartEvent::content)).subscribe(v -> {
                    log.info("Loading done!!!!!!");
                });
                return Mono.empty();
            } else {
                return Mono.error(new RuntimeException("Unexpected event: " + event));
            }
        } else {
            return partEvents; // either complete or error signal
        }
    }
}
