package com.engilyin.usefularticles.utils;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.r2dbc.AutoConfigureDataR2dbc;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Configuration
@AutoConfigureDataR2dbc
@EnableR2dbcAuditing
@EnableAutoConfiguration
public class DbTestConfig {

}
