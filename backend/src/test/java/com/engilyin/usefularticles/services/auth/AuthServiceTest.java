package com.engilyin.usefularticles.services.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.engilyin.usefularticles.dao.entities.users.User;
import com.engilyin.usefularticles.dao.repositories.users.UserRepository;
import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.exceptions.UserNotFoundExeception;
import com.engilyin.usefularticles.exceptions.WrongPasswordExeception;
import com.engilyin.usefularticles.security.TokenProvider;

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

	@BeforeEach
	void setUp() throws Exception {

		authService = new AuthService(userRepository, tokenProvider);
	}

	@Test
	void normalAuth() {

		User user = User.builder().username(TEST_USERNAME).name("ABC").password(TEST_ENCODED_PASSWORD).build();

		when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));
		when(tokenProvider.generateToken(TEST_USERNAME)).thenReturn("12345");

		Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

		StepVerifier.create(authResult).assertNext(auth -> {

			assertNotNull(auth);
			assertThat(user.getUsername(), is(notNullValue()));
			assertThat(user.getUsername(), equalTo(TEST_USERNAME));
			assertThat(user.getName(), is(notNullValue()));
		}).verifyComplete();
	}

	@Test
	void userNotFound() {

		when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.empty());

		Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

		StepVerifier.create(authResult).expectError(UserNotFoundExeception.class);
	}

	@Test
	void wrongPassword() {

		User user = User.builder().username(TEST_USERNAME).name("ABC").password("").build();

		when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(Mono.just(user));

		Mono<AuthResult> authResult = authService.authenticate(TEST_USERNAME, TEST_ENCODED_PASSWORD);

		StepVerifier.create(authResult).expectError(WrongPasswordExeception.class);
	}

}
