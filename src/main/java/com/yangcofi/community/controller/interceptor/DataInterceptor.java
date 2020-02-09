package com.yangcofi.community.controller.interceptor;

import com.yangcofi.community.entity.User;
import com.yangcofi.community.service.DataService;
import com.yangcofi.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName DataInterceptor
 * @Description TODO
 * @Author YangC
 * @Date 2019/11/27 22:11
 **/
@Component
public class DataInterceptor implements HandlerInterceptor {
    @Autowired
    private DataService dataService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        //统计UV
        String ip = request.getRemoteHost();
        dataService.recordUV(ip);

        //统计DAU
        User user = hostHolder.getUser();
        if (user != null){
            dataService.recordDAU(user.getId());
        }
        return true;
    }
}
