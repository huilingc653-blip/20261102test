package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.FcVoucherType;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpFcVoucherTypeDao extends BaseDao<FcVoucherType, String> {

    /***
     * 根据租户id和付款方式编码查询付款方式
     *
     * @param tenantId 租户id
     * @param voucherType 付款方式编码
     * @return 付款方式
     */
    public List<FcVoucherType> listVoucherTypeByType(String tenantId, String voucherType) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, voucher_type as voucherType, voucher_name as voucherName, tenant_id as tenantId ");
        sql.append(" from FC_VOUCHER_TYPE ");
        sql.append(" where voucher_type = :voucherType and tenant_id = :tenantId ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("voucherType", voucherType);
        params.put("tenantId", tenantId);


        return this.listBySQLAliasToBean(sql.toString(), params, FcVoucherType.class);

    }
}
