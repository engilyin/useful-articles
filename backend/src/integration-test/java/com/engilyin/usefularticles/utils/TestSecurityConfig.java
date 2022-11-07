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

package com.engilyin.usefularticles.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class TestSecurityConfig {
	
	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
		return http.cors().disable().exceptionHandling().authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		})).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		}))
				.and()
				.csrf()
				.disable()
				.authorizeExchange()
				.anyExchange()
				.permitAll()
				.and()
				.build();
	}

}
