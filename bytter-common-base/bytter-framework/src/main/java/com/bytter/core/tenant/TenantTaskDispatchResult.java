package com.bytter.core.tenant;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * 多租户任务单次触发分发结果。
 * <p>
 * 区分两类结果：已执行（{@link #isExecuted()}）进入逐租户执行阶段并携带租户结果明细；
 * 未执行（{@link #isSkipped()}）携带原因码（未分配租户/映射不可达/查询异常等），用于 XXL 结果转换与留痕。
 *
 * @author zhaohc
 * @since 2026-09-07
 */
@Getter
public class TenantTaskDispatchResult {

    /**
     * XXL 任务ID
     */
    private final Long jobId;
    /**
     * 任务名称
     */
    private final String taskName;
    /**
     * 未执行原因；为 null 表示已进入租户执行阶段
     */
    private final TenantTaskSkipReason skipReason;
    /**
     * 补充说明（失败原因摘要等）
     */
    private final String message;
    /**
     * 已执行租户的结果明细；未执行阶段时为空
     */
    private final List<TaskExecutionResult> results;

    private TenantTaskDispatchResult(Long jobId, String taskName, TenantTaskSkipReason skipReason,
                                     String message, List<TaskExecutionResult> results) {
        this.jobId = jobId;
        this.taskName = taskName;
        this.skipReason = skipReason;
        this.message = message;
        this.results = results;
    }

    /**
     * 已执行结果。
     *
     * @param jobId   XXL 任务ID。
     * @param taskName 任务名称。
     * @param results 逐租户执行结果明细。
     * @return 分发结果。
     */
    public static TenantTaskDispatchResult executed(Long jobId, String taskName, List<TaskExecutionResult> results) {
        return new TenantTaskDispatchResult(jobId, taskName, null, null,
                results == null ? Collections.emptyList() : results);
    }

    /**
     * 未执行结果（跳过/fail-closed）。
     *
     * @param jobId      XXL 任务ID。
     * @param taskName   任务名称。
     * @param skipReason 未执行原因码。
     * @param message    补充说明。
     * @return 分发结果。
     */
    public static TenantTaskDispatchResult skipped(Long jobId, String taskName,
                                                   TenantTaskSkipReason skipReason, String message) {
        return new TenantTaskDispatchResult(jobId, taskName, skipReason, message, Collections.emptyList());
    }

    /**
     * 本次触发是否已进入租户执行阶段。
     */
    public boolean isExecuted() {
        return skipReason == null;
    }

    /**
     * 本次触发是否未执行（跳过/fail-closed）。
     */
    public boolean isSkipped() {
        return skipReason != null;
    }
}
