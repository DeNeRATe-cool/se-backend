package com.se.service;

import com.se.dto.Result;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StuService {

    Result getAllStudent();
}
