package com.bytter.biz.allocation.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.BisGatherPolicyAgreement;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 内部资金归集管理协议签订
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/12
 */
@Repository
public class BisGatherPolicyAgreementDao extends BaseDao<BisGatherPolicyAgreement,String> {
    public List<BisGatherPolicyAgreement> getUniqueByProperty(String corpId, String id) {
        Map<String,Object> params = new HashMap();
        StringBuilder hql = new StringBuilder();
        hql.append("from BisGatherPolicyAgreement where 1=1 ");
        if (ObjectUtil.isNotEmpty(corpId)) {
            hql.append(" and corpId = :corpId ");
            params.put("corpId", corpId);
        }
        if (ObjectUtil.isNotEmpty(id)) {
            hql.append(" and id != :id ");
            params.put("id", id);
        }
        return this.listByHQL(hql.toString(),params);
    }
}
