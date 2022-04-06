package com.example.logging.common.aop;

import com.example.logging.common.constant.LogType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        putLogType(LogType.ACTION);
        try {
            log.info(objectMapper.writeValueAsString(LogPost.of(request.getMethod(), request.getRequestURI(), convertJsonParam(joinPoint.getArgs()))));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
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

    @Getter
    @ToString
    protected static class LogPost {
        private final String method;
        private final String url;
        private final String args;

        private LogPost(String method, String url, String args) {
            this.method = method;
            this.url = url;
            this.args = args;
        }

        protected static LogPost of(String method, String url, String args) {
            return new LogPost(method, url, args);
        }
    }
}
