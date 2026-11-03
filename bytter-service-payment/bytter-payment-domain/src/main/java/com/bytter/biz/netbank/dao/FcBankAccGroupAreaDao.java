package com.bytter.biz.netbank.dao;

import com.bytter.biz.netbank.entity.FcBankAccGroup;
import com.bytter.biz.netbank.entity.FcBankAccGroupArea;
import com.bytter.biz.netbank.vo.FcBankAccGroupVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import org.springframework.stereotype.Repository;
import org.ujmp.core.interfaces.HasShortArray;

import java.util.HashMap;
import java.util.Map;

import java.util.HashMap;
import java.util.List;

/**
 * @author linqp
 * @Description 银行账户组
 */
@Repository
public class FcBankAccGroupAreaDao extends BaseDao<FcBankAccGroupArea, String> {
    public void deleteByGroupId(String id) {
        String sql = " delete from FC_BANK_ACC_GROUP_AREA where GROUP_ID=:id";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        createSQLQuery(sql, params).executeUpdate();
    }
    public List<FcBankAccGroup> getValidPayType() {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fbag.id as id,fbag.acc_group_code as accGroupCode,fbag.ACC_GROUP_NAME as accGroupName ");
        sql.append(" from FC_BANK_ACC_GROUP fbag");
        return listBySQLAliasToBean(sql.toString(),new HashMap<>(),FcBankAccGroup.class);
    }

    public List<FcBankAccGroup> listByIds(List<String> codeList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select fbag.id as id,fbag.acc_group_code as accGroupCode,fbag.ACC_GROUP_NAME as accGroupName ");
        sql.append(" from FC_BANK_ACC_GROUP fbag where fbag.acc_group_code in (:codes) ");
        Map<String,Object> params = new HashMap<>();
        params.put("codes",codeList);
        return listBySQLAliasToBean(sql.toString(),params,FcBankAccGroup.class);
    }
}
