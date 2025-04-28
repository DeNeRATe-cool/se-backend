package com.se.exception;

public class ParamIllegalException extends RuntimeException {
    public ParamIllegalException() {
        super("参数不合规!");
    }
}
