package com.yangcofi.community.controller;

import com.google.code.kaptcha.Producer;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.service.CommonService;
import com.yangcofi.community.service.UserService;
import com.yangcofi.community.util.CommunityConstant;
import com.yangcofi.community.util.CommunityUtil;
import com.yangcofi.community.util.RedisKeyUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName LoginController
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/13 14:04
 **/

@Controller
public class LoginController implements CommunityConstant {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    UserService userService;

    @Autowired
    CommonService commonService;

    @Autowired
    private Producer kaptchaProducer;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, User user){
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()){
            model.addAttribute("msg", "注册成功，已向您的邮箱发送激活邮件，请尽快激活!");
            model.addAttribute("target", "/index");
            return "/site/operate-result";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "/site/register";        //当跳回至这个错误页面的时候，user也是在model里面，可以在页面上直接访问到
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }

    //http://localhost:8080/community/activation/用户Id/激活码
    @RequestMapping(path = "/activation/{userId}/{code}", method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code){
        int result = userService.activation(userId, code);
        if (result == ACTIVATION_SUCCESS){
            model.addAttribute("msg", "激活成功，您的账号可以正常使用了!");
            model.addAttribute("target", "/login");
        }else if (result == ACTIVATION_REPEAT){
            model.addAttribute("msg", "无效操作，您的账号已经激活过了!");
            model.addAttribute("target", "/index");
        }else {
            model.addAttribute("msg", "激活失败，激活码不正确!");
            model.addAttribute("target", "/index");
        }
        return "/site/operate-result";
    }

    @RequestMapping(path = "/kaptcha", method = RequestMethod.GET)
    public void getKaptcha(HttpServletResponse response/*, HttpSession session*/){          //将验证码存在了session里面  在用Redis存储时不用了
        //生成验证码
        String text = kaptchaProducer.createText();
        BufferedImage image = kaptchaProducer.createImage(text);

        //将验证码存入session，以便后面使用
//        session.setAttribute("kaptcha", text);

        //将验证码存在Redis里面 需要构造key，而key又需要验证码的归属者
        //验证码的归属
        String kaptchaOwner = CommunityUtil.generateUUID();
        //这个东西要发给客户端 客户端需要用Cookie保存
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        //设置Cookie的生存时间 60s
        cookie.setMaxAge(60);
        //有效路径  设置为整个项目下都有效
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        //验证码的归属有了以后，接下来要去存这个验证码
        String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(redisKey, text, 60, TimeUnit.SECONDS);                //因为存的就是一个字符串就用opsForValue即可
        //对登录进行具体的验证时需要用到，所以path = "/login"也要处理


        //将图片直接输出给浏览器 人工输出
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);   //可以不用关闭，因为Response最后是由SpringMVC自动去关闭的
        } catch (IOException e) {
            logger.error("响应验证码失败"+e.getMessage());
        }
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)       //路径可以一样，方式不行
    public String login(String username, String password, String code, boolean rememberme,
                        Model model, /*HttpSession session,*/ HttpServletResponse response,
                        @CookieValue("kaptchaOwner") String kaptchaOwner){
        //验证码对不对， 验证码直接在表现层判断出来， 业务层不管
//        String kaptcha = (String) session.getAttribute("kaptcha");
        //从Redis中来取 需要key，而key又需要KaptchaOwner，而KaptchaOwner又需要从Cookie里取
        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)){
            //数据没有失效
            String redisKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(redisKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)){
            model.addAttribute("codeMsg", "验证码不正确!");
            return "/site/login";
        }
        //检查账号密码
        int expiredSeconds = rememberme ? REMEMBER_EXPIRED_SECONDS : DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredSeconds);
        if (map.containsKey("ticket")){
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredSeconds);
            response.addCookie(cookie);
            return "redirect:/index";
        }else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "/site/login";
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        SecurityContextHolder.clearContext();
        return "redirect:/login";       //默认get请求
    }

    @RequestMapping(path = "/forget", method = RequestMethod.GET)
    public String getForgetPage(){
        return "/site/forget";
    }

    @RequestMapping(path = "/forget/getVerificationCode", method = RequestMethod.POST)
    @ResponseBody
    public String getVerificationCode(String email, HttpSession session){
        if (StringUtils.isBlank(email)){
            return CommunityUtil.getJSONString(-1, "请求邮箱不能为空值！");
        }
        String verCode = CommunityUtil.generateUUID().substring(0, 6);
        session.setAttribute("verCode", verCode);
        session.setAttribute("email", email);
        commonService.sendVerifyEmail(email, verCode);
        return CommunityUtil.getJSONString(0, "验证邮件发送成功！");
    }

    @RequestMapping(path = "/forget", method = RequestMethod.POST)
    public String forget(String email, String code, String password, HttpSession session){
        System.out.println(session.getAttribute("email") + "--------------------------------");
        if (!StringUtils.isBlank(email) && email.equals(session.getAttribute("email"))){
            User targetUser = commonService.getUserByEmail(email);
            String changePwd = CommunityUtil.md5(password + targetUser.getSalt());
            if (code.equals(session.getAttribute("verCode"))){
                commonService.updatePwdById(targetUser.getId(), changePwd);
            }
            return "redirect:/login";
        }else {
            return "redirect:/forget";
        }
    }
}

/**
 * Redirect；
 * Forword:
 *
 * */
