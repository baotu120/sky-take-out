package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.*;

/**
 * @Author: Vic
 * @Create 2023-08-05 21:45
 */
@Slf4j
@Aspect
@Component
public class AutoFillAspect {
//    @Before("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
//    public void autoFillProperty(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        log.info("access AOP application, set communal arguments");
//        //1. get arguments of origin method
//        Object[] args = joinPoint.getArgs();
//        if (ObjectUtils.isEmpty(args)) {
//            return;
//        }
//        Object obj = args[0];
//
//        log.info("before set communal arguments, obj:{}",obj);
//        //2. through reflect get the method of this object
//        Method setCreateTime = obj.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
//        Method setUpdateTime = obj.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
//        Method setCreateUser = obj.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
//        Method setUpdateUser = obj.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);
//
//        //3. Get the value attribute of the annotation
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        AutoFill annotation = methodSignature.getMethod().getAnnotation(AutoFill.class);
//        OperationType operationType = annotation.value();
//
//        //4. if it's inserted
//        if (operationType.equals(OperationType.INSERT)) {
//            setCreateTime.invoke(obj, LocalDateTime.now());
//            setCreateUser.invoke(obj, BaseContext.getCurrentId());
//        }
//
//        //5. if it's update
//
//        setUpdateTime.invoke(obj, LocalDateTime.now());
//        setUpdateUser.invoke(obj, BaseContext.getCurrentId());
//        log.info("after set communal arguments, obj:{}",obj);
//
//    }

    @Before("execution(* com.sky.mapper.*.*(..)) && @annotation(autoFill)")
    public void autoFillProperty(JoinPoint joinPoint, AutoFill autoFill) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("access AOP application, set communal arguments");
        //1. get arguments of origin method
        Object[] args = joinPoint.getArgs();
        if (ObjectUtils.isEmpty(args)) {
            return;
        }
        Object obj = args[0];

        log.info("before set communal arguments, obj:{}",obj);
        //2. through reflect get the method of this object
        Method setCreateTime = obj.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
        Method setUpdateTime = obj.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
        Method setCreateUser = obj.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
        Method setUpdateUser = obj.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

        //3. Get the value attribute of the annotation

        OperationType operationType = autoFill.value();

        //4. if it's inserted
        if (operationType.equals(OperationType.INSERT)) {
            setCreateTime.invoke(obj, LocalDateTime.now());
            setCreateUser.invoke(obj, BaseContext.getCurrentId());
        }

        //5. if it's update

        setUpdateTime.invoke(obj, LocalDateTime.now());
        setUpdateUser.invoke(obj, BaseContext.getCurrentId());
        log.info("after set communal arguments, obj:{}",obj);

    }
}
