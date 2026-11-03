package com.bytter.system.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.system.entity.BtBankAccAppCur;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: BT_BANK_ACC_CUR
 * @date 2017-06-02 18:03:22
 */
@Repository
public class BtBankAccAppCurDao extends BaseDao<BtBankAccAppCur, String> {
    /**
     * 获取银行账号申请表币别中间表
     *
     * @param accAppId
     * @param curId
     * @return
     */
    public BtBankAccAppCur getBtBankAccAppCur(String accAppId, String curId) {
        StringBuilder hql = new StringBuilder();
        HashMap<String, Object> params = new HashMap<String, Object>();
        hql.append(" from BtBankAccAppCur cur where cur.appId = :accAppId and cur.curId = :curId ");
        params.put("accAppId", accAppId);
        params.put("curId", curId);
        return (BtBankAccAppCur) this.createQuery(hql.toString()).setProperties(params).uniqueResult();
    }
}
