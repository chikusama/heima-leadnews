package com.heima.wemedia.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageRequestDto;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.wemedia.mapper.WmMaterialMapper;
import com.heima.wemedia.service.WmMaterialService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.sql.Wrapper;
import java.util.Date;
import java.util.UUID;

/**
 * 自媒体图文素材信息表(WmMaterial)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-28 17:09:12
 */
@Service("wmMaterialService")
public class WmMaterialServiceImpl extends ServiceImpl<WmMaterialMapper, WmMaterial> implements WmMaterialService {

    @Autowired
    private FileStorageService fileStorageService;


    @Override
    public WmMaterial uploadPic(MultipartFile multipartFile) throws IOException {
        //从参数中获取文件名
        String originalFilename = multipartFile.getOriginalFilename();
        //获取文件名后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //生成新的文件名用于存储到minio当中
        String fileName = UUID.randomUUID().toString();
        fileName.replace("-", "");
        InputStream inputStream = multipartFile.getInputStream();
        String htmlFile = fileStorageService.uploadHtmlFile("", fileName + suffix, inputStream);
        WmMaterial wmMaterial = new WmMaterial();
        wmMaterial.setIsCollection(0);
        wmMaterial.setType((short) 0);
        wmMaterial.setUrl(htmlFile);
        wmMaterial.setCreatedTime(new Date());
        wmMaterial.setUserId(WmThreadLocalUtil.getUser().getId());
        this.save(wmMaterial);

        return wmMaterial;
    }

    @Override
    public PageResponseResult getMaterialList(WmMaterialDto wmMaterialDto) {
        //创建Page对象
        Page<WmMaterial> page = new Page<>(wmMaterialDto.getPage(), wmMaterialDto.getSize());
        //根据参数sql查询
        LambdaQueryWrapper<WmMaterial> lqw = new LambdaQueryWrapper<>();
        lqw.eq(WmMaterial::getIsCollection, wmMaterialDto.getIsCollection());
        //从线程池中获取userId
        lqw.eq(WmMaterial::getUserId, WmThreadLocalUtil.getUser().getId());
        lqw.orderByDesc(WmMaterial::getCreatedTime);

        this.page(page, lqw);
        //创建一个RR对象
        ResponseResult responseResult = ResponseResult.okResult(page.getRecords());
        PageResponseResult pageResponseResult = new PageResponseResult(wmMaterialDto.getPage(), wmMaterialDto.getSize(), (int) page.getTotal());
        BeanUtils.copyProperties(responseResult, pageResponseResult);
        return pageResponseResult;
    }

    @Override
    public ResponseResult collectMaterial(Integer id) {

        //根据id进行查询
        WmMaterial material = this.getOne(Wrappers.<WmMaterial>lambdaQuery().eq(WmMaterial::getId, id));
        //进行取反操作将数据封装到RR中传回去
        if (material.getIsCollection() == 0) {
            material.setIsCollection(1);
        }else{
            material.setIsCollection(0);
        }
        this.updateById(material);

        return ResponseResult.okResult(material);
    }
}

