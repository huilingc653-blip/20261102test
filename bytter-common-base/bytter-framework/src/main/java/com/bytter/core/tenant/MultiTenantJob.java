package com.bytter.core.tenant;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 多租户任务注解。
 *
 * @author zhaohc
 * @since 2026/1/8 16:11
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MultiTenantJob {
    /**
     * 任务名称（唯一标识）
     */
    String value();

    /**
     * 是否启用多租户执行
     */
    boolean enabled() default true;

    /**
     * 分片策略
     */
    String shardingStrategy() default "hash";

    /**
     * 最大并发数（每个任务的租户并发数）
     */
    int maxConcurrent() default 10;

    /**
     * 重试次数
     */
    int retryCount() default 3;

    /**
     * 批次大小
     */
    int batchSize() default 100;

    /**
     * 任务预估执行时间（毫秒）
     */
    long estimatedExecutionTime() default 30000L;

    /**
     * 任务优先级（0-10，越大优先级越高）
     */
    int priority() default 5;

    /**
     * 任务超时时间（毫秒）
     */
    long timeout() default 300000L;

}
