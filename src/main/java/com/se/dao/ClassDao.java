package com.se.dao;

import com.se.entity.Class;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClassDao {

    @Select("select * from t_class where name = #{name}")
    List<Class> getClassEntityByName(String name);

    @Select("select * from t_class where class_code=#{class_code}")
    List<Class> getClassEntityByClassCode(String class_code);

    @Insert("insert into t_class(course_id,name,class_code)" +
            " values(#{course_id},#{name},#{class_code})")
    void add(Class classEntity);

}
