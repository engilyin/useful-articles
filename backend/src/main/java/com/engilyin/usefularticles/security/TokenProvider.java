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

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.consts.Consts;
import com.engilyin.usefularticles.exceptions.WrongJwtException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {

	private final JwtProperties jwtProperties;

	public String generateToken(String subject, String... roles) {

		Claims claims = Jwts.claims().setSubject(subject);
		claims.put(Consts.AUTHORITIES_KEY, roles);

		return Jwts.builder()
				.setClaims(claims)
				.setIssuer("http://engilyin.com")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(Instant.now().plus(jwtProperties.getSessionTime(), ChronoUnit.SECONDS)))
				.signWith(jwtProperties.getKey())
				.compact();
	}

	public Authentication getAuthentication(String token) throws WrongJwtException {

		try {
			Claims claims = getAllClaimsFromToken(token);

			log.debug("The request user: {}", claims.getSubject());
			log.debug("The expiration date: {}", claims.getExpiration());

			@SuppressWarnings("unchecked")
			List<SimpleGrantedAuthority> authorities = buildAuthorityList(
					claims.get(Consts.AUTHORITIES_KEY, List.class));

			User principal = new User(claims.getSubject(), "", authorities);

			return new UsernamePasswordAuthenticationToken(principal, token, authorities);
		} catch (JwtException | IllegalArgumentException e) {
			throw new WrongJwtException(e);
		}
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(jwtProperties.getKey()).build().parseClaimsJws(token).getBody();
	}

	private List<SimpleGrantedAuthority> buildAuthorityList(List<String> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
	}

}
