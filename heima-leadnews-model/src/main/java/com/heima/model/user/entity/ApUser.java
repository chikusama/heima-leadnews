package com.heima.model.user.entity;

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
 * APP用户信息表(ApUser)表实体类
 *
 * @author cyyer
 * @since 2022-10-25 11:47:48
 */
@ApiModel("ApUser")
@Data
@TableName("ap_user")
public class ApUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(name = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 密码、通信等加密盐
     */
    @ApiModelProperty(name = "密码、通信等加密盐")
    @TableField("salt")
    private String salt;
    /**
     * 用户名
     */
    @ApiModelProperty(name = "用户名")
    @TableField("name")
    private String name;
    /**
     * 密码,md5加密
     */
    @ApiModelProperty(name = "密码,md5加密")
    @TableField("password")
    private String password;
    /**
     * 手机号
     */
    @ApiModelProperty(name = "手机号")
    @TableField("phone")
    private String phone;
    /**
     * 头像
     */
    @ApiModelProperty(name = "头像")
    @TableField("image")
    private String image;
    /**
     * 性别：0 男|1女|2|未知
     */
    @ApiModelProperty(name = "性别：0 男|1女|2|未知")
    @TableField("sex")
    private Boolean sex;
    /**
     * 是否具备资质：0未|1是
     */
    @ApiModelProperty(name = "是否具备资质：0未|1是")
    @TableField("is_certification")
    private Boolean certification;
    /**
     * 是否身份认证
     */
    @ApiModelProperty(name = "是否身份认证")
    @TableField("is_identity_authentication")
    private Boolean identityAuthentication;
    /**
     * 状态：0正常|1锁定
     */
    @ApiModelProperty(name = "状态：0正常|1锁定")
    @TableField("status")
    private Boolean status;
    /**
     * 用户角色：0普通用户|1 自媒体人|2 大V
     */
    @ApiModelProperty(name = "用户角色：0普通用户|1 自媒体人|2 大V")
    @TableField("flag")
    private Boolean flag;
    /**
     * 注册时间
     */
    @ApiModelProperty(name = "注册时间")
    @TableField("created_time")
    private Date createdTime;
}

