package com.arhibale.springstore.aspect;

import com.arhibale.springstore.aspect.annotations.Time;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeAspect {

    @Around("@annotation(time)")
    public Object timeMethod(ProceedingJoinPoint joinPoint, Time time) throws Throwable {
        long timeMillis = System.currentTimeMillis();
        Object o = joinPoint.proceed();
        System.out.printf("The method worked %d ms%n", System.currentTimeMillis() - timeMillis);
        return o;
    }
}