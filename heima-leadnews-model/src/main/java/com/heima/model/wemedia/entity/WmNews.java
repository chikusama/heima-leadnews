package com.heima.model.wemedia.entity;

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
 * 自媒体图文内容信息表(WmNews)表实体类
 *
 * @author killerqueen
 * @since 2022-10-29 14:01:32
 */
@ApiModel("WmNews")
@Data
@TableName("wm_news")
public class WmNews implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 自媒体用户ID
     */
    @ApiModelProperty(name = "自媒体用户ID")
    @TableField("user_id")
    private Integer userId;
    /**
     * 标题
     */
    @ApiModelProperty(name = "标题")
    @TableField("title")
    private String title;
    /**
     * 图文内容
     */
    @ApiModelProperty(name = "图文内容")
    @TableField("content")
    private String content;
    /**
     * 文章布局
     * 0 无图文章
     * 1 单图文章
     * 3 多图文章
     */
    @ApiModelProperty(name = "文章布局 0无图文章 1单图文章 3多图文章")
    @TableField("type")
    private Short type;
    /**
     * 图文频道ID
     */
    @ApiModelProperty(name = "图文频道ID")
    @TableField("channel_id")
    private Integer channelId;
    @ApiModelProperty(name = "${column.comment}")
    @TableField("labels")
    private String labels;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @TableField("created_time")
    private Date createdTime;
    /**
     * 提交时间
     */
    @ApiModelProperty(name = "提交时间")
    @TableField("submited_time")
    private Date submitedTime;
    /**
     * 当前状态
     * 0 草稿
     * 1 提交（待审核）
     * 2 审核失败
     * 3 人工审核
     * 4 人工审核通过
     * 8 审核通过（待发布）
     * 9 已发布
     */
    @ApiModelProperty(name = "当前状态  0草稿 1提交（待审核）  2审核失败 3人工审核 4人工审核通过 8审核通过（待发布） 9已发布")
    @TableField("status")
    private Short status;
    /**
     * 定时发布时间，不定时则为空
     */
    @ApiModelProperty(name = "定时发布时间，不定时则为空")
    @TableField("publish_time")
    private Date publishTime;
    /**
     * 拒绝理由
     */
    @ApiModelProperty(name = "拒绝理由")
    @TableField("reason")
    private String reason;
    /**
     * 发布库文章ID
     */
    @ApiModelProperty(name = "发布库文章ID")
    @TableField("article_id")
    private Long articleId;
    /**
     * //图片用逗号分隔
     */
    @ApiModelProperty(name = "//图片用逗号分隔")
    @TableField("images")
    private String images;
    @ApiModelProperty(name = "${column.comment}")
    @TableField("enable")
    private Short enable;
}

