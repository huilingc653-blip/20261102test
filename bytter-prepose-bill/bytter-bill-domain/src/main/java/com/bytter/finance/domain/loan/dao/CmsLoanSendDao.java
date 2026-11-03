package com.bytter.finance.domain.loan.dao;
import com.bytter.finance.domain.basedata.vo.CmsLoanSendOutCheckVO;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.vo.CmsLoanSendTallyQueryVO;
import com.bytter.finance.domain.loan.vo.CmsLoanSendVO;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.dto.CmsLoanSendDTO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.constant.CommonConstant;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 贷款发放表
 * @author onlineGenerator
 * @date 2017-07-28 11:27:00
 * @version 1.0
 *
 */
@Repository
public class CmsLoanSendDao extends BaseDao<CmsLoanSend, String> {
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCmsLoanSend(DataTablesInput input, String pageType) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select s.id as id,s.bill_code as billCode,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,");
        sql.append(
                "s.contract_code as contractCode,i.provide_loan_acc as provideLoanAcc,s.create_date as createDate,s.cur_code as curCode,");
        //TODO 补充名称相关的参数字段信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append("s.loan_money as loanMoney,0 as remainMoney,s.loan_date as loanDate,s.repay_date as repayDate,");
        sql.append(
                "s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType,i.loan_term as loanTerm,");
        sql.append("i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code where 1 = 1 ");
        if ("createContract".equals(pageType)) {// 合同生成页面
            sql.append(" and s.status in (" + CmsConstant.APPROVED + "," + CmsConstant.WAITTOTAL + ","
                    + CmsConstant.COMPELETOTAL + ")");
        } else if ("stayRepay".equals(pageType)) {// 待还款申请页面
            sql.append(" and s.status in (" + CmsConstant.COMPELETOTAL + "," + CmsConstant.EXTENDDATING + ","
                    + CmsConstant.COMPELTEXTENDATE + ")");
        } else if ("createDeferred".equals(pageType)) {// 待展期申请页面
            sql.append(" and s.status in (" + CmsConstant.COMPELETOTAL + "," + CmsConstant.EXTENDDATING + ","
                    + CmsConstant.COMPELTEXTENDATE + ")");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>贷款合同生成列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput loanContractList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.* from ( ");
        sql.append("select s.id as id,s.corp_id  as corpId,i.net_code as netId,s.loan_code as loanCode, ");
        sql.append(
                "s.bill_code as billCode,s.create_date as createDate,s.cur_code as curCode,s.loan_money as loanMoney, ");
        sql.append(
                "(select BT_NVL_NUM(sum(r.repay_money), 0) from cms_loan_repay r where r.send_code = s.bill_code and r.status >= 102)   as repayedMoney, ");
        sql.append("s.contract_code as contractCode,i.provide_loan_acc as provideLoanAcc,s.loan_date as loanDate, ");
        sql.append(
                "s.repay_date as repayDate,s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType, ");
        sql.append("i.loan_term as loanTerm,i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code where 1 = 1 ");
        sql.append(" and s.status in (" + CmsConstant.APPROVED + "," + CmsConstant.WAITTOTAL + ","
                + CmsConstant.COMPELETOTAL + ")");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "s.corp_id"));
        sql.append(" ) a  where 1=1 ");
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>贷款还款申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput loanRepayList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.* from ( ");
        sql.append(
                "select  s.id as id,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,s.bill_code as billCode, ");
        sql.append(
                "s.loan_acc as loanAcc,s.contract_code as contractCode,s.create_date as createDate,s.cur_code as curCode,s.loan_money as loanMoney, ");
        sql.append("r.repay_money as repayedMoney,s.loan_date as loanDate,s.repay_date as repayDate, ");
        sql.append("(case when s.status >= " + CmsConstant.COMPELTEXTENDATE
                + " then (select max(d.deferred_repay_date) from cms_loan_deferred d where d.li_code = s.bill_code and d.status = "
                + CmsConstant.COMPELETOTAL + ") else null end) as  deferredReplayDate, ");
        sql.append(
                "s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType,i.loan_term as loanTerm, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus,(select count(1) from cms_loan_repay where send_code =s.bill_code and status !="
                        + CmsConstant.DELETED + " and status < " + CmsConstant.COMPELETOTAL + " ) repayingCount ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code ");
        sql.append(
                " left join ( select send_code,sum(repay_money) as repay_money from cms_loan_repay repay where status>="
                        + CmsConstant.COMPELETOTAL + " group by send_code) r on s.bill_code = r.send_code ");
        sql.append("where s.status in (" + CmsConstant.COMPELETOTAL + ", " + CmsConstant.COMPELTEXTENDATE + ", "
                + CmsConstant.EXTENDDATING + ") ");
        sql.append(
                "and not exists (select 1 from cms_loan_repay re where re.send_code = s.bill_code and (re.status in(97,-1))) ");
        sql.append(
                "and s.loan_money > (select BT_NVL_NUM(sum(repay_money), 0) from cms_loan_repay e where s.bill_code = e.send_code and e.status <> "
                        + CmsConstant.DELETED + ")");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "s.corp_id"));
        sql.append(" ) a  where 1=1 ");
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * 功能介绍 : 贷款自动还款数据
     */
    public List<CmsLoanSendVO> getAutoDataAppTables() {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.* from ( ");
        sql.append(
                "select  s.id as id,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,s.bill_code as billCode, ");
        sql.append(
                "s.loan_acc as loanAcc,s.create_date as createDate,s.contract_code as contractCode,s.cur_code as curCode,s.loan_money as loanMoney, ");
        sql.append("r.repay_money as repayedMoney,s.loan_date as loanDate,s.repay_date as repayDate, ");
        sql.append("(case when s.status >= " + CmsConstant.COMPELTEXTENDATE
                + " then (select max(d.deferred_repay_date) from cms_loan_deferred d where d.li_code = s.bill_code and d.status = "
                + CmsConstant.COMPELETOTAL + ") else null end) as  deferredReplayDate, ");
        sql.append(
                "s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType,i.loan_term as loanTerm, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus,(select count(1) from cms_loan_repay where send_code =s.bill_code and status !="
                        + CmsConstant.DELETED + " and status < " + CmsConstant.COMPELETOTAL + " ) repayingCount ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code ");
        sql.append(
                " left join ( select send_code,sum(repay_money) as repay_money from cms_loan_repay repay where status>="
                        + CmsConstant.COMPELETOTAL + " group by send_code) r on s.bill_code = r.send_code ");
        sql.append("where s.status in (" + CmsConstant.COMPELETOTAL + ", " + CmsConstant.COMPELTEXTENDATE + ", "
                + CmsConstant.EXTENDDATING + ") ");
        sql.append(
                "and i.IS_AUTO_REPAY='1'  and not exists (select 1 from cms_loan_repay re where re.send_code = s.bill_code and (re.status in(97,-1))) ");
        sql.append(
                "and s.loan_money > (select BT_NVL_NUM(sum(repay_money), 0) from cms_loan_repay e where s.bill_code = e.send_code and e.status <> "
                        + CmsConstant.DELETED + ")");
        sql.append(" ) a  where 1=1 and a.repayingCount=0 ");
        Map<String, Object> params = new HashMap<String, Object>();
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanSendVO.class);
    }
    /**
     * <b>功能介绍：</b>贷款还款计划修改列表<br>
     * <b>日期：</b>2017年9月21日<br>
     * <b>作者：</b>songkk<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataRepayPlanable(DataTablesInput input) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.* from ( ");
        sql.append(
                "select  s.id as id,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,s.bill_code as billCode, ");
        sql.append(
                "s.loan_acc as loanAcc,s.create_date as createDate,s.cur_code as curCode,s.loan_money as loanMoney, ");
        sql.append("r.repay_money as repayedMoney,s.loan_date as loanDate,s.repay_date as repayDate, ");
        sql.append("s.deferred_date as  deferredReplayDate,s.deferred_rate as deferredRate, ");
        sql.append(
                "s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType,i.loan_term as loanTerm, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus,(select count(1) from cms_loan_repay where send_code =s.bill_code and status !="
                        + CmsConstant.DELETED + " and status < " + CmsConstant.COMPELETOTAL + " ) repayingCount ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code ");
        // sql.append(" left join cms_loan_deferred d on d.li_code = s.bill_code and d.status =
        // "+CmsConstant.COMPELETOTAL+"");
        sql.append(
                " left join ( select send_code,sum(repay_money) as repay_money from cms_loan_repay repay where status>="
                        + CmsConstant.COMPELETOTAL + " group by send_code) r on s.bill_code = r.send_code ");
        sql.append("where s.status in (" + CmsConstant.COMPELETOTAL + ", " + CmsConstant.EXTENDDATING + ", "
                + CmsConstant.COMPELTEXTENDATE + ") ");
        sql.append(
                "and s.loan_money > (select BT_NVL_NUM(sum(repay_money), 0) from cms_loan_repay e where s.bill_code = e.send_code and e.status <> "
                        + CmsConstant.DELETED + ")");
        sql.append(" ) a  where 1=1 ");
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>贷款展期申请列表<br>
     * <b>日期：</b>2017年8月7日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput loanDeferredList(DataTablesInput input, String isMoreDeferred, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select a.* from ( ");
        sql.append(
                "select s.id as id,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,s.bill_code as billCode, ");
        sql.append(
                "s.create_date as createDate,s.cur_code as curCode,s.loan_money as loanMoney,r.repay_money as repayedMoney, ");
        sql.append(
                "s.contract_code as contractCode,s.provide_loan_acc as provideLoanAcc,s.loan_date as loanDate,s.loan_acc as loanAcc, ");
        sql.append(
                "s.repay_date as repayDate,s.repay_type as repayType,i.loan_class as loanClass,i.loan_type as loanType, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "i.loan_term as loanTerm,i.tern_unit as ternUnit,s.status as status,s.biz_status as bizStatus,(select count(1) from cms_loan_deferred where li_code = s.bill_code and status>="
                        + CmsConstant.APPROVED + " ) as deferredNum ");
        sql.append("from cms_loan_send s join cms_loan_info i on s.loan_code = i.bill_code ");
        sql.append("left join (select send_code, sum(repay_money) repay_money from cms_loan_repay where status >= "
                + CmsConstant.COMPELETOTAL + " group by send_code) r on s.Bill_Code = r.send_code ");
        sql.append("where s.status in (" + CmsConstant.COMPELETOTAL + ", " + CmsConstant.COMPELTEXTENDATE
                + ") and s.loan_money > (select BT_NVL_NUM(sum(repay_money), 0) from cms_loan_repay where s.bill_code = cms_loan_repay.send_code ");
        sql.append("and cms_loan_repay.status >= " + CmsConstant.COMPELETOTAL + ")) a ");
        sql.append("Left join (select BT_NVL_NUM(sum(repay_money), 0) repay_money, send_code from cms_loan_repay ");
        sql.append("where status >= " + CmsConstant.TEMP + " and status < " + CmsConstant.COMPELETOTAL
                + "  group by send_code) b ");
        sql.append("on b.send_code = a.billCode ");
        sql.append("where (b.repay_money is null OR b.repay_money <= 0) ");
        if ("1".equals(isMoreDeferred)) {// 一次展期
            sql.append("and a.deferredNum=0 ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corpId"));
        // sql.append("order by repayDate ");
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月19日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select s.id,s.corp_id as corpId,i.net_code as netId,s.loan_code as loanCode,s.bill_code as billCode,");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName, ");
        sql.append(
                "s.create_date as createDate,s.cur_code as curCode,i.loan_money as loanInfoMoney,s.loan_money as loanMoney,s.loan_rate as loanRate,s.loan_date as loanDate,");
        sql.append("s.repay_date as repayDate,i.loan_term as loanTerm,i.tern_unit as ternUnit ");
        sql.append("from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code where 1=1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "s.id")).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>根据billcode获取<br>
     * <b>日期：</b>2017年8月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanSend getCmsLoanSendByBillCode(String billCode) {
        return getUniqueBy("billCode", billCode);
    }
    /**
     * <b>功能介绍：</b>根据loancode查询发放记录<br>
     * <b>日期：</b>2017年11月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param loanCode
     * @return
     */
    public List<CmsLoanSend> getCmsLoanSendByLoanCode(String loanCode) {
        String hql = "from CmsLoanSend s where s.loanCode = ?0 ";
        return listByHQL(hql, loanCode);
    }
    public List<CmsLoanSend> getLoanSendByLoanCode(String loanCode) {
        String sql = "select * from cms_loan_send s where s.loan_code = :loanCode ";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanCode", loanCode);
        return listBySQLAliasToBean(sql, params, CmsLoanSend.class);
    }
    /**
     * <b>功能介绍：</b>根据id查询发放数据<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanSendVO getCmsLoanSendById(String id) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(getCmsLoanSendSQL());
        sql.append("from cms_loan_send s ");
        sql.append(" left join cms_repay_type rt on s.repay_type=rt.id ");
//        sql.append(",r.base_type_name as referenceRateTypeName,p.period_name as repayCycleName from cms_loan_send s ");
//        sql.append("left join tb_base_rate r on s.reference_rate_type = r.id ");
        sql.append(" left join cms_loan_info i on i.bill_code = s.loan_code ");
//        sql.append("left join tb_credit_period p on s.repay_cycle = p.id where s.id = :id ");
        sql.append("where s.id = :id ");
        params.put("id", id);
        List<CmsLoanSendVO> sendVO = listBySQLAliasToBean(sql.toString(), params, CmsLoanSendVO.class);
        if (sendVO.size() > 0) {
            return sendVO.get(0);
        } else {
            return null;
        }
    }
    /**
     * <b>功能介绍：</b>根据id查询发放数据<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanSendVO getCmsLoanSendVOByBillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(getCmsLoanSendSQL());
        sql.append(
                " , i.net_Code as netId ,i.create_by as infoCreateBy,i.update_by as infoUpdateBy,s.create_by as sendCreateBy  ");
        sql.append(" from cms_loan_send s ");
        sql.append(" left join cms_loan_info i on i.bill_code = s.loan_code ");
        sql.append(" left join cms_repay_type rt on s.repay_type=rt.id ");
        sql.append(" where 1 = 1 and s.bill_code=:billCode");
        params.put("billCode", billCode);
        List<CmsLoanSendVO> sendVO = listBySQLAliasToBean(sql.toString(), params, CmsLoanSendVO.class);
        if (sendVO.size() > 0) {
            return sendVO.get(0);
        } else {
            return null;
        }
    }
    /**
     * <b>功能介绍：</b>贷款发放详细数据<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @return
     */
    public String getCmsLoanSendSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select s.id,s.contract_code,s.loan_code,s.corp_id,s.loan_money,s.loan_acc,s.loan_date,s.repay_date,");
        sql.append(
                "s.loan_rate,s.provide_loan_acc,s.cur_code,s.overdue_rate,s.deferred_rate,s.tex_rate,s.net_acc,s.status,");
        sql.append(
                "s.voucher_no,s.order_no,s.voucher_no1,s.order_no1,s.refuse_why,s.loan_send_out_type,s.float_way,s.points_proportion_text,");
        sql.append(
                "s.points_proportion_select,s.rate_entries_effect_date,s.interest_way,s.reference_rate_type,s.repay_cycle,");
        sql.append(
                "s.ratetimeoftakeeffect,s.pay_type,s.send_time,s.borrow_corp_id,s.repay_type,rt.repay_name as repayTypeName,s.clb_agreement_rate,s.clb_fixed_rate,");
        sql.append(
                "s.ploadcode,s.bill_code,s.attachment_id,s.biz_status,s.deferred_date,i.float_way_next as floatWayNext, ");
        sql.append("s.create_date as createDate, ");
        //TODO 补充名称相关信息
        sql.append("i.net_name as netName,i.corp_name as corpName,i.cur_name as curName ");
        return sql.toString();
    }
    /**
     * <b>功能介绍：</b>根据贷款申请编号获取放款次数<br>
     * <b>日期：</b>2017年8月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public Integer getCmsLoanSendCountByloanCode(String loanCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                "select count(1) sendCount from cms_loan_info i left join cms_loan_send s on i.bill_code = s.loan_code where s.loan_code = :loanCode and s.status >= "
                        + CmsConstant.COMPELETOTAL);
        params.put("loanCode", loanCode);
        BigDecimal sendCount = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return sendCount.intValue();
    }
    /**
     * <b>功能介绍：</b>根据贷款单号获取已发放金额<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public BigDecimal getSendedMoney(String loanCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        // StringBuilder sql = new StringBuilder("select BT_NVL_NUM(sum(s.loan_money),0) as sendedMoney from
        // cms_loan_send s where s.loan_code = :loanCode and s.status != " + CmsConstant.DELETED);
        StringBuilder sql = new StringBuilder(
                "select BT_NVL_NUM(sum(s.loan_money),0) as sendedMoney from cms_loan_send s where s.loan_code = :loanCode and s.status not in(:status)");
        params.put("loanCode", loanCode);
        params.put("status", new Integer[] { CmsConstant.DELETED, CmsConstant.BACK });
        return (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
    }
    /**
     * <b>功能介绍：</b>判断发放合同号是否已存在<br>
     * <b>日期：</b>2017年8月11日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean existsContractCode(String contractCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) count from cms_loan_send s where s.contract_code = :contractCode and s.status != "
                        + CmsConstant.DELETED);
        params.put("contractCode", contractCode);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>判断是否可以发放<br>
     * <b>日期：</b>2018年1月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param loanCode
     * @return
     */
    public boolean validateSendStatus(String loanCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) from cms_loan_send s where s.loan_code=:loanCode and s.status > :status0 and s.status < :status1 ");
        params.put("loanCode", loanCode);
        // params.put("status0", CmsConstant.DELETED);
        params.put("status0", CmsConstant.BACK);
        params.put("status1", CmsConstant.COMPELETOTAL);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>发放次数是否正确<br>
     * <b>日期：</b>2018年1月2日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param loanCode
     * @param sendTime
     * @return
     */
    public boolean validateSendTime(String loanCode, int sendTime) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "select count(1) from cms_loan_send s where s.loan_code=:loanCode and s.status>=:status and s.send_time = :sendTime ");
        params.put("loanCode", loanCode);
        params.put("status", CmsConstant.COMPELETOTAL);
        params.put("sendTime", sendTime);
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>查询到期提醒的数据<br>
     * <b>日期：</b>2017年11月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param EndDate
     * @return
     */
    public List<CmsLoanSend> getSendExpirationWarnData(Date EndDate) {
        StringBuilder hql = new StringBuilder();
        hql.append(
                "from CmsLoanSend s where s.repayDate <= :endDate and s.status>:Dstatus and s.status < :status and (s.sendSign is null or s.sendSign = :sendSign ) ");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("endDate", EndDate);
        params.put("Dstatus", CmsConstant.DELETED);
        params.put("status", CmsConstant.FINISH);
        params.put("sendSign", CommonConstant.SEND_SIGN_NO);
        return listByHQL(hql.toString(), params);
    }
    /**
     * <b>功能介绍：</b>查询内部贷款发放到期<br>
     * <b>日期：</b>2017年11月16日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param netId
     * @param endDate
     * @return
     */
    public List<CmsLoanSendOutCheckVO> getLoanSendOutInfoByEndDate(String netId, Date endDate) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(
                "select s.bill_code as billCode,s.create_date as loanDate,s.loan_acc as loanAcc,i.cur_code as curId,s.loan_money as loanSendOutMoney,");
        //TODO 补充币种名称
        sql.append(" i.cur_name as curName,");
        sql.append(
                " s.loan_date as loanSendOutDate,s.repay_date as repayDate,s.repay_type as repayType,rt.repay_name as repayTypeName,i.loan_class as loanClass,");
        sql.append(" i.loan_term as loanTerm,i.tern_unit as ternUnit,s.status as status,i.corp_name as loanCorp ");
        sql.append(" from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code ");
        sql.append(" left join cms_repay_type rt on s.repay_type=rt.id ");
        //TODO 单位录入时就是从外部获取的
//        sql.append(" left join sys_corp sc on sc.id = s.corp_id ");
        sql.append(" where (s.status > :status0 and s.status < :status1 ) ");
        if (EmptyUtils.isNotEmpty(netId)) {
            sql.append("and s.corp_id = :netId ");
//            sql.append("and sc.net_id = :netId ");
            params.put("netId", netId);
        }
        if (!EmptyUtils.isEmpty(endDate)) {
            sql.append("and s.repay_date <= :endDate ");
            params.put("endDate", endDate);
        }
        params.put("status0", CmsConstant.DELETED);
        params.put("status1", CmsConstant.FINISH);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanSendOutCheckVO.class);
    }
    /**
     * 待记账查询sql
     *
     * @return
     */
    public String querySql() {
        String sql = "select s.id as id,i.bill_code as infoBillCode,s.status as status,s.biz_status as bizStatus,i.corp_id as corpId,"
                + " i.loan_money as loanMoney,i.loan_term as loanTerm,i.tern_unit as ternUnit,i.cur_code as curCode,s.bill_code as sendBillCode,"
                + " s.loan_date as loanDate,s.pay_type as payType,s.contract_code as contractCode,s.loan_money as sendMoney,s.interest_way as interestWay,"
                + " s.loan_rate as loanRate,s.deferred_rate as deferredRate,s.overdue_rate as overdueRate,s.loan_acc as loanAcc,rt.repay_name as repayType,"
                + " i.net_code as netCode,s.provide_loan_acc as provideLoanAcc,i.loan_genre as loanGenre,t.name as loanGenreName,s.repay_date as repayDate,"
                + " s.deferred_date as deferredDate,i.purpose as purpose ,s.remind_sign as remindSign,repayType.repay_name as repayTypeName "
                + " from cms_loan_send s left join cms_loan_info i on s.loan_code = i.bill_code "
                //TODO 放款中记账数据（主动查的，与还款不同）
//                + " left join cms_loan_type t on t.code = i.loan_genre where t.type = :type and t.status != -2 and s.order_no is not null "
                + " left join cms_loan_type t on t.code = i.loan_genre  "
                + " left join cms_repay_type repayType on s.repay_Type = repayType.id "
                + " left join cms_repay_type rt on s.repay_type = rt.id "
                + " where t.type = :type and t.status != -2 "
//                + " left join cms_loan_type t on t.code = i.loan_genre left join fc_accbook book on s.order_no = book.order_no where t.type = :type and t.status != -2 "
                + " and s.status in (:status) ";
        return sql;
    }
    /**
     * <b>功能介绍：</b>查询手工记账业务数据<br>
     * <b>日期：</b>2018年3月26日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     * @param input
     * @param opType
     * @param userId
     * @return
     */
    public DataTablesOutput getLoanTallyList(DataTablesInput input, String opType, String userId,
            List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(querySql());
        params.put("type", BaseDataConstant.TYPE_CREDIT);
        if ("add".equals(opType)) {
            params.put("status", new Integer[] { CmsConstant.WAITTOTAL, CmsConstant.NBC_STATUS_MODIFY_BACK });
        } else if ("edit".equals(opType)) {
            //TODO 记账数据变更
            sql.append(" and s.operator_code = :operatorCode ");
            params.put("operatorCode", userId);// 只能修改自己的账务
            params.put("status", new Integer[] { CmsConstant.NBC_STATUS_TEMP, CmsConstant.NBC_STATUS_CHECK_BACK,
                    CmsConstant.NBC_STATUS_MODIFY_COMPLETE });
        } else if ("check".equals(opType)) {
            sql.append(" and s.operator_code != :operatorCode ");
            params.put("operatorCode", userId);// 不能复核自己的账务
            params.put("status", new Integer[] { CmsConstant.NBC_STATUS_SUBMIT });
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanSendTallyQueryVO.class, "", false, params);
    }
    /**
     * 查询发放待记账的数据
     *
     * @return
     */
    public List<CmsLoanSendDTO> querySendTallyData() {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(querySql());
        sql.append(" and (s.remind_sign = '0' or s.remind_sign is null)");
        params.put("type", BaseDataConstant.TYPE_CREDIT);
        params.put("status", CmsConstant.WAITTOTAL);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanSendDTO.class);
    }
    /**
     * 单位校验使用-待记账
     *
     * @Param corpId
     * @return
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select count(s.id) ");
        sql.append(" from cms_loan_send s  ");
        sql.append(" left join cms_loan_info i on s.loan_code = i.bill_code");
        sql.append(" left join cms_loan_type t on t.code = i.loan_genre");
//        sql.append(" left join fc_accbook book on s.order_no = book.order_no ");
        sql.append(" where 1= 1 ");
        sql.append(" and t.type = 1 and t.status != -2");
        sql.append(" and s.status = 97 ");
        //TODO 补充记账数据
        sql.append(" and s.order_no is not null ");
        sql.append(" and s.corp_id = :corpId");
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        return Integer.valueOf(createSQLQuery(sql.toString(), param).uniqueResult().toString());
    }
}
