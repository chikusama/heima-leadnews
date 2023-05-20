package com.heima.wemedia.controller.v1;


import com.heima.model.common.dtos.PageResponseResult;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.wemedia.dtos.WmMaterialDto;
import com.heima.model.wemedia.dtos.WmNewsDto;
import com.heima.model.wemedia.dtos.WmNewsPageReqDto;
import com.heima.model.wemedia.entity.WmNews;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.heima.wemedia.service.WmNewsService;

/**
 * 自媒体图文内容信息表(WmNews)表控制层
 *
 * @author killerqueen
 * @since 2022-10-29 14:01:31
 */
@Api(tags = "自媒体图文内容信息表接口")
@RestController
@RequestMapping("/api/v1/news")
@Slf4j
public class WmNewsController {
    /**
     * Service层对象
     */
    @Autowired
    private WmNewsService wmNewsService;


    /**
     * http://localhost:8802/wemedia/MEDIA/wemedia/api/v1/news/list
     */

    //文章列表查询
    @PostMapping("/list")
    public PageResponseResult getAllArticles(@RequestBody WmNewsPageReqDto wmNewsPageReqDto) {
        return wmNewsService.selectAllArticles(wmNewsPageReqDto);
    }

    @PostMapping("/submit")
    public ResponseResult submitContent(@RequestBody WmNewsDto wmNewsDto) {
        wmNewsService.saveContent(wmNewsDto);
        return ResponseResult.okResult("success");
    }

    /**
     * 修改接口
     *
     * @param id
     * @return
     */
    @GetMapping("one/{id}")
    public ResponseResult getOne(@PathVariable Integer id) {
        WmNews news = wmNewsService.getById(id);
        return ResponseResult.okResult(news);
    }

    /**
     * 上下架接口
     */
    @PostMapping("/down_or_up")
    public ResponseResult upDownNews(@RequestBody WmNews wmNews) {
        return wmNewsService.upDownNews(wmNews);
    }
}

