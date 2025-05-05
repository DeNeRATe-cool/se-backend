package com.se.exception.checkException;

public class NotCheckFinishException extends RuntimeException {
    public NotCheckFinishException() {
        super("题目没有批改完, 无法提交");
    }
}
