package com.mitrais.microservices.netflixzuulapigatewayserver.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCrudController<T, ID> {
    protected static final String ROLE_ADMIN = "ROLE_ADMIN";
    protected static final String ERROR = "error";

    abstract ResponseEntity<?> findAll();
    abstract Resource<?> findById(ID id);
    abstract ResponseEntity<?> create(T t);
    abstract ResponseEntity<?> update(T t, Long id);
    abstract ResponseEntity<?> delete(Long id);

    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(value= HttpStatus.BAD_REQUEST,
            reason="Database request error")
    public void dataAccessExceptionHandler(){

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> methodArgumentNotValid(MethodArgumentNotValidException ex){
        List<String> messages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(
                error -> messages.add(error.getField() + " " + error.getDefaultMessage())
        );
        String message = String.join(", ", messages);
        return new ResponseEntity<>(Collections.singletonMap(ERROR, message), HttpStatus.BAD_REQUEST);
    }
}
