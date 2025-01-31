/*
 Copyright 2022-2025 engilyin

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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.engilyin.usefularticles.consts.Consts;
import com.engilyin.usefularticles.dao.entities.users.User;
import com.engilyin.usefularticles.dao.repositories.users.UserRepository;
import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.exceptions.WrongPasswordExeception;
import com.engilyin.usefularticles.security.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private static final String TEST_USERNAME = "test";
    private static final String TEST_ENCODED_PASSWORD = "testEncodedPassword";

    AuthService authService;

    @Mock
    UserRepository userRepository;

    @Mock
    TokenProvider tokenProvider;

    @Mock
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() throws Exception {

        authService = new AuthService(userRepository, tokenProvider, passwordEncoder);
    }

    @Test
    void normalAuth() {

        User user = User.builder()
                .username(TEST_USERNAME)
                .fullname("ABC")
                .password(TEST_ENCODED_PASSWORD)
                .role(Consts.GENERIC_ROLE)
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));
        when(tokenProvider.generateToken(TEST_USERNAME, Consts.GENERIC_ROLE)).thenReturn("12345");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

        StepVerifier.create(authResult)
                .assertNext(auth -> {
                    assertNotNull(auth);
                    assertThat(user.getUsername(), is(notNullValue()));
                    assertThat(user.getUsername(), equalTo(TEST_USERNAME));
                    assertThat(user.getFullname(), is(notNullValue()));
                })
                .verifyComplete();
    }

    @Test
    void userNotFound() {

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.empty());

        Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

        StepVerifier.create(authResult).expectError(UserNotFoundExeception.class);
    }

    @Test
    void wrongPassword() {

        User user = User.builder()
                .username(TEST_USERNAME)
                .fullname("ABC")
                .password("")
                .build();

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));

        Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

        StepVerifier.create(authResult).expectError(WrongPasswordExeception.class);
    }
}
