package com.bytter.basic.dao;
import com.bytter.basic.entity.BtPurposeCorp;
import com.bytter.basic.vo.BtPurposeCorpVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zuoy
 * @version 1.0
 * @Title: Dao
 * @Description: 用途单位中间表
 * @date 2017-10-26 09:57:15
 */
@Repository
public class BtPurposeCorpDao extends BaseDao<BtPurposeCorp, String> {
    /**
     * 根据用途id查找用途单位
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCorp> getByPurposeId(String purposeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pc.purpose_id, pc.corp_id from bt_purpose_corp pc where pc.purpose_id = :purposeId ");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("purposeId", purposeId);
        return this.listBySQLAliasToBean(sql.toString(), params, BtPurposeCorp.class);
    }
    public void deleteBtPurposeCorp(String purposeId) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " delete from bt_purpose_corp where purpose_id = :purposeId ";
        params.put("purposeId", purposeId);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 根据用途id查找用途单位,带父单位ID,用于进行树形排序
     *
     * @param purposeId 用途ID
     * @return
     */
    public List<BtPurposeCorpVO> getCorpByPurposeId(String purposeId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select pc.purpose_id as purposeId, pc.corp_id as corpId,c.name AS corpName, c.parent_id as parentCorpId, c.code as corpCode from bt_purpose_corp pc ");
        sql.append(" left join sys_corp c on c.id = pc.corp_id ");
        sql.append(" where pc.purpose_id = :purposeId order by c.sort");
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("purposeId", purposeId);
        return this.listBySQLAliasToBean(sql.toString(), params, BtPurposeCorpVO.class);
    }
}
