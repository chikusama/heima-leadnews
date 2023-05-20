package com.heima.task;

import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;

public class DelayTaskApp {
    public static void main(String[] args) throws InterruptedException {
        //1.创建延时任务队列
        DelayQueue<DelayTask> queue = new DelayQueue<DelayTask>();

        //2.添加延时任务元素：向延时任务队列汇总
        queue.add(new DelayTask(2, System.currentTimeMillis() + 10000L));
        queue.add(new DelayTask(3, System.currentTimeMillis() + 15000L));
        queue.add(new DelayTask(1, System.currentTimeMillis() + 5000L));

        //3.取出过期的任务：如果到期了，则取出来
        int size = queue.size();
        System.out.println("当前时间是：" + System.currentTimeMillis());
        for (int i = 0; i < size; i++) {
            System.out.println(queue.take() + " ------ " + LocalDateTime.now());
        }
    }
}
