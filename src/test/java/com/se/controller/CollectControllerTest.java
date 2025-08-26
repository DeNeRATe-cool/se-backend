package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Problem;
import com.se.service.CollectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CollectControllerTest {

    @Mock
    private CollectService collectService;

    @InjectMocks
    private CollectController collectController;

    private Problem testProblem;

    @BeforeEach
    void setUp() {
        testProblem = new Problem();
//        testProblem.setId(1);
//        testProblem.setTitle("Test Problem");
    }

    // ==================== addLike 方法测试 ====================
    @Test
    void addLike_Success() {
        // 调用被测试方法
        Result result = collectController.addLike(1, 1);

        // 验证结果
        assertNotNull(result);
        verify(collectService).addLike(1, 1);
    }

    @Test
    void addLike_Failure_InvalidParameters() {
        // 调用被测试方法
        Result result = collectController.addLike(null, 1);

        // 验证结果
        assertNotNull(result);
    }

    // ==================== addWrong 方法测试 ====================
    @Test
    void addWrong_Success() {
        // 调用被测试方法
        Result result = collectController.addLike(1, 1, 1); // 注意：方法名是addLike，但参数不同

        // 验证结果
        assertNotNull(result);
        verify(collectService).addWrong(1, 1, 1);
    }

    @Test
    void addWrong_Failure_InvalidParameters() {
        // 调用被测试方法
        Result result = collectController.addLike(null, 1, 1);

        // 验证结果
        assertNotNull(result);
    }

    // ==================== deleteLike 方法测试 ====================
    @Test
    void deleteLike_Success() {
        // 调用被测试方法
        Result result = collectController.deleteLike(1, 1);

        // 验证结果
        assertNotNull(result);
        verify(collectService).deleteLike(1, 1);
    }

    @Test
    void deleteLike_Failure_InvalidParameters() {
        // 调用被测试方法
        Result result = collectController.deleteLike(null, 1);

        // 验证结果
        assertNotNull(result);
    }

    // ==================== getWrong 方法测试 ====================
    @Test
    void getWrong_Success_WithData() {
        // 模拟依赖行为
        when(collectService.getWrong(1))
                .thenReturn(Arrays.asList(Arrays.asList("Wrong1"), Arrays.asList("Wrong2")));

        // 调用被测试方法
        Result result = collectController.getWrong(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(2, result.getTotal());
        List<?> wrongList = (List<?>) result.getData();
        assertEquals(2, wrongList.size());
    }

    @Test
    void getWrong_Success_NoData() {
        // 模拟依赖行为
        when(collectService.getWrong(1))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result result = collectController.getWrong(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<?> wrongList = (List<?>) result.getData();
        assertTrue(wrongList.isEmpty());
    }

    // ==================== getLike 方法测试 ====================
    @Test
    void getLike_Success_WithData() {
        // 模拟依赖行为
        when(collectService.getLike(1))
                .thenReturn(Arrays.asList(testProblem));
        // 调用被测试方法
        Result result = collectController.getLike(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Problem> likeList = (List<Problem>) result.getData();
        assertEquals(1, likeList.size());
        assertEquals(testProblem, likeList.get(0));
    }

    @Test
    void getLike_Success_NoData() {
        // 模拟依赖行为
        when(collectService.getLike(1))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result result = collectController.getLike(1);

        // 验证结果
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Problem> likeList = (List<Problem>) result.getData();
        assertTrue(likeList.isEmpty());
    }
}