package com.se.service;

import com.se.dao.StuProbExerDao;
import com.se.dto.StuProbExer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface StuProbExerService {
    // 判断题目是否存在
    Boolean isProbExist(Integer prob_id);
    // 判断题目列表是否都存在
    void checkProbListExist(List<Integer> prob_ids);
    // 通过 exer 得到 prob id list
    List<Integer> getProbIDListByExerId(Integer exer_id);
}
