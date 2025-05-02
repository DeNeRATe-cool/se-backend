package com.se.exception.courseException;

public class DuplicateInvitationException extends RuntimeException{
    public DuplicateInvitationException(String msg)
    {
        super(msg);
    }
}
