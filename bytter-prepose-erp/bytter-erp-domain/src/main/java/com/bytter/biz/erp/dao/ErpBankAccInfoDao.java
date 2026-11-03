package com.bytter.biz.erp.dao;

import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.bytter.biz.erp.entity.BankAccInfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ErpBankAccInfoDao extends BaseDao<BankAccInfo, String> {

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
        if (queryString != null && queryString.contains("v_acc_list")) {
            sqlQuery.addScalar("bankAccId", StandardBasicTypes.STRING)
                    .addScalar("bankAcc", StandardBasicTypes.STRING)
                    .addScalar("accName", StandardBasicTypes.STRING)
                    .addScalar("prentAcc", StandardBasicTypes.STRING)
                    .addScalar("entityAcc", StandardBasicTypes.STRING)
                    .addScalar("corpId", StandardBasicTypes.STRING)
                    .addScalar("curCode", StandardBasicTypes.STRING)
                    .addScalar("curId", StandardBasicTypes.STRING)
                    .addScalar("counterAcc", StandardBasicTypes.STRING)
                    .addScalar("superCounterAcc", StandardBasicTypes.STRING)
                    .addScalar("parentSign", StandardBasicTypes.STRING)
                    .addScalar("accType", StandardBasicTypes.STRING)
                    .addScalar("bankCode", StandardBasicTypes.STRING)
                    .addScalar("bankName", StandardBasicTypes.STRING)
                    .addScalar("prov", StandardBasicTypes.STRING)
                    .addScalar("provId", StandardBasicTypes.STRING)
                    .addScalar("provCode", StandardBasicTypes.STRING)
                    .addScalar("city", StandardBasicTypes.STRING)
                    .addScalar("cityId", StandardBasicTypes.STRING)
                    .addScalar("cityCode", StandardBasicTypes.STRING)
                    .addScalar("privateNo", StandardBasicTypes.STRING)
                    .addScalar("orgCode", StandardBasicTypes.STRING)
                    .addScalar("bifCode", StandardBasicTypes.STRING)
                    .addScalar("isOnline", StandardBasicTypes.INTEGER)
                    .addScalar("validSign", StandardBasicTypes.STRING)
                    .addScalar("bifCodeId", StandardBasicTypes.STRING)
                    .addScalar("isCapitalPool", StandardBasicTypes.INTEGER)
                    .addScalar("abcSecondaryAccount", StandardBasicTypes.STRING)
                    .addScalar("primaryAccount", StandardBasicTypes.STRING)
                    .addScalar("basicAccountSign", StandardBasicTypes.STRING)
                    .addScalar("isDomesticBank", StandardBasicTypes.INTEGER)
                    .addScalar("natureCode", StandardBasicTypes.STRING)
                    .addScalar("bankOrgCode", StandardBasicTypes.STRING)
                    .addScalar("attributeId", StandardBasicTypes.STRING)
                    .addScalar("electricBill", StandardBasicTypes.STRING)
                    .addScalar("tenantId", StandardBasicTypes.STRING)
                    .addScalar("tenantCode", StandardBasicTypes.STRING)
                    .addScalar("bankTypeId", StandardBasicTypes.STRING);
        }
        return sqlQuery;
    }

    /***
     * 根据租户id，英文简码获取币别信息
     *
     * @param tenantId 租户id
     * @param bankAcc 银行账号
     * @param curId  币别id
     * @return 币别信息
     */
    public List<BankAccInfo> queryBankAccInfoByAcc(String tenantId, String bankAcc, String curId) {
        Map<String, Object> params = Maps.newHashMap();

        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT v.bank_acc_id AS bankAccId, ");
        sql.append("        v.bank_acc AS bankAcc, ");
        sql.append("        v.acc_name AS accName, ");
        sql.append("        v.prent_acc AS prentAcc, ");
        sql.append("        v.entity_acc AS entityAcc, ");
        sql.append("        v.corp_id AS corpId, ");
        sql.append("        v.cur_code AS curCode, ");
        sql.append("        v.cur_id AS curId, ");
        sql.append("        v.counter_acc AS counterAcc, ");
        sql.append("        v.super_counter_acc AS superCounterAcc, ");
        sql.append("        v.parent_sign AS parentSign, ");
        sql.append("        v.acc_type AS accType, ");
        sql.append("        v.bank_code AS bankCode, ");
        sql.append("        v.bank_name AS bankName, ");
        sql.append("        v.prov AS prov, ");
        sql.append("        v.provId AS provId, ");
        sql.append("        v.provCode AS provCode, ");
        sql.append("        v.city AS city, ");
        sql.append("        v.cityId AS cityId, ");
        sql.append("        v.cityCode AS cityCode, ");
        sql.append("        v.private_no AS privateNo, ");
        sql.append("        v.org_code AS orgCode, ");
        sql.append("        v.bif_code AS bifCode, ");
        sql.append("        v.is_online AS isOnline, ");
        sql.append("        v.valid_sign AS validSign, ");
        sql.append("        v.bifCodeId AS bifCodeId, ");
        sql.append("        v.is_capital_pool AS isCapitalPool, ");
        sql.append("        v.abc_secondary_account AS abcSecondaryAccount, ");
        sql.append("        v.primary_account AS primaryAccount, ");
        sql.append("        v.basic_account_sign AS basicAccountSign, ");
        sql.append("        v.is_domestic_bank AS isDomesticBank, ");
        sql.append("        v.nature_code AS natureCode, ");
        sql.append("        v.bank_org_code AS bankOrgCode, ");
        sql.append("        v.attribute_id AS attributeId, ");
        sql.append("        v.electric_bill AS electricBill, ");
        sql.append("        v.tenant_id AS tenantId, ");
        sql.append("        v.tenant_code AS tenantCode, ");
        sql.append("        v.bank_type_id AS bankTypeId ");
        sql.append(" from v_acc_list v ");
        sql.append(" where v.tenant_id = :tenantId and v.bank_acc = :bankAcc ");
        if (StringUtils.isNotEmpty(curId)) {
            sql.append(" and v.cur_id = :curId ");
            params.put("curId", curId);
        }

        params.put("tenantId", tenantId);
        params.put("bankAcc", bankAcc);

        return this.listBySQLAliasToBean(sql.toString(), params, BankAccInfo.class);

    }
}
