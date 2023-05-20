package com.heima.model.wemedia.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 自媒体图文引用素材信息表(WmNewsMaterial)表实体类
 *
 * @author killerqueen
 * @since 2022-10-29 17:22:51
 */
@ApiModel("WmNewsMaterial")
@Data
@TableName("wm_news_material")
public class WmNewsMaterial implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 素材ID
     */
    @ApiModelProperty(name = "素材ID")
    @TableField("material_id")
    private Integer materialId;
    /**
     * 图文ID
     */
    @ApiModelProperty(name = "图文ID")
    @TableField("news_id")
    private Integer newsId;
    /**
     * 引用类型
     * 0 内容引用
     * 1 主图引用
     */
    @ApiModelProperty(name = "引用类型 0内容引用 1主图引用")
    @TableField("type")
    private Short type;
    /**
     * 引用排序
     */
    @ApiModelProperty(name = "引用排序")
    @TableField("ord")
    private Short ord;
}

