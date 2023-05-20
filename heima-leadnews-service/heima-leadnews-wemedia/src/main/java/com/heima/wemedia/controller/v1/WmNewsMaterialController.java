package com.heima.wemedia.controller.v1;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;

import java.util.List;

import com.heima.model.wemedia.entity.WmNewsMaterial;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.wemedia.service.WmNewsMaterialService;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * 自媒体图文引用素材信息表(WmNewsMaterial)表控制层
 *
 * @author killerqueen
 * @since 2022-10-29 17:22:50
 */
@Api(tags = "自媒体图文引用素材信息表接口")
@RestController
@RequestMapping("/api/v1/wmNewsMaterial")
public class WmNewsMaterialController {
    /**
     * Service层对象
     */
    @Autowired
    private WmNewsMaterialService wmNewsMaterialService;

}

