package com.yangcofi.community.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @ClassName WkConfig
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/9 16:51
 **/
@Configuration
public class WkConfig {

    private static final Logger logger = LoggerFactory.getLogger(WkConfig.class);

    @Value("${wk.image.storage}")
    private String wkImageStorage;

    @PostConstruct
    public void init(){
        //创建wk图片目录
        File file = new File(wkImageStorage);
        if (!file.exists()){
            file.mkdir();
            logger.info("创建wk图片目录: " + wkImageStorage);
        }
    }
}
