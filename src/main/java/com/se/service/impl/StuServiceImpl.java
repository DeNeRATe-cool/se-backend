package com.se.service.impl;

import com.se.dao.StuDao;
import com.se.dto.Result;
import com.se.entity.User;
import com.se.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuDao stuDao;

    @Override
    public Result getAllStudent() {
        List<User> students = stuDao.getAllStudent();
        return Result.ok(students, Integer.valueOf(students.size()));
    }
}
