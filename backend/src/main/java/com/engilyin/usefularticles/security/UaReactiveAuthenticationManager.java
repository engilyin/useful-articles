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
import java.util.stream.Collectors;

import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.consts.UaConsts;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UaReactiveAuthenticationManager implements ReactiveAuthenticationManager {

	private final TokenProvider tokenProvider;

	@Override
	@SuppressWarnings("unchecked")
	public Mono<Authentication> authenticate(Authentication authentication) {
		String authToken = authentication.getCredentials().toString();
		String username = usernameByToken(authToken);
		if (username != null && !tokenProvider.isTokenExpired(authToken)) {
			Claims claims = tokenProvider.getAllClaimsFromToken(authToken);
			List<String> roles = claims.get(UaConsts.AUTHORITIES_KEY, List.class);
			List<SimpleGrantedAuthority> authorities = roles.stream().map(role -> new SimpleGrantedAuthority(role))
					.collect(Collectors.toList());
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username,
					authorities);
			SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, authorities));
			return Mono.just(auth);
		} else {
			return Mono.empty();
		}
	}

	private String usernameByToken(String authToken) {
		try {
			return tokenProvider.getUsernameFromToken(authToken);
		} catch (Exception e) {
			return null;
		}
	}
}