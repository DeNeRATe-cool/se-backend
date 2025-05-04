package com.se.service.impl;

import com.se.constant.ProblemEntityConstant;
import com.se.dao.ExerDao;
import com.se.dao.ProbDao;
import com.se.dao.StuDao;
import com.se.dao.StuProbExerDao;
import com.se.entity.Problem;
import com.se.exception.ParamIllegalException;
import com.se.service.StuProbExerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StuProbExerServiceImpl implements StuProbExerService {

    @Autowired
    private StuProbExerDao stuProbExerDao;

    @Autowired
    private ExerDao exerDao;

    @Autowired
    private StuDao stuDao;

    @Autowired
    private ProbDao probDao;

    @Override
    public Boolean isProbExist(Integer prob_id) {
        List<Problem> problemList = probDao.selectByProbId(prob_id);
        return !problemList.isEmpty();
    }

    @Override
    public void checkProbListExist(List<Integer> prob_ids) {
        for(Integer prob_id : prob_ids) {
            if(!isProbExist(prob_id)) {
                throw new ParamIllegalException(ProblemEntityConstant.PROBLEM_NOT_EXIST);
            }
        }
    }
}
