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
package com.engilyin.usefularticles.services.articles;

import com.engilyin.usefularticles.dao.services.articles.FeedArticlesService;
import com.engilyin.usefularticles.data.articles.ArticleFeedItem;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ListArticleService {

    private final FeedArticlesService feedArticlesService;

    public Flux<ArticleFeedItem> list(long offset, long limit, Optional<String> firstItemId) {
        return feedArticlesService.articleFeed(offset, limit, firstItemId);
    }
}
