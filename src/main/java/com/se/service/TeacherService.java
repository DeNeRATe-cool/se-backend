package com.se.service;

import com.se.entity.User;

import java.util.List;
import java.util.Map;

public interface TeacherService {
    List<User> listByCourse(int courseId);

    List<User> listByCondition(Map<String, String> params);
}
