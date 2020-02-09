package com.yangcofi.community.service;

import com.yangcofi.community.dao.AlphaDao;
import com.yangcofi.community.dao.DiscussPostMapper;
import com.yangcofi.community.dao.UserMapper;
import com.yangcofi.community.entity.DiscussPost;
import com.yangcofi.community.entity.User;
import com.yangcofi.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Date;


@Service
//@Scope("prototype")
public class AlphaService {

    private static final Logger logger = LoggerFactory.getLogger(AlphaService.class);

    @Autowired
    private AlphaDao alphaDao;

    //为了演示事务 在这里杜撰一个加入新用户 给用户发一个帖子 这样一个事务

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    //这个bean是Spring自动创建 并自动装配到容器里面的 直接注入就可以用 利用这个bean我们就可以利用它执行sql 这个执行sql就可以保证事务性
    private TransactionTemplate transactionTemplate;


    public AlphaService() {
//        System.out.println("实例化AlphaService");
    }

    @PostConstruct
    public void init() {
//        System.out.println("初始化AlphaService");
    }

    @PreDestroy
    public void destroy() {
//        System.out.println("销毁AlphaService");
    }

    public String find() {
        return alphaDao.select();
    }

    //传播机制：业务方法A可能会调用业务方法B      B以什么为准呢     出现了交叉的问题
    //REQUIRED：支持当前事务(外部事务) A掉B 对B来说 A就是当前事务 外部事务不存在 就创建新事务 按我的来
    //REQUIRES_NEW：创建一个新的事务 并且暂停外部事物
    //NESTED：如果当前存在事务(外部事务) 则嵌套在外部事物中执行 即有独立的提交和回滚 外部事物不存在就和REQUIRED一样。
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)            //读取已提交的数据 传播机制
    //声明式事务
    public Object save1(){
        //先增加用户
        User user = new User();
        user.setUsername("alpha");
        user.setSalt(CommunityUtil.generateUUID().substring(0,5));
        user.setPassword(CommunityUtil.md5("123" + user.getSalt()));        //123使用户输入的密码
        user.setEmail("alpha@qq.com");
        user.setHeaderUrl("http://image.nowcoder.com/head/99t.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
        //插入这个用户以后 mybatis回向数据库要这个ID
        //在user-mapper.xml种我们在insert种写了keyProperty="id"，那么生成id之后，就会自动赋给"id"，这个属性

        // 再增加帖子 以用户的身份发一个帖子
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle("hello");
        post.setContent("新人报到");
        post.setCreateTime(new Date());
        discussPostMapper.insertDiscussPost(post);

        Integer.valueOf("abc"); //故意踢整个错误 看事务会不会回滚 将abc字符串转成整数，肯定是不行的。 没加管理事务之前 是可以插进去的 因为DML语句执行完马上就生效 不管你这里报没报错
        return "OK";
    }

    //编程式事务
    public Object save2(){
        //设置隔离级别
        transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
        //设置传播机制
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return transactionTemplate.execute(new TransactionCallback<Object>() {
            @Override
            public Object doInTransaction(TransactionStatus transactionStatus) {
                //这个方法是回调方法由 TransactionTemplate底层自动调

                //先增加用户
                User user = new User();
                user.setUsername("beta");
                user.setSalt(CommunityUtil.generateUUID().substring(0,5));
                user.setPassword(CommunityUtil.md5("123" + user.getSalt()));        //123使用户输入的密码
                user.setEmail("beta@qq.com");
                user.setHeaderUrl("http://image.nowcoder.com/head/999t.png");
                user.setCreateTime(new Date());
                userMapper.insertUser(user);
                //插入这个用户以后 mybatis回向数据库要这个ID
                //在user-mapper.xml种我们在insert种写了keyProperty="id"，那么生成id之后，就会自动赋给"id"，这个属性

                // 再增加帖子 以用户的身份发一个帖子
                DiscussPost post = new DiscussPost();
                post.setUserId(user.getId());
                post.setTitle("你好");
                post.setContent("我是新人");
                post.setCreateTime(new Date());
                discussPostMapper.insertDiscussPost(post);

                Integer.valueOf("abc"); //故意踢整个错误 看事务会不会回滚 将abc字符串转成整数，肯定是不行的。 没加管理事务之前 是可以插进去的 因为DML语句执行完马上就生效 不管你这里报没报错

                return "ok";
            }
        });        //这个方法需要我们传一个接口(回调接口) 可以用匿名的方式实现(直接new0
    }

    //可以让该方法在多线程的情况下异步地调用 如果启动一个线程调用这个方法，是和主线程异步的
    @Async
    public void execute1(){
        logger.debug("execute1");
    }

    //让这个方法定时地去执行
    @Scheduled(initialDelay = 10000, fixedRate = 1000)
    public void execute2(){
        logger.debug("execute2");
    }
}
