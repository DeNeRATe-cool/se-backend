package com.se.service.impl;

import com.se.constant.ClassEntityConstant;
import com.se.constant.TeacherEntityConstant;
import com.se.dao.ClassDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.AddAdminInClassDTO;
import com.se.entity.Class;
import com.se.entity.User;
import com.se.exception.classException.ClassNotExistException;
import com.se.exception.classException.DuplicateClassException;
import com.se.exception.courseException.CourseClassNotMatchException;
import com.se.service.UserCourseClassService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassServiceImplTest {

    @InjectMocks
    private ClassServiceImpl classServiceImpl;

    @Mock
    private ClassDao classDao;

    @Mock
    private UserCourseClassDao userCourseClassDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserCourseClassService userCourseClassService;

    private Class classEntity;

    @BeforeEach
    void setup()
    {
        classEntity = new Class();
        classEntity.setName("TestClass");
        classEntity.setCourse_id(1);
        classEntity.setClass_id(1);
    }

    @Test
    void add_success()
    {
        Class classEntity = new Class();
        classEntity.setName("Math101");
        classEntity.setCourse_id(1);

        when(classDao.getClassEntityByName("Math101"))
                .thenReturn(Collections.emptyList());
        when(classDao.getClassEntityByClassCode(anyString()))
                .thenReturn(Collections.emptyList());

        Class result = classServiceImpl.add(classEntity);

        assertNotNull(result.getClass_code());
        verify(classDao).add(classEntity);
    }

    @Test
    void add_failure()
    {
        Class classEntity = new Class();
        classEntity.setName("Math101");

        when(classDao.getClassEntityByName("Math101"))
                .thenReturn(Collections.singletonList(new Class()));

        assertThrows(DuplicateClassException.class, () -> classServiceImpl.add(classEntity));

        verify(classDao, never()).add(any());
        verify(userCourseClassService, never()).insert(anyInt(),anyInt(),anyInt(),anyInt());
    }

    @Test
    void addAdmin_Success_AddTeacher() {
        // 准备测试数据
        AddAdminInClassDTO dto = new AddAdminInClassDTO();
        dto.setUser_id(1);
        dto.setCourse_id(1);
        dto.setClass_id(1);

        // 创建老师用户
        User teacher = new User();
        teacher.setUser_id(1);
        teacher.setIdentity(TeacherEntityConstant.IDENTITY_CODE); // 老师身份

        // 模拟依赖行为
        when(userCourseClassService.isCourseAndClassMatch(1, 1)).thenReturn(true);
        when(userDao.getUserByID(1)).thenReturn(Collections.singletonList(teacher));
        when(userCourseClassService.teacherInCourse(teacher, 1)).thenReturn(true);
        when(userCourseClassDao.select(1, 1, 1)).thenReturn(Collections.emptyList());
        // 模拟返回的管理员列表
        List<User> expectedAdminList = Collections.singletonList(teacher);
        when(userCourseClassService.getAdminListByClass(1)).thenReturn(expectedAdminList);

        // 调用被测试方法
       List<User> result = classServiceImpl.addAdmin(dto);

//         验证结果
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(teacher, result.get(0));

//         验证依赖调用
        verify(userCourseClassDao).add(1, 1, 1, TeacherEntityConstant.IDENTITY_CODE); // 验证添加管理员
        verify(userCourseClassService).getAdminListByClass(1); // 验证获取管理员列表
    }

    @Test
    void addAdmin_Failure_CourseAndClassNotMatch()
    {
        AddAdminInClassDTO dto = new AddAdminInClassDTO();
        dto.setUser_id(1);
        dto.setCourse_id(1);
        dto.setClass_id(1);

        when(userCourseClassService.isCourseAndClassMatch(1, 1)).thenReturn(false);

        assertThrows(CourseClassNotMatchException.class, () -> classServiceImpl.addAdmin(dto));
    }

    @Test
    void listTeacherAndTutor_Success()
    {
        when(classDao.getClassEntityByClassId(anyInt())).thenReturn(Collections.singletonList(classEntity));

        List<User> res = classServiceImpl.listTeacherAndTutor(1);
        assertNotNull(res);
        verify(userCourseClassService).getAdminListByClass(1);
    }

    @Test
    void listTeacherAndTutor_Failure_ClassNotExists()
    {
        when(classDao.getClassEntityByClassId(anyInt())).thenReturn(Collections.emptyList());

        assertThrows(ClassNotExistException.class, () -> classServiceImpl.listTeacherAndTutor(1));
    }
}