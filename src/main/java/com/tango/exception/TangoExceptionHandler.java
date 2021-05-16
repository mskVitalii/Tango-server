package com.tango.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class TangoExceptionHandler {

    @ExceptionHandler(value = {TangoException.class})
    public ResponseEntity<Object> handleTangoException(TangoException e) {
        TangoExceptionModel exception = new TangoExceptionModel(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Z")));

        return new ResponseEntity<>(exception, HttpStatus.BAD_REQUEST);
    }
}
