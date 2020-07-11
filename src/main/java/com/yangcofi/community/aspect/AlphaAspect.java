package com.yangcofi.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @ClassName AlphaAspect
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/29 13:48
 **/
@Component
@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.yangcofi.community.service.*.*(..))")        //*表示方法的返回值 这里是什么返回值都行 所有的service组件里面所有的方法  ..表示所有的参数  这些都要处理
    //定义切点 要把代码具体植入到业务Bean的那些地方
    public void pointCut(){
    }

    @Before("pointCut()")               //在切点(pointCut())前面植入代码
    public void before(){
//        System.out.println("before");
    }

    @After("pointCut()")
    public void after(){
//        System.out.println("after");
    }


    //有了返回值以后我还想处理一些逻辑
    @AfterReturning("pointCut()")
    public void afterReturning(){
//        System.out.println("afterReturning");
    }

    //在抛异常的时候植入代码
    @AfterThrowing("pointCut()")
    public void afterThrowing(){
//        System.out.println("afterThrowing");
    }

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{      //ProceedingJoinPoint 目标植入的部位 也就是连接点
//        System.out.println("around before");
        //连接点指代的是程序植入的部位
        Object obj = joinPoint.proceed();        //调用目标组件的方法。 目标组件可能有返回值
        //因为程序再执行的时候会植入一个代理对象 所以这个逻辑被植入到那个代理对象obj里面了 用来代替原始对象
//        System.out.println("around after");
        return obj;
    }
}


//在业务组件的一开始记录日志。