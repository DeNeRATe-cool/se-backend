package com.se.service.impl;

import com.se.constant.*;
import com.se.dao.ClassDao;
import com.se.dao.CourseDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.UserCourseClass;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.exception.courseException.CourseClassNotMatchException;
import com.se.exception.userException.UserNotFoundException;
import com.se.exception.userException.UserPermissionException;
import com.se.service.UserCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserCourseClassServiceImpl implements UserCourseClassService {
    @Autowired
    private CourseDao courseDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ClassDao classDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    public List<User> getAdminListByCourse(Integer courseId) {
        List<User> list = getTeacherListByCourse(courseId);
        list.addAll(getTutorListByCourse(courseId));
        return list;
    }

    @Override
    public Boolean userExist(Integer user_id) {
        return !userDao.getUserByID(user_id).isEmpty();
    }

    @Override
    public Boolean userIsTeacher(Integer user_id) {
        if(!userExist(user_id))
        {
            return false;
        }
        User user = userDao.getUserByID(user_id).get(0);
        return user.getIdentity().equals(TeacherEntityConstant.IDENTITY_CODE);
    }

    @Override
    public Boolean userIsStudent(Integer user_id) {
        if(!userExist(user_id)) {
            return false;
        }
        User user = userDao.getUserByID(user_id).get(0);
        return user.getIdentity().equals(StudentEntityConstant.IDENTITY_CODE);
    }

    @Override
    public Boolean classExist(Integer class_id) {
        return !classDao.getClassEntityByClassId(class_id).isEmpty();
    }

    @Override
    public Boolean classExist(String class_code) {
        return !classDao.getClassEntityByClassCode(class_code).isEmpty();
    }

    @Override
    public Boolean studentInCourse(Integer user_id, Integer course_id) {
        if(!userIsStudent(user_id)) {
            return false;
        }
        List<UserCourseClass> userCourseClassList =
                userCourseClassDao.getListByUserIDAndCourseIDAndIdentity(user_id,course_id,StudentEntityConstant.IDENTITY_CODE);
        return !userCourseClassList.isEmpty();
    }

    @Override
    public List<Class> listClassesByCourse(Integer course_id) {
        List<UserCourseClass>userCourseClassList = userCourseClassDao.getClassListByCourse(course_id);
        List<Class>classList = new ArrayList<>();
        for(UserCourseClass userCourseClass : userCourseClassList) {
            Integer class_id = userCourseClass.getClass_id();
            classList.addAll(classDao.getClassEntityByClassId(class_id));
        }
        return classList;
    }

    @Override
    public List<User> listStuByClass(Integer class_id) {
        List<UserCourseClass>userCourseClassList = userCourseClassDao.getUserListByClassID(class_id);
        List<User>userList = new ArrayList<>();
        for(UserCourseClass userCourseClass : userCourseClassList) {
            Integer user_id = userCourseClass.getUser_id();
            if(userIsStudent(user_id))
            {
                userList.add(userDao.getUserByID(user_id).get(0));
            }
        }
        return userList;
    }

    @Override
    public List<Course> getCourseListByClass(Integer class_id) {

        List<UserCourseClass>userCourseClassList = userCourseClassDao.getCourseByClass(class_id);
        if(userCourseClassList.isEmpty())return null;
        List<Course> courseList = courseDao.getByID(userCourseClassList.get(0).getCourse_id());
        return courseList;
    }

    @Override
    public List<Class> listClassByStudent(Integer user_id) {
        List<UserCourseClass>userCourseClassList = userCourseClassDao.getByUserID(user_id);
        List<Class>classList = new ArrayList<>();
        for(UserCourseClass userCourseClass : userCourseClassList) {
            Integer class_id = userCourseClass.getClass_id();
            if(class_id != -1)
            {
                classList.add(classDao.getClassEntityByClassId(class_id).get(0));
            }
        }
        return classList;
    }

    @Override
    public User safeGetUser(Integer user_id) {
        List<User> userList = userDao.getUserByID(user_id);
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        return userList.get(0);
    }

    @Override
    public User safeGetUser(String username) {
        List<User> userList = userDao.getUserByUsername(username);
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        return userList.get(0);
    }

    @Override
    public User tryGetUser(Integer user_id) {
        List<User> userList = userDao.getUserByID(user_id);
        if(userList.isEmpty())
        {
            return null;
        }
        return userList.get(0);
    }

    @Override
    public User tryGetUser(String username) {
        List<User> userList = userDao.getUserByUsername(username);
        if(userList.isEmpty())
        {
            return null;
        }
        return userList.get(0);
    }


    @Override
    public void checkCourseAndClassAndAdmin(Integer course_id, Integer class_id, Integer user_id) {
        User user = safeGetUser(user_id);
        if(user.getIdentity().equals(TeacherEntityConstant.IDENTITY_CODE) &&
                !teacherInCourse(user_id,course_id))
        {
            throw new UserPermissionException(UserEntityConstant.USER_PERMISSION_DENIED);
        }
        if(user.getIdentity().equals(StudentEntityConstant.IDENTITY_CODE) &&
                !tutorInCourse(user_id,course_id))
        {
            throw new UserPermissionException(UserEntityConstant.USER_PERMISSION_DENIED);
        }

        if(!isCourseAndClassMatch(course_id,class_id))
        {
            throw new CourseClassNotMatchException(CourseEntityConstant.COURSE_CLASS_NOT_MATCH);
        }
    }

    @Override
    public void insert(Integer user_id, Integer course_id, Integer class_id, Integer identity) {
        userCourseClassDao.add(user_id,course_id,class_id,identity);
    }

    @Override
    public void checkIsAdminForCourse(Integer user_id, Integer course_id) {
        if(!teacherInCourse(user_id,course_id) && !tutorInCourse(user_id,course_id))
        {
            throw new UserPermissionException(CourseEntityConstant.ADMIN_NOT_IN_COURSE);
        }
    }

    public Boolean isAdminForCourse(Integer user_id, Integer course_id) {
        if(teacherInCourse(user_id,course_id) || tutorInCourse(user_id,course_id))return true;
        return false;
    }

    @Override
    public void checkIsStudent(Integer user_id) {
        if(!userIsStudent(user_id))
        {
            throw new UserPermissionException(StudentEntityConstant.STUDENT_IDENTITY_ERROR);
        }
    }


    @Override
    public void checkCourseAndClass(Integer course_id, Integer class_id) {
        if(!isCourseAndClassMatch(course_id,class_id))
        {
            throw new CourseClassNotMatchException(CourseEntityConstant.COURSE_CLASS_NOT_MATCH);
        }
    }

    @Override
    public void delete(Integer course_id, Integer class_id, Integer user_id) {
        userCourseClassDao.delete(course_id,class_id,user_id);
    }

    @Override
    public UserCourseClass select(Integer user_id, Integer course_id, Integer class_id) {
        List<UserCourseClass>userCourseClassList = userCourseClassDao.select(user_id,course_id,class_id);
        if(userCourseClassList.isEmpty())return null;
        return userCourseClassList.get(0);
    }

    @Override
    public List<Course> listCourseByUserIdAndIdentity(Integer userId, Integer identity) {
        List<UserCourseClass> res =  userCourseClassDao.getListByUserIdAndIdentity(userId,identity);
        if(res.isEmpty())return null;
        List<Course> courseList = new ArrayList<>();
        Set<Integer> courseIdSet = new HashSet<>();
        for(int i = 0;i < res.size();i++)
        {
            if(courseIdSet.contains(res.get(i).getCourse_id()))
            {
                continue;
            }
            courseIdSet.add(res.get(i).getCourse_id());
            courseList.add(courseDao.getByID(res.get(i).getCourse_id()).get(0));
        }
        return courseList;
    }

    public List<User> getTeacherListByCourse(Integer course_id)
    {
        return getUserListByCourseAndIdentity(course_id, TeacherEntityConstant.IDENTITY_CODE);
    }

    @Override
    public Boolean studentInCourse(User user, Integer course_id) {
        return studentInCourse(user.getUser_id(),course_id);
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

    /**
     * 判断老师属于该课程
     * @param user
     * @param course_id
     * @return
     */
    public Boolean teacherInCourse(User user,Integer course_id)
    {
        return teacherInCourse(user.getIdentity(), course_id);
    }

    public Boolean teacherInCourse(Integer user_id,Integer course_id)
    {
        List<User> teacherList = getTeacherListByCourse(course_id);
        for(User u : teacherList)
        {
            if(u.getUser_id().equals(user_id))return true;
        }
        return false;
    }

    /**
     * 判断助教属于该课程
     * @param user
     * @param course_id
     * @return
     */
    public Boolean tutorInCourse(User user, Integer course_id)
    {
        return tutorInCourse(user.getUser_id(),course_id);
    }

    public Boolean tutorInCourse(Integer user_id,Integer course_id)
    {
        List<User> tutorList = getTutorListByCourse(course_id);
        for(User u : tutorList)
        {
            if(u.getUser_id().equals(user_id))return true;
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
