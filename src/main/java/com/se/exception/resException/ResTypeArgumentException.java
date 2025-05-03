package com.se.exception.resException;

public class ResTypeArgumentException extends RuntimeException {
    public ResTypeArgumentException() {
        super("资源更新前后类型不匹配");
    }
}
