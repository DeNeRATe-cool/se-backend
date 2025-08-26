package com.se.service.impl;

import com.se.dao.ProbDao;
import com.se.dto.CreateProbDTO;
import com.se.entity.Problem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProblemServiceImplTest {

    @Mock
    private ProbDao probDao;

    @InjectMocks
    private ProblemServiceImpl problemService;

    private Problem testProblem;
    private CreateProbDTO createProbDTO;

    @BeforeEach
    void setUp() {
        testProblem = new Problem();
        testProblem.setProb_id(1);
        testProblem.setType(1);
        testProblem.setAnswer("Test Answer");
        testProblem.setStr_content(Arrays.asList("Content 1", "Content 2"));
        testProblem.setCreate_time(new Date());

        createProbDTO = new CreateProbDTO();
        createProbDTO.setType(1);
        createProbDTO.setAnswer("New Answer");
        createProbDTO.setStr_content("aaa");
    }

    // ==================== PublicProb 方法测试 ====================

    @Test
    void PublicProb_Success_WithType() {
        // 准备测试数据
        int type = 1;

        // 模拟依赖行为
        when(probDao.getProbByType(type)).thenReturn(Collections.singletonList(testProblem));

        // 调用被测试方法
        List<Problem> result = problemService.PublicProb(type);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProblem, result.get(0));
    }

    @Test
    void PublicProb_Success_WithoutType() {
        // 准备测试数据
        Integer type = null;

        // 模拟依赖行为
        when(probDao.getAllPublicProb()).thenReturn(Collections.singletonList(testProblem));

        // 调用被测试方法
        List<Problem> result = problemService.PublicProb(type);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProblem, result.get(0));
    }

    // ==================== createProb 方法测试 ====================

    @Test
    void createProb_Success_ValidProblem() {
        // 准备测试数据
        Problem problem = new Problem();
        problem.setType(1);
        problem.setAnswer("New Answer");
        problem.setStr_content(Arrays.asList("Line 1", "Line 2"));

        // 模拟依赖行为
        when(probDao.createProb(any(Problem.class))).thenReturn(1);

        // 调用被测试方法
        Problem result = problemService.createProb(problem);

        // 验证结果
        assertNotNull(result);
        assertNotNull(result.getCreate_time());
        assertEquals("[\"Line 1\",\"Line 2\"]", result.getContent());

        // 验证依赖调用
        verify(probDao).createProb(any(Problem.class));
    }

    @Test
    void createProb_Success_EmptyContent() {
        // 准备测试数据
        Problem problem = new Problem();
        problem.setType(1);
        problem.setAnswer("New Answer");
        problem.setStr_content(Collections.emptyList());

        // 模拟依赖行为
        when(probDao.createProb(any(Problem.class))).thenReturn(1);

        // 调用被测试方法
        Problem result = problemService.createProb(problem);

        // 验证结果
        assertNotNull(result);
        assertEquals("[]", result.getContent());
    }

    // ==================== SelfProb 方法测试 ====================

    @Test
    void SelfProb_Success_WithType() {
        // 准备测试数据
        int userId = 1;
        int type = 1;

        // 模拟依赖行为
        when(probDao.getSelfProb(userId, type)).thenReturn(Collections.singletonList(testProblem));

        // 调用被测试方法
        List<Problem> result = problemService.SelfProb(userId, type);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProblem, result.get(0));
    }

    @Test
    void SelfProb_Success_WithoutType() {
        // 准备测试数据
        int userId = 1;
        Integer type = null;

        // 模拟依赖行为
        when(probDao.getSelfAllProb(userId)).thenReturn(Collections.singletonList(testProblem));

        // 调用被测试方法
        List<Problem> result = problemService.SelfProb(userId, type);

        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testProblem, result.get(0));
    }

    @Test
    void SelfProb_Success_NoProblems() {
        // 准备测试数据
        int userId = 1;
        int type = 1;

        // 模拟依赖行为
        when(probDao.getSelfProb(userId, type)).thenReturn(Collections.emptyList());

        // 调用被测试方法
        List<Problem> result = problemService.SelfProb(userId, type);

        // 验证结果
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // ==================== getProb 方法测试 ====================

    @Test
    void getProb_Success_ProblemExists() {
        // 准备测试数据
        int probId = 1;

        // 模拟依赖行为
        when(probDao.getProblemById(probId)).thenReturn(testProblem);

        // 调用被测试方法
        Problem result = problemService.getProb(probId);

        // 验证结果
        assertNotNull(result);
        assertEquals(probId, result.getProb_id());
    }

    @Test
    void getProb_Success_ProblemNotExists() {
        // 准备测试数据
        int probId = 999;

        // 模拟依赖行为
        when(probDao.getProblemById(probId)).thenReturn(null);

        // 调用被测试方法
        Problem result = problemService.getProb(probId);

        // 验证结果
        assertNull(result);
    }
}