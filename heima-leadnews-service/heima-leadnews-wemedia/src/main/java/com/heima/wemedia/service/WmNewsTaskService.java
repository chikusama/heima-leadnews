package com.heima.wemedia.service;

import com.heima.model.wemedia.entity.WmNews;

import java.util.Date;

public interface WmNewsTaskService {

    public void addNewsToTask(WmNews news);

    public void scanNewsByTask();

    void addNewsToTask(Integer id, Date publishTime);

}
