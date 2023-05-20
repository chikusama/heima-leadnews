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
 * 自媒体图文素材信息表(WmMaterial)表实体类
 *
 * @author killerqueen
 * @since 2022-10-28 17:09:11
 */
@ApiModel("WmMaterial")
@Data
@TableName("wm_material")
public class WmMaterial implements Serializable {
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
     * 图片地址
     */
    @ApiModelProperty(name = "图片地址")
    @TableField("url")
    private String url;
    /**
     * 素材类型
     * 0 图片
     * 1 视频
     */
    @ApiModelProperty(name = "素材类型 0图片 1视频")
    @TableField("type")
    private Short type;
    /**
     * 是否收藏
     */
    @ApiModelProperty(name = "是否收藏")
    @TableField("is_collection")
    private Integer isCollection;
    /**
     * 创建时间
     */
    @ApiModelProperty(name = "创建时间")
    @TableField("created_time")
    private Date createdTime;
}

