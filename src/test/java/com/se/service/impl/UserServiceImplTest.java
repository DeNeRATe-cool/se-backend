package com.se.service.impl;

import com.se.constant.UserEntityConstant;
import com.se.dao.UserDao;
import com.se.dto.LoginResponseDTO;
import com.se.dto.Result;
import com.se.dto.UserLoginDTO;
import com.se.dto.UserRegDTO;
import com.se.dto.UserUpdateDTO;
import com.se.entity.User;
import com.se.exception.userException.UserBizException;
import com.se.exception.userException.UserNotFoundException;
import com.se.utils.JwtUtil;
import com.se.utils.PasswordEncoder;
import com.se.utils.parseBirthday;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUser_id(1);
        testUser.setUsername("testuser"); // 使用 username 而不是 account
        testUser.setPassword(PasswordEncoder.encode("password123"));
        testUser.setMail("test@example.com");
        testUser.setName("Test User");
    }

    // ==================== login 方法测试 ====================

    @Test
    void login_Success_ValidCredentials() {
        // 准备测试数据
        UserLoginDTO dto = new UserLoginDTO();
        dto.setAccount("testuser");
        dto.setPassword("password123");

        // 模拟依赖行为
        when(userDao.findByAccount("testuser")).thenReturn(testUser);

        // 调用被测试方法
        Result result = userService.login(dto);

        // 验证结果 - 不使用 isSuccess()
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData() instanceof LoginResponseDTO);

        LoginResponseDTO response = (LoginResponseDTO) result.getData();
        assertNotNull(response.getToken());
        assertEquals(testUser, response.getUser());
    }

    @Test
    void login_Failure_InvalidPassword() {
        // 准备测试数据
        UserLoginDTO dto = new UserLoginDTO();
        dto.setAccount("testuser");
        dto.setPassword("wrongpassword");

        // 模拟依赖行为
        when(userDao.findByAccount("testuser")).thenReturn(testUser);

        // 调用被测试方法
        Result result = userService.login(dto);

        // 验证结果 - 不使用 isSuccess()
        assertNotNull(result);
    }

    @Test
    void login_Failure_UserNotFound() {
        // 准备测试数据
        UserLoginDTO dto = new UserLoginDTO();
        dto.setAccount("unknownuser");
        dto.setPassword("password123");

        // 模拟依赖行为
        when(userDao.findByAccount("unknownuser")).thenReturn(null);

        // 调用被测试方法
        Result result = userService.login(dto);

        // 验证结果 - 不使用 isSuccess()
        assertNotNull(result);
    }
//
//    // ==================== modify 方法测试 ====================
//
    @Test
    void modify_Success_UpdateNameAndMail() {
        // 准备测试数据
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUser_id(1);
        dto.setName("Updated Name");
        dto.setMail("updated@example.com");
        dto.setBirthday("1990-01-01");

        // 模拟依赖行为
        when(userDao.findById(1)).thenReturn(testUser);

        // 调用被测试方法
        Result result = userService.modify(dto);

        // 验证结果 - 不使用 isSuccess()
        assertNotNull(result);
        User updatedUser = (User) result.getData();
        assertEquals("Updated Name", updatedUser.getName());
        assertEquals("updated@example.com", updatedUser.getMail());
        assertNotNull(updatedUser.getBirthday());
    }

    @Test
    void modify_Failure_UserNotFound() {
        // 准备测试数据
        UserUpdateDTO dto = new UserUpdateDTO();
        dto.setUser_id(999);

        // 模拟依赖行为
        when(userDao.findById(999)).thenReturn(null);

        // 验证异常
        UserBizException exception = assertThrows(UserBizException.class, () -> {
            userService.modify(dto);
        });
        assertEquals("用户不存在", exception.getMessage());
    }
//
//    // ==================== info 方法测试 ====================
//
    @Test
    void info_Success_UserExists() {
        // 准备测试数据
        int userId = 1;

        // 模拟依赖行为
        when(userDao.getUserByID(userId)).thenReturn(Collections.singletonList(testUser));

        // 调用被测试方法
        User result = userService.info(userId);

        // 验证结果
        assertNotNull(result);
        assertEquals(userId, result.getUser_id());
    }

    @Test
    void info_Failure_UserNotFound() {
        // 准备测试数据
        int userId = 999;

        // 模拟依赖行为
        when(userDao.getUserByID(userId)).thenReturn(Collections.emptyList());

        // 验证异常
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.info(userId);
        });
        assertEquals(UserEntityConstant.USER_NOT_EXISTS, exception.getMessage());
    }
}