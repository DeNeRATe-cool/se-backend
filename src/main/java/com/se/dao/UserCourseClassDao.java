package com.se.dao;

import com.se.dto.UserCourseClass;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserCourseClassDao {

    @Select("select * from t_user_course_class where class_id = -1 and course_id = #{course_id} and identity = #{identity}")
    List<UserCourseClass> getUserListByCourseIDAndIdentityCodeIgnoreClassID(@Param("course_id")Integer course_id, @Param("identity")Integer identity);

    @Select("select * from t_user_course_class where course_id=#{course_id}")
    List<UserCourseClass>getUserListByCourseID(@Param("course_id")Integer course_id);

    @Select("select * from t_user_course_class where course_id = #{course_id} and identity = #{identity}")
    List<UserCourseClass> getStudentClassList(@Param("course_id")Integer course_id,@Param("identity")Integer identity);

    @Select("select * from t_user_course_class where user_id=#{user_id} and course_id=#{course_id} and " +
            "class_id=#{class_id}")
    List<UserCourseClass> select(@Param("user_id")Integer user_id,@Param("course_id")Integer course_id,
                                 @Param("class_id")Integer class_id);


    @Insert("insert into t_user_course_class(user_id, course_id, class_id, identity)" +
            " values(#{user_id},#{course_id},#{class_id},#{identity})")
    void add(@Param("user_id") Integer user_id, @Param("course_id") Integer course_id, @Param("class_id")Integer class_id, @Param("identity")Integer identity);

    @Select("select * from t_user_course_class where class_id=#{class_id} and user_id != -1")
    List<UserCourseClass> getUserListByClassID(@Param("class_id")Integer class_id);


    @Select("select * from t_user_course_class where user_id=-1 and course_id=#{course_id} and " +
            "class_id=#{class_id}")
    List<UserCourseClass> getCourseClassList(@Param("course_id") Integer course_id,@Param("class_id") Integer class_id);

    @Select("select * from t_user_course_class where user_id=#{user_id} and course_id=#{course_id} and " +
            "identity=#{identity}")
    List<UserCourseClass> getListByUserIDAndCourseIDAndIdentity(@Param("user_id") Integer user_id,@Param("course_id") Integer course_id,
                                                                @Param("identity") Integer identity);


    @Select("select * from t_user_course_class where user_id=-1 and course_id=#{course_id}")
    List<UserCourseClass> getClassListByCourse(@Param("course_id")Integer course_id);

    @Select("select * from t_user_course_class where user_id=#{user_id}")
    List<UserCourseClass> getByUserID(@Param("user_id") Integer user_id);

    @Delete("delete from t_user_course_class where course_id=#{course_id} and " +
            " class_id=#{class_id} and user_id=#{user_id}")
    void delete(@Param("course_id") Integer course_id, @Param("class_id") Integer class_id, @Param("user_id") Integer user_id);

    @Select("select * from t_user_course_class where class_id=#{class_id} and user_id=-1")
    List<UserCourseClass> getCourseByClass(@Param("class_id") Integer class_id);
}
