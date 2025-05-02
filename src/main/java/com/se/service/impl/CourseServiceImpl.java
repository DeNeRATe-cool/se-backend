package com.se.service.impl;

import com.se.constant.*;
import com.se.dao.CourseDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.AddAdminInCourseDTO;
import com.se.dto.UserCourseClass;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.exception.ParamIllegalException;
import com.se.exception.courseException.CourseNotFoundException;
import com.se.exception.courseException.DuplicateCourseException;
import com.se.exception.courseException.DuplicateInvitationException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.CourseService;
import com.se.service.UserCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    @Autowired
    private UserCourseClassService userCourseClassService;

    public void add(Course course)
    {
        List<Course> res = courseDao.getCourseByName(course.getName());
        if(res.size()!=0)throw new DuplicateCourseException("课程名已经存在");
        courseDao.add(course);
    }

    /**
     * 在 t_user_course_class 表添加记录
     * class_id = -1
     * 异常情况：
     *  已经是该课程 老师 / 助教
     *  已经是该课程 学生
     * @param addAdminDTO
     *  username + course_id
     * @return
     */
    @Override
    public List<User> addAdmin(AddAdminInCourseDTO addAdminDTO) {
        /*
        * 由 username 得到 user_id
        * */
        List<User> userList = userDao.getUserByUsername(addAdminDTO.getUsername());
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USERNAME_NOT_MATCH);
        }
        User u = userList.get(0);
        /*
        * 检查是不是该课程老师或者助教
        * 或者是不是选修该课程的学生
        * class_id = -1
        *
        * */
        List<UserCourseClass> uccList = userCourseClassDao.getUserListByCourseID(addAdminDTO.getCourse_id());
        for(UserCourseClass i:uccList)
        {
            if(Objects.equals(i.getUser_id(), u.getUser_id()))
            {
                if(Objects.equals(u.getIdentity(), TeacherEntityConstant.IDENTITY_CODE))
                {
                    throw new DuplicateInvitationException(CourseEntityConstant.DUPLICATE_TEACHER_INVITED);
                }
                else if(Objects.equals(u.getIdentity(), StudentEntityConstant.IDENTITY_CODE))
                {
                    throw new DuplicateInvitationException(CourseEntityConstant.DUPLICATE_STUDENT_INVITED);
                }
                else
                {
                    throw new DuplicateInvitationException(CourseEntityConstant.DUPLICATE_TUTOR_INVITED);
                }
            }
        }
        Integer identity_code = Objects.equals(TeacherEntityConstant.IDENTITY_CODE,u.getIdentity()) ?
                TeacherEntityConstant.IDENTITY_CODE : TutorEntityConstant.IDENTITY_CODE;
        // 添加成功
        userCourseClassDao.add(u.getUser_id(), addAdminDTO.getCourse_id(), -1,identity_code);

        // 返回全体老师助教列表
        // 课程 老师列表
        List<UserCourseClass> teacherRecList =
                userCourseClassDao.getUserListByCourseIDAndIdentityCodeIgnoreClassID(
                        addAdminDTO.getCourse_id(), TeacherEntityConstant.IDENTITY_CODE
                );
        // 课程 助教列表
        List<UserCourseClass> tutorRecList =
                userCourseClassDao.getUserListByCourseIDAndIdentityCodeIgnoreClassID(
                        addAdminDTO.getCourse_id(), TutorEntityConstant.IDENTITY_CODE
                );

        teacherRecList.addAll(tutorRecList);

        List<User> res = new ArrayList<>();

        for(UserCourseClass ucc: teacherRecList)
        {
            User u_i = userDao.getUserByID(ucc.getUser_id()).get(0);
            res.add(u_i);
        }
        return res;
    }

    @Override
    public List<User> listTeacherAndTutor(Integer courseId) {
        if(courseDao.getByID(courseId).isEmpty())
        {
            throw new CourseNotFoundException(CourseEntityConstant.COURSE_NOT_FOUND);
        }
        return userCourseClassService.getAdminListByCourse(courseId);
    }

    @Override
    public Course courseInfo(Integer courseId) {
        if(courseId <= 0)
        {
            throw new ParamIllegalException();
        }
        List<Course> courseList = courseDao.getByID(courseId);
        if(courseList.isEmpty())
        {
            throw new CourseNotFoundException(CourseEntityConstant.COURSE_NOT_FOUND);
        }
        return courseList.get(0);
    }

}
