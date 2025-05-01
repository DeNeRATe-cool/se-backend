package com.se.constant;

import com.se.entity.User;

/**
 * 用户类型枚举
 */
public enum UserTypeEnum {

    TEACHER(0, "Teacher"),
    STUDENT(1, "Student"),
    TUTOR(2, "Tutor");

    private final int code;
    private final String label;

    UserTypeEnum(int code, String label) {
        this.code = code;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public String getLabel() {
        return label;
    }

    /**
     * 根据 code 获取枚举
     */
    public static UserTypeEnum fromCode(int code) {
        for (UserTypeEnum type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的用户类型 code: " + code);
    }

}