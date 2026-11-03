package com.bytter.core.tenant;

import com.alibaba.ttl.threadpool.TtlExecutors;
import com.bytter.core.common.context.UserContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 智能线程池管理器
 * 特点：
 * 1. 使用TTL包装所有线程池，解决线程污染
 * 2. 分级线程池：快、中、慢任务分离
 * 3. 动态调整线程池大小
 * 4. 优雅关闭
 *
 * @author zhaohc
 * @since 2026/1/6 14:17
 */
@Slf4j
@Component
public class SmartThreadPoolManager {

    // ========== 线程池定义 ==========

    /**
     * 快速任务池：处理短任务（<10秒），原始线程池（用于监控和管理）
     */
    private ThreadPoolExecutor fastPoolRaw;
    /**
     * 快速任务池：处理短任务（<10秒），TTL包装后的线程池（用于任务执行）
     */
    private ExecutorService fastPool;

    /**
     * 普通任务池：处理中等任务（10秒-2分钟），原始线程池（用于监控和管理）
     */
    private ThreadPoolExecutor normalPoolRaw;
    /**
     * 普通任务池：处理中等任务（10秒-2分钟），TTL包装后的线程池（用于任务执行）
     */
    private ExecutorService normalPool;

    /**
     * 慢速任务池：处理长任务（>2分钟），原始线程池（用于监控和管理）
     */
    private ThreadPoolExecutor slowPoolRaw;
    /**
     * 慢速任务池：处理长任务（>2分钟），TTL包装后的线程池（用于任务执行）
     */
    private ExecutorService slowPool;

    // ========== 配置参数 ==========

    @Resource
    private TenantTaskProperties tenantTask;

    // ========== 初始化与销毁 ==========

    @PostConstruct
    public void init() {
        log.info("初始化智能线程池管理器，多租户任务配置参数：{}", tenantTask);

        // 1. 创建快速任务池
        TenantTaskProperties.ThreadPool fast = tenantTask.getFast();
        fastPoolRaw = createRawThreadPool("fast-pool", fast.getCoreSize(), fast.getMaxSize(),
                new SynchronousQueue<>(), new FastRejectPolicy());
        fastPool = TtlExecutors.getTtlExecutorService(fastPoolRaw);

        // 2. 创建普通任务池
        TenantTaskProperties.ThreadPool normal = tenantTask.getNormal();
        normalPoolRaw = createRawThreadPool("normal-pool", normal.getCoreSize(), normal.getMaxSize(),
                new LinkedBlockingQueue<>(normal.getQueueCapacity()),
                new ThreadPoolExecutor.CallerRunsPolicy());
        normalPool = TtlExecutors.getTtlExecutorService(normalPoolRaw);

        // 3. 创建慢速任务池
        TenantTaskProperties.ThreadPool slow = tenantTask.getSlow();
        slowPoolRaw = createRawThreadPool("slow-pool", slow.getCoreSize(), slow.getMaxSize(),
                new LinkedBlockingQueue<>(slow.getQueueCapacity()), new SlowRejectPolicy());
        slowPool = TtlExecutors.getTtlExecutorService(slowPoolRaw);

        // 4. 预热核心线程
        preStartCoreThreads();

        log.info("智能线程池管理器初始化完成");
        logPoolStatus();
    }

    @PreDestroy
    public void destroy() {
        log.info("关闭智能线程池管理器...");

        shutdownPool("fast-pool", fastPool, fastPoolRaw);
        shutdownPool("normal-pool", normalPool, normalPoolRaw);
        shutdownPool("slow-pool", slowPool, slowPoolRaw);

        log.info("智能线程池管理器关闭完成");
    }

    // ========== 线程池创建 ==========

    /**
     * 创建原始线程池（未包装）
     */
    private ThreadPoolExecutor createRawThreadPool(String poolName, int coreSize, int maxSize, BlockingQueue<Runnable> queue,
                                                   RejectedExecutionHandler handler) {

        ThreadFactory threadFactory = new NamedThreadFactory(poolName);

        return new ThreadPoolExecutor(
                coreSize,
                maxSize,
                60L, TimeUnit.SECONDS,
                queue,
                threadFactory,
                handler);
    }

    /**
     * 预热核心线程
     */
    private void preStartCoreThreads() {
        fastPoolRaw.prestartAllCoreThreads();
        normalPoolRaw.prestartAllCoreThreads();
        slowPoolRaw.prestartAllCoreThreads();

        log.info("线程池预热完成");
    }

    // ========== 统一收编 ==========

    /**
     * 将任意 ExecutorService 统一收编为 TTL 包装的上下文感知执行器。
     * <p>
     * 供业务侧自建线程池（裸线程/固定池/具名池）统一收口，避免租户上下文在线程复用中丢失或串扰。
     * 内部已由 {@link TtlExecutors#getTtlExecutorService(ExecutorService)} 保证关闭包装器时同步关闭原线程池。
     * </p>
     *
     * @param executor 原始线程池。
     * @return TTL 包装后的上下文感知执行器。
     */
    public static ExecutorService wrap(ExecutorService executor) {
        return TtlExecutors.getTtlExecutorService(executor);
    }

    // ========== 任务提交API ==========

    /**
     * 提交任务（智能选择线程池）
     */
    public <T> CompletableFuture<T> submitTask(Callable<T> task, long estimatedTime, int priority) {
        ExecutorService executor = selectExecutor(estimatedTime, priority);

        // 注意：由于线程池已经用TTL包装，任务不需要再次包装
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
    }

    /**
     * 提交Runnable任务
     */
    public CompletableFuture<Void> submitTask(Runnable task, long estimatedTime, int priority) {
        ExecutorService executor = selectExecutor(estimatedTime, priority);

        // 注意：由于线程池已经用TTL包装，任务不需要再次包装
        return CompletableFuture.runAsync(task, executor);
    }

    /**
     * 智能选择线程池
     */
    public ExecutorService selectExecutor(long estimatedTime, int priority) {
        // 高优先级任务使用优先级池
        if (priority >= 8) {
            log.debug("高优先级任务[{}]使用快速线程池", priority);
            return fastPool;
        }

        // 根据预估时间选择
        if (estimatedTime < 10000) { // <10秒
            return fastPool;
        } else if (estimatedTime < 120000) { // <2分钟
            return normalPool;
        } else { // >=2分钟
            return slowPool;
        }
    }

    /**
     * 为租户提交任务（自动设置上下文）
     */
    public <T> CompletableFuture<T> submitForTenant(String tenantId, Callable<T> task, long estimatedTime, int priority) {

        return submitTask(() -> {
            // 设置租户上下文
            UserContextHelper.helper().setTTLTenant(tenantId);
            try {
                return task.call();
            } finally {
                // 恢复原始上下文（如果有）
                UserContextHelper.helper().clearTTLTenant();
            }
        }, estimatedTime, priority);
    }

    /**
     * 批量为租户提交任务
     */
    public <T> Map<String, CompletableFuture<T>> submitForTenants(Map<String, Callable<T>> tenantTasks, long estimatedTime, int priority) {

        Map<String, CompletableFuture<T>> futures = new ConcurrentHashMap<>();
        ExecutorService executor = selectExecutor(estimatedTime, priority);

        tenantTasks.forEach((tenantId, task) -> {
            CompletableFuture<T> future = CompletableFuture.supplyAsync(() -> {
                // 设置租户上下文
                UserContextHelper.helper().setTTLTenant(tenantId);
                try {
                    return task.call();
                } catch (Exception e) {
                    log.error("任务执行异常", e);
                    throw new RuntimeException(e);
                } finally {
                    // 清理租户上下文
                    UserContextHelper.helper().clearTTLTenant();
                }
            }, executor);

            futures.put(tenantId, future);
        });

        return futures;
    }

    // ========== 线程池监控 ==========

    /**
     * 获取线程池状态
     */
    public Map<String, ThreadPoolStatus> getThreadPoolStatus() {
        Map<String, ThreadPoolStatus> status = new LinkedHashMap<>();

        status.put("fast-pool", getPoolStatus(fastPoolRaw));
        status.put("normal-pool", getPoolStatus(normalPoolRaw));
        status.put("slow-pool", getPoolStatus(slowPoolRaw));

        return status;
    }

    /**
     * 获取线程池状态。
     *
     * @param pool 线程池。
     * @return 线程池状态。
     */
    private ThreadPoolStatus getPoolStatus(ThreadPoolExecutor pool) {
        if (pool == null) {
            return null;
        }

        return ThreadPoolStatus.builder()
                .poolSize(pool.getPoolSize())
                .activeCount(pool.getActiveCount())
                .corePoolSize(pool.getCorePoolSize())
                .maxPoolSize(pool.getMaximumPoolSize())
                .largestPoolSize(pool.getLargestPoolSize())
                .queueSize(pool.getQueue().size())
                .completedTaskCount(pool.getCompletedTaskCount())
                .taskCount(pool.getTaskCount())
                .build();
    }

    /**
     * 记录线程池状态，每分钟记录一次
     */
    // @Scheduled(fixedRate = 60000)
    public void logPoolStatus() {
        if (!log.isInfoEnabled()) {
            return;
        }

        Map<String, ThreadPoolStatus> status = getThreadPoolStatus();

        status.forEach((poolName, poolStatus) -> {
            if (poolStatus != null) {
                double usageRate = poolStatus.getQueueSize() > 0 ?
                        (double) poolStatus.getActiveCount() / poolStatus.getMaxPoolSize() : 0;

                if (usageRate > 0.8) {
                    log.warn("线程池[{}]使用率过高: {}/{}, 队列: {}, 已完成: {}",
                            poolName, poolStatus.getActiveCount(), poolStatus.getMaxPoolSize(),
                            poolStatus.getQueueSize(), poolStatus.getCompletedTaskCount());
                } else {
                    log.info("线程池[{}]状态: 活跃{}/{}, 队列: {}, 已完成: {}",
                            poolName, poolStatus.getActiveCount(), poolStatus.getMaxPoolSize(),
                            poolStatus.getQueueSize(), poolStatus.getCompletedTaskCount());
                }
            }
        });
    }

    // ========== 辅助方法 ==========

    /**
     * 关闭线程池（关闭TTL包装器会自动关闭原始线程池）。
     *
     * @param poolName 线程池名称。
     * @param ttlPool  TTL包装的线程池。
     * @param rawPool  原始线程池。
     */
    private void shutdownPool(String poolName, ExecutorService ttlPool, ThreadPoolExecutor rawPool) {
        if (ttlPool == null || ttlPool.isShutdown()) {
            return;
        }

        log.info("关闭线程池: {}", poolName);
        ttlPool.shutdown();

        try {
            if (!ttlPool.awaitTermination(30, TimeUnit.SECONDS)) {
                ttlPool.shutdownNow();
                log.warn("线程池[{}]强制关闭", poolName);
            }
        } catch (InterruptedException e) {
            ttlPool.shutdownNow();
            Thread.currentThread().interrupt();
            log.error("关闭线程池[{}]被中断", poolName, e);
        }
    }

    /**
     * 获取原始快速线程池（用于特殊场景）
     */
    public ThreadPoolExecutor getRawFastPool() {
        return fastPoolRaw;
    }

    /**
     * 获取原始普通线程池（用于特殊场景）
     */
    public ThreadPoolExecutor getRawNormalPool() {
        return normalPoolRaw;
    }

    /**
     * 获取原始慢速线程池（用于特殊场景）
     */
    public ThreadPoolExecutor getRawSlowPool() {
        return slowPoolRaw;
    }

    // ========== 内部类 ==========

    /**
     * 快速任务拒绝策略（快速失败）
     */
    private static class FastRejectPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("快速任务被拒绝，队列已满");
            throw new RejectedExecutionException("快速任务队列已满");
        }
    }

    /**
     * 慢速任务拒绝策略（延迟重试）
     */
    private static class SlowRejectPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            log.warn("慢速任务被拒绝，尝试延迟重试");

            // 延迟后重试
            try {
                Thread.sleep(1000); // 延迟1秒
                if (!executor.isShutdown()) {
                    executor.execute(r);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RejectedExecutionException("任务被中断", e);
            }
        }
    }

    /**
     * 命名的线程工厂
     */
    private static class NamedThreadFactory implements ThreadFactory {
        private final AtomicInteger counter = new AtomicInteger(1);
        private final String namePrefix;

        public NamedThreadFactory(String poolName) {
            this.namePrefix = poolName + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, namePrefix + counter.getAndIncrement());
            thread.setDaemon(false);
            thread.setPriority(Thread.NORM_PRIORITY);

            // 设置未捕获异常处理器
            thread.setUncaughtExceptionHandler((t, e) -> {
                log.error("线程[{}]执行异常: {}", t.getName(), e.getMessage(), e);
            });

            return thread;
        }
    }
}
