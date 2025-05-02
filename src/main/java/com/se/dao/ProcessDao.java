package com.se.dao;

import com.se.entity.Process;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessDao {
    @Options(useGeneratedKeys = true, keyProperty = "process_id", keyColumn = "process_id")
    @Insert("insert into t_process (name, course_id, class_id, time) values (#{name}, #{course_id}, #{class_id}, #{time})")
    void insert(Process process);

    @Select("select * from t_process where course_id = #{course_id} and class_id = #{class_id}")
    List<Process> queryByClass(@Param("course_id") Integer course_id, @Param("class_id") Integer class_id);
}
