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
package com.engilyin.usefularticles.ui.handlers;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.CacheControl;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.engilyin.usefularticles.util.Util;

import lombok.extern.slf4j.Slf4j;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ContentStreamHandler {

    @Value("${articles.attachment.base-folder}")
    private String baseFileFolder;

    public Mono<ServerResponse> getPartialContent(ServerRequest request) {

        throw new RuntimeException("Not yet supported");

    }

    public Mono<ServerResponse> getFullContent(ServerRequest request) {

        String contentname = request.requestPath().value().substring("/stream".length());

        Path contentPath = Paths.get(Util.correctFileSeparator(baseFileFolder + contentname));

        long contentLength = contentPath.toFile().length();

        AsynchronousFileChannel asynchronousFileChannel;
        try {
            asynchronousFileChannel = AsynchronousFileChannel.open(contentPath, StandardOpenOption.READ);

            return ServerResponse.ok()
                    .contentLength(contentLength)
                    .headers(httpHeaders -> httpHeaders.setCacheControl(CacheControl.noCache()))
                    .body(BodyInserters.fromDataBuffers(DataBufferUtils.readAsynchronousFileChannel(
                            () -> asynchronousFileChannel, DefaultDataBufferFactory.sharedInstance, 4096)));

        } catch (IOException e) {
            log.error("Unable to open the requested contnet file: {}", contentPath, e);
            throw Exceptions.propagate(e);
        }
    }
}
