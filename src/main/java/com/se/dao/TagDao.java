package com.se.dao;

import com.se.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TagDao {

    @Select("select * from t_tag")
    List<Tag> findAll();
}
