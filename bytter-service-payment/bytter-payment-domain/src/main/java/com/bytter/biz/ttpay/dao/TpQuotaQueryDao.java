package com.bytter.biz.ttpay.dao;
import com.bytter.biz.ttpay.entity.TpQuota;
import com.bytter.biz.ttpay.vo.TpQuotaVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: DAO
 * @Description: 限额查询信息Dao
 * @date 2022-05-06
 */
@Repository
public class TpQuotaQueryDao extends BaseDao<TpQuota, String> {
    /**
     * 查询限额信息
     *
     * @param dataTablesInput 查询条件
     * @return
     */
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("");
        sql.append(" select t.ID as id, t.CORP_ID as corpID, t.MERCHANT_ID as merchantId, t.MERCHANT_NO as merchantNo, t.PAY_WAY as payWay, ");
        sql.append(" t.DAY_SINGLE as daySingle, t.DAY_TOTAL as dayTotal, t.MONTH_TOTAL as monthTotal, ");
        sql.append(" t.VALID_SIGN as validSign, t.EFFECT_DATE as effectDate, t.rmk as rmk, ");
        sql.append(" t.create_By as createBy,t.create_Date as createDate,t.update_By as updateBy,t.update_Date as updateDate ");
        sql.append(" from TP_QUOTA t ");
        sql.append(" where 1=1 ");
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" and ").append(SqlUtil.getInSqlByParams(corpAuthList, "t.CORP_ID", params));
        return getDynamicBySQL(dataTablesInput, sql.toString(), TpQuotaVO.class, "", false, params);
    }
}
