package com.se.controller;

import com.se.dao.ProbDao;
import com.se.dao.StuProbExerDao;
import com.se.dto.Result;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;
import com.se.exception.checkException.LengthNotMatchException;
import com.se.exception.checkException.NotCheckFinishException;
import com.se.service.ExerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckControllerTest {

    @Mock
    private ExerService exerService;

    @Mock
    private StuProbExerDao stuProbExerDao;

    @Mock
    private ProbDao probDao;

    @InjectMocks
    private CheckController checkController;

    private Exercise testExercise;
    private User testUser;
    private Problem testProblem;
    private StuProbExer testStuProbExer;

    @BeforeEach
    void setUp() {
        testExercise = new Exercise();
        testExercise.setName("Test Exercise");

        testUser = new User();
        testUser.setUser_id(1);
        testUser.setName("Test User");

        testProblem = new Problem();

        testStuProbExer = new StuProbExer();
        testStuProbExer.setExer_id(1);
        testStuProbExer.setProb_id(1);
    }

    // ==================== getNotCheckedExercise 方法测试 ====================
    @Test
    void getNotCheckedExercise_Success_ExercisesExist() {
        // 准备测试数据
        int courseId = 1, userId = 1;

        // 模拟依赖行为
        when(exerService.getNotCheckedExercise(courseId, userId))
                .thenReturn(Arrays.asList(testExercise));

        // 调用被测试方法
        Result response = checkController.getNotCheckedExercise(courseId, userId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
        assertEquals(testExercise, exercises.get(0));
    }

    @Test
    void getNotCheckedExercise_Success_NoExercises() {
        // 准备测试数据
        int courseId = 1, userId = 1;

        // 模拟依赖行为
        when(exerService.getNotCheckedExercise(courseId, userId))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result response = checkController.getNotCheckedExercise(courseId, userId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
//    // ==================== getNotCheckedStu 方法测试 ====================
    @Test
    void getNotCheckedStu_Success_StudentsExist() {
        // 准备测试数据
        int exerId = 1;

        // 模拟依赖行为
        when(exerService.getNotCheckedStu(exerId))
                .thenReturn(Arrays.asList(testUser));

        // 调用被测试方法
        Result response = checkController.getNotCheckedStu(exerId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<User> students = (List<User>) result.getData();
        assertEquals(1, students.size());
        assertEquals(testUser, students.get(0));
    }

    @Test
    void getNotCheckedStu_Success_NoStudents() {
        // 准备测试数据
        int exerId = 1;

        // 模拟依赖行为
        when(exerService.getNotCheckedStu(exerId))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result response = checkController.getNotCheckedStu(exerId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<User> students = (List<User>) result.getData();
        assertTrue(students.isEmpty());
    }
//
//    // ==================== getCheckInfo 方法测试 ====================
    @Test
    void getCheckInfo_Success_ValidData() {
        // 准备测试数据
        int exerId = 1, userId = 1;

        // 模拟依赖行为
        when(stuProbExerDao.getProblemListByExerID(exerId))
                .thenReturn(Arrays.asList(testStuProbExer));
        when(probDao.getProblemById(1)).thenReturn(testProblem);
        when(stuProbExerDao.getInfoByUserIDAndProbIDAndExerID(userId, 1, exerId))
                .thenReturn(testStuProbExer);

        // 调用被测试方法
        Result response = checkController.getCheckInfo(exerId, userId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }

    @Test
    void getCheckInfo_Success_NoProblems() {
        // 准备测试数据
        int exerId = 1, userId = 1;

        // 模拟依赖行为
        when(stuProbExerDao.getProblemListByExerID(exerId))
                .thenReturn(Collections.emptyList());

        // 调用被测试方法
        Result response = checkController.getCheckInfo(exerId, userId);

        // 验证结果
        Result result = response;
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }
//
//
//    // ==================== getGradeAndRank 方法测试 ====================
//    @Test
//    void getGradeAndRank_Success_ValidData() {
//        // 准备测试数据
//        int exerId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getGradeAndRank(exerId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
//
//    @Test
//    void getGradeAndRank_Success_NoData() {
//        // 准备测试数据
//        int exerId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getGradeAndRank(exerId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
//
//    // ==================== getAccessRatio 方法测试 ====================
//    @Test
//    void getAccessRatio_Success_ValidData() {
//        // 准备测试数据
//        int exerId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getAccessRatio(exerId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
//
//    @Test
//    void getAccessRatio_Success_NoData() {
//        // 准备测试数据
//        int exerId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getAccessRatio(exerId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
//
//    // ==================== getHistory 方法测试 ====================
//    @Test
//    void getHistory_Success_ValidData() {
//        // 准备测试数据
//        int userId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getHistory(userId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
//
//    @Test
//    void getHistory_Success_NoData() {
//        // 准备测试数据
//        int userId = 1;
//
//        // 调用被测试方法
//        ResponseEntity<Result> response = checkController.getHistory(userId);
//
//        // 验证结果
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        Result result = response.getBody();
//        assertNotNull(result);
//        assertTrue(result.isSuccess());
//    }
}