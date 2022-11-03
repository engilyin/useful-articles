package com.engilyin.usefularticles.ui.data.auth;

import lombok.Value;

@Value
public class LoginResponse {
    String username;

    String name;

    String role;

    String token;
}
