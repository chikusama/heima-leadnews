package com.heima.schedule.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;

import java.util.List;

import com.heima.model.schedule.entity.TaskinfoLogs;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.schedule.service.TaskinfoLogsService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (TaskinfoLogs)表控制层
 *
 * @author killerqueen
 * @since 2022-11-03 16:00:36
 */
@Api(tags = "接口")
@RestController
@RequestMapping("/api/v1/taskinfoLogs")
public class TaskinfoLogsController {
    /**
     * Service层对象
     */
    @Autowired
    private TaskinfoLogsService taskinfoLogsService;
}

