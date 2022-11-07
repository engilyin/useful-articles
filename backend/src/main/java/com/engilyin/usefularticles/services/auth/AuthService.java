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

package com.engilyin.usefularticles.services.auth;

import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.dao.entities.users.User;
import com.engilyin.usefularticles.dao.repositories.users.UserRepository;
import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.exceptions.WrongPasswordExeception;
import com.engilyin.usefularticles.security.TokenProvider;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final TokenProvider tokenProvider;

	public Mono<AuthResult> authenticate(String username, String password) {
		return userRepository.findByUsername(username)
				.switchIfEmpty(Mono.error(() -> new UserNotFoundExeception(username)))
				.flatMap(user -> doAuthentication(user, password));
	}

	private Mono<AuthResult> doAuthentication(User user, String requestPassword) {
		if (passwordMatches(requestPassword, user.getPassword())) {
			return Mono.just(AuthResult.builder()
					.name(user.getName())
					.username(user.getUsername())
					.role(user.getRole())
					.token(generateToken(user.getUsername()))
					.build());
		} else {
			throw new WrongPasswordExeception();
		}
	}

	private String generateToken(String username) {
		return tokenProvider.generateToken(username);
	}

	private boolean passwordMatches(String requestPassword, String userPassword) {
		return userPassword.equals(requestPassword);
	}
}
