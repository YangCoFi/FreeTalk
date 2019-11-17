package com.yangcofi.community;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @ClassName BlockingQueueTests
 * @Description TODO
 * @Author YangC
 * @Date 2019/9/8 9:13
 **/

public class BlockingQueueTests {
    public static void main(String[] args) {
        //因为需要生产者，消费者线程 所以额外定义两个类
        //实例化阻塞队列
        BlockingQueue queue = new ArrayBlockingQueue(10);       //队列中最多只能存10个数
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();        //多弄几个消费者 就类似一个工厂 消费者有很多个
        new Thread(new Consumer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}

class Producer implements Runnable{

    //因为线程是要交给阻塞队列去管理   希望在实例化这个Producer类的时候，就把这个BlockingQueue传进来给我
    private BlockingQueue<Integer> queue;
    @Override
    public void run() {
        try{
            //频繁不断地往队列存入数据
            for (int i = 0; i < 100; i ++){         //生产者要生产100个数据 每个数据要将其放到队列里 放的时候要有一个间隔
                Thread.sleep(20);
                queue.put(i);        //put阻塞方法
                System.out.println(Thread.currentThread().getName() + "生产了，此时队列剩余:" + queue.size());    //这个线程生产了一个数以后，队列里有多少数
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }     //因为是线程，所以要实现Runnable接口

    public Producer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }
}


class Consumer implements Runnable{
    private BlockingQueue<Integer> queue;

    public Consumer(BlockingQueue<Integer> queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(new Random().nextInt(1000));     //在0到1000之间睡一个数
                queue.take();
                System.out.println(Thread.currentThread().getName() + "消费了，此时队列剩余:" + queue.size());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}