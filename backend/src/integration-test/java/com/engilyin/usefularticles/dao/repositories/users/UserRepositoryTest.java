package com.engilyin.usefularticles.dao.repositories.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.engilyin.usefularticles.dao.entities.users.User;

import reactor.core.publisher.Hooks;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DataR2dbcTest
class UserRepositoryTest {
	
	@Autowired
	UserRepository userRepository;

	@BeforeEach
	void setUp() throws Exception {
        Hooks.onOperatorDebug();
	}

	@Test
	void testFindByUsername() {
		
		var user = User.builder()
				.id(1)
				.username("test")
				.name("Test user")
				.password("testpass")
				.role("generic")
				.build();
		
		userRepository.findByUsername("test")
		     .as(StepVerifier::create)
		     .expectNext(user)
		     .verifyComplete();
	}

}
