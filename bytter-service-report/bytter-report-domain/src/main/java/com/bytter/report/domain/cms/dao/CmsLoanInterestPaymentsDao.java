package com.bytter.report.domain.cms.dao;


import cn.hutool.core.util.ObjectUtil;
import com.bytter.report.domain.cms.vo.CmsInterestPayVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CmsLoanInterestPaymentsDao extends BaseDao<CmsInterestPayVO, String> {

    public List<CmsInterestPayVO> getList(CmsInterestPayVO vo) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from (select interestPay.bankName, ");
        sql.append(querySql());
        sql.append(" from (select send.corp_id as corpId,send.loan_code as billCode,send.cur_code as curCode,send.bill_code as sendBillCode,");
        sql.append(" send.loan_money as sendMoney,send.loan_date as sendDate,send.repay_date as endDate,send.send_time as sendTime,");
        sql.append(" send.loan_rate as loanRate,loanInterestPay.prediceRepayDate,loanInterestPay.isRepay,loanInterestPay.surplusMoney,");
        sql.append(" send.deferred_date as deferredDate,send.deferred_rate as deferredRate,loanInterestPay.Cmstype,");
        sql.append(" loanInterestPay.repaymentIst,loanInterestPay.repaymentIst repaymentRelIst, loanInterestPay.repayMoney,' ' as bankName,'' as bankId,send.overdue_rate as overdueRate ");
        sql.append(" from cms_loan_send send ");
        sql.append(" left join (select schedule.cms_type as cmsType,schedule.bill_no as billNo,repayment.predice_repay_date as prediceRepayDate,");
        sql.append(" repayment.is_repay as isRepay, repayment.surplus_money  as surplusMoney,repayment.repayment_ist as repaymentIst,");
        sql.append(" repayment.repayment_money as repayMoney from cms_loan_send send  inner join cms_repayment_schedule schedule ");
        sql.append(" on send.id = schedule.bill_no inner join cms_repayment_scheduledetailed repayment ");
        sql.append(" on schedule.id = repayment.relation_id and repayment.valid_sign = 1) loanInterestPay on loaninterestPay.billNo = send.id");
        sql.append(" where 1 = 1");
        if (EmptyUtils.isNotEmpty(vo.getCorpId()) && !" ".equals(vo.getCorpId())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(vo.getCorpId().split(",")), 900, "send.corp_id") + ") ");
        }

        String cmsType = null;
        if (EmptyUtils.isNotEmpty(vo.getCmsType()) && !" ".equals(vo.getCmsType())) {
            if ("01".equals(vo.getCmsType())) {
                cmsType = "A2";
            }
            sql.append(" and send.bill_code like :cmsType ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchStartDate())) {
            sql.append(" and send.loan_date >= :startDate ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchEndDate())) {
            sql.append(" and send.loan_date <= :endDate ");
        }
        sql.append(" and (send.status = 102 or send.status = 104 )) interestpay");
        sql.append(" inner join sys_corp a on interestpay.corpId = a.id ");
        sql.append(" inner join bt_currency cur  on interestPay.curCode = cur.id ");//内部贷款结束
        if (EmptyUtils.isNotEmpty(vo.getBankName())) {
            sql.append(" and interestpay.bankName like :bankName");
        }
        sql.append(" union all ");
        sql.append(" select bank.bank_Name,");
        sql.append(querySql());
        sql.append(" from (select send.corp_id as corpId,send.loan_code as billCode,send.cur_code as curCode,");
        sql.append(" send.bill_code as sendBillCode,send.money as sendMoney,send.send_date as sendDate, send.repay_date as endDate,");
        sql.append(" send.send_time as sendTime,send.pl_rate as loanRate,loanInterestPay.cmsType,loanInterestPay.prediceRepayDate,");
        sql.append(" send.deferred_date as deferredDate,send.deferred_rate as deferredRate,loanInterestPay.isRepay,");
        sql.append(" loanInterestPay.surplusMoney,loanInterestPay.repaymentIst,loanInterestPay.repaymentRelIst repaymentRelIst,loanInterestPay.repayMoney,info.bank_id as bankId,  ");
        sql.append(" send.overdue_rate as overdueRate ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_provide_loan_info info  on info.bill_code = send.loan_code ");
        sql.append(" left join (select schedule.cms_type as cmsType,schedule.bill_no as billNo,pro.repay_date as prediceRepayDate,");
        sql.append(" CONCAT(pro.status,'') as isRepay, pro.left_money as surplusMoney,pro.pay_interest as repaymentIst,pro.real_interest repaymentRelIst, pro.repay_money as repayMoney ");
        sql.append(" from cms_provide_loan_send send ");
        sql.append(" inner join cms_repayment_schedule schedule on send.id = schedule.bill_no ");
        sql.append(" inner join cms_repayment_scheduledetailed repayment on schedule.id = repayment.relation_id ");
        sql.append(" inner join cms_provide_interest pro on repayment.id = pro.repayment_schedule_id ");
        sql.append(" and pro.interest_sign = 1)loanInterestPay on loanInterestPay.billNo = send.id ");
        sql.append(" where 1=1 ");//条件

        if (EmptyUtils.isNotEmpty(vo.getCorpId()) && !" ".equals(vo.getCorpId())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(vo.getCorpId().split(",")), 900, "send.corp_id") + ") ");
        }

        if (EmptyUtils.isNotEmpty(vo.getCmsType()) && !" ".equals(vo.getCmsType())) {
            if ("02".equals(vo.getCmsType())) {
                cmsType = "B2";
            }
            sql.append("and send.bill_code like  :cmsType  ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchStartDate())) {
            sql.append(" and send.send_date >= :startDate ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchEndDate())) {
            sql.append(" and send.send_date <= :endDate ");
        }
        sql.append(" and (send.status = 102 or send.status = 104 )) interestPay");
        sql.append(" inner join sys_corp a  on interestPay.corpId = a.id ");
        sql.append(" inner join bt_bank bank on interestPay.bankId = bank.sys_bank_code ");
        sql.append(" inner join bt_currency cur on interestPay.curCode = cur.id ");//银行借款结束
        if (EmptyUtils.isNotEmpty(vo.getBankName())) {
            sql.append(" and bank.bank_name like :bankName");
        }
        sql.append(" union all ");
        sql.append(" select interestPay.bankName, ");
        sql.append(querySql());
        sql.append(" from (select send.precative_id as corpId,send.father_code as billCode,info.cur_id as curCode,send.bill_code as sendBillCode,");
        sql.append(" send.money as sendMoney, send.send_date as sendDate,send.deadline_date as endDate,send.send_time as sendTime,send.rate as loanRate,");
        sql.append(" loanInterestPay.cmsType,loanInterestPay.prediceRepayDate,send.deferred_date as deferredDate,send.deferred_rate as deferredRate,");
        sql.append(" loanInterestPay.isRepay, loanInterestPay.surplusMoney,loanInterestPay.repaymentIst,loanInterestPay.repaymentIst repaymentRelIst,loanInterestPay.repayMoney,");
        sql.append(" '' as bankId, '' as bankName,send.overdue_rate as overdueRate ");
        sql.append(" from cms_precative_loan_send send ");
        sql.append(" inner join cms_precative_loan_info info on info.bill_code = send.father_code ");
        sql.append(" left join (select schedule.cms_type as cmsType,schedule.bill_no as billNo,repayment.predice_repay_date as prediceRepayDate,");
        sql.append(" repayment.is_repay  as isRepay, repayment.surplus_money as surplusMoney,repayment.repayment_ist as repaymentIst,");
        sql.append(" repayment.repayment_money as repayMoney ");
        sql.append(" from cms_precative_loan_send send ");
        sql.append(" inner join cms_repayment_schedule schedule  on send.bill_code = schedule.bill_no ");
        sql.append(" inner join cms_repayment_scheduledetailed repayment  on schedule.id = repayment.relation_id ");
        sql.append(" and repayment.valid_sign = 1) loanInterestPay ");
        sql.append(" on loaninterestPay.billNo = send.bill_code ");
        sql.append(" where 1 = 1 ");
        if (EmptyUtils.isNotEmpty(vo.getCorpId()) && !" ".equals(vo.getCorpId())) {
            sql.append(" and (" + getSQLIn(Arrays.asList(vo.getCorpId().split(",")), 900, "send.precative_id") + ") ");
        }

        if (EmptyUtils.isNotEmpty(vo.getCmsType()) && !" ".equals(vo.getCmsType())) {
            if ("03".equals(vo.getCmsType())) {
                cmsType = "C2";
            }
            sql.append(" and send.bill_code like :cmsType ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchStartDate())) {
            sql.append(" and send.send_date >= :startDate ");
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchEndDate())) {
            sql.append(" and send.send_date <= :endDate ");
        }
        sql.append(" and (send.status = 102 or send.status = 104)) interestpay");
        sql.append(" inner join sys_corp a on interestpay.corpId = a.id ");
        sql.append(" inner join bt_currency cur on interestPay.curCode = cur.id ");//委托贷款
        if (EmptyUtils.isNotEmpty(vo.getBankName())) {
            sql.append(" and interestpay.bankName like :bankName");
        }

        sql.append(" ) inter  order by inter.sendBillCode, inter.prediceRepayDate asc");

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cmsType", "%" + cmsType + "%");
        params.put("bankName", "%" + vo.getBankName() + "%");
        if (ObjectUtil.isNotEmpty(vo.getSearchStartDate())) {
            params.put("startDate", vo.getSearchStartDate());
        }
        if (ObjectUtil.isNotEmpty(vo.getSearchEndDate())) {
            params.put("endDate", vo.getSearchEndDate());
        }
        return listBySQLAliasToBean(sql.toString(), params, CmsInterestPayVO.class);
    }

    public StringBuilder querySql() {
        StringBuilder sql = new StringBuilder();
        sql.append(" a.name as corpName,cur.cur_name,interestPay.corpId,interestPay.billCode,interestPay.curCode,interestPay.sendBillCode, ");
        sql.append("interestPay.sendMoney, interestPay.sendDate,interestPay.endDate,interestPay.sendTime,interestPay.loanRate,");
        sql.append("interestPay.prediceRepayDate,interestPay.isRepay,interestPay.surplusMoney,interestPay.Cmstype,BT_NVL_NUM(interestPay.repaymentIst, 0) as repaymentIst,");
        sql.append("interestPay.repayMoney,interestPay.deferredDate,interestPay.deferredRate,interestPay.bankId,interestPay.overdueRate ");
        sql.append(",repaymentRelIst");

        return sql;
    }

    public StringBuilder queryViewSql() {
        StringBuilder sql = new StringBuilder();
        sql.append("select corp.name as corpName,info.bill_code as billCode,cur.id as curCode,cur.cur_name as curName,repayment.repayment_money as repayMoney,");
        sql.append("send.bill_code as sendBillCode,repayment.predice_Repay_Date as prediceRepayDate,repayment.is_repay as isRepay,");
        sql.append("repayment.Surplus_Money as SurplusMoney,send.deferred_date as deferredDate,send.deferred_rate as deferredRate,");
        return sql;
    }

    public List<CmsInterestPayVO> getDetails(String sendBillCode, String isRepay, String cmsType) {
        String cmsType1 = sendBillCode.substring(0, 2);
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        if ("A2".equals(cmsType)) {//内部贷款
            sql.append(queryViewSql());
            sql.append(" send.corp_id as corpId,send.loan_money as sendMoney,send.loan_date as sendDate, ");
            sql.append(" send.loan_rate as loanRate,send.overdue_rate as overdueRate,repayment.repayment_ist as repaymentIst");
            sql.append(" from cms_loan_send send ");
            sql.append(" inner join cms_loan_info info on send.loan_code = info.bill_code ");
            sql.append(" inner join cms_repayment_schedule schedule  on send.id = schedule.bill_no ");
            sql.append(" inner join cms_repayment_scheduledetailed repayment on schedule.id = repayment.relation_id ");
            sql.append(" inner join bt_currency cur  on send.cur_Code = cur.id ");
            sql.append(" inner join sys_corp corp on send.corp_id = corp.id ");
            sql.append(" where 1=1  ");
            sql.append(" and send.bill_code =:sendBillCode ");//and pro.status = :isRepay 
            sql.append(" order by send.bill_code, repayment.predice_Repay_Date asc");

        }

        if ("B2".equals(cmsType)) {//银行借款
            sql.append("select send.corp_id  as corpId,bank.bank_name as bankName, info.bill_code as billCode,cur.id as curCode, cur.cur_name as curName,");
            sql.append(" pro.repay_money as repayMoney,send.bill_code as sendBillCode,send.money as sendMoney,pro.repay_date as prediceRepayDate,");
            sql.append(" pro.interest_start_date as interestStartDate,send.send_date as sendDate,pro.bln_inst_date as blnInstDate,");
            sql.append(" send.pl_rate as loanRate,'' || pro.status as isRepay,pro.loan_money as bearingMoney ,pro.left_Money as SurplusMoney, ");
            sql.append(" pro.pay_interest as repaymentIst ,corp.name as corpName,send.deferred_date as deferredDate,");
            sql.append(" send.deferred_rate as deferredRate ,send.overdue_rate as overdueRate, ");
            sql.append(" pro.real_interest as repaymentRelIst ");
            sql.append(" from cms_provide_loan_send send ");
            sql.append(" inner join cms_provide_loan_info info on send.loan_code = info.bill_code ");
            sql.append(" inner join cms_repayment_schedule schedule  on send.id = schedule.bill_no ");
            sql.append(" inner join cms_repayment_scheduledetailed repayment on schedule.id = repayment.relation_id ");
            sql.append(" left join cms_provide_interest pro  on repayment.id = pro.repayment_schedule_id ");
            sql.append(" inner join bt_bank bank on info.bank_Id = bank.sys_bank_code ");
            sql.append(" inner join bt_currency cur  on send.cur_Code = cur.id ");
            sql.append(" inner join sys_corp corp on send.corp_id = corp.id ");
            sql.append(" where 1=1  and pro.interest_sign = '1'");
            sql.append(" and send.bill_code =:sendBillCode  ");//and pro.status = :isRepay
            sql.append("order by send.bill_code, pro.interest_Start_Date asc");

            /*BigDecimal repay = new BigDecimal(isRepay);
            params.put("isRepay",repay);*/
        }

        if ("C2".equals(cmsType)) {//委托贷款
            sql.append(queryViewSql());
            sql.append("send.precative_id as corpId,send.money as sendMoney,send.send_date as sendDate,send.rate as loanRate, ");
            sql.append(" send.overdue_rate as overdueRate ");
            sql.append(" from cms_precative_loan_send send ");
            sql.append(" inner join cms_precative_loan_info info on send.father_code = info.bill_code ");
            sql.append(" inner join cms_repayment_schedule schedule  on send.bill_code = schedule.bill_no ");
            sql.append(" inner join cms_repayment_scheduledetailed repayment on schedule.id = repayment.relation_id ");
            sql.append(" inner join bt_currency cur on info.cur_id = cur.id ");
            sql.append(" inner join sys_corp corp on send.precative_id = corp.id ");
            sql.append(" where 1=1  ");
            sql.append(" and send.bill_code =:sendBillCode ");//and pro.status = :isRepay 
            sql.append(" order by send.bill_code, repayment.predice_Repay_Date asc");
        }
        if ("".equals(cmsType)) {
            if ("A2".equals(cmsType1)) {
                sql.append(" select send.corp_id as corpId,corp.name as corpName,send.loan_code as billCode,cur.id as curCode,cur.cur_name as curName, ");
                sql.append("send.bill_code as sendBillCode,send.loan_money as sendMoney,send.loan_date as sendDate,");
                sql.append("send.repay_date as endDate,send.loan_rate as loanRate,send.deferred_rate as  deferredRate, ");
                sql.append(" send.deferred_date as deferredDate,send.overdue_rate as overdueRate,repay.repay_accrual as repaymentIst ");
                sql.append(" from cms_loan_send send ");
                sql.append(" left join cms_loan_repay repay on send.bill_code = repay.send_code ");
                sql.append(" inner join bt_currency cur  on send.cur_Code = cur.id ");
                sql.append(" inner join sys_corp corp on send.corp_id = corp.id ");
                sql.append(" where send.bill_code = :sendBillCode");
            } else if ("C2".equals(cmsType1)) {
                sql.append(" select send.precative_id as corpId,corp.name as corpName,send.father_code as billCode,cur.id as curCode,cur.cur_name as curName,");
                sql.append(" send.bill_code as sendBillCode,send.money as sendMoney,send.send_date as sendDate,");
                sql.append(" send.deadline_date as endDate,send.RATE as loanRate, send.deferred_rate as deferredRate,");
                sql.append(" send.deferred_date as deferredDate,send.overdue_rate as overdueRate,repay.repay_accrual as repaymentIst ");
                sql.append(" from cms_precative_loan_send send ");
                sql.append(" inner join cms_precative_loan_info info on send.father_code = info.bill_code ");
                sql.append(" left join cms_precative_loan_repay repay on send.bill_code = repay.send_code ");
                sql.append(" inner join bt_currency cur on info.cur_id = cur.id ");
                sql.append(" inner join sys_corp corp on send.precative_id = corp.id ");
                sql.append(" where send.bill_code = :sendBillCode");
            }
        }
        params.put("sendBillCode", sendBillCode);

        return listBySQLAliasToBean(sql.toString(), params, CmsInterestPayVO.class);
    }
}
