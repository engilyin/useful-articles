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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.engilyin.usefularticles.dao.entities.articles.Article;
import com.engilyin.usefularticles.services.articles.AddArticleService;
import com.engilyin.usefularticles.services.articles.ListArticleService;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddRequest;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddResponse;
import com.engilyin.usefularticles.ui.mappers.WebArticleMapper;
import com.engilyin.usefularticles.ui.routers.RoutesConfig;
import com.engilyin.usefularticles.ui.validation.ObjectValidator;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@Slf4j
class ArticleHandlerTest {

	private static final String TEST_ARTICLE_NAME = "test-art";

	@Mock
	ListArticleService listArticleService;

	@Mock
	AddArticleService addArticleService;

	@Mock
	WebArticleMapper articleMapper;
	
	@Mock
	ObjectValidator validator;

	WebTestClient client;

	@BeforeEach
	void setUp() throws Exception {
		ArticleHandler articleHandler = new ArticleHandler(listArticleService, addArticleService, articleMapper, validator);
		RouterFunction<?> routes = new RoutesConfig().articlesApis(articleHandler);
		client = WebTestClient.bindToRouterFunction(routes).build();
	}

	@Test
	void testList() {
		when(listArticleService.list()).thenReturn(Flux.empty());

		client.get().uri(uriBuilder -> uriBuilder.path("/api/articles").build()).exchange().expectStatus().isOk();
//				.expectBody()
//				.jsonPath("username")
//				.isEqualTo(TEST_USERNAME);
	}

	@Test
	void testAddArticle() {
		
		var response = ArticleAddResponse.builder().articleName(TEST_ARTICLE_NAME).build();
		when(addArticleService.add(nullable(String.class), nullable(Article.class)))
				.thenReturn(Mono.just(response));

		when(validator.validate(any())).thenAnswer(i -> i.getArgument(0));
		when(articleMapper.fromAddRequest(any())).thenReturn(Article.builder().build());
		
		
		client.post()
				.uri(uriBuilder -> uriBuilder.path("/api/articles").build())
				.bodyValue(ArticleAddRequest.builder().build())
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.jsonPath("articleName")
				.isEqualTo(TEST_ARTICLE_NAME);
	}

}
