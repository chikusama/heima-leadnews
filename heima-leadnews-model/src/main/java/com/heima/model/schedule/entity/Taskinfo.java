package com.heima.model.schedule.entity;

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
 * (Taskinfo)表实体类
 *
 * @author killerqueen
 * @since 2022-11-03 16:00:38
 */
@ApiModel("Taskinfo")
@Data
@TableName("taskinfo")
public class Taskinfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 任务id
     */
    @ApiModelProperty(name = "任务id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long taskId;
    /**
     * 执行时间
     */
    @ApiModelProperty(name = "执行时间")
    @TableField("execute_time")
    private Date executeTime;
    /**
     * 参数
     */
    @ApiModelProperty(name = "参数")
    @TableField("parameters")
    private byte[] parameters;
    /**
     * 优先级
     */
    @ApiModelProperty(name = "优先级")
    @TableField("priority")
    private Integer priority;
    /**
     * 任务类型
     */
    @ApiModelProperty(name = "任务类型")
    @TableField("task_type")
    private Integer taskType;
}

