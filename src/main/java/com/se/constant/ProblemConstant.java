package com.se.constant;

import java.util.HashMap;
import java.util.Map;

public class ProblemConstant {
    public static final HashMap<String, Integer> problemType = new HashMap<>(Map.ofEntries(
            Map.entry("选择题", 0),
            Map.entry("判断题", 1),
            Map.entry("简答题", 2),
            Map.entry("编程题", 3))
    );
}
