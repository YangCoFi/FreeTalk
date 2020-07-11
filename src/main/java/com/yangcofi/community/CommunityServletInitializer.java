package com.yangcofi.community;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @ClassName CommunityServletInitializer
 * @Description TODO
 * @Author YangC
 * @Date 2020/5/31 16:24
 **/
public class CommunityServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder){
        return builder.sources(CommunityApplication.class);
    }
}
