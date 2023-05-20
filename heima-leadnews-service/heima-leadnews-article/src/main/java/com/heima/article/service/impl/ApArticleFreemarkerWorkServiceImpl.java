package com.heima.article.service.impl;


import com.alibaba.cloud.commons.io.StringBuilderWriter;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.service.ApArticleContentService;
import com.heima.article.service.ApArticleFreemarkerWorkService;
import com.heima.article.service.ApArticleService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.article.entity.ApArticle;
import com.heima.model.article.entity.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.net.nntp.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Service
public class ApArticleFreemarkerWorkServiceImpl implements ApArticleFreemarkerWorkService {


    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ApArticleContentService apArticleContentService;

    @Autowired
    Configuration configuration;

    @Autowired
    ApArticleService apArticleService;

    public String getFreemarkerUrl(Long id){
        ApArticle dto = apArticleService.getById(id);
        //从数据库中查询文章内容
        ApArticleContent articleContent = apArticleContentService.getOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, dto.getId()));
        //使用freemarker将模板写入
        //创建一个map集合用来存放对像方便解析
        Map<String,Object> root = new HashMap<>();
        //将内容转换成json格式
        //JSONArray content = JSONArray.parseArray(articleContent.getContent());
        JSONArray contentArray = JSONArray.parseArray(articleContent.getContent());
        root.put("content",contentArray);
        //使用freemarker
        Template template = null;
        try {
            template = configuration.getTemplate("article.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //导出freemarker
        Writer out = new StringBuilderWriter();
        try {
            template.process(root,out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将freemarker做成的模板保存到minio中
        InputStream byteInputStream = new ByteArrayInputStream(out.toString().getBytes());
        String url = fileStorageService.uploadHtmlFile("", dto.getId() + ".html", byteInputStream);
        return  url;
    }
}
