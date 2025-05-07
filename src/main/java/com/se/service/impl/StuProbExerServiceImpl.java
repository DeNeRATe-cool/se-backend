package com.se.service.impl;

import com.se.constant.ProblemEntityConstant;
import com.se.dao.ExerDao;
import com.se.dao.ProbDao;
import com.se.dao.StuDao;
import com.se.dao.StuProbExerDao;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.exception.ParamIllegalException;
import com.se.service.StuProbExerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Integer countFinish(Integer exerId) {
        List<StuProbExer> stuProbExerList = stuProbExerDao.listByExerIdWithProbIdInvalAndStuIdValid(exerId);
        Integer count = 0;
        for(StuProbExer stuProbExer : stuProbExerList){
            if(stuProbExer.getIs_finish())
            {
                count+=1;
            }
        }
        return count;
    }

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

    /**
     * 要求任务必须是模板任务
     * @param exer_id
     * @return
     */
    @Override
    public List<Integer> getProbIDListByExerId(Integer exer_id) {
        List<StuProbExer> spe_list = stuProbExerDao.getProblemListByExerID(exer_id);
        List<Integer> prob_id_list = new ArrayList<Integer>();
        for(StuProbExer spe: spe_list) {
            prob_id_list.add(spe.getProb_id());
        }
        return prob_id_list;
    }

    /**
     * 任务可以不是模板任务
     * @param exerId
     * @return
     */
    @Override
    public List<Problem> getProbModelListByExerId(Integer exerId) {
        List<StuProbExer> spe_list = stuProbExerDao.getProblemListByExerIDBroaden(exerId);
        List<Integer>prob_id_list = new ArrayList<>();
        for(StuProbExer spe: spe_list) {
            if(!prob_id_list.contains(spe.getProb_id())) {
                prob_id_list.add(spe.getProb_id());
            }
        }
        List<Problem> problemList = new ArrayList<>();
        for(Integer prob_id : prob_id_list) {
            Problem problem = probDao.getProblemById(prob_id);
            problemList.add(problem);
        }
        return problemList;
    }

    @Override
    public List<StuProbExer> getStuProbExerByExerIdAndUserId(Integer exerId, Integer userId) {
        List<StuProbExer> stuProbExerList = stuProbExerDao.listProbByExerIdAndUserId(exerId,userId);
        return stuProbExerList;
    }

    public Boolean checkStudentFinishExercise(Integer exerId, Integer userId) {
        List<StuProbExer> stuProbExerList = getStuProbExerByExerIdAndUserId(exerId, userId);
        for(StuProbExer spe: stuProbExerList) {
            if(!spe.getIs_finish())
            {
                System.out.println(spe.getProb_id());
                return false;
            }
        }
        return true;
    }

    @Override
    public void setFinishedByExerIdAndUserId(Integer exerId, Integer userId) {
        stuProbExerDao.setFinishedByExerIdAndUserId(exerId,userId);
    }

}
