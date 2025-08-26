package com.se.controller;

import com.github.pagehelper.PageInfo;
import com.se.dto.AddAdminInCourseDTO;
import com.se.dto.Result;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private Course testCourse;
    private User testUser;
    private Class testClass;
    private AddAdminInCourseDTO testAddAdminDTO;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setCourse_id(1);
        testCourse.setName("Test Course");
        
        testUser = new User();
        testUser.setUser_id(1);
        testUser.setName("Test User");
        
        testClass = new Class();
        testClass.setClass_id(1);
        testClass.setName("Test Class");
        
        testAddAdminDTO = new AddAdminInCourseDTO();
        testAddAdminDTO.setUsername("aaa");
        testAddAdminDTO.setCourse_id(1);
    }

    // ==================== list 方法测试 ====================
    @Test
    void list_Success_WithCourses() {
        when(courseService.list()).thenReturn(Collections.singletonList(testCourse));
        Result result = courseController.list();
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Course> courses = (List<Course>) result.getData();
        assertEquals(1, courses.size());
        assertEquals(testCourse, courses.get(0));
    }

    @Test
    void list_Success_NoCourses() {
        when(courseService.list()).thenReturn(Collections.emptyList());
        Result result = courseController.list();
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Course> courses = (List<Course>) result.getData();
        assertTrue(courses.isEmpty());
    }

    // ==================== listPage 方法测试 ====================
    @Test
    void listPage_Success() {
        PageInfo<Course> pageInfo = new PageInfo<>(Collections.singletonList(testCourse));
        when(courseService.listPage(1, 10)).thenReturn(pageInfo);
        Result result = courseController.listPage(1, 10);
        assertNotNull(result);
        PageInfo<Course> resultPage = (PageInfo<Course>) result.getData();
        assertEquals(1, resultPage.getList().size());
        assertEquals(testCourse, resultPage.getList().get(0));
    }

    // ==================== add 方法测试 ====================
    @Test
    void add_Success() {
        when(courseService.add(any(Course.class))).thenReturn(testCourse);
        Result result = courseController.add(testCourse);
        assertNotNull(result);
        assertEquals(testCourse, result.getData());
    }

    @Test
    void add_Failure() {
        when(courseService.add(any(Course.class))).thenReturn(null);
        Result result = courseController.add(testCourse);
        assertNotNull(result);
        assertNull(result.getData());
    }

    // ==================== addAdmin 方法测试 ====================
    @Test
    void addAdmin_Success() {
        when(courseService.addAdmin(testAddAdminDTO)).thenReturn(Collections.singletonList(testUser));
        Result result = courseController.addAdmin(testAddAdminDTO);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void addAdmin_Failure() {
        when(courseService.addAdmin(testAddAdminDTO)).thenReturn(Collections.emptyList());
        Result result = courseController.addAdmin(testAddAdminDTO);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }

    // ==================== listTeacherAndTutor 方法测试 ====================
    @Test
    void listTeacherAndTutor_Success() {
        when(courseService.listTeacherAndTutor(1)).thenReturn(Collections.singletonList(testUser));
        Result result = courseController.listTeacherAndTutor(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void listTeacherAndTutor_Failure() {
        when(courseService.listTeacherAndTutor(1)).thenReturn(Collections.emptyList());
        Result result = courseController.listTeacherAndTutor(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }

    // ==================== courseInfo 方法测试 ====================
    @Test
    void courseInfo_Success() {
        when(courseService.courseInfo(1)).thenReturn(testCourse);
        Result result = courseController.courseInfo(1);
        assertNotNull(result);
        assertEquals(testCourse, result.getData());
    }

    @Test
    void courseInfo_Failure() {
        when(courseService.courseInfo(1)).thenReturn(null);
        Result result = courseController.courseInfo(1);
        assertNotNull(result);
        assertNull(result.getData());
    }

    // ==================== listByCourseId 方法测试 ====================
    @Test
    void listByCourseId_Success() {
        when(courseService.listByCourseId(1, 1)).thenReturn(Collections.singletonList(testClass));
        Result result = courseController.listByCourseId(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertEquals(1, classes.size());
        assertEquals(testClass, classes.get(0));
    }

    @Test
    void listByCourseId_Failure() {
        when(courseService.listByCourseId(1, 1)).thenReturn(Collections.emptyList());
        Result result = courseController.listByCourseId(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertTrue(classes.isEmpty());
    }


    @Test
    void getUserClass_Failure() {
        when(courseService.getUserClass(1, 1)).thenReturn(null);
        Result result = courseController.getUserClass(1, 1);
        assertNotNull(result);
        assertNull(result.getData());
    }
}