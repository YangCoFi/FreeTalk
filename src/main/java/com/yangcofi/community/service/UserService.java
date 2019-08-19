package com.yangcofi.community.service;

import com.yangcofi.community.dao.LoginTicketMapper;
import com.yangcofi.community.dao.UserMapper;
import com.yangcofi.community.entity.LoginTicket;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.util.CommunityConstant;
import com.yangcofi.community.util.CommunityUtil;
import com.yangcofi.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService implements CommunityConstant {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Value("${community.path.domain}")                //因为这里是注入一个固定的值，而不是一个bean，所以用Value()注解
    private String domain;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public User findUserById(int id) {
        return userMapper.selectById(id);
    }

    //把返回的结果封装在Map里面
    public Map<String, Object> register(User user){
        Map<String, Object> map = new HashMap<>();
        //对空值进行判断处理
        if (user == null){
            throw new IllegalArgumentException("参数不能为空!");
        }
        if (StringUtils.isBlank(user.getUsername())){
            map.put("usernameMsg", "账号不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("passwordMsg", "密码不能为空！");
            return map;
        }
        if (StringUtils.isBlank(user.getPassword())){
            map.put("emailMsg", "邮箱不能为空!");
            return map;
        }

        //验证账号
        User userSearch = userMapper.selectByName(user.getUsername());
        if (userSearch != null){
            map.put("usernameMsg", "该账号已存在!");
            return map;
        }

        //验证邮箱
        userSearch = userMapper.selectByEmail(user.getEmail());
        if (userSearch != null){
            map.put("emailMsg", "改邮箱已被注册!");
            return map;
        }

        //注册用户就是把用户的信息保存在库里
        user.setSalt(CommunityUtil.generateUUID().substring(0, 5));
        user.setPassword(CommunityUtil.md5(user.getPassword() + user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(CommunityUtil.generateUUID());       //激活码可以长一点
        //给一个随机头像
        user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setCreateTime(new Date());
        //在执行Insert的时候自动生成ID
        userMapper.insertUser(user);        //一开始用户传进来是没有ID的，这句话执行完成之后myBatis会生成一个ID。因为我们在application.properties中配置了mybatis.configuration.useGeneratedKeys=true

        //注册成功要给用户发一个激活参数
        Context context = new Context();
        context.setVariable("email", user.getEmail());
        //http://localhost:8080/community/activation/用户Id/激活码
        //把路径动态地拼出来
        String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
        context.setVariable("url", url);

        //利用模板引擎生成邮件的内容
        String content = templateEngine.process("/mail/activation", context);
        mailClient.sendMail(user.getEmail(), "激活账号", content);
        return map;
    }

    public int activation(int userId, String code){
        User user = userMapper.selectById(userId);
        if (user.getStatus() == 1){
            return ACTIVATION_REPEAT;
        }else if (user.getActivationCode().equals(code)){
            userMapper.updateStatus(userId, 1);
            return ACTIVATION_SUCCESS;
        }else {
            return ACTIVATION_FAILURE;
        }
    }

    public Map<String, Object> login(String username, String password, int expiredSeconds){
        Map<String, Object> map = new HashMap<>();

        //空值的处理
        if (StringUtils.isBlank(username)){
            map.put("usernameMsg", "账号不能为空!");
            return map;
        }
        if (StringUtils.isBlank(password)){
            map.put("passwordMsg", "密码不能为空!");
            return map;
        }

        //验证账号
        User user = userMapper.selectByName(username);
        if (user == null){
            map.put("usernameMsg", "该账号不存在!");
            return map;
        }

        if (user.getStatus() == 0){ //注册了，但未激活
            map.put("usernameMsg", "账号未激活!");
            return map;
        }

        //验证密码
        password = CommunityUtil.md5(password + user.getSalt());
        if (!user.getPassword().equals(password)){
            map.put("passwordMsg", "密码不正确!");
            return map;
        }

        //生成登录凭证
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(user.getId());
        loginTicket.setTicket(CommunityUtil.generateUUID());
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000));   //过期时间
        loginTicketMapper.insertLoginTicket(loginTicket);
        map.put("ticket", loginTicket.getTicket());
        return map;
    }

    public void logout(String ticket){
        loginTicketMapper.updateStatus(ticket, 1);    //1表示无效
    }

    public LoginTicket findLoginTicket(String ticket){
        return loginTicketMapper.selectByTicket(ticket);
    }

    public int updateHeader(int userId, String headerUrl){
        return userMapper.updateHeader(userId, headerUrl);
    }

//    public Map<String, Object> SendVerificationCode(User user){
//        Map<String, Object> map = new HashMap<>();
//        //验证邮箱
//        User userFind = userMapper.selectByEmail(user.getEmail());
//        if (userFind == null){
//            map.put("emailMsg", "无效邮箱!");
//            return map;
//        }
//
//        //注册成功要给用户发一个激活参数
//        Context context = new Context();
//        context.setVariable("email", user.getEmail());
//        //http://localhost:8080/community/forget/用户Id/激活码
//        //把路径动态地拼出来
//        String url = domain + contextPath + "/forget/" + user.getId() + "/" + user.getActivationCode();
//        context.setVariable("url", url);
//
//        //利用模板引擎生成邮件的内容
//        String content = templateEngine.process("/mail/forget", context);
//        mailClient.sendMail(user.getEmail(), "验证码", content);
//    }

}
