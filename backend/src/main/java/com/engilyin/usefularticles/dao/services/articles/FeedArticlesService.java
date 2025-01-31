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
package com.engilyin.usefularticles.dao.services.articles;

import com.engilyin.usefularticles.dao.mappers.ArticleFeedMapper;
import com.engilyin.usefularticles.dao.services.sys.Db;
import com.engilyin.usefularticles.data.articles.ArticleFeedItem;
import io.r2dbc.spi.Row;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class FeedArticlesService {

    private static final String ARTICLE_FEED_SQL =
            """
            SELECT a.*,
                   u.username as author_username,
                   u.fullname as author_fullname, (
                     	SELECT COUNT(*)
                     	FROM comments c
                     	WHERE c.article_id = a.article_id
                     ) as comment_count
            FROM articles a
            INNER JOIN users u ON a.author_id = u.user_id
                  """;

    private static final String ARTICLE_FEED_PAGE_SQL =
            """
            ORDER BY a.article_id DESC
            OFFSET %d LIMIT %d
            """;

    private static final String ARTICLE_FEED_FIRST_ITEM_ID_SQL = """
            WHERE a.article_id <=
            """;

    private static final String ARTICLE_FEED_BY_ID_SQL =
            """
            WHERE a.article_id = :articleId
            """;

    private final DatabaseClient client;

    private final ArticleFeedMapper articleFeedMapper;

    public Mono<ArticleFeedItem> articleById(long articleId) {

        return client.sql(ARTICLE_FEED_SQL + " " + ARTICLE_FEED_BY_ID_SQL)
                .bind("articleId", articleId)
                .map((r, m) -> this.createItem(r))
                .one();
    }

    public Flux<ArticleFeedItem> articleFeed(long offset, long limit, Optional<String> firstItemId) {

        return client.sql(String.format(
                        ARTICLE_FEED_SQL
                                + firstItemId
                                        .filter(i -> offset > 0)
                                        .map(s -> Long.parseLong(s))
                                        .map(itemId -> " " + ARTICLE_FEED_FIRST_ITEM_ID_SQL + itemId)
                                        .orElse("")
                                + " " + ARTICLE_FEED_PAGE_SQL,
                        offset,
                        limit))
                // .bind("authorId", authorId)
                .map((r, m) -> this.createItem(r))
                .all();
    }

    private ArticleFeedItem createItem(Row row) {
        return articleFeedMapper.fromMap(Db.rowToMap(row));
    }
}
