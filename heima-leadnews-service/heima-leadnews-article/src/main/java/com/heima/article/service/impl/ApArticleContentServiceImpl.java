package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.article.entity.ApArticleContent;
import com.heima.article.mapper.ApArticleContentMapper;
import com.heima.article.service.ApArticleContentService;
import org.springframework.stereotype.Service;

/**
 * 文章内容(ApArticleContent)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:53
 */
@Service("apArticleContentService")
public class ApArticleContentServiceImpl extends ServiceImpl<ApArticleContentMapper, ApArticleContent> implements ApArticleContentService {

}

