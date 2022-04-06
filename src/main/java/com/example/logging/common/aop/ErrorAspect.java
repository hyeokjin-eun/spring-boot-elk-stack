package com.example.logging.common.aop;

import com.example.logging.common.constant.LogType;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Slf4j
//@Aspect
//@Component
@RequiredArgsConstructor
public class ErrorAspect {

    @Pointcut("execution(* com.example.logging.common.error.GlobalExceptionHandler.*(..))")
    public void errorPointCut() {}

    @Around("errorPointCut()")
    public Object executionLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    @Before("errorPointCut()")
    public void beforeLog() {
        putLogType(LogType.ERROR);
    }

    private void putLogType(LogType logType) {
        MDC.put("l.type", String.valueOf(logType));
    }
}
