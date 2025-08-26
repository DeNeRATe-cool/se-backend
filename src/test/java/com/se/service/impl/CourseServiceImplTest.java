package com.se.service.impl;

import com.se.constant.CourseEntityConstant;
import com.se.constant.StudentEntityConstant;
import com.se.constant.TeacherEntityConstant;
import com.se.constant.TutorEntityConstant;
import com.se.constant.UserEntityConstant;
import com.se.dao.CourseDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.AddAdminInCourseDTO;
import com.se.dto.UserCourseClass;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.exception.courseException.CourseNotFoundException;
import com.se.exception.courseException.DuplicateCourseException;
import com.se.exception.courseException.DuplicateInvitationException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.UserCourseClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // 放宽严格模式
class CourseServiceImplTest {

    @Mock
    private CourseDao courseDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserCourseClassDao userCourseClassDao;

    @Mock
    private UserCourseClassService userCourseClassService;

    @InjectMocks
    private CourseServiceImpl courseService;

    // ==================== add 方法测试 ====================
    
    @Test
    void add_Success_WhenCourseNameNotExists() {
        // 准备测试数据
        Course course = new Course();
        course.setName("New Course");
        course.setCreator_id(1);
        course.setCourse_id(1);
        // 模拟依赖行为
        when(courseDao.getCourseByName("New Course")).thenReturn(Collections.emptyList());
        when(courseDao.getByID(anyInt())).thenReturn(Collections.singletonList(course));
        
        // 调用被测试方法
        Course result = courseService.add(course);
        
        // 验证结果
        assertNotNull(result);
        assertEquals("New Course", result.getName());
        
        // 验证依赖调用
        verify(courseDao).getCourseByName("New Course");
        verify(courseDao).add(course);
        verify(userCourseClassService).insert(eq(1), anyInt(), eq(-1), eq(TeacherEntityConstant.IDENTITY_CODE));
        verify(courseDao).getByID(anyInt());
    }

    @Test
    void add_Failure_WhenCourseNameExists() {
        // 准备测试数据
        Course course = new Course();
        course.setName("Existing Course");

        // 模拟依赖行为
        when(courseDao.getCourseByName("Existing Course")).thenReturn(Collections.singletonList(new Course()));

        // 验证异常
        assertThrows(DuplicateCourseException.class, () -> {
            courseService.add(course);
        });

        // 验证未调用添加方法
        verify(courseDao, never()).add(any());
    }
//

//
    // ==================== courseInfo 方法测试 ====================

    @Test
    void courseInfo_Success_WhenCourseExists() {
        // 准备测试数据
        int courseId = 1;
        Course expectedCourse = new Course();
        expectedCourse.setCourse_id(courseId);

        // 模拟依赖行为
        when(courseDao.getByID(courseId)).thenReturn(Collections.singletonList(expectedCourse));

        // 调用被测试方法
        Course result = courseService.courseInfo(courseId);

        // 验证结果
        assertNotNull(result);
        assertEquals(courseId, result.getCourse_id());
    }

    @Test
    void courseInfo_Failure_WhenCourseNotExists() {
        // 准备测试数据
        int courseId = 1;

        // 模拟依赖行为
        when(courseDao.getByID(courseId)).thenReturn(Collections.emptyList());

        // 验证异常
        assertThrows(CourseNotFoundException.class, () -> {
            courseService.courseInfo(courseId);
        });
    }
}