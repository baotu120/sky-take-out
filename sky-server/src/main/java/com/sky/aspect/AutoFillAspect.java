package com.sky.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @Author: Vic
 * @Create 2023-08-05 21:45
 */
@Slf4j
@Aspect
@Component
public class AutoFillAspect {
    @Before("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillProperty(JoinPoint joinPoint){
        //1. get arguments of origin method
        Object[] args = joinPoint.getArgs();
        if (ObjectUtils.isEmpty(args)){
            return;
        }
        Object obj = args[0];
        //2. through reflect get the method of this object

    }
}
