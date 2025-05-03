package com.se.utils;

import com.se.constant.ResourceConstant;

import java.util.UUID;

public class ResUtil {
    public static String generateCode() {
        return UUID.randomUUID().toString();
    }

    public static String getSuffix(String res) {
        if(!res.contains(".")) return ResourceConstant.NotASuffix;
        return res.substring(res.lastIndexOf(".")+1);
    }
}
