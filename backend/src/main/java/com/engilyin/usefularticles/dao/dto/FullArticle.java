package com.engilyin.usefularticles.dao.dto;

import com.engilyin.usefularticles.dao.entities.articles.Article;
import com.engilyin.usefularticles.dao.entities.users.User;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class FullArticle {

	private final Article article;

	private final User user;
}
