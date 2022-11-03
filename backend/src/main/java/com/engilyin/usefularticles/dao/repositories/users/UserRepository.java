package com.engilyin.usefularticles.dao.repositories.users;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.engilyin.usefularticles.dao.entities.users.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    Mono<User> findByUsername(String username);

}

