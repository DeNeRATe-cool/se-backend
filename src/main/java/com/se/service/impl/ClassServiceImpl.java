package com.se.service.impl;

import com.se.constant.*;
import com.se.dao.ClassDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.AddAdminInClassDTO;
import com.se.dto.UserCourseClass;
import com.se.entity.Class;
import com.se.entity.User;
import com.se.exception.classException.ClassNotExistException;
import com.se.exception.classException.DuplicateClassException;
import com.se.exception.courseException.CourseClassNotMatchException;
import com.se.exception.courseException.UserNotInCourseException;
import com.se.exception.userException.UserNotFoundException;
import com.se.exception.userException.UserPermissionException;
import com.se.service.ClassService;
import com.se.service.CourseService;
import com.se.service.UserCourseClassService;
import com.se.utils.FakeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassDao classDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCourseClassService userCourseClassService;

    @Override
    public void add(Class classEntity) {
        if(!classDao.getClassEntityByName(classEntity.getName()).isEmpty())
        {
            throw new DuplicateClassException(ClassEntityConstant.CLASS_NAME_EXISTS);
        }
        String code = FakeUtils.generateCode(ClassEntityConstant.CLASS_CODE_PLACE_NUM);
        while(!classDao.getClassEntityByClassCode(code).isEmpty())
        {
            code = FakeUtils.generateCode(ClassEntityConstant.CLASS_CODE_PLACE_NUM);
        }
        classEntity.setClass_code(code);
        classDao.add(classEntity);
    }

    /**
     * 前提： 该 老师 / 助教属于课程
     * 为老师 / 助教 指定 班级
     * @param addAdminInClassDTO
     * @return
     */
    @Override
    public List<User> addAdmin(AddAdminInClassDTO addAdminInClassDTO) {
        Integer add_user_id = addAdminInClassDTO.getUser_id();
        Integer add_course_id = addAdminInClassDTO.getCourse_id();
        Integer add_class_id = addAdminInClassDTO.getClass_id();
        if(!userCourseClassService.isCourseAndClassMatch(add_course_id,add_class_id))
        {
            throw new CourseClassNotMatchException(CourseEntityConstant.COURSE_CLASS_NOT_MATCH);
        }
        List<User> userList = userDao.getUserByID(add_user_id);
        // 找不到用户
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        User add_user = userList.get(0);

        // course_id + identity
        // 老师和助教必须先成为课程助教
        if(add_user.getIdentity().equals(TeacherEntityConstant.IDENTITY_CODE))
        {
            if(!userCourseClassService.teacherInCourse(add_user, addAdminInClassDTO.getCourse_id()))
            {
                throw new UserNotInCourseException(CourseEntityConstant.TEACHER_NOT_IN_COURSE);
            }
        }

        if(add_user.getIdentity().equals(StudentEntityConstant.IDENTITY_CODE)
            && !userCourseClassService.tutorInCourse(add_user,addAdminInClassDTO.getCourse_id()))
        {
            throw new UserNotInCourseException(CourseEntityConstant.TUTOR_NOT_IN_COURSE);
        }

        boolean isTeacher = add_user.getIdentity().equals(TeacherEntityConstant.IDENTITY_CODE);
        Integer code = isTeacher ? TeacherEntityConstant.IDENTITY_CODE : TutorEntityConstant.IDENTITY_CODE;

        List<UserCourseClass>userCourseClassList = userCourseClassDao.select(addAdminInClassDTO.getUser_id(),
                addAdminInClassDTO.getCourse_id(),
                addAdminInClassDTO.getClass_id(),
                code);
        if(!userCourseClassList.isEmpty())
        {
            throw new DuplicateClassException(UserEntityConstant.DUPLICATE_JOIN_CLASS);
        }

        userCourseClassDao.add(addAdminInClassDTO.getUser_id(),
                addAdminInClassDTO.getCourse_id(),
                addAdminInClassDTO.getClass_id(),
                code);

        List<User>res = userCourseClassService.getAdminListByClass(addAdminInClassDTO.getClass_id());
        return res;
    }

    @Override
    public List<User> listTeacherAndTutor(Integer classId) {
        if(classDao.getClassEntityByClassId(classId).isEmpty())
        {
            throw new ClassNotExistException(ClassEntityConstant.CLASS_NOT_EXISTS);
        }
        return userCourseClassService.getAdminListByClass(classId);
    }

    @Override
    public List<Class> list() {
        return classDao.list();
    }
}
