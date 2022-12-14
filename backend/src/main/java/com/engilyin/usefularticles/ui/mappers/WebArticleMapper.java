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
package com.engilyin.usefularticles.ui.mappers;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.configurations.MappingConfig;
import com.engilyin.usefularticles.dao.entities.articles.Article;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddRequest;

@Mapper(config = MappingConfig.class)
@Component
public interface WebArticleMapper {

    Article fromAddRequest(ArticleAddRequest request);

}
