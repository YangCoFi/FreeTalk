package com.yangcofi.community;

import com.yangcofi.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @ClassName MailTests
 * @Description Test Mail Function
 * @Author YangC
 * @Date 2019/8/13 13:28
 **/

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;      //模板引擎的作用就是帮助我们生成一个动态网页

    @Test
    public void testTextMail(){
        mailClient.sendMail("yangcofi@163.com","Test", "welcome!!!");
    }

    @Test
    public void testHtmlMail(){
        Context context = new Context();
        context.setVariable("username", "van");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("974862203@qq.com", "html", content);
    }

}
