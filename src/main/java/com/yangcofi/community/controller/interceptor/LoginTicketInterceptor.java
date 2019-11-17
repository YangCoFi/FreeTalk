package com.yangcofi.community.controller.interceptor;

import com.yangcofi.community.entity.LoginTicket;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.service.UserService;
import com.yangcofi.community.util.CookieUtil;
import com.yangcofi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Security;
import java.util.Date;

/**
 * @ClassName LoginTicketInterceptor
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/16 20:17
 **/

//https://www.cnblogs.com/xzwblog/p/7227509.html
//ThreadLocal

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ticket = CookieUtil.getValue(request, "ticket");           //接口中的方法不能改 不能用@CookieValue去获得Cookie
        if (ticket != null){
            LoginTicket loginTicket = userService.findLoginTicket(ticket);
            //判断凭证当前有没有效 是否超时 无效就当作你为登录
            if (loginTicket != null && loginTicket.getStatus() == 0 && loginTicket.getExpired().after(new Date())){
                //0 表示有效 并且超时时间晚于当前时间
                //根据凭证查询用户
                User user = userService.findUserById(loginTicket.getUserId());
                //在本次请求中持有用户
                //在多个线程里隔离存这个对象 只要这个请求没有处理完 这个线程就一直还在 当服务器做出相应之后，线程会销毁
                hostHolder.setUser(user);
                //构建用户认证的结果，并存入SecurityContext，以便于Security进行授权
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        user, user.getPassword(), userService.getAuthorities(user.getId()));
                SecurityContextHolder.setContext(new SecurityContextImpl(authentication));
            }
        }
        return true;            //return false的话后面就不会执行了
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        //在调用模板引擎之前 需要将User对象存入到model
        User user = hostHolder.getUser();
        System.out.println(user);
        if (user != null && modelAndView != null){
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
        SecurityContextHolder.clearContext();
    }
}
