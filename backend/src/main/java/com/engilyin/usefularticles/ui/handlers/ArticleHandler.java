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

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.engilyin.usefularticles.consts.Consts;
import com.engilyin.usefularticles.data.articles.ArticleFeedItem;
import com.engilyin.usefularticles.services.articles.AddArticleService;
import com.engilyin.usefularticles.services.articles.ListArticleService;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddRequest;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddResponse;
import com.engilyin.usefularticles.ui.mappers.WebArticleMapper;
import com.engilyin.usefularticles.ui.validation.ObjectValidator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ArticleHandler {

    private final ListArticleService listArticleService;

    private final AddArticleService addArticleService;

    private final WebArticleMapper articleMapper;

    private final ObjectValidator validator;

    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok()
                .body(listArticleService.list(Long.parseLong(request.queryParam(Consts.OFFSET_PARAM).orElse("0")),
                        Long.parseLong(request.queryParam(Consts.LIMIT_PARAM).orElse(Consts.DEFAULT_PAGE_SIZE)), 
                        request.queryParam(Consts.FIRST_PARAM)),
                        ArticleFeedItem.class);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        return Mono
                .zip(request.principal().map(Principal::getName).defaultIfEmpty(""),
                        request.bodyToMono(ArticleAddRequest.class)
                                .doOnNext(body -> validator.validate(body))
                                .map(articleMapper::fromAddRequest))
                .flatMap(tuple -> ServerResponse.ok()
                        .body(addArticleService.add(tuple.getT1(), tuple.getT2()), ArticleAddResponse.class));
    }

}
