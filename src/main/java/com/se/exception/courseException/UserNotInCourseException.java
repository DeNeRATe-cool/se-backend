package com.se.exception.courseException;

public class UserNotInCourseException extends RuntimeException{
    public UserNotInCourseException(String msg)
    {
        super(msg);
    }
}
