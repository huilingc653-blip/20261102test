package com.bytter.core.tenant;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Map;

/**
 * 租户任务上下文。
 *
 * @author zhaohc
 * @since 2026/1/5 19:29
 */
@Data
@Builder
public class TaskExecutionContext {
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 分片索引
     */
    private int shardIndex;
    /**
     * 分片总数
     */
    private int shardTotal;
    /**
     * 触发时间
     */
    private Date triggerTime;
    /**
     * 任务参数
     */
    private Map<String, Object> parameters;

    /**
     * 获取任务参数。
     *
     * @param key   参数键。
     * @param type  参数类型。
     * @param <T>   参数类型。
     * @return 参数值。
     */
    public <T> T getParameter(String key, Class<T> type) {
        Object value = parameters != null ? parameters.get(key) : null;
        return type.isInstance(value) ? type.cast(value) : null;
    }
}
