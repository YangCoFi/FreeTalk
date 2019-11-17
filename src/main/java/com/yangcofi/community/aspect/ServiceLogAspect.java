package com.yangcofi.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName ServiceLogAspect
 * @Description TODO
 * @Author YangC
 * @Date 2019/8/29 17:19
 **/

@Component
@Aspect
public class ServiceLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);

    @Pointcut("execution(* com.yangcofi.community.service.*.*(..))")        //*表示方法的返回值 这里是什么返回值都行 所有的service组件里面所有的方法  ..表示所有的参数  这些都要处理
    //定义切点 要把代码具体植入到业务Bean的那些地方
    public void pointCut(){

    }

    @Before("pointCut()")
    public void before(JoinPoint joinPoint){        //连接点指指的是程序植入的目标 就是你要调用的那个方法
        //用户[1.2.3.4]在什么时间[xxx]访问了什么功能[com.yangcofi.community.service.xxx()]
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null){
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        String ip = request.getRemoteHost();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String target = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();        //得到类名 . 方法名
        logger.info(String.format("用户[%s],在[%s]，访问了[%s].", ip, now, target));
    }
}

//除了环绕通知以外的也可以加上连接点的参数


//在Spring API中提供了一个非常便捷的工具类RequestContextHolder，能够在Controller中获取request对象和response对象，使用方法如下
//HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
//HttpServletResponse resp = ((ServletWebRequest)RequestContextHolder.getRequestAttributes()).getResponse();