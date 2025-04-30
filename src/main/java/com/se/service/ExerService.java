package com.se.service;


import java.util.List;

public interface ExerService {
    List<Integer> getStuFinishExerNum(int user_id, int course_id, int class_id);

}
