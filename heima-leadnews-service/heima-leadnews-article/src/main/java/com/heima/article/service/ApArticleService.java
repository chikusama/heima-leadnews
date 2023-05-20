package com.heima.article.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.dtos.ApArticleDto;
import com.heima.model.common.dtos.ResponseResult;

import java.util.List;

/**
 * 文章表(ApArticle)表服务接口
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:51
 */
public interface ApArticleService extends IService<ApArticle> {

    List<ApArticle> getArticleListByType(Short loadtype, ApArticleDto dto);

    ResponseResult saveArticleViaNews(ArticleDto dto);
}

