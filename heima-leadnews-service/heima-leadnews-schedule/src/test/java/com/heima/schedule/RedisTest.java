package com.heima.schedule;

import com.heima.common.redis.RedisCacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskinfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
public class RedisTest {

    @Resource
    RedisCacheService redisCacheService;

    @Autowired
    private TaskinfoService taskinfoService;

    @Test
    void listTest(){
        redisCacheService.lLeftPush("t1","aa");
        redisCacheService.lLeftPush("t1","ee");
        redisCacheService.lLeftPush("t1","cc");
        redisCacheService.lLeftPush("t1","dd");

        String str = redisCacheService.lRightPop("t1");
        System.out.println("str = " + str);
    }



    /**
     * 添加任务
     */
    @Test
    public void addDataTest(){
        Task task=new Task();
        task.setTaskType(100);
        task.setExecuteTime(new Date().getTime());
        task.setPriority(50);
        task.setParameters("task test".getBytes());
        Long taskId = taskinfoService.addTask(task);
        System.out.println(taskId);
    }

    @Test
    public void scanTest(){
        for (int i = 0; i <5 ; i++) {
            Task task=new Task();
            task.setTaskType(1110+i);
            task.setExecuteTime(new Date().getTime()+500*i);
            task.setPriority(530);
            task.setParameters("task test".getBytes());
            Long taskId = taskinfoService.addTask(task);
            System.out.println(taskId);
        }
    }

}
