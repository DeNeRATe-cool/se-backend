package com.se.service;

import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface TeacherService {
    List<User> listByCourse(int courseId);

    List<User> listByCondition(Map<String, String> params);

    List<User> list();
}
