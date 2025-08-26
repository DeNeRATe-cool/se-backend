package com.se.controller;

import com.se.dto.*;
import com.se.entity.Exercise;
import com.se.service.ExerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerControllerTest {

    @Mock
    private ExerService exerService;

    @InjectMocks
    private ExerController exerController;

    private Exercise testExercise;
    private ProbInExer testProbInExer;
    private CreateExerDTO testCreateExerDTO;
    private PushExerDTO testPushExerDTO;
    private SubmitExerciseDTO testSubmitDTO;
    private SaveExerciseDTO testSaveDTO;

    @BeforeEach
    void setUp() {
        testExercise = new Exercise();
        testExercise.setName("Test Exercise");
        
        testProbInExer = new ProbInExer();

        testCreateExerDTO = new CreateExerDTO();
        testCreateExerDTO.setCourse_id(1);
        testCreateExerDTO.setName("Test Exercise");
        
        testPushExerDTO = new PushExerDTO();
        testPushExerDTO.setExer_id(1);
        testPushExerDTO.setCourse_id(1);
        testPushExerDTO.setClass_id(1);
        
        testSubmitDTO = new SubmitExerciseDTO();
        testSubmitDTO.setExer_id(1);
        testSubmitDTO.setUser_id(1);
        
        testSaveDTO = new SaveExerciseDTO();
        testSaveDTO.setExer_id(1);
        testSaveDTO.setUser_id(1);
        testSaveDTO.setAnslist(Collections.singletonList("Answer"));
    }

    // ==================== getStuFinishExerNum 方法测试 ====================
    @Test
    void getStuFinishExerNum_Success() {
        when(exerService.getStuFinishExerNum(1, 1, 1))
            .thenReturn(Arrays.asList(1, 2, 3));
        Result result = exerController.getStuFinishExerNum(1, 1, 1);
        assertNotNull(result);
        assertEquals(3, result.getTotal());
        List<Integer> nums = (List<Integer>) result.getData();
        assertEquals(3, nums.size());
    }

    @Test
    void getStuFinishExerNum_Success_Empty() {
        when(exerService.getStuFinishExerNum(1, 1, 1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.getStuFinishExerNum(1, 1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Integer> nums = (List<Integer>) result.getData();
        assertTrue(nums.isEmpty());
    }

    // ==================== create 方法测试 ====================
    @Test
    void create_Success() {
        when(exerService.create(testCreateExerDTO)).thenReturn(testExercise);
        Result result = exerController.create(testCreateExerDTO);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void create_Failure() {
        when(exerService.create(testCreateExerDTO)).thenReturn(null);
        Result result = exerController.create(testCreateExerDTO);
        assertNotNull(result);
        assertNull(result.getData());
    }
//
    // ==================== push 方法测试 ====================
    @Test
    void push_Success() {
        doNothing().when(exerService).push(testPushExerDTO);
        Result result = exerController.push(testPushExerDTO);
        assertNotNull(result);
    }

    // ==================== info 方法测试 ====================
    @Test
    void info_Success() {
        when(exerService.info(1)).thenReturn(testExercise);
        Result result = exerController.info(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void info_Failure() {
        when(exerService.info(1)).thenReturn(null);
        Result result = exerController.info(1);
        assertNotNull(result);
        assertNull(result.getData());
    }
//
    // ==================== listProblemByExerId 方法测试 ====================
    @Test
    void listProblemByExerId_Success() {
        when(exerService.listProblemByExerId(1))
            .thenReturn(Collections.singletonList(testProbInExer));
        Result result = exerController.listProblemByExerId(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<ProbInExer> problems = (List<ProbInExer>) result.getData();
        assertEquals(1, problems.size());
    }

    @Test
    void listProblemByExerId_Failure() {
        when(exerService.listProblemByExerId(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listProblemByExerId(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<ProbInExer> problems = (List<ProbInExer>) result.getData();
        assertTrue(problems.isEmpty());
    }
//
    // ==================== listExerByCourseAndClass 方法测试 ====================
    @Test
    void listExerByCourseAndClass_Success() {
        when(exerService.listExerByCourseAndClass(1, 1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listExerByCourseAndClass(1, 1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }

    @Test
    void listExerByCourseAndClass_Failure() {
        when(exerService.listExerByCourseAndClass(1, 1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listExerByCourseAndClass(1, 1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== listExerByStuId 方法测试 ====================
    @Test
    void listExerByStuId_Success() {
        when(exerService.listExerByStuId(1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listExerByStuId(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }

    @Test
    void listExerByStuId_Failure() {
        when(exerService.listExerByStuId(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listExerByStuId(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== listDoneExerByStuId 方法测试 ====================
    @Test
    void listDoneExerByStuId_Success() {
        when(exerService.listDoneExerByStuId(1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listDoneExerByStuId(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }

    @Test
    void listDoneExerByStuId_Failure() {
        when(exerService.listDoneExerByStuId(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listDoneExerByStuId(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== listToDoExerByStuId 方法测试 ====================
    @Test
    void listToDoExerByStuId_Success() {
        when(exerService.listToDoExerByStuId(1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listToDoExerByStuId(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }


    @Test
    void listToDoExerByStuId_Failure() {
        when(exerService.listToDoExerByStuId(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listToDoExerByStuId(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== listPublicExerByCourse 方法测试 ====================
    @Test
    void listPublicExerByCourse_Success() {
        when(exerService.listPublicExerByCourse(1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listPublicExerByCourse(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }

    @Test
    void listPublicExerByCourse_Failure() {
        when(exerService.listPublicExerByCourse(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listPublicExerByCourse(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== listSelfCreateExer 方法测试 ====================
    @Test
    void listSelfCreateExer_Success() {
        when(exerService.listSelfCreateExer(1))
            .thenReturn(Collections.singletonList(testExercise));
        Result result = exerController.listSelfCreateExer(1);
        assertNotNull(result);
        assertEquals(1, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertEquals(1, exercises.size());
    }

    @Test
    void listSelfCreateExer_Failure() {
        when(exerService.listSelfCreateExer(1))
            .thenReturn(Collections.emptyList());
        Result result = exerController.listSelfCreateExer(1);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        List<Exercise> exercises = (List<Exercise>) result.getData();
        assertTrue(exercises.isEmpty());
    }
//
    // ==================== submit 方法测试 ====================
    @Test
    void submit_Success() {
        doNothing().when(exerService).submit(1, 1);
        Result result = exerController.submit(testSubmitDTO);
        assertNotNull(result);
    }

    // ==================== save 方法测试 ====================
    @Test
    void save_Success() {
        doNothing().when(exerService).save(1, 1, Collections.singletonList("Answer"));
        Result result = exerController.save(testSaveDTO);
        assertNotNull(result);
    }

    // ==================== countFinsh 方法测试 ====================
    @Test
    void countFinsh_Success() {
        when(exerService.countFinish(1)).thenReturn(10);
        Result result = exerController.countFinsh(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void countFinsh_Zero() {
        when(exerService.countFinish(1)).thenReturn(0);
        Result result = exerController.countFinsh(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    // ==================== generateExerciseReport 方法测试 ====================
    @Test
    void generateExerciseReport_Success() throws IOException {
        when(exerService.generateExerciseReport(1)).thenReturn("http://example.com/report");
        Result result = exerController.generateExerciseReport(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }
//
//
    // ==================== checkFinish 方法测试 ====================
    @Test
    void checkFinish_Success() {
        List<List<Object>> userLists = new ArrayList<>();
        userLists.add(Collections.singletonList(new Object()));
        Result result = exerController.checkFinish(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }

    @Test
    void checkFinish_Empty() {
        when(exerService.checkFinish(1)).thenReturn(Collections.emptyList());
        Result result = exerController.checkFinish(1);
        assertNotNull(result);
        assertNotNull(result.getData());
    }
}