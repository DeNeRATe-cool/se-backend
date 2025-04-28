package com.se.service;

import com.se.entity.Process;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface ProcessService {
    void createProcess(Process process);
}
