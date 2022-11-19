package com.engilyin.usefularticles.dao.repositories.articles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

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
		articlesRepository.findByAuthorId(1)
				.as(StepVerifier::create)
//				.expectNextCount(2)
				.consumeNextWith(article -> {
					assertThat(article.getArticleName(), equalTo("first-article"));
				  } )
				.consumeNextWith(article -> {
					assertThat(article.getArticleName(), equalTo("another-article"));
				} )
				// .expectNext(article)
				.verifyComplete();
	}

}
