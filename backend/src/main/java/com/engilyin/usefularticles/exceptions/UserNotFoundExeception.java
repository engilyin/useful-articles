package com.engilyin.usefularticles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Unable to find user")
public class UserNotFoundExeception extends RuntimeException {

    private static final long serialVersionUID = -7861285204569171467L;

}
