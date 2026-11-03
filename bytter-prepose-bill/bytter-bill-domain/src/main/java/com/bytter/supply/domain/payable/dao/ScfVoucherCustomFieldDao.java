package com.bytter.supply.domain.payable.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.supply.domain.payable.entity.ScfVoucherCustomField;
import com.bytter.supply.domain.payable.vo.ScfVoucherCustomFieldVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ScfVoucherCustomFieldDao extends BaseDao<ScfVoucherCustomField, String> {

    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.* FROM scf_voucher_custom_field c WHERE 1=1 ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), ScfVoucherCustomFieldVO.class, "c", false);
    }

    public Map<String, List<ScfVoucherCustomFieldVO>> getCustomFieldByMainId(List<String> mainIdList) {
        Map<String, List<ScfVoucherCustomFieldVO>> result = new HashMap<>();
        if (mainIdList == null || mainIdList.isEmpty()) {
            return result;
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id as id,");
        sql.append(" c.VOUCHER_ID as voucherId,");
        sql.append(" c.FIELD_CODE as fieldCode,");
        sql.append(" c.FIELD_NAME as fieldName,");
        sql.append(" c.FIELD_TYPE as fieldType,");
        sql.append(" c.FIELD_VALUE as fieldValue ");
        sql.append(" FROM scf_voucher_custom_field c WHERE c.VOUCHER_ID IN (:voucherIds) ");
        Map<String, Object> params = new HashMap<>();
        params.put("voucherIds", mainIdList);
        List<ScfVoucherCustomFieldVO> fieldList = listBySQLAliasToBean(sql.toString(), params, ScfVoucherCustomFieldVO.class);
        if (fieldList != null && !fieldList.isEmpty()) {
            result = fieldList.stream().collect(Collectors.groupingBy(ScfVoucherCustomFieldVO::getVoucherId));
        }
        return result;
    }

    public List<ScfVoucherCustomFieldVO> getCustomFieldByVoucherId(String voucherId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT c.id as id,");
        sql.append(" c.VOUCHER_ID as voucherId,");
        sql.append(" c.FIELD_CODE as fieldCode,");
        sql.append(" c.FIELD_NAME as fieldName,");
        sql.append(" c.FIELD_TYPE as fieldType,");
        sql.append(" c.FIELD_VALUE as fieldValue ");
        sql.append(" FROM scf_voucher_custom_field c WHERE 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(voucherId)) {
            sql.append(" AND c.VOUCHER_ID = :voucherId");
            params.put("voucherId", voucherId);
        }
        return listBySQLAliasToBean(sql.toString(), params, ScfVoucherCustomFieldVO.class);
    }

    public void deleteByVoucherId(String voucherId) {
        String sql = "DELETE FROM scf_voucher_custom_field WHERE VOUCHER_ID = :voucherId";
        Map<String, Object> params = new HashMap<>();
        params.put("voucherId", voucherId);
        createSQLQuery(sql, params).executeUpdate();
    }
}
