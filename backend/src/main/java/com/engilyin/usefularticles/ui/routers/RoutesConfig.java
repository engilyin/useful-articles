package com.engilyin.usefularticles.ui.routers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.engilyin.usefularticles.ui.handlers.AuthHandler;

import lombok.RequiredArgsConstructor;;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {

	@Bean
	public RouterFunction<ServerResponse> authApis(AuthHandler authHandler) {
		return route().path("/auth", builder -> builder.POST("/signin",
				contentType(APPLICATION_JSON).and(accept(APPLICATION_JSON)), authHandler::signin)
//	                                .GET("/{id}", userHandler::getUser)
//	                                .GET("", userHandler::getUsers)
//	                                .GET("/{id}/posts", userHandler::getPostsByUser)
		).build();
	}

	@Bean
	RouterFunction<ServerResponse> staticResourceRouter() {
		return RouterFunctions.resources("/**", new ClassPathResource("static/"));
	}

	@Bean
	public RouterFunction<ServerResponse> indexRouter(@Value("classpath:/static/index.html") final Resource indexHtml) {
		return route(GET("/"), request -> ok().contentType(MediaType.TEXT_HTML).bodyValue(indexHtml));
	}
}
