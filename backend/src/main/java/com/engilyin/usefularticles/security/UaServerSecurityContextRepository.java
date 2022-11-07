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

package com.engilyin.usefularticles.security;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class UaServerSecurityContextRepository implements ServerSecurityContextRepository {

	private static final String TOKEN_PREFIX = "Bearer ";

	private final UaReactiveAuthenticationManager authenticationManager;

	@Override
	public Mono<Void> save(ServerWebExchange swe, SecurityContext sc) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public Mono<SecurityContext> load(ServerWebExchange swe) {
		ServerHttpRequest request = swe.getRequest();
		String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		Authentication auth = findAuthentication(authHeader);
		if (auth != null) {
			return this.authenticationManager.authenticate(auth)
					.map((authentication) -> new SecurityContextImpl(authentication));
		} else {
			return Mono.empty();
		}
	}

	private Authentication findAuthentication(String authHeader) {
		if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
			String authToken = authHeader.replace(TOKEN_PREFIX, "");
			return new UsernamePasswordAuthenticationToken(authToken, authToken);
		} else {
			log.warn("couldn't find bearer string, will ignore the header.");
		}
		return null;
	}

}