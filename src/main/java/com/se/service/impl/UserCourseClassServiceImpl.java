package com.se.service.impl;

import com.se.constant.TeacherEntityConstant;
import com.se.constant.TutorEntityConstant;
import com.se.dao.CourseDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.UserCourseClass;
import com.se.entity.User;
import com.se.service.UserCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserCourseClassServiceImpl implements UserCourseClassService {
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    public List<User> getAdminListByCourse(Integer courseId) {
        List<User> list = getTeacherListByCourse(courseId);
        list.addAll(getTutorListByCourse(courseId));
        return list;
    }

    public List<User> getTeacherListByCourse(Integer course_id)
    {
        return getUserListByCourseAndIdentity(course_id, TeacherEntityConstant.IDENTITY_CODE);
    }

    public List<User> getTutorListByCourse(Integer course_id)
    {
        return getUserListByCourseAndIdentity(course_id, TutorEntityConstant.IDENTITY_CODE);
    }

    public List<User> getUserListByCourseAndIdentity(Integer course_id,Integer identity)
    {
        List<User>res = new ArrayList<>();
        List<UserCourseClass> userList =
                userCourseClassDao.getUserListByCourseIDAndIdentityCodeIgnoreClassID(
                        course_id, identity
                );
        for(UserCourseClass ucc: userList)
        {
            User u_i = userDao.getUserByID(ucc.getUser_id()).get(0);
            res.add(u_i);
        }
        return res;
    }

    public Boolean teacherInCourse(User user,Integer course_id)
    {
        List<User> teacherList = getTeacherListByCourse(course_id);
        for(User u : teacherList)
        {
            if(u.getUser_id().equals(user.getUser_id()))return true;
        }
        return false;
    }

    public Boolean tutorInCourse(User user, Integer course_id)
    {
        List<User> tutorList = getTutorListByCourse(course_id);
        for(User u : tutorList)
        {
            if(u.getUser_id().equals(user.getUser_id()))return true;
        }
        return false;
    }

    @Override
    public List<User> getAdminListByClass(Integer class_id) {
        List<User> userList = new ArrayList<>();
        List<UserCourseClass>userCourseClassList = userCourseClassDao.getUserListByClassID(class_id);
        for(UserCourseClass ucc : userCourseClassList)
        {
            if(ucc.getIdentity().equals(TeacherEntityConstant.IDENTITY_CODE)
            || ucc.getIdentity().equals(TutorEntityConstant.IDENTITY_CODE))
            {
                User user = userDao.getUserByID(ucc.getUser_id()).get(0);
                userList.add(user);
            }
        }
        return userList;
    }

    @Override
    public Boolean isCourseAndClassMatch(Integer course_id, Integer class_id) {
        List<UserCourseClass>userCourseClassList = userCourseClassDao.getCourseClassList(course_id,class_id);
        return !userCourseClassList.isEmpty();

    }

}
