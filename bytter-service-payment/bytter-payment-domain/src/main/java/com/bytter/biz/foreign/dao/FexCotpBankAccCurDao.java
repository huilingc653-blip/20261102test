package com.bytter.biz.foreign.dao;

import com.bytter.biz.foreign.entity.FexCotpBankAccCur;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: FEX_COTP_BANKACC_CUR
 * @date 2024-02-24 18:03:22
 */
@Repository
public class FexCotpBankAccCurDao extends BaseDao<FexCotpBankAccCur, String> {

    /**
     * 根据账号ID删除币种信息
     * @param bankAccId
     */
    public int deleteByBankAccId(String bankAccId) {
        String sql = "delete from FEX_COTP_BANKACC_CUR where bank_acc_id=:bankAccId";
        Map<String, Object> map = new HashMap<>();
        map.put("bankAccId", bankAccId);
        return createSQLQuery(sql, map).executeUpdate();
    }
}
