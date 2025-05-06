package com.se.dao;

import com.se.dto.StuProbExer;
import com.se.entity.Problem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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

    @Insert("insert into t_stu_prob_exer(stu_id,prob_id,exer_id,score,is_finish,is_check,idx) " +
            "values(-1,#{prob_id},#{exer_id},#{score},0,0,#{idx})")
    void createInsertStuProbExer(@Param("prob_id") Integer prob_id, @Param("exer_id") Integer exer_id, @Param("score") Integer score, @Param("idx") Integer idx);

    /**
     * 根据 exer_id 查询练习中包含的题目 ID 列表
     */
    @Select("select * from t_stu_prob_exer where exer_id = #{exerId} and stu_id = -1")
    List<StuProbExer> getProblemListByExerID(Integer exerId);

    /**
     * 任务可以不是模板任务
     * @param exer_id
     * @return
     */
    @Select("select * from t_stu_prob_exer where exer_id = #{exerId}")
    List<StuProbExer> getProblemListByExerIDBroaden(Integer exer_id);

    /**
     * 通过学习 + 题目 + 练习查询记录
     */
    @Select("select * from t_stu_prob_exer where stu_id = #{userId} and prob_id = #{probId} and exer_id = #{exerId}")
    StuProbExer getInfoByUserIDAndProbIDAndExerID(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("exerId") Integer exerId);

    /**
     * 更新学生的练习中题目的分数
     */
    @Update("update t_stu_prob_exer set is_check = 1, score = #{score} where stu_id = #{userId} and prob_id = #{probId} and exer_id = #{exerId}")
    void updateScoreByUserIDAndProbIDAndExerID(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("exerId") Integer exerId, @Param("score") Integer score);

    /**
     * 更新学生的练习中题目的反馈信息
     */
    @Update("update t_stu_prob_exer set is_check = 1, comment = #{info} where stu_id = #{userId} and prob_id = #{probId} and exer_id = #{exerId}")
    void updateCheckInfoByUserIDAndProbIDAndExerID(@Param("userId") Integer userId, @Param("probId") Integer probId, @Param("exerId") Integer exerId, @Param("info") String info);

    /**
     * 更新学生的练习总分
     */
    @Update("update t_stu_prob_exer set is_check = 1, score = #{score} where stu_id = #{userId} and exer_id = #{exerId} and prob_id = -1")
    void updateTotalScoreByUserIDAndExerID(@Param("userId") Integer userId, @Param("exerId") Integer exerId, @Param("score") Integer score);

    /**
     * 获取任务的所有学生练习结果
     */
    @Select("select * from t_stu_prob_exer where exer_id = #{exerId} and prob_id = -1")
    List<StuProbExer> getStuResultByExerID(Integer exerId);

    /**
     * 通过任务 + 题目获取所有批改过的学生某道题的结果
     */
    @Select("select * from t_stu_prob_exer where exer_id = #{exerId} and prob_id = #{probId} and stu_id <> -1 and is_check = 1")
    List<StuProbExer> getStuProbResultByExerIDAndProbID(@Param("exerId") Integer exerId, @Param("probId") Integer probId);

    @Insert("insert into t_stu_prob_exer(stu_id,prob_id,exer_id,score,comment,submit,is_check,is_finish,idx) " +
            "values(#{stu_id},#{prob_id},#{exer_id},#{score},#{comment},#{submit},#{is_check},#{is_finish},#{idx})")
    void insert(StuProbExer stuProbExer);

    @Select("select * from t_stu_prob_exer where stu_id=#{userId} and prob_id=-1")
    List<StuProbExer> getByStuIdWithProbInval(Integer userId);
}
