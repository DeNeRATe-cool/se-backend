package com.se.service;

import com.se.dto.Result;
import com.se.dto.UserLoginDTO;
import com.se.dto.UserRegDTO;
import com.se.entity.User;

public interface UserService {

    User info(Integer id);
    User register(UserRegDTO userRegDTO);

    Result login(UserLoginDTO dto);
}
