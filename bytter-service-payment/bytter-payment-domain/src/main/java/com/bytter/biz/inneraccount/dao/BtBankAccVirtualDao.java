package com.bytter.biz.inneraccount.dao;
import com.bytter.biz.inneraccount.entity.BtBankAccVirtual;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 虚拟账号记账单元信息
 * @date 2022-08-01 17:50:16
 */
@Repository
public class BtBankAccVirtualDao extends BaseDao<BtBankAccVirtual, String> {
    /**
     * 根据账户ID获取虚拟账号子单元信息
     * @param bankAccId
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public List<BtBankAccVirtual> getListByBankAccId(String bankAccId) {
        StringBuilder hql = new StringBuilder();
        hql.append(" from BtBankAccVirtual where bankAccId = :bankAccId ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bankAccId", bankAccId);
        return this.createQuery(hql.toString()).setProperties(params).list();
    }
    /**
     * 根据账户ID删除虚拟账户子单元信息
     * @param bankAccId
     */
    public int deleteByBankAccId(String bankAccId) {
        String sql = "delete from bt_bank_acc_virtual where bank_acc_id=:bankAccId";
        Map<String, Object> map = new HashMap<>();
        map.put("bankAccId", bankAccId);
        return createSQLQuery(sql, map).executeUpdate();
    }
}
