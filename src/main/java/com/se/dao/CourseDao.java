package com.se.dao;

import com.se.entity.Class;
import com.se.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CourseDao {

    /**
     * 根据 course 名称查询课程
     * @param name
     */
    @Select("select * from t_course where name = #{name}")
    List<Course> getCourseByName(String name);

    @Select("select * from t_course where course_id = #{course_id}")
    List<Course> getByID(Integer course_id);

    @Insert("insert into t_course(name,creator_id,syllabus,assMethod,score,time)" +
            " values(#{name}, #{creator_id}, #{syllabus}, #{assMethod}, #{score}, #{time})")
    @Options(useGeneratedKeys = true, keyProperty = "course_id")
    void add(Course course);

    @Select("select * from t_course where course_id != -1")
    List<Course> list();
}
