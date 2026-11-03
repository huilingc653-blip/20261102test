package com.bytter.biz.allocation.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.biz.allocation.entity.BisGatherPolicyAccount;
import com.bytter.biz.allocation.vo.BisGatherPolicyAccountVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 资金中心调拨账户配置
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/2/11
 */
@Repository
public class BisGatherPolicyAccountDao  extends BaseDao<BisGatherPolicyAccount, String> {
    public DataTablesOutput<BisGatherPolicyAccountVO> getAccountDatatable(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id,a.corp_name as corpName,a.bank_acc as bankAcc,a.bank_name as bankName,a.priority,");
        sql.append(" a.is_down as isDown,a.status,a.create_date as createDate,a.create_by as createBy,a.update_date as updateDate,");
        sql.append(" a.update_by as updateBy ");
        sql.append(" from bis_gather_policy_account a ");
        return this.dealDynamicBySQL( dataTablesInput,sql.toString(), BisGatherPolicyAccountVO.class, "a", true);
    }

    public List<BisGatherPolicyAccount> getUniqueByProperty(String propertyName, Object propertyValue, String id) {
        StringBuilder hql = new StringBuilder();
        hql.append("from BisGatherPolicyAccount where 1=1 ");
        if (ObjectUtil.isNotEmpty(propertyName) && ObjectUtil.isNotEmpty(propertyValue)) {
            hql.append(" and " + propertyName + " = '" + propertyValue + "' ");
        }
        if (ObjectUtil.isNotEmpty(id)) {
            hql.append(" and id != '" + id + "' ");
        }
        return this.listByHQL(hql.toString(), new HashMap());
    }

    /**
     * 资金中心配置的调拨账户
     */
    public List<BisGatherPolicyAccount> getAllIsDown() {
        StringBuilder hql = new StringBuilder();
        hql.append("from BisGatherPolicyAccount where status = :status ");
        hql.append("order by priority ");
        Map<String,Object> params = new HashMap<>();
        params.put("status", CommonConstant.YES);
        return this.listByHQL(hql.toString(), params);
    }
}
