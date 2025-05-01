package com.se.service;

import com.se.dto.Result;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Transactional
public interface StuService {

    Result getAllStudent();

    List<User> listByCourseID(Integer courseId);

    List<User> listByCondition(Map<String, String> params);
}
