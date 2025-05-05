package com.se.dao;

import com.se.entity.Exercise;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface ExerDao {

    @Select("select * from t_exer where exer_id = #{id}")
    Exercise getExerById(Integer id);

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

    @Insert("insert into t_exer(class_id,course_id,creator_id,begin_time,end_time,is_public,name,is_multi,score) " +
            "values(#{class_id},#{course_id},#{creator_id},#{begin_time},#{end_time},#{is_public}," +
            "#{name},#{is_multi},#{score})")
    @Options(useGeneratedKeys = true, keyProperty = "exer_id") // 正确
    void insert(Exercise exercise);

    @Select("select end_time from t_exer where exer_id = #{exerId};")
    Date getEndTime(Integer exerId);
}
