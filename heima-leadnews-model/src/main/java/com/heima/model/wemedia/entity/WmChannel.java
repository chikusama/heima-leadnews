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
 * 频道信息表(WmChannel)表实体类
 *
 * @author killerqueen
 * @since 2022-10-29 11:57:13
 */
@ApiModel("WmChannel")
@Data
@TableName("wm_channel")
public class WmChannel implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "${column.comment}")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 频道名称
     */
    @ApiModelProperty(name = "频道名称")
    @TableField("name")
    private String name;
    /**
     * 频道描述
     */
    @ApiModelProperty(name = "频道描述")
    @TableField("description")
    private String description;
    /**
     * 是否默认频道
     */
    @ApiModelProperty(name = "是否默认频道")
    @TableField("is_default")
    private Boolean isdDefault;


    @ApiModelProperty(name = "${column.comment}")
    @TableField("status")
    private Boolean status;
    /**
     * 默认排序
     */
    @ApiModelProperty(name = "默认排序")
    @TableField("ord")
    private Integer ord;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @TableField("created_time")
    private Date createdTime;
}

