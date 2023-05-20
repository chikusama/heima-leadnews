package com.heima.article.controller.v1;


import com.heima.common.content.ArticleConstants;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.dtos.ApArticleDto;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heima.article.service.ApArticleService;

import java.util.List;

/**
 * 文章表(ApArticle)表控制层
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:50
 */
@Api(tags = "文章表接口")
@RestController
@RequestMapping("/api/v1/article")
@Slf4j
public class ApArticleController {
    /**
     * Service层对象
     */
    @Autowired
    private ApArticleService apArticleService;

    /**
     * {loaddir: 1, index: 1, tag: "__all__", size: 10, maxBehotTime: 0, minBehotTime: 20000000000000}
     * URL: POST http://localhost:8801/app/article/api/v1/article/load/
     */
    @PostMapping("/load")
    public ResponseResult loadArticle(@RequestBody ApArticleDto dto) {

        //调用sevice方法查询ApArticle列表数据
        List<ApArticle> articleList = apArticleService.getArticleListByType(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
        return ResponseResult.okResult(articleList);
    }


    /**
     * {loaddir: 0, index: 1, tag: "__all__", size: 10, maxBehotTime: 1618759217000,minBehotTime:1599485479000}
     *
     * @param dto
     * @return
     */
    @PostMapping("/loadnew")
    public ResponseResult loadNewArticle(@RequestBody ApArticleDto dto) {

        //调用sevice方法查询ApArticle列表数据
        List<ApArticle> articleList = apArticleService.getArticleListByType(ArticleConstants.LOADTYPE_LOAD_NEW, dto);
        return ResponseResult.okResult(articleList);
    }

    @PostMapping("/loadmore")
    public ResponseResult loadMoreArticle(@RequestBody ApArticleDto dto) {

        //调用sevice方法查询ApArticle列表数据
        List<ApArticle> articleList = apArticleService.getArticleListByType(ArticleConstants.LOADTYPE_LOAD_MORE, dto);
        return ResponseResult.okResult(articleList);
    }


    /**
     * 自媒体端的文章进行同步
     */
    @PostMapping("/save")
    public ResponseResult saveArticleViaNewsByFeign(@RequestBody ArticleDto dto){
        return apArticleService.saveArticleViaNews(dto);
    }
}

