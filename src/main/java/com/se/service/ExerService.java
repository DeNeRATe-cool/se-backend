package com.se.service;


import com.se.dto.CreateExerDTO;
import com.se.dto.PushExerDTO;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;

import java.util.List;

public interface ExerService {
    List<Integer> getStuFinishExerNum(Integer user_id, Integer course_id, Integer class_id);

    List<Exercise> getNotCheckedExercise(Integer courseId, Integer userId);

    List<User> getNotCheckedStu(Integer exerId);

    Exercise create(CreateExerDTO createExerDTO);

    List<List<?>> getCheckInfo(List<StuProbExer> baseList, List<Problem> proList, List<StuProbExer> stuExerList, Integer userId);

    void submitCheckInfo(Integer userId, Integer exerId, List<Integer> scores, List<String> infos, List<StuProbExer> baseList, List<StuProbExer> stuExerList);

    List<List<?>> getGradeAndRank(Integer exerId);

    List<List<?>> getAccessRatio(Integer exerId);

    void push(PushExerDTO pushExerDTO);
}
