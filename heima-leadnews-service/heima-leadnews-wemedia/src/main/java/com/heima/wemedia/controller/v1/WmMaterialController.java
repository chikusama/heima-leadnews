package com.heima.wemedia.controller.v1;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.model.wemedia.entity.WmNews;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


/**
 * 自媒体图文素材信息表(WmMaterial)表控制层
 *
 * @author killerqueen
 * @since 2022-10-28 17:09:10
 */
@Api(tags = "自媒体图文素材信息表接口")
@RestController
@RequestMapping("/api/v1/material")
@Slf4j
public class WmMaterialController {
    

    /**
     * Service层对象
     */
    @Autowired
    private WmMaterialService wmMaterialService;

    /**
     * http://localhost:8802/wemedia/MEDIA/wemedia/api/v1/material/upload_picture
     * @return
     */
    @PostMapping("/upload_picture")
    public ResponseResult uploadPic(MultipartFile multipartFile) throws IOException {
        //调用service
        return ResponseResult.okResult(wmMaterialService.uploadPic(multipartFile));
    }

    /**
     * 获得PageInfo类型的数据
     * http://localhost:8802/wemedia/MEDIA/wemedia/api/v1/material/list
     * {page: 1, size: 20, isCollection: 0}
     * @param wmMaterialDto
     * @return
     */
    @PostMapping("/list")
    public PageResponseResult getMaterialList(@RequestBody WmMaterialDto wmMaterialDto){
        return wmMaterialService.getMaterialList(wmMaterialDto);
    }


    /**
     * 收藏
     */
    @GetMapping("/collect/{id}")
    public ResponseResult collectMaterial(@PathVariable Integer id){
        return wmMaterialService.collectMaterial(id);
    }


}

