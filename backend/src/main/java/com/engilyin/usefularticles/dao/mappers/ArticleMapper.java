package com.engilyin.usefularticles.dao.mappers;

import java.util.Map;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.dao.entities.articles.Article;

@Mapper
@Component
public interface ArticleMapper extends BaseMapper {

	Article fromMap(Map<String, String> source);
	
}
