package com.se.service.impl;

import com.se.constant.StudentEntityConstant;
import com.se.constant.TeacherEntityConstant;
import com.se.dao.StuDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.Result;
import com.se.dto.UserCourseClass;
import com.se.entity.User;
import com.se.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuDao stuDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    @Override
    public Result getAllStudent() {
        List<User> students = stuDao.getAllStudent();
        return Result.ok(students, Integer.valueOf(students.size()));
    }

    @Override
    public List<User> listByCourseID(Integer courseId) {
        Integer code = StudentEntityConstant.IDENTITY_CODE;
        List<UserCourseClass> userCourseClassList = userCourseClassDao.getStudentClassList(courseId,code);
        List<User> res = new ArrayList<>();
        for(UserCourseClass userCourseClass : userCourseClassList)
        {
            res.addAll(userDao.getUserByID(userCourseClass.getUser_id()));
        }
        return res;
    }

    @Override
    public List<User> listByCondition(Map<String, String> params) {
        String username = params.get("username");
        String mail = params.get("mail");
        String name = params.get("name");
        Integer code = StudentEntityConstant.IDENTITY_CODE;
        List<User> res = userDao.getUserByCondition(code,username,mail,name);
        return res;
    }
}
