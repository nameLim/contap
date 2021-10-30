package com.project.contap.aop;


import com.project.contap.util.AddLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class UseTimeAop {
    @Around("execution(public * com.project.contap.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        long startTime = System.currentTimeMillis();
        try {
            Object output = joinPoint.proceed();
            return output;
        } finally {
            long endTime = System.currentTimeMillis();
            long runTime = endTime - startTime;
            AddLog.addAPIUseTime(request,runTime);
        }
    }
}
