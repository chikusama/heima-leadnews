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
 * 文章内容(ApArticleContent)表实体类
 *
 * @author killerqueen
 * @since 2022-10-26 12:25:52
 */
@ApiModel("ApArticleContent")
@Data
@TableName("ap_article_content")
public class ApArticleContent implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 文章内容主键
     */
    @ApiModelProperty(name = "文章内容主键")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 文章ID
     */
    @ApiModelProperty(name = "文章ID")
    @TableField("article_id")
    private Long articleId;
    /**
     * 文章内容
     */
    @ApiModelProperty(name = "文章内容")
    @TableField("content")
    private String content;
}

