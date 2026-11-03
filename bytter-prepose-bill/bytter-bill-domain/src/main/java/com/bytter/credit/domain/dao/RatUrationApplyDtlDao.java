package com.bytter.credit.domain.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.bytter.credit.dto.RatUrationApplyDtlDTO;
import org.springframework.stereotype.Repository;
import com.bytter.credit.domain.entity.RatUrationApplyDtl;
import com.bytter.core.base.BaseDao;
/**
 * @Title: Dao
 * @Description: 授信占用明细表
 * @author onlineGenerator
 * @date 2017-08-22 17:45:20
 * @version 1.0
 *
 */
@Repository
public class RatUrationApplyDtlDao extends BaseDao<RatUrationApplyDtl, String> {
    /**
     * 功能介绍 : 根据占用申请Id查询综合占用详情。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationApplyDtlDTO> queryRatUrationByApplyId(String applyId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select ru.id as id,ru.item_flag as itemFlag,ru.item_order as itemOrder,rc.sys_item_code as sysItemCode, ");
        sql.append(" ru.item_Code as itemCode,rc.name as itemName,ru.max_money as maxMoney ,");
        sql.append(" case when ru.credit_type is null then '1' else ru.credit_type end as creditType ");
        sql.append(" from rat_uration_apply_dtl ru left join rat_item_custom rc on ru.item_code = rc.id ");
        sql.append(" where ru.apply_code = :applyId ");
        params.put("applyId", applyId);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationApplyDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据占用Id查询综合占用详情。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationApplyDtlDTO> queryRatUrationById(String urationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru.id as id,ru.item_flag as itemFlag,ru.item_order as itemOrder, ");
        sql.append(" ru.item_Code as itemCode,rc.name as itemName,ru.max_money as maxMoney, ");
        sql.append(" rs.comp_sign as compSign,ru.apply_money as applyMoney ");
        sql.append(" from rat_uration_apply_dtl ru left join rat_item_custom rc on ru.item_code = rc.id ");
        sql.append(" inner join rat_uration rd on ru.apply_code = rd.id ");
        sql.append(
                " inner join rat_bkration_dis_detail rs on ru.item_code = rs.item_code and rd.dis_code = rs.dis_code ");
        sql.append(" where ru.apply_code = :urationId ");
        params.put("urationId", urationId);
        return listBySQLAliasToBean(sql.toString(), params, RatUrationApplyDtlDTO.class);
    }
    /**
     * 功能介绍 : 根据占用Id查询综合占用实体。<br>
     * <b>日期：</b>2017年8月23日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public List<RatUrationApplyDtl> queryRatUrationDtlById(String urationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select ru from RatUrationApplyDtl ru ");
        sql.append(" where ru.applyCode = :urationId ");
        params.put("urationId", urationId);
        return listByHQL(sql.toString(), params);
    }
    /**
     * 功能介绍 : 删除原授信综合占用明细。<br>
     * <b>日期：</b>2017年8月18日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void deleteRatUrationDtlByCode(String urationId) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" delete from rat_uration_apply_dtl where apply_code = :urationId ");
        params.put("urationId", urationId);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 功能介绍 : 更新占用实际最高占用额度。<br>
     * <b>日期：</b>2017年9月21日<br>
     * 
     * @version 1.0
     * @author liyh
     */
    public void updateBkrationApplyMaxMoney(RatUrationApplyDtlDTO dtlDTO) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" update rat_uration_apply_dtl rd set rd.apply_money = :applyMoney ");
        sql.append(" where rd.id = :id ");
        params.put("id", dtlDTO.getId());
        params.put("applyMoney", dtlDTO.getApplyMoney());// 实际占用金额
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
