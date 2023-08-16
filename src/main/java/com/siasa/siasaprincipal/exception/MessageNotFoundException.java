package com.siasa.siasaprincipal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageNotFoundException extends ResponseStatusException {
    public MessageNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
