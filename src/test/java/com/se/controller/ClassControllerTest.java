package com.se.controller;

import com.se.dto.AddAdminInClassDTO;
import com.se.dto.ApplyJoinClassDTO;
import com.se.dto.DelStuInClassDTO;
import com.se.dto.Result;
import com.se.entity.Class;
import com.se.entity.User;
import com.se.service.ClassService;
import com.se.utils.ExcelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassControllerTest {

    @Mock
    private ClassService classService;

    @InjectMocks
    private ClassController classController;

    private Class testClass;
    private User testUser;
    private AddAdminInClassDTO testAddAdminDTO;
    private ApplyJoinClassDTO testApplyDTO;
    private DelStuInClassDTO testDelStuDTO;

    @BeforeEach
    void setUp() {
        testClass = new Class();
        testClass.setName("Test Class");
        
        testUser = new User();
        testUser.setUser_id(1);
        testUser.setName("Test User");
        
        testAddAdminDTO = new AddAdminInClassDTO();
        testAddAdminDTO.setUser_id(1);
        testAddAdminDTO.setCourse_id(1);
        testAddAdminDTO.setClass_id(1);
        
        testApplyDTO = new ApplyJoinClassDTO();
        testApplyDTO.setUser_id(1);
        testApplyDTO.setCourse_id(1);
        testApplyDTO.setClass_code("1");
        
        testDelStuDTO = new DelStuInClassDTO();
        testDelStuDTO.setUser_id(1);
        testDelStuDTO.setCourse_id(1);
        testDelStuDTO.setClass_id(1);
    }

    // ==================== add 方法测试 ====================
    @Test
    void add_Success() {
        when(classService.add(any(Class.class))).thenReturn(testClass);
        Result result = classController.add(testClass);
        assertNotNull(result);
        assertEquals(testClass, result.getData());
    }

    @Test
    void add_Failure() {
        when(classService.add(any(Class.class))).thenReturn(null);
        Result result = classController.add(testClass);
        assertNotNull(result);
        assertNull(result.getData());
    }

    // ==================== info 方法测试 ====================
    @Test
    void info_Success() {
        when(classService.info(1)).thenReturn(testClass);
        Result result = classController.info(1);
        assertNotNull(result);
        assertEquals(testClass, result.getData());
    }

    @Test
    void info_Failure() {
        when(classService.info(1)).thenReturn(null);
        Result result = classController.info(1);
        assertNotNull(result);
        assertNull(result.getData());
    }
//
//    // ==================== addAdmin 方法测试 ====================
    @Test
    void addAdmin_Success() {
        when(classService.addAdmin(testAddAdminDTO)).thenReturn(Collections.singletonList(testUser));
        Result result = classController.addAdmin(testAddAdminDTO);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void addAdmin_Failure() {
        when(classService.addAdmin(testAddAdminDTO)).thenReturn(Collections.emptyList());
        Result result = classController.addAdmin(testAddAdminDTO);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }
//
//    // ==================== listTeacherAndTutor 方法测试 ====================
    @Test
    void listTeacherAndTutor_Success() {
        when(classService.listTeacherAndTutor(1)).thenReturn(Collections.singletonList(testUser));
        Result result = classController.listTeacherAndTutor(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void listTeacherAndTutor_Failure() {
        when(classService.listTeacherAndTutor(1)).thenReturn(Collections.emptyList());
        Result result = classController.listTeacherAndTutor(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }
//
//    // ==================== list 方法测试 ====================
    @Test
    void list_Success() {
        when(classService.list()).thenReturn(Collections.singletonList(testClass));
        Result result = classController.list();
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertEquals(1, classes.size());
        assertEquals(testClass, classes.get(0));
    }

    @Test
    void list_Failure() {
        when(classService.list()).thenReturn(Collections.emptyList());
        Result result = classController.list();
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertTrue(classes.isEmpty());
    }
//
//
//    // ==================== apply 方法测试 ====================
    @Test
    void apply_Success() {
        doNothing().when(classService).apply(testApplyDTO);
        Result result = classController.apply(testApplyDTO);
        assertNotNull(result);
    }
//
//    // ==================== listByClassID 方法测试 ====================
    @Test
    void listByClassID_Success() {
        when(classService.listByClassID(1, 1, 1)).thenReturn(Collections.singletonList(testUser));
        Result result = classController.listByClassID(1, 1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void listByClassID_Failure() {
        when(classService.listByClassID(1, 1, 1)).thenReturn(Collections.emptyList());
        Result result = classController.listByClassID(1, 1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }
//
//    // ==================== addStu 方法测试 ====================
    @Test
    void addStu_Success() {
        Map<String, String> params = new HashMap<>();
        params.put("course_id", "1");
        params.put("class_id", "1");
        params.put("user_id", "1");
        params.put("username", "testuser");

        when(classService.addStu(1, 1, 1, "testuser")).thenReturn(Collections.singletonList(testUser));
        Result result = classController.addStu(params);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }
//
//    // ==================== listClassByStu 方法测试 ====================
    @Test
    void listClassByStu_Success() {
        when(classService.listClassByStu(1)).thenReturn(Collections.singletonList(testClass));
        Result result = classController.listClassByStu(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertEquals(1, classes.size());
        assertEquals(testClass, classes.get(0));
    }

    @Test
    void listClassByStu_Failure() {
        when(classService.listClassByStu(1)).thenReturn(Collections.emptyList());
        Result result = classController.listClassByStu(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertTrue(classes.isEmpty());
    }
//
//    // ==================== delStu 方法测试 ====================
    @Test
    void delStu_Success() {
        when(classService.delStu(1, 1, 1)).thenReturn(Collections.singletonList(testUser));
        Result result = classController.delStu(testDelStuDTO);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void delStu_Failure() {
        when(classService.delStu(1, 1, 1)).thenReturn(Collections.emptyList());
        Result result = classController.delStu(testDelStuDTO);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> users = (List<User>) result.getData();
        assertTrue(users.isEmpty());
    }
//
//
    // ==================== listByAdmin 方法测试 ====================
    @Test
    void listByAdmin_Success() {
        when(classService.listByAdmin(1, 1)).thenReturn(Collections.singletonList(testClass));
        Result result = classController.listByAdmin(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertEquals(1, classes.size());
        assertEquals(testClass, classes.get(0));
    }

    @Test
    void listByAdmin_Failure() {
        when(classService.listByAdmin(1, 1)).thenReturn(Collections.emptyList());
        Result result = classController.listByAdmin(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Class> classes = (List<Class>) result.getData();
        assertTrue(classes.isEmpty());
    }
//
//    // ==================== listByStu 方法测试 ====================
    @Test
    void listByStu_Success() {
        when(classService.listByStu(1, 1)).thenReturn(testClass);
        Result result = classController.listByStu(1, 1);
        assertNotNull(result);
        assertEquals(testClass, result.getData());
    }

    @Test
    void listByStu_Failure() {
        when(classService.listByStu(1, 1)).thenReturn(null);
        Result result = classController.listByStu(1, 1);
        assertNotNull(result);
        assertNull(result.getData());
    }
}