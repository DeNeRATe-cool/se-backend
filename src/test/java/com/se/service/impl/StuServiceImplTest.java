package com.se.service.impl;

import com.se.constant.StudentEntityConstant;
import com.se.dao.StuDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.Result;
import com.se.dto.UserCourseClass;
import com.se.entity.User;
import com.se.exception.EntityNotFoundException;
import com.se.exception.ParamNotEnoughException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StuServiceImplTest {

    @Mock
    private StuDao stuDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserCourseClassDao userCourseClassDao;

    @InjectMocks
    private StuServiceImpl stuService;

    private User testStudent;
    private UserCourseClass testUserCourseClass;

    @BeforeEach
    void setUp() {
        testStudent = new User();
        testStudent.setUser_id(1);
        testStudent.setName("Test Student");
        testStudent.setIdentity(StudentEntityConstant.IDENTITY_CODE);

        testUserCourseClass = new UserCourseClass();
        testUserCourseClass.setUser_id(1);
        testUserCourseClass.setCourse_id(1);
        testUserCourseClass.setClass_id(1);
    }

    // ==================== getAllStudent 方法测试 ====================
    @Test
    void getAllStudent_Success_StudentsExist() {
        // 模拟依赖行为
        when(stuDao.getAllStudent()).thenReturn(Arrays.asList(testStudent));

        // 调用被测试方法
        Result result = stuService.getAllStudent();

        // 验证结果
        assertEquals(1, result.getTotal());
        List<User> students = (List<User>) result.getData();
        assertEquals(1, students.size());
        assertEquals(testStudent, students.get(0));
    }

    @Test
    void getAllStudent_Success_NoStudents() {
        // 模拟依赖行为
        when(stuDao.getAllStudent()).thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result result = stuService.getAllStudent();

        // 验证结果
        assertEquals(0, result.getTotal());
        List<User> students = (List<User>) result.getData();
        assertTrue(students.isEmpty());
    }

    // ==================== listByCourseID 方法测试 ====================
    @Test
    void listByCourseID_Success_StudentsExist() {
        // 准备测试数据
        int courseId = 1;

        // 模拟依赖行为
        when(userCourseClassDao.getStudentClassList(courseId, StudentEntityConstant.IDENTITY_CODE))
                .thenReturn(Arrays.asList(testUserCourseClass));
        when(userDao.getUserByID(1)).thenReturn(Arrays.asList(testStudent));

        // 调用被测试方法
        List<User> result = stuService.listByCourseID(courseId);

        // 验证结果
        assertEquals(1, result.size());
        assertEquals(testStudent, result.get(0));
    }

    @Test
    void listByCourseID_Success_NoStudents() {
        // 准备测试数据
        int courseId = 1;

        // 模拟依赖行为
        when(userCourseClassDao.getStudentClassList(courseId, StudentEntityConstant.IDENTITY_CODE))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        List<User> result = stuService.listByCourseID(courseId);

        // 验证结果
        assertTrue(result.isEmpty());
    }
}