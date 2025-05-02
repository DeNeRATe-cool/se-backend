package com.se.exception.classException;

public class ClassNotExistException extends RuntimeException {
    public ClassNotExistException(String message) {
        super(message);
    }
}
