package com.yangcofi.community.controller.advice;

import com.yangcofi.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName ExceptionAdvice
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/29 11:23
 **/
@ControllerAdvice(annotations = Controller.class)                   //Spring容器会扫描所有的带有Controller注解的Bean  统一去处理
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(Exception.class);
    @ExceptionHandler({Exception.class})               //表示这个方法是处理括号里面的异常的
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {           //当外面Controller发生了异常，可以把异常最为参数传到这个方法里面来Exception e
        logger.error("服务器发生异常: " + e.getMessage());         //e.getMessage()是大致的错误信息
        for (StackTraceElement element : e.getStackTrace()){                //这是一个数组 里面是StackTraceElement
            logger.error(element.toString());
        }

        //浏览器访问服务器，可能是同步的请求，返回一个网页，这没有问题；但也有可能是异步请求，返回一个JSON，这个时候就没必要重定向到这里了
        String xRequestedWith = request.getHeader("x-requested-with");
        if ("XMLHttpRequest".equals(xRequestedWith)){
            //异步请求 他希望你返回xml 当然JSON也是可以的
            response.setContentType("application/plain;charset=utf-8");         //如果你写的是json，向浏览器返回一个字符串，浏览器会自动把它转换成json对象。 plain就是返回一个普通的字符串，但是是json格式 我们手动去转 $.parseJSON
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常!"));
        }else {                 //普通请求
            response.sendRedirect(request.getContextPath() + "/error");            //request.getContextPath()获取项目的访问路径
        }
    }
}
