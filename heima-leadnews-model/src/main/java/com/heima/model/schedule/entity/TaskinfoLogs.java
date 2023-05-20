package com.heima.model.schedule.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.io.Serializable;

/**
 * (TaskinfoLogs)表实体类
 *
 * @author killerqueen
 * @since 2022-11-03 16:00:37
 */
@ApiModel("TaskinfoLogs")
@Data
@TableName("taskinfo_logs")
public class TaskinfoLogs implements Serializable {
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
    /**
     * 版本号,用乐观锁
     */
    @ApiModelProperty(name = "版本号,用乐观锁")
    @Version
    private Integer version;
    /**
     * 状态 0=初始化状态 1=EXECUTED 2=CANCELLED
     */
    @ApiModelProperty(name = "状态 0=初始化状态 1=EXECUTED 2=CANCELLED")
    @TableField("status")
    private Integer status;
}

