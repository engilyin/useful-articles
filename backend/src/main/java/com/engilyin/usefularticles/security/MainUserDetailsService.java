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

import java.util.Arrays;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.engilyin.usefularticles.dao.repositories.users.UserRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class MainUserDetailsService implements ReactiveUserDetailsService {

	private final UserRepository userRepository;

	@Override
	public Mono<UserDetails> findByUsername(String username) {

		return userRepository.findByUsername(username)
				.map(u -> User.withUsername(u.getUsername())
						.password(u.getPassword())
						.authorities(Arrays.asList(new SimpleGrantedAuthority(u.getRole())))
						.accountExpired(u.isLocked())
						.credentialsExpired(u.isLocked())
						.disabled(u.isLocked())
						.accountLocked(u.isLocked())
						.build());
	}

}
