package com.heima.wemedia.controller.v1;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;

import java.util.List;

import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.entity.WmChannel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.wemedia.service.WmChannelService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 频道信息表(WmChannel)表控制层
 *
 * @author killerqueen
 * @since 2022-10-29 11:57:12
 */
@Api(tags = "频道信息表接口")
@RestController
@RequestMapping("/api/v1/channel")
public class WmChannelController {
    /**
     * Service层对象
     */
    @Autowired
    private WmChannelService wmChannelService;

    //频道查询
    @GetMapping("/channels")
    public ResponseResult getChannelList(){
       return ResponseResult.okResult(wmChannelService.selectAllChannels());
    }



}

