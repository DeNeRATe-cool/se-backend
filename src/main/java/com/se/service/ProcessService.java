package com.se.service;

import com.se.entity.Process;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface ProcessService {
    void createProcess(Process process);

    List<Process> queryByClass(Integer course_id, Integer class_id);

    void addResource(String fileName, Integer process_id, Integer course_id, Integer class_id, String url, Boolean is_public, String tags);
}
