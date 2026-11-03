package com.bytter.basic.service.impl;


import com.bytter.system.api.ISysTenantManagerService;
import com.bytter.system.entity.SysTenantManager;
import com.bytter.system.service.SysTenantManagerService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service("sysTenantManagerServiceI")
@DubboService
public class SysTenantManagerServiceImpl implements ISysTenantManagerService {


    @Autowired
    private SysTenantManagerService sysTenantManagerService;

    @Override
    public String queryTenantIdByCode(String tenantCode) {
        SysTenantManager sysTenantManager = sysTenantManagerService.getByTenantCode(tenantCode);
        if (null != sysTenantManager) {
            return sysTenantManager.getTenantCode();
        }
        return null;
    }

    /**
     * 根据租户ID查询签约银行机构ID。
     *
     * @param tenantId 租户ID
     * @return 签约银行机构ID
     */
    @Override
    public String queryCorpIdByTenantId(String tenantId) {
        SysTenantManager sysTenantManager = sysTenantManagerService.getByTenantId(tenantId);
        return sysTenantManager == null ? null : sysTenantManager.getCorpId();
    }
}
