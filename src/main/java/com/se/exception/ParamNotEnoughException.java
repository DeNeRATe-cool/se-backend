package com.se.exception;

public class ParamNotEnoughException extends RuntimeException {
    public ParamNotEnoughException() {
        super("参数数量不足!");
    }
}
