package com.se.service;

import com.se.dao.StuProbExerDao;
import com.se.dto.ProbInExer;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StuProbExerService {
    // 得到任务完成数量
    Integer countFinish(Integer exerId);

    // 判断题目是否存在
    Boolean isProbExist(Integer prob_id);
    // 判断题目列表是否都存在
    void checkProbListExist(List<Integer> prob_ids);
    // 通过 exer 得到 prob id list
    List<Integer> getProbIDListByExerId(Integer exer_id);
    // 根据exerId得到题目列表
    List<ProbInExer> getProbModelListByExerId(Integer exerId);
    // 根据exerId得到题目列表 非模板
    List<Integer> getProbIdListByExerId(Integer exerId);
    // 根据用户id + 任务id得到学生的 stuProbExer列表
    List<StuProbExer> getStuProbExerByExerIdAndUserId(Integer exerId, Integer userId);
    // 判断学生是否完成任务

    /**
     * 前提：任务和学生对应
     * @param exerId
     * @param userId
     * @return
     */
    Boolean checkStudentFinishExercise(Integer exerId, Integer userId);

    void setFinishedByExerIdAndUserId(Integer exerId, Integer userId);

    void save(Integer exerId, Integer userId, List<String> anslist);
}
