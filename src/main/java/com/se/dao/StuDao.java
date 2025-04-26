package com.se.dao;

import com.se.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface StuDao {
    @Select("select * from t_user where identity = 1")
    List<User> getAllStudent();
}
