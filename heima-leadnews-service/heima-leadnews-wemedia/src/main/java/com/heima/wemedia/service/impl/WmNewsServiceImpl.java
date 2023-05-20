package com.heima.wemedia.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.common.aliyun.GreenImageScan;
import com.heima.common.aliyun.GreenTextScan;
import com.heima.common.redis.RedisCacheService;
import com.heima.file.service.FileStorageService;
import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.entity.WmMaterial;
import com.heima.model.wemedia.entity.WmNews;
import com.heima.model.wemedia.entity.WmNewsMaterial;
import com.heima.utils.thread.WmThreadLocalUtil;
import com.heima.common.content.ContentType;
import com.heima.common.content.NewsStatus;
import com.heima.wemedia.mapper.WmNewsMapper;
import com.heima.wemedia.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 自媒体图文内容信息表(WmNews)表服务实现类
 *
 * @author killerqueen
 * @since 2022-10-29 14:01:32
 */
@Service("wmNewsService")
@Slf4j
@Transactional
public class WmNewsServiceImpl extends ServiceImpl<WmNewsMapper, WmNews> implements WmNewsService {
    @Autowired
    WmNewsMaterialService wmNewsMaterialService;

    @Autowired
    WmMaterialService wmMaterialService;

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
    WmAutoScanService wmAutoScanService;

    @Autowired
    RedisCacheService cacheService;

    @Autowired
    private WmNewsTaskService wmNewsTaskService;


    @Override
    public PageResponseResult selectAllArticles(WmNewsPageReqDto wmNewsPageReqDto) {
        //调用接口分页查询
        IPage<WmNews> page = new Page<>(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize());
        LambdaQueryWrapper<WmNews> lqw = new LambdaQueryWrapper<>();

        //动态查询
        //根据标题进行查询
        lqw.like(wmNewsPageReqDto.getKeyword() != null, WmNews::getTitle, wmNewsPageReqDto.getKeyword());
        //根据频道进行查询
        lqw.eq(wmNewsPageReqDto.getChannelId() != null, WmNews::getChannelId, wmNewsPageReqDto.getChannelId());
        //根据发布还是编辑状态进行查询
        lqw.eq(wmNewsPageReqDto.getStatus() != null, WmNews::getStatus, wmNewsPageReqDto.getStatus());
        //根据日期进行查询
        if (wmNewsPageReqDto.getBeginPubdate() != null && wmNewsPageReqDto.getEndPubdate() != null) {
            lqw.between(wmNewsPageReqDto.getBeginPubdate().compareTo(wmNewsPageReqDto.getEndPubdate()) == -1, WmNews::getPublishTime, wmNewsPageReqDto.getBeginPubdate(), wmNewsPageReqDto.getEndPubdate());
        }
        //根据当前用户进行查询
        lqw.eq(WmNews::getUserId, WmThreadLocalUtil.getUser().getId());
        lqw.orderByDesc(WmNews::getCreatedTime);
        this.page(page, lqw);
        //创建page对象
        PageResponseResult pageResponseResult = new PageResponseResult(wmNewsPageReqDto.getPage(), wmNewsPageReqDto.getSize(), (int) page.getTotal());
        ResponseResult responseResult = ResponseResult.okResult(page.getRecords());
        BeanUtils.copyProperties(responseResult, pageResponseResult);
        //先将host置空
        pageResponseResult.setHost("");
        return pageResponseResult;

    }



    /**
     * 发布修改文章或保存为草稿
     * @param dto
     * @return
     */
    @Override
    public ResponseResult submitNews(WmNewsDto dto) {

        //0.条件判断
        if(dto == null || dto.getContent() == null){
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }

        //1.保存或修改文章

        WmNews wmNews = new WmNews();
        //属性拷贝 属性名词和类型相同才能拷贝
        BeanUtils.copyProperties(dto,wmNews);
        //封面图片  list---> string
        if(dto.getImages() != null && dto.getImages().size() > 0){
            //[1dddfsd.jpg,sdlfjldk.jpg]-->   1dddfsd.jpg,sdlfjldk.jpg
            String imageStr = StringUtils.join(dto.getImages(), ",");
            wmNews.setImages(imageStr);
        }
        //如果当前封面类型为自动 -1
        if(dto.getType().equals(ContentType.SYS_AUTO)){
            wmNews.setType(null);
        }

        upDownNews(wmNews);

        //2.判断是否为草稿  如果为草稿结束当前方法1
        if(dto.getStatus().equals(NewsStatus.DRAFT)){
            return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);
        }

        //3.不是草稿，保存文章内容图片与素材的关系
        //获取到文章内容中的图片信息
        List<String> materials =  ectractUrlInfo(dto.getContent());
        saveRelativeInfoForContent(materials,wmNews.getId());

        //4.不是草稿，保存文章封面图片与素材的关系，如果当前布局是自动，需要匹配封面图片
        saveRelativeInfoForCover(dto,wmNews,materials);

        //审核文章
        //        wmNewsAutoScanService.autoScanWmNews(wmNews.getId());
        wmNewsTaskService.addNewsToTask(wmNews.getId(),wmNews.getPublishTime());

        return ResponseResult.okResult(AppHttpCodeEnum.SUCCESS);

    }

    private List<String> ectractUrlInfo(String content) {
        return null;

    }

    private void saveRelativeInfoForCover(WmNewsDto dto, WmNews wmNews, List<String> materials) {


    }

    private void saveRelativeInfoForContent(List<String> materials, Integer id) {

    }

    @Override
    public void saveContent(WmNewsDto wmNewsDto) {
        //创建一个News对象
        WmNews wmNews = new WmNews();
        BeanUtils.copyProperties(wmNewsDto, wmNews);
        wmNews.setUserId(WmThreadLocalUtil.getUser().getId());
        wmNews.setCreatedTime(new Date());
        //将文章中的图片出去来放入中间表
        String content = wmNewsDto.getContent();
        //获取文章中的所有图片作为封面
        List<String> imageCover = wmNewsDto.getImages();
        //使用stream进行过滤,取出"type":"image"
        List<Map> contentList = JSONArray.parseArray(content, Map.class);
        //{type :
        // value : }
        List<String> images = contentList.stream().filter(map -> {
            if (map.get("type").equals("image")) {
                //包含这个值的留下返回true
                return true;
            }
            return false;
        }).map(m -> (
                //将value取出来存放成一个新的值
                m.get("value").toString()
        )).collect(Collectors.toList());
        //根据用户选取的封面类型进行判断
        Short type = wmNewsDto.getType();
        if (type == ContentType.SYS_AUTO) {
            //判断获取图的list
            int size = images.size();
            if (size == 0) {
                type = ContentType.NONE_PIC;

            } else if (size > 0 && size < 3) {
                //将图片集合中的第一张作为封面
                imageCover = images.stream().limit(1).collect(Collectors.toList());
                type = ContentType.ONE_PIC;

            } else if (size > 3) {
                //3张图作为封面
                imageCover = images.stream().limit(3).collect(Collectors.toList());
                type = ContentType.MULTI_PIC;

            }
        }
        //将值塞入
        if (StringUtils.isNotBlank(String.valueOf(images))) {

            wmNews.setImages(String.join(",", imageCover));
        }

        wmNews.setType(type);
        //根据wmNewsDto中的Id值判断是否存在
        Integer id = wmNews.getId();
        WmNews news = this.getOne(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getId, id));

        if (news == null) {
            //不存在则为新增
            this.save(wmNews);
        } else {
            //存在则为修改
            //删除原来关联中间表的信息
            wmNewsMaterialService.remove(Wrappers.<WmNewsMaterial>lambdaQuery().eq(WmNewsMaterial::getNewsId, wmNews.getId()));
            //将覆盖好的值存入中间表
            this.updateById(wmNews);
        }

        //判断是否为草稿
        Short status = wmNewsDto.getStatus();
        if (status != NewsStatus.DRAFT) {
            /**
             * type:0 内容引用 | 1 封面引用
             */
            //将正文中的图片插入中间表
            saveMaterialBatch(wmNews, images, (short) 0);
            //将封面插入中间表
            saveMaterialBatch(wmNews, imageCover, (short) 1);
        }

        //将news中的数据和article进行同步
        //autoScanWmNews(wmNews.getId());
        wmAutoScanService.autoScanWmNews(wmNews.getId());


    }

    /**
     * 管理文章上下架功能
     *
     * @param wmNews
     * @return
     */
    @Override
    public ResponseResult upDownNews(WmNews wmNews) {
        //根据传过来的惨呼进行查询
        WmNews news = getOne(Wrappers.<WmNews>lambdaQuery().eq(WmNews::getId, wmNews.getId()));
        //将状态进行修改
        news.setEnable(wmNews.getEnable());
        //修改数据库的表
        updateById(news);
        return ResponseResult.okResult(news);
    }

    /**
     * 保存中间表
     *
     * @param imageList
     * @param type
     */
    private void saveMaterialBatch(WmNews wmNews, List<String> imageList, Short type) {
        if (imageList.size() == 0) {
            return;
        }
        //根据imageCover中的值进行in查询
        List<WmMaterial> materials = wmMaterialService.list(Wrappers.<WmMaterial>lambdaQuery().in(imageList.size() > 0, WmMaterial::getUrl, imageList));
        //遍历集合将material封装到中间表的素材中
        List<WmNewsMaterial> newsMaterials = materials.stream().map(wmMaterial -> {
            WmNewsMaterial newsMaterial = new WmNewsMaterial();
            newsMaterial.setNewsId(wmNews.getId());
            newsMaterial.setMaterialId(wmMaterial.getId());
            newsMaterial.setOrd((short) materials.indexOf(wmMaterial));
            newsMaterial.setType(type);
            return newsMaterial;
        }).collect(Collectors.toList());

        //批量插入到中间表中
        wmNewsMaterialService.saveBatch(newsMaterials);

    }


    /**
     * 实现自动审核
     *
     * @param id 自媒体文章id
     */

   /* @Async
    public void autoScanWmNews(Integer id) {

        //根据id进行查询
        WmNews news = getById(id);
        log.info("获取到的news数据是:{}",news);

        //从news中提取:
        */

    /**
     * 1.正文+正文中的图片
     * 2.标题和封面
     *//*
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
                    String text = (String) nc.get("text");
                    newsContentBuilder.append(text);
                } else if (nc.get("type").equals("image")) {
                    //url字符串
                    String text = (String) nc.get("image");
                    images.add(text);
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
            if (imageScan.size()>0) {
                imageScan.forEach(imagePath -> {
                    byte[] bytes = fileStorageService.downLoadFile(imagePath);
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

        }

        getResultByAliScan(news);


    }

    private void getResultByAliScan(WmNews news) {
        log.info("获取到的news数据是:{}",news);
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
            Long articleId = (Long) responseResult.getData();

            news.setArticleId(articleId);

            news.setStatus(NewsStatus.RELEASED);
            updateById(news);
        }*/


    //文本和图片审核结果解析
    private boolean scanSuggest(WmNews news, Map map) {
        if (map.get("suggestion").equals("block")) {
            //修改状态 status
            news.setStatus(NewsStatus.JUDGE_FAILURE);
            //修改审核失败原因 reason
            news.setReason("审核内容违规");
            updateById(news);
            return true;
        }

        if (map.get("suggestion").equals("review")) {
            //修改状态 status
            news.setStatus(NewsStatus.MANUAL_CHECK);
            //修改审核失败原因 reason
            news.setReason("内容需要进一步人工审核");
            updateById(news);
            return true;
        }
        return false;
    }





}

