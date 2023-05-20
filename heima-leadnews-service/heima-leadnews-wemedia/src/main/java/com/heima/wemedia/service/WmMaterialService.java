package com.heima.wemedia.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.entity.WmMaterial;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 自媒体图文素材信息表(WmMaterial)表服务接口
 *
 * @author killerqueen
 * @since 2022-10-28 17:09:12
 */
public interface WmMaterialService extends IService<WmMaterial> {

    WmMaterial uploadPic(MultipartFile multipartFile) throws IOException;

    PageResponseResult getMaterialList(WmMaterialDto wmMaterialDto);

    ResponseResult collectMaterial(Integer id);
}

