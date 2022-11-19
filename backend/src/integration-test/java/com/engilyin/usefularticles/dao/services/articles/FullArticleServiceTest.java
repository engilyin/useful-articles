package com.engilyin.usefularticles.dao.services.articles;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.engilyin.usefularticles.dao.mappers.ArticleMapperImpl;
import com.engilyin.usefularticles.dao.mappers.UserMapperImpl;
import com.engilyin.usefularticles.utils.DbTestConfig;

import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@ContextConfiguration(classes = { UserMapperImpl.class, ArticleMapperImpl.class, FullArticlesService.class, DbTestConfig.class })
@DataR2dbcTest
class FullArticleServiceTest {
	
	@Autowired
	FullArticlesService fullArticleService;

	@BeforeEach
	void setUp() throws Exception {
		Hooks.onOperatorDebug();
	}

	@Test
	void testFindByAuthorId() {
		fullArticleService.findByAuthorId(1)
		.as(StepVerifier::create)
//		.expectNextCount(2)
		.consumeNextWith(article -> {
			assertThat(article.getArticle().getArticleName(), equalTo("another-article"));
			assertThat(article.getUser().getUsername(), equalTo("test"));
		  } )
		.consumeNextWith(article -> {
			assertThat(article.getArticle().getArticleName(), equalTo("first-article"));
			assertThat(article.getUser().getUsername(), equalTo("test"));
		} )
		.verifyComplete();
	}

}
