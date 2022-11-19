package com.engilyin.usefularticles.dao.services.articles;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.dao.dto.FullArticle;
import com.engilyin.usefularticles.dao.mappers.ArticleMapper;
import com.engilyin.usefularticles.dao.mappers.UserMapper;
import com.engilyin.usefularticles.util.Util;

import io.r2dbc.spi.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
@Component
@Slf4j
public class FullArticlesService {

	private final DatabaseClient client;

	private final ArticleMapper articleMapper;

	private final UserMapper userMapper;

	public Flux<FullArticle> findByAuthorId(long authorId) {
		String query = """
				SELECT * FROM articles a
				                  INNER JOIN users u ON a.author_id = u.user_id
				                  WHERE u.user_id = :authorId
				""";

		return client.sql(query).bind("authorId", authorId).map(this::createObjects).all();

	}

	private FullArticle createObjects(Row row) {

		try {
			Map<String, String> source = row.getMetadata()
					.getColumnMetadatas()
					.stream()
					.collect(Collectors.toMap(
							col -> 
							Util.snakeToCamel(col.getName()), 
							col -> 
							Optional.ofNullable(row.get(col.getName(), String.class)).orElse("")
					));
			return FullArticle.builder()
					.article(articleMapper.fromMap(source))
					.user(userMapper.fromMap(source))
					.build();
		} catch (Throwable ex) {
			log.error("Unable to get data", ex);
		}
		return null;
	}

}
