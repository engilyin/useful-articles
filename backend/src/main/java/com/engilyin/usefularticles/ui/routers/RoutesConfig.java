package com.engilyin.usefularticles.ui.routers;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.engilyin.usefularticles.ui.handlers.AuthHandler;

import lombok.RequiredArgsConstructor;;

@Configuration
@RequiredArgsConstructor
public class RoutesConfig {

	@Bean
	public RouterFunction<ServerResponse> authApis(AuthHandler authHandler) {
	        return route()
	                .path("/auth", builder ->
	                        builder.POST("/login", contentType(APPLICATION_JSON).and(accept(APPLICATION_JSON)), authHandler::login)
//	                                .GET("/{id}", userHandler::getUser)
//	                                .GET("", userHandler::getUsers)
//	                                .GET("/{id}/posts", userHandler::getPostsByUser)
	                )
	                .build();
	    }

}
