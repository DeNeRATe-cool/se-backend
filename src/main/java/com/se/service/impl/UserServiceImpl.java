package com.se.service.impl;

import com.se.constant.StudentEntityConstant;
import com.se.constant.UserEntityConstant;
import com.se.dao.UserDao;
import com.se.dto.*;
import com.se.entity.User;
import com.se.exception.userException.UserBizException;
import com.se.exception.userException.UserNotFoundException;
import com.se.service.UserService;
import com.se.utils.JwtUtil;
import com.se.utils.PasswordEncoder;
import com.se.utils.parseBirthday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

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
        String token= JwtUtil.generateToken(user.getUser_id());
        LoginResponseDTO loginResponseDTO=new LoginResponseDTO(token,user);
        return Result.ok(loginResponseDTO);
    }

    @Override
    public Result modify(UserUpdateDTO dto) {
        User user=userDao.findById(dto.getUser_id());
        if(user==null){
            throw new UserBizException("用户不存在");
        }
//        if (StringUtils.hasText(dto.getPassword())) {
//            String password= PasswordEncoder.encode(dto.getPassword());
//            user.setPassword(password);
//        }
        if (StringUtils.hasText(dto.getMail())) {
            user.setMail(dto.getMail());
        }
        if (StringUtils.hasText(dto.getBirthday())) {
            user.setBirthday(parseBirthday.parseBirthday(dto.getBirthday()));
        }
        if (StringUtils.hasText(dto.getName())) {
            user.setName(dto.getName());
        }
        userDao.updateUser(user);
        return Result.ok(user);
    }

    @Override
    public User testRegister(UserRegDTO userRegDTO) {
        String DBpassword = PasswordEncoder.encode(userRegDTO.getPassword());
        User user = new User();
        user.setUsername(userRegDTO.getUsername());
        user.setIdentity(userRegDTO.getIdentity());
        user.setPassword(DBpassword);
        String name = StringUtils.hasText(userRegDTO.getName())?userRegDTO.getName():userRegDTO.getUsername();
        user.setName(name);
        user.setMail(userRegDTO.getMail());

        user.setBirthday(parseBirthday.parseBirthday(userRegDTO.getBirthday()));
        userDao.insert(user);
        return user;
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
        System.out.println(userRegDTO.getConfirmPassword());
        if(!userRegDTO.getPassword().equals(userRegDTO.getConfirmPassword())){
            throw new UserBizException("密码输入不一致，请重新确认密码");
        }
        if(storedCode == null || !storedCode.equals(userRegDTO.getVerifyCode())) {
            throw new UserBizException("验证码错误或者过期");
        }
        String DBpassword = PasswordEncoder.encode(userRegDTO.getPassword());
        User user = new User();
        user.setUsername(userRegDTO.getUsername());
        user.setIdentity(userRegDTO.getIdentity());
        user.setPassword(DBpassword);
        String name = StringUtils.hasText(userRegDTO.getName())?userRegDTO.getName():userRegDTO.getUsername();
        user.setName(name);
        user.setMail(userRegDTO.getMail());

        user.setBirthday(parseBirthday.parseBirthday(userRegDTO.getBirthday()));
        userDao.insert(user);
        return user;
    }
}
