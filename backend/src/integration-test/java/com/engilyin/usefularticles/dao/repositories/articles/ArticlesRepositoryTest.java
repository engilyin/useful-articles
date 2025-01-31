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
package com.engilyin.usefularticles.dao.repositories.articles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DataR2dbcTest
class ArticlesRepositoryTest {

    @Autowired
    ArticlesRepository articlesRepository;

    @BeforeEach
    void setUp() throws Exception {
        Hooks.onOperatorDebug();
    }

    @Test
    void testFindByAuthor() {

        //		var article = Article.builder()
        //				.articleId("first-article")
        //				.author("test")
        //				.description("This is just a test article")
        //				.attachment("/user/te/test/2022/11/my-attach.mp3")
        //				.build();
        articlesRepository
                .findByAuthorId(1)
                .as(StepVerifier::create)
                //				.expectNextCount(2)
                .consumeNextWith(article -> {
                    assertThat(article.getArticleName(), equalTo("first-article"));
                })
                .consumeNextWith(article -> {
                    assertThat(article.getArticleName(), equalTo("another-article"));
                })
                // .expectNext(article)
                .verifyComplete();
    }
}
