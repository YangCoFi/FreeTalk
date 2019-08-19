package com.yangcofi.community.util;

/**
 * @ClassName CommunityConstant
 * @Description 状态常量接口
 * @Author YangC
 * @Date 2019/8/13 22:01
 **/
public interface  CommunityConstant {
    /**
     * 激活成功
     * */
    int ACTIVATION_SUCCESS = 0;

    /**
     * 重复激活
     * */
    int ACTIVATION_REPEAT = 1;

    /**
     * 重复失败
     * */
    int ACTIVATION_FAILURE = 2;

    /**
     * 默认状态登录凭证的超时时间
     * */
    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;        //不勾记住我的时候 存12小时

    /**
     * 记住状态下的登录凭证超时时间
     * */
    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;
}
