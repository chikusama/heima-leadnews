package com.heima.article;


import com.alibaba.cloud.commons.io.StringBuilderWriter;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.article.service.ApArticleContentService;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.entity.ApArticleContent;
import freemarker.template.Configuration;
import freemarker.template.Template;
import io.minio.MinioClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class ArticleTest {

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    ApArticleContentService apArticleContentService;

    @Autowired
    Configuration configuration;

    @Autowired
    MinioClient minioClient;

    @Test
    void minIoFileStarterTest() throws FileNotFoundException {
        InputStream inputStream = new FileInputStream("C:\\Users\\redhe\\Pictures\\previewFix.jpg");
        String urlFile = fileStorageService.uploadHtmlFile("", "1.jpg", inputStream);
        System.out.println("urlFile = " + urlFile);
    }
    /**
     * 将数据库中的内容转换成template上传到minio
     */
    @Test
    void uploadContentTest() throws Exception {
        Long articleId=1302862387124125698L;
        //从数据库中查询文章内容
        ApArticleContent articleContent = apArticleContentService.getOne(Wrappers.<ApArticleContent>lambdaQuery().eq(ApArticleContent::getArticleId, articleId));
        //使用freemarker将模板写入
        //创建一个map集合用来存放对像方便解析
        Map<String,Object> root = new HashMap<>();
        //将内容转换成json格式
        JSONArray content = JSONArray.parseArray(articleContent.getContent());
        root.put("content",content);
        //使用freemarker
        Template template = configuration.getTemplate("article.ftl");
        //导出freemarker
        Writer out = new StringBuilderWriter();
        template.process(root,out);
        //将freemarker做成的模板保存到minio中

        InputStream byteInputStream = new ByteArrayInputStream(out.toString().getBytes());
        String s = fileStorageService.uploadHtmlFile("", articleId + ".html", byteInputStream);
        System.out.println("s = " + s);

    }
}
