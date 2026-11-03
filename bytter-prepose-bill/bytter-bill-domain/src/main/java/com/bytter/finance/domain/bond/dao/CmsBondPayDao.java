package com.bytter.finance.domain.bond.dao;
import com.bytter.core.constant.NetbankConstant;
import com.bytter.finance.domain.bond.entity.CmsBondPay;
import com.bytter.finance.domain.bond.vo.CmsBondPayVo;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.workflow.api.IWorkFlowService;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author mof
 * @version 1.0
 * @Title: Dao
 * @Description: 债券还款表
 * @date 2022-06-07 10:07:46
 */
@Repository
public class CmsBondPayDao extends BaseDao<CmsBondPay, String> {
    public DataTablesOutput getDataTablesUnion(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        //TODO 补充状态字段
        sql.append(
                " select r.id as registerId, r.CORP_ID as corpId,r.status as status, r.CORP_NAME as corpName, r.TYPE_ID as typeId, t.BOND_NAME as bondTypeName, ");
        sql.append(
                " r.ISSUE_NAME as issueName, r.BOND_CODE as bondCode, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_SHORT_NAME as bondShortName, r.BOND_CUR as bondCur,r.CUR_NAME as bondCurName,r.ISSUE_RMB_TOTAL as issueCurTotal ");
        sql.append(" from CMS_BOND_REGISTER_INFO r ");
        sql.append(" left join CMS_BOND_TYPE t on r.TYPE_ID = t.id ");
        sql.append(" where 1 = 1 and r.id in (select i.register_id from CMS_BOND_ISSUE_INFO i where i.status ='95' )");
        sql.append(" and r.status = '95'");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondPayVo.class, "", false);
    }
    public List<CmsBondPayVo> getById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.register_id as registerId, p.issue_id as issueId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(" p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.repay_money as repayMoney, p.is_pay_interest as isPayInterest, p.interest as interest, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(" i.bond_issue_num as bondIssueNum, i.issue_money as issueMoney ");
        sql.append(" from CMS_BOND_PAY p ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on p.issue_id = i.id ");
        sql.append(" where 1 = 1 ");
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotBlank(id)) {
            sql.append(" and p.ID= :id ");
            params.put("id", id);
        }
        return listBySQLAliasToBean(sql.toString(), params, CmsBondPayVo.class);
    }
    /**
     * 根据债券主表id和是否付息查询债券还款信息
     *
     * @param registerId
     *            债券主表id
     * @param isPayInterest
     *            是否付息
     * @return 债券还款信息
     */
    public List<CmsBondPayVo> getByRegisterId(String registerId, String isPayInterest) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.register_id as registerId, p.issue_id as issueId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(" p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.repay_money as repayMoney, p.is_pay_interest as isPayInterest, p.interest as interest, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_BOND_PAY p ");
        sql.append(" where 1 = 1 ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and p.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        if (StringUtils.isNotBlank(isPayInterest)) {
            sql.append(" and p.is_pay_interest= :isPayInterest ");
            params.put("isPayInterest", isPayInterest);
        }
        sql.append(" order by p.create_Date desc ");
        List<CmsBondPayVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondPayVo.class);
        return list;
    }
    /**
     * 根据债券主表id查询债券还款详情
     *
     * @param registerId
     *            债券主表id
     * @return 债券还款信息
     */
    public List<CmsBondPayVo> getBondPayByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.register_id as registerId, p.issue_id as issueId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(" p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.repay_money as repayMoney, p.is_pay_interest as isPayInterest, p.interest as interest, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(" i.bond_issue_num as bondIssueNum, r.purpose as purpose ");
        sql.append(" from CMS_BOND_PAY p ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on p.issue_id = i.id ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on p.register_id = r.id ");
        sql.append(" where 1 = 1 and p.status != -2 ");
        sql.append(" and p.is_pay_interest is not null");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and p.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" order by p.pay_date ");
        List<CmsBondPayVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondPayVo.class);
        return list;
    }
    /**
     * 根据债券主表id查询债券还息详情
     *
     * @param registerId
     *            债券主表id
     * @return 债券还息信息
     */
    public List<CmsBondPayVo> getBondPayInterestByRegisterId(String registerId) {
        if (EmptyUtils.isEmpty(registerId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.register_id as registerId, p.issue_id as issueId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(" p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.repay_money as repayMoney, p.is_pay_interest as isPayInterest, p.interest as interest, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate, ");
        sql.append(" i.bond_issue_num as bondIssueNum, p.purpose as purpose ");
        sql.append(" from CMS_BOND_PAY p ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on p.issue_id = i.id ");
        sql.append(" where 1 = 1 and p.status != -2 ");
        sql.append(" and p.is_pay_interest is null ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(registerId)) {
            sql.append(" and p.register_id= :registerId ");
            params.put("registerId", registerId);
        }
        sql.append(" order by p.pay_date ");
        List<CmsBondPayVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondPayVo.class);
        return list;
    }
    /**
     * 校验该发行是否还有在审批中的数据
     *
     * @param issueId
     *            债券发行id
     * @return 债券还款信息
     */
    public List<CmsBondPayVo> getInfoByStatus(String issueId) {
        if (EmptyUtils.isEmpty(issueId)) {
            return new ArrayList<>();
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select p.id as id, p.register_id as registerId, p.issue_id as issueId, ");
        sql.append(" p.online_payment_acc as onlinePaymentAcc, p.online_acc_name as onlineAccName, ");
        sql.append(" p.online_bank_name as onlineBankName, p.status as status, p.online_sign as onlineSign, ");
        sql.append(
                " p.rmk as rmk, p.pay_date as payDate, p.repay_money as repayMoney, p.is_pay_interest as isPayInterest, p.interest as interest, ");
        sql.append(
                " p.create_By as createBy, p.create_Date as createDate, p.update_By as updateBy, p.update_Date as updateDate ");
        sql.append(" from CMS_BOND_PAY p ");
        sql.append(" where 1 = 1 and p.status ='" + CmsConstant.COMMITAPPROVE + "' ");
        Map<String, String> params = new HashMap<String, String>();
        if (StringUtils.isNotBlank(issueId)) {
            sql.append(" and p.issue_id= :issueId ");
            params.put("issueId", issueId);
        }
        List<CmsBondPayVo> list = listBySQLAliasToBean(sql.toString(), params, CmsBondPayVo.class);
        return list;
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param issueId
     *            发放编号
     * @param passageSign
     *            在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public BigDecimal getRepayMoneyByIssueId(String issueId, String passageSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select BT_NVL_NUM(sum(pay.repay_money),0) SUMMONEY  ");
        sql.append(" from CMS_BOND_PAY pay ");
        sql.append(" left join CMS_BOND_REPAY_PLAN_DTL pdtl on pay.id = pdtl.pay_id ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" where 1=1  ");
        if ("1".equals(passageSign)) {// 还款在途
            sql.append(" and pay.status >= :status1");
            sql.append(" and pay.status < :status");
            params.put("status1", CmsConstant.BACK);
            params.put("status", CmsConstant.COMPELETOTAL);
        } else if ("2".equals(passageSign)) {// 已还金额
            sql.append(" and pay.status >= :status ");
            params.put("status", CmsConstant.COMPELETOTAL);
        } else {//
            sql.append(" and pay.status >= :status1");
            params.put("status1", CmsConstant.BACK);
        }
        sql.append(" and plan.issue_id =:issueId ");
        params.put("issueId", issueId);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = new BigDecimal(rs.get("SUMMONEY").toString());
        return count;
    }
    public DataTablesOutput dataTableBondPayApprove(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select pay.id as id, pay.corp_id as corpId, pay.pay_no as payNo, pay.pay_type as payType, pay.STATUS as status, ");
        sql.append(
                " pay.create_By as createBy, pay.create_Date as createDate, pay.update_By as updateBy, pay.update_Date as updateDate, ");
        sql.append(
                " r.id as registerId, r.TYPE_ID AS typeId, r.CORP_NAME AS corpName, i.bond_issue_num as bondIssueNum, ");
        sql.append(
                " r.ISSUE_NAME AS issueName, r.APPROVAL_NUMBER as approvalNumber, r.BOND_FULL_NAME as bondFullName, r.BOND_CODE as bondCode,");
        sql.append(
                " r.BOND_SHORT_NAME as bondShortName, r.BOND_DEADLINE as bondDeadline, r.DEADLINE_UNIT as deadlineUnit, ");
        //TODO 补充币种名称
        sql.append(" r.BOND_CUR as bondCur,r.CUR_NAME as bondCurName, r.ISSUE_CUR_TOTAL as issueCurTotal, r.ISSUE_RMB_TOTAL as issueRmbTotal, ");
        sql.append(
                " r.IS_TERRITORY as isTerritory, r.is_delete as isDelete, r.ATTACHMENT_ID as attachmentId, t.bond_name as bondTypeName ");
        sql.append(" from CMS_BOND_PAY pay ");
        sql.append(" left join CMS_BOND_REPAY_PLAN_DTL pdtl on pay.id = pdtl.pay_id ");
        sql.append(" left join CMS_BOND_REPAY_PLAN plan on plan.id = pdtl.plan_id ");
        sql.append(" left join CMS_BOND_REGISTER_INFO r on pay.register_id = r.id ");
        sql.append(" left join CMS_BOND_ISSUE_INFO i on pay.issue_id = i.id ");
        sql.append(" left join CMS_BOND_TYPE t on r.type_id = t.id ");
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "pay.id")).append(" ");
        }
        String flowStatus = dataTablesInput.getParameter().get("flowStatus");
        // 已审批不过滤状态
        if (!IWorkFlowService.PASS.equals(flowStatus)) {
            sql.append(" and pay.status<> ").append(NetbankConstant.STATUE_CHECK);
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsBondPayVo.class, null, false);
    }
}
