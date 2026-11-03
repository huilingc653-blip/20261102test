package com.bytter.finance.domain.bond.dao;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.finance.domain.bond.entity.CmsBondIssueInfo;
import com.bytter.finance.domain.bond.vo.CmsBondIssueInfoVo;
import com.bytter.finance.domain.bond.vo.CmsBondRegisterInfoVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.dto.GuaContractCmsBondDTO;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 债券发行dao
 *
 * @author ouyj
 */
@Repository
public class CmsBondIssueInfoDao extends BaseDao<CmsBondIssueInfo, String> {
    /**
     * 获取公共查询sql
     *
     * @param flag
     *            是否发行查询
     * @return 公共查询sql
     */
    private StringBuilder getCommonSql(boolean flag) {
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as id, i.id as issueId, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName, r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDate, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk,");
        sql.append(" r.IS_TERRITORY as isTerritory, ");
        if (flag) {
            sql.append(" r.STATUS as status, ");
        } else {
            sql.append(" case when i.STATUS is null then 100 else i.STATUS end as status, ");
        }
        sql.append(" r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName, ");
        sql.append(" i.BOND_ISSUE_NUM as bondIssueNum ");
        sql.append(" from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.REGISTER_ID = r.id ");
        return sql;
    }
    /**
     * 获取审批查询sql
     *
     * @return 公共查询sql
     */
    private StringBuilder getApproveCommonSql() {
        StringBuilder sql = new StringBuilder(
                "SELECT r.ID as registerId, i.id as id, r.TYPE_ID AS typeId, r.CORP_ID AS corpId, r.CORP_NAME AS corpName,");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_CUR as bondCur, r.CUR_NAME as bondCurName,r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(
                " r.BOND_CREDIT_RATE as bondCreditRate, r.DEBTOR_CREDIT_RATE as debtorCreditRate, r.CONVERT_START_DATE as convertStartDate, ");
        sql.append(
                " r.CONVERT_END_DATE as convertEndDate, r.CONVERT_RATE as convertRate, r.PURPOSE as purpose, r.RMK as rmk,");
        sql.append(" r.IS_TERRITORY as isTerritory, case when i.STATUS is null then 0 else i.STATUS end as status, ");
        sql.append(" r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName,  ");
        sql.append(" i.BOND_ISSUE_NUM as bondIssueNum ");
        sql.append(" from CMS_BOND_REGISTER_INFO r left join CMS_BOND_TYPE t on r.type_id = t.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on i.REGISTER_ID = r.id ");
        return sql;
    }
    /**
     * 获取债券发行列表数据
     *
     * @param input
     *            前端参数
     * @param corpList
     *            单位集合
     * @param flag
     *            是否发行查询
     * @return 债券发行列表数据
     */
    public DataTablesOutput getDataTable(DataTablesInput input, List<String> corpList, boolean flag) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getCommonSql(flag);
        sql.append(" where (i.is_delete = :isDelete or i.is_delete is null) ");
        sql.append(" and (r.is_delete = :isDelete or r.is_delete is null) ");
        sql.append(" AND (I.CREATE_DATE IN (SELECT MAX(s.CREATE_DATE) ");
        sql.append(" FROM CMS_BOND_ISSUE_INFO S ");
        sql.append(" LEFT JOIN CMS_BOND_REGISTER_INFO R ON S.REGISTER_ID = R.ID GROUP BY S.REGISTER_ID)");
        sql.append(" OR i.create_date IS NULL) ");
        String corpSql = SqlUtil.getInSqlByParams(corpList, "r.CORP_ID", params);
        sql.append(" and ").append(corpSql).append(" ");
        if (!flag) {
            sql.append(" and r.status = :status95 ");
            // 过滤审批通过数据
            params.put("status95", CmsConstant.BUSINESSSTATE_95);
        }
        // 查询未删除的债券发行
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondRegisterInfoVo.class, "", false, null, params);
    }
    /**
     * 获取债券发行明细列表数据
     *
     * @param input
     *            前端参数
     * @return 债券发行列表数据
     */
    public DataTablesOutput getIssueDtlDatatable(DataTablesInput input) {
        Map<String, Object> params = new HashMap<>(2);
        StringBuilder sql = new StringBuilder(
                "select i.BOND_ISSUE_NUM as bondIssueNum, i.LEAD_UNDERWRITER as leadUnderwriter, i.BATCH_NO as batchNo, ");
        sql.append(
                " i.ISSUE_MONEY as issueMoney, i.RMB_ISSUE_TOTAL as rmbIssueTotal, i.START_DATE as startDate, i.DUE_DATE as dueDate,");
        sql.append(" i.ISSUE_START_DATE as issueStartDate, i.ISSUE_END_DATE as issueEndDate, ");
        sql.append(" i.ISSUE_PRICE as issuePrice, i.ISSUE_RMK as issueRmk, i.STATUS as status");
        sql.append(" from cms_bond_issue_info i ");
        sql.append(" where i.register_id = :registerId and (i.is_delete = :isDelete or i.is_delete is null) ");
        params.put("registerId", input.getFormdata().get("registerId"));
        // 查询未删除的债券注册
        params.put("isDelete", CmsConstant.IS_FALSE);
        return getDynamicBySQL(input, sql.toString(), CmsBondIssueInfoVo.class, "", false, null, params);
    }
    /**
     * 债券发行审批列表数据
     *
     * @param input
     *            前端查询参数
     * @param businessIdList
     *            业务id集合
     * @return 债券发行审批列表数据
     */
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> businessIdList) {
        Map<String, Object> params = new HashMap<>(1);
        StringBuilder sql = getApproveCommonSql();
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(businessIdList) && businessIdList.size() > 0) {
            String businessSql = SqlUtil.getInSqlByParams(businessIdList, "i.id", params);
            sql.append(" and ").append(businessSql).append(" ");
        }
        String flowStatus = input.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and i.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(input, sql.toString(), CmsBondIssueInfoVo.class, "", false, null, params);
    }
    public CmsBondIssueInfoVo getByIssueId(String issueId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.id as id, i.bond_issue_num as bondIssueNum, i.batch_no as batchNo, ");
        sql.append(" i.lead_underwriter as leadUnderwriter, i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(" i.start_date as startDate, i.due_date as dueDate, i.repay_type as repayType, i.issue_start_date as issueStartDate, ");
        sql.append(" i.issue_end_date as issueEndDate, i.assign_start_date as assignStartDate, ");
        sql.append(" i.assign_end_date as assignEndDate, i.announcement_date as announcementDate, i.payment_date as paymentDate, ");
        sql.append(" i.issue_price as issuePrice, i.issue_scope as issueScope, i.issue_rmk as issueRmk, ");
        sql.append(" i.calcu_interest_type as calcuInterestType, i.is_float as isFloat, i.quote_date as quoteDate, ");
        sql.append(" i.interest_year_days as interestYearDays, i.is_leap_year as isLeapYear, i.leap_is_interest as leapIsInterest, ");
        sql.append(" i.interest_days as interestDays, i.fix_price_base as fixPriceBase, i.rate_deadline as rateDeadline, ");
        sql.append(" i.base_rate as baseRate, i.basic_point as basicPoint, i.face_value_rate as faceValueRate, ");
        sql.append(" i.pay_date as payDate, i.pay_frequency as payFrequency, i.assign_type as assignType, ");
        sql.append(" i.create_By as createBy, i.create_Date as createDate, i.update_By as updateBy, i.update_Date as updateDate, ");
        sql.append(" r.id as registerId, r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        //TODO 补充币种名称
        sql.append(" r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode,r.CUR_NAME as bondCurName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur, r.ISSUE_RMB_TOTAL as issueCurTotal ");
        sql.append(" from CMS_BOND_ISSUE_INFO i ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on i.register_id = r.id ");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and i.id= :issueId ");
            params.put("issueId", issueId);
        }
        sql.append(" order by i.create_Date desc ");
        return this.getBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfoVo.class);
    }

    public List<CmsBondIssueInfoVo> getRegisterById(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.id as id, i.bond_issue_num as bondIssueNum, i.batch_no as batchNo, ");
        sql.append(
                " i.lead_underwriter as leadUnderwriter, i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(
                " i.start_date as startDate, i.due_date as dueDate, i.repay_type as repayType, i.issue_start_date as issueStartDate, ");
        sql.append(" i.issue_end_date as issueEndDate, i.assign_start_date as assignStartDate, ");
        sql.append(
                " i.assign_end_date as assignEndDate, i.announcement_date as announcementDate, i.payment_date as paymentDate, ");
        sql.append(" i.issue_price as issuePrice, i.issue_scope as issueScope, i.issue_rmk as issueRmk, ");
        sql.append(" i.calcu_interest_type as calcuInterestType, i.is_float as isFloat, i.quote_date as quoteDate, ");
        sql.append(
                " i.interest_year_days as interestYearDays, i.is_leap_year as isLeapYear, i.leap_is_interest as leapIsInterest, ");
        sql.append(
                " i.interest_days as interestDays, i.fix_price_base as fixPriceBase, i.rate_deadline as rateDeadline, ");
        sql.append(" i.base_rate as baseRate, i.basic_point as basicPoint, i.face_value_rate as faceValueRate, ");
        sql.append(" i.pay_date as payDate, i.pay_frequency as payFrequency, i.assign_type as assignType, ");
        sql.append(
                " i.create_By as createBy, i.create_Date as createDate, i.update_By as updateBy, i.update_Date as updateDate, ");
        sql.append(
                " r.id as registerId, r.CORP_ID as corpId, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        //TODO 补充币种名称
        sql.append(
                " r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.CUR_NAME as bondCurName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur, r.ISSUE_RMB_TOTAL as issueCurTotal ");
        sql.append(" from CMS_BOND_ISSUE_INFO i ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on i.register_id = r.id ");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" order by i.create_Date desc ");
        List<CmsBondIssueInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfoVo.class);
        return list;
    }
    public List<CmsBondIssueInfoVo> listValidBondIssueNum(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.id as id, i.register_id as registerId, i.bond_issue_num as bondIssueNum, ");
        sql.append(" i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(" plan.is_save as isSave, plan.status as planStatus ");
        sql.append(" from CMS_BOND_ISSUE_INFO i ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on i.id = plan.issue_id ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" order by i.create_Date desc ");
        List<CmsBondIssueInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfoVo.class);
        return list;
    }
    public List<CmsBondIssueInfoVo> listValidBondIssueByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select i.id as id, i.register_id as registerId, i.bond_issue_num as bondIssueNum, ");
        sql.append(" i.issue_money as issueMoney, i.rmb_issue_total as rmbIssueTotal, ");
        sql.append(
                " i.issue_start_date as issueStartDate,i.issue_end_date as issueEndDate, i.issue_price as issuePrice,i.issue_rmk issueRmk ");
        sql.append(" from CMS_BOND_ISSUE_INFO i ");
        sql.append(" where 1 = 1 and (i.is_delete = :isDelete or i.is_delete is null) ");
        Map<String, String> params = new HashMap<String, String>();
        // 查询未删除的债券发行
        params.put("isDelete", CmsConstant.IS_FALSE);
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and i.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" and i.status = :status95 ");
        // 审批通过数据
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        sql.append(" order by i.create_Date desc ");
        List<CmsBondIssueInfoVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfoVo.class);
        return list;
    }
    /**
     * 根据发行编号查询对应数据
     *
     * @param bondIssueNum
     *            发行编号
     * @param id
     *            id
     * @return 发行数据
     */
    public List<CmsBondIssueInfo> getBondByBondIssueNum(String bondIssueNum, String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<String, String>(4);
        sql.append(" select i.id as id from CMS_BOND_ISSUE_INFO i where i.bond_issue_num = :bondIssueNum ");
        sql.append(" and (i.is_delete = :isDelete or i.is_delete is null) ");
        if (id != null) {
            sql.append(" and i.id != :id ");
            params.put("id", id);
        }
        params.put("bondIssueNum", bondIssueNum);
        params.put("isDelete", CmsConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfo.class);
    }
    /**
     * 根据债券注册代码查询审批通过的发行数据
     *
     * @param bondCode
     *            债券注册代码
     * @param type
     *            债券类型
     * @return 发行数据
     */
    public List<CmsBondIssueInfo> listByBondCode(String bondCode, String type) {
        StringBuilder sql = new StringBuilder();
        Map<String, String> params = new HashMap<>(4);
        sql.append(" select i.id as id from cms_bond_register_info r left join CMS_BOND_ISSUE_INFO i ");
        sql.append(" on r.id = i.register_id left join cms_bond_type t on r.type_id = t.id ");
        sql.append(" where r.bond_code = :bondCode and i.status = :status95  ");
        sql.append(" and (r.is_delete = :isDelete or r.is_delete is null) ");
        if (StringUtils.isNotEmpty(type)) {
            sql.append(" and t.BOND_NAME = :type ");
            params.put("type", type);
        }
        params.put("bondCode", bondCode);
        params.put("status95", CmsConstant.BUSINESSSTATE_95);
        params.put("isDelete", CmsConstant.IS_FALSE);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsBondIssueInfo.class);
    }
    public void updateStatus(List<String> issueIds, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        sql.append(" update CMS_BOND_ISSUE_INFO i set i.biz_status = :bizStatus where 1=1 and ");
        sql.append(getSQLIn(issueIds, 900, "i.id"));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("bizStatus", bizStatus);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    public List<GuaContractCmsBondDTO> getGuaCmsBond() {
        StringBuilder sql = new StringBuilder();
        sql.append("select info.BOND_ISSUE_NUM as bondIssueNum, r.BOND_CUR as oriCurId, r.CUR_NAME as oriCurName, info.issue_money as oriMoney, ");
        sql.append(" info.issue_start_date as issueStartDate, info.issue_end_date as issueEndDate, info.id as id ");
        sql.append(" from CMS_BOND_ISSUE_INFO info ");
        sql.append("  left join CMS_BOND_REGISTER_INFO r on info.register_id = r.id  ");
        sql.append(" where info.IS_HOOK_GUARANTEE = '1'");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsBondDTO.class);
    }
}
