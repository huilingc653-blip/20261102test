package com.bytter.core.tenant;

import lombok.Getter;

/**
 * 多租户任务本次触发未执行的原因码。
 * <p>
 * 用于三类非执行情况的可区分留痕（P2-03）：未分配租户（跳过）、任务租户映射数据源不可达（fail-closed）、
 * 映射查询异常（fail-closed），禁止静默成功。
 *
 * @author zhaohc
 * @since 2026-09-07
 */
@Getter
public enum TenantTaskSkipReason {

    /**
     * 任务在 SYS_TENANT_SCHEDULED_TASK 中未分配任何租户：按 D2 语义不执行，记录跳过观测日志供审计。
     */
    NO_ASSIGNED_TENANT("任务未分配租户，跳过执行"),

    /**
     * 执行线程无 XXL 任务上下文，无法取得 jobId：按精确匹配要求不执行（fail-closed）。
     */
    JOB_ID_MISSING("无法获取XXL任务ID"),

    /**
     * 任务-租户映射数据源或提供方不可达（D4）：本次触发不执行并告警，不得回退全量活跃租户。
     */
    MAPPING_UNAVAILABLE("任务租户映射数据源不可达"),

    /**
     * 任务-租户映射查询发生异常（D4）：本次触发不执行并告警，不得回退全量活跃租户。
     */
    MAPPING_QUERY_ERROR("任务租户映射查询异常");

    private final String description;

    TenantTaskSkipReason(String description) {
        this.description = description;
    }

}
