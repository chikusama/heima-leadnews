package com.heima.article.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.dtos.ApArticleDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 文章表(ApArticle)表数据库访问层
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:50
 */
@Repository
public interface ApArticleMapper extends BaseMapper<ApArticle> {

    List<ApArticle> listLoadByType(@Param("loadtype") Short loadtype,@Param("dto") ApArticleDto dto);
}

