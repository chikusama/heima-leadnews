package com.heima.article.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.article.entity.ApArticleConfig;
import com.heima.article.mapper.ApArticleConfigMapper;
import com.heima.article.service.ApArticleConfigService;
import org.springframework.stereotype.Service;

/**
 * 文章配置表(ApArticleConfig)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:52
 */
@Service("apArticleConfigService")
public class ApArticleConfigServiceImpl extends ServiceImpl<ApArticleConfigMapper, ApArticleConfig> implements ApArticleConfigService {

}

