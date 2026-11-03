package com.bytter.finance.domain.provide.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BaseDataConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.StringUtils;

import com.bytter.finance.domain.provide.entity.CmsProvideHistory;
import com.bytter.finance.domain.provide.vo.CmsProvideHistoryVO;
import com.bytter.finance.domain.provide.vo.LoanQueryVO;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2023/10/25<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Repository
public class CmsProvideHistoryDao extends BaseDao<CmsProvideHistory, String> {



    /**
     * 功能介绍 : 借款历史查询<br>
     */
    public DataTablesOutput queryHistoryLoan(DataTablesInput dataTablesInput) {
        StringBuilder sql = new StringBuilder();
        sql.append("select his.*,type.name as plClassName ");
        sql.append("from cms_provide_history his");
        sql.append(" left join cms_loan_type type on type.code = his.pl_class and type.status!=-2 and type='" + BaseDataConstant.TYPE_BORROW + "'");
        sql.append(" where 1=1 ");
        Map<String, Object> params = dataTablesInput.getFormdata();
        if (null != params.get("status") && StringUtils.isNotEmpty(params.get("status").toString())) {
            List<String> bizStatus = Arrays.asList(params.get("status").toString().split(","));
            if (ListUtils.isNotNullOrEmpty(bizStatus)) {
                sql.append(" and (");
                sql.append(getSQLIn(bizStatus,900,"his.biz_status"));
                sql.append(") ");
            }
        }
        return getDynamicBySQL(dataTablesInput, sql.toString(), CmsProvideHistoryVO.class,null, false);
    }

    /**
     * 功能介绍 : 借款查询<br>
     */
    public List<LoanQueryVO> queryLoanForReport() {
        StringBuilder sql = new StringBuilder();
        sql.append("select CASE WHEN info.is_domestic_bank = '0' THEN bsi.bank_name else bank.bank_name end bankName,info.corp_id as corpId,info.bill_code as infoBillCode,");
        sql.append(" send.bill_code as sendBillCode,info.pl_account as plAccount,send.contract_code as contractCode,");
        sql.append(" info.con_id as conId,");
        sql.append(" info.RMK as rmk,");
        sql.append(" info.create_name createName, ");
        sql.append(" info.create_date as createDate,info.cur_id as curId,info.pl_money as plMoney,send.money as sendMoney,");
        sql.append(" repay.sum_repay_money as sumRepayMoney,");
        sql.append(" (BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay.sum_repay_money,0) - BT_NVL_NUM(repayPassage.sum_repay_passage_money,0)) as send_balance_money,");
        sql.append(" BT_NVL_NUM(repayPassage.sum_repay_passage_money,0) as sum_repay_passage_money,");
        sql.append(" BT_NVL_NUM(send.pl_rate,info.pl_rate) as plRate,send.send_date as sendDate,send.repay_date as repayDate,");
        sql.append(" repay.repay_date as upRepayDate,send.repay_type as repayType,info.pl_class as pl_Class,type.name as plClassName,");
        sql.append(" send.pl_term as plTerm,BT_NVL_NUM(dc.deferred_count,0) as deferred_count, ");
        sql.append(" info.tern_unit as ternUnit,info.consultant_fee as consultantFee,info.status as infoStatus, ");
        sql.append(" send.status as sendStatus,BT_NVL_CHAR(abolish.biz_status,info.biz_status) as infoBizStatus,");
        sql.append(" BT_NVL_CHAR(abolish.biz_status,send.biz_status) as sendBizStatus");
        sql.append(" from cms_provide_loan_info info");
        sql.append(" left join cms_provide_loan_send send on send.loan_code = info.bill_code");
        sql.append(" left join (select send_code,sum(repay_money) as sum_repay_money,max(repay_date) as repay_date from cms_provide_loan_repay where status >= 102 group by send_code) repay on repay.send_code = send.bill_code");
        sql.append(" left join (select send_code,sum(repay_money) as sum_repay_passage_money from cms_provide_loan_repay where status < 102 and status >= -1 group by send_code) repayPassage on repayPassage.send_code = send.bill_code");
        sql.append(" left join (select li_code as send_code,count(*) as deferred_count from cms_provide_loan_deferred where status != -2 group by li_code ) dc on dc.send_code = send.bill_code ");
        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
        sql.append(" left join bt_swift_info bsi on bsi.swift_code = info.bank_id ");
        sql.append(" left join cms_loan_type type on type.code = info.pl_class and type.status!=-2 and type='" + BaseDataConstant.TYPE_BORROW + "'");
        sql.append(" left join cms_provide_loan_abolish abolish on abolish.pli_code = info.bill_code and abolish.status > 0 and abolish.status < 110");
        sql.append(" where 1=1 ");

        return listBySQLAliasToBean(sql.toString(),null, LoanQueryVO.class);
    }

    /**删除某一日期的，银行借款数据*/
    public void deleteHisProvide(Date date){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append("delete from CMS_PROVIDE_HISTORY where to_char(report_date,'yyyy-MM-dd')=:date");
        params.put("date", DateUtils.format(date,"yyyy-MM-dd"));
        createSQLQuery(sql.toString(), params).executeUpdate();
    }
}
