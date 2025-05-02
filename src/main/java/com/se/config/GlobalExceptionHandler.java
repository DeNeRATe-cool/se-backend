package com.se.config;

import com.se.dto.Result;
import com.se.exception.ParamIllegalException;
import com.se.exception.ParamNotEnoughException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http2.HpackDecoder;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
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
     * @param ex
     * @return
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
     * @param ex
     * @return
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
     * @param ex
     * @return
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
     * @param ex
     * @return
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
     * @param ex
     * @return
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
     * @param ex
     * @return
     */
    @ExceptionHandler(UserPermissionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handleUserPermissionDenied(UserPermissionException ex)
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
}
