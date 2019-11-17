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

    //因为一个线程内可以存在多个 ThreadLocal 对象，所以其实是 ThreadLocal 内部维护了一个 Map ，这个 Map 不是直接使用的 HashMap ，而是 ThreadLocal 实现的一个叫做 ThreadLocalMap 的静态内部类。而我们使用的 get()、set() 方法其实都是调用了这个ThreadLocalMap类对应的 get()、set() 方法。例如下面的 set 方法：

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


//ThreadLocal顾名思义可以理解为线程本地变量，ThreadLocal将变量的各个副本值保存在各个线程Thread，Thread对象实例采用ThreadLocalMap数据结构来存储副本值。每个线程往这个ThreadLocal中读写是线程隔离，一种将可变数据通过每个线程有自己的独立副本从而实现线程封闭的机制。