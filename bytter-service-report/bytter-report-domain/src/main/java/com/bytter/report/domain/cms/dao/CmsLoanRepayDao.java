package com.bytter.report.domain.cms.dao;

import com.bytter.biz.finacial.api.constant.NbconsoleConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.report.domain.cms.entity.CmsLoanRepay;
import com.bytter.report.domain.cms.vo.CmsLoanRepayTallyQueryVO;
import com.bytter.report.domain.cms.vo.CmsLoanRepayVO;
import com.bytter.finance.dto.CmsLoanRepayDTO;
import com.bytter.finance.dto.CmsLoanRepayMoneyDTO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 贷款还款表
 * @author onlineGenerator
 * @date 2017-08-03 10:35:09
 * @version 1.0
 *
 */
@Repository
public class CmsLoanRepayDao extends BaseDao<CmsLoanRepay, String> {
    /**
     * <b>功能介绍：</b>还款申请修改列表数据<br>
     * <b>日期：</b>2017年8月3日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCmsLoanRepayEdit(DataTablesInput input, List<String> corpAuth, String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select r.id,i.corp_id as corpId,i.net_code as netCode,i.cur_code as curCode,r.bill_code as billCode,r.create_date as createDate,s.bill_code as sendCode,r.repay_money as repayMoney,r.repay_date as repayDate,r.pay_type as payType,r.status,r.biz_status as bizStatus, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName ");
        sql.append("from cms_loan_info i, cms_loan_send s, cms_loan_repay r ");
        sql.append("where i.bill_code = s.loan_code and s.bill_code = r.send_code ");
        sql.append("and r.create_by = '" + userId + "' ");
        sql.append("and r.status in (" + CmsConstant.TEMP + "," + CmsConstant.BACK + ") ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanRepayVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>利息确认列表数据<br>
     * <b>日期：</b>2017年10月27日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param input
     * @return
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listRepayInteretAffirm(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select r.id,s.id as sendId,i.corp_id as corpId,i.net_code as netCode,i.bill_code as loanCode,r.bill_code as billCode,s.bill_code as sendCode,r.create_date as createDate,i.cur_code as curCode,");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "s.loan_money as loanMoney,r.repay_money as repayMoney,r.repay_accrual as repayAccrual,r.repay_date as repayDate,r.repayway as repayway,r.biz_status as bizStatus from cms_loan_repay r ");
        sql.append("left join cms_loan_send s on s.bill_code = r.send_code ");
        sql.append("left join cms_loan_info i on i.bill_code = s.loan_code ");
        sql.append("where r.status = " + CmsConstant.APPROVED);
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "r.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanRepayVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>审批列表数据查询<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select r.id,i.corp_id as corpId,i.net_code as netCode,r.bill_code as billCode,r.create_date as createDate,s.bill_code as sendCode,r.repay_money as repayMoney,r.repay_date as repayDate,r.pay_type as payType,r.contract_code as contractCode, ");
        //TODO 补充名称相关信息
        sql.append(" i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(" i.cur_code as curCode ");
        sql.append("from cms_loan_info i, cms_loan_send s, cms_loan_repay r ");
        sql.append("where i.bill_code = s.loan_code and s.bill_code = r.send_code ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "r.id")).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanRepayVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>查询贷款已还金额<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public BigDecimal getRepayedMoney(String loanCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select BT_NVL_NUM(sum(repay_money),0) repayMoney from cms_loan_repay r, cms_loan_send s ");
        sql.append(
                "where r.send_code = s.bill_code and r.status >=" + CmsConstant.COMPELETOTAL + " and r.status <> 120");
        sql.append(" and r.repay_type !=" + CmsConstant.REPAY_TYPE_4);// 排除只还利息的还款申请
        sql.append(" and s.loan_code =:loanCode ");
        params.put("loanCode", loanCode);
        BigDecimal repayMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return repayMoney;
    }
    /**
     * <b>功能介绍：</b>本次发放已还金额<br>
     * <b>日期：</b>2017年9月20日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param sendCode
     * @return
     */
    public BigDecimal getSendRepayedMoney(String sendCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                "select BT_NVL_NUM(sum(repay_money),0) repayMoney from cms_loan_repay r where r.send_code = :sendCode and r.status>=:status and r.repay_type != :repayType ");
        params.put("sendCode", sendCode);
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("repayType", CmsConstant.REPAY_TYPE_4);// 排除只还利息的还款申请
        BigDecimal repayMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return repayMoney;
    }
    /**
     * <b>功能介绍：</b>查询在途金额<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public BigDecimal getInwayMoney(String loanCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select BT_NVL_NUM(sum(repay_money),0) repayMoney from cms_loan_repay r, cms_loan_send s ");
        sql.append(
                "where r.send_code = s.bill_code and ((r.status < :status1 and r.status > :status2) or r.status = 120) ");
        sql.append(" and r.repay_type !=" + CmsConstant.REPAY_TYPE_4);// 排除只还利息的还款申请
        sql.append(" and s.loan_code =:loanCode ");
        params.put("loanCode", loanCode);
        params.put("status1", CmsConstant.COMPELETOTAL);
        params.put("status2", CmsConstant.DELETED);
        BigDecimal inwayMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return inwayMoney;
    }
    /**
     * <b>功能介绍：</b>app审批在途金额<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public BigDecimal getMobileInwayMoney(String sendCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney   from cms_loan_repay clr ");
        sql.append("  where clr.status != -2  and (clr.status < 102 or clr.status = 120)  ");
        sql.append(" and clr.send_code =:sendCode ");
        params.put("sendCode", sendCode);
        BigDecimal inwayMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return inwayMoney;
    }
    /**
     * <b>功能介绍：</b>获取发放申请的剩余还款金额<br>
     * <b>日期：</b>2017年8月17日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     */
    public BigDecimal getRemainMoney(String sendCode, String repayCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select s.loan_money - (select BT_NVL_NUM(sum(repay_money),0) from cms_loan_repay r where  r.send_code = s.bill_code and r.status != "
                        + CmsConstant.DELETED + " and r.repay_type != " + CmsConstant.REPAY_TYPE_4);
        if (EmptyUtils.isNotEmpty(repayCode)) {// 修改还款信息时
            sql.append(" and r.bill_code != :repayCode ");
            params.put("repayCode", repayCode);
        }
        sql.append(") as remainMoney from cms_loan_send s where s.bill_code = :sendCode ");
        params.put("sendCode", sendCode);
        BigDecimal remainMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return remainMoney;
    }
    /**
     * <b>功能介绍：</b>还款中记录数<br>
     * <b>日期：</b>2017年9月15日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param sendCode
     * @return
     */
    public Integer getRepayingCount(String sendCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) from cms_loan_repay r where r.send_code =:sendCode and r.status !="
                        + CmsConstant.DELETED + " and r.status < " + CmsConstant.COMPELETOTAL);
        params.put("sendCode", sendCode);
        BigDecimal repayCount = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return repayCount.intValue();
    }
    /**
     * <b>功能介绍：</b>根据发放编号查询还款明细<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     * 
     * @version 1.0
     * @param sendCode
     * @return
     */
    public List<CmsLoanRepay> getCmsLoanRepayBySendCode(String sendCode, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder hql = new StringBuilder("from CmsLoanRepay r where r.sendCode = :sendCode  ");
        if ("0".equals(type)) {// 在途
            hql.append(" and (( status < :status and status <> :Dstatus) or  status = 120 )");
        } else {
            hql.append("and status >= :status and status <> 120 ");
        }
        hql.append("order by r.billCode desc ");
        params.put("sendCode", sendCode);
        params.put("Dstatus", CmsConstant.DELETED);
        params.put("status", CmsConstant.COMPELETOTAL);
        return listByHQL(hql.toString(), params);
    }
    /**
     * 记账页面查询语句
     * 
     * @return
     */
    public String querySql() {
        String sql = "select r.id,r.bill_code as billCode,r.status,r.biz_status as bizStatus,i.net_code as netCode,"
                + "i.corp_id as corpId,r.contract_code as contractCode,i.cur_code as curId,s.bill_code as sendBillCode,"
                + "s.provide_loan_acc as provideLoanAcc,s.loan_date as loanDate,r.repay_date as repayDate,r.tally_date as tallyDate,"
                + "s.loan_money as loanMoney,r.repay_money as repayMoney,r.repay_accrual as repayAccrual,r.repay_acc as repayAcc, "
                + "r.loan_acc as loanAcc,r.repay_type as repayType,r.repayway ,r.interest_start_date as interestStartDate,r.remind_sign as remindSign "
                + "from cms_loan_repay r " + "left join cms_loan_send s on r.send_code = s.bill_code "
                //TODO 核算相关的表改造
                + "left join cms_loan_info i on i.bill_code = s.loan_code where 1 = 1 and r.status in (:status) and r.order_no is not null ";
//                + "left join cms_loan_info i on i.bill_code = s.loan_code left join fc_accbook book on r.order_no = book.order_no where 1 = 1 and r.status in (:status) ";
        return sql;
    }
    /**
     * <b>功能介绍：</b>查询手工记账业务数据<br>
     * @version 1.0
     * @param input
     * @param opType
     * @param userId
     * @return
     */
    public DataTablesOutput getLoanTallyList(DataTablesInput input, String opType, String userId,
            List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(querySql());
        if (NbconsoleConstant.OpType.INIT.get().equals(opType)) {
            params.put("status", new Integer[] { CmsConstant.WAITTOTAL, CmsConstant.NBC_STATUS_MODIFY_BACK });
        } else if (NbconsoleConstant.OpType.EDIT.get().equals(opType)) {
            //TODO 核算相关表的改造
            sql.append(" and r.operator_code = :operatorCode ");
            params.put("operatorCode", userId);// 只能修改自己的账务
            params.put("status", new Integer[] { CmsConstant.NBC_STATUS_TEMP, CmsConstant.NBC_STATUS_CHECK_BACK,
                    CmsConstant.NBC_STATUS_SUBMIT, CmsConstant.NBC_STATUS_MODIFY_COMPLETE });
        } else if (NbconsoleConstant.OpType.CHECK.get().equals(opType)) {
            sql.append(" and r.operator_code != :operatorCode ");
            params.put("operatorCode", userId);// 不能复核自己的账务
            params.put("status", new Integer[] { CmsConstant.NBC_STATUS_SUBMIT });
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanRepayTallyQueryVO.class, "", false, params);
    }
    public CmsLoanRepayDTO getCmsLoanRepayById(String id) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_loan_repay where id = '" + id + "'");
        return this.getBySQLAliasToBean(sql.toString(), null, CmsLoanRepayDTO.class);
    }
    public CmsLoanRepayDTO getCmsLoanRepayByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from cms_loan_repay where bill_code = '" + billCode + "' ");
        return this.getBySQLAliasToBean(sql.toString(), null, CmsLoanRepayDTO.class);
    }
    /**
     * 查询还款待记账的数据
     * 
     * @return
     */
    public List<CmsLoanRepayDTO> queryRepayTallyData() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(querySql());
        sql.append(" and (r.remind_sign = '0' or r.remind_sign is null)");
        params.put("status", CmsConstant.WAITTOTAL);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanRepayDTO.class);
    }
    /**
     * 单位校验使用-待记账
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpIdTally(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(r.id) ");
        sql.append(" from cms_loan_repay r  ");
        sql.append(" left join cms_loan_send s on r.send_code = s.bill_code ");
        sql.append(" left join cms_loan_info i on i.bill_code = s.loan_code");
//        sql.append(" left join fc_accbook book on r.order_no = book.order_no ");
        sql.append(" where 1= 1 ");
        //TODO 统计待记账的数据
        sql.append(" and  r.status = 97 and order_no is not null ");
        sql.append(" and r.corp_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
    }
    /**
     * 单位校验使用 -利息待确认
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from CMS_LOAN_REPAY where 1 = 1 and status = 95  and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
    /**
     * 单位校验使用 -未全部还清的贷款
     * 
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId1(String corpId) {
        // 放款金额-已还款金额
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(BT_NVL_NUM( (s.loan_money - BT_NVL_NUM(repayLoan.repayMoney, 0)), 0)) as loanMoney ");
        sql.append(" from cms_loan_info a ");
        sql.append(" left join cms_loan_send s on a.bill_code = s.loan_code ");
        sql.append(" left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney,clr.send_code ");
        sql.append(" from cms_loan_repay clr  where clr.status >= 102  and clr.status <> 120 ");
        sql.append(" group by clr.send_code ");
        sql.append(" ) repayLoan on s.bill_code = repayLoan.send_code ");
        sql.append("  where 1 = 1 and s.status =102  and a.status != -2 ");
        sql.append(" and a.corp_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        CmsLoanRepayVO vo = getBySQLAliasToBean(sql.toString(), param, CmsLoanRepayVO.class);
        if (null != vo && null != vo.getLoanMoney()) {
            if (vo.getLoanMoney().compareTo(BigDecimal.ZERO) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }
    /**
     * <br>
     * <b>功能：</b> 查询贷款合同金额及还款金额 <br>
     * <b>作者：</b>liyh<br>
     * <b>日期：</b> 2021/3/12 0012 <br>
     */
    public List<CmsLoanRepayMoneyDTO> queryLoanRepayMoneyByLoanAcc(String loanAcc, Date endDate) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select send.loan_acc as loanAcc,bt_nvl_num(send.loan_money,0) as loanMoney, ");
        sql.append(
                " bt_nvl_num(rep1.sumRepayMoney,0) as sumRepayMoney, bt_nvl_num(rep2.repay_money,0) as repayMoney, ");
        //TODO 账户名称录入
        sql.append(
                " send.acc_name as accName, send.provide_loan_acc as interestAcc, send.interest_acc_name as interestAccName ");
//        sql.append(
//                " acc1.acc_name as accName, send.provide_loan_acc as interestAcc, acc2.acc_name as interestAccName ");
        sql.append(" from cms_loan_send send ");
        sql.append(" left join ( ");
        sql.append(" select repay.loan_acc,sum(repay_money) as sumRepayMoney from cms_loan_repay repay ");
        sql.append(" where repay.repay_date <= :endDate ");
        sql.append(" and repay.loan_acc = :loanAcc ");
        sql.append(" and repay.status >= 102 ");
        sql.append(" group by repay.loan_acc ");
        sql.append(" ) rep1 on send.loan_acc = rep1.loan_acc ");
        sql.append(" left join ( ");
        sql.append(" select repay1.loan_acc,repay1.repay_money ");
        sql.append(" from cms_loan_repay repay1 inner join  ");
        sql.append(" (select repay2.loan_acc,max(repay2.repay_date) as repayDate from cms_loan_repay repay2 ");
        sql.append(" where repay2.repay_date <= :endDate ");
        sql.append(" and repay2.loan_acc = :loanAcc ");
        sql.append(" and repay2.status >= 102 ");
        sql.append(" group by repay2.loan_acc) repay3 ");
        sql.append(" on repay1.loan_acc = repay3.loan_acc and repay1.repay_date = repay3.repayDate ");
        sql.append(" ) rep2 on send.loan_acc = rep2.loan_acc ");
        //TODO 账户名称录入在发放表中
//        sql.append(" left join fc_acc acc1 on acc1.blnctr_acc = send.loan_acc ");
//        sql.append(" left join fc_acc acc2 on acc2.blnctr_acc = send.provide_loan_acc ");
        sql.append(" where send.loan_acc = :loanAcc ");
        params.put("loanAcc", loanAcc);
        params.put("endDate", endDate);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanRepayMoneyDTO.class);
    }
}
