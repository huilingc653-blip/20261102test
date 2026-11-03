package com.bytter.guarantee.domain.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.domain.entity.GuaranteePlan;
import com.bytter.guarantee.domain.vo.RepaymentScheduleVO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class GuaranteePlanDao extends BaseDao<GuaranteePlan, String> {
    /**
     * 根据单号删除之前的还款计划子表(修改valid_Sign 为2)
     *
     * @param billCode
     *            单号
     */
    public void deleteRepaymentSchedule(String billCode, String ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "delete from  cme_guarantee_plan   ";
        if (StringUtils.isNotEmpty(billCode)) {
            params.put("billCode", billCode);
            sql += " where bill_code=:billCode  ";
        }
        if (StringUtils.isNotEmpty(ids)) {
            params.put("ids", ids);
            sql += " where ids=:ids  ";
        }
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 根据还款计划批次号修改还款计划对应的业务单据id
     *
     * @param billCode
     *            单号
     */
    public void updateBillCodeByIds(String ids, String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = "update cme_guarantee_plan set bill_code =:billCode where ids=:ids   ";
        params.put("ids", ids);
        params.put("billCode", billCode);
        createSQLQuery(sql, params).executeUpdate();
    }
    /**
     * 根据id获取还款计划
     *
     * @param id
     * @return
     */
    public List<RepaymentScheduleVO> listRepayMentScheduleById(String id, String billNo) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sub.id as id,sub.bill_code as billCode,sub.predice_repay_date as prediceRepayDate, ");
        sql.append(
                " sub.repayment_money as repaymentMoney,sub.repayment_ist as repaymentIst,sub.current_repay_money as currentRepayMoney, ");
        sql.append(" sub.remark as remark,sub.IS_REPAY as isRepay ");
        sql.append(" from cme_guarantee_plan sub  ");
        sql.append(" where 1=1 ");
        if (StringUtils.isNotEmpty(billNo)) {
            sql.append(" and bill_code=:id ");
            params.put("id", billNo);
        } else {
            sql.append(" and ids=:id ");
            params.put("id", id);
        }
        sql.append(" order by prediceRepayDate");
        return listBySQLAliasToBean(sql.toString(), params, RepaymentScheduleVO.class);
    }
    /**
     * <br>
     * <b>功能：</b>查询付款计划（企e金服接口）<br>
     * <b>作者：</b>黄茜<br>
     * <b>日期：</b>2018-11-25<br>
     *
     * @param
     * @param billCode
     * @return
     */
    public String getPlanIds(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select ids from cme_guarantee_plan where bill_Code=:billCode ");
        Map<String, Object> params = new HashMap<>();
        params.put("billCode", billCode);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        String ids = "";
        if (!EmptyUtils.isEmpty(result)) {
            ids = (String) result;
        }
        return ids;
    }
}
