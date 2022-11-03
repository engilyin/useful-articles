package com.engilyin.usefularticles.ui.data.auth;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class LoginRequest {
	String username;
	String password;
}