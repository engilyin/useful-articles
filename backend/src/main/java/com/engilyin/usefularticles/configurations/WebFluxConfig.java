package com.engilyin.usefularticles.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.accept.RequestedContentTypeResolverBuilder;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {

	public void configureContentTypeResolver(RequestedContentTypeResolverBuilder builder) {
		builder.fixedResolver(MediaType.APPLICATION_JSON);
	}
}