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

import static org.mockito.Mockito.when;

import com.engilyin.usefularticles.services.articles.AddArticleService;
import com.engilyin.usefularticles.services.articles.ListArticleService;
import com.engilyin.usefularticles.services.sys.MultipartUploadService;
import com.engilyin.usefularticles.ui.requestloaders.ArticleRequestLoader;
import com.engilyin.usefularticles.ui.routers.RoutesConfig;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class ArticleHandlerTest {

    private static final String TEST_ARTICLE_NAME = "test-art";

    @Mock
    ListArticleService listArticleService;

    @Mock
    AddArticleService addArticleService;

    @Mock
    ArticleRequestLoader articleRequestLoader;

    @Mock
    MultipartUploadService multipartUploadService;

    WebTestClient client;

    @BeforeEach
    void setUp() throws Exception {
        ArticleHandler articleHandler =
                new ArticleHandler(listArticleService, addArticleService, articleRequestLoader, multipartUploadService);
        RouterFunction<?> routes = new RoutesConfig().articlesApis(articleHandler);
        client = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void testList() {
        when(listArticleService.list(0, 2, Optional.empty())).thenReturn(Flux.empty());

        client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/articles")
                        .queryParam("offset", 0)
                        .queryParam("limit", 2)
                        .build())
                .exchange()
                .expectStatus()
                .isOk();
        //				.expectBody()
        //				.jsonPath("username")
        //				.isEqualTo(TEST_USERNAME);
    }
}
