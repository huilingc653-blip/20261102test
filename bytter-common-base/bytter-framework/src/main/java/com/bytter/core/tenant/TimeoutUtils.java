package com.bytter.core.tenant;

import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.util.concurrent.*;

/**
 * 线程超时工具类。
 *
 * @author zhaohc
 * @since 2026/1/6 16:01
 */
@Slf4j
public class TimeoutUtils {

    /**
     * 线程超时调度器
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(
            Math.max(2, Runtime.getRuntime().availableProcessors() / 2),
            r -> {
                Thread thread = new Thread(r, "timeout-scheduler");
                thread.setDaemon(true);
                return thread;
            });

    /**
     * 为CompletableFuture添加超时处理（Java 8兼容版本）
     */
    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, long timeout, TimeUnit unit) {
        if (future.isDone()) {
            return future;
        }

        if (timeout <= 0) {
            return future;
        }

        final CompletableFuture<T> timeoutFuture = new CompletableFuture<>();

        // 记录任务开始时间
        final long startTime = System.currentTimeMillis();

        // 设置超时任务
        ScheduledFuture<?> timeoutTask = scheduler.schedule(() -> {
            long elapsed = System.currentTimeMillis() - startTime;

            if (!future.isDone() && !timeoutFuture.isDone()) {
                if (log.isDebugEnabled()) {
                    log.debug("任务超时触发，已执行时间: {}ms, 超时时间: {}ms",
                            elapsed, unit.toMillis(timeout));
                }

                timeoutFuture.completeExceptionally(
                        new TimeoutException(String.format(
                                "任务执行超时: 已执行%sms (超时时间: %s%s)",
                                elapsed, timeout, unit))
                );

                // 如果需要中断，尝试中断执行线程
                if (!future.isDone()) {
                    future.cancel(true);
                }
            }
        }, timeout, unit);

        // 原始任务完成时，取消超时任务
        future.whenComplete((result, throwable) -> {
            long elapsed = System.currentTimeMillis() - startTime;
            timeoutTask.cancel(true);

            if (log.isDebugEnabled()) {
                log.debug("任务正常完成，执行时间: {}ms", elapsed);
            }

            if (throwable != null) {
                timeoutFuture.completeExceptionally(throwable);
            } else {
                timeoutFuture.complete(result);
            }
        });

        return timeoutFuture;
    }

    /**
     * 为CompletableFuture添加默认超时值
     */
    public static <T> CompletableFuture<T> orTimeout(CompletableFuture<T> future, long timeoutMillis) {
        return orTimeout(future, timeoutMillis, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭调度器（应用关闭时调用）
     */
    public static void shutdown() {
        scheduler.shutdown();
    }
}
