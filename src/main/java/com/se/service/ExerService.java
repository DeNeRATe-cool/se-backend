package com.se.service;


import com.se.dto.CreateExerDTO;
import com.se.dto.ProbInExer;
import com.se.dto.PushExerDTO;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Transactional
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

    Exercise info(Integer exerId);

    List<ProbInExer> listProblemByExerId(Integer exerId);

    List<Exercise> listExerByCourseAndClass(Integer courseId, Integer classId);

    List<Exercise> listExerByStuId(Integer user_id);

    List<Exercise> listDoneExerByStuId(Integer userId);

    List<Exercise> listToDoExerByStuId(Integer userId);

    List<Exercise> listPublicExerByCourse(Integer courseId);

    List<Exercise> listSelfCreateExer(Integer userId);

    void submit(Integer exerId, Integer userId);

    Integer countFinish(Integer exerId);

    List<List<?>> getHistory(Integer userId);

    String generateExerciseReport(Integer userId) throws IOException;

    String generateFeedbackReport(Integer exerId) throws IOException;

    void save(Integer exerId, Integer userId, List<String> anslist);

    // 根据现在时间更新完成状态
    void updateFinishedStateByNowTime(Integer userId);

    // 任务的学生完成情况 第一个列表是完成了的用户列表，第二个是未完成的用户列表
    List<List<User>> checkFinish(Integer exerId);
}
