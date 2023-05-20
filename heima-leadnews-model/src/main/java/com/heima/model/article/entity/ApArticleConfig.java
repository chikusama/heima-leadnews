package com.heima.model.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 文章配置表(ApArticleConfig)表实体类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:52
 */
@ApiModel("ApArticleConfig")
@Data
@TableName("ap_article_config")
public class ApArticleConfig implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文章配置主键
     */
    @ApiModelProperty(name = "文章配置主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 文章ID
     */
    @ApiModelProperty(name = "文章ID")
    @TableField("article_id")
    private Long articleId;
    /**
     * 是否可评论
     */
    @ApiModelProperty(name = "是否可评论")
    @TableField("is_comment")
    private Boolean comment;
    /**
     * 是否转发
     */
    @ApiModelProperty(name = "是否转发")
    @TableField("is_forward")
    private Boolean forward;
    /**
     * 是否下架
     */
    @ApiModelProperty(name = "是否下架")
    @TableField("is_down")
    private Short down;
    /**
     * 是否已删除
     */
    @ApiModelProperty(name = "是否已删除")
    @TableField("is_delete")
    private Short delete;
}

