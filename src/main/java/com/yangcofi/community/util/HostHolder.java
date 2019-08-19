package com.yangcofi.community.util;

import com.yangcofi.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @ClassName HostHolder
 * @Description 用于代替session对象(可以持有User，并线程隔离) 这里我们不想用session 为了解决多线程持有User。
 * @Author YangC
 * @Date 2019/8/16 20:32
 **/

/**
 * 用于代替session对象(可以持有User，并线程隔离) 这里我们不想用session 为了解决多线程持有User。
 * */

@Component
public class HostHolder {

    //获取当前线程的map
    private ThreadLocal<User> users = new ThreadLocal<>();

    public void setUser(User user){
        users.set(user);
    }

    public User getUser(){
        return users.get();
    }

    public void clear(){
        users.remove();
    }


}
