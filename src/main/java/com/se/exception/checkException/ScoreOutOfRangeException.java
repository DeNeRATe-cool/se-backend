package com.se.exception.checkException;

public class ScoreOutOfRangeException extends RuntimeException {
    public ScoreOutOfRangeException(String errorMessage) {
        super(errorMessage);
    }
}
