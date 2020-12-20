package io.kimmking.rpcfx.demo.provider.annotation;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @aothor Master_PXu 
 * @date 时间 2020年12月16日下午3:31:30
 * @project_name 项目名 rpcfx-demo-provider
 * @type_name 类名 TimeCount
 * @function 功能 TODO
 */

@Aspect
@Component
@Slf4j
public class TimeCount {
	@Pointcut("@annotation(io.kimmking.rpcfx.demo.provider.annotation.Time)")
    public void agrsAop() {}


    @Around("agrsAop()")
    public Object doProcess(ProceedingJoinPoint point) throws Throwable {
        log.info("==>@Around begin----- ");
        long startTime = System.currentTimeMillis();
        Object object = point.proceed();
        long endTime = System.currentTimeMillis(); 
        log.info("==>@Around end-----,proceed {} ms",(endTime - startTime));
        return object;
    }
}
