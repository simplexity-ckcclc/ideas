/**
 * Author:  huangyucong <huangyucong@sensetime.com>
 * Created: 17-11-3
 */

package com.ckcclc.springboot.config;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(RequestAspect.class);

    @Pointcut("@annotation(com.ckcclc.springboot.common.CompleteMark)")
    public void complete(){}

    @After("complete()")
    public void after() {
        logger.info("complete");
    }

//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")
//    public void throwException(){}
//
//    @AfterThrowing(pointcut = "throwException()", throwing="e")
//    public void doAfterThrowingTask(JoinPoint joinPoint, Exception e){
//        Object[] args = joinPoint.getArgs();
//        System.out.println("size: " + args.length);
//        for (Object object : args) {
//            System.out.println(object.getClass().getName());
//            if (object instanceof Person) {
//                System.out.println(((Person)object).getAge());
//            }
//        }
//        // you can intercept thrown exception here.
//    }
}
