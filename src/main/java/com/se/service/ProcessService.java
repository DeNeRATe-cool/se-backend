package com.se.service;

import com.se.entity.Class;
import com.se.entity.Process;
import com.se.entity.Resource;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProcessService {
    void createProcess(Process process);

    List<Process> queryByClass(Integer course_id, Integer class_id);

    void addResource(String fileName, Integer process_id, Integer course_id, Integer class_id, String url, Boolean is_public, String tags);

    String downloadResource(Integer id);

    List<Resource> getResourceByProcess(Integer processId, Integer courseId);

    List<Resource> getResourceByTag(List<User> tutorList, List<Class> classList, Integer courseId, Integer userId, String tags);

    List<Resource> getPublicResourceByCourse(Integer courseId);

    void deleteResource(Integer resId);

    void updateResource(String fileName, String url, Integer resId, Boolean isPublic);
}
