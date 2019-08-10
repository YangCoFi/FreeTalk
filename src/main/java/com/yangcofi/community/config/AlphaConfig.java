package com.yangcofi.community.config;

/**
 * @ClassName AlphaConfig
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/10 20:19
 **/

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
}
