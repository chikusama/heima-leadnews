package com.heima.schedule.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.schedule.entity.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.service.TaskinfoLogsService;
import org.springframework.stereotype.Service;

/**
 * (TaskinfoLogs)表服务实现类
 *
 * @author killerqueen
 * @since 2022-11-03 16:00:37
 */
@Service("taskinfoLogsService")
public class TaskinfoLogsServiceImpl extends ServiceImpl<TaskinfoLogsMapper, TaskinfoLogs> implements TaskinfoLogsService {

}

