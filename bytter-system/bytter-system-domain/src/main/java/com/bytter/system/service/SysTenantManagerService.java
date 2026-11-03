package com.bytter.system.service;

import com.bytter.core.base.BaseDao;
import com.bytter.core.base.BaseService;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.dao.SysTenantManagerDao;
import com.bytter.system.entity.SysTenantManager;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 租户Service
 *
 * @author wuxc
 * @date 2026-01-29
 */
@Service("sysTenantManagerService")
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SysTenantManagerService extends BaseService<SysTenantManager, String> {

    @Resource
    private SysTenantManagerDao sysTenantManagerDao;

    @Override
    public BaseDao<SysTenantManager, String> getEntityDao() {
        return sysTenantManagerDao;
    }

    /**
     *
     * 根据企业客户号查询租户数据
     *
     * @param tenantCode
     * @return
     */
    public SysTenantManager getByTenantCode(String tenantCode){
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantCode", tenantCode));
        List list = criteria.list();
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }else if (list.size() > 1) {
            logger.error("企业客户号【{}】有重复数据【{}】，请联系管理员删除当前用户重复数据！", tenantCode, list.size());
        }
        return (SysTenantManager) list.get(0);
    }

    /**
     * 根据租户ID查询租户签约信息。
     *
     * @param tenantId 租户ID
     * @return 租户签约信息
     */
    public SysTenantManager getByTenantId(String tenantId) {
        Criteria criteria = createCriteria();
        criteria.add(Restrictions.eq("tenantId", tenantId));
        List list = criteria.list();
        if (EmptyUtils.isEmpty(list)) {
            return null;
        }
        if (list.size() > 1) {
            logger.error("租户ID【{}】存在【{}】条签约信息，请检查租户管理数据！", tenantId, list.size());
        }
        return (SysTenantManager) list.get(0);
    }
}
