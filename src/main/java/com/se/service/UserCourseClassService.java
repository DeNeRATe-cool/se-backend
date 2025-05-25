package com.se.service;

import com.se.dto.UserCourseClass;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserCourseClassService {
    // 老师是否在课程中
    public Boolean teacherInCourse(User user, Integer course_id);
    public Boolean teacherInCourse(Integer user_id, Integer course_id);
    // 课程id + 身份 获得用户名单
    public List<User> getUserListByCourseAndIdentity(Integer course_id, Integer identity);
    // 课程助教名单
    public List<User> getTutorListByCourse(Integer course_id);
    // 课程老师名单
    public List<User> getTeacherListByCourse(Integer course_id);
    // 是否为目标课程学生
    public Boolean studentInCourse(User user, Integer course_id);
    // 是否为目标课程助教
    public Boolean tutorInCourse(User user, Integer course_id);
    public Boolean tutorInCourse(Integer user_id, Integer course_id);
    // 班级老师助教名单
    public List<User> getAdminListByClass(Integer class_id);
    // 课程和班级是否匹配
    public Boolean isCourseAndClassMatch(Integer course_id, Integer class_id);
    // 课程老师助教名单
    public List<User> getAdminListByCourse(Integer courseId);
    // 检查用户是否存在
    public Boolean userExist(Integer user_id);
    // 用户是教师
    public Boolean userIsTeacher(Integer user_id);
    // 用户是学生
    public Boolean userIsStudent(Integer user_id);
    // 检查班级是否存在
    public Boolean classExist(Integer class_id);
    public Boolean classExist(String class_code);
    // 学生是否在班级中
    public Boolean studentInCourse(Integer user_id, Integer course_id);
    // 根据课程得到班级列表
    public List<Class> listClassesByCourse(Integer course_id);
    // 根据班级获取学生名单
    public List<User> listStuByClass(Integer class_id);
    // 根据班级得到课程
    public List<Course> getCourseListByClass(Integer class_id);
    // 根据学生得到班级名单
    public List<Class> listClassByStudent(Integer user_id);
    // 检查并得到user
    public User safeGetUser(Integer user_id);
    public User safeGetUser(String username);
    // 尝试得到 User 成功返回 User 失败返回 null
    public User tryGetUser(Integer user_id);
    public User tryGetUser(String username);
    // 检查user_id 是否匹配课程，课程是否匹配班级，用户是否为助教或者老师
    public void checkCourseAndClassAndAdmin(Integer course_id, Integer class_id, Integer user_id);
    // 添加记录
    public void insert(Integer user_id, Integer course_id, Integer class_id, Integer identity);
    // 检查是不是课程的管理者
    public void checkIsAdminForCourse(Integer user_id, Integer course_id);
    // 判断是不是课程的管理者
    public Boolean isAdminForCourse(Integer user_id, Integer course_id);
    // 检查是不是学生
    public void checkIsStudent(Integer user_id);

    // 检查课程和班级是否匹配 是否存在
    public void checkCourseAndClass(Integer course_id, Integer class_id);
    // 删除
    public void delete(Integer course_id, Integer class_id, Integer user_id);
    // 查询
    public UserCourseClass select(Integer user_id, Integer course_id, Integer class_id);
    // 根据用户id 作为 指定身份，查询课程列表
    List<Course> listCourseByUserIdAndIdentity(Integer userId, Integer identity);

    // 获取学生在课程中的班级
    List<Class> listClassesByCourseAndStuId(Integer courseId, Integer userId);
}
