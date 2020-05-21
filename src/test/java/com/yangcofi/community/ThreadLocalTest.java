package com.yangcofi.community;

import org.hibernate.validator.constraints.EAN;

import java.sql.Time;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName ThreadLocalTest
 * @Description TODO
 * @Author YangC
 * @Date 2020/2/22 22:59
 **/
public class ThreadLocalTest {
    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(1);
        new Thread(() -> {
            System.out.println("t1 starts ...");
            try {
                TimeUnit.SECONDS.sleep(5);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("t1 ends ...");
            latch.countDown();
        }, "t1").start();



        new Thread(() -> {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2 starts ...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("t2 ends ...");
        }, "t2").start();
    }
}
