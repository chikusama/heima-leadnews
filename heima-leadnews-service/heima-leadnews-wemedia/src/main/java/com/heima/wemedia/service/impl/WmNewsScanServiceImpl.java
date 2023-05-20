package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.content.NewsStatus;
import com.heima.feign.article.ArticleNewsFeign;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.entity.WmChannel;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.entity.WmUser;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.WmChannelService;
import com.heima.wemedia.service.WmNewsScanService;
import com.heima.wemedia.service.WmUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WmNewsScanServiceImpl implements WmNewsScanService {
    @Autowired
    GreenImageScan greenImageScan;

    @Autowired
    GreenTextScan greenTextScan;

    @Autowired
    FileStorageService fieFileStorageService;

    @Autowired
    WmUserService wmUserService;

    @Autowired
    WmChannelService channelService;


    @Autowired
    ArticleNewsFeign articleNewsFeign;

    @Autowired
    WmNewsMapper wmNewsMapper;

    /**
     * 在提交待审核时候会进行文本、图片审核
     *
     * @Async作用：采用异步线程去执行文章审核；放在方法/类，那么底层使用aop产生代理
     */
    @Override
    @Async
    public void scanNews(WmNews news) {
        long id = Thread.currentThread().getId();
        System.out.println("id2 = " + id);

        // 在提交待审核时候会进行文本、图片审核
        if (news.getStatus() == NewsStatus.SUBMIT) {
            //0.将news文本=标题+正文文本； news图片url=正文图片url+封面图片url
            //定义一个content拼接所有文本
            StringBuilder textScan = new StringBuilder();
            //定义一个集合用来封装所有图片地址
            List<String> images = new ArrayList<>();

            //解析news获取标题作为文本
            textScan.append(news.getTitle());

            //解析news获取封面图片
            String imagesFromCover = news.getImages();
            if (StringUtils.isNotBlank(imagesFromCover)) {
                images.addAll(Arrays.asList(imagesFromCover.split(",")));
            }


            //解析news中正文获取文本和图片 [{"type":"text","value":"1234"},{"type":"image","value":"http://..."}]
            String content = news.getContent();
            List<Map> maps = JSONArray.parseArray(content, Map.class);
            maps.forEach(map -> {
                //提取文本
                if (map.get("type").equals("text")) {
                    textScan.append(map.get("value"));
                }
                //提取图片
                if (map.get("type").equals("image")) {
                    images.add((String) map.get("value"));
                }
            });
            //针对图片地址需要进行去重
            List<String> imagesScan = images.stream().distinct().collect(Collectors.toList());


            //1.文本审核
            try {
                //文本审核结果解析： 例如{suggestion=block, label=contraband}  {suggestion=review,label=xx}
                Map textMap = greenTextScan.greeTextScan(textScan.toString());
                //审核失败
                if (scanSuggest(news, textMap)) return;

            } catch (Exception e) {
                e.printStackTrace();
            }


            //2.图片审核 所有图片地址imagesScan
            List<byte[]> list = new ArrayList<>();
            if (imagesScan.size() > 0) {
                imagesScan.forEach(imageUrl -> {
                    byte[] bytes = fieFileStorageService.downLoadFile(imageUrl);
                    list.add(bytes);
                });
                try {
                    Map imageMap = greenImageScan.imageScan(list);
                    //审核失败
                    if (scanSuggest(news, imageMap)) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //3.审核通过：ArticleFeign完成远程调用文章微服务保存相关article表
            //WmNews对象---->ArticleDto
            ArticleDto articleDto = new ArticleDto();
            BeanUtils.copyProperties(news, articleDto);
            //设置作者
            articleDto.setAuthorId(news.getUserId());
            WmUser wmUser = wmUserService.getById(news.getUserId());
            articleDto.setAuthorName(wmUser.getNickname());

            //设置频道名称
            WmChannel channel = channelService.getById(news.getChannelId());
            articleDto.setChannelName(channel.getName());
            //设置文章布局
            articleDto.setLayout(Integer.valueOf(news.getType()));
            //设置创建时间
            articleDto.setCreatedTime(new Date());

            //设置文章id（修改场景）
            if (news.getArticleId() != null) {
                articleDto.setId(news.getArticleId());
            }
            //使用feign远程保存文章
            ResponseResult responseResult = articleNewsFeign.saveArticleViaNewsByFeign(articleDto);
            if (responseResult.getCode() == 200) {
                //如果审核成功：修改news中status=9 会写articleId
                Long articleId = (Long) responseResult.getData();
                //修改news表中article_id
                news.setArticleId(articleId);
                //修改news表中的状态为9 已经发布
                news.setStatus(NewsStatus.RELEASED);
                //update wm_news set status=9 ,article_id=?,...where id=?
                wmNewsMapper.updateById(news);
            }
        }

    }

    //文本和图片审核结果解析
    private boolean scanSuggest(WmNews news, Map map) {
        if (map.get("suggestion").equals("block")) {
            //修改状态 status
            news.setStatus(NewsStatus.JUDGE_FAILURE);
            //修改审核失败原因 reason
            news.setReason("审核内容违规");
            wmNewsMapper.updateById(news);
            return true;
        }

        if (map.get("suggestion").equals("review")) {
            //修改状态 status
            news.setStatus(NewsStatus.MANUAL_CHECK);
            //修改审核失败原因 reason
            news.setReason("内容需要进一步人工审核");
            wmNewsMapper.updateById(news);
            return true;
        }
        return false;
    }
}
