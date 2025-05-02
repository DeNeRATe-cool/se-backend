package com.se.exception.courseException;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(String msg) {
        super(msg);
    }
}
