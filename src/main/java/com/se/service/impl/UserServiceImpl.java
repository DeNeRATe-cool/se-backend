package com.se.service.impl;

import com.se.constant.UserEntityConstant;
import com.se.dao.UserDao;
import com.se.entity.User;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Override
    public User info(Integer id) {
        List<User> userList = userDao.getUserByID(id);
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        return userList.get(0);
    }
}
