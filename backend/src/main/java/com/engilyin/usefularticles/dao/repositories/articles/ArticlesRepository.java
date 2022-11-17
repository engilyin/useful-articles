package com.engilyin.usefularticles.dao.repositories.articles;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.engilyin.usefularticles.dao.entities.articles.Article;

import reactor.core.publisher.Flux;

public interface ArticlesRepository  extends ReactiveCrudRepository<Article, String> {

    Flux<Article> findByAuthorId(long authorId);

}
