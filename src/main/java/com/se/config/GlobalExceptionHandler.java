package com.se.config;

import com.se.dto.Result;
import com.se.exception.ParamIllegalException;
import com.se.exception.ParamNotEnoughException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
