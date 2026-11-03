package com.bytter.bill.domain.bill.dao;

import com.bytter.bill.domain.bill.entity.AbEmitBillTargetProportionInfo;
import com.bytter.bill.domain.bill.vo.AbEmitBillTargetProportionInfoVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @Description 银承开票占比目标维护dao
* @author zhous
* @date 2025/4/22 10:32
*/
@Repository
public class AbEmitBillTargetProportionInfoDao extends BaseDao<AbEmitBillTargetProportionInfo, String> {

    /**
    * @Description 银承开票占比目标维护列表查询
    * @author zhous
    * @date 2025/4/23 9:03
    */
    public DataTablesOutput<AbEmitBillTargetProportionInfoVO> dataTable(DataTablesInput input, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder("select ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" ae.id,ae.corp_id as corpId,ae.corp_name as corpName,ae.corp_code as corpCode, ae.current_year as currentYear,ae.target_proportion as targetProportion");
        sql.append(" from ab_emit_bill_target_ratio_info ae ");
        sql.append(" where 1=1 and ae.is_delete is null ");
        sql.append(" and ").append(SqlUtil.getInSql(corpAuthList, "ae.corp_id", true));
        DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(), AbEmitBillTargetProportionInfoVO.class, "ae", false, params);
        return dataOutInfo;
    }

    /**
    * @Description 根据单位id和年份查找是否存在
    * @param corpId 单位id
    * @param currentYear 年份
    * @author zhous
    * @date 2025/4/23 9:02
    */
    public boolean existBillTargetProportionInfo(String corpId, String currentYear) {
        String hql = " from AbEmitBillTargetProportionInfo ae where ae.corpId = :corpId and ae.currentYear=:currentYear and ae.isDelete is null ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("corpId", corpId);
        params.put("currentYear", currentYear);
        List list = listByHQL(hql, params);
        if (list != null && list.size() > 0) {
            return true;
        }
        return false;
    }

    /**
    * @Description 根据id删除
    * @author zhous
    * @date 2025/4/23 9:02
    */
    public void deleteById(String id, ICurrentUser currentUser) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" update ab_emit_bill_target_ratio_info set is_delete=:isDelete , update_by=:updateBy , update_date=:updateDate ");
        sql.append(" where 1=1 and id =:id ");
        params.put("id",id);
        params.put("isDelete","1");
        params.put("updateBy",currentUser.getLoginName());
        params.put("updateDate", DateUtils.getDate());
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
