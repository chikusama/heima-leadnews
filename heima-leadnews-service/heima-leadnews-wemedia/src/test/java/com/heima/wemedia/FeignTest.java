package com.heima.wemedia;


import com.heima.feign.article.ArticleNewsFeign;
import com.heima.model.article.dtos.ArticleDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class FeignTest {

    @Autowired
    ArticleNewsFeign articleNewsFeign;

    @Test
    void feignTest(){
        ArticleDto dto = new ArticleDto();
        dto.setContent("1234455325");
        dto.setAuthorId(1102);
        dto.setTitle("11111");
        dto.setLayout(1);
        dto.setLabels("黑马头条");
        dto.setPublishTime(new Date());
        dto.setImages("http://192.168.200.130:9000/leadnews/2021/04/26/5ddbdb5c68094ce393b08a47860da275.jpg");
        articleNewsFeign.saveArticleViaNewsByFeign(dto);
    }
}
