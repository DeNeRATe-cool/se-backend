package com.se.service.impl;

import com.se.constant.CourseEntityConstant;
import com.se.constant.ExerEntityConstant;
import com.se.dao.*;
import com.se.dto.CreateExerDTO;
import com.se.dto.PushExerDTO;
import com.se.dto.StuProbExer;
import com.se.entity.Class;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;
import com.se.exception.ParamIllegalException;
import com.se.exception.courseException.CourseClassNotMatchException;
import com.se.exception.courseException.UserNotInCourseException;
import com.se.exception.exerciseException.ExerciseNotFinishException;
import com.se.service.UserCourseClassService;
import com.se.service.StuProbExerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerServiceImplTest {

    @Mock
    private UserDao userDao;

    @Mock
    private StuDao stuDao;

    @Mock
    private ExerDao exerDao;

    @Mock
    private StuProbExerDao stuProbExerDao;

    @Mock
    private UserCourseClassDao userCourseClassDao;

    @Mock
    private UserCourseClassService userCourseClassService;

    @Mock
    private StuProbExerService stuProbExerService;

    @Mock
    private ProbDao probDao;

    @InjectMocks
    private ExerServiceImpl exerService;

    private User testUser;
    private Exercise testExercise;
    private CreateExerDTO createExerDTO;
    private PushExerDTO pushExerDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUser_id(1);
        testUser.setIdentity(0); // 学生身份

        testExercise = new Exercise();
        testExercise.setExer_id(1);
        testExercise.setCourse_id(1);
        testExercise.setClass_id(1);
        testExercise.setCreator_id(1);
        testExercise.setBegin_time(new Date());
        testExercise.setEnd_time(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)); // 7天后
        testExercise.setName("Test Exercise");

        createExerDTO = new CreateExerDTO();
        createExerDTO.setCourse_id(1);
        createExerDTO.setCreator_id(1);
        createExerDTO.setBegin_time(new DateTime());
        createExerDTO.setEnd_time(new DateTime(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        createExerDTO.setIs_public(true);
        createExerDTO.setName("New Exercise");
        createExerDTO.setIs_multi(false);
        createExerDTO.setProbs(Collections.singletonList(101));
        createExerDTO.setScores(Collections.singletonList(10));

        pushExerDTO = new PushExerDTO();
        pushExerDTO.setExer_id(1);
        pushExerDTO.setCourse_id(1);
        pushExerDTO.setClass_id(1);
        pushExerDTO.setCreator_id(1);
        pushExerDTO.setBegin_time(new DateTime());
        pushExerDTO.setEnd_time(new DateTime(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000));
        pushExerDTO.setIs_multi(false);
        pushExerDTO.setName("Pushed Exercise");
        pushExerDTO.setIs_every_class(false);
    }

    // ==================== getStuFinishExerNum 方法测试 ====================
    
    @Test
    void getStuFinishExerNum_Success_WhenStudentInCourse() {
        // 准备测试数据
        int userId = 1, courseId = 1, classId = 1;
        
        // 模拟依赖行为
        when(userCourseClassService.studentInCourse(userId, courseId)).thenReturn(true);
        when(exerDao.getExerByStuIDAndExerID(classId, courseId)).thenReturn(Collections.singletonList(1));
        
        // 创建StuProbExer对象
        StuProbExer stuProbExer = new StuProbExer();
        stuProbExer.setStu_id(userId);
        stuProbExer.setExer_id(1);
        stuProbExer.setIs_finish(true);
        when(stuProbExerDao.getStuExerByUserIDAndExerID(userId, 1)).thenReturn(Collections.singletonList(stuProbExer));
        
        // 调用被测试方法
        List<Integer> result = exerService.getStuFinishExerNum(userId, courseId, classId);
        
        // 验证结果
        assertNotNull(result);
        assertEquals(1, result.get(0)); // 完成数
        assertEquals(1, result.get(1)); // 总任务数
    }

    @Test
    void getStuFinishExerNum_Failure_WhenStudentNotInCourse() {
        // 准备测试数据
        int userId = 1, courseId = 1, classId = 1;
        
        // 模拟依赖行为
        when(userCourseClassService.studentInCourse(userId, courseId)).thenReturn(false);
        
        // 验证异常
        assertThrows(ParamIllegalException.class, () -> {
            exerService.getStuFinishExerNum(userId, courseId, classId);
        });
    }

//    // ==================== submit 方法测试 ====================
//
    @Test
    void submit_Success_WhenExerciseFinished() {
        // 准备测试数据
        int exerId = 1, userId = 1;

        // 模拟依赖行为
        doNothing().when(userCourseClassService).checkIsStudent(userId);
        when(exerDao.getExerById(exerId)).thenReturn(testExercise);
        when(userCourseClassService.studentInCourse(userId, 1)).thenReturn(true);
        when(stuProbExerService.checkStudentFinishExercise(exerId, userId)).thenReturn(true);

        // 调用被测试方法
        exerService.submit(exerId, userId);

        // 验证依赖调用
        verify(stuProbExerService).setFinishedByExerIdAndUserId(exerId, userId);
    }

    @Test
    void submit_Failure_WhenExerciseNotFinished() {
        // 准备测试数据
        int exerId = 1, userId = 1;

        // 模拟依赖行为
        doNothing().when(userCourseClassService).checkIsStudent(userId);
        when(exerDao.getExerById(exerId)).thenReturn(testExercise);
        when(userCourseClassService.studentInCourse(userId, 1)).thenReturn(true);
        when(stuProbExerService.checkStudentFinishExercise(exerId, userId)).thenReturn(false);

        // 验证异常
        assertThrows(ExerciseNotFinishException.class, () -> {
            exerService.submit(exerId, userId);
        });
    }
//
//    // ==================== info 方法测试 ====================
//
    @Test
    void info_Success_WhenExerciseExists() {
        // 准备测试数据
        int exerId = 1;

        // 模拟依赖行为
        when(exerDao.getExerById(exerId)).thenReturn(testExercise);

        // 调用被测试方法
        Exercise result = exerService.info(exerId);

        // 验证结果
        assertNotNull(result);
        assertEquals(exerId, result.getExer_id());
    }

    @Test
    void info_Failure_WhenExerciseNotExists() {
        // 准备测试数据
        int exerId = 999;

        // 模拟依赖行为
        when(exerDao.getExerById(exerId)).thenReturn(null);

        // 验证异常
        assertThrows(ParamIllegalException.class, () -> {
            exerService.info(exerId);
        });
    }
}