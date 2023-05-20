package com.heima.model.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * 文章表(ApArticle)表实体类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:51
 */
@ApiModel("ApArticle")
@Data
@TableName("ap_article")
public class ApArticle implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 标题
     */
    @ApiModelProperty(name = "标题")
    @TableField("title")
    private String title;
    /**
     * 文章作者的ID
     */
    @ApiModelProperty(name = "文章作者的ID")
    @TableField("author_id")
    private Integer authorId;
    /**
     * 作者昵称
     */
    @ApiModelProperty(name = "作者昵称")
    @TableField("author_name")
    private String authorName;
    /**
     * 文章所属频道ID
     */
    @ApiModelProperty(name = "文章所属频道ID")
    @TableField("channel_id")
    private Integer channelId;
    /**
     * 频道名称
     */
    @ApiModelProperty(name = "频道名称")
    @TableField("channel_name")
    private String channelName;
    /**
     * 文章布局类型：
     * 0 无图文章
     * 1 单图文章
     * 2 多图文章
     */
    @ApiModelProperty(name = "文章布局类型： 0无图文章 1单图文章2多图文章")
    @TableField("layout")
    private Integer layout;
    /**
     * 文章标记
     * 0 普通文章
     * 1 热点文章
     * 2 置顶文章
     * 3 精品文章
     * 4 大V 文章
     */
    @ApiModelProperty(name = "文章标记 0普通文章 1热点文章 2置顶文章 3精品文章 4大V 文章")
    @TableField("flag")
    private Integer flag;
    /**
     * 文章图片
     * 多张逗号分隔
     */
    @ApiModelProperty(name = "文章图片多张逗号分隔")
    @TableField("images")
    private String images;
    /**
     * 文章标签最多3个 逗号分隔
     */
    @ApiModelProperty(name = "文章标签最多3个 逗号分隔")
    @TableField("labels")
    private String labels;
    /**
     * 点赞数量
     */
    @ApiModelProperty(name = "点赞数量")
    @TableField("likes")
    private Integer likes;
    /**
     * 收藏数量
     */
    @ApiModelProperty(name = "收藏数量")
    @TableField("collection")
    private Integer collection;
    /**
     * 评论数量
     */
    @ApiModelProperty(name = "评论数量")
    @TableField("comment")
    private Integer comment;
    /**
     * 阅读数量
     */
    @ApiModelProperty(name = "阅读数量")
    @TableField("views")
    private Integer views;
    /**
     * 省市
     */
    @ApiModelProperty(name = "省市")
    @TableField("province_id")
    private Integer provinceId;
    /**
     * 市区
     */
    @ApiModelProperty(name = "市区")
    @TableField("city_id")
    private Integer cityId;
    /**
     * 区县
     */
    @ApiModelProperty(name = "区县")
    @TableField("county_id")
    private Integer countyId;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @TableField("created_time")
    private Date createdTime;
    /**
     * 发布时间
     */
    @ApiModelProperty(name = "发布时间")
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 同步状态
     */
    @ApiModelProperty(name = "同步状态")
    @TableField("sync_status")
    private Integer syncStatus;
    /**
     * 来源
     */
    @ApiModelProperty(name = "来源")
    @TableField("origin")
    private Integer origin;
    /**
     * 静态页面地址
     */
    @ApiModelProperty(name = "静态页面地址")
    @TableField("static_url")
    private String staticUrl;
}

