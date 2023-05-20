package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.content.NewsStatus;
import com.heima.common.tess4j.Tess4jClient;
import com.heima.feign.article.ArticleNewsFeign;
import com.heima.file.service.FileStorageService;
import com.heima.model.article.dtos.ArticleDto;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.entity.WmSensitive;
import com.heima.utils.common.SensitiveWordUtil;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.*;
import lombok.extern.slf4j.Slf4j;
import net.sourceforge.tess4j.TesseractException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@Async
public class WmAutoScanServiceImpl implements WmAutoScanService {

    @Autowired
    WmNewsMapper wmNewsMapper;

    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    GreenImageScan greenImageScan;

    @Autowired
    GreenTextScan greenTextScan;

    @Autowired
    WmChannelService wmChannelService;

    @Autowired
    WmUserService userService;

    @Autowired
    ArticleNewsFeign articleNewsFeign;

    @Autowired
    WmSensitiveService wmSensitiveService;

    @Autowired
    Tess4jClient tess4jClient;

    /**
     * 实现自动审核
     *
     * @param id 自媒体文章id
     */
    public void autoScanWmNews(Integer id) {
        //根据id进行查询
        WmNews news = wmNewsMapper.selectById(id);
        log.info("获取到的news数据是:{}", news);

        //从news中提取:
        /**
         * 1.正文+正文中的图片
         * 2.标题和封面
         */
        //创建一个字符串用来进行拼接
        StringBuilder newsContentBuilder = new StringBuilder();
        //根据news的status进行判断
        Short status = news.getStatus();
        if (status == NewsStatus.DRAFT) {
            return;
        }
        //从news中获取正文,包括images,因此要进行过滤
        String content = news.getContent();
        //创建一个图片字符串用来存储
        List<String> images = new ArrayList<>();
        List<Map> newsContent = JSONArray.parseArray(content, Map.class);
        if (status == NewsStatus.SUBMIT) {
            //从map集合中拿去对应的数据
            newsContent.forEach(nc -> {
                if (nc.get("type").equals("text")) {
                    newsContentBuilder.append((String) nc.get("value"));
                } else if (nc.get("type").equals("image")) {
                    //url字符串
                    images.add((String) nc.get("value"));
                }
            });
            //从封面和标题中获取数据进行存储
            newsContentBuilder.append(news.getTitle());
            //从封面中获取数据
            String newsImages = news.getImages();
            if (StringUtils.isNotBlank(newsImages)) {
                images.addAll(Arrays.asList(newsImages.split(",")));
            }

            List<String> imageScan = images.stream().distinct().collect(Collectors.toList());
            boolean sensitiveResult = getOwnSensitiveWords(news, newsContentBuilder.toString());
            if (sensitiveResult) {
                return;
            }

            //1.文本审核
            try {
                //文本审核结果解析： 例如{suggestion=block, label=contraband}  {suggestion=review,label=xx}
                Map textMap = greenTextScan.greeTextScan(newsContentBuilder.toString());
                //审核失败
                if (scanSuggest(news, textMap)) return;

            } catch (Exception e) {
                e.printStackTrace();
            }

            //2.图片审核 所有图片地址imagesScan
            List<byte[]> list = new ArrayList<>();

            if (imageScan.size() > 0) {
                for (String imageUrl : imageScan) {
                    byte[] bytes = fileStorageService.downLoadFile(imageUrl);
                    list.add(bytes);
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                    try {
                        String ocrImageScan =  tess4jClient.doOCR(ImageIO.read(inputStream));
                        if (getOwnSensitiveWords(news, ocrImageScan))
                            return;
                    } catch (TesseractException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Map imageMap = greenImageScan.imageScan(list);
                    //审核失败
                    if (scanSuggest(news, imageMap)) return;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        getResultByAliScan(news);


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

    private void getResultByAliScan(WmNews news) {
        log.info("获取到的news数据是:{}", news);
        //将news转换成article
        ArticleDto articleDto = new ArticleDto();
        BeanUtils.copyProperties(news, articleDto);
        articleDto.setAuthorId(news.getUserId());
        articleDto.setAuthorName(userService.getById(news.getUserId()).getNickname());

        articleDto.setChannelId(wmChannelService.getById(news.getChannelId()).getId());
        articleDto.setLayout(Integer.valueOf(news.getType()));

        articleDto.setChannelName(wmChannelService.getById(news.getChannelId()).getName());
        articleDto.setPublishTime(new Date());
        articleDto.setPublishTime(new Date());
        if (news.getArticleId() != null) {
            articleDto.setId(news.getArticleId());
        }

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

    /**
     * 敏感词库处理
     */
    public boolean getOwnSensitiveWords(WmNews wmNews, String text) {
        List<WmSensitive> list = wmSensitiveService.list();
        List<String> sensitiveList = list.stream().map(m -> m.getSensitives()).collect(Collectors.toList());
        SensitiveWordUtil.initMap(sensitiveList);

        if (StringUtils.isNotBlank(String.valueOf(text))) {
            Map<String, Integer> matchWords = SensitiveWordUtil.matchWords(text.toString());
            if (matchWords.size() > 0) {
                wmNews.setStatus(NewsStatus.JUDGE_FAILURE);

                wmNews.setReason("审核内容存在敏感词汇:" + matchWords);
                wmNewsMapper.updateById(wmNews);
                return true;
            }
        }
        return false;
    }

}
