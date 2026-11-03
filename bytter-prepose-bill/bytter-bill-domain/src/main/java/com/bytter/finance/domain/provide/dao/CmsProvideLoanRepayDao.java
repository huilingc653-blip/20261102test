package com.bytter.finance.domain.provide.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.common.datatable.OrderString;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.provide.entity.CmsProvideLoanRepay;
import com.bytter.finance.domain.provide.vo.LoanRepayVO;
import com.bytter.finance.dto.CmsProvideInterestDTO;
import com.bytter.finance.dto.CmsProvideLoanRepayDTO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 借款还款
 * @date 2017-08-08 15:15:54
 */
@Repository
public class CmsProvideLoanRepayDao extends BaseDao<CmsProvideLoanRepay, String> {
    /**
     * 查询还款列表 包括暂存数据
     *
     * @param sql
     */
    public void getRepayQuerySql(StringBuilder sql) {
        sql.append(
                "select repay.id as id,info.corp_id as corpId,info.bill_code as infoBillCode,info.contract_no as contractCode,send.bill_code as sendCode, ");
        sql.append(
                " info.pl_account as loanAcc,info.create_date as createDate,send.cur_code as curId,send.money as sendMoney,(send.money - BT_NVL_NUM(repaySum.sum_money,0)) as plBalance, ");
        //TODO 补充名称相关信息
        sql.append(" info.corp_name as corpName,send.cur_name as curName, info.bank_name as bankName, ");
        sql.append(
                " send.send_date as sendData,send.repay_date as sendDueData,send.repay_type as repayType,type.name as plClassName, ");
        sql.append(
                " info.pl_type as plType,send.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,BT_NVL_CHAR(repay.biz_status,send.biz_status) as bizStatus");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" inner join cms_provide_loan_send send on info.bill_code = send.loan_code ");
        sql.append(
                " left join cms_provide_loan_repay repay on repay.send_code = send.bill_code and repay.status <> -2 ");
        sql.append(
                " left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code ");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='"
                + BaseDataConstant.TYPE_BORROW + "' ");
    }
    /**
     * 待还款列表 有详细的申请和发放信息 - 不含还款信息
     *
     * @param sql
     */
    public void getQuerySql(StringBuilder sql) {
        sql.append("select send.id as id,info.corp_id as corpId,info.corp_name as corpName,info.bill_code as infoBillCode,info.contract_no as contractCode,info.bank_name as bankName,send.bill_code as sendCode, ");
        sql.append(" info.pl_account as loanAcc,info.create_date as createDate,send.cur_code as curId,send.cur_name as curName,send.money as sendMoney,(send.money - BT_NVL_NUM(repaySum.sum_money,0)) as plBalance, ");
        sql.append(" send.send_date as sendData,send.repay_date as sendDueData,send.repay_type as repayType,type.name as plClassName,");
        sql.append(" info.create_name createName,repayType.REPAY_NAME repayTypeName1,accrualType.ACCRUAL_NAME accrualTypeName,");
        sql.append(" info.pl_type as plType,send.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,send.biz_status as bizStatus ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" inner join cms_provide_loan_send send on info.bill_code = send.loan_code ");
        sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.TENANT_ID = info.TENANT_ID and type.status != -2 and type = '" + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(" left join CMS_REPAY_TYPE repayType on send.repay_type = repayType.id");
        sql.append(" left join CMS_ACCRUAL_TYPE accrualType on send.accrual_type = accrualType.id");
    }
    /**
     * 还款修改、补录、审批列表 - 还款信息为主 附带申请和发放单号
     *
     * @param sql
     */
    public void getQueryRepaySql(StringBuilder sql) {
        sql.append("select repay.id as id,info.corp_id as corpId,info.corp_name as corpName,repay.bill_code as billCode,info.bill_code as infoBillCode,send.bill_code as sendCode,repay.online_sign as onlineSign, repay.is_advanced_repay as isAdvancedRepay, ");
        sql.append(" repay.CREATE_NAME repayCreateName, ");
        sql.append(" repay.create_date as createDate,repay.repay_money as repayMoney,repay.repay_date as repayDate,repay.status as status,repay.biz_status as bizStatus,info.cur_Id as curId,info.cur_name as curName ");
        sql.append(" from cms_provide_loan_repay repay");
        sql.append(" inner join cms_provide_loan_send send on send.bill_code = repay.send_code ");
        sql.append(" inner join cms_provide_loan_info info on info.bill_code = send.loan_code ");
    }
    /**
     * 功能介绍 : 还款申请列表 - 分页<br>
     */
    public DataTablesOutput getDataAppTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        getQuerySql(sql);
        // getRepayQuerySql(sql);
        sql.append(" where send.status in (102,104) and info.status > -1 ");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        sql.append(" and not exists(select 1 from cms_provide_loan_repay plr where  plr.send_code = send.bill_code  and plr.status in (-1,97,0,95,11,12)) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "send.corp_id"));
        OrderString orderString = new OrderString("info.create_date", "desc");
        dataTablesInput.setOrderString(orderString);
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 借款自动还款数据
     */
    public List<LoanRepayVO> getAutoDataAppTables() {
        StringBuilder sql = new StringBuilder();
        getQuerySql(sql);
        sql.append(" where send.status in (102,104) and info.status > -1 ");
        sql.append(" and info.IS_AUTO_PAY='1'");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        sql.append(" and not exists(select 1 from cms_provide_loan_repay plr where  plr.send_code = send.bill_code  and (plr.status in (97,0,95,11)) ) ");
        return this.listBySQLAliasToBean(sql.toString(), null, LoanRepayVO.class);
    }
    /**
     * 功能介绍 : 还款跟踪列表 - 分页<br>
     *
     * @param dataTablesInput
     * @param corpAuth
     * @return
     */
    public DataTablesOutput getDataOnlineRepayFollow(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select repay.id as id,info.corp_id as corpId,info.corp_name as corpName,info.bill_code as infoBillCode,info.contract_no as contractCode,info.bank_name as bankName,send.bill_code as sendCode,repay.online_status as onlineStatus, ");
        sql.append(" info.pl_account as loanAcc,info.create_date as createDate,send.cur_code as curId,send.cur_name as curName,send.money as sendMoney,(send.money - BT_NVL_NUM(repaySum.sum_money,0)) as plBalance,case when repay.online_status = 0 then 0 else repay.REQ_STATUS end as reqInterimStatus, ");
        sql.append(" repay.bill_code as billCode,repay.repay_money as repayMoney,send.send_date as sendData,repay.REQ_BILL_NO as billNo,repay.REQ_STATUS as reqStatus,repay.VOUCHER_STAT as voucherStat ");
        sql.append(" from cms_provide_loan_info info ");
        sql.append(" inner join cms_provide_loan_send send on info.bill_code = send.loan_code ");
        sql.append(" inner join cms_provide_loan_repay repay on repay.send_code = send.bill_code ");
        sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code ");
        sql.append(" where repay.status = '" + CmsConstant.FLOWWAIT + "'");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "send.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 还款计划修改列表 - 分页<br>
     */
    public DataTablesOutput dataRepayPlanable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select send.id as id,info.corp_id as corpId,info.corp_name as corpName,info.bill_code as infoBillCode,info.contract_no as contractCode,info.bank_name as bankName,send.bill_code as sendCode, ");
        sql.append(" info.pl_account as loanAcc,info.create_date as createDate,send.cur_code as curId,send.cur_name as curName,send.money as sendMoney,(send.money - BT_NVL_NUM(repaySum.sum_money,0)) as plBalance, ");
        sql.append(" send.send_date as sendData,send.repay_date as sendDueData,send.repay_type as repayType,type.name as plClassName, ");
        sql.append(" info.pl_type as plType,send.pl_term as plTerm,info.tern_unit as ternUnit,send.status as status,send.biz_status as bizStatus, ");
        sql.append(" info.create_name createName,repayType.REPAY_NAME repayTypeName1, ");
        sql.append(" send.deferred_date as deferredrepaydate,send.deferred_rate as deferredRate ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info  on info.bill_code = send.loan_code ");
        sql.append(" left join (select sum(repay_money) as sum_money,send_code from cms_provide_loan_repay plr where plr.status <> -2 group by send_code) repaySum on repaySum.send_code = send.bill_code ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='" + BaseDataConstant.TYPE_BORROW + "' ");
        sql.append(" left join CMS_REPAY_TYPE repayType on send.repay_type = repayType.id");
        sql.append(" where send.status >= 95 and info.status > -1 ");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "send.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 计息方式修改列表 - 分页<br>
     */
    public DataTablesOutput dataInterestWayable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        getQuerySql(sql);
        sql.append(" where send.status >= 95 ");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        // 新数据还款方式必须是利息按剩余本金，历史存量数据必须是计提方式为空的数据才可以做计息方式调整
        sql.append(" and (repayType.CAPITAL_AND_INTEREST = '1' or send.ACCRUAL_TYPE is null )");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "send.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 浮动方式修改列表 - 分页<br>
     */
    public DataTablesOutput dataFolatingModeable(DataTablesInput dataTablesInput, List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        getQuerySql(sql);
        sql.append(" where send.status >= 95 ");
        // 浮动方式为固定的过滤掉（功能没有）
        // sql.append(" and send.FLOAT_WAY<>'01'");
        sql.append(" and ((repaySum.sum_money is null and send.money > 0) or send.money > repaySum.sum_money)");
        sql.append(" and send.repay_type != '" + CmsConstant.REPAY_TYPE_07 + "' ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "send.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 还款修改列表 - 分页<br>
     */
    public DataTablesOutput getDataUpdateTables(DataTablesInput dataTablesInput, String createName,
                                                List<String> corpAuth) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        getQueryRepaySql(sql);
        sql.append(" where repay.status in (0,-1) ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "repay.corp_id"));
        sql.append(" and repay.create_by = '" + createName + "'");
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 还款申请审批列表 - 分页<br>
     */
    public DataTablesOutput getDataRepayApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        getQueryRepaySql(sql);
        /*
         * sql.append(" where repay.status = :status "); params.put("status", CmsConstant.COMMITAPPROVE);
         */
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "repay.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 还款补录列表 - 分页<br>
     */
    public DataTablesOutput getDataRepairTables(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        getQueryRepaySql(sql);
        sql.append(" where repay.status = 95 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "repay.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 还款补录审批列表 - 分页<br>
     */
    public DataTablesOutput getDataRepayRepairApproveTables(DataTablesInput dataTablesInput, List<String> ids) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        getQueryRepaySql(sql);
        /*
         * sql.append(" where repay.status = :status "); params.put("status", CmsConstant.REPAIRAPPRAVE);
         */
        sql.append(" where 1 = 1");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "repay.id"));
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanRepayVO.class, null, false);
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param sendBillCode 发放编号
     * @param passageSign  在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public BigDecimal getRepayMoneyBySendBillCode(String sendBillCode, String passageSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select BT_NVL_NUM(sum(repay_money),0) SUMMONEY from cms_provide_loan_repay where 1=1 ");
        if ("1".equals(passageSign)) {// 还款在途
            sql.append(" and status >= :status1");
            sql.append(" and status < :status");
            params.put("status1", CmsConstant.BACK);
            params.put("status", CmsConstant.COMPELETOTAL);
        } else if ("2".equals(passageSign)) {// 已还金额
            sql.append(" and status >= :status ");
            params.put("status", CmsConstant.COMPELETOTAL);
        } else {//
            sql.append(" and status >= :status1");
            params.put("status1", CmsConstant.BACK);
        }
        sql.append(
                " and send_code in (select bill_code  from cms_provide_loan_send where bill_code =:billCode) group by send_code");
        params.put("billCode", sendBillCode);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = new BigDecimal(rs.get("SUMMONEY").toString());
        return count;
    }
    /**
     * 功能介绍 : 查询还款金额<br>
     *
     * @param sendBillCode 发放编号
     * @param passageSign  在途标识 1查询在途金额 2已还 其他为 无状态查询
     */
    public List getRepayListBySendBillCode(String sendBillCode, String passageSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder("select * from cms_provide_loan_repay where 1=1 ");
        if ("1".equals(passageSign)) {// 还款在途
            sql.append(" and status >= :status1");
            sql.append(" and status < :status");
            params.put("status1", CmsConstant.BACK);
            params.put("status", CmsConstant.COMPELETOTAL);
        } else if ("2".equals(passageSign)) {// 已还金额
            sql.append(" and status >= :status ");
            params.put("status", CmsConstant.COMPELETOTAL);
        } else {//
            sql.append(" and status >= :status1");
            params.put("status1", CmsConstant.BACK);
        }
        sql.append(" and send_code=:billCode");
        params.put("billCode", sendBillCode);
        sql.append(" order by repay_date desc ");
        return this.listBySQLAliasToBean(sql.toString(), params, LoanRepayVO.class);
    }
    /**
     * 功能介绍 : 查询借款单总共还款金额<br>
     *
     * @param billCode 借款编号
     */
    public BigDecimal getInfoRepayMoneySum(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select BT_NVL_NUM(sum(repay.repay_money),0) as SUMMONEY from ");
        sql.append(" cms_provide_loan_send send ");
        sql.append(" left join cms_provide_loan_repay repay on repay.send_code = send.bill_code ");
        sql.append(" where repay.status = :status and send.loan_code = :billCode ");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("billCode", billCode);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal count = new BigDecimal(rs.get("SUMMONEY").toString());
        return count;
    }
    /**
     * 查询还利息 还款单据的数据 <102 未记账的 sendCode发放单据
     */
    public Integer getRepayAccrualCount(String sendCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(*) as COU from ");
        sql.append(" cms_provide_loan_repay repay");
        sql.append(" where repay.status < :status and repay.status <> -2 and repay.send_code = :sendCode ");
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("sendCode", sendCode);
        Map rs = getBySQL(sql.toString(), params);
        if (rs == null) {
            return new Integer(0);
        }
        Integer count = new Integer(rs.get("COU").toString());
        return count;
    }
    public LoanRepayVO queryRepayVOById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select repay.id as id,repay.provide_interest_id as provideInterestId,send.bill_code as sendCode,info.bill_code as infoBillCode,info.corp_id as corpId, ");
        //TODO 补充名称信息
        sql.append(" info.corp_name as corpName, ");
        sql.append(
                " repay.online_payment_acc as onlinePaymentAcc,repay.online_acc_name as onlineAccName,repay.online_bank_name as onlineBankName, ");
        sql.append(
                " repay.repay_money as repayMoney,repay.repay_accrual as repayAccrual,repay.online_reality_interest as onlineRealityInterest,repay.online_money as onlineMoney ");
        sql.append(" from cms_provide_loan_repay repay");
        sql.append(" inner join cms_provide_loan_send send on send.bill_code = repay.send_code ");
        sql.append(" inner join cms_provide_loan_info info on info.bill_code = send.loan_code ");
        sql.append(" where repay.id = :id");
        params.put("id", id);
        return this.getBySQLAliasToBean(sql.toString(), params, LoanRepayVO.class);
    }
    public LoanRepayVO queryRepayVOByBillCode(String billCode, String onlineSign) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select repay.id as id,repay.provide_interest_id as provideInterestId,info.bill_code as infoBillCode, " );
        sql.append(" info.corp_id as corpId, repay.online_purpose as onlinePurpose,repay.online_other_acc_charge as onlineOtherAccCharge,");
        sql.append(" info.corp_name as corpName,info.CON_ID as conId,info.CON_TYPE_ID as conTypeId,send.loan_acc as loanAcc, ");
        sql.append(" repay.online_payment_acc_id as onlinePaymentAccId,repay.online_payment_acc as onlinePaymentAcc," );
        sql.append(" repay.online_acc_name as onlineAccName,repay.online_bank_name as onlineBankName,repay.bill_code as billCode, ");
        sql.append(" repay.repay_money as repayMoney,repay.repay_accrual as repayAccrual,repay.online_reality_interest as onlineRealityInterest," );
        sql.append(" repay.online_money as onlineMoney,repay.order_no as orderNo,repay.online_sign as onlineSign,");
        sql.append(" repay.contract_code as contractCode,repay.send_code as sendCode,repay.repay_date as repayDate," );
        sql.append(" repay.status as status,repay.create_name as createName,repay.create_by as createBy,repay.tally_sign as tallySign, ");
        sql.append(" repay.tally_code as tallyCode,repay.create_date as createDate,send.money as sendMoney, repay.rmk as rmk ");
        sql.append(" from cms_provide_loan_repay repay");
        sql.append(" inner join cms_provide_loan_send send on send.bill_code = repay.send_code ");
        sql.append(" inner join cms_provide_loan_info info on info.bill_code = send.loan_code ");
        sql.append(" where repay.bill_code = :billCode ");
        if (EmptyUtils.isNotEmpty(onlineSign) && onlineSign != null) {
            sql.append(" and repay.online_sign = :onlineSign");
            params.put("onlineSign", onlineSign);
        }
        params.put("billCode", billCode);
        return this.getBySQLAliasToBean(sql.toString(), params, LoanRepayVO.class);
    }
    public CmsProvideLoanRepay getRepayInfoByBillCode(String billCode) {
        String sql = " select * from  cms_provide_loan_repay where bill_code = :billCode ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billCode", billCode);
        return this.getBySQLAliasToBean(sql, params, CmsProvideLoanRepay.class);
    }

    public LoanRepayVO getRepayInfoByInterestId(String interestId){
        String sql = " select * from  cms_provide_loan_repay where PROVIDE_INTEREST_ID = :interestId and status<>-2";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("interestId",interestId);
        return this.getBySQLAliasToBean(sql, params, LoanRepayVO.class);
    }

    /**
     * 根据单位查询是否存在相关业务单据
     *
     * @param corpId
     * @return
     */
    public int queryBusBillByCorpId(String corpId, String status, String bizStatus) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        sql.append("select count(*) from cms_provide_loan_repay where 1=1 and corp_id=:corpId ");
        if (EmptyUtils.isNotEmpty(bizStatus)) {
            sql.append(" and BIZ_STATUS in(:bizStatus) ");
            params.put("bizStatus", bizStatus.split(","));
        }
        if (EmptyUtils.isNotEmpty(status)) {
            sql.append(" and status in(:status) ");
            params.put("status", status.split(","));
        }
        params.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), params).uniqueResult().toString());
    }
    /**
     * 单位校验使用-待记账
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(info.id) ");
        sql.append(" from cms_provide_loan_info info  ");
        sql.append(" left join cms_provide_loan_send s on info.bill_code = s.loan_code ");
        sql.append(" left join cms_provide_loan_repay r on s.bill_code = r.send_code");
        //TODO 录入时就是外部获取
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id ");
        sql.append(" where 1= 1 ");
        sql.append(" and r.status = 97 and (r.tally_Sign ='' or r.tally_Sign ='0' or r.tally_sign is null)");
        sql.append(" and info.corp_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
    }
    public BigDecimal queryProvideUseRatMoney(String billNo, Date calEndDate) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(provide.money) money from ( ");
        sql.append(" select sum(ist.repay_money) as money ").append(" from cms_provide_interest ist ")
                .append(" where ist.bill_code in ").append(" (select bill_code ").append(" from cms_provide_loan_send ")
                .append(" where loan_code =:billNo ) ").append(" and ist.status=0 ")// 查询还款明细中未还款的数据
                .append(" and ist.interest_sign=1 ").append(" and ist.repay_date <=:calEndDate  ");
        sql.append(" union ");
        sql.append(" select sum(repay.repay_money) as money ")// 查询还款中的数据
                .append(" from cms_provide_loan_repay repay ").append(" where repay.send_code in ")
                .append(" ((select bill_code ").append(" from cms_provide_loan_send ")
                .append(" where loan_code =:billNo)) ").append(" and repay.biz_status not in (-2, 110, 48) ")
                .append(" and repay.repay_date <= :calEndDate ");
        sql.append(" ) provide ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("billNo", billNo);
        params.put("calEndDate", calEndDate);
        Map<String, Object> result = getBySQL(sql.toString(), params);
        BigDecimal calReleaseMoney = BigDecimal.ZERO;
        if (null != result && null != result.get("MONEY")) {
            calReleaseMoney = new BigDecimal(result.get("MONEY").toString());
        }
        return calReleaseMoney;
    }


    /**
     * 定时任务查询待生成需求单的融资到期
     * @return
     */
    public List<CmsProvideInterestDTO> listTaskRepayForNisDemand(Date day){
        StringBuilder sql = new StringBuilder();
        Map<String,Object> params = new HashMap<>();
        sql.append(" select interest.*,send.online_payment_acc_id as onlinePaymentAccId   ");
        sql.append(" from cms_provide_interest interest ");
        sql.append(" inner join cms_provide_loan_send send on send.bill_code = interest.bill_code  ");
        sql.append(" inner join cms_provide_loan_info info on info.bill_code = send.loan_code  ");
        sql.append(" JOIN bt_bank_acc acc ON send.ONLINE_PAYMENT_ACC_ID = acc.ID AND acc.`STATUS` = 95 AND acc.VALID_SIGN = '1' AND acc.IS_DEMAND = '1' ");
        sql.append(" left join nis_demand d on interest.id = d.pay_bill_id ");
        sql.append(" where  send.status = 102  ");
        sql.append(" and BT_TO_CHAR(interest.repay_date, 'yyyy-MM-dd') > :startDate   ");
        sql.append(" and BT_TO_CHAR(interest.repay_date, 'yyyy-MM-dd') <= :endDate   ");
        sql.append(" and d.pay_bill_id is null  ");//没有生成需求单的
        params.put("startDate", DateUtils.getIntegralStartTime(new Date()));
        params.put("endDate", DateUtils.formatDate(day));
        return this.listBySQLAliasToBean(sql.toString(), params, CmsProvideInterestDTO.class);
    }
}
