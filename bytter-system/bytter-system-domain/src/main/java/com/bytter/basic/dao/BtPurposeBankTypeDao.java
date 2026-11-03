package com.bytter.basic.dao;
import com.bytter.basic.entity.BtPurposeBankType;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 用途行类别中间表
 * @date 2022-11-23 09:57:15
 */
@Repository
public class BtPurposeBankTypeDao extends BaseDao<BtPurposeBankType, String> {
    /**
     * 根据用途id查找用途银行类别
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeBankType> getByPurposeId(String purposeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pc.purpose_id, pc.bank_type_id from bt_purpose_bank_type pc where pc.purpose_id = :purposeId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("purposeId", purposeId);
        return this.listBySQLAliasToBean(sql.toString(), params, BtPurposeBankType.class);
    }
    public void deleteBtPurposeBankType(String purposeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from bt_purpose_bank_type where purpose_id = :purposeId ";
        params.put("purposeId", purposeId);
        createSQLQuery(sql, params).executeUpdate();
    }
}
