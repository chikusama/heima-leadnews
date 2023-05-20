package com.heima.schedule;

import com.heima.common.redis.RedisCacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskinfoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Set;

@SpringBootTest
public class TaskTest {

    @Resource
    RedisCacheService redisCacheService;

    @Autowired
    private TaskinfoService taskinfoService;

    @Test
    public void testKeys(){
        Set<String> keys = redisCacheService.keys("future_*");
        System.out.println(keys);

        Set<String> scan = redisCacheService.scan("future_*");
        System.out.println(scan);
    }

    @Test
    public void test(){
        for (int i = 0; i <100 ; i++) {
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
