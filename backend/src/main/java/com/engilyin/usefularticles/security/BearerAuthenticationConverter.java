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

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.engilyin.usefularticles.consts.Consts;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BearerAuthenticationConverter implements ServerAuthenticationConverter {
	
	private static final String BEARER = "Bearer ";
	
    private static final Predicate<String> checkBearerLength = authValue -> authValue.length() > BEARER.length();
    private static final Function<String,Mono<String>> extractBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(BEARER.length()));
    
    private final TokenProvider tokenProvider;

	@Override
	public Mono<Authentication> convert(ServerWebExchange exchange) {

		return Mono.justOrEmpty(exchange)
				.flatMap(this::findAuthHeader)
				.filter(checkBearerLength)
				.flatMap(extractBearerValue)
				.flatMap(token -> Mono.just(tokenProvider.getAllClaimsFromToken(token)))
				.flatMap(this::createAuthentication)
				.log();
	}

	private Mono<String> findAuthHeader(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION));
	}
	
	private Mono<Authentication> createAuthentication(Claims claims) {

		@SuppressWarnings("unchecked")
		List<String> roles = claims.get(Consts.AUTHORITIES_KEY, List.class);
		
		List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
		
		User principal = new User(claims.getSubject(), "", authorities);

        return Mono.just(new UsernamePasswordAuthenticationToken(principal, "", authorities));
	}
}
