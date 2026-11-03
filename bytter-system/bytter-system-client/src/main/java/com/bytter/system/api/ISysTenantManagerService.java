package com.bytter.system.api;

public interface ISysTenantManagerService {
    /**
     *
     * 查询租户号
     *
     * @param tenantCode
     * @return
     */
    String queryTenantIdByCode(String tenantCode);

    /**
     * 根据租户ID查询签约银行机构ID。
     *
     * @param tenantId 租户ID
     * @return SYS_TENANT_MANAGER.CORP_ID
     */
    String queryCorpIdByTenantId(String tenantId);
}
