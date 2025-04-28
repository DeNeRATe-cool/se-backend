package com.se.dao;

import com.se.entity.Process;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface ProcessDao {
    @Options(useGeneratedKeys = true, keyProperty = "process_id", keyColumn = "process_id")
    @Insert("insert into t_process (name, course_id, class_id, time) values (#{name}, #{course_id}, #{class_id}, #{time})")
    void insert(Process process);
}
