package com.engilyin.usefularticles.ui.handlers;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;

import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.exceptions.WrongPasswordExeception;
import com.engilyin.usefularticles.services.auth.AuthService;
import com.engilyin.usefularticles.ui.data.auth.SigninRequest;
import com.engilyin.usefularticles.ui.routers.RoutesConfig;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AuthHandlerTest {

	private static final String TEST_USERNAME = "TEST_USERNAME";

	@Mock
	AuthService authService;

	WebTestClient client;

	@BeforeEach
	void setUp() throws Exception {
		AuthHandler authHandler = new AuthHandler(authService);
		RouterFunction<?> routes = new RoutesConfig().authApis(authHandler);
		client = WebTestClient.bindToRouterFunction(routes).build();
	}

	@Test
	void testLogin() {
		SigninRequest loginRequest = SigninRequest.builder().username(TEST_USERNAME).password("").build();
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
		SigninRequest loginRequest = SigninRequest.builder().username(TEST_USERNAME).password("").build();
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
		SigninRequest loginRequest = SigninRequest.builder().username(TEST_USERNAME).password("").build();
		when(authService.authenticate(anyString(), anyString())).thenThrow(new WrongPasswordExeception());

		client.post()
				.uri(uriBuilder -> uriBuilder.path("/auth/signin").build())
				.bodyValue(loginRequest)
				.exchange()
				.expectStatus()
				.isUnauthorized();
	}
}
