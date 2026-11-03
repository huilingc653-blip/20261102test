package com.bytter.finance.domain.loan.dao;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.constant.BaseDataConstant;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.finance.domain.loan.entity.CmsLoanInfo;
import com.bytter.finance.domain.loan.entity.CmsLoanSend;
import com.bytter.finance.domain.loan.vo.*;
import com.bytter.finance.dto.CmsLoanInfoDTO;
import com.bytter.finance.dto.GuaContractCmsLoanDTO;
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
 * @Description: 贷款信息表
 * @date 2017-07-21 09:46:16
 */
@Repository
public class CmsLoanInfoDao extends BaseDao<CmsLoanInfo, String> {
    /**
     * <b>功能介绍：</b>根据编码查找<br>
     * <b>日期：</b>2017年7月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanInfo findByBillCode(String billCode) {
        return getUniqueBy("billCode", billCode);
    }
    /**
     * <b>功能介绍：</b>页面列表数据<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput listCmsLoanInfo(DataTablesInput input, String pageType, List<String> corpAuth,
                                            String userId) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCmsLoanInfoSQL());
        sql.append("from cms_loan_info i left join cms_loan_type t on t.code = i.loan_genre and t.TYPE = 1  ");
        sql.append(" left join cms_repay_type rt on i.repay_type=rt.id where 1 = 1 ");
        //内部贷款补录查询重复优化
        sql.append("and t.type="+BaseDataConstant.TYPE_CREDIT+ " ");
        if ("edit".equals(pageType)) {// 需修改的数据
            sql.append(" and (i.status = " + CmsConstant.TEMP + " or i.status = " + CmsConstant.BACK + ") ");
            sql.append(" and i.create_by = '" + userId + "' ");
        } else if ("repair".equals(pageType)) {// 补录
            sql.append(" and i.status = " + CmsConstant.APPROVED + " ");
        } else if ("send".equals(pageType)) {// 贷款发放
            sql.append(" and i.status = " + CmsConstant.PREPARESEND + " ");
        }
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        return getDynamicBySQL(input, sql.toString(), CmsLoanInfoVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>贷款待发放列表数据<br>
     * <b>日期：</b>2017年8月8日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput listCmsLoanStaySendData(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select i.id as id,i.corp_id as corpId,i.net_code as netCode,i.bill_code as billCode,i.create_date as createDate, ");
        sql.append(
                "i.cur_code as curCode,i.loan_money as loanMoney,i.loan_date as loanDate,i.repay_date as repayDate,i.repay_type as repayType,rt.repay_name as repayTypeName, ");
        sql.append(
                "i.loan_send_out_type as loanSendOutType,i.loan_term as loanTerm,i.tern_unit as ternUnit,i.status as status,BT_NVL_CHAR(se.biz_status,i.biz_status) as bizStatus,");
        //TODO 补充相关名称信息
        sql.append("a.repay_money as repayMoney,b.send_out_money as sendMoney,se.bill_code as sendBillCode, i.net_name as netName,i.corp_name as corpName,i.cur_name as curName ");
        sql.append("from cms_loan_info i ");
        sql.append(" left join cms_repay_type rt on i.repay_type=rt.id  ");
        sql.append(
                "left join (select s.loan_code, sum(repay_money) repay_money from cms_loan_repay r, cms_loan_send s where r.send_code = s.bill_code ");
        sql.append("and r.status >= " + CmsConstant.COMPELETOTAL
                + " group by s.loan_code) a on a.loan_code = i.bill_code  ");
        sql.append(
                "left join (select send.loan_code,sum(send.loan_money) send_out_money from cms_loan_send send, cms_loan_info cms ");
        sql.append("where send.loan_code = cms.bill_code and send.status >= " + CmsConstant.COMPELETOTAL
                + " group by send.loan_code) b on b.loan_code = i.bill_code ");
        sql.append("left join cms_loan_send se on se.loan_code=i.bill_code and se.status >= -1 and se.status < 102 ");
        sql.append("where i.status = " + CmsConstant.PREPARESEND
                + " and ((b.send_out_money is null and i.loan_money > 0) or i.loan_money > b.send_out_money) ");
        sql.append("and not exists (select 1 from cms_loan_send ss where ss.loan_code=i.bill_code and (ss.status < "
                + CmsConstant.COMPELETOTAL + " and ss.status>" + CmsConstant.BACK + ") or ss.status = "
                + CmsConstant.NETWAITTOTAL + ") ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "i.corp_id"));
        DataTablesOutput result = getDynamicBySQL(input, sql.toString(), CmsLoanInfoVO.class, "", false);
        return result;
    }
    /**
     * 查询审批通过的担保合同
     *
     * @param dataTablesInput
     * @return
     */
//    public DataTablesOutput getApprovedDataTableCheck(DataTablesInput dataTablesInput, String corpId) {
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<>();
//        sql.append(
//                "SELECT B.ID AS id,B.CORP_ID AS corpId,B.GUA_NO AS guaNo,B.GUA_TYPE AS guaType,B.GUA_PERSON AS guaPerson,");
//        sql.append(
//                "B.DEBTOR_PERSON AS debtorPerson,B.CREDITOR_PERSON AS creditorPerson,B.CUR_ID AS curId,B.GUA_MONEY AS guaMoney,");
//        sql.append(
//                "B.CONTRACT_CODE AS contractCode,B.START_DATE AS startDate,B.END_DATE AS endDate,B.STATUS AS status,");
//        sql.append(
//                "B.WARR AS warr,B.RMK AS rmk,B.ATTACHMENT_ID AS attachmentId,B.CREATE_BY AS createBy,B.CREATE_DATE AS createDate,");
//        sql.append("B.UPDATE_BY AS updateBy,B.UPDATE_DATE AS updateDate,BT_NVL(B.INNER_GUA,'0') AS innerGua  ");
//        sql.append(
//                "FROM GUA_CONTRACT_INFO B LEFT JOIN SYS_CORP C ON B.CORP_ID=C.ID LEFT JOIN BT_CURRENCY D ON B.CUR_ID=D.ID ");
//        sql.append("WHERE (B.BUSINESS_TYPE=0 or B.BUSINESS_TYPE is null) AND B.STATUS IN(95) ");
//        sql.append("AND C.ID= :ID ");
//        params.put("ID", corpId);
//        // 使用本服务自己VO
//        return getDynamicBySQL(dataTablesInput, sql.toString(), LoanContractInfoVO.class, "", false, params);
//    }
    /**
     * <b>功能介绍：</b>审批列表数据<br>
     * <b>日期：</b>2017年8月18日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("rawtypes")
    public DataTablesOutput dataTableApprove(DataTablesInput input, List<String> ids) {
        StringBuilder sql = new StringBuilder();
        sql.append(getCmsLoanInfoSQL());
        sql.append("from cms_loan_info i left join cms_loan_type t on t.code = i.loan_genre and t.type = ' " + BaseDataConstant.TYPE_CREDIT + " ' ");
        sql.append(" left join cms_repay_type rt on i.repay_type=rt.id where 1 = 1 ");
        if (!EmptyUtils.isEmpty(ids) && ids.size() > 0) {
            sql.append(" and ").append(getSQLIn(ids, 900, "i.id")).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanInfoVO.class, "", false);
    }
    /**
     * <b>功能介绍：</b>贷款已发放金额<br>
     * <b>日期：</b>2017年9月15日<br>
     * <b>作者：</b>caozx<br>
     *
     * @param loanCode
     * @return
     * @version 1.0
     */
    public BigDecimal getSendedMoney(String loanCode) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("select BT_NVL_NUM(sum(send.loan_money),0) sendMoney from cms_loan_send send, cms_loan_info cms ");
        sql.append("where send.loan_code = cms.bill_code and send.status >=" + CmsConstant.COMPELETOTAL);
        sql.append(" and send.loan_code=:loanCode");
        params.put("loanCode", loanCode);
        BigDecimal sendMoney = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        return sendMoney;
    }
    /**
     * <b>功能介绍：</b>是否存在待处理的发放记录<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    @SuppressWarnings("unchecked")
    public boolean existsStayExcLoanSend(String loanCode) {
        boolean flag = false;
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("loanCode", loanCode);
        StringBuilder sql = new StringBuilder("select s.status from cms_loan_send s where s.loan_code=:loanCode");
        List<BigDecimal> statuss = createSQLQuery(sql.toString(), params).list();
        for (BigDecimal status : statuss) {
            if (status.intValue() < CmsConstant.COMPELETOTAL && status.intValue() > CmsConstant.BACK
                    || status.intValue() == CmsConstant.NETWAITTOTAL) {
                flag = true;
                break;
            }
        }
        return flag;
    }
    /**
     * <b>功能介绍：</b>根据ID查询贷款申请单<br>
     * <b>日期：</b>2017年7月25日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanInfoVO getCmsLoanInfoById(String id) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ii.* from (");
        sql.append(getCmsLoanInfoSQL());
        sql.append(" from cms_loan_info i left join cms_loan_type t on t.code = i.loan_genre and t.type = ' " + BaseDataConstant.TYPE_CREDIT + " ' ");
        sql.append(" left join cms_repay_type rt on i.repay_type=rt.id ");
        sql.append(" where i.id = :id ");
        sql.append(") ii ");
//        sql.append("left join tb_base_rate r on ii.referenceRateType = r.id ");
//        sql.append("left join tb_credit_period p on ii.repayCycle = p.id ");
        params.put("id", id);
        List<CmsLoanInfoVO> result = listBySQLAliasToBean(sql.toString(), params, CmsLoanInfoVO.class);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    /**
     * <b>功能介绍：</b>根据BillCode查询贷款申请单<br>
     */
    public CmsLoanInfoVO getLoanInfoByBillCode(String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append("select ii.* from (");
        sql.append(getCmsLoanInfoSQL());
        sql.append(" from cms_loan_info i left join cms_loan_type t on t.code = i.loan_genre and t.type = ' " + BaseDataConstant.TYPE_CREDIT + " ' ");
        sql.append(" left join cms_repay_type rt on i.repay_type=rt.id ");
        sql.append(" where i.bill_code = :billCode ");
        sql.append(") ii ");
//        sql.append("left join tb_base_rate r on ii.referenceRateType = r.id ");
//        sql.append("left join tb_credit_period p on ii.repayCycle = p.id ");
        params.put("billCode", billCode);
        List<CmsLoanInfoVO> result = listBySQLAliasToBean(sql.toString(), params, CmsLoanInfoVO.class);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }
    /**
     * <b>功能介绍：</b>根据贷款编号查询发放列表<br>
     * <b>日期：</b>2017年8月9日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public List<CmsLoanSendVO> getLoanSendItem(String loanCode, String type) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select  s.bill_code as billCode,s.create_date as createDate,i.cur_code as curCode,i.loan_money as loanInfoMoney,s.loan_money as loanMoney,s.loan_date as loanDate,s.repay_date as repayDate,");
        //TODO 补充币种名称
        sql.append("i.cur_name as curName, ");
        sql.append(
                "s.loan_rate as loanRate,s.loan_send_out_type as loanSendOutType,s.repay_type as repayType,rt.repay_name as repayTypeName,i.loan_term as loanTerm,i.tern_unit as ternUnit,s.status,s.biz_status ");
        sql.append("from cms_loan_info i left join cms_loan_send s on i.bill_code = s.loan_code ");
        sql.append(" left join cms_repay_type rt on s.repay_type=rt.id ");
        if ("1".equals(type)) {// 报表发放明细查询
            sql.append("where s.bill_code=:loanCode order by s.bill_code ");
        } else {// 待发放列表发放明细查询
            sql.append("where s.loan_code=:loanCode order by s.bill_code ");
        }
        // sql.append("and s.status!="+CmsConstant.DELETED);
        params.put("loanCode", loanCode);
        return listBySQLAliasToBean(sql.toString(), params, CmsLoanSendVO.class);
    }
    /**
     * <b>功能介绍：</b>统计是否是第一次分发<br>
     * <b>日期：</b>2017年7月29日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public int isFirstPay(String billCode) {
        int count = 0;
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder(
                "from CmsLoanSend s where s.loanCode = :billCode and s.status >= " + CmsConstant.COMPELETOTAL);
        params.put("billCode", billCode);
        List<CmsLoanSend> list = listByHQL(sql.toString(), params, false);
        if (list == null || list.size() == 0) {
            count = 0;
        } else {
            count = list.size();
        }
        return count;
    }
    /**
     * <b>功能介绍：</b>判断贷款合同号是否存在<br>
     * <b>日期：</b>2017年8月10日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public boolean existsContractCode(String contractCode, String billCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractCode", contractCode);
        StringBuilder sql = new StringBuilder(
                "select count(1) count from cms_loan_info i  where i.contract_code = :contractCode ");
        if (EmptyUtils.isNotEmpty(billCode)) {
            sql.append(" and i.bill_code != :billCode ");
            params.put("billCode", billCode);
        }
        BigDecimal count = (BigDecimal) createSQLQuery(sql.toString(), params).uniqueResult();
        if (count.intValue() > 0) {
            return true;
        }
        return false;
    }
    /**
     * <b>功能介绍：</b>根据billCode查找贷款申请<br>
     * <b>日期：</b>2017年8月14日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public CmsLoanInfo getCmsLoanInfoBybillCode(String billCode) {
        return getUniqueBy("billCode", billCode);
    }
    public CmsLoanInfoDTO getLoanInfoBybillCode(String billCode) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from cms_loan_info where bill_code = :billCode");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("billCode", billCode);
        return getBySQLAliasToBean(sql.toString(), param, CmsLoanInfoDTO.class);
    }
    /**
     * <b>功能介绍：</b>贷款申请详细数据<br>
     * <b>日期：</b>2017年7月24日<br>
     * <b>作者：</b>caozx<br>
     *
     * @version 1.0
     */
    public String getCmsLoanInfoSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append(
                "select i.id as id,i.net_code as netcode,i.corp_id as corpid,i.bill_code as billcode,i.contract_code as contractcode,");
        sql.append(
                "i.IS_AUTO_REPAY as isAutoRepay,i.IS_OVER_DISTRIBUTE as isOverDistribute,i.IS_OVER_REPAIR as isOverRepair, ");
        sql.append(
                "i.loan_money as loanmoney,i.loan_term as loanterm,i.tern_unit as ternunit,i.cur_code as curcode,i.loan_date as loandate,");
        sql.append(
                "i.repay_date as repaydate,i.repay_type as repaytype,rt.repay_name as repayTypeName,i.loan_class as loanclass,i.loan_genre as loangenre, t.name as loanGenreName,");
        sql.append(
                "i.loan_type as loantype,i.surety_explain as suretyexplain,i.surety_money as suretymoney,i.provide_loan_acc as provideloanacc,");
        sql.append(
                "i.loan_rate as loanrate,i.overdue_rate as overduerate,i.deferred_rate as deferredrate,i.repair_op as repairop,i.status as status,i.biz_status as bizStatus,");
        sql.append(
                "i.loan_grade as loangrade,i.purpose as purpose,i.rmk as rmk,i.refuse_why as refusewhy,i.is_hang_credit as ishangcredit,");
        sql.append(
                "i.ration_code as rationcode,i.item_code as itemcode,i.loan_send_out_type as loansendouttype,i.float_way as floatway,");
        sql.append(
                "i.points_proportion_text as pointsproportiontext,i.points_proportion_select as pointsproportionselect,");
        sql.append(
                "i.rate_entries_effect_date as rateentrieseffectdate,i.interest_way as interestway,i.reference_rate_type as referenceratetype,");
        sql.append(
                "i.repay_cycle as repaycycle,i.ratetimeoftakeeffect as ratetimeoftakeeffect,i.send_time as sendtime,i.clb_agreement_rate as clbagreementrate,");
        sql.append(
                "i.borrow_corp_id as borrowcorpid,i.pay_type as paytype,i.clb_fixed_rate as clbfixedrate,i.ploadcode as ploadcode,");
        sql.append(
                "i.industry_code as industrycode,i.create_date as createDate,i.attachment_id as attachmentId,i.float_way_next as floatWayNext,  ");
        sql.append("i.IS_HANG_GUARANTEE as isHangGuarantee,i.GUA_CONTRACT as guaContract, ");
        sql.append("i.con_type_id as conTypeId,i.con_id as conId,i.net_name as netName,i.corp_name as corpName,i.cur_name as curName ");
        return sql.toString();
    }
    /***
     * 内部贷款查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput dataTableLoanQuery(DataTablesInput input, List<String> corpAuth) {
        /*
         * if(!EmptyUtils.isEmptyList(input.getOrder())&&input.getOrder().get(0).getColumn()>1){
         * input.addOrder("infoBillCode", true); List<Order> list = input.getOrder(); Collections.sort(list,new
         * Comparator<Order>() { public int compare(Order o1, Order o2) { return
         * o1.getColumn().compareTo(o2.getColumn()); } }); input.setOrder(list); }
         */
        StringBuilder sql = new StringBuilder();
        sql.append(
                " select a.bill_code as infoBillCode ,a.net_code as netCode ,a.corp_id as corpId, a.Contract_code as contractCode  , s.bill_Code as sendBillCode, ");
        //TODO 补充名称相关信息
        sql.append("a.net_name as netName,a.corp_name as corpName,a.cur_name as curName, ");
        sql.append(
                " a.create_date as createDate , a.cur_code as curCode , a.tern_Unit as ternUnit, a.loan_money as loanMoney , s.loan_money as sendMoney ,  ");
        sql.append(" repayLoan.repayMoney as repayMoney, BT_NVL_NUM(repayLoan.repayAccrual,0) as repayAccrual, ");
        sql.append(
                " (s.loan_money - BT_NVL_NUM(repayLoan.repayMoney,0) - BT_NVL_NUM(repayAwayLoan.repayAwayMoney,0)) as stayMoney,a.con_id as conId,");
        sql.append(
                " repayAwayLoan.repayAwayMoney as repayAwayMoney , a.loan_date as loanDate ,a.repay_date as repayDate, s.repay_type as repayType ,rt.repay_name as repayTypeName,a.LOAN_CLASS as loanClass, s.loan_acc as loanAcc,");
        sql.append(" a.loan_term as loanTerm , a.loan_genre as loanGener , a.status as status ,");
        sql.append(
                " case when s.biz_status is null then a.biz_status else s.biz_status end as bizStatus, a.loan_type as loanType, a.loan_Genre as loanGenre,t.name as loanGenreName,def.deferredNum ");
        sql.append(
                " from cms_loan_info a  left join cms_loan_send s  on a.bill_code = s.loan_code and s.status != -2 ");
        sql.append(" left join cms_repay_type rt on s.repay_type=rt.id ");
        sql.append(
                " left join (select BT_NVL_NUM(count(0),0) deferredNum,d.li_code from cms_loan_deferred d where d.status !=-2 group by d.li_code) def on def.li_code = s.bill_code ");
        sql.append(" left join cms_loan_type t on t.status != -2 and t.code = a.loan_genre and t.type = "
                + BaseDataConstant.TYPE_CREDIT);
        sql.append(
                " left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney,BT_NVL_NUM(sum(repay_accrual), 0) repayAccrual, clr.send_code ");
        sql.append(
                " from cms_loan_repay clr  where clr.status >= 102  and clr.status <> 120  group by clr.send_code) repayLoan ");
        sql.append(" on s.bill_code = repayLoan.send_code   ");
        sql.append(" left join (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, clr.send_code ");
        sql.append(
                " from cms_loan_repay clr where clr.status != -2   and (clr.status < 102 or  clr.status = 120) group by clr.send_code) repayAwayLoan ");
        sql.append(" on s.bill_code = repayAwayLoan.send_code ");
        sql.append(
                " left join (select def.li_code,   max(def.create_date) defenter_date,  max(def.deferred_repay_date) deferred_repay_date  from cms_loan_deferred def ");
        sql.append(" group by (def.li_code)) defe  on defe.li_code = a.bill_code  where 1 = 1 and a.status != -2 ");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id")).append(" ");
        return getDynamicBySQL(input, sql.toString(), CmsLoanQueryVO.class, "", false);
    }
    /**
     * 根据id修改还款方式
     *
     * @param id
     */
    public void updateRepayType(String id, String repayType) {
        Map<String, String> params = new HashMap<String, String>();
        StringBuilder sql = new StringBuilder();
        sql.append("update cms_loan_send s set s.repay_type =:repayType where s.id=:id");
        params.put("repayType", repayType);
        params.put("id", id);
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
    /***
     * 内部贷款发放完成数据查询
     *
     * @param input
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public DataTablesOutput loanInfoList(DataTablesInput input, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select a.id,a.bill_code as infoBillCode, ").append(" a.net_code as netCode, ")
                .append(" a.corp_id as corpId, ").append(" a.Contract_code as contractCode, ")
                //TODO 补充名称相关信息
                .append(" a.net_name as netName, a.corp_name as corpName,a.cur_name as curName, ")
                .append(" s.bill_Code as sendBillCode, ").append(" a.create_date as createDate, ")
                .append(" a.cur_code as curCode, ").append(" a.tern_Unit as ternUnit, ")
                .append(" a.loan_money as loanMoney, ").append(" s.loan_money as sendMoney, ")
                .append(" a.con_id as conId, ").append(" a.loan_date as loanDate, ")
                .append(" a.repay_date as repayDate, ").append(" a.repay_type as repayType, ")
                .append(" a.LOAN_CLASS as loanClass, ").append(" s.loan_acc as loanAcc, ")
                .append(" a.loan_term as loanTerm, ").append(" a.loan_genre as loanGener, ")
                .append(" a.status as status, ")
                .append(" case when s.biz_status is null then a.biz_status else s.biz_status end as bizStatus, ")
                .append(" a.loan_type as loanType, ").append(" a.loan_Genre as loanGenre, ")
                .append(" t.name as loanGenreName, ").append(" def.deferredNum ").append(" from cms_loan_info a ")
                .append(" left join cms_loan_send s ").append(" on a.bill_code = s.loan_code ")
                // .append(" and s.status in (102, 103, 104) ")
                .append(" left join (select BT_NVL_NUM(count(0), 0) deferredNum, d.li_code ")
                .append(" from cms_loan_deferred d ").append(" where d.status != -2 ")
                .append(" group by d.li_code) def ").append(" on def.li_code = s.bill_code ")
                .append(" left join cms_loan_type t ").append(" on t.status != -2 ")
                .append(" and t.code = a.loan_genre ").append(" and t.type = 1 ")
                .append(" left join (select def.li_code, ").append(" max(def.create_date) defenter_date, ")
                .append(" max(def.deferred_repay_date) deferred_repay_date ").append(" from cms_loan_deferred def ")
                .append(" group by (def.li_code)) defe ").append(" on defe.li_code = a.bill_code ")
                .append(" where 1 = 1 ").append(" and a.status>=96  and a.status<110 ");
        if (StringUtils.isNotEmpty(corpAuth)) {
            sql.append(" and ").append(getSQLIn(corpAuth, 900, "a.corp_id")).append(" ");
        }
        return getDynamicBySQL(input, sql.toString(), CmsLoanQueryVO.class, "", false);
    }
    /**
     * 单位校验使用
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDeleteByCorpId(String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(*) from CMS_LOAN_INFO where 1 = 1 and status != -2 and corp_id = ?");
        return Integer.valueOf(createSQLQuery(sql.toString(), corpId).uniqueResult().toString());
    }
    /**
     * 单位校验使用 -暂存,待审批,审批打回
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId(String corpId) {
        String sql = "select count(*) from CMS_LOAN_INFO where 1 = 1 and status not in (95,96) and status <> -2 and corp_id = ?";
        Integer num = Integer.valueOf(createSQLQuery(sql, corpId).uniqueResult().toString());
        String sql1 = "select count(*) from cms_loan_send where 1 = 1 and status in (11,-2) and corp_id = ?";
        Integer num1 = Integer.valueOf(createSQLQuery(sql1, corpId).uniqueResult().toString());
        String sql2 = "select count(*) from cms_loan_repay where 1 = 1 and status in (0,11,-1) and corp_id = ?";
        Integer num2 = Integer.valueOf(createSQLQuery(sql2, corpId).uniqueResult().toString());
        String sql3 = "select count(*) from cms_loan_deferred where 1 = 1 and status in (0,11,-1) and corp_id = ?";
        Integer num3 = Integer.valueOf(createSQLQuery(sql3, corpId).uniqueResult().toString());
        num = num + num1 + num2 + num3;
        return num;
    }
    /**
     * 单位校验使用 -补录、待发放、待生成合同
     *
     * @return
     * @Param corpId
     */
    public Integer verifyCorpDisableByCorpId1(String corpId) {
        String sql = "select count(*) from CMS_LOAN_INFO where 1 = 1 and status in (95) and corp_id = ?";
        Integer num = Integer.valueOf(createSQLQuery(sql, corpId).uniqueResult().toString());
        String sql1 = "select count(*) from CMS_LOAN_INFO i  where 1 = 1  and i.status in (96)  and i.corp_id = ? "
                + "and  not exists (select 1 from cms_loan_send s  where s.loan_code  = i.bill_code ) ";
        Integer num1 = Integer.valueOf(createSQLQuery(sql1, corpId).uniqueResult().toString());
        String sql2 = "select count(*) from cms_loan_send s where 1 = 1 and s.status = 95 and s.corp_id = ? "
                + " and  not exists (select 1 from cms_contract c where s.bill_code = c.li_code ) ";
        Integer num2 = Integer.valueOf(createSQLQuery(sql2, corpId).uniqueResult().toString());
        return num + num1 + num2;
    }
    //TODO 这个有问题  没时间 后续出现再改（‘龙少’移过来的）
    public DataTablesOutput<GuaContractCmsLoanVO> getGuaForLoan(DataTablesInput input, List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select cli.bill_code as billCode, ");
        sql.append(" cli.id as id,lType.name as loanGenre,cli.contract_code as contractCode,cli.cur_code as curCode, ");
        //TODO 补充名称相关信息
        sql.append(" cli.cur_name as curName,cli.corp_name as corpName, ");
        sql.append(" cli.loan_money as loanMoney,cli.loan_date as loanDate,cli.repay_date as repayDate,cli.corp_id as corpId,gco.status status,gco.use_date useDate ");
        sql.append(" from  CMS_LOAN_INFO cli inner join cms_loan_type lType on cli.loan_genre=lType.code ");
        sql.append(" left join (select max(m.use_date) use_date,m.status status,m.business_id business_id,syscode as sysCode,m.contract_id contract_id from gua_contract_occup m ");
        sql.append(" where m.sysCode = :sysCode ");
        Map<String,Object> params = new HashMap<>();
        if (input.getFormdata().get("ged_useDate") != null && StrUtil.isNotBlank(input.getFormdata().get("ged_useDate").toString())) {
            DateTime ged_useDate = DateUtil.beginOfDay(DateUtil.parse(input.getFormdata().get("ged_useDate").toString(),"yyyy-MM-dd"));
            sql.append(" and m.use_date >= :startDate ");
            params.put("startDate", ged_useDate.toJdkDate());
            input.getFormdata().put("ged_useDate",ged_useDate.toJdkDate());
        }
        if(input.getFormdata().get("led_useDate") != null && StrUtil.isNotBlank(input.getFormdata().get("led_useDate").toString())){
            DateTime led_useDate = DateUtil.endOfDay(DateUtil.parse(input.getFormdata().get("led_useDate").toString(),"yyyy-MM-dd"));
            sql.append(" and :endDate >= m.use_date ");
            params.put("endDate", led_useDate.toJdkDate());
            input.getFormdata().put("led_useDate",led_useDate.toJdkDate());
        }
        if (input.getFormdata().get("eqg_status") != null) {
            sql.append(" and m.status = :status ");
            params.put("status", input.getFormdata().get("eqg_status"));
        }
        sql.append(" group by business_id,status,syscode,contract_id ");
        sql.append(") gco on gco.business_id = cli.id ");
        params.put("sysCode", input.getFormdata().get("eqs_sysCode"));
        sql.append(" left join gua_contract_info gci on gco.contract_id = gci.id ");
        sql.append(" where 1=1 ");
        sql.append(" and lType.type=1 and cli.is_hang_guarantee = '1'");
        //sql.append(" and " + getSQLIn(businessIdList, 900, "cli.id"));
        if (EmptyUtils.isNotEmpty(corpIds)) {
            sql.append(" and " + getSQLIn(corpIds, 900, "cli.corp_id"));
        } else {
            sql.append(" and 1 <> 1 ");
        }


        return getDynamicBySQL(input, sql.toString(), GuaContractCmsLoanVO.class, "cli", false,params);
    }
    /**
     * 获取所有挂接担保的内部贷款数据
     * @return
     */
    public List<GuaContractCmsLoanDTO> getHangGuaForLoan() {
        StringBuilder sql = new StringBuilder();
        sql.append("select cli.bill_code as billCode, lType.name as loanGenre,cli.contract_code as contractCode,cli.cur_code as curCode, ");
        sql.append(" cli.cur_name as curName, cli.loan_money as loanMoney,cli.loan_date as loanDate,");
        sql.append(" cli.repay_date as repayDate,cli.id as id ");
        sql.append(" from CMS_LOAN_INFO cli inner join cms_loan_type lType on cli.loan_genre=lType.code ");
        sql.append(" where 1=1 ");
        sql.append(" and lType.type=1 and cli.is_hang_guarantee = '1'");
        return this.listBySQLAliasToBean(sql.toString(), null, GuaContractCmsLoanDTO.class);
    }
}
