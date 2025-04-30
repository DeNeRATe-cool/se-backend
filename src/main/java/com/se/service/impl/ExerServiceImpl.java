package com.se.service.impl;

import com.se.dao.ExerDao;
import com.se.dao.StuProbExerDao;
import com.se.dto.StuProbExer;
import com.se.service.ExerService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExerServiceImpl implements ExerService {

    @Autowired
    private ExerDao exerDao;

    @Autowired
    private StuProbExerDao stuProbExerDao;

    /**
     * 根据class_id course_id 在 t_exer 中 查找所有的任务id -> List<Exerid>
     * 根据 user_id 和 exer_id 在 t_stu_prob_exer 查找 <stu_id , exer_id, if_finish> 对 exer_id 去重后计数
     * @param user_id
     * @param course_id
     * @param class_id
     * @return
     */
    @Override
    public List<Integer> getStuFinishExerNum(int user_id, int course_id, int class_id) {
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
}
