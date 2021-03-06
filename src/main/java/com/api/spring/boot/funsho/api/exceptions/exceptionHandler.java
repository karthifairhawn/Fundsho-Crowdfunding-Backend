package com.api.spring.boot.funsho.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.util.Date;

@RestController
@RestControllerAdvice
public class exceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(userNotFoundException.class)
    public final ResponseEntity<Object>
        handleAllException(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse er = new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(er, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(notFoundException.class)
    public final ResponseEntity<Object>
        notFoundException(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse er = new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(er, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(unauthorizedException.class)
    public final ResponseEntity<Object> unauthorizedException(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse er = new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(er, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(notAcceptableException.class)
    public final ResponseEntity<Object> notAcceptableException(Exception ex, WebRequest request) throws Exception {
        ExceptionResponse er = new ExceptionResponse(new Date(),ex.getMessage(),request.getDescription(false));
        return new ResponseEntity(er, HttpStatus.NOT_ACCEPTABLE);
    }

}
