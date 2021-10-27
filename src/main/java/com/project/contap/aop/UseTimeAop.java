package com.project.contap.aop;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.stereotype.Component;

@Aspect
@Component
public class UseTimeAop {
    @Around("execution(public * com.project.contap.controller..*(..))")
    public Object execute(ProceedingJoinPoint joinPoint) throws Throwable {
        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 핵심기능 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료 시간 - 시작 시간
            long runTime = endTime - startTime;

            Logger log = LogManager.getLogger("APITime");
            log.error(runTime+"ms");

            // 로그인 회원이 없는 경우, 수행시간 기록하지 않음
//            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//            if (auth != null && auth.getPrincipal().getClass() == UserDetailsImpl.class) {
//                // 로그인 회원 정보
//                UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
//                User loginUser = userDetails.getUser();
//
//                // API 사용시간 및 DB 에 기록
//                ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser)
//                        .orElse(null);
//                if (apiUseTime == null) {
//                    // 로그인 회원의 기록이 없으면
//                    apiUseTime = new ApiUseTime(loginUser, runTime);
//                } else {
//                    // 로그인 회원의 기록이 이미 있으면
//                    apiUseTime.addUseTimeandTotalCount(runTime);
//                }
//
//                System.out.println("[API Use Time] Username: " + loginUser.getUsername() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
//                apiUseTimeRepository.save(apiUseTime);
//            }
        }
    }



}
