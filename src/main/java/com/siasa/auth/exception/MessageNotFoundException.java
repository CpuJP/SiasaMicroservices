package com.siasa.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//Mensaje de excepcion personalizado para errores donde no se encuentra un dato solicitado en una api en base de datos
public class MessageNotFoundException extends ResponseStatusException {
    public MessageNotFoundException(String message){
        super(HttpStatus.NOT_FOUND, message);
    }
}
