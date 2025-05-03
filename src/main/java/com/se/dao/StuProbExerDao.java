package com.se.dao;

import com.se.dto.StuProbExer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StuProbExerDao {

    // 根据 user_id 和 exer_id 在 t_stu_prob_exer 查找 <stu_id , exer_id, if_finish> 对 exer_id 去重后计数

    /**
     * 根据 user_id 和 exer_id 在 t_stu_prob_exer 查找 <stu_id , exer_id, if_finish>
     * @param stu_id
     * @param exer_id
     * @return
     */
    @Select("select * from t_stu_prob_exer where stu_id=#{stu_id} and exer_id=#{exer_id}")
    List<StuProbExer> getStuProbExerByUserIDAndExerID(@Param("stu_id")Integer stu_id, @Param("exer_id")Integer exer_id);

    /**
     * 根据 user_id 和 exer_id 在 t_stu_prob_exer 查找 <stu_id , exer_id, is_finish> 获取 学生-任务 对应关系 probid = -1
     * @param stu_id
     * @param exer_id
     * @return
     */
    @Select("select * from t_stu_prob_exer where stu_id=#{stu_id} and exer_id=#{exer_id} and prob_id=-1")
    List<StuProbExer> getStuExerByUserIDAndExerID(@Param("stu_id")Integer stu_id, @Param("exer_id")Integer exer_id);

    /**
     * 根据 exer_id 查询 prob_id = -1, 且 is_check = 0 的学生 ID
      */
    @Select("select stu_id from t_stu_prob_exer where exer_id = #{exer_id} and prob_id = -1 and is_check = 0")
    List<Integer> getNotCheckStuByExerID(Integer exer_id);
}
