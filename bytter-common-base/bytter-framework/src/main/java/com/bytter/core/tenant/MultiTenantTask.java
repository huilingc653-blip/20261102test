package com.bytter.core.tenant;

import lombok.extern.slf4j.Slf4j;

/**
 * 多租户任务接口。
 *
 * @author zhaohc
 * @since 2026/1/6 14:10
 */
public interface MultiTenantTask {
    /**
     * 获取任务名称（唯一标识）
     */
    String getTaskName();

    /**
     * 执行单个租户的任务
     * @param tenantId 租户ID
     * @param context 任务上下文
     */
    void executeForTenant(String tenantId, TaskExecutionContext context);

    /**
     * 获取分片策略
     */
    default String getShardingStrategy() {
        return "hash";
    }

    /**
     * 获取最大并发数（每个任务的租户并发数）
     */
    default int getMaxConcurrent() {
        return 10;
    }

    /**
     * 获取重试次数
     */
    default int getRetryCount() {
        return 3;
    }

    /**
     * 获取任务预估执行时间（毫秒），用于智能调度，默认30秒。
     */
    default long getEstimatedExecutionTime() {
        return 30000L;
    }

    /**
     * 获取任务优先级（0-10，越大优先级越高）
     */
    default int getPriority() {
        return 5;
    }

    /**
     * 获取任务超时时间（毫秒），默认5分钟。
     */
    default long getTimeout() {
        return 300000L;
    }

    /**
     * 异常处理策略
     */
    default void handleException(String tenantId, Exception e) {
        // 默认记录日志
        System.err.println("任务[" + getTaskName() + "]租户[" + tenantId + "]执行异常: " + e.getMessage());
    }
}
