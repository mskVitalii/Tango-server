package com.tango.exception;

public class TangoException extends RuntimeException {
    public TangoException(String message) {
        super(message);
    }

    public TangoException(String message, Throwable cause) {
        super(message, cause);
    }
}
