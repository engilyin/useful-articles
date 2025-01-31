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
package com.engilyin.usefularticles.ui.controllers;

import com.engilyin.usefularticles.ui.openapi.api.AuthApi;
import com.engilyin.usefularticles.ui.openapi.model.AuthResult;
import com.engilyin.usefularticles.ui.openapi.model.PostUserRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class AuthController implements AuthApi {
    @Override
    public Mono<AuthResult> postUser(@Valid Mono<PostUserRequest> postUserRequest, ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        return null;
    }
}
