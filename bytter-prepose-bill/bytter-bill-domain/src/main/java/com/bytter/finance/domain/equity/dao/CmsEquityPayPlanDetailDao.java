package com.bytter.finance.domain.equity.dao;
import com.bytter.finance.domain.equity.entity.CmsEquityPayPlanDetail;
import com.bytter.finance.domain.equity.vo.CmsEquityPayPlanDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zhneghj
 * @version 1.0
 * @Title: 缴付/过户计划明细Dao
 * @Description: 缴付/过户计划明细Dao
 * @date 2022-06-21
 */
@Repository
public class CmsEquityPayPlanDetailDao extends BaseDao<CmsEquityPayPlanDetail, String> {
    /**
     * 根据融资编号查询计划明细
     *
     * @param planId
     *            计划主表id
     * @param equityCode
     *            融资编号
     * @return 计划明细
     */
    public List<CmsEquityPayPlanDetailVO> listPayPlanDetailByEquityCodeOrPlanId(String planId, String equityCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select ppd.id as id, ppd.PLAN_ID as planId,ppd.PAY_DATE as payDate,ppd.equity_Code as equityCode,");
        sql.append(" ppd.PAY_MONEY as payMoney,ppd.RE_PAY_MONEY as rePayMoney,ppd.ACCOUNTED as accounted,");
        sql.append(" ppd.ASS_NAME as assName,ppd.IS_PAY as isPay,ppd.VALID_SIGN as validSign,");
        sql.append(
                " ppd.ADD_OR_UPDATE as addOrUpdate,ppd.RMK as rmk, ppd.CREATE_DATE as createDate,ppd.UPDATE_DATE as updateDate ");
        sql.append(" from CMS_EQUITY_PAY_PLAN_DETAIL ppd");
        sql.append(" left join CMS_EQUITY_PAY_PLAN pp on pp.id=ppd.plan_id");
        sql.append(" where 1=1");
        if (StringUtils.isNotEmpty(planId)) {
            sql.append(" and ppd.plan_id = :planId ");
            params.put("planId", planId);
        }
        if (StringUtils.isNotEmpty(equityCode)) {
            sql.append(" and pp.EQUITY_CODE = :equityCode ");
            params.put("equityCode", equityCode);
        }
        // sql.append(" order by ppd.PAY_DATE ");
        sql.append(" order by ppd.RE_PAY_MONEY desc");
        return listBySQLAliasToBean(sql.toString(), params, CmsEquityPayPlanDetailVO.class);
    }
    /**
     * 根据计划主表id删除计划明细表数据
     *
     * @param planId
     *            计划主表id
     * @param detailIds
     *            计划明细表id
     * @return 结果数
     */
    public int deletePayPlanDetailByPlanId(String planId, List<String> detailIds) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append(" DELETE CmsEquityPayPlanDetail where planId=:planId ");
        if (!CollectionUtils.isEmpty(detailIds)) {
            sql.append("  and id not in (:detailIds) ");
            params.put("detailIds", detailIds);
        }
        params.put("planId", planId);
        return this.batchExecute(sql.toString(), params);
    }
}
