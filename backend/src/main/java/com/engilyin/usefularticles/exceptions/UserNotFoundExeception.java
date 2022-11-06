package com.engilyin.usefularticles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundExeception extends RuntimeException {

	private static final long serialVersionUID = -7861285204569171467L;
	
	public static final String ERROR_MESSAGE_PREFIX = "The user not found: ";

	public UserNotFoundExeception(String username) {
		super(ERROR_MESSAGE_PREFIX + username);
	}
}
