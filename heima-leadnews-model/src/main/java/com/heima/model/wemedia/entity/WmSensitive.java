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
 * 敏感词信息表(WmSensitive)表实体类
 *
 * @author killerqueen
 * @since 2022-11-01 17:49:59
 */
@ApiModel("WmSensitive")
@Data
@TableName("wm_sensitive")
public class WmSensitive implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 敏感词
     */
    @ApiModelProperty(name = "敏感词")
    @TableField("sensitives")
    private String sensitives;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @TableField("created_time")
    private Date createdTime;
}

