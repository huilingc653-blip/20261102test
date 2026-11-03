package com.bytter.finance.domain.bond.dao;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlanDtl;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanDtlVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 债券还款计划明细表
 * @date 2022-06-06 10:07:46
 */
@Repository
public class CmsBondRepayPlanDtlDao extends BaseDao<CmsBondRepayPlanDtl, String> {
    public CmsBondRepayPlanDtlVo getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pdtl.id as id, pdtl.plan_id as planId, pdtl.repay_date as repayDate, pdtl.pay_id as payId, ");
        sql.append(
                " pdtl.plan_repay_date as planRepayDate, pdtl.repay_money as repayMoney, pdtl.remain_money as remainMoney, ");
        sql.append(" pdtl.repay_amount as repayAmount, pdtl.repay_rate as repayRate, ");
        sql.append(" pdtl.is_repay as isRepay, pdtl.valid_sign as validSign, pdtl.rmk as rmk, ");
        sql.append(
                " pdtl.create_by as createBy, pdtl.create_date as createDate, pdtl.update_by as updateBy, pdtl.update_date as updateDate, ");
        sql.append(" i.id as issueId, i.bond_issue_num as bondIssueNum, i.issue_money as issueMoney ");
        sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and pdtl.id= :id");
            params.put("id", id);
        }
        List<CmsBondRepayPlanDtlVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanDtlVo.class);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据债券id查询还款计划明细
     *
     * @param registerId
     *            债券id
     * @return 还款计划明细
     */
    public List<CmsBondRepayPlanDtlVo> getByRegisterId(String registerId) {
        if (!StringUtils.isNotEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pdtl.id as id, pdtl.plan_id as planId, pdtl.repay_date as repayDate, pdtl.pay_id as payId,  ");
        sql.append(
                " pdtl.plan_repay_date as planRepayDate, pdtl.repay_money as repayMoney, pdtl.remain_money as remainMoney, ");
        sql.append(" pdtl.repay_amount as repayAmount, pdtl.repay_rate as repayRate, ");
        sql.append(" pdtl.is_repay as isRepay, pdtl.valid_sign as validSign, pdtl.rmk as rmk, ");
        sql.append(
                " pdtl.create_by as createBy, pdtl.create_date as createDate, pdtl.update_by as updateBy, pdtl.update_date as updateDate, ");
        sql.append(" i.id as issueId, i.bond_issue_num as bondIssueNum, i.issue_money as issueMoney, ");
        sql.append(" case when (exists(select 1 from CMS_BOND_PAY p where p.id = pdtl.pay_id and p.status = 11)) then 11 else 0 end as payStatus ");
        sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 and pdtl.valid_sign <> '" + CmsConstant.DELETED + "' ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id=:registerId");
            params.put("registerId", registerId);
        }
        sql.append(" order by pdtl.repay_date ");
        List<CmsBondRepayPlanDtlVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanDtlVo.class);
        return list;
    }
    /**
     * 根据债券发行id查询还款计划明细
     *
     * @param issueId
     *            债券发行id
     * @return 还款计划明细
     */
    public List<CmsBondRepayPlanDtlVo> getByIssueId(String issueId) {
        if (!StringUtils.isNotEmpty(issueId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pdtl.id as id, pdtl.plan_id as planId, pdtl.repay_date as repayDate, pdtl.pay_id as payId,  ");
        sql.append(
                " pdtl.plan_repay_date as planRepayDate, pdtl.repay_money as repayMoney, pdtl.remain_money as remainMoney, ");
        sql.append(" pdtl.repay_amount as repayAmount, pdtl.repay_rate as repayRate, ");
        sql.append(" pdtl.is_repay as isRepay, pdtl.valid_sign as validSign, pdtl.rmk as rmk, ");
        sql.append(
                " pdtl.create_by as createBy, pdtl.create_date as createDate, pdtl.update_by as updateBy, pdtl.update_date as updateDate, ");
        sql.append(" i.id as issueId, i.bond_issue_num as bondIssueNum ");
        sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 and pdtl.valid_sign <> '" + CmsConstant.DELETED + "' ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and i.id=:issueId");
            params.put("issueId", issueId);
        }
        List<CmsBondRepayPlanDtlVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanDtlVo.class);
        return list;
    }
    public List<CmsBondRepayPlanDtlVo> getByPlanId(String planId) {
        if (!StringUtils.isNotEmpty(planId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pdtl.id as id, pdtl.plan_id as planId, pdtl.repay_date as repayDate, i.bond_issue_num as bondIssueNum, ");
        sql.append(
                " pdtl.plan_repay_date as planRepayDate, pdtl.repay_money as repayMoney, pdtl.remain_money as remainMoney, ");
        sql.append(" pdtl.repay_amount as repayAmount, pdtl.repay_rate as repayRate, pdtl.pay_id as payId, ");
        sql.append(" pdtl.is_repay as isRepay, pdtl.valid_sign as validSign, pdtl.rmk as rmk, ");
        sql.append(
                " pdtl.create_by as createBy, pdtl.create_date as createDate, pdtl.update_by as updateBy, pdtl.update_date as updateDate ");
        sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 and pdtl.valid_sign <> '" + CmsConstant.DELETED + "' ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(planId)) {
            sql.append(" and pdtl.plan_id=:planId");
            params.put("planId", planId);
        }
        List<CmsBondRepayPlanDtlVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanDtlVo.class);
        return list;
    }
    public CmsBondRepayPlanDtlVo getByPayId(String payId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pdtl.id as id, pdtl.plan_id as planId, pdtl.repay_date as repayDate, pdtl.pay_id as payId, ");
        sql.append(
                " pdtl.plan_repay_date as planRepayDate, pdtl.repay_money as repayMoney, pdtl.remain_money as remainMoney, ");
        sql.append(" pdtl.repay_amount as repayAmount, pdtl.repay_rate as repayRate, ");
        sql.append(" pdtl.is_repay as isRepay, pdtl.valid_sign as validSign, pdtl.rmk as rmk, ");
        sql.append(
                " pdtl.create_by as createBy, pdtl.create_date as createDate, pdtl.update_by as updateBy, pdtl.update_date as updateDate, ");
        sql.append(" i.id as issueId, i.bond_issue_num as bondIssueNum, i.issue_money as issueMoney ");
        sql.append(" from CMS_BOND_REPAY_PLAN_DTL pdtl ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(payId)) {
            sql.append(" and pdtl.pay_id= :payId");
            params.put("payId", payId);
        }
        List<CmsBondRepayPlanDtlVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanDtlVo.class);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
    /**
     * 根据单号删除之前的还款计划子表(逻辑删除)
     */
    public void deleteBondRepayPlanDtl(List<String> planIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update CMS_BOND_REPAY_PLAN_DTL set valid_Sign=:validSign where is_repay=:isRepay ");
        sql.append(" and ").append(getSQLIn(planIds, 900, "plan_id"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isRepay", CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO);
        params.put("validSign", String.valueOf(CmsConstant.DELETED));
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /**
     * 根据id修改还款处理id
     *
     * @param id
     *            id
     * @param payId
     *            还款处理id
     */
    public void updatePayId(String id, String payId) {
        String sql = "update CMS_BOND_REPAY_PLAN_DTL set pay_id = :payId where id = :id";
        Map<String, Object> params = new HashMap<>(2);
        params.put("id", id);
        params.put("payId", payId);
        this.batchSQLExecute(sql, params);
    }
}