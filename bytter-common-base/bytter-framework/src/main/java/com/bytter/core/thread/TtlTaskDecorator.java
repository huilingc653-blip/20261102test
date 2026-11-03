package com.bytter.core.thread;

import com.alibaba.ttl.TtlRunnable;
import org.springframework.core.task.TaskDecorator;

/**
 * 线程池任务装饰器：任务提交前用 TTL 包装，跨线程传递租户/用户上下文，
 * 解决线程复用导致的租户上下文丢失与串扰（Rule：TENANT-CONTEXT-PROPAGATION）。
 *
 * @author zhaohc
 */
public class TtlTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        return TtlRunnable.get(runnable);
    }
}
