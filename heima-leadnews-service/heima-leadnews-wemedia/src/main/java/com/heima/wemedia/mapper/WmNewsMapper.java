package com.heima.wemedia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.heima.model.wemedia.entity.WmNews;
import org.springframework.stereotype.Repository;

/**
 * 自媒体图文内容信息表(WmNews)表数据库访问层
 *
 * @author killerqueen
 * @since 2022-10-29 14:01:32
 */
@Repository
public interface WmNewsMapper extends BaseMapper<WmNews> {

}

