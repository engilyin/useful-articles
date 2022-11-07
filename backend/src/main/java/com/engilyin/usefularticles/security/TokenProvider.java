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
import java.util.Arrays;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.consts.UaConsts;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenProvider {

	public String getUsernameFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.getSubject();
	}

	public Date getExpirationDateFromToken(String token) {
		final Claims claims = getAllClaimsFromToken(token);
		return claims.getExpiration();
	}

	public Claims getAllClaimsFromToken(String token) {
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}

	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(String subject) {

		Claims claims = Jwts.claims().setSubject(subject);
		claims.put("scopes", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN")));
		SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

		return Jwts.builder().setClaims(claims).setIssuer("http://engilyin.com")
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(Date.from(Instant.now().plus(UaConsts.ACCESS_TOKEN_VALIDITY_HOURS, ChronoUnit.HOURS)))
				.signWith(key).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
