package com.bytter.core.tenant;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.context.XxlJobContext;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * XXL-Job多租户切面。
 *
 * @author zhaohc
 * @since 2026/1/8 16:14
 */
@Slf4j
@Aspect
@Component
public class MultiTenantJobAspect {

    @Resource
    private TenantShardingExecutor tenantShardingExecutor;

    @Resource
    private TenantTaskProperties tenantTask;

    // 缓存任务处理器映射
    private final Map<String, MultiTenantTask> taskCache = new ConcurrentHashMap<>();

    /**
     * 切入所有被@XxlJob注解的方法
     */
    @Around("@annotation(xxlJob)")
    public Object aroundXxlJob(ProceedingJoinPoint joinPoint, XxlJob xxlJob) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 1. 检查是否有@MultiTenantJob注解
        MultiTenantJob multiTenantJob = method.getAnnotation(MultiTenantJob.class);
        if (multiTenantJob != null && !multiTenantJob.enabled()) {
            log.info("任务[{}]已禁用多租户改造，执行原方法", xxlJob.value());
            // 如果不是多租户任务，执行原方法
            return joinPoint.proceed();
        }
        // 获取任务名称
        String taskName = multiTenantJob != null ? multiTenantJob.value() : xxlJob.value();
        // 获取原方法参数
        Object[] originalArgs = joinPoint.getArgs();
        // 2. 获取或创建任务处理器
        MultiTenantTask task = getOrCreateTask(taskName, joinPoint, method, multiTenantJob, xxlJob, originalArgs);

        // 3. 执行多租户任务
        return executeMultiTenantJob(taskName, task);
    }

    /**
     * 获取或创建任务处理器
     */
    private MultiTenantTask getOrCreateTask(String taskName, ProceedingJoinPoint joinPoint, Method method, MultiTenantJob annotation, XxlJob xxlJob, Object[] originalArgs) {

        return taskCache.computeIfAbsent(taskName, k -> {
            Object targetBean = joinPoint.getTarget();
            if (annotation == null) {
                // 如果未使用注解，则默认以XxlJob注解创建适配器
                return new XxlAdapterMultiTenantTask(targetBean, method, xxlJob, tenantTask.getSharding(), originalArgs);
            }

            // 创建适配器，将原方法包装为MultiTenantTask
            return new MethodAdapterMultiTenantTask(targetBean, method, annotation, originalArgs);
        });
    }

    /**
     * 执行多租户任务
     */
    private Object executeMultiTenantJob(String taskName, MultiTenantTask task) {
        Long jobId = resolveCurrentJobId();
        try {
            log.info("开始执行多租户任务: {}, jobId={}", taskName, jobId);

            // 迁移期开关未启用时执行范围=全部有效租户；启用后按当前触发任务 jobId 精确取已分配租户并执行
            TenantTaskDispatchResult dispatch = tenantShardingExecutor.executeTaskForAllTenants(jobId, task);

            // 转换为XXL-JOB返回格式
            return convertToXxlJobResult(dispatch);
        } catch (Exception e) {
            log.error("执行多租户任务[{}]失败", taskName, e);
            XxlJobHelper.handleFail("任务执行失败: " + e.getMessage());
            return ReturnT.FAIL;
        }
    }

    /**
     * 解析当前触发任务的 XXL jobId；非 XXL 线程触发（无上下文）时返回 null。
     */
    private Long resolveCurrentJobId() {
        XxlJobContext xxlJobContext = XxlJobContext.getXxlJobContext();
        return xxlJobContext == null ? null : xxlJobContext.getJobId();
    }

    /**
     * 将分发结果转换为XXL-JOB格式。
     * <p>
     * 未执行（跳过/fail-closed）按原因码留痕；任一已分配租户失败必须使 XXL 明确失败（XXL-003/任务 C），
     * 禁止静默成功与吞异常。
     */
    private ReturnT<String> convertToXxlJobResult(TenantTaskDispatchResult dispatch) {
        String taskName = dispatch.getTaskName();

        // 未执行：无分配租户=跳过留痕；映射不可达/查询异常=fail-closed（原因码可区分，P2-03）
        if (dispatch.isSkipped()) {
            TenantTaskSkipReason reason = dispatch.getSkipReason();
            String skipMsg = String.format("任务[%s]未执行：jobId=%s，原因=%s(%s)%s",
                    taskName, dispatch.getJobId(), reason.name(), reason.getDescription(),
                    dispatch.getMessage() == null ? "" : "，" + dispatch.getMessage());
            if (reason == TenantTaskSkipReason.NO_ASSIGNED_TENANT) {
                // 未分配租户是定义内的跳过语义（D2）：显式返回跳过说明，不做静默成功
                log.warn("{}", skipMsg);
                return new ReturnT<>(ReturnT.SUCCESS_CODE, skipMsg);
            }
            // 映射数据源不可达/查询异常：fail-closed，本次不执行并上报失败（告警可见）
            log.error("{}", skipMsg);
            XxlJobHelper.handleFail(skipMsg);
            return new ReturnT<>(ReturnT.FAIL_CODE, skipMsg);
        }

        List<TaskExecutionResult> results = dispatch.getResults();
        if (results == null) {
            // executed() 工厂不产生 null 明细，防御性保留
            String errMsg = String.format("任务[%s]执行状态异常：已执行但无结果明细", taskName);
            log.error("{}", errMsg);
            XxlJobHelper.handleFail(errMsg);
            return new ReturnT<>(ReturnT.FAIL_CODE, errMsg);
        }

        long successCount = results.stream().filter(TaskExecutionResult::isSuccess).count();
        long failCount = results.size() - successCount;

        if (results.isEmpty()) {
            // 仅迁移期模式（系统无有效租户 tenant_status=2）会出现“已执行、0 租户”：
            // 按正常完成返回并在文案中明确“处理0个租户”，不按“状态异常”误报 FAIL（评审 P2-1）
            String emptyMsg = String.format("任务[%s]执行完成，当前无有效租户可处理（tenant_status=2 租户为空），处理0个租户", taskName);
            log.warn("{}", emptyMsg);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, emptyMsg);
        }

        if (failCount == 0) {
            String message = String.format("任务[%s]执行成功，共处理%d个租户", taskName, successCount);
            log.info(message);
            return new ReturnT<>(ReturnT.SUCCESS_CODE, message);
        } else {
            String message = String.format("任务[%s]执行完成，成功%d个，失败%d个", taskName, successCount, failCount);

            // 收集失败详情
            String failDetails = results.stream()
                    .filter(r -> !r.isSuccess())
                    .map(r -> String.format("租户%s: %s", r.getTenantId(), r.getMessage()))
                    .limit(10) // 限制详情数量
                    .collect(Collectors.joining("; "));

            if (failCount > 10) {
                failDetails += String.format("...等%d个失败", failCount);
            }

            log.warn("{}, 失败详情: {}", message, failDetails);
            return new ReturnT<>(ReturnT.FAIL_CODE, message + "。失败详情: " + failDetails);
        }
    }

    /**
     * XXL注解适配器 - 将原方法包装为MultiTenantTask
     */
    private static class XxlAdapterMultiTenantTask implements MultiTenantTask {
        private final Object targetBean;
        private final Method method;
        private final XxlJob xxlJob;
        private final TenantTaskProperties.Sharding sharding;
        private final Object[] originalArgs;

        private XxlAdapterMultiTenantTask(Object targetBean, Method method, XxlJob xxlJob, TenantTaskProperties.Sharding sharding, Object[] originalArgs) {
            this.targetBean = targetBean;
            this.method = method;
            this.xxlJob = xxlJob;
            this.sharding = sharding;
            this.originalArgs = originalArgs;
        }

        @Override
        public String getTaskName() {
            return xxlJob.value();
        }

        @Override
        public void executeForTenant(String tenantId, TaskExecutionContext context) {
            executeOriginalMethod(targetBean, method, originalArgs, getTaskName());
        }

        @Override
        public String getShardingStrategy() {
            return sharding.getStrategy();
        }

        @Override
        public int getMaxConcurrent() {
            return sharding.getMaxConcurrent();
        }

        @Override
        public int getRetryCount() {
            return sharding.getRetryCount();
        }

        @Override
        public long getTimeout() {
            return sharding.getTaskTimeoutMinutes() * 60 * 1000L;
        }
    }

    /**
     * 多租户注解适配器 - 将原方法包装为MultiTenantTask
     */
    private static class MethodAdapterMultiTenantTask implements MultiTenantTask {
        private final Object targetBean;
        private final Method method;
        private final MultiTenantJob annotation;
        private final Object[] originalArgs;

        public MethodAdapterMultiTenantTask(Object targetBean, Method method, MultiTenantJob annotation, Object[] originalArgs) {
            this.targetBean = targetBean;
            this.method = method;
            this.annotation = annotation;
            this.originalArgs = originalArgs;
        }

        @Override
        public String getTaskName() {
            return annotation.value();
        }

        @Override
        public void executeForTenant(String tenantId, TaskExecutionContext context) {
            executeOriginalMethod(targetBean, method, originalArgs, getTaskName());
        }

        @Override
        public String getShardingStrategy() {
            return annotation.shardingStrategy();
        }

        @Override
        public int getMaxConcurrent() {
            return annotation.maxConcurrent();
        }

        @Override
        public int getRetryCount() {
            return annotation.retryCount();
        }

        @Override
        public long getEstimatedExecutionTime() {
            // 从注解属性获取，如果没有则使用默认

            return annotation.estimatedExecutionTime();
        }

        @Override
        public int getPriority() {
            return annotation.priority();
        }

        @Override
        public long getTimeout() {
            return annotation.timeout();
        }

        @Override
        public void handleException(String tenantId, Exception e) {
            log.error("任务[{}]租户[{}]执行异常", getTaskName(), tenantId, e);
        }
    }

    /**
     * 执行原始方法
     */
    private static void executeOriginalMethod(Object targetBean, Method method, Object[] originalArgs, String taskName) {
        try {
            // 获取方法参数类型
            Class<?>[] parameterTypes = method.getParameterTypes();
            // 根据参数类型决定如何调用
            if (parameterTypes.length == 0) {
                // 无参方法
                method.invoke(targetBean);
            } else if (originalArgs != null && originalArgs.length == parameterTypes.length) {
                // 使用原始参数
                method.invoke(targetBean, originalArgs);
            } else {
                // 参数不匹配，尝试无参调用（兼容模式）
                log.warn("任务[{}]参数不匹配，尝试无参调用。期望参数: {}, 实际参数: {}",
                        taskName, parameterTypes.length,
                        originalArgs != null ? originalArgs.length : 0);
                method.invoke(targetBean);
            }
        } catch (Exception e) {
            throw new RuntimeException("执行租户任务失败: " + e.getMessage(), e);
        }
    }

}
