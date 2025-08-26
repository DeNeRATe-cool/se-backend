package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Class;
import com.se.entity.Process;
import com.se.entity.Resource;
import com.se.entity.User;
import com.se.exception.ParamNotEnoughException;
import com.se.service.CourseService;
import com.se.service.ProcessService;
import com.se.service.UserCourseClassService;
import com.se.utils.OssService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProcessControllerTest {

    @Mock
    private OssService ossService;

    @Mock
    private ProcessService processService;

    @Mock
    private CourseService courseService;

    @Mock
    private UserCourseClassService userCourseClassService;

    @InjectMocks
    private ProcessController processController;

    private Process testProcess;
    private Resource testResource;
    private Class testClass;
    private User testUser;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        testProcess = new Process();
        testProcess.setName("Test Process");
        
        testResource = new Resource();
        testResource.setName("Test Resource");
        
        testClass = new Class();
        testClass.setName("Test Class");
        
        testUser = new User();
        testUser.setName("Test User");
        
        testFile = new MockMultipartFile("test.txt", "test content".getBytes());
    }

    // ==================== createProcess 方法测试 ====================
    @Test
    void createProcess_Success() {
        doNothing().when(processService).createProcess(testProcess);
        Result result = processController.createProcess(testProcess);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    // ==================== upload 方法测试 ====================
    @Test
    void upload_Success() throws IOException {
        when(ossService.uploadFile(anyString(), any()))
            .thenReturn("http://example.com/resource");
        doNothing().when(processService).addResource(anyString(), anyInt(), anyInt(), anyInt(), anyString(), anyBoolean(), anyString());
        
        Result result = processController.upload(
            testFile, 1, 1, 1, "tag1,tag2", true
        );
        
        assertNotNull(result);
        assertEquals("http://example.com/resource", result.getData());
    }

    @Test
    void upload_Failure_NoFile() {
        assertThrows(ParamNotEnoughException.class, () -> {
            processController.upload(null, 1, 1, 1, "tag1,tag2", true);
        });
    }

    // ==================== download 方法测试 ====================
    @Test
    void download_Success() {
        when(processService.downloadResource(1)).thenReturn("http://example.com/resource");
        Result result = processController.download(1);
        assertNotNull(result);
        assertEquals("http://example.com/resource", result.getData());
    }

    // ==================== getResourceByProcess 方法测试 ====================
    @Test
    void getResourceByProcess_Success() {
        when(processService.getResourceByProcess(1, 1))
            .thenReturn(Collections.singletonList(testResource));
        Result result = processController.getResourceByProcess(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Resource> resources = (List<Resource>) result.getData();
        assertEquals(1, resources.size());
    }

    @Test
    void getResourceByProcess_Empty() {
        when(processService.getResourceByProcess(1, 1))
            .thenReturn(Collections.emptyList());
        Result result = processController.getResourceByProcess(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Resource> resources = (List<Resource>) result.getData();
        assertTrue(resources.isEmpty());
    }

    // ==================== getResourceByTag 方法测试 ====================
    @Test
    void getResourceByTag_Success() {
        when(courseService.listTeacherAndTutor(1))
            .thenReturn(Collections.singletonList(testUser));
        when(userCourseClassService.listClassesByCourse(1))
            .thenReturn(Collections.singletonList(testClass));
        when(processService.getResourceByTag(anyList(), anyList(), eq(1), eq(1), anyString()))
            .thenReturn(Collections.singletonList(testResource));
        
        Result result = processController.getResourceByTag(1, 1, "tag1");
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Resource> resources = (List<Resource>) result.getData();
        assertEquals(1, resources.size());
    }

    // ==================== deleteResource 方法测试 ====================
    @Test
    void deleteResource_Success() {
        doNothing().when(processService).deleteResource(1);
        Result result = processController.deleteResource(1);
        assertNotNull(result);
    }

    // ==================== updateResource 方法测试 ====================
    @Test
    void updateResource_Success() throws IOException {
        when(ossService.uploadFile(anyString(), any()))
            .thenReturn("http://example.com/updated");
        doNothing().when(processService).updateResource(anyString(), anyString(), anyInt(), anyBoolean());
        
        Result result = processController.updateResource(1, testFile, true);
        assertNotNull(result);
        assertEquals("http://example.com/updated", result.getData());
    }

    @Test
    void updateResource_Failure_NoFile() {
        assertThrows(ParamNotEnoughException.class, () -> {
            processController.updateResource(1, null, true);
        });
    }

    // ==================== getResourceHistory 方法测试 ====================
    @Test
    void getResourceHistory_Success() {
        when(processService.getResourceHistory(1))
            .thenReturn(Collections.singletonList(testResource));
        Result result = processController.getResourceHistory(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Resource> resources = (List<Resource>) result.getData();
        assertEquals(1, resources.size());
    }

    @Test
    void getResourceHistory_Empty() {
        when(processService.getResourceHistory(1))
            .thenReturn(Collections.emptyList());
        Result result = processController.getResourceHistory(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Resource> resources = (List<Resource>) result.getData();
        assertTrue(resources.isEmpty());
    }

    // ==================== queryByClass 方法测试 ====================
    @Test
    void queryByClass_Success() {
        when(processService.queryByClass(1, 1))
            .thenReturn(Collections.singletonList(testProcess));
        Result result = processController.queryByClass(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Process> processes = (List<Process>) result.getData();
        assertEquals(1, processes.size());
    }

    @Test
    void queryByClass_Empty() {
        when(processService.queryByClass(1, 1))
            .thenReturn(Collections.emptyList());
        Result result = processController.queryByClass(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Process> processes = (List<Process>) result.getData();
        assertTrue(processes.isEmpty());
    }
}