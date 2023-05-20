package com.heima.article.controller.v1;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heima.article.service.ApArticleContentService;

/**
 * 文章内容(ApArticleContent)表控制层
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:52
 */
@Api(tags = "文章内容接口")
@RestController
@RequestMapping("/api/v1/apArticleContent")
@Slf4j
public class ApArticleContentController {
    /**
     * Service层对象
     */
    @Autowired
    private ApArticleContentService apArticleContentService;


}

