package com.engilyin.usefularticles.data.auth;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class AuthResult {
    String username;

    String name;

    String token;

}
