package com.yangcofi.community.controller.interceptor;

import com.yangcofi.community.annotation.LoginRequired;
import com.yangcofi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @ClassName LoginRequiredInterceptor
 * @Description 用拦截器尝试拦截带有注解的方法，在拦截到这个方法以后就判断登陆与否
 * 在请求的最初去判断登录与否
 * @Author YangC
 * @Date 2019/8/17 14:42
 **/

@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //拦截时判断拦截的是不是方法，因为还可能时资源其他内容，得判断一下
        if (handler instanceof HandlerMethod){
            //如果拦截到的是方法的话，则handler就是这个类型HandlerMethod
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
            if (loginRequired != null && hostHolder.getUser() == null){
                //方法是需要登陆的，但你没登陆
                //用responce去重定向
                response.sendRedirect(request.getContextPath() + "/login");        //从请求中直接取到应用的路径
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
