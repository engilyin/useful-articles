package com.engilyin.usefularticles.ui.handlers;

import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.engilyin.usefularticles.data.auth.AuthResult;
import com.engilyin.usefularticles.services.auth.AuthService;
import com.engilyin.usefularticles.ui.data.auth.LoginRequest;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthHandler {

	private final AuthService authService;

	public Mono<ServerResponse> login(ServerRequest request) {

		return ok().body(
				request.bodyToMono(LoginRequest.class)
						.flatMap(login -> authService.authenticate(login.getUsername(), login.getPassword())),
				AuthResult.class);

	}

}
