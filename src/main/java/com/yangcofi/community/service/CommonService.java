package com.yangcofi.community.service;

import com.yangcofi.community.dao.CommonMapper;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.util.MailClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @ClassName CommonService
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/10 16:00
 **/
@Service
public class CommonService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private CommonMapper commonMapper;

    public void sendVerifyEmail(String email, String verCode){
        Context context = new Context();
        context.setVariable("email", email);
        context.setVariable("verCode", verCode);
        //利用模板引擎生成邮件的内容
        String content = templateEngine.process("/mail/verify", context);
        mailClient.sendMail(email, "修改密码验证邮件", content);
    }

    public User getUserByEmail(String email) {
        return commonMapper.getUserByEmail(email);
    }

    public void updatePwdById(int id, String password) {
        commonMapper.updatePwdById(id, password);
    }
}
