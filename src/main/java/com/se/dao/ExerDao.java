package com.se.dao;

import com.se.entity.Exercise;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ExerDao {


    /**
     * //根据class_id course_id 在 t_exer 中 查找所有的任务id -> List<Exerid>
     * @param class_id
     * @param course_id
     * @return
     */
    @Select("select exer_id from t_exer where class_id=#{class_id} and course_id=#{course_id}")
    List<Integer> getExerByStuIDAndExerID(@Param("class_id") Integer class_id, @Param("course_id") Integer course_id);

    @Select("select * from t_exer where class_id = #{classId}")
    List<Exercise> getExerByClass(Integer classId);
}
