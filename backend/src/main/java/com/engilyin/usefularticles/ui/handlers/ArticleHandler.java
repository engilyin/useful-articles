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
package com.engilyin.usefularticles.ui.handlers;

import com.engilyin.usefularticles.consts.Consts;
import com.engilyin.usefularticles.data.articles.ArticleFeedItem;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.services.articles.AddArticleService;
import com.engilyin.usefularticles.services.articles.ListArticleService;
import com.engilyin.usefularticles.services.sys.MultipartUploadService;
import com.engilyin.usefularticles.ui.requestloaders.ArticleAddResponseCreator;
import com.engilyin.usefularticles.ui.requestloaders.ArticleRequestLoader;
import java.security.Principal;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.codec.multipart.PartEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ArticleHandler {

    private final ListArticleService listArticleService;

    private final AddArticleService addArticleService;

    private final ArticleRequestLoader articleRequestLoader;

    private final MultipartUploadService multipartUploadService;

    public Mono<ServerResponse> list(ServerRequest request) {
        return ServerResponse.ok()
                .body(
                        listArticleService.list(
                                Long.parseLong(
                                        request.queryParam(Consts.OFFSET_PARAM).orElse("0")),
                                Long.parseLong(
                                        request.queryParam(Consts.LIMIT_PARAM).orElse(Consts.DEFAULT_PAGE_SIZE)),
                                request.queryParam(Consts.FIRST_PARAM)),
                        ArticleFeedItem.class);
    }

    public Mono<ServerResponse> add(ServerRequest request) {
        return request.principal()
                .map(Principal::getName)
                .switchIfEmpty(Mono.error(new UserNotFoundExeception("You need to authenticate")))
                .map(username -> new ArticleAddResponseCreator(articleRequestLoader, username))
                .flatMap(resultCreator ->
                        multipartUploadService.loadData(request.bodyToFlux(PartEvent.class), resultCreator))
                .flatMap(resultCreator ->
                        ServerResponse.ok().body(createAddResponseBody(resultCreator), ArticleFeedItem.class));
    }

    private Mono<ArticleFeedItem> createAddResponseBody(ArticleAddResponseCreator creator) {
        return addArticleService.add(creator.username(), articleRequestLoader.mapToValidArticle(creator.build()));
    }
}
