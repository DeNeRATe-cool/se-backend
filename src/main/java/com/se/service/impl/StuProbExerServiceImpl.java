package com.se.service.impl;

import com.se.constant.ExerEntityConstant;
import com.se.constant.ProblemEntityConstant;
import com.se.dao.ExerDao;
import com.se.dao.ProbDao;
import com.se.dao.StuDao;
import com.se.dao.StuProbExerDao;
import com.se.dto.ProbInExer;
import com.se.dto.StuProbExer;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.exception.ParamIllegalException;
import com.se.service.StuProbExerService;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
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

    private ProbInExer getProbInExerFromProblemAndScore(Problem problem,Integer score)
    {
        ProbInExer probInExer = new ProbInExer();
        probInExer.setScore(score);
        probInExer.setAnalysis(problem.getAnalysis());
        probInExer.setContent(problem.getContent());
        probInExer.setAnswer(problem.getAnswer());
        probInExer.setCreator_id(problem.getCreator_id());
        probInExer.setType(problem.getType());
        probInExer.setDescription(problem.getDescription());
        probInExer.setIs_public(problem.getIs_public());
        probInExer.setStr_content(problem.getStr_content());
        probInExer.setCreate_time(problem.getCreate_time());
        probInExer.setProb_id(problem.getProb_id());
        return probInExer;
    }

    /**
     * 任务可以不是模板任务
     * @param exerId
     * @return
     */
    @Override
    public List<ProbInExer> getProbModelListByExerId(Integer exerId) {
        List<StuProbExer> spe_list = stuProbExerDao.getProblemListByExerIDBroaden(exerId);

        List<StuProbExer> spels = stuProbExerDao.getProblemListByExerID(exerId);
        System.out.println(spels.size());
        List<Problem>pls = new ArrayList<>();
        List<ProbInExer>res = new ArrayList<>();
        for(StuProbExer spe: spels) {
            Problem p = probDao.getProblemById(spe.getProb_id());

            res.add(getProbInExerFromProblemAndScore(p,spe.getScore()));
        }
        res.sort(Comparator.comparingInt(ProbInExer::getType).thenComparingInt(ProbInExer::getProb_id));
        return res;

//        List<Integer>prob_id_list = new ArrayList<>();
//        for(StuProbExer spe: spe_list) {
//            if(!prob_id_list.contains(spe.getProb_id())) {
//                prob_id_list.add(spe.getProb_id());
//            }
//        }
//        List<Problem> problemList = new ArrayList<>();
//        for(Integer prob_id : prob_id_list) {
//            Problem problem = probDao.getProblemById(prob_id);
//            problemList.add(problem);
//        }
//        return problemList;
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

    @Override
    public List<Integer> getProbIdListByExerId(Integer exerId)
    {
        List<Integer> idls = stuProbExerDao.getProbIdListByExerID(exerId);
        List<Problem> pls = new ArrayList<>();
        for(Integer id: idls) {
            Problem p = probDao.getProblemById(id);
            if(!pls.contains(p) && id != -1){
                pls.add(p);
            }
        }
        pls.sort(Comparator.comparingInt(Problem::getType).thenComparingInt(Problem::getProb_id));
        List<Integer> res = new ArrayList<>();
        for(Problem p: pls) {
            res.add(p.getProb_id());
        }
        return res;
    }


    @Override
    public void save(Integer exerId, Integer userId, List<String> anslist) {

        int i = 0;

        List<Integer>probidls = getProbIdListByExerId(exerId);
        List<Problem> probls = new ArrayList<>();
        for(Integer prob_id: probidls) {
            probls.add(probDao.getProblemById(prob_id));
        }
        for(Problem probl: probls) {
            Integer prob_id = probl.getProb_id();
            if(StringUtil.isBlank(anslist.get(i)))
            {
                stuProbExerDao.updateSubmit(userId,prob_id,exerId,anslist.get(i),0);
            }
            else stuProbExerDao.updateSubmit(userId,prob_id,exerId,anslist.get(i),1);
            i += 1;
        }
    }

}
