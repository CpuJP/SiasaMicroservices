package com.siasa.principalfailover.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

//Mensaje de excepcion personalizado para errores de no contenido en la consulta de apis en base de datos
public class MessageNotContentException extends ResponseStatusException {

    public MessageNotContentException(String message) {
        super(HttpStatus.NO_CONTENT, message);
    }
}
