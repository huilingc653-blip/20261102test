package com.bytter.core.tenant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 租户任务执行结果。
 *
 * @author zhaohc
 * @since 2026/1/5 19:38
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskExecutionResult {
    /**
     * 租户ID
     */
    private String tenantId;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 是否成功
     */
    private boolean success;
    /**
     * 结果消息
     */
    private String message;
    /**
     * 异常信息
     */
    private Throwable error;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 任务耗时
     */
    private long costTime;

    /**
     * 创建成功结果。
     *
     * @param tenantId 租户ID。
     * @return 成功结果。
     */
    public static TaskExecutionResult success(String tenantId, String taskName, Date startTime, Date endTime, long costTime) {
        return TaskExecutionResult.builder()
                .success(true)
                .tenantId(tenantId)
                .taskName(taskName)
                .startTime(startTime)
                .endTime(endTime)
                .costTime(costTime)
                .message("执行成功")
                .build();
    }

    /**
     * 创建失败结果。
     *
     * @param tenantId 租户ID。
     * @param taskName 任务名称。
     * @param message  结果消息。
     * @param error    异常信息。
     * @return 失败结果。
     */
    public static TaskExecutionResult failure(String tenantId, String taskName, String message, Throwable error) {
        return TaskExecutionResult.builder()
                .success(false)
                .tenantId(tenantId)
                .taskName(taskName)
                .message(message)
                .error(error)
                .build();
    }
}
