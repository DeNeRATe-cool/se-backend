package com.se.exception.userException;

public class UserPermissionException extends RuntimeException{
    public UserPermissionException(String msg)
    {
        super(msg);
    }
}
