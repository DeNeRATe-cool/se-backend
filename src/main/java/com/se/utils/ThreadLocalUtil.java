package com.se.utils;

import org.apache.poi.ss.formula.functions.T;

public class ThreadLocalUtil {

    private static final ThreadLocal THREAD_LOCAL = new ThreadLocal();

    public static <T> T get()
    {
        return (T) THREAD_LOCAL.get();
    }

    public static void set(Object value)
    {
        THREAD_LOCAL.set(value);
    }

    public static void remove()
    {
        THREAD_LOCAL.remove();
    }

}
