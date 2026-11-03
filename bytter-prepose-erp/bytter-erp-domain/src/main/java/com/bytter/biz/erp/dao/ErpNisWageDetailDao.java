package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.NisWageDetail;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author fangcf
 * @date 2026-05-13
 */
@Repository
public class ErpNisWageDetailDao extends BaseDao {

    /**
     * 获取代发工资明细
     *
     * @param tenantId  租户id
     * @param wageDtlNo 工资明细单号
     * @return 工资明细
     */
    public NisWageDetail getOne(String tenantId, String wageDtlNo) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" select * from NIS_WAGE_DETAIL d ");
        sql.append(" where d.TENANT_ID = :tenantId and d.WAGE_DTL_NO = :wageDtlNo ");

        params.put("tenantId", tenantId);
        params.put("wageDtlNo", wageDtlNo);

        return this.getBySQLAliasToBean(sql.toString(), params, NisWageDetail.class);
    }
}
