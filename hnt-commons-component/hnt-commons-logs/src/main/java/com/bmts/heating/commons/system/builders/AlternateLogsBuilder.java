//package com.bmts.heating.commons.system.builders;
//
//import com.bmts.heating.commons.system.logs.TsccLog;
//import com.bmts.heating.commons.system.pojo.LogDispose;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.Signature;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.core.annotation.AnnotationUtils;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
//@Aspect
//@Component
//public class AlternateLogsBuilder {
//
//    @Pointcut("@annotation(com.bmts.heating.commons.system.logs.TsccLog)")
//    public void exceptionCommandAnnotationPointcut() {}
//
//    @Pointcut("@annotation(com.bmts.heating.commons.system.logs.TsccLog)")
//    public void logsCommandAnnotationPointcut() {}
//
//    @Around("logsCommandAnnotationPointcut()")
//    public Object methodsAnnotatedWithLogs(final ProceedingJoinPoint joinPoint)throws Throwable{
//        Object backup=null;
//        //获取当前方法
//        Signature s = joinPoint.getSignature();
//        MethodSignature ms = (MethodSignature)s;
//        Method method =ms.getMethod();
//
//        //获取目标方法请求参数
//        Object[] arguments =joinPoint.getArgs();
//        //获得注解
//        TsccLog annotation = AnnotationUtils.findAnnotation(method, TsccLog.class);
//        String config=annotation.describe();
//        boolean falg=annotation.IsParameter();
//        LogDispose.LogLevel level=annotation.logLevel();
//
//        //存入flume
//        return joinPoint.proceed(arguments);
//    }
//
//    @AfterThrowing(throwing="ex",pointcut = "exceptionCommandAnnotationPointcut()")
//    public Object methodsAnnotatedWithException(JoinPoint joinPoint, Throwable ex)throws Throwable{
//        Object backup=null;
//
//        return backup;
//    }
//}
