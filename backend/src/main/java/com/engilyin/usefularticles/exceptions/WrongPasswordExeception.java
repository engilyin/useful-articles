package com.engilyin.usefularticles.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNAUTHORIZED, reason = "Wrong password")
public class WrongPasswordExeception extends RuntimeException {

    private static final long serialVersionUID = 1464782994598651202L;

}
