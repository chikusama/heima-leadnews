package com.heima.wemedia.service;

import com.heima.model.wemedia.entity.WmNews;

public interface WmNewsScanService {
    /**
     * 文章审核
     *
     * @param news
     */
    public void scanNews(WmNews news);
}
