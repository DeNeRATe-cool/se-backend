package com.se.exception.checkException;

public class LengthNotMatchException extends RuntimeException {
    public LengthNotMatchException() {
        super("列表参数长度不匹配");
    }
}
