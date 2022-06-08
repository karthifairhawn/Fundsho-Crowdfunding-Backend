package com.api.spring.boot.funsho.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class unauthorizedException extends RuntimeException {
    public unauthorizedException(String message) {
        super(message);
    }
}
