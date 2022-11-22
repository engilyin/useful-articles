package com.engilyin.usefularticles.security;

import java.util.Base64;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import io.jsonwebtoken.security.Keys;
import lombok.Getter;

@Getter
@Configuration
public class JwtProperties {

    private final int sessionTime;

    private final SecretKey key;

    public JwtProperties(@Value("${jwt.secret}") String secret, @Value("${jwt.session-time}") int sessionTime) {
        this.key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
        this.sessionTime = sessionTime;
    }

}
