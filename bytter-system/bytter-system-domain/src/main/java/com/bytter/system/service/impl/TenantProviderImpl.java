package com.bytter.system.service.impl;

import cn.hutool.core.collection.ListUtil;
import com.bytter.core.tenant.TenantProvider;
import com.bytter.system.dao.SysTenantManagerDao;
import com.bytter.system.entity.SysTenantManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 租户提供者实现。
 *
 * @author zhaohc
 * @since 2026/1/8 15:49
 */
@Slf4j
@Service
@DubboService
public class TenantProviderImpl implements TenantProvider {

    @Resource
    private SysTenantManagerDao sysTenantManagerDao;

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAllActiveTenantIds() {
        // log.info("获取所有活跃租户");
        List<String> tenantList = sysTenantManagerDao.createSQLQuery("select tenant_code from sys_tenant_manager where tenant_status = 2", new HashMap<>()).list();
        // log.info("活跃租户列表：{}", tenantList);
        return tenantList;
    }

    /**
     * 按 XXL 任务ID取已分配且启用状态的租户。
     * <p>
     * SYS_TENANT_SCHEDULED_TASK 由内管端维护，与 sys_tenant_manager 同库直查（D4）；
     * TENANT_ID 引用 sys_tenant_manager.ID，通过联查输出租户编码 TENANT_CODE，口径待任务 A 内管联调复核（D1）。
     *
     * @param jobId XXL 任务ID。
     * @return 被授权租户编码列表，未分配时为空列表。
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<String> getAuthorizedTenantsByJobId(Long jobId) {
        Map<String, Object> param = new HashMap<>();
        param.put("jobId", jobId);
        List<String> tenantList = sysTenantManagerDao.createSQLQuery(
                "select tm.tenant_code from sys_tenant_scheduled_task t "
                        + "inner join sys_tenant_manager tm on t.tenant_id = tm.id "
                        + "where t.job_id = :jobId and tm.tenant_status = 2",
                param).list();
        // log.info("jobId[{}] 已分配租户列表：{}", jobId, tenantList);
        return tenantList;
    }

}
