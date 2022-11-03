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