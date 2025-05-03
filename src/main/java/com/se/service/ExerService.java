package com.se.service;


import com.se.entity.Exercise;

import java.util.List;

public interface ExerService {
    List<Integer> getStuFinishExerNum(Integer user_id, Integer course_id, Integer class_id);

    List<Exercise> getNotCheckedExercise(Integer courseId, Integer userId);
}
