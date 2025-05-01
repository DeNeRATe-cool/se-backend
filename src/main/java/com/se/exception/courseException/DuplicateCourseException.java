package com.se.exception.courseException;

public class DuplicateCourseException extends RuntimeException{
    public DuplicateCourseException(String message)
    {
        super(message);
    }
}
