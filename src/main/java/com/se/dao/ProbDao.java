package com.se.dao;

import com.se.entity.Problem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProbDao {

    @Select("select * from t_prob where prob_id = #{id}")
    Problem getProblemById(Integer id);

    @Select("select * from t_prob where prob_id=#{prob_id}")
    List<Problem> selectByProbId(Integer prob_id);

    @Select("select * from t_prob where type = #{type} and is_public = True")
    List<Problem> getProbByType(Integer type);

    @Select("select * from t_prob where is_public = True")
    List<Problem> getAllPublicProb();

    @Insert("insert into t_prob(is_public,type,creator_id,description,content,answer,analysis,create_time)"+
    "values (#{is_public},#{type},#{creator_id},#{description},#{content},#{answer},#{analysis},now())")
//    @Options(useGeneratedKeys = true, keyProperty = "prob_id")
    int createProb(Problem problem);

    @Select("select * from t_prob where type = #{types} and creator_id = #{userid}")
    List<Problem> getSelfProb(@Param("userid") Integer userid,@Param("types") Integer types);

    @Select("select * from t_prob where creator_id=#{userid}")
    List<Problem> getSelfAllProb(@Param("userid") Integer userid);
}
