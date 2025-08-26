package com.se.controller;

import com.se.dto.Result;
import com.se.dto.UserLoginDTO;
import com.se.dto.UserRegDTO;
import com.se.dto.UserUpdateDTO;
import com.se.entity.User;
import com.se.service.MailService;
import com.se.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserUpdateDTO testUpdateDTO;
    private UserLoginDTO testLoginDTO;
    private UserRegDTO testRegDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setName("Test User");
        
        testUpdateDTO = new UserUpdateDTO();
        testUpdateDTO.setUser_id(1);
        
        testLoginDTO = new UserLoginDTO();
        testLoginDTO.setAccount("test@example.com");
        
        testRegDTO = new UserRegDTO();
        testRegDTO.setMail("test@example.com");
    }

    // ==================== modifyUser 方法测试 ====================
    @Test
    void modifyUser_Success() {
        when(userService.modify(testUpdateDTO)).thenReturn(Result.ok());
        Result result = userController.modifyUser(testUpdateDTO);
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }

    @Test
    void modifyUser_Failure() {
        when(userService.modify(testUpdateDTO)).thenReturn(Result.fail("修改失败"));
        Result result = userController.modifyUser(testUpdateDTO);
        assertNotNull(result);
        assertFalse(result.getSuccess());
    }

    // ==================== login 方法测试 ====================
    @Test
    void login_Success() {
        when(userService.login(testLoginDTO)).thenReturn(Result.ok(testUser));
        Result result = userController.login(testLoginDTO);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
    }

    @Test
    void login_Failure() {
        when(userService.login(testLoginDTO)).thenReturn(Result.fail("登录失败"));
        Result result = userController.login(testLoginDTO);
        assertNotNull(result);
        assertFalse(result.getSuccess());
    }

    // ==================== sendVerifyCode 方法测试 ====================
    @Test
    void sendVerifyCode_Success() {
        when(mailService.sendVerifyCode("test@example.com")).thenReturn(Result.ok());
        Result result = userController.sendVerifyCode("test@example.com");
        assertNotNull(result);
        assertTrue(result.getSuccess());
    }


    @Test
    void sendVerifyCode_Failure() {
        when(mailService.sendVerifyCode("test@example.com")).thenReturn(Result.fail("发送失败"));
        Result result = userController.sendVerifyCode("test@example.com");
        assertNotNull(result);
        assertFalse(result.getSuccess());
    }

    // ==================== register 方法测试 ====================
    @Test
    void register_Success() {
        when(userService.register(testRegDTO)).thenReturn(testUser);
        Result result = userController.register(testRegDTO);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
    }

    @Test
    void register_Failure() {
        when(userService.register(testRegDTO)).thenReturn(null);
        Result result = userController.register(testRegDTO);
        assertNotNull(result);
        assertTrue(result.getSuccess()); // 注意：即使返回null，Result.ok()也是成功的
        assertNull(result.getData());
    }

    // ==================== info 方法测试 ====================
    @Test
    void info_Success() {
        when(userService.info(1)).thenReturn(testUser);
        Result result = userController.info(1);
        assertNotNull(result);
        assertTrue(result.getSuccess());
        assertNotNull(result.getData());
    }

    @Test
    void info_Failure() {
        when(userService.info(1)).thenReturn(null);
        Result result = userController.info(1);
        assertNotNull(result);
        assertTrue(result.getSuccess()); // 注意：即使返回null，Result.ok()也是成功的
        assertNull(result.getData());
    }
}