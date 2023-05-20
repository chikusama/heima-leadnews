package com.heima.feign.article;

import com.heima.feign.article.ArticleNewsFeign;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import org.springframework.stereotype.Component;

@Component
public class IArticleFeignFallback  implements ArticleNewsFeign {
    @Override
    public ResponseResult saveArticleViaNewsByFeign(ArticleDto dto) {
        return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
    }
}
