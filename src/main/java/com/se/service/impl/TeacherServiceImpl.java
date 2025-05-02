package com.se.service.impl;

import com.se.constant.TeacherEntityConstant;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.UserCourseClass;
import com.se.entity.User;
import com.se.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    /**
     * 根据课程id 在 user_course_class 表中得到userid list
     * 根据userid 诸葛从user表中查找 user identity
     * @param courseId
     */
    @Override
    public List<User> listByCourse(int courseId) {
        Integer code = TeacherEntityConstant.IDENTITY_CODE;
        List<UserCourseClass> userCourseClassList = userCourseClassDao.getUserListByCourseIDAndIdentityCodeIgnoreClassID(courseId, code);
        List<User> ls = new ArrayList<>();
        for(UserCourseClass ucc: userCourseClassList)
        {
            Integer id = ucc.getUser_id();
            ls.addAll(userDao.getUserByID(id));
        }
        return ls;
    }


    @Override
    public List<User> listByCondition(Map<String, String> params) {
        String username = params.get("username");
        String mail = params.get("mail");
        String name = params.get("name");
        Integer code = TeacherEntityConstant.IDENTITY_CODE;
        List<User> res = userDao.getUserByCondition(code,username,mail,name);
        return res;
    }
}
