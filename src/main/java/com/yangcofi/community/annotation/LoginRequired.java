package com.yangcofi.community.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)               //ElementType.METHOD即这个注解可以写在方法上，因为拦截器拦截的就是方法
@Retention(RetentionPolicy.RUNTIME)        //声明这个注解在程序运行的时候才有效
public @interface LoginRequired {
    //打上这个标记即表示登陆的时候才能访问
}
