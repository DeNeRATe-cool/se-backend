package com.se.service.impl;

import cn.hutool.core.date.DateTime;
import com.se.constant.ExerEntityConstant;
import com.se.constant.ProblemEntityConstant;
import cn.hutool.core.date.DateUtil;
import com.se.dao.ExerDao;
import com.se.dao.StuDao;
import com.se.dao.StuProbExerDao;
import com.se.dao.UserCourseClassDao;
import com.se.dto.CreateExerDTO;
import com.se.dto.StuProbExer;
import com.se.dto.UserCourseClass;
import com.se.entity.Exercise;
import com.se.entity.Problem;
import com.se.entity.User;
import com.se.exception.checkException.ScoreOutOfRangeException;
import com.se.exception.ParamIllegalException;
import com.se.service.ExerService;
import com.se.utils.ProblemUtil;
import com.se.service.StuProbExerService;
import com.se.service.UserCourseClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class ExerServiceImpl implements ExerService {

    @Autowired
    private StuDao stuDao;

    @Autowired
    private ExerDao exerDao;

    @Autowired
    private StuProbExerDao stuProbExerDao;

    @Autowired
    private UserCourseClassDao userCourseClassDao;

    @Autowired
    private UserCourseClassService userCourseClassService;

    @Autowired
    private StuProbExerService stuProbExerService;

    /**
     * 根据class_id course_id 在 t_exer 中 查找所有的任务id -> List<Exerid>
     * 根据 user_id 和 exer_id 在 t_stu_prob_exer 查找 <stu_id , exer_id, if_finish> 对 exer_id 去重后计数
     * @param user_id
     * @param course_id
     * @param class_id
     * @return
     */

    @Override
    public List<Integer> getStuFinishExerNum(Integer user_id, Integer course_id, Integer class_id) {
        // 找到课程和班级对应 的 任务列表
        List<Integer> exerIDList = exerDao.getExerByStuIDAndExerID(class_id,course_id);
        List<StuProbExer> stuProbExerList = null;
        List<StuProbExer> list = null;
        Integer finish_cnt = 0;
        for(Integer exerid: exerIDList)
        {
            list = stuProbExerDao.getStuExerByUserIDAndExerID(user_id, exerid);
            if(list.get(0).getIs_finish())finish_cnt += 1;
        }
        List<Integer>res = new ArrayList<>();
        res.add(finish_cnt);
        res.add(exerIDList.size());
        return res;
    }

    @Override
    public List<Exercise> getNotCheckedExercise(Integer courseId, Integer userId) {
        // 获取需查询班级
        List<Integer> classList = new ArrayList<>();
        List<UserCourseClass> originList = new ArrayList<>();
        if(courseId == null || courseId < 0) {
            originList.addAll(userCourseClassDao.getListByUserIdAndIdentity(userId, 0));
            originList.addAll(userCourseClassDao.getListByUserIdAndIdentity(userId, 2));
        } else {
            originList.addAll(userCourseClassDao.getListByUserIDAndCourseIDAndIdentity(userId, courseId, 0));
            originList.addAll(userCourseClassDao.getListByUserIDAndCourseIDAndIdentity(userId, courseId, 2));
        }
        for(UserCourseClass userCourseClass: originList)
            classList.add(userCourseClass.getClass_id());
        // 去重以防同一个老师/助教多次出现在一个班级中
        classList = new ArrayList<>(new HashSet<>(classList));

        // 获取任务
        List<Exercise> exerListAll = new ArrayList<>();
        for(Integer classId : classList)
            exerListAll.addAll(exerDao.getExerByClass(classId));

        // 获取存在为批改的任务
        List<Exercise> toCheckList = new ArrayList<>();
        for(Exercise exercise : exerListAll) {
            if(
                    !stuProbExerDao.getNotCheckStuByExerID(exercise.getExer_id()).isEmpty() &&
                    DateUtil.compare(exerDao.getEndTime(exercise.getExer_id()), new Date()) <= 0
            )
                toCheckList.add(exercise);
        }

        return toCheckList;
    }

    @Override
    public List<User> getNotCheckedStu(Integer exerId) {
        List<Integer> stuIdList = stuProbExerDao.getNotCheckStuByExerID(exerId);
        return stuIdList.stream().map(id -> stuDao.getStudentById(id)).collect(Collectors.toList());
    }

    @Override
    public List<List<?>> getCheckInfo(List<StuProbExer> baseList, List<Problem> proList, List<StuProbExer> stuExerList, Integer userId) {
        OptionProblemCheck(baseList, proList, stuExerList, userId);
        List<List<?>> infoList = new ArrayList<>();
        infoList.add(proList);
        infoList.add(
                stuExerList.stream().map(StuProbExer::getScore).collect(Collectors.toList())
        );
        infoList.add(
                stuExerList.stream().map(StuProbExer::getComment).collect(Collectors.toList())
        );
        infoList.add(
                stuExerList.stream().map(StuProbExer::getIs_check).collect(Collectors.toList())
        );
        return infoList;
    }

    @Override
    public void submitCheckInfo(Integer userId, Integer exerId, List<Integer> scores, List<String> infos, List<StuProbExer> baseList, List<StuProbExer> stuExerList) {
        Integer totalScore = 0;
        for(int i = 0; i < baseList.size(); i++) {
            if(scores.get(i) < 0 || scores.get(i) > baseList.get(i).getScore())
                throw new ScoreOutOfRangeException("第 " + (i + 1) + " 题分数不在题目合规范围内");
            totalScore += scores.get(i);
            stuProbExerDao.updateScoreByUserIDAndProbIDAndExerID(userId, baseList.get(i).getProb_id(), exerId, scores.get(i));
            stuProbExerDao.updateCheckInfoByUserIDAndProbIDAndExerID(userId, baseList.get(i).getProb_id(), exerId, infos.get(i));
        }
        stuProbExerDao.updateTotalScoreByUserIDAndExerID(userId, exerId, totalScore);
    }

    private void OptionProblemCheck(List<StuProbExer> baseList, List<Problem> proList, List<StuProbExer> stuExerList, Integer userId) {
        for(int i = 0; i < proList.size(); i++) {
            Problem problem = proList.get(i);
            StuProbExer stuProbExer = stuExerList.get(i);
            // 是否为可以自动批改的题目
            if(ProblemUtil.canBeCheckedByAuto(problem.getType())) {
                // 自动判断确定的答案是否正确
                boolean correct = stuProbExer.getSubmit().equals(problem.getAnswer());
                int newScore = correct ? baseList.get(i).getScore() : 0;
                stuProbExerDao.updateScoreByUserIDAndProbIDAndExerID(
                        userId,
                        problem.getProb_id(),
                        stuProbExer.getExer_id(),
                        newScore
                );
                stuProbExer.setScore(newScore);
                stuProbExer.setIs_check(true);
            }
        }
    }

    /**
     * 创建任务
     * 验证参数
     * 将题目列表 逐条 插入到 stu-prob-exer
     * @param createExerDTO
     * @return
     */
    @Override
    public Exercise create(CreateExerDTO createExerDTO) {
        Integer course_id = createExerDTO.getCourse_id();
        Integer class_id = createExerDTO.getClass_id();
        Integer creator_id = createExerDTO.getCreator_id();
        DateTime beginTime = createExerDTO.getBegin_time();
        DateTime endTime = createExerDTO.getEnd_time();
        Boolean is_public = createExerDTO.getIs_public();
        String name = createExerDTO.getName();
        Boolean is_multi = createExerDTO.getIs_multi();
        List<Integer> probs = createExerDTO.getProbs();
        List<Integer> scores = createExerDTO.getScores();

        userCourseClassService.checkCourseAndClass(course_id,class_id);
        userCourseClassService.checkIsAdminForCourse(creator_id,course_id);

        if(!endTime.isAfter(beginTime))
        {
            throw new ParamIllegalException(ExerEntityConstant.EndTimeEarlierThenBeginTime);
        }

        if(probs.size() != scores.size())
        {
            throw new ParamIllegalException(ExerEntityConstant.PROBS_SCORES_LENGTH_NOT_MATCH);
        }

        int probs_size = probs.size();
        stuProbExerService.checkProbListExist(probs);
        int sum = 0;
        for(int i = 0;i < probs_size;i++){
            sum += scores.get(i);
        }

        Exercise exercise = new Exercise();
        exercise.setClass_id(class_id);
        exercise.setCreator_id(creator_id);
        exercise.setBegin_time(beginTime);
        exercise.setEnd_time(endTime);
        exercise.setIs_public(is_public);
        exercise.setName(name);
        exercise.setCourse_id(course_id);
        exercise.setIs_multi(is_multi);
        exercise.setScore(sum);

        exerDao.insert(exercise);
        Integer exer_id = exercise.getExer_id();

        for(int i = 0;i < probs_size; ++i)
        {
            Integer prob_id = probs.get(i);
            Integer score = scores.get(i);
//            stuProbExerDao.createInsertStuProbExer(prob_id,);
            stuProbExerDao.createInsertStuProbExer(prob_id, exer_id, score, i+1);
        }
        return exercise;
    }
}
