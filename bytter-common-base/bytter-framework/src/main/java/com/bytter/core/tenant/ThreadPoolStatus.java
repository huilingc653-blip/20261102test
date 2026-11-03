package com.bytter.core.tenant;

import lombok.Builder;
import lombok.Data;

/**
 * 线程池状态。
 *
 * @author zhaohc
 * @since 2026/1/6 14:19
 */
@Data
@Builder
public class ThreadPoolStatus {
    /**
     * 线程池大小。
     */
    private int poolSize;
    /**
     * 线程活动数。
     */
    private int activeCount;
    /**
     * 线程核心数。
     */
    private int corePoolSize;
    /**
     * 线程最大数。
     */
    private int maxPoolSize;
    /**
     * 线程最大活动数。
     */
    private int largestPoolSize;
    /**
     * 线程队列大小。
     */
    private int queueSize;
    /**
     * 已完成任务数。
     */
    private long completedTaskCount;
    /**
     * 总任务数。
     */
    private long taskCount;
}
