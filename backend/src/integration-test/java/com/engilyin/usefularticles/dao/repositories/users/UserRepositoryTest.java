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
package com.engilyin.usefularticles.dao.repositories.users;

import com.engilyin.usefularticles.consts.Consts;
import com.engilyin.usefularticles.dao.entities.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.r2dbc.DataR2dbcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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
                .userId(1)
                .username("test")
                .fullname("Test user")
                .password("testpass")
                .role(Consts.GENERIC_ROLE)
                .build();

        userRepository
                .findByUsername("test")
                .as(StepVerifier::create)
                .expectNext(user)
                .verifyComplete();
    }
}
