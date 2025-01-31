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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.services.auth.AuthService;
import com.engilyin.usefularticles.ui.data.auth.SigninRequest;
import com.engilyin.usefularticles.ui.errorhandling.GlobalExceptionHandler;
import com.engilyin.usefularticles.ui.routers.RoutesConfig;
import com.engilyin.usefularticles.utils.TestSecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = {
            RoutesConfig.class,
            AuthHandler.class,
            TestSecurityConfig.class,
            GlobalExceptionHandler.class,
            ErrorProperties.class,
            ServerCodecConfigurer.class
        })
@WebFluxTest
@Slf4j
public class AuthHandlerIntegrtionTest {

    private static final String TEST_USERNAME = "TEST_USERNAME";

    @Autowired
    ApplicationContext context;

    @MockBean
    AuthService authService;

    WebTestClient webTestClient;

    @BeforeEach
    public void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(context).build();
    }

    @Test
    public void auth_validCreds_token() {

        var authResult = AuthResult.builder().username(TEST_USERNAME).build();

        when(authService.authenticate(anyString(), anyString())).thenReturn(Mono.just(authResult));

        webTestClient
                .post()
                .uri("/auth/signin") // GET method and URI
                .bodyValue(SigninRequest.builder()
                        .username(TEST_USERNAME)
                        .password("")
                        .build())
                .accept(MediaType.APPLICATION_JSON) // setting ACCEPT-Content
                .exchange() // gives access to response
                .expectStatus()
                .isOk() // checking if response is OK
                .expectBody(AuthResult.class)
                .value(ar -> {
                    assertThat(ar.getUsername(), equalTo(TEST_USERNAME));
                });
    }

    @Test
    public void auth_invalidCreds_userNotFound() {

        when(authService.authenticate(anyString(), anyString())).thenThrow(new UserNotFoundExeception(TEST_USERNAME));

        webTestClient
                .post()
                .uri("/auth/signin") // GET method and URI
                .bodyValue(SigninRequest.builder()
                        .username(TEST_USERNAME)
                        .password("")
                        .build())
                .accept(MediaType.APPLICATION_JSON) // setting ACCEPT-Content
                .exchange() // gives access to response
                .expectStatus()
                .isNotFound() // checking if response is OK
                .expectBody()
                .consumeWith(r -> log.info("The Error Result: {}", new String(r.getResponseBody())))
                .jsonPath("status")
                .isEqualTo(404)
                .jsonPath("message")
                .isEqualTo(UserNotFoundExeception.ERROR_MESSAGE_PREFIX + TEST_USERNAME);
    }
}
