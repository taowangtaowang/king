package com.irs.annotation;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

@Aspect
public class DataSourceAspect {
	//配置接入点
    @Pointcut("execution(* com.irs.controller..*.*(..))")
    private void controllerAspect(){}//定义一个切入点
    @Before("controllerAspect()")
    public void dataSwitch(JoinPoint joinPoint){
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature =(MethodSignature) signature;
        Method method = methodSignature.getMethod();
        DataSource data = null;
        String dataSource = "";
        if(method != null){
            data = method.getAnnotation(DataSource.class);
            if(data != null){
                dataSource = data.dataSource();
                if(dataSource != null){
                    DynamicDataSource.setDataSourceKey(dataSource);
                }
            }
        }
    }
}
