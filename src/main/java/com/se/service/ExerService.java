package com.se.service;


import java.util.List;

public interface ExerService {
    List<Integer> getStuFinishExerNum(Integer user_id, Integer course_id, Integer class_id);

}
