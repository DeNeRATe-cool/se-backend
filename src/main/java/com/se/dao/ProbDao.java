package com.se.dao;

import com.se.entity.Problem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProbDao {

    @Select("select * from t_prob where prob_id = #{id}")
    Problem getProblemById(Integer id);

    @Select("select * from t_prob where prob_id=#{prob_id}")
    List<Problem> selectByProbId(Integer prob_id);
}
