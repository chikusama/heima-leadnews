package com.heima.wemedia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.entity.WmNews;

/**
 * 自媒体图文内容信息表(WmNews)表服务接口
 *
 * @author killerqueen
 * @since 2022-10-29 14:01:32
 */
public interface WmNewsService extends IService<WmNews> {

    PageResponseResult selectAllArticles(WmNewsPageReqDto wmNewsPageReqDto);

    /**
     * 文章管理
     * @param wmNewsDto
     */
    void saveContent(WmNewsDto wmNewsDto);

    /**
     * 文章上下架
     * @param wmNews
     * @return
     */
    ResponseResult upDownNews(WmNews wmNews);

    ResponseResult submitNews(WmNewsDto dto);

}

