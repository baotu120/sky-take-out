package com.sky.handler;

import com.sky.constant.MessageConstant;
import com.sky.exception.BaseException;
import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler to handle business exceptions thrown in the project
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * catch business exception
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(BaseException ex){
        log.error("Exception information：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    /**
     * catch DuplicateKeyException, error will arise when duplicate fields in the database
     * @param ex
     * @return
     */
    @ExceptionHandler
    public Result exceptionHandler(DuplicateKeyException ex){
        log.error("Exception information：{}", ex.getMessage());
        String errorMessage = MessageConstant.UNKNOWN_ERROR;
        String message = ex.getCause().getMessage();
        if (StringUtils.hasLength(message)){
            String[] msgs = message.split(" ");
            errorMessage = msgs[2] + MessageConstant.ALREADY_EXISTS;
        }
        return Result.error(errorMessage);
    }

}
