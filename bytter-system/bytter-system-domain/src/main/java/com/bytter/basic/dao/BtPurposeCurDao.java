package com.bytter.basic.dao;
import com.bytter.basic.entity.BtPurposeCur;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author huangx
 * @version 1.0
 * @Title: Dao
 * @Description: 用途币种中间表
 * @date 2022-11-23 09:57:15
 */
@Repository
public class BtPurposeCurDao extends BaseDao<BtPurposeCur, String> {
    /**
     * 根据用途id查找用途币别
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCur> getByPurposeId(String purposeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pc.purpose_id, pc.cur_id from bt_purpose_cur pc where pc.purpose_id = :purposeId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("purposeId", purposeId);
        return this.listBySQLAliasToBean(sql.toString(), params, BtPurposeCur.class);
    }
    public void deleteBtPurposeCur(String purposeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from bt_purpose_cur where purpose_id = :purposeId ";
        params.put("purposeId", purposeId);
        createSQLQuery(sql, params).executeUpdate();
    }
}
