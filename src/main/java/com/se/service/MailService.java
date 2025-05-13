package com.se.service;

import com.se.dto.Result;

public interface MailService {
    Result sendVerifyCode(String Mail);
}
