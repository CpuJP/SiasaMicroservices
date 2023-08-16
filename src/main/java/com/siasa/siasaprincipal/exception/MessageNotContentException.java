package com.siasa.siasaprincipal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class MessageNotContentException extends ResponseStatusException {

    public MessageNotContentException(String message) {
        super(HttpStatus.NO_CONTENT, message);
    }
}
