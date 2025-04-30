package com.se.dao;

import com.se.entity.Class;
import com.se.entity.Course;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
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

    @Insert("insert into t_course(name,creator_id,syllabus,assMethod,score,time)" +
            " values(#{name}, #{creator_id}, #{syllabus}, #{assMethod}, #{score}, #{time})")
    void add(Course course);

}
