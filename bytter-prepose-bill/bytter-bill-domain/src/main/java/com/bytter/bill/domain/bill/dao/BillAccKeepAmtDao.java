package com.bytter.bill.domain.bill.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.bill.domain.bill.entity.BillAccKeepAmt;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 银承账号留存金额
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author ZRS
 * @date 2025/3/26
 */
@Repository
public class BillAccKeepAmtDao extends BaseDao<BillAccKeepAmt,String> {
    public BillAccKeepAmt checkByBankAcc(String bankAcc, String id) {
        Map<String, Object> params = new HashMap<>();
        StringBuilder hql = new StringBuilder("from BillAccKeepAmt where bankAcc = :bankAcc ");
        if (ObjectUtil.isNotEmpty(id)){
            hql.append(" and id != :id ");
            params.put("id", id);
        }
        params.put("bankAcc", bankAcc);
        List<BillAccKeepAmt> billAccKeepAmts = this.listByHQLAliasToBean(hql.toString(), params, BillAccKeepAmt.class);
        if (ObjectUtil.isEmpty(billAccKeepAmts)){
            return null;
        }
        return billAccKeepAmts.get(0);
    }
}
