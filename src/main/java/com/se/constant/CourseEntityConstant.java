package com.se.constant;

public class CourseEntityConstant {

    // 邀请老师 / 助教  进入课程
    public static final String DUPLICATE_TEACHER_INVITED = "重复邀请老师进入课程";
    public static final String DUPLICATE_TUTOR_INVITED = "重复邀请助教进入课程";
    public static final String DUPLICATE_STUDENT_INVITED = "选修课程学生不能同时担任助教";

    // 老师 / 助教 不在课程中
    public static final String TEACHER_NOT_IN_COURSE = "老师不属于该课程";
    public static final String TUTOR_NOT_IN_COURSE  = "学生不是助教 OR 助教不属于该课程";
    public static final String ADMIN_NOT_IN_COURSE = "用户不是该课程的老师或者助教";

    // 课程 班级 不匹配
    public static final String COURSE_CLASS_NOT_MATCH = "课程与班级不匹配";

    // 课程没有找到
    public static final String COURSE_NOT_FOUND = "该课程不存在";

    // 学生重复加入课程
    public static final String STUDENT_DUPLICATE_JOIN_COURSE = "学生重复加入课程";

    // 课程id不合法
    public static final String COURSE_ID_INVALID = "课程id应该为正数";

    // 学生不在课程中
    public static final String STUDENT_NOT_IN_COURSE = "学生不在课程中";

}
