package com.siasa.reportes.siasareportes.controller;

import com.siasa.reportes.siasareportes.exception.ReporteVacioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReporteVacioException.class)
    public ResponseEntity<String> handlerReporteVacioException(ReporteVacioException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NO_CONTENT);
    }
}
