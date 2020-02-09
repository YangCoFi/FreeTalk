package com.yangcofi.community.config;

import com.yangcofi.community.controller.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName WebMvcConfig
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/16 19:56
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //之前在配置类里主要是声明一个第三方的Bean
    //这里要实现一个接口
    @Autowired
    private AlphaInterceptor alphaInterceptor;

    @Autowired
    private LoginTicketInterceptor loginTicketInterceptor;

    @Autowired
    private MessageInterceptor messageInterceptor;

    @Autowired
    private DataInterceptor dataInterceptor;

    //这是之前的拦截器方案，现在我们用security来代替
//    @Autowired
//    LoginRequiredInterceptor loginRequiredInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(alphaInterceptor)      //把这个Bean添加给他就可以了
//                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg")         //浏览器访问静态资源时可以随便访问，不去拦截，因为没有业务逻辑.所有目录下的css等文件都要排除掉
//                .addPathPatterns("/register", "/login");

        registry.addInterceptor(loginTicketInterceptor)      //把这个Bean添加给他就可以了
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");         //浏览器访问静态资源时可以随便访问，不去拦截，因为没有业务逻辑.所有目录下的css等文件都要排除掉;

//        registry.addInterceptor(loginRequiredInterceptor)      //把这个Bean添加给他就可以了
//                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");         //浏览器访问静态资源时可以随便访问，不去拦截，因为没有业务逻辑.所有目录下的css等文件都要排除掉;

        registry.addInterceptor(messageInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");

        registry.addInterceptor(dataInterceptor)
                .excludePathPatterns("/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/**/*.jpeg");
    }
}
