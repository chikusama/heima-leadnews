package com.heima.schedule.controller;


import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.schedule.dtos.Task;
import com.heima.schedule.service.TaskinfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(tags = "接口")
@RestController
@RequestMapping("/api/v1/task")
@Slf4j
public class TaskinfoController {
    /**
     * Service层对象
     */
    @Autowired
    private TaskinfoService taskinfoService;

    /**
     * 添加任务
     *
     * @param
     */
    @PostMapping("/add")
    public void addTask(@RequestBody Task task) {
        taskinfoService.addTask(task);
    }

    /**
     * 取消任务
     *
     * @param taskId 任务id
     * @return 取消结果
     */
    @GetMapping("/cancel/{taskId}")
    public ResponseResult cancelTask(@PathVariable("taskId") long taskId) {
        return ResponseResult.okResult(taskinfoService.cancelTask(taskId));
    }

    /**
     * 按照类型和优先级来拉取任务
     *
     * @param type
     * @param priority
     */
    @GetMapping("/poll/{type}/{priority}")
    public Task poll(@PathVariable("type") int type, @PathVariable("priority") int priority) {
        return taskinfoService.pollTask(type, priority);
    }

}

