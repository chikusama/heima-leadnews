package com.heima.article.controller.v1;


import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heima.article.service.ApArticleConfigService;

/**
 * 文章配置表(ApArticleConfig)表控制层
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:51
 */
@Api(tags = "文章配置表接口")
@RestController
@RequestMapping("/api/v1/apArticleConfig")
@Slf4j
public class ApArticleConfigController {
    /**
     * Service层对象
     */
    @Autowired
    private ApArticleConfigService apArticleConfigService;

}

