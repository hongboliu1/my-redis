package com.ai.redis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuhb
 * @date 2019-11-20 22:55
 */
@Aspect
public class AopJBean {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Pointcut(value = "execution(* com.ai.redis.model.MyTestBean.*(..)))")
    public void test() {

    }

    @Before("test()")
    public void beforeTest() {
        System.out.println("beforeTest");
    }

    @After("test()")
    public void afterTest() {
        System.out.println("afterTest");
    }

    @Around("test()")
    public Object aroundTest(ProceedingJoinPoint point) {
        System.out.println("before1");
        Object o = null;
        try {
            o = point.proceed();
        } catch (Throwable e) {
            logger.error(e.getMessage(), e);
        }
        System.out.println("after1");
        return o;
    }
}
