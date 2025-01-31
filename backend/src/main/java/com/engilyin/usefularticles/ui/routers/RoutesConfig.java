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
package com.engilyin.usefularticles.ui.routers;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import com.engilyin.usefularticles.ui.handlers.ArticleHandler;
import com.engilyin.usefularticles.ui.handlers.AuthHandler;
import com.engilyin.usefularticles.ui.handlers.ContentStreamHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {

    @Bean
    public RouterFunction<ServerResponse> authApis(AuthHandler authHandler) {
        return route().path(
                        "/auth", builder -> builder.POST("/signin", authHandler::signin)
                        //	                                .GET("/{id}", userHandler::getUser)
                        //	                                .GET("", userHandler::getUsers)
                        //	                                .GET("/{id}/posts", userHandler::getPostsByUser)
                        )
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> articlesApis(ArticleHandler articleHandler) {
        return route().path("/api", builder -> builder.GET("/articles", articleHandler::list)
                        .POST("/articles", contentType(MediaType.MULTIPART_FORM_DATA), articleHandler::add))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> streamApis(ContentStreamHandler streamHandler) {
        return route().path("/stream/**", contentBuilder -> contentBuilder
                        .GET(
                                "",
                                param("partial").and(contentType(MediaType.APPLICATION_OCTET_STREAM)),
                                streamHandler::getPartialContent)
                        .GET("", contentType(MediaType.APPLICATION_OCTET_STREAM), streamHandler::getFullContent))
                .build();
    }

    @Bean
    RouterFunction<ServerResponse> staticResourceRouter() {
        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
    }

    @Bean
    public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/static/index.html") final Resource indexHtml) {
        return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml));
    }

    private static RequestPredicate param(String parameter) {
        return RequestPredicates.all()
                .and(request -> request.queryParam(parameter).isPresent());
    }
}
