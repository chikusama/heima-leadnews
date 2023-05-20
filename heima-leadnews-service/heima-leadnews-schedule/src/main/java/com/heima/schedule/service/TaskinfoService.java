package com.heima.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.entity.Taskinfo;

/**
 * (Taskinfo)表服务接口
 *
 * @author killerqueen
 * @since 2022-11-03 16:00:39
 */
public interface TaskinfoService extends IService<Taskinfo> {

    /**
     * 添加任务
     * @param task   任务对象
     * @return       任务id
     */
    public long addTask(Task task) ;


    /**
     * 取消任务
     */
    public boolean cancelTask(long taskId);


    /**
     * 拉取任务
     */
    Task pollTask(Integer taskType, Integer priority);
}

