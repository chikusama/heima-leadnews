package com.heima.task;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 1.DelayTask(实现Delayed接口)： 延时任务，进入队列中元素可以设置有效期，有效期到期可以出队列
 * 2.DelayQueue：延时队列,存放演示任务数据结构
 */
public class DelayTask implements Delayed {
    /**
     * 任务id
     */
    private Integer taskId;
    /**
     * 任务超时
     */
    private Long executeTime;


    public DelayTask(Integer taskId, Long executeTime) {
        this.taskId = taskId;
        this.executeTime = executeTime;
    }


    /**
     * 获取在队列中元素剩余延迟时间
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return executeTime - System.currentTimeMillis();
    }

    /**
     * 作用：按照到期时间进行排序，确定元素出队列的顺序
     * 0：队列中的两个元素剩余过期时间相同
     * -1：队列中前一个元素早于后一个元素过期
     * 1：队列中前一个元素晚于后一个元素过期
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        DelayTask t = (DelayTask) o;
        if (this.executeTime - t.executeTime < 0) {
            return -1;
        } else if ((this.executeTime - t.executeTime) > 0) {
            return 1;
        } else {
            return 0;
        }
    }


    @Override
    public String toString() {
        return "DelayedTask{" +
                "taskId=" + taskId +
                ", executeTime=" + executeTime +
                '}';
    }
}
