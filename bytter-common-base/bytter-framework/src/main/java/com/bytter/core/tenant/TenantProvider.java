package com.bytter.core.tenant;

import java.util.List;

/**
 * 租户提供者接口。
 *
 * @author zhaohc
 * @since 2026/1/6 13:59
 */
public interface TenantProvider {

    /**
     * 获取所有有效（启用状态）租户ID列表。
     * <p>
     * 迁移期开关（D3 v3）关闭时，执行器以该方法结果确定任务执行范围（与上线前行为一致）；
     * 开关开启后执行范围改由 {@link #getAuthorizedTenantsByJobId(Long)} 决定。
     *
     * @return 有效租户ID列表
     */
    List<String> getAllActiveTenantIds();

    /**
     * 根据 XXL 任务ID获取被授权（已分配）且启用状态的租户ID列表。
     * <p>
     * 数据源为内管端维护的任务-租户分配关系（SYS_TENANT_SCHEDULED_TASK），与 sys_tenant_manager 同库直查
     * （D4）；当前实现位于 bytter-system（持有该库），其余执行器服务通过 Dubbo 引用获得。
     * 迁移期开关（D3 v3）启用后，执行侧以此方法结果为准确定执行范围：未分配即跳过留痕、
     * 映射数据源不可达/查询异常即 fail-closed，不回退为全量活跃租户（D2/D3/D4）；
     * 开关关闭（默认）时执行范围由 {@link #getAllActiveTenantIds()} 决定。
     *
     * @param jobId XXL 任务ID（= xxl_job_info.id = 触发上下文 jobId）。
     * @return 被授权租户ID列表；未分配租户时返回空列表。
     */
    List<String> getAuthorizedTenantsByJobId(Long jobId);

}
