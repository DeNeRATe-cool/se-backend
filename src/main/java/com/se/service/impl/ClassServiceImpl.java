package com.se.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.se.constant.*;
import com.se.dao.ClassDao;
import com.se.dao.UserCourseClassDao;
import com.se.dao.UserDao;
import com.se.dto.AddAdminInClassDTO;
import com.se.dto.ApplyJoinClassDTO;
import com.se.dto.UserCourseClass;
import com.se.entity.Class;
import com.se.entity.Course;
import com.se.entity.User;
import com.se.exception.EntityNotFoundException;
import com.se.exception.classException.ClassNotExistException;
import com.se.exception.classException.DuplicateClassException;
import com.se.exception.courseException.CourseClassNotMatchException;
import com.se.exception.courseException.DuplicateJoinCourseException;
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
    public Class add(Class classEntity) {
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
        userCourseClassService.insert(-1,
                classEntity.getCourse_id(),
                classEntity.getClass_id(),
                -1);
        return classEntity;
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
                addAdminInClassDTO.getClass_id());
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

    /**
     *
     * @param applyJoinClassDTO
     * course_id class_code user_id`
     * 1, 用户存在 + 验证学生身份
     * 2. 班级存在
     * 3. 避免重复加入
     * 4. 验证课程-班级是否匹配
     */
    @Override
    public void apply(ApplyJoinClassDTO applyJoinClassDTO) {
        Integer apply_user_id = applyJoinClassDTO.getUser_id();
        Integer apply_course_id = applyJoinClassDTO.getCourse_id();
        String apply_class_code = applyJoinClassDTO.getClass_code();
        if(!userCourseClassService.userExist(apply_user_id))
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        if(!userCourseClassService.userIsStudent(apply_user_id))
        {
            throw new UserPermissionException(StudentEntityConstant.STUDENT_IDENTITY_ERROR);
        }
        if(!userCourseClassService.classExist(apply_class_code))
        {
            throw new ClassNotExistException(ClassEntityConstant.CLASS_NOT_EXISTS);
        }
        if(userCourseClassService.studentInCourse(apply_user_id,apply_course_id))
        {
            throw new DuplicateJoinCourseException(CourseEntityConstant.STUDENT_DUPLICATE_JOIN_COURSE);
        }
        Class class_entity = classDao.getClassEntityByClassCode(apply_class_code).get(0);
        if(!userCourseClassService.isCourseAndClassMatch(apply_course_id,class_entity.getClass_id()))
        {
            throw new CourseClassNotMatchException(CourseEntityConstant.COURSE_CLASS_NOT_MATCH);
        }

        userCourseClassDao.add(apply_user_id,apply_course_id,
                class_entity.getClass_id(),StudentEntityConstant.IDENTITY_CODE);
    }

    /**
     * 1. 验证身份 是否为课程的老师或者助教
     * @param courseId
     * @param classId
     * @param userId
     * @return
     */
    @Override
    public List<User> listByClassID(Integer courseId, Integer classId, Integer userId) {
        /*
        * 检查用户存在
        * */
        User user = userCourseClassService.safeGetUser(userId);

        /**
         * 用户是课程的老师或者助教
         */
        userCourseClassService.checkCourseAndClassAndAdmin(courseId,classId,userId);
        return userCourseClassService.listStuByClass(classId);

    }

    /**
     * 课程 班级 用户匹配
     * 用户不能已经在课程里了
     * @param courseId
     * @param classId
     * @param userId
     * @param username
     * @return
     */
    @Override
    public List<User> addStu(Integer courseId, Integer classId, Integer userId, String username) {
        User inviter = userCourseClassService.safeGetUser(userId);
        User invitee = userCourseClassService.safeGetUser(username);
        userCourseClassService.checkCourseAndClassAndAdmin(courseId,classId,userId);
        // 检查学生身份
        if(!userCourseClassService.userIsStudent(invitee.getUser_id()))
        {
            throw new UserPermissionException(StudentEntityConstant.STUDENT_IDENTITY_ERROR);
        }
        // 检查该学生已经是助教
        if(userCourseClassService.tutorInCourse(invitee.getUser_id(),courseId))
        {
            throw new UserPermissionException(TutorEntityConstant.IDENTITY_STUDENT_CONFLICT);
        }
        // 检查用户在课程里了
        if(userCourseClassService.studentInCourse(invitee.getUser_id(),courseId))
        {
            throw new DuplicateJoinCourseException(CourseEntityConstant.STUDENT_DUPLICATE_JOIN_COURSE);
        }

        userCourseClassService.insert(invitee.getUser_id(),
                courseId,
                classId,
                StudentEntityConstant.IDENTITY_CODE);

        return userCourseClassService.listStuByClass(classId);
    }

    @Override
    public List<Class> listClassByStu(Integer userId) {
        User u = userCourseClassService.safeGetUser(userId);
        if(!userCourseClassService.userIsStudent(userId))
        {
            throw new UserPermissionException(StudentEntityConstant.STUDENT_IDENTITY_ERROR);
        }
        return userCourseClassService.listClassByStudent(userId);
    }

    @Override
    public List<User> delStu(Integer courseId, Integer classId, Integer userId) {
        User u = userCourseClassService.safeGetUser(userId);
        userCourseClassService.checkIsStudent(userId);
        userCourseClassService.checkCourseAndClass(courseId,classId);
        if(userCourseClassService.select(userId,courseId,classId) != null)
        {
            userCourseClassService.delete(courseId,classId,userId);
        }
        else {
            throw new EntityNotFoundException(ClassEntityConstant.STUDENT_NOT_IN_CLASS_DELETE_DENIED);
        }
        return userCourseClassService.listStuByClass(classId);
    }

    /**
     * 对于每一个List<String> 格式为 [username, name]
     * 检查username是否合法，是否存在
     * 不存在 则跳过
     * 存在 则
     * @param res
     * @return
     */
    @Override
    public List<User> addFile(List<List<String>> res, Integer user_id, Integer class_id) {
        if(classDao.getClassEntityByClassId(class_id).isEmpty())
        {
            throw new ClassNotExistException(ClassEntityConstant.CLASS_NOT_EXISTS);
        }

        Course course = userCourseClassService.getCourseListByClass(class_id).get(0);

        if(!userCourseClassService.teacherInCourse(user_id,course.getCourse_id())
        && !userCourseClassService.tutorInCourse(user_id,course.getCourse_id()))
        {
            throw new UserPermissionException(UserEntityConstant.USER_PERMISSION_DENIED);
        }

        for(List<String> row : res )
        {
            String username = row.get(0);
            String name = row.get(1);
//            System.out.println(username);
            User u = userCourseClassService.tryGetUser(username);
            if(u == null)
            {
//                System.out.println("null");
                continue;
            }
            if(!u.getName().equals(name))
            {
//                System.out.println("name not match");
                continue;
            }


            // 检查学生身份
            if(!userCourseClassService.userIsStudent(u.getUser_id()))
            {
//                System.out.println("not student");
                continue;
            }
            // 检查该学生已经是助教
            if(userCourseClassService.tutorInCourse(u.getUser_id(),course.getCourse_id()))
            {
//                System.out.println("already tutor");
                continue;
            }
            // 检查用户在课程里了
            if(userCourseClassService.studentInCourse(u.getUser_id(),course.getCourse_id()))
            {
//                System.out.println(u.getUser_id() + "in course already");
                continue;
            }

            userCourseClassService.insert(u.getUser_id(),
                    course.getCourse_id(),
                    class_id,
                    StudentEntityConstant.IDENTITY_CODE);

//            System.out.println(username);

        }
        return userCourseClassService.listStuByClass(class_id);
    }

    @Override
    public PageInfo<Class> listPage(Integer page, Integer size) {
        PageHelper.startPage(page, size);
        List<Class> res = list();
        return new PageInfo<>(res);
    }

    @Override
    public List<Class> listByAdmin(Integer courseId, Integer userId) {
        userCourseClassService.checkIsAdminForCourse(userId, courseId);
        List<Class>res = userCourseClassService.listClassForCourseByUserId(courseId,userId);
        return res;
    }

    @Override
    public Class info(Integer classId) {
        List<Class> classList = classDao.getClassEntityByClassId(classId);
        if(classList.isEmpty())return null;
        return classList.get(0);
    }
}
