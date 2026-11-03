package com.bytter.system.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.system.entity.NisBindingBankacc;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行账号绑定账号中间表
 */
@Repository
public class NisBindingBankaccDao extends BaseDao<NisBindingBankacc, String> implements Serializable {


    /**
     * 根据bankId删除
     * @param bankId
     */
    public void deleteBybankId(String bankId) {
        createSQLQuery("delete from nis_binding_bankacc where bank_id=?", bankId).executeUpdate();
    }

    /**
     * 保存
     * @param bankId
     * @param bindingBankId
     */
    public void saveNisBindingBankacc(String bankId, String bindingBankId){
        String sql = " INSERT INTO nis_binding_bankacc (BANK_ID, BINDING_BANK_ID) VALUES(:bankId,:bindingBankId)";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankId", bankId);
        params.put("bindingBankId", bindingBankId);
        createSQLQuery(sql, params).executeUpdate();
    }

    public List<String> listBybankId(String bankId){
        String sql = " select binding_bank_id from nis_binding_bankacc where BANK_ID = :bankId";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankId", bankId);
        List<String> list = createSQLQuery(sql,params).list();
        return list;
    }

}
