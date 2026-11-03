package com.bytter.core.tenant;

import com.bytter.core.common.context.UserContextHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransientConnectionException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用多租户任务执行器 - 核心工具类。
 * <p>
 * 用法：
 * 1. 全局切面 {@link MultiTenantJobAspect} 自动切入所有 @XxlJob 方法。
 * 2. 执行范围由迁移期开关决定（D3 v3 决策）：
 *    关闭（默认）＝查询全部有效租户执行，与上线前行为一致，用于灰度观察与一键回退；
 *    开启后＝按当前触发任务 jobId 精确取已分配租户执行，未分配跳过留痕、映射不可达/查询异常 fail-closed
 *    （D2/D3/D4），不回退为全量活跃租户。
 * 3. 执行范围固定为单实例顺序执行（2026-09-07 起移除分片分配，任务 F）。
 *
 * @author zhaohc
 * @since 2026/1/5 19:32
 */
@Slf4j
@Component
public class TenantShardingExecutor {

    /**
     * 任务-租户映射结果缓存上限
     */
    private static final int MAX_CACHED_JOBS = 2000;

    @Resource
    private TenantProvider tenantProviderI;
    @Resource
    private PlatformTransactionManager transactionManager;
    @Resource
    private EntityManagerFactory entityManagerFactory;
    @Resource
    private TenantTaskProperties tenantTaskProperties;

    // ========== 执行统计 ==========
    private final Map<String, ExecutionStatistics> executionStats = new ConcurrentHashMap<>();

    // 事务配置缓存
    private final Map<String, TransactionTemplate> transactionTemplateCache = new ConcurrentHashMap<>();

    // ========== 任务-租户映射（按 jobId）短TTL缓存 ==========
    private final Map<Long, AuthorizedTenantCacheEntry> authorizedTenantCache = new ConcurrentHashMap<>();

    // ========== 全部有效租户（迁移期模式，D3 v3）短TTL缓存 ==========
    // 全量有效租户集合被所有任务共享、与 jobId 无关，故采用独立单条目缓存而非复用 authorizedTenantCache：
    // 若以 jobId 为 key 复用后者，不同任务会各自 miss 后重复直查（缓存不跨任务共享），且迁移期的全量数据
    // 会污染开关开启后同 jobId 的「已分配租户」条目，存在误将全量当已分配执行的风险。
    // 只缓存成功查询结果；查询失败照旧上抛（切面失败可见），不以过期数据做故障降级（2026-09-08）。
    // 缓存为单实例（per-JVM）内共享：多实例部署时各实例独立缓存、分别直查一次（评审 P3 明示）。
    private volatile AuthorizedTenantCacheEntry allActiveTenantsCache;

    @PostConstruct
    public void logExecutionMode() {
        log.info("任务租户执行迁移期开关加载完成：assignedTenantEnabled={}；false=按全部有效租户执行（旧行为），true=按jobId已分配租户执行（fail-closed）",
                tenantTaskProperties.isAssignedTenantEnabled());
    }

    // ========== 核心执行方法 ==========

    /**
     * 获取或创建事务模板
     */
    private TransactionTemplate getOrCreateTransactionTemplate(String tenantId) {
        // 缓存键
        String cacheKey = tenantId + "|default";
        return transactionTemplateCache.computeIfAbsent(cacheKey, key -> {
            // 创建事务定义
            DefaultTransactionDefinition definition = createTransactionDefinition(tenantId);
            // 创建事务模板
            TransactionTemplate template = new TransactionTemplate(transactionManager, definition);
            template.setName("tenant-tx-" + tenantId);
            return template;
        });
    }

    /**
     * 创建事务定义
     */
    private DefaultTransactionDefinition createTransactionDefinition(String tenantId) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        // 默认配置：独立事务，默认隔离级别，300秒超时
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        // 设置超时时间（根据任务配置调整）
        definition.setTimeout(300);  // 5分钟
        // 设置隔离级别
        definition.setIsolationLevel(TransactionDefinition.ISOLATION_DEFAULT);
        definition.setName("tenant-tx-" + tenantId);
        return definition;
    }

    /**
     * 获取当前事务中的 EntityManager（关键方法）。
     * <p>
     * 仅返回由 JpaTransactionManager 在当前线程事务中绑定、由事务统一提交/回滚的 EntityManager；
     * 未处于事务中或绑定丢失时立即快速失败，绝不新建/兜底一个不受本事务管理的 EntityManager——
     * 否则任务内写入将绕过 REQUIRES_NEW 的回滚语义，造成半提交与跨租户数据错位（评审 P2）。
     */
    private EntityManager getTransactionalEntityManager() {
        EntityManager em = EntityManagerFactoryUtils.getTransactionalEntityManager(entityManagerFactory);
        if (em == null || !em.isOpen()) {
            throw new IllegalStateException("当前线程无事务绑定的 EntityManager，拒绝绕过事务执行（请确认任务在事务内执行）");
        }
        log.debug("从当前事务绑定资源获取 EntityManager: {}", em);
        return em;
    }

    /**
     * 为当前触发任务执行多租户任务。
     * <p>
     * 执行范围受迁移期开关 {@link TenantTaskProperties#isAssignedTenantEnabled()} 控制（D3 v3 决策）：
     * <ul>
     *   <li>关闭（默认，迁移期）：按全部有效租户执行，与上线前行为一致，用于分配核对、灰度观察与一键回退；</li>
     *   <li>开启（目标语义）：按当前触发 jobId 的已分配租户执行——未分配租户跳过留痕，
     *       映射数据源不可达/查询异常 fail-closed，不回退为全量活跃租户（D2/D3/D4）。</li>
     * </ul>
     *
     * @param jobId 当前触发任务的 XXL jobId；非 XXL 线程触发时为 null（仅开关开启时用于精确取分配租户）。
     * @param task  多租户任务适配器。
     * @return 分发结果（已执行/未执行原因）。
     */
    public TenantTaskDispatchResult executeTaskForAllTenants(Long jobId, MultiTenantTask task) {
        String taskName = task.getTaskName();
        long startTime = System.currentTimeMillis();
        try {
            // 0. 迁移期开关（D3 v3）：默认关闭=按全部有效租户执行（旧语义）；开启后按 jobId 已分配租户执行
            if (!tenantTaskProperties.isAssignedTenantEnabled()) {
                return executeForAllActiveTenants(jobId, taskName, task);
            }

            // 1. 无 XXL 任务上下文/无法取得 jobId：无法按精确口径确定执行范围，fail-closed
            if (jobId == null) {
                log.warn("任务[{}]未取得XXL任务ID(jobId为null)，本次不执行：原因=JOB_ID_MISSING", taskName);
                return TenantTaskDispatchResult.skipped(null, taskName,
                        TenantTaskSkipReason.JOB_ID_MISSING,
                        TenantTaskSkipReason.JOB_ID_MISSING.getDescription());
            }

            // 2. 按触发 jobId 取已分配租户（同源直查 + 短TTL缓存；异常时 fail-closed）
            List<String> authorizedTenants;
            try {
                authorizedTenants = getAuthorizedTenants(jobId);
            } catch (Exception e) {
                TenantTaskSkipReason reason = isMappingUnavailable(e)
                        ? TenantTaskSkipReason.MAPPING_UNAVAILABLE : TenantTaskSkipReason.MAPPING_QUERY_ERROR;
                String message = reason.getDescription() + ": " + e.getMessage();
                log.error("任务[{}]按jobId[{}]获取已分配租户失败，本次不执行（fail-closed），原因={}，详情: {}",
                        taskName, jobId, reason.name(), message, e);
                return TenantTaskDispatchResult.skipped(jobId, taskName, reason, message);
            }

            // 3. 未分配租户：跳过留痕（D2），不误计成功
            if (authorizedTenants.isEmpty()) {
                log.warn("任务[{}]在jobId[{}]下未分配任何租户，本次不执行：原因=NO_ASSIGNED_TENANT", taskName, jobId);
                return TenantTaskDispatchResult.skipped(jobId, taskName,
                        TenantTaskSkipReason.NO_ASSIGNED_TENANT,
                        TenantTaskSkipReason.NO_ASSIGNED_TENANT.getDescription());
            }

            log.info("任务[{}](jobId={})（精确模式：已分配租户）获取到{}个已分配租户，按稳定顺序串行执行",
                    taskName, jobId, authorizedTenants.size());

            // 4. 单实例顺序执行全部已分配租户（D7；分片分配路径已移除）
            List<TaskExecutionResult> results = executeForTenantsSerial(task, authorizedTenants);
            analyzeResults(taskName, results);
            return TenantTaskDispatchResult.executed(jobId, taskName, results);
        } finally {
            long costTime = System.currentTimeMillis() - startTime;
            log.debug("任务[{}]执行完成，耗时: {}ms", taskName, costTime);
        }
    }

    /**
     * 迁移期模式（开关未启用）执行：执行范围为全部有效租户（tenant_status=2），与上线前行为一致（D3 v3）。
     * <p>
     * 该模式不消费任务-租户映射，不存在映射不可达/未分配的 fail-closed 语义；
     * 有效租户源查询失败时向上抛出，由切面按失败可见处理（XXL FAIL + handleFail），不静默成功。
     *
     * @param jobId    当前触发任务的 XXL jobId（仅用于日志，可为 null）。
     * @param taskName 任务名称。
     * @param task     多租户任务适配器。
     * @return 分发结果（已执行，携带逐租户明细）。
     */
    private TenantTaskDispatchResult executeForAllActiveTenants(Long jobId, String taskName, MultiTenantTask task) {
        List<String> normalized = getCachedAllActiveTenantIds();
        if (normalized.isEmpty()) {
            log.warn("任务[{}]（迁移期模式）当前无有效租户(tenant_status=2)，无租户可执行", taskName);
        } else {
            log.info("任务[{}]（迁移期模式，jobId={}）获取到{}个有效租户，按稳定顺序串行执行",
                    taskName, jobId, normalized.size());
        }
        List<TaskExecutionResult> results = executeForTenantsSerial(task, normalized);
        analyzeResults(taskName, results);
        return TenantTaskDispatchResult.executed(jobId, taskName, results);
    }

    /**
     * 获取全部有效租户（tenant_status=2，迁移期模式），带短TTL缓存。
     * <p>
     * 缓存以 volatile 单条目承载「跨任务共享的全量集合」，TTL 复用 {@link TenantTaskProperties#getMappingCacheSeconds()}；
     * 仅缓存成功查询结果（含空列表），避免同一时段内多次任务触发对租户源重复直查（2026-09-08）。
     * 多线程并发 miss 时允许重复直查一次（短 TTL + 低触发并发下不做击穿控制）。
     *
     * @return 去重、稳定排序后的全部有效租户列表；无有效租户时为空列表。
     */
    private List<String> getCachedAllActiveTenantIds() {
        long now = System.currentTimeMillis();
        AuthorizedTenantCacheEntry cached = allActiveTenantsCache;
        if (cached != null && cached.expireAtMillis >= now) {
            log.debug("命中全部有效租户缓存，缓存内租户数: {}", cached.tenantIds.size());
            return cached.tenantIds;
        }

        List<String> activeTenants = tenantProviderI.getAllActiveTenantIds();
        List<String> normalized = normalizeTenantIds(activeTenants);
        int ttlSeconds = Math.max(0, tenantTaskProperties.getMappingCacheSeconds());
        allActiveTenantsCache = new AuthorizedTenantCacheEntry(normalized, now + ttlSeconds * 1000L);
        return normalized;
    }

    /**
     * 按 jobId 获取已分配租户（带短TTL缓存）。
     *
     * @param jobId XXL 任务ID。
     * @return 去重、稳定排序后的租户列表；未分配时为空列表。
     */
    private List<String> getAuthorizedTenants(Long jobId) {
        long now = System.currentTimeMillis();
        AuthorizedTenantCacheEntry cached = authorizedTenantCache.get(jobId);
        if (cached != null && cached.expireAtMillis >= now) {
            return cached.tenantIds;
        }

        List<String> authorized = tenantProviderI.getAuthorizedTenantsByJobId(jobId);
        List<String> normalized = normalizeTenantIds(authorized);
        // 成功查询结果（含空分配）都做短TTL缓存，避免每次触发重复直查映射表
        int ttlSeconds = Math.max(0, tenantTaskProperties.getMappingCacheSeconds());
        authorizedTenantCache.put(jobId, new AuthorizedTenantCacheEntry(normalized, now + ttlSeconds * 1000L));
        evictIfNecessary();
        return normalized;
    }

    /**
     * 租户列表规范化：去除 null/空值、去重，并按稳定顺序排序，保证每次触发执行顺序一致（XXL-010）。
     */
    private List<String> normalizeTenantIds(List<String> rawTenantIds) {
        if (rawTenantIds == null || rawTenantIds.isEmpty()) {
            return Collections.emptyList();
        }
        LinkedHashSet<String> distinct = new LinkedHashSet<>();
        int dropped = 0;
        for (String tenantId : rawTenantIds) {
            if (tenantId == null || tenantId.trim().isEmpty()) {
                dropped++;
                continue;
            }
            distinct.add(tenantId.trim());
        }
        if (dropped > 0) {
            log.warn("租户源返回的租户列表含{}个null/空值租户ID，已丢弃", dropped);
        }
        List<String> sorted = new ArrayList<>(distinct);
        Collections.sort(sorted);
        return Collections.unmodifiableList(sorted);
    }

    /**
     * 缓存超过上限时清理过期项；仍超限则重建缓存。
     */
    private void evictIfNecessary() {
        if (authorizedTenantCache.size() <= MAX_CACHED_JOBS) {
            return;
        }
        long now = System.currentTimeMillis();
        authorizedTenantCache.entrySet().removeIf(entry -> entry.getValue().expireAtMillis < now);
        if (authorizedTenantCache.size() > MAX_CACHED_JOBS) {
            log.warn("任务-租户映射缓存超过上限[{}]，清空重建", MAX_CACHED_JOBS);
            authorizedTenantCache.clear();
        }
    }

    /**
     * 判断异常是否为“数据源/提供方不可达”，用于区分原因码 MAPPING_UNAVAILABLE 与 MAPPING_QUERY_ERROR。
     */
    private boolean isMappingUnavailable(Throwable throwable) {
        for (Throwable cause = throwable; cause != null; cause = cause.getCause()) {
            if (cause instanceof DataAccessResourceFailureException
                    || cause instanceof SQLTransientConnectionException
                    || cause instanceof SQLNonTransientConnectionException) {
                return true;
            }
            // Dubbo 提供方（bytter-system）不可达/超时，同样视为映射源不可达
            if (cause.getClass().getName().startsWith("org.apache.dubbo.rpc.RpcException")) {
                return true;
            }
            String message = cause.getMessage();
            if ((containsIgnoreCase(message, "connection refused")
                    || containsIgnoreCase(message, "connection is not available")
                    || containsIgnoreCase(message, "cannot get a connection")
                    || containsIgnoreCase(message, "communications link failure")
                    || containsIgnoreCase(message, "unavailable")
                    || containsIgnoreCase(message, "timeout"))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsIgnoreCase(String text, String keyword) {
        return text != null && keyword != null && text.toLowerCase().contains(keyword.toLowerCase());
    }

    /**
     * 串行执行指定租户列表（单实例全量执行，D7）。
     */
    private List<TaskExecutionResult> executeForTenantsSerial(MultiTenantTask task, List<String> tenantIds) {
        String taskName = task.getTaskName();

        // 创建任务上下文
        TaskExecutionContext context = TaskExecutionContext.builder()
                .taskName(taskName)
                .triggerTime(new Date())
                .parameters(Collections.emptyMap())
                .build();

        // 串行执行所有已分配租户任务
        List<TaskExecutionResult> results = new ArrayList<>();
        for (String tenantId : tenantIds) {
            log.debug("开始执行任务[{}]，租户[{}]", taskName, tenantId);
            results.add(executeWithRetry(task, tenantId, context));
        }
        return results;
    }

    /**
     * 带重试的执行。
     * <p>
     * {@link #executeOnce} 失败时抛出 {@link TenantTaskExecutionException} 交由本方法决策：
     * 按根因判定是否属于可重试的基础设施异常，达到重试上限或不可重试时返回最终失败结果
     * （评审 P1：原实现因 executeOnce 吞异常、返回失败结果，导致此处的重试分支永不触发）。
     */
    private TaskExecutionResult executeWithRetry(MultiTenantTask task, String tenantId, TaskExecutionContext context) {
        String taskName = task.getTaskName();
        int retryCount = Math.max(1, task.getRetryCount());
        Throwable lastError = null;
        int lastAttempt = 0;

        for (int attempt = 1; attempt <= retryCount; attempt++) {
            try {
                return executeOnce(task, tenantId, context);
            } catch (TenantTaskExecutionException e) {
                lastAttempt = attempt;
                lastError = e.getCause() != null ? e.getCause() : e;
                log.error("任务[{}]租户[{}]第{}次执行失败: {}", taskName, tenantId, attempt, e.getMessage(), e);

                // 可重试且未达上限：指数退避后继续；否则结束
                if (isRetryableException(e) && attempt < retryCount) {
                    log.warn("任务[{}]租户[{}]第{}次失败为可重试异常，{}秒后重试", taskName, tenantId, attempt, attempt);
                    try {
                        TimeUnit.SECONDS.sleep(attempt);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return TaskExecutionResult.failure(tenantId, taskName,
                                "任务执行被中断（第" + attempt + "次尝试）", ie);
                    }
                    continue;
                }
                break;
            } catch (RuntimeException e) {
                // 防御：非预期运行时异常（如包装层缺陷）不再重试
                lastAttempt = attempt;
                lastError = e;
                log.error("任务[{}]租户[{}]第{}次执行出现非预期异常: {}", taskName, tenantId, attempt, e.getMessage(), e);
                break;
            }
        }

        String failureMessage = "执行失败（共尝试" + lastAttempt + "次）"
                + (lastError != null ? "，原因: " + lastError.getMessage() : "");
        return TaskExecutionResult.failure(tenantId, taskName, failureMessage, lastError);
    }

    /**
     * 单次执行：保存租户上下文旧值 → 先置租户再开事务 → 事务内执行任务 → finally 精确恢复旧值（任务 B）。
     * <p>
     * 评审修复要点：
     * - 租户上下文在事务开启之前设置，消除“无租户/错误租户”状态下创建事务与连接的窗口（P3）；
     * - 事务内仅允许使用事务绑定的 EntityManager（见 {@link #getTransactionalEntityManager()}），
     *   并在提交前强制 flush，保证任务写入处于本租户 REQUIRES_NEW 事务的原子范围内；
     * - 任何失败抛出 {@link TenantTaskExecutionException} 交由 {@link #executeWithRetry} 决策，
     *   不再原地吞异常返回失败结果（P1）。
     */
    private TaskExecutionResult executeOnce(MultiTenantTask task, String tenantId, TaskExecutionContext context)
            throws TenantTaskExecutionException {
        String taskName = task.getTaskName();
        Date startTime = new Date();
        // 进入前保存旧值，finally 中精确恢复，避免线程复用残留与嵌套场景串租户（XXL-015）
        String previousTenant = UserContextHelper.helper().getTTLTenant();
        // 关键：先置租户、再开事务，确保 REQUIRES_NEW 事务自创建至提交全程处于该租户上下文（P3）
        UserContextHelper.helper().setTTLTenant(tenantId);
        try {
            // 使用编程式事务执行任务（每租户独立事务模板）
            TransactionTemplate transactionTemplate = getOrCreateTransactionTemplate(tenantId);
            transactionTemplate.execute(status -> {
                try {
                    // 事务内幂等确认租户上下文，防御事务模板复用或异常路径下上下文丢失
                    UserContextHelper.helper().setTTLTenant(tenantId);

                    // 获取当前事务绑定的 EntityManager（取不到即快速失败）
                    EntityManager entityManager = getTransactionalEntityManager();
                    log.debug("租户[{}]使用事务绑定 EntityManager: {}, 事务状态: {}",
                            tenantId, entityManager, entityManager.getTransaction().isActive());

                    // 执行任务
                    task.executeForTenant(tenantId, context);

                    // 事务内强制刷新，使任务写入先落入本事务再提交，保证与回滚语义一致
                    if (entityManager.isOpen()) {
                        entityManager.flush();
                        log.debug("租户[{}] EntityManager 已刷新", tenantId);
                    }

                    log.debug("任务[{}]租户[{}]事务内执行完成", taskName, tenantId);
                    return null;  // 任务为 void，返回 null
                } catch (TenantTaskExecutionException e) {
                    // 已标记回滚的任务失败，原样上抛
                    status.setRollbackOnly();
                    throw e;
                } catch (RuntimeException e) {
                    // 标记回滚并包装为可识别异常，交由上层决定是否重试
                    status.setRollbackOnly();
                    log.error("任务[{}]租户[{}]执行异常，事务回滚", taskName, tenantId, e);
                    throw new TenantTaskExecutionException(
                            "任务[" + taskName + "]租户[" + tenantId + "]执行异常，事务回滚", e);
                }
            });

            Date endTime = new Date();
            long costTime = endTime.getTime() - startTime.getTime();
            log.debug("任务[{}]租户[{}]执行成功，耗时: {}ms", taskName, tenantId, costTime);

            return TaskExecutionResult.success(tenantId, taskName, startTime, endTime, costTime);
        } catch (TenantTaskExecutionException e) {
            // 任务/业务执行失败：仅记录日志，交由上层重试决策
            log.error("任务[{}]租户[{}]事务执行失败", taskName, tenantId, e);
            throw e;
        } catch (RuntimeException e) {
            // 事务基础设施异常（开启/提交事务失败等）统一包装，同样交由上层决策
            log.error("任务[{}]租户[{}]事务基础设施异常", taskName, tenantId, e);
            throw new TenantTaskExecutionException(
                    "任务[" + taskName + "]租户[" + tenantId + "]事务执行失败", e);
        } finally {
            // 精确恢复进入前的租户上下文：有旧值恢复旧值，无旧值清理残留
            if (previousTenant != null) {
                UserContextHelper.helper().setTTLTenant(previousTenant);
            } else {
                UserContextHelper.helper().clearTTLTenant();
            }
        }
    }

    /**
     * 判断是否可重试的异常：沿 cause 链识别网络、数据库连接等瞬时基础设施异常。
     * 业务逻辑异常（非上述类型）不重试。
     */
    private boolean isRetryableException(Throwable e) {
        for (Throwable t = e; t != null; t = t.getCause()) {
            if (t instanceof IOException
                    || t instanceof SQLException
                    || t instanceof TimeoutException
                    || t instanceof DataAccessResourceFailureException) {
                return true;
            }
            // Dubbo RPC 异常
            if (t.getClass().getName().startsWith("org.apache.dubbo.rpc.RpcException")) {
                return true;
            }
            // 连接类瞬时故障按消息识别（与 isMappingUnavailable 同源的关键词集合）
            String message = t.getMessage();
            if (message != null) {
                String lower = message.toLowerCase();
                if (lower.contains("connection refused")
                        || lower.contains("connection is not available")
                        || lower.contains("cannot get a connection")
                        || lower.contains("communications link failure")
                        || lower.contains("connection reset")
                        || lower.contains("connect timed out")
                        || lower.contains("read timed out")
                        || lower.contains("socket timeout")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 租户任务执行失败异常：由 {@link #executeOnce} 在任务/事务失败时抛出，
     * 供 {@link #executeWithRetry} 依据根因决策是否重试。
     */
    private static class TenantTaskExecutionException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        TenantTaskExecutionException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * 分析执行结果
     */
    private void analyzeResults(String taskName, List<TaskExecutionResult> results) {
        if (results == null || results.isEmpty()) {
            return;
        }

        long successCount = results.stream().filter(TaskExecutionResult::isSuccess).count();
        long failureCount = results.size() - successCount;

        // 更新统计信息
        ExecutionStatistics stats = executionStats.computeIfAbsent(taskName, k -> new ExecutionStatistics());
        stats.recordExecution(results.size(), (int) successCount, (int) failureCount);

        log.info("任务[{}]执行结果: 成功{}/{}，失败{}", taskName, successCount, results.size(), failureCount);

        // 如果有失败，记录详细信息
        if (failureCount > 0) {
            results.stream()
                    .filter(r -> !r.isSuccess())
                    .forEach(r ->
                            log.error("任务[{}]租户[{}]执行失败: {}", taskName, r.getTenantId(), r.getMessage()));
        }
    }

    // ========== 辅助类 ==========

    /**
     * 租户列表缓存项：同时作为「任务-租户映射（按 jobId，authorizedTenantCache）」与
     * 「全部有效租户（迁移期模式，allActiveTenantsCache）」两类短TTL缓存的条目结构。
     */
    private static class AuthorizedTenantCacheEntry {
        private final List<String> tenantIds;
        private final long expireAtMillis;

        private AuthorizedTenantCacheEntry(List<String> tenantIds, long expireAtMillis) {
            this.tenantIds = tenantIds;
            this.expireAtMillis = expireAtMillis;
        }
    }

    /**
     * 执行统计
     */
    private static class ExecutionStatistics {
        private final AtomicInteger totalExecutions = new AtomicInteger(0);
        private final AtomicInteger successCount = new AtomicInteger(0);
        private final AtomicInteger failureCount = new AtomicInteger(0);

        public void recordExecution(int total, int success, int failure) {
            totalExecutions.addAndGet(total);
            successCount.addAndGet(success);
            failureCount.addAndGet(failure);
        }
    }
}
