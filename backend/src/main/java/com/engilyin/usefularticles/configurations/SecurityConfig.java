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

package com.engilyin.usefularticles.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;

import com.engilyin.usefularticles.security.BearerAuthenticationFilter;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

	private final String[] protectedPatterns = { "/api/**" };

	@Bean
	SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
			BearerAuthenticationFilter authenticationFilter,
			PasswordEncoder passwordEncoder) {

		return http.cors().disable().exceptionHandling().authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
		})).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
			swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
		}))
				.and()
				.csrf()
				.disable()
				.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
				.httpBasic()
				.disable()
				.formLogin()
				.disable()
				.logout()
				.disable()
				.authorizeExchange()
				.pathMatchers(protectedPatterns)
				.authenticated()
				// .hasRole(Consts.GENERIC_ROLE)
				// .pathMatchers("/api/profile/{user}/**").access(this::currentUserMatchesPath)
				.anyExchange()
				.permitAll()
				.and()
				.addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)

				.build();
	}

//	private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication,
//			AuthorizationContext context) {
//
//		return authentication.map(a -> context.getVariables().get("user").equals(a.getName()))
//				.map(AuthorizationDecision::new);
//
//	}

//	/**
//	 * That will handle requests containing a Bearer token inside the HTTP
//	 * Authorization header. Set a dummy authentication manager to this filter, it's
//	 * not needed because the converter handles this.
//	 *
//	 * @return bearerAuthenticationFilter that will authorize requests containing a
//	 *         JWT
//	 */
//	private AuthenticationWebFilter bearerAuthenticationFilter(ReactiveAuthenticationManager authenticationManager,
//			BearerAuthenticationConverter bearerAuthenticationConverter) {
//
//		var bearerAuthenticationFilter = new AuthenticationWebFilter(authenticationManager);
//
//		bearerAuthenticationFilter.setServerAuthenticationConverter(bearerAuthenticationConverter);
//		bearerAuthenticationFilter
//				.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers(protectedPatterns));
//
//		return bearerAuthenticationFilter;
//	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
