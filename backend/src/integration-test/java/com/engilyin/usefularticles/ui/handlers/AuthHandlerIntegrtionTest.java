package com.engilyin.usefularticles.ui.handlers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthHandlerIntegrtionTest {

	@Autowired
	private WebTestClient webTestClient;
 
	@Test
	public void auth_validCreds_token() {
		webTestClient
		.get().uri("/auth/login") // GET method and URI
		.accept(MediaType.APPLICATION_JSON) //setting ACCEPT-Content
		.exchange() //gives access to response
		.expectStatus().isOk() //checking if response is OK
		.expectBody(String.class).isEqualTo("Hello World!"); // checking for response type and message
	}
}
