package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.BtCurrency;
import com.bytter.biz.erp.entity.SysRegion;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpSysRegionDao extends BaseDao<SysRegion, String> {

    /***
     * 通过重写 createSQLQuery 方法,为所有字段显式指定了 Hibernate 类型。
     * 这样即使数据库中的 CHAR(1) 字段包含空字符串,Hibernate 也会将其作为 String 类型处理,而不会尝试转换为 Character 类型,
     * 从而避免了 StringIndexOutOfBoundsException 异常
     * @param queryString
     * @return SQLQuery
     */
    @Override
    protected SQLQuery createSQLQuery(String queryString) {
        SQLQuery sqlQuery = super.createSQLQuery(queryString);
        if (queryString != null && queryString.contains("SYS_REGION")) {
            sqlQuery.addScalar("id", StandardBasicTypes.STRING)
                    .addScalar("parentId", StandardBasicTypes.STRING)
                    .addScalar("bankProv", StandardBasicTypes.STRING)
                    .addScalar("bankInputCity", StandardBasicTypes.STRING)
                    .addScalar("provInputName", StandardBasicTypes.STRING)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .addScalar("reLevel", StandardBasicTypes.STRING)
                    .addScalar("sort", StandardBasicTypes.STRING)
                    .addScalar("tenantId", StandardBasicTypes.STRING);
        }
        return sqlQuery;
    }

    /***
     * 根据城市编码查询省市信息
     *
     * @param bankInputCity
     * @return 省市信息
     */
    public List<SysRegion> listRegionByCity(String bankInputCity) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select id, parent_id as parentId, bank_prov as bankProv, bank_input_city as bankInputCity, prov_input_name as provInputName, ");
        sql.append(" name, re_level as reLevel, sort, tenant_id as tenantId ");
        sql.append(" from SYS_REGION ");
        sql.append(" where BANK_INPUT_CITY = :bankInputCity ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("bankInputCity", bankInputCity);

        return this.listBySQLAliasToBean(sql.toString(), params, SysRegion.class);

    }
}
