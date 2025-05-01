package com.se.exception.classException;

public class DuplicateCourseException extends RuntimeException{
    public DuplicateCourseException(String message)
    {
        super(message);
    }
}
