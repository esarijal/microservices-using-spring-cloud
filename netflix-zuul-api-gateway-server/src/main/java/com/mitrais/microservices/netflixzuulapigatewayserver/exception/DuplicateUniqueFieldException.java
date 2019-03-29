package com.mitrais.microservices.netflixzuulapigatewayserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateUniqueFieldException extends RuntimeException {
    public DuplicateUniqueFieldException(String message) {
        super(message);
    }
}
