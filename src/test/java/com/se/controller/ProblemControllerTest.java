package com.se.controller;

import com.se.dto.Result;
import com.se.entity.Problem;
import com.se.service.ProblemService;
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
class ProblemControllerTest {

    @Mock
    private ProblemService problemService;

    @InjectMocks
    private ProblemController problemController;

    private Problem testProblem;

    @BeforeEach
    void setUp() {
        testProblem = new Problem();
    }

    // ==================== GetProbInfo 方法测试 ====================
    @Test
    void GetProbInfo_Success() {
        when(problemService.getProb(1)).thenReturn(testProblem);
        Result result = problemController.GetProbInfo(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void GetProbInfo_Failure() {
        when(problemService.getProb(1)).thenReturn(null);
        Result result = problemController.GetProbInfo(1);
        assertNotNull(result);
        assertNull(result.getData());
    }

    // ==================== GetPublicProb 方法测试 ====================
    @Test
    void GetPublicProb_Success_WithType() {
        when(problemService.PublicProb(1)).thenReturn(Collections.singletonList(testProblem));
        Result result = problemController.GetPublicProb(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertEquals(1, problems.size());
    }

    @Test
    void GetPublicProb_Success_WithoutType() {
        when(problemService.PublicProb(null)).thenReturn(Collections.singletonList(testProblem));
        Result result = problemController.GetPublicProb(null);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertEquals(1, problems.size());
    }

    @Test
    void GetPublicProb_Success_Empty() {
        when(problemService.PublicProb(1)).thenReturn(Collections.emptyList());
        Result result = problemController.GetPublicProb(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertTrue(problems.isEmpty());
    }

    // ==================== CreateProb 方法测试 ====================
    @Test
    void CreateProb_Success() {
        when(problemService.createProb(any(Problem.class))).thenReturn(testProblem);
        Result result = problemController.CreateProb(testProblem);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void CreateProb_Failure() {
        when(problemService.createProb(any(Problem.class))).thenReturn(null);
        Result result = problemController.CreateProb(testProblem);
        assertNotNull(result);
        assertNull(result.getData());
    }

    // ==================== GetselfProb 方法测试 ====================
    @Test
    void GetselfProb_Success_WithType() {
        when(problemService.SelfProb(1, 1)).thenReturn(Collections.singletonList(testProblem));
        Result result = problemController.GetselfProb(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertEquals(1, problems.size());
    }

    @Test
    void GetselfProb_Success_WithoutType() {
        when(problemService.SelfProb(1, null)).thenReturn(Collections.singletonList(testProblem));
        Result result = problemController.GetselfProb(1, null);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertEquals(1, problems.size());
    }

    @Test
    void GetselfProb_Success_Empty() {
        when(problemService.SelfProb(1, 1)).thenReturn(Collections.emptyList());
        Result result = problemController.GetselfProb(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Problem> problems = (List<Problem>) result.getData();
        assertTrue(problems.isEmpty());
    }
}