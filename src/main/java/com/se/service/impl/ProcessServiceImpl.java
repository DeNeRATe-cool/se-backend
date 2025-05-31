package com.se.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.se.constant.ResourceConstant;
import com.se.dao.ProcessDao;
import com.se.entity.Class;
import com.se.entity.Resource;
import com.se.entity.User;
import com.se.exception.ParamNotEnoughException;
import com.se.exception.resException.ResTypeArgumentException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.ProcessService;
import com.se.service.UserCourseClassService;
import com.se.utils.ResTagFilterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.se.entity.Process;
import com.se.utils.ResUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProcessServiceImpl implements ProcessService {
    @Autowired
    private ProcessDao processDao;

    @Autowired
    private UserCourseClassService userCourseClassService;

    @Override
        public void createProcess(Process process) {
        if (process.getTime() == null || StrUtil.isBlank(process.getName())
        || process.getClass_id() == null || process.getCourse_id() == null)
            throw new ParamNotEnoughException();
        processDao.insert(process);
    }

    @Override
    public List<Process> queryByClass(Integer course_id, Integer class_id) {
        if(course_id == null || class_id == null)
            throw new ParamNotEnoughException();
        return processDao.queryByClass(course_id, class_id);
    }

    @Override
    public void addResource(String fileName, Integer process_id, Integer course_id, Integer class_id, String url, Boolean is_public, String tags) {
        String resCode = ResUtil.generateCode();
        String type = fileName.contains(".")
                ? fileName.substring(fileName.lastIndexOf(".") + 1)
                : ResourceConstant.defaultType;
        if(Boolean.TRUE.equals(is_public)) processDao.addResource(resCode, fileName, -1, course_id, class_id, type, url, DateUtil.now(), tags);
        processDao.addResource(resCode, fileName, process_id, course_id, class_id, type, url, DateUtil.now(), tags);
    }

    @Override
    public String downloadResource(Integer id) {
        if(id == null)
            throw new ParamNotEnoughException();
        return processDao.getResourceUrl(id);
    }

    @Override
    public List<Resource> getResourceByProcess(Integer processId, Integer courseId) {
        if(processId == null || courseId == null)
            throw new ParamNotEnoughException();
        List<Resource> resList = processDao.getResourceByProcess(processId, courseId);
        return ResTagFilterUtil.uniqueResource(resList);
    }

    @Override
    public List<Resource> getResourceByTag(List<User> tutorList, List<Class> classList, Integer courseId, Integer userId, String tags) {
        if(courseId == null || userId == null)
            throw new ParamNotEnoughException();
        boolean isTutor = false;
        for(User user : tutorList) {
            if(user.getUser_id().equals(userId)) {
                isTutor = true;
                break;
            }
        }
        List<Resource> resList;
        if(isTutor) {
            resList = processDao.getResourceByCourse(courseId);
        } else {
            Integer classId = null;
            for(Class c : classList) {
                List<User> students = userCourseClassService.listStuByClass(c.getClass_id());
                boolean inClass = false;
                for(User user : students) {
                    if(user.getUser_id().equals(userId)) {
                        inClass = true;
                        break;
                    }
                }
                if(inClass) {
                    classId = c.getClass_id();
                    break;
                }
            }
            if(classId == null) {
                throw new UserNotFoundException("用户不在课程内");
            }
            resList = processDao.getResourceByClass(classId);
            resList.addAll(getPublicResourceByCourse(courseId));
        }
        return ResTagFilterUtil.uniqueResource(
                ResTagFilterUtil.filterByTag(resList, tags));
    }

    @Override
    public List<Resource> getPublicResourceByCourse(Integer courseId) {
        return processDao.getPublicResourceByCourse(courseId);
    }

    @Override
    public void deleteResource(Integer resId) {
        Resource res = processDao.getResourceById(resId);
        String resCode = res.getRes_code();
        processDao.deleteByCode(resCode);
    }

    @Override
    public void updateResource(String fileName, String url, Integer resId, Boolean isPublic) {
        Resource res = processDao.getResourceById(resId);
        if(!res.getType().equals(ResUtil.getSuffix(fileName)))
            throw new ResTypeArgumentException();
        if(Boolean.TRUE.equals(isPublic)) processDao.addResource(res.getRes_code(), fileName, -1, res.getCourse_id(), res.getClass_id(), res.getType(), url, DateUtil.now(), res.getTag());
        processDao.addResource(res.getRes_code(), fileName, res.getProcess_id(), res.getCourse_id(), res.getClass_id(), res.getType(), url, DateUtil.now(), res.getTag());
    }

    @Override
    public List<Resource> getResourceHistory(Integer resId) {
        Resource res = processDao.getResourceById(resId);
        List<Resource> resList = processDao.getResourceByCode(res.getRes_code());
        resList.sort(
                Comparator.comparing(Resource::getDate)
                        .reversed()
        );
        return resList;
    }
}
