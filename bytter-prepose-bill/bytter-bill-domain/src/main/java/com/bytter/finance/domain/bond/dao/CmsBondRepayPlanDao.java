package com.bytter.finance.domain.bond.dao;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.finance.domain.bond.entity.CmsBondRepayPlan;
import com.bytter.finance.domain.bond.vo.CmsBondRepayPlanVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 债券还款计划主表
 * @date 2022-06-06 10:07:46
 */
@Repository
public class CmsBondRepayPlanDao extends BaseDao<CmsBondRepayPlan, String> {
    /**
     * 筛选待登记的，并且已经发行的债券信息
     *
     * @param corpAuthList
     *            单位权限
     * @return 发行信息集合
     */
    public List<CmsBondRepayPlanVo> getByRepayPlan(List<String> corpAuthList, String isSave) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.REPAY_TYPE as repayType, ");
        sql.append(" p.is_save as isSave, p.status as status, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(" i.id as issueId, i.status as issueStatus, ");
        sql.append(" r.id as registerId, r.corp_id as corpId ");
        sql.append(" from CMS_BOND_REPAY_PLAN p ");
        sql.append(" RIGHT join CMS_BOND_ISSUE_INFO i on p.ISSUE_ID = i.id ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on i.REGISTER_ID = r.id ");
        sql.append(" where 1 = 1 and r.status = '95' and i.status = '95' ");
        sql.append(" and p.is_save= :isSave ");
        Map<String, Object> params = new HashMap<>();
        params.put("isSave", isSave);
        sql.append(" and ").append(getSQLIn(corpAuthList, 900, "r.corp_id"));
        return listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanVo.class);
    }
    /**
     * 筛选已经添加了还款计划的债券信息
     *
     * @return 筛选添加还款计划的数据
     */
    public List<CmsBondRepayPlanVo> getAllDataByPlan(List<String> corpAuthList) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.REPAY_TYPE as repayType, ");
        sql.append(" p.is_save as isSave,p.status as status, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(" i.id as issueId, i.status as issueStatus, ");
        sql.append(" r.id as registerId, r.corp_id as corpId ");
        sql.append(" from CMS_BOND_REPAY_PLAN p ");
        sql.append(" RIGHT join CMS_BOND_ISSUE_INFO i on p.ISSUE_ID = i.id ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on i.REGISTER_ID = r.id ");
        sql.append(" where 1 = 1 and r.status not in('0','11') and i.status not in('0','11') ");
        //剔除已还完款的
        sql.append(" and  exists(select 1 from CMS_BOND_REPAY_PLAN_DTL d where p.id = d.PLAN_ID and d.is_repay = '0' and d.valid_sign !=-2)  ");
        Map<String, Object> params = new HashMap<>();
        sql.append(" and ").append(getSQLIn(corpAuthList, 900, "r.corp_id"));
        return listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanVo.class);
    }
    public List<CmsBondRepayPlanVo> getInfoById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, ");
        sql.append(" p.rate as rate, p.repay_cycle as repayCycle, p.pay_way as payWay, p.pay_money as payMoney, ");
        sql.append(" p.status as status, p.is_savesign as isSaveSign,");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_BOND_REPAY_PLAN p ");
        sql.append(" where 1 = 1 and p.ID=:id ");
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanVo.class);
    }
    /**
     * 根据债券id查询还款计划
     *
     * @param registerId
     *            债劵注册id
     * @return 还款计划
     */
    public List<CmsBondRepayPlanVo> getRegisterById(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.issue_id as issueId, p.corp_id as corpId, p.repay_type as repayType, p.pay_type as payType, ");
        sql.append(
                " p.is_save as isSave, p.repay_cycel as repayCycle, p.repay_start_date as repayBeginDate, p.status as status, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_BOND_REPAY_PLAN p ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and p.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" order by p.create_Date desc ");
        List<CmsBondRepayPlanVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanVo.class);
        return list;
    }
    /**
     * 根据债券发行id查询还款计划
     *
     * @param issueId
     *            债券发行id
     * @return 还款计划主表
     */
    public CmsBondRepayPlanVo getByIssueId(String issueId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select p.id as id, p.issue_id as issueId, p.register_id as registerId, p.corp_id as corpId, p.repay_type as repayType, p.pay_type as payType, ");
        sql.append(
                " p.is_save as isSave, p.repay_cycel as repayCycle, p.repay_start_date as repayBeginDate, p.status as status, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_BOND_REPAY_PLAN p ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and p.issue_id= :issueId ");
            params.put("issueId", issueId);
        }
        sql.append(" order by p.create_Date desc ");
        List<CmsBondRepayPlanVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondRepayPlanVo.class);
        if (ListUtils.isNullOrEmpty(list)) {
            return new CmsBondRepayPlanVo();
        }
        return list.get(0);
    }
    /**
     * 功能介绍 : 申请审批列表表查询<br>
     */
    public DataTablesOutput getDataInfoApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select plan.id as id, ");
        sql.append(
                " plan.create_By as createBy, plan.create_Date as createDate, plan.update_By as updateBy, plan.update_Date as updateDate, ");
        sql.append(" r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        sql.append(
                " r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur,r.CUR_NAME as bondCurName, r.ISSUE_RMB_TOTAL as issueCurTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDte, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk, ");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.STATUS as status, r.IS_DELETE as isDelete, r.ATTACHMENT_ID as attachmentId, ");
        sql.append(" i.bond_issue_num as bondIssueNum, i.batch_no as batchNo, ");
        sql.append(
                " i.lead_underwriter as leadUnderwriter, i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(" i.start_date as startDate, i.due_date as dueDate, i.repay_type as repayType ");
        sql.append(" from CMS_BOND_REPAY_PLAN plan ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on r.id = plan.register_id");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "plan.id")).append(" ");
        }
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and plan.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondRepayPlanVo.class, null, false);
    }
    /**
     * 功能介绍 : 补录审批查询列表<br>
     */
    public DataTablesOutput getDataUpdateApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select plan.id as id, ");
        sql.append(
                " plan.create_By as createBy, plan.create_Date as createDate, plan.update_By as updateBy, plan.update_Date as updateDate, ");
        sql.append(" r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        sql.append(
                " r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName,r.ISSUE_RMB_TOTAL as issueCurTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDte, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk, ");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.STATUS as status, r.IS_DELETE as isDelete, r.ATTACHMENT_ID as attachmentId, ");
        sql.append(" i.bond_issue_num as bondIssueNum, i.batch_no as batchNo, ");
        sql.append(
                " i.lead_underwriter as leadUnderwriter, i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(" i.start_date as startDate, i.due_date as dueDate, i.repay_type as repayType ");
        sql.append(" from CMS_BOND_REPAY_PLAN plan ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on r.id = plan.register_id");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.id = plan.issue_id ");
        sql.append(" where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "plan.id")).append(" ");
        }
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and plan.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondRepayPlanVo.class, null, false);
    }
}
