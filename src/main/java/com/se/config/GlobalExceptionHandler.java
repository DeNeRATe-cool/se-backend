package com.se.config;

import com.se.dto.Result;
import com.se.exception.EntityNotFoundException;
import com.se.exception.ParamIllegalException;
import com.se.exception.ParamNotEnoughException;
import com.se.exception.checkException.LengthNotMatchException;
import com.se.exception.checkException.NotCheckFinishException;
import com.se.exception.checkException.ScoreOutOfRangeException;
import com.se.exception.classException.ClassNotExistException;
import com.se.exception.classException.DuplicateClassException;
import com.se.exception.courseException.*;
import com.se.exception.resException.ResTypeArgumentException;
import com.se.exception.userException.UserNotFoundException;
import com.se.exception.userException.UserPermissionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 请求方式错误
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Result handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail("请求方法不支持：" + ex.getMethod());
    }

    /**
     * content-type错误
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public Result handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail("不支持的 Content-Type：" + ex.getContentType());
    }

    /**
     * 请求格式错误
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleNotReadable(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail("请求体格式错误：" + ex.getMessage());
    }

    /**
     * 参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.error(ex.getMessage(), ex);
        return Result.fail("参数类型不匹配：" + ex.getName());
    }

    /**
     * 处理主键冲突异常
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result handleDuplicateKey(DuplicateKeyException ex) {
        log.error("Duplicate key exception: {}", ex.getMessage(), ex);
        return Result.fail("主键冲突");
    }

    /**
     * 处理数据完整性违反异常（包括外键冲突等）
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleDataIntegrity(DataIntegrityViolationException ex) {
        log.error("Data integrity violation: {}", ex.getMessage(), ex);
        Throwable cause = ex.getRootCause();
        if (cause instanceof SQLException) {
            int errorCode = ((SQLException) cause).getErrorCode();
            if (errorCode == 1451 || errorCode == 1452) {
                return Result.fail("外键约束冲突");
            }
        }
        return Result.fail("数据完整性违反");
    }

    /**
     * 处理所有其他数据库访问异常
     */
    @ExceptionHandler(DataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleDataAccess(DataAccessException ex) {
        log.error("Database access exception: {}", ex.getMessage(), ex);
        return Result.fail("数据库访问异常");
    }

    /**
     * 对象不存在
     */
    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleNullPointer(NullPointerException ex) {
        log.error("NullPointer exception: {}", ex.getMessage(), ex);
        return Result.fail("访问对象不存在");
    }

    /**
     * 处理阿里云sso服务网络连接问题
     */
    @ExceptionHandler(ClientException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleClientException(ClientException ex) {
        log.error("Client exception: {}", ex.getMessage(), ex);
        return Result.fail("文件存储连接异常");
    }

    /**
     * 处理阿里云sso服务存储问题
     */
    @ExceptionHandler(OSSException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Result handleOSSException(OSSException ex) {
        log.error("OSS exception: {}", ex.getMessage(), ex);
        return Result.fail("文件对象存储时错误");
    }

    /**
     * 处理自定义异常 Parameters illegal
     */
    @ExceptionHandler(ParamIllegalException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleParamIllegal(ParamIllegalException ex) {
        log.error("Param illegal exception: {}", ex.getMessage(), ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 处理自定义异常 Parameters not enough
     */
    @ExceptionHandler(ParamNotEnoughException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleParamNotEnough(ParamNotEnoughException ex) {
        log.error("Param not enough exception: {}", ex.getMessage(), ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 创建重复班级 名称重复
     */
    @ExceptionHandler(DuplicateClassException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result handleDuplicateClass(DuplicateClassException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 创建重复课程
     */
    @ExceptionHandler(DuplicateCourseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Result handleDuplicateCourse(DuplicateCourseException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 班级和课程不匹配
     */
    @ExceptionHandler(CourseClassNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleCourseClassNotMatch(CourseClassNotMatchException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 用户不在课程中
     */
    @ExceptionHandler(UserNotInCourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleUserNotInCourse(UserNotInCourseException ex)
    {
        log.error(ex.getMessage(), ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 找不到用户
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleUserNotFound(UserNotFoundException ex)
    {
        log.error(ex.getMessage(), ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 用户权限错误
     */
    @ExceptionHandler(UserPermissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleUserPermissionDenied(UserPermissionException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(CourseNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleCourseNotFound(CourseNotFoundException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result handleIOException(IOException ex) {
        log.error("IO exception: {}", ex.getMessage(), ex);
        return Result.fail("IO流错误");
    }

    @ExceptionHandler(ClassNotExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleClassNotExist(ClassNotExistException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    // 学生重复加入课程
    @ExceptionHandler(DuplicateJoinCourseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleDuplicateJoinCourse(DuplicateJoinCourseException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 数据库查询不到
     * @param ex
     * @return
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleEntityNotFound(EntityNotFoundException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    @ExceptionHandler(DuplicateInvitationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleDuplicateInvitation(DuplicateInvitationException ex)
    {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 资源更新前后类型不统一
     */
    @ExceptionHandler(ResTypeArgumentException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public Result handleResTypeArgument(ResTypeArgumentException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 列表参数长度不匹配
     */
    @ExceptionHandler(LengthNotMatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleLengthNotMatch(LengthNotMatchException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 题目没有批改完成，无法提交
     */
    @ExceptionHandler(NotCheckFinishException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleNotCheckFinish(NotCheckFinishException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }

    /**
     * 批改分数超过题目范围
     */
    @ExceptionHandler(ScoreOutOfRangeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleScoreOutOfRange(ScoreOutOfRangeException ex) {
        log.error(ex.getMessage(),ex);
        return Result.fail(ex.getMessage());
    }
}
