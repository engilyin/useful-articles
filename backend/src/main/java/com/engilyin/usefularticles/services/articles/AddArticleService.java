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
package com.engilyin.usefularticles.services.articles;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.engilyin.usefularticles.dao.entities.articles.Article;
import com.engilyin.usefularticles.dao.repositories.articles.ArticlesRepository;
import com.engilyin.usefularticles.dao.repositories.users.UserIdRepository;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AddArticleService {

	private final ArticlesRepository articlesRepository;

	private final UserIdRepository userIdRepository;

	public Mono<ArticleAddResponse> add(String authorUsername, Article addArticle) {

		log.debug("User: {} adding the articke: {}", authorUsername, addArticle.getArticleName());

		return userIdRepository.findByUsername(authorUsername)
				.switchIfEmpty(Mono.error(() -> new UserNotFoundExeception(authorUsername)))
				.doOnNext(u -> addArticle.setAuthorId(u.getUserId()))
				.flatMap(u -> articlesRepository.save(addArticle)
						.flatMap(r -> Mono.just(ArticleAddResponse.builder().articleName(r.getArticleName()).build())));

	}

}
