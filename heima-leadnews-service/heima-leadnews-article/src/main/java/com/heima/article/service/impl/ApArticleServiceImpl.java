package com.heima.article.service.impl;

import com.alibaba.cloud.commons.io.StringBuilderWriter;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.article.service.ApArticleConfigService;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleFreemarkerWorkService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.article.mapper.ApArticleMapper;
import com.heima.article.service.ApArticleService;
import com.heima.model.article.dtos.ApArticleDto;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.model.article.entity.ApArticleContent;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.sql.Wrapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文章表(ApArticle)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:51
 */
@Service("apArticleService")
public class ApArticleServiceImpl extends ServiceImpl<ApArticleMapper, ApArticle> implements ApArticleService {

    @Autowired
    private ApArticleMapper apArticleMapper;

    @Autowired
    private ApArticleConfigService apArticleConfigService;

    @Autowired
    ApArticleContentService apArticleContentService;

    @Autowired
    ApArticleFreemarkerWorkService freemarkerWorkService;


    @Override
    public List<ApArticle> getArticleListByType(Short loadtype, ApArticleDto dto) {
        return apArticleMapper.listLoadByType(loadtype, dto);
    }

    @Override
    public ResponseResult saveArticleViaNews(ArticleDto dto) {
        //feign是由news端进行远程调用的,因此dto中包含news的数据
        //需要将这个数据存储到三张表中
        //判断dto中的文章是否有值
        String content = dto.getContent();
        if (content == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST);
        }
        //判断此时数据中是否存在主键,如果不存在主键则为新增
        Long id = dto.getId();
        ApArticle apArticle = new ApArticle();
        BeanUtils.copyProperties(dto, apArticle);



        if (apArticle.getId() == null) {
            this.save(apArticle);
            //将apArticle中的值也同步到config和content中
            ApArticleConfig apArticleConfig = new ApArticleConfig();
            apArticleConfig.setArticleId(apArticle.getId());
            apArticleConfig.setDown((short) 0);
            apArticleConfig.setDelete((short) 0);
            apArticleConfigService.save(apArticleConfig);

            ApArticleContent apArticleContent = new ApArticleContent();
            apArticleContent.setArticleId(apArticle.getId());
            apArticleContent.setContent(content);
            apArticleContentService.save(apArticleContent);


        } else {
            //id不为0则为修改
            //根据id查询article中是否包含该条数据
            ApArticle articleById = this.getById(id);
            if (articleById != null) {
                this.updateById(apArticle);
            }

            ApArticleContent articleContent = apArticleContentService
                    .getOne(Wrappers.<ApArticleContent>lambdaQuery()
                            .eq(ApArticleContent::getArticleId, id));

            if (articleContent != null) {
                articleContent.setContent(content);
                apArticleContentService.updateById(articleContent);
            }


        }
        String articleUrl = freemarkerWorkService.getFreemarkerUrl(apArticle.getId());
        System.out.println("articleUrl = " + articleUrl);
        apArticle.setStaticUrl(articleUrl);
        this.updateById(apArticle);

        return ResponseResult.okResult(apArticle.getId() == null ? "" : apArticle.getId());

    }
}

