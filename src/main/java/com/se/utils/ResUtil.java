package com.se.utils;

import java.util.UUID;

public class ResUtil {
    public static String generateCode() {
        return UUID.randomUUID().toString();
    }
}
