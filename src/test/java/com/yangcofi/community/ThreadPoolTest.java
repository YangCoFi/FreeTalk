package com.yangcofi.community;

import com.yangcofi.community.service.AlphaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.concurrent.*;

/**
 * @ClassName ThreadPoolTest
 * @Description TODO
 * @Author YangC
 * @Date 2019/12/8 17:51
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ThreadPoolTest {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolTest.class);

    //  JDK普通线程池
    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    // JDK可执行定时任务的线程池
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);

    @Autowired
    private AlphaService alphaService;

    //Spring普通线程池
    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    //Spring定时任务线程池
    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    private void sleep(long m){
        try {
            Thread.sleep(m);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //1.JDK线程池 Junit测试和main方法不一样 在main方法里启动一个线程，如果线程没有执行完，main会等待；而junit和启动的线程是并发的 junit跑完了之后，会直接结束 解决办法 sleep阻塞等待
    @Test
    public void testExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello Executor Service");
            }
        };
        for (int i = 0; i < 10; i ++){
            executorService.submit(task);   //submit就是调用一个线程执行这个线程体；
        }
        //注意要sleep一下 因为junit和里面开的线程是并发执行的
        sleep(10000);
    }

    //2.JDK定时任务线程池
    @Test
    public void testScheduledExecutorService(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ScheduledExecutor Service");
            }
        };
        scheduledExecutorService.scheduleAtFixedRate(task, 10000, 1000, TimeUnit.MILLISECONDS);
        //注意要sleep一下 因为junit和里面开的线程是并发执行的
        sleep(30000);
    }

    //3.Spring普通线程池
    @Test
    public void testThreadPoolTaskExecutor(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskExecutor");
            }
        };
        for (int i = 0; i < 10; i ++){
            taskExecutor.submit(task);
        }
        sleep(10000);
    }

    //4.Spring定时任务的线程池
    @Test
    public void testThreadPoolTaskScheduler(){
        Runnable task = new Runnable() {
            @Override
            public void run() {
                logger.debug("Hello ThreadPoolTaskScheduler");
            }
        };
        Date startTime = new Date(System.currentTimeMillis() + 10000); //当前时间延迟10000ms
        taskScheduler.scheduleAtFixedRate(task, 1000);
        sleep(30000);
    }

    //5.Spring普通线程池(简化方式)
    @Test
    public void testThreadPoolTaskExecutorSimple(){
        for (int i = 0; i < 10; i ++){
            alphaService.execute1();
        }
        sleep(10000);
    }

    //6.定时任务线程池
    @Test
    public void testThreadPoolTaskSchedulerSimple(){
        sleep(30000);
    }
}
