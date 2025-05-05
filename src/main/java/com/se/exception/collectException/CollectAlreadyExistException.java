package com.se.exception.collectException;

public class CollectAlreadyExistException extends RuntimeException {
    public CollectAlreadyExistException() {
        super("收藏的题目已存在");
    }
}
