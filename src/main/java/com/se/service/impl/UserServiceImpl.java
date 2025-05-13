package com.se.service.impl;

import com.se.constant.UserEntityConstant;
import com.se.dao.UserDao;
import com.se.dto.Result;
import com.se.dto.UserLoginDTO;
import com.se.dto.UserRegDTO;
import com.se.entity.User;
import com.se.exception.userException.UserBizException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.UserService;
import com.se.utils.PasswordEncoder;
import com.se.utils.parseBirthday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
//    Map<String,Integer> mailCodeMap=new ConcurrentHashMap<>();

    @Override
    public Result login(UserLoginDTO dto) {
        String account=dto.getAccount();
        String password=dto.getPassword();
        if (!StringUtils.hasText(account) || !StringUtils.hasText(password)) {
            throw new UserBizException("账号和密码不能为空");
        }
        User user=userDao.findByAccount(account);
        if(user==null){
            return Result.fail("用户不存在");
        }
        if(!PasswordEncoder.matches(user.getPassword(),password)){
            return Result.fail("密码错误");
        }

        return Result.ok(user);
    }

    @Override
    public User info(Integer id) {
        List<User> userList = userDao.getUserByID(id);
        if(userList.isEmpty())
        {
            throw new UserNotFoundException(UserEntityConstant.USER_NOT_EXISTS);
        }
        return userList.get(0);
    }

    @Override
    public User register(UserRegDTO userRegDTO) {
        Integer storedCode = MailSerivceImpl.VERIFY_CODE_CACHE.get(userRegDTO.getMail());
        if(storedCode==null||!storedCode.equals(userRegDTO.getVerifyCode())){
            throw new UserBizException("验证码错误或者过期");
        }
        String DBpassword= PasswordEncoder.encode(userRegDTO.getPassword());
        User user=new User();
        user.setUsername(userRegDTO.getUsername());
        user.setPassword(DBpassword);
        user.setName(userRegDTO.getName());
        user.setMail(userRegDTO.getMail());

        user.setBirthday(parseBirthday.parseBirthday(userRegDTO.getBirthday()));
        userDao.insert(user);
        return user;
    }
}
