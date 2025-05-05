package com.se.dao;

import com.se.dto.WrongInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CollectDao {
    @Select("select prob_id from t_collect where stu_id = #{stu_id}")
    List<Integer> getCollectByStuId(Integer stu_id);

    @Insert("insert into t_collect (stu_id, prob_id) values (#{stuId}, #{probId})")
    void insertCollect(@Param("stuId") Integer stuId, @Param("probId") Integer probId);

    @Select("select prob_id from t_collect where stu_id = #{stuId} and prob_id = #{probId}")
    Integer getIfExist(@Param("stuId") Integer stuId, @Param("probId") Integer probId);

    @Delete("delete from t_collect where stu_id = #{stuId} and prob_id = #{probId}")
    void deleteCollect(@Param("stuId") Integer stuId, @Param("probId") Integer probId);

    @Select("select prob_id, exer_id from t_wrong where stu_id = #{stu_id}")
    List<WrongInfo> getWrongByStuId(Integer stu_id);

    @Insert("insert into t_wrong (stu_id, prob_id, exer_id) values (#{stuId}, #{probId}, #{exerId})")
    void insertWrong(@Param("stuId") Integer stuId, @Param("probId") Integer probId, @Param("exerId") Integer exerId);
}
