package com.se.service.impl;

import com.se.constant.ResourceConstant;
import com.se.dao.ProcessDao;
import com.se.entity.Class;
import com.se.entity.Process;
import com.se.entity.Resource;
import com.se.entity.User;
import com.se.exception.ParamNotEnoughException;
import com.se.exception.resException.ResTypeArgumentException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.UserCourseClassService;
import com.se.utils.ResTagFilterUtil;
import com.se.utils.ResUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessServiceImplTest {

    @Mock
    private ProcessDao processDao;

    @Mock
    private UserCourseClassService userCourseClassService;

    @InjectMocks
    private ProcessServiceImpl processService;

    private Process testProcess;
    private Resource testResource;
    private User testUser;
    private Class testClass;

    @BeforeEach
    void setUp() {
        testProcess = new Process();
        testProcess.setName("Test Process");
        testProcess.setTime(new Date());
        testProcess.setCourse_id(1);
        testProcess.setClass_id(1);

        testResource = new Resource();
        testResource.setName("test.pdf");
        testResource.setUrl("https://example.com/test.pdf");
        testResource.setType("pdf");
        testResource.setRes_code("RES001");
        testResource.setCourse_id(1);
        testResource.setClass_id(1);
        testResource.setProcess_id(1);

        testUser = new User();
        testUser.setUser_id(1);
        testUser.setName("Test User");

        testClass = new Class();
        testClass.setClass_id(1);
        testClass.setCourse_id(1);
        testClass.setName("Test Class");
    }

    // ==================== createProcess 方法测试 ====================

    @Test
    void createProcess_Success_ValidData() {
        // 调用被测试方法
        processService.createProcess(testProcess);

        // 验证依赖调用
        verify(processDao).insert(testProcess);
    }

    @Test
    void createProcess_Failure_MissingParameters() {
        // 准备测试数据
        Process invalidProcess = new Process();
        invalidProcess.setName("Invalid");
        // 缺少 time, course_id, class_id

        // 验证异常
        assertThrows(ParamNotEnoughException.class, () -> {
            processService.createProcess(invalidProcess);
        });
    }

    // ==================== queryByClass 方法测试 ====================

    @Test
    void queryByClass_Success_ValidData() {
        // 准备测试数据
        int courseId = 1, classId = 1;

        // 模拟依赖行为
        when(processDao.queryByClass(courseId, classId)).thenReturn(Collections.singletonList(testProcess));

        // 调用被测试方法
        List<Process> result = processService.queryByClass(courseId, classId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProcess, result.get(0));
    }

    @Test
    void queryByClass_Failure_MissingParameters() {
        // 验证异常
        assertThrows(ParamNotEnoughException.class, () -> {
            processService.queryByClass(null, 1);
        });
    }
//
//    // ==================== addResource 方法测试 ====================
//
    @Test
    void addResource_Success_PublicResource() {
        // 准备测试数据
        String fileName = "public.pdf";
        int processId = 1, courseId = 1, classId = 1;
        String url = "https://example.com/public.pdf";
        boolean isPublic = true;
        String tags = "tag1,tag2";

        // 模拟依赖行为
        doNothing().when(processDao).addResource(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString());

        // 调用被测试方法
        processService.addResource(fileName, processId, courseId, classId, url, isPublic, tags);

        // 验证依赖调用
        verify(processDao, times(2)).addResource(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    void addResource_Success_PrivateResource() {
        // 准备测试数据
        String fileName = "private.pdf";
        int processId = 1, courseId = 1, classId = 1;
        String url = "https://example.com/private.pdf";
        boolean isPublic = false;
        String tags = "tag1";

        // 模拟依赖行为
        doNothing().when(processDao).addResource(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString());

        // 调用被测试方法
        processService.addResource(fileName, processId, courseId, classId, url, isPublic, tags);

        // 验证依赖调用
        verify(processDao, times(1)).addResource(anyString(), anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyString(), anyString(), anyString());
    }
//
//    // ==================== downloadResource 方法测试 ====================
//
    @Test
    void downloadResource_Success_ResourceExists() {
        // 准备测试数据
        int resourceId = 1;

        // 模拟依赖行为
        when(processDao.getResourceUrl(resourceId)).thenReturn("https://example.com/test.pdf");

        // 调用被测试方法
        String result = processService.downloadResource(resourceId);

        // 验证结果
        assertEquals("https://example.com/test.pdf", result);
    }

    @Test
    void downloadResource_Failure_MissingParameters() {
        // 验证异常
        assertThrows(ParamNotEnoughException.class, () -> {
            processService.downloadResource(null);
        });
    }
//
//    // ==================== getResourceByProcess 方法测试 ====================
//
    @Test
    void getResourceByProcess_Success_ValidData() {
        // 准备测试数据
        int processId = 1, courseId = 1;

        // 模拟依赖行为
        when(processDao.getResourceByProcess(processId, courseId)).thenReturn(Collections.singletonList(testResource));

        // 调用被测试方法
        List<Resource> result = processService.getResourceByProcess(processId, courseId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testResource, result.get(0));
    }

    @Test
    void getResourceByProcess_Failure_MissingParameters() {
        // 验证异常
        assertThrows(ParamNotEnoughException.class, () -> {
            processService.getResourceByProcess(null, 1);
        });
    }
//
   // ==================== getPublicResourceByCourse 方法测试 ====================
//
    @Test
    void getPublicResourceByCourse_Success_ResourcesExist() {
        // 准备测试数据
        int courseId = 1;

        // 模拟依赖行为
        when(processDao.getPublicResourceByCourse(courseId)).thenReturn(Collections.singletonList(testResource));

        // 调用被测试方法
        List<Resource> result = processService.getPublicResourceByCourse(courseId);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testResource, result.get(0));
    }

    @Test
    void getPublicResourceByCourse_Success_NoResources() {
        // 准备测试数据
        int courseId = 1;

        // 模拟依赖行为
        when(processDao.getPublicResourceByCourse(courseId)).thenReturn(Collections.emptyList());

        // 调用被测试方法
        List<Resource> result = processService.getPublicResourceByCourse(courseId);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
//
}