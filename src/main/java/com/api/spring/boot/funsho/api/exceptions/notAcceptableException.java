package com.api.spring.boot.funsho.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class notAcceptableException extends RuntimeException{
        public notAcceptableException(String msg){
        super(msg);
    }    
}
