package com.se.dao;

import com.se.constant.UserTypeEnum;
import com.se.dto.UserCourseClass;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserCourseClassDao {

    @Select("select * from t_user_course_class where class_id = -1 and course_id = #{course_id} and identity = #{identity}")
    List<UserCourseClass>getUserListByCourseIDAndIdentityCode(@Param("course_id")Integer course_id, @Param("identity")Integer identity);

    @Select("select * from t_user_course_class where course_id = #{course_id} and identity = #{identity}")
    List<UserCourseClass> getStudentClassList(@Param("course_id")Integer course_id,@Param("identity")Integer identity);
}
