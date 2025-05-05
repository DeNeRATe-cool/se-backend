package com.se.exception.collectException;

public class CollectNotExistException extends RuntimeException {
    public CollectNotExistException() {
        super("收藏的题目不存在");
    }
}
