package com.se.dao;

import com.se.entity.Class;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ClassDao {

    @Select("select * from t_class where name = #{name}")
    List<Class> getClassEntityByName(String name);

    @Select("select * from t_class where class_code=#{class_code}")
    List<Class> getClassEntityByClassCode(String class_code);

    @Select("select * from t_class where class_id=#{class_id}")
    List<Class> getClassEntityByClassId(Integer class_id);

    @Insert("insert into t_class(course_id,name,class_code)" +
            " values(#{course_id},#{name},#{class_code})")
    @Options(useGeneratedKeys = true, keyProperty = "class_id")
    void add(Class classEntity);

    @Select("select * from t_class where class_id!=-1")
    List<Class> list();
}
