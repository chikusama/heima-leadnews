package com.itheima.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyTask {
    //@Scheduled(cron = "0/3 * * * * ?")
    @Scheduled(fixedRate = 3000)
    public void study() {
        System.out.println("学习");
    }
}
