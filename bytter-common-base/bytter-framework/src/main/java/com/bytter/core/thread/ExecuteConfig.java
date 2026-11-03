package com.bytter.core.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类。
 *
 * @author zhaohc
 */
@Configuration
@EnableAsync
public class ExecuteConfig implements AsyncConfigurer {

    private static final Logger LOG = LoggerFactory.getLogger(ExecuteConfig.class);

    /**
     * 核心线程数
     */
    @Value("${bytter.executor.thread.core_pool_size}")
    private int corePoolSize;
    /**
     * 最大线程数
     */
    @Value("${bytter.executor.thread.max_pool_size}")
    private int maxPoolSize;
    /**
     * 队列大小
     */
    @Value("${bytter.executor.thread.queue_capacity}")
    private int queueCapacity;
    /**
     * 线程名称前缀
     */
    @Value("${bytter.executor.thread.name_prefix}")
    private String namePrefix;
    /**
     * 获取当前机器的核数
     */
    public static final int CPU_NUM = Runtime.getRuntime().availableProcessors();

    @Bean(name = "asyncThreadPoolExecutor")
    public ThreadPoolTaskExecutor asyncThreadPoolExecutor() {
        LOG.info("asyncThreadPoolExecutor 开始执行，初始化异步线程池！");
        ThreadPoolTaskExecutor executor = new VisitableThreadPoolTaskExecutor();
        // 配置核心线程数
        executor.setCorePoolSize(CPU_NUM);
        // 配置最大线程数
        executor.setMaxPoolSize(CPU_NUM * 2);
        // 配置队列大小
        executor.setQueueCapacity(queueCapacity);
        // 配置线程名称前缀
        executor.setThreadNamePrefix(namePrefix);
        // 当pool达到线程最大数时，不在新线程中执行任务，而是由调用者所在线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        // 线程池关闭时等待所有任务都完成再继续销毁其他Bean
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 设置线程池中任务等待时间，若超时未销毁则强制销毁，防止应用阻塞
        executor.setAwaitTerminationSeconds(60);
        // 挂载 TTL 任务装饰器，跨线程传递租户/用户上下文
        executor.setTaskDecorator(new TtlTaskDecorator());
        // 执行初始化
        executor.initialize();
        return executor;
    }

    /**
     * 无 qualifier 的 {@code @Async} 默认使用受管异步线程池，避免退化为 SimpleAsyncTaskExecutor。
     */
    @Override
    public Executor getAsyncExecutor() {
        return asyncThreadPoolExecutor();
    }

}
