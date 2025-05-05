package com.se.dao;

import com.se.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("select * from t_user where user_id=#{user_id}")
    List<User> getUserByID(Integer user_id);

    @Select("select * from t_user where user_id=#{user_id}")
    User getSingleUserByID(Integer user_id);

    @Select("select * from t_user where username=#{username}")
    List<User> getUserByUsername(String username);

    List<User> getUserByCondition(@Param("identity") Integer identity, @Param("username") String username,@Param("mail") String mail,@Param("name") String name);
}
