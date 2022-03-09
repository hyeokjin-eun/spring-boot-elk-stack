package com.example.logging.common.aop;

import com.example.logging.common.constant.LogType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {

    private final ObjectMapper objectMapper;

    @Pointcut("execution(* com.example.logging.*.api.*.*(..))")
    public void controllerPointCut() {}

    @Around("controllerPointCut()")
    public Object executionLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    @Before("controllerPointCut()")
    public void beforeLog(JoinPoint joinPoint) {
        MDC.put("t.id", String.valueOf(UUID.randomUUID()));
        putLogType(LogType.ACTION);
        log.info(convertJsonParam(joinPoint.getArgs()));
        putLogType(LogType.SERVICE);
    }

    @AfterReturning(pointcut = "controllerPointCut()", returning = "result")
    public void afterReturningLog(Object result) {
        putLogType(LogType.ACTION);
        log.info(convertJsonParam(result));
    }

    @AfterThrowing(pointcut = "controllerPointCut()")
    public void afterThrowingLog() {
        putLogType(LogType.ERROR);
    }

    private String convertJsonParam(Object args) {
        try {
            return objectMapper.writeValueAsString(args);
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }
    }

    private void putLogType(LogType logType) {
        MDC.put("l.type", String.valueOf(logType));
    }
}
