package com.heima.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.content.ScheduleConstants;
import com.heima.common.redis.RedisCacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.entity.Taskinfo;
import com.heima.model.schedule.entity.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskinfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * (Taskinfo)表服务实现类
 *
 * @author robin
 * @since 2022-11-03 14:54:21
 */
@Service("taskinfoService")
@Slf4j
public class TaskinfoServiceImpl extends ServiceImpl<TaskinfoMapper, Taskinfo> implements TaskinfoService {
    @Autowired
    TaskinfoLogsMapper taskinfoLogsMapper;

    @Autowired
    TaskinfoMapper taskinfoMapper;

    @Autowired
    RedisCacheService cacheService;


    /**
     * 未来数据定时刷新
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh() {

        String taskSync = cacheService.tryLock("FUTURE_TASK_SYNC", 1000 * 30);

        if (StringUtils.isNotBlank(taskSync)) {
            System.out.println(System.currentTimeMillis() / 1000 + "执行了定时任务");

            // 获取所有未来数据集合的key值
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");// future_*
            for (String futureKey : futureKeys) { // future_250_250
                //获取该组key下当前需要消费的任务数据
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                if (!tasks.isEmpty()) {
                    //获取toipcKey
                    String topicKey = ScheduleConstants.TOPIC + futureKey.split(ScheduleConstants.FUTURE)[1];
                    //将这些任务数据添加到消费者队列中
                    cacheService.refreshWithPipeline(futureKey, topicKey, tasks);
                    System.out.println("成功的将" + futureKey + "下的当前需要执行的任务数据刷新到" + topicKey + "下");
                }
            }
        }



    }




    @Override
    public long addTask(Task task) {
        //1.需要先把任务保存到mysql数据库中：taskinfo+taskinfo_logs
        Boolean isSave = saveToDB(task);
        //2.只要保存到mysql之后才会保存到redis数据库中：list+zset
        if (isSave) {
            saveToCache(task);
        }
        return task.getTaskId();
    }

    @Override
    public boolean cancelTask(long taskId) {
        //定义一个定量表示取消任务是否成功
        boolean isCancel = false;
        //1.删除数据库中任务：
        Task task = updateToDB(taskId, ScheduleConstants.CANCELLED);

        //2.删除redis中任务：
        if (task != null) {
            removeTaskFromCache(task);
            isCancel = true;
        }
        return isCancel;
    }

    /**
     * 外部调用pollTask就可以从list中获取任务进行消费
     *
     * @param taskType
     * @param priority
     * @return
     */
    @Override
    public Task pollTask(Integer taskType, Integer priority) {
        Task task = new Task();
        try {
            //1.从list中获取数据并删除：rpop key  ===>获取尾部的元素返回再删除
            String taskJson = cacheService.lRightPop(ScheduleConstants.TOPIC + taskType + "_" + priority);
            if (StringUtils.isNotBlank(taskJson)) {
                task = JSON.parseObject(taskJson, Task.class);
            }
            //2.删除任务并且修改任务：delete from taskinfo,update taskinfo_logs set status=1
            updateToDB(task.getTaskId(), ScheduleConstants.EXECUTED);
        } catch (Exception e) {
            task = null;
        }
        return task;
    }

    /**
     * 删除redis中任务
     *
     * @param task 要被删除的任务
     */
    private void removeTaskFromCache(Task task) {
        long executeTime = task.getExecuteTime();
        long systemTime = System.currentTimeMillis();
        //从redis中list中删除任务 lrem 从list中删除  key  value=[aa,bb,aa]====>  key  value=[bb]
        String key = task.getTaskType() + "_" + task.getPriority();
        if (executeTime <= systemTime) {
            cacheService.lRemove(ScheduleConstants.TOPIC + key, 0, JSON.toJSONString(task));
        }
        //从redis中zset中删除任务
        if (executeTime > systemTime) {
            cacheService.zRemove(ScheduleConstants.FUTURE + key, JSON.toJSONString(task));
        }
    }

    /**
     * 修改taskinfo以及taskinfo_logs
     *
     * @param taskId 删除的任务
     * @param status 要修改的状态
     */
    private Task updateToDB(long taskId, Integer status) {
        Task task = new Task();
        try {
            //1、delete from taskinfo where taskId=?
            this.removeById(taskId);

            //2、update taskinfo_logs set status=2,version=version+1 where taskId=? and version=oldVersion
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);

            //3、返回Task：给操作redis删除数据使用
            BeanUtils.copyProperties(taskinfoLogs, task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        } catch (Exception e) {
            task = null;
        }
        return task;
    }

    /**
     * 将任务保存到redis缓存中
     *
     * @param task 要添加的任务
     */
    private void saveToCache(Task task) {
        //系统时间
        long systemTime = System.currentTimeMillis();
        //任务执行时间
        long executeTime = task.getExecuteTime();
        //预设计时间: JDK1.8 提供日历
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();

        //只要保存到mysql之后才会保存到redis数据库中：list+zset
        //将list和zset数据结构key的结构：topic_1_1001  future_1_1001
        String key = task.getTaskType() + "_" + task.getPriority();
        //1.添加list中：executeTime<=systemTime
        if (executeTime <= systemTime) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        }
        //2.添加zset中：systemTime < executeTime <= nextScheduleTime
        if (executeTime > systemTime && executeTime <= nextScheduleTime) {
            cacheService.zAdd(ScheduleConstants.FUTURE + key, JSON.toJSONString(task), executeTime);
        }
    }

    /**
     * @param task 添加任务
     * @return Boolean 代表的是方法执行是否完成
     * 开发经验：请设计一个方法是否成功执行，将执行结果返回boolean
     */
    private Boolean saveToDB(Task task) {
        Boolean isSave = false;
        //需要先把任务保存到mysql数据库中：taskinfo+taskinfo_logs
        try {
            //1.插入taskinfo表中
            Taskinfo taskinfo = new Taskinfo();
            BeanUtils.copyProperties(task, taskinfo);
            taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
            this.save(taskinfo);
            task.setTaskId(taskinfo.getTaskId());
            //2.插入taskinfo_logs
            TaskinfoLogs taskinfoLogs = new TaskinfoLogs();
            BeanUtils.copyProperties(taskinfo, taskinfoLogs);
            taskinfoLogs.setVersion(0);
            taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
            taskinfoLogsMapper.insert(taskinfoLogs);
            isSave = true;
        } catch (Exception e) {
            isSave = false;
        }
        //3.返回操作结果
        return isSave;
    }
}

