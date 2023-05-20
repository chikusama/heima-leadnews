package com.heima.feign.article;


import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * news调用article的feign
 *
 */
@FeignClient(value = "leadnews-article",fallback = IArticleFeignFallback.class)
public interface ArticleNewsFeign {


    @PostMapping("/api/v1/article/save")
    ResponseResult saveArticleViaNewsByFeign(@RequestBody ArticleDto dto);
}
