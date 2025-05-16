package com.se.dao;

import com.se.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM t_user WHERE username = #{value} OR mail = #{value} LIMIT 1")
    User findByAccount(@Param("value") String account);

    @Insert("insert into t_user (username,password,name,mail,birthday,identity) values (#{username},#{password},#{name},#{mail},#{birthday},#{identity})")
    void insert(User user);

    @Select("select * from t_user where user_id=#{user_id}")
    List<User> getUserByID(Integer user_id);

    @Select("select * from t_user where user_id=#{user_id}")
    User getSingleUserByID(Integer user_id);

    @Select("select * from t_user where username=#{username}")
    List<User> getUserByUsername(String username);

    List<User> getUserByCondition(@Param("identity") Integer identity, @Param("username") String username,@Param("mail") String mail,@Param("name") String name);

    @Select("select * from t_user where user_id=#{user_id}")
    User findById(Integer user_id);

    @Update("UPDATE t_user SET password = #{password}, name = #{name}, mail = #{mail}, birthday = #{birthday} WHERE user_id = #{user_id}")
    void updateUser(User user);
}
