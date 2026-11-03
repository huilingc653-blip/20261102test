package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.BtCurrency;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpBtCurrencyDao extends BaseDao<BtCurrency, String> {

    /***
     * 根据租户id，英文简码获取币别信息
     *
     * @param tenantId
     * @param englishCodeList
     * @return 币别信息
     */
    public List<BtCurrency> listBtCurrency(String tenantId, List<String> englishCodeList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, cur_code as curCode, cur_name as curName, english_code as englishCode, tenant_id as tenantId ");
        sql.append(" from BT_CURRENCY ");
        sql.append(" where valid_sign = :validSign and english_code in (:englishCodeList) and tenant_id = :tenantId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("validSign", CommonConstant.VALID_SIGN_YES);
        params.put("englishCodeList", englishCodeList);
        params.put("tenantId", tenantId);

        return this.listBySQLAliasToBean(sql.toString(), params, BtCurrency.class);

    }
}
