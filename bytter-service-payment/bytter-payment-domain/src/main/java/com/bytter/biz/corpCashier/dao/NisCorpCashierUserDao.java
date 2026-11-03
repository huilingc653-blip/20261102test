package com.bytter.biz.corpCashier.dao;

import com.bytter.biz.corpCashier.entity.NisCorpCashierUser;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description: 单位出纳表用户表Dao
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2025
 *
 * @author bytter
 * @date 2025/7/11
 */
@Repository
public class NisCorpCashierUserDao extends BaseDao<NisCorpCashierUser,String> {
    public void deleteCashier(String corpCashierId) {
        String sql = "delete from nis_corp_cashier_user where corp_cashier_id = :corpCashierId ";
        Map<String,Object> params = new HashMap<>();
        params.put("corpCashierId", corpCashierId);
        this.createSQLQuery(sql, params).executeUpdate();
    }

    public List<NisCorpCashierUser> getCashierUser(List<String> corpIds) {
        Map<String,Object> params = new HashMap<>();
        params.put("corpIds", corpIds);
        String hql = " from NisCorpCashierUser where corpId in (:corpIds) ";
        return this.listByHQL(hql, params);
    }
}
