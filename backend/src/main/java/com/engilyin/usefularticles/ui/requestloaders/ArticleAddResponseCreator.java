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

import java.io.File;
import java.time.LocalDateTime;
import java.util.Optional;

import com.engilyin.usefularticles.services.sys.MultipartDataAccumulator;
import com.engilyin.usefularticles.ui.data.articles.ArticleAddRequest;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ArticleAddResponseCreator implements MultipartDataAccumulator<ArticleAddRequest> {

    private final ArticleRequestLoader articleRequestLoader;

    private final String username;

    private String requestJson;

    private String attachmentName;

    @Override
    public void pushField(String name, String value) {
        this.requestJson = value;
    }

    @Override
    public String generateFilename(String originalName) {
        String separator = File.separator;
        var now = LocalDateTime.now();
        this.attachmentName = separator + now.getYear() + separator + now.getMonth() + separator + now.getDayOfMonth()
                + separator + originalName;
        return articleRequestLoader.baseFileFolder() + attachmentName;

    }

    @Override
    public ArticleAddRequest build() {
        var result = articleRequestLoader.fromJson(requestJson);
        result.setArticleAttachment(Optional.ofNullable(attachmentName).orElse(""));
        return result;
    }

    public String username() {
        return this.username;
    }

}
