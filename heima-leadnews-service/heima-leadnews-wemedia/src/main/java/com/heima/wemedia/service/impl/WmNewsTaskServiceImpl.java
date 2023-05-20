package com.heima.wemedia.service.impl;


import com.heima.feign.schedule.IScheduleClient;
import com.heima.model.common.enums.TaskTypeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.utils.common.ProtostuffUtil;
import com.heima.wemedia.service.WmNewsScanService;
import com.heima.wemedia.service.WmNewsTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class WmNewsTaskServiceImpl implements WmNewsTaskService {
    @Resource
    IScheduleClient scheduleClient;
    @Autowired
    WmNewsScanService newsScanService;

    /**
     * 需要使用Feign远程调用Schedule中添加任务接口
     *
     * @param news
     */
    @Override
    public void addNewsToTask(WmNews news) {
        log.info("添加任务到延迟服务中----begin");
        if (news != null) {
            //1.创建Task对象
            Task task = new Task();
            //文章发布执行时间
            task.setExecuteTime(news.getPublishTime().getTime());
            task.setTaskType(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType());
            task.setPriority(TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
            task.setParameters(ProtostuffUtil.serialize(news));
            //2.调用Feign完成添加任务到延迟队列中
            scheduleClient.addTask(task);
        }
        log.info("添加任务到延迟服务中----end");
    }

    /**
     * 每秒从list中拉去任务进行消费
     */
    @Override
    @Scheduled(fixedRate = 1000)
    public void scanNewsByTask() {
        log.info("拉取延迟队列中任务");
        //1.调用Feign远程拉取任务
        Task task = scheduleClient.poll(TaskTypeEnum.NEWS_SCAN_TIME.getTaskType(), TaskTypeEnum.NEWS_SCAN_TIME.getPriority());
        //2.根据传递的参数进行审核文章、保存article文章
        if (task != null && task.getParameters().length > 0) {
            WmNews news = ProtostuffUtil.deserialize(task.getParameters(), WmNews.class);
            newsScanService.scanNews(news);
        }
    }

    @Override
    public void addNewsToTask(Integer id, Date publishTime) {

    }


}
