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
package com.engilyin.usefularticles.ui.requestloaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.dao.entities.articles.Article;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddRequest;
import com.engilyin.usefularticles.ui.mappers.WebArticleMapper;
import com.engilyin.usefularticles.ui.validation.ObjectValidator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArticleRequestLoader {

    private final WebArticleMapper articleMapper;

    private final ObjectValidator validator;
    
    private final ObjectMapper mapper;

    @Value("${articles.attachment.base-folder}")
    private String baseFileFolder;

    public Article mapToValidArticle(ArticleAddRequest articleAddRequest) {
        validator.validate(articleAddRequest);
        return articleMapper.fromAddRequest(articleAddRequest);
    }
    
    public String baseFileFolder() {
        return baseFileFolder;
    }
    
    public ArticleAddRequest fromJson(String json) {
        try {
            return mapper.readValue(json, ArticleAddRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
