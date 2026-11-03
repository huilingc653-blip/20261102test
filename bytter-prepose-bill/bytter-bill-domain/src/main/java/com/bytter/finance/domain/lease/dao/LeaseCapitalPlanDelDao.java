package com.bytter.finance.domain.lease.dao;
import com.bytter.finance.domain.lease.entity.LeaseCapitalPlanDel;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanDelVO;
import com.bytter.finance.domain.lease.vo.LeaseCapitalPlanQueryVO;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
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
 * @Description: 租赁计划明细表
 * @date 2022-04-02 14:22:49
 */
@Repository
public class LeaseCapitalPlanDelDao extends BaseDao<LeaseCapitalPlanDel, String> {
    public DataTablesOutput<LeaseCapitalPlanQueryVO> getDataTablesQuery(DataTablesInput dataTablesInput, List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.lease_plan_id as leasePlanId, p.pay_date as payDate,p.pay_plan_date as payPlanDate, ");
        sql.append(
                " p.lease_monry as leaseMonry, p.lease_interest as leaseInterest, p.this_money as thisMoney, p.this_cost as thisCost,");
        sql.append(
                " p.bail_money as bailMoney,p.surplus_money as surplusMoney, p.is_repay as isRepay, p.rmk as rmk, plan.status as status, ");
        sql.append(
                " p.create_by as createBy, p.create_date as createDate, p.update_by as updateBy, p.update_date as updateDate, ");
        sql.append(
                " l.id as leaseId,l.lessor as lessor,l.lease_type as leaseType, l.corp_id as corpId,l.lease_no as leaseNo, ");
        sql.append(
                " l.lease_code as leaseCode, l.supplier as supplier, l.pay_code as payCode,l.ori_curid as oriCurid, l.ori_money as oriMoney, ");
        //TODO 补充单位币种名称
        sql.append(" l.corp_name as corpName, l.ori_cur_name as oriCurName, ");
        sql.append(
                " l.rmb_money as rmbMoney,l.begin_date as beginDate, l.end_date as endDate, l.status as leaseStatus, l.biz_status as bizStatus ");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
        sql.append(" inner join CMS_LEASE_CAPITAL_PLAN plan on p.lease_plan_id = plan.id ");
        sql.append(" inner join CMS_FINANCING_LEASE_INFO l on plan.lease_id = l.id ");
        sql.append(" where 1 = 1 and p.valid_sign <> '" + CmsConstant.DELETED + "' ");
        sql.append(" and plan.status <> '" + CmsConstant.BACK + "' ");
        Map<String, Object> params = new HashMap<>();
        String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "l.corp_id", params);
        sql.append(" and ").append(corpAuthSql).append(" ");
        return getDynamicBySQL(dataTablesInput, sql.toString(), LeaseCapitalPlanQueryVO.class, "", false, params);
    }
    public LeaseCapitalPlanDelVO getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, plan.lease_id as leaseId,p.lease_plan_id as leasePlanId, p.pay_date as payDate,p.pay_plan_date as payPlanDate, ");
        sql.append(
                " p.lease_monry as leaseMonry, p.lease_interest as leaseInterest, p.this_money as thisMoney, p.this_cost as thisCost,");
        sql.append(" p.bail_money as bailMoney,p.surplus_money as surplusMoney, p.is_repay as isRepay, p.rmk as rmk, ");
        sql.append(
                " p.create_by as createBy, p.create_date as createDate, p.update_by as updateBy, p.update_date as updateDate ");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
        sql.append(" left join CMS_LEASE_CAPITAL_PLAN plan on p.lease_plan_id = plan.id");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and p.id= :id");
            params.put("id", id);
        }
        List<LeaseCapitalPlanDelVO> list = listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanDelVO.class);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        } else {
            return null;
        }
    }
    public List<LeaseCapitalPlanDelVO> getByLeasePlanId(String leasePlanId) {
        if (!StringUtils.isNotEmpty(leasePlanId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.lease_plan_id as leasePlanId, p.pay_date as payDate,p.pay_plan_date as payPlanDate, ");
        sql.append(
                " p.lease_monry as leaseMonry, p.lease_interest as leaseInterest, p.this_money as thisMoney, p.this_cost as thisCost,");
        sql.append(" p.bail_money as bailMoney,p.surplus_money as surplusMoney, p.is_repay as isRepay, p.rmk as rmk, ");
        sql.append(
                " p.create_by as createBy, p.create_date as createDate, p.update_by as updateBy, p.update_date as updateDate ");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
        sql.append(" where 1 = 1 and p.valid_sign <> '" + CmsConstant.DELETED + "' ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(leasePlanId)) {
            sql.append(" and p.lease_plan_id=:leasePlanId");
            params.put("leasePlanId", leasePlanId);
        }
        sql.append(" order by p.pay_date");
        List<LeaseCapitalPlanDelVO> list = listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanDelVO.class);
        return list;
    }
    /**
     * 根据租赁id查询租金计划明细
     *
     * @param leaseId
     *            租赁id
     * @return 租金计划明细
     */
    public List<LeaseCapitalPlanDelVO> getInfoByLeaseId(String leaseId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.lease_plan_id as leasePlanId, p.pay_date as payDate,p.pay_plan_date as payPlanDate, ");
        sql.append(
                " p.lease_monry as leaseMonry, p.lease_interest as leaseInterest, p.this_money as thisMoney, p.this_cost as thisCost,");
        sql.append(" p.bail_money as bailMoney,p.surplus_money as surplusMoney, p.is_repay as isRepay, p.rmk as rmk, ");
        sql.append(
                " p.create_by as createBy, p.create_date as createDate, p.update_by as updateBy, p.update_date as updateDate ");
        sql.append(" from CMS_LEASE_CAPITAL_PLANDEL p ");
        sql.append(" left join CMS_LEASE_CAPITAL_PLAN plan on p.lease_plan_id = plan.id");
        sql.append(" where 1 = 1 and p.valid_sign <> '" + CmsConstant.DELETED + "' ");
        Map<String, String> params = new HashMap<String, String>();
        params.put("validSign", String.valueOf(CmsConstant.DELETED));
        if (StringUtils.isNotBlank(leaseId)) {
            sql.append(" and plan.lease_id= :leaseId");
            params.put("leaseId", leaseId);
        }
        sql.append(" order by p.pay_date");
        List<LeaseCapitalPlanDelVO> list = listBySQLAliasToBean(sql.toString(), params, LeaseCapitalPlanDelVO.class);
        return list;
    }
    /**
     * 根据单号删除之前的还款计划子表(逻辑删除)
     */
    public void deleteLeaseCapitalPlanDel(String leasePlanId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isRepay", CmsConstant.REPAYMENT_SCHEDULE_ISREPAYNO);
        params.put("leasePlanId", leasePlanId);
        params.put("validSign", String.valueOf(CmsConstant.DELETED));
        String sql = "update CMS_LEASE_CAPITAL_PLANDEL set valid_Sign=:validSign where lease_plan_id=:leasePlanId and is_repay=:isRepay ";
        createSQLQuery(sql, params).executeUpdate();
    }
}
