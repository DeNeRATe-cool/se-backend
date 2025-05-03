package com.se.service.impl;

import com.se.dao.ExerDao;
import com.se.dao.StuDao;
import com.se.dao.StuProbExerDao;
import com.se.dao.UserCourseClassDao;
import com.se.dto.StuProbExer;
import com.se.dto.UserCourseClass;
import com.se.entity.Class;
import com.se.entity.Exercise;
import com.se.entity.User;
import com.se.service.ExerService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            if(!stuProbExerDao.getNotCheckStuByExerID(exercise.getExer_id()).isEmpty())
                toCheckList.add(exercise);
        }

        return toCheckList;
    }

    @Override
    public List<User> getNotCheckedStu(Integer exerId) {
        List<Integer> stuIdList = stuProbExerDao.getNotCheckStuByExerID(exerId);
        return stuIdList.stream().map(id -> stuDao.getStudentById(id)).collect(Collectors.toList());
    }
}
