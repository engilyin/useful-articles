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
package com.engilyin.usefularticles.ui.handlers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.exceptions.WrongPasswordExeception;
import com.engilyin.usefularticles.services.auth.AuthService;
import com.engilyin.usefularticles.ui.data.auth.SigninRequest;
import com.engilyin.usefularticles.ui.routers.RoutesConfig;
import com.engilyin.usefularticles.ui.validation.ObjectValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

    private static final String TEST_USERNAME = "TEST_USERNAME";

    @Mock
    AuthService authService;

    @Mock
    ObjectValidator validator;

    WebTestClient client;

    @BeforeEach
    void setUp() throws Exception {
        AuthHandler authHandler = new AuthHandler(authService, validator);
        RouterFunction<?> routes = new RoutesConfig().authApis(authHandler);
        client = WebTestClient.bindToRouterFunction(routes).build();
    }

    @Test
    void testLogin() {
        SigninRequest loginRequest =
                SigninRequest.builder().username(TEST_USERNAME).password("").build();
        AuthResult authResult = AuthResult.builder().username(TEST_USERNAME).build();
        when(authService.authenticate(anyString(), anyString())).thenReturn(Mono.just(authResult));

        client.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/signin").build())
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("username")
                .isEqualTo(TEST_USERNAME);
    }

    @Test
    void testLoginUserNotFound() {
        SigninRequest loginRequest =
                SigninRequest.builder().username(TEST_USERNAME).password("").build();
        when(authService.authenticate(anyString(), anyString())).thenThrow(new UserNotFoundExeception(TEST_USERNAME));

        client.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/signin").build())
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void testLoginWrongPassword() {
        SigninRequest loginRequest =
                SigninRequest.builder().username(TEST_USERNAME).password("").build();
        when(authService.authenticate(anyString(), anyString())).thenThrow(new WrongPasswordExeception());

        client.post()
                .uri(uriBuilder -> uriBuilder.path("/auth/signin").build())
                .bodyValue(loginRequest)
                .exchange()
                .expectStatus()
                .isUnauthorized();
    }
}
