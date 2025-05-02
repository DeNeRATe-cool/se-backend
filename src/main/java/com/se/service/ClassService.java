package com.se.service;

import com.se.dto.AddAdminInClassDTO;
import com.se.dto.ApplyJoinClassDTO;
import com.se.entity.Class;
import com.se.entity.User;

import java.util.List;

public interface ClassService {
    void add(Class classEntity);

    List<User> addAdmin(AddAdminInClassDTO addAdminInClassDTO);

    List<User> listTeacherAndTutor(Integer classId);

    List<Class> list();

    void apply(ApplyJoinClassDTO applyJoinClassDTO);

    List<User> listByClassID(Integer courseId, Integer classId, Integer userId);

    List<User> addStu(Integer courseId, Integer classId, Integer userId, String username);
}
