package com.bytter.core.tenant;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 多租户任务配置。
 *
 * @author zhaohc
 * @since 2026/1/7 17:11
 */
@Data
@Component
@ConfigurationProperties(prefix = "bytter.tenant.task")
public class TenantTaskProperties {
    /**
     * 快速任务池
     */
    private ThreadPool fast = new ThreadPool();
    /**
     * 普通任务池
     */
    private ThreadPool normal = new ThreadPool();
    /**
     * 慢速任务池
     */
    private ThreadPool slow = new ThreadPool();
    /**
     * 分片配置（自 2026-09-07 起执行范围固定为单实例全量执行，字段随任务 E2 清理）
     */
    private Sharding sharding = new Sharding();
    /**
     * 租户列表查询结果缓存 TTL（秒）。
     * <p>短 TTL 缓存，默认 60 秒，同时服务于两类查询：
     * ① 任务-租户映射（SYS_TENANT_SCHEDULED_TASK，开关开启后按 jobId 查询已分配租户）；
     * ② 全部有效租户（tenant_status=2，迁移期模式，跨任务共享同一份全集）。
     * 用于降低同一时段内多次任务触发对租户数据源的重复直查压力。
     */
    private int mappingCacheSeconds = 60;
    /**
     * 是否启用「按内管端已分配租户执行」（迁移期开关，D3 v3 决策）。
     * <p>默认 {@code false}（迁移期）：执行范围为全部有效租户（tenant_status=2），与上线前行为一致，
     * 用于分配数据核对、灰度观察与一键回退；置为 {@code true} 后：执行范围改为
     * SYS_TENANT_SCHEDULED_TASK 中该 jobId 的已分配租户（D2/D3/D4 目标语义）——未分配即跳过留痕、
     * 映射数据源不可达/查询异常即 fail-closed。各服务经配置中心独立控制该值，可按服务分批灰度切换。
     */
    private boolean assignedTenantEnabled = false;

    /**
     * 线程池配置
     */
    @Data
    public static class ThreadPool {
        /**
         * 核心线程数
         */
        private int coreSize = 10;
        /**
         * 最大线程数
         */
        private int maxSize = 20;
        /**
         * 队列容量
         */
        private int queueCapacity = 100;
    }

    /**
     * 分片配置
     */
    @Data
    public static class Sharding {
        /**
         * 分片策略(hash-哈希, roundrobin-轮询, range-范围)
         */
        private String strategy = "hash";
        /**
         * 每个租户最大并发数
         */
        private int maxConcurrent = 5;
        /**
         * 任务超时（分钟）
         */
        private int taskTimeoutMinutes = 5;
        /**
         * 任务重试次数
         */
        private int retryCount = 3;
    }
}
