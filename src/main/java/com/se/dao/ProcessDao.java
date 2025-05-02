package com.se.dao;

import com.se.entity.Process;
import com.se.entity.Resource;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ProcessDao {
    @Options(useGeneratedKeys = true, keyProperty = "process_id", keyColumn = "process_id")
    @Insert("insert into t_process (name, course_id, class_id, time) values (#{name}, #{course_id}, #{class_id}, #{time})")
    void insert(Process process);

    @Select("select * from t_process where course_id = #{course_id} and class_id = #{class_id}")
    List<Process> queryByClass(@Param("course_id") Integer course_id, @Param("class_id") Integer class_id);

    @Insert("insert into t_resource (res_code, name, process_id, course_id, type, url, date, tag, class_id) values" +
            "(#{res_code}, #{name}, #{process_id}, #{course_id}, #{type}, #{url}, #{date}, #{tag}, #{class_id}) ")
    void addResource(
            @Param("res_code") String resCode,
            @Param("name") String fileName,
            @Param("process_id") Integer process_id,
            @Param("course_id") Integer course_id,
            @Param("class_id") Integer class_id,
            @Param("type") String type,
            @Param("url") String url,
            @Param("date") String now,
            @Param("tag") String tags);

    @Select("select url from t_resource where res_id = #{arg0}")
    String getResourceUrl(Integer id);

    @Select("select * from t_resource where process_id = #{processId} and course_id = #{courseId}")
    List<Resource> getResourceByProcess(@Param("processId") Integer processId, @Param("courseId") Integer courseId);
}
