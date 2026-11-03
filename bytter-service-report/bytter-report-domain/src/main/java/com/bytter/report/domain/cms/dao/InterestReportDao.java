package com.bytter.report.domain.cms.dao;
import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.finance.constant.CmsConstant;
import com.bytter.report.domain.cms.vo.BankLoanWithholdingVO;
import com.bytter.report.domain.cms.vo.CmsProvideInterestVO;
import com.bytter.report.domain.cms.vo.InterestReportVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InterestReportDao extends BaseDao {
    /**
     *
     * @param input
     * @param ids
     * @return
     */
    public DataTablesOutput listInterest(DataTablesInput input, List<String> ids){
        StringBuilder sql = new StringBuilder();
        sql.append(" select info.id,");
        sql.append("        send.CORP_ID corpId,");
        sql.append("        bankinfo.BANK_NAME bankName, ");
        sql.append("        info.BILL_CODE infoBillNo, ");
        sql.append("        interest.CUR_ID curId, ");
        sql.append("        send.BILL_CODE sendBillNo, ");
        sql.append("        send.MONEY sendMoney, ");
        sql.append("        interest.left_money financeBalance, ");
        sql.append("        send.SEND_DATE sendDate, ");
        //还款时还款日期用的预计明细字段是SIGN_REPAY_DATE
        sql.append("        interest.SIGN_REPAY_DATE repayDate, ");
        sql.append("        interest.REPAY_MONEY repayMoney, ");
        sql.append("        interest.PAY_INTEREST repayInterest, ");
        sql.append("        (interest.REPAY_MONEY + interest.PAY_INTEREST) repayTotal,");
        sql.append("        info.PL_CLASS plClass,lp.name plClassName,info.pl_rate plRate,info.repay_date endDate, ");
        sql.append("        info.TERN_UNIT ternUnit,info.pl_term plTerm,info.pl_purpose plPurpose ");
        sql.append(" from CMS_PROVIDE_INTEREST interest ");
        sql.append("          left join CMS_PROVIDE_LOAN_SEND send on interest.BILL_CODE = send.BILL_CODE ");
        sql.append("          left join CMS_PROVIDE_LOAN_INFO info on send.LOAN_CODE = info.BILL_CODE ");
        sql.append("          left join cms_loan_type lp on info.PL_CLASS = lp.code ");
        sql.append("          left join CMS_REPAYMENT_SCHEDULEDETAILED repaymentDtl ");
        sql.append("                    on interest.REPAYMENT_SCHEDULE_ID = repaymentDtl.ID ");
        sql.append("          left join CMS_REPAYMENT_SCHEDULE repayment on repaymentDtl.RELATION_ID = repayment.ID ");
        sql.append("          left join BT_INPUT_BANK_INFO bankinfo on info.BANK_ID = bankinfo.SYS_BANK_CODE ");
        sql.append(" where interest.INTEREST_SIGN = '1' ");
        sql.append("  and (send.status = " + CmsConstant.COMPELETOTAL+" or send.status ="+CmsConstant.EXTENDDATING+" or send.status ="+CmsConstant.COMPELTEXTENDATE+")");
        sql.append("   and (repayment.CMS_TYPE = 'B1' or repayment.CMS_TYPE = 'B2')");
        sql.append(" and ("+getSQLIn(ids,900,"send.CORP_ID")+") ");
        //OrderString orderString = new OrderString("send.BILL_CODE", "desc");
        //input.setOrderString(orderString);
        return getDynamicBySQL(input, sql.toString(), InterestReportVO.class, "interest", false, null);
    }

    /**
     * 功能介绍 : 借款查询<br>
     */
    public DataTablesOutput queryBankLoanWithholding(DataTablesInput dataTablesInput, List<String> corpAuth) {
        StringBuilder sql = new StringBuilder();
        sql.append("select case when bibi.id is not null then bibi.BANK_NAME else bibs.BANK_NAME end as bankName, info.bill_code as infoBillCode,info.corp_id as corpId,");
        sql.append("send.bill_code as sendBillCode,info.cur_id as curId,bc.cur_name as curName,info.pl_money as plMoney,");
        sql.append("send.money sendMoney,repay.sum_repay_money sumRepayMoney,repay.sum_repay_interest sumRepayInterest,");
        sql.append("(BT_NVL_NUM(send.money,0) - BT_NVL_NUM(repay.sum_repay_money,0) - BT_NVL_NUM(repayPassage.sum_repay_passage_money,0)) as sendBalanceMoney,");
        sql.append("BT_NVL_NUM(repayPassage.sum_repay_passage_money,0) as sumRepayPassageMoney,");
        sql.append("send.send_date as sendDate,send.repay_date as repayDate,clt.id as plClassId, clt.name as plClassName,");
        sql.append("c.NAME corpName ");
        sql.append("from cms_provide_loan_info info ");
        sql.append("LEFT JOIN sys_corp c on info.corp_id = c.id ");
        sql.append("LEFT JOIN bt_input_bank_info bibi on info.BANK_ID = bibi.SYS_BANK_CODE ");
        sql.append("LEFT JOIN bt_input_bank_swift bibs on info.BANK_ID = bibs.SWIFT_CODE ");
        sql.append("left join cms_provide_loan_send send  on send.loan_code = info.bill_code ");
        sql.append("left join (select send_code,sum(repay_money) as sum_repay_money, ");
        sql.append("                  sum(ONLINE_REALITY_INTEREST) as sum_repay_interest, ");
        sql.append("                  max(repay_date) as repay_date ");
        sql.append("             from cms_provide_loan_repay where status >= 102 group by send_code ");
        sql.append(") repay on repay.send_code = send.bill_code ");
        sql.append("left join (select send_code, sum(repay_money) as sum_repay_passage_money ");
        sql.append("             from cms_provide_loan_repay where status < 102 and status >= -1 ");
        sql.append("            group by send_code ");
        sql.append(") repayPassage on repayPassage.send_code = send.bill_code  ");
        sql.append(" left join BT_BANK_TYPE bank ON info.BANK_TYPE = bank.ID ");
        sql.append(" left join bt_currency bc on info.CUR_ID = bc.ID ");
        sql.append(" left join cms_loan_type clt on clt.code = info.pl_class and clt.status!=-2 and clt.TYPE='2' ");
        sql.append(" left join cms_repay_type repayType on repayType.id=send.REPAY_TYPE ");
        sql.append(" where (CASE WHEN send.money IS NULL  THEN 0 ELSE send.money END - CASE WHEN repay.sum_repay_money  IS NULL THEN 0 ELSE repay.sum_repay_money END - ");
        sql.append(" CASE WHEN repayPassage.sum_repay_passage_money  IS NULL  THEN 0 ELSE repayPassage.sum_repay_passage_money END) > 0 ");
        sql.append(" and repayType.CAPITAL_AND_INTEREST not in ('2','3') and repayType.CAPITAL_FREQUENCY_TYPE<>'N'");
        sql.append(" and ").append(getSQLIn(corpAuth, 900, "info.corp_id"));
        return getDynamicBySQL(dataTablesInput, sql.toString(), BankLoanWithholdingVO.class,null, false);
    }

    /***
     * 查询预计利息
     * @param sendCodes
     * @param beginDate
     * @param endDate
     * @return TODO
     */
    public List<CmsProvideInterestVO> queryInterestList(List<String> sendCodes,String beginDate, String endDate) {
        Map<String,Object> params = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT i.* ");
        sql.append("FROM cms_provide_interest i ");
        sql.append("WHERE i.interest_sign = '1' and i.bill_code in (:sendCodes)  ");
        if (ObjectUtil.isNotEmpty(beginDate)){
            sql.append("AND bt_to_char(i.repay_date,'yyyy-MM-dd') >= :beginDate ");
            params.put("beginDate", beginDate);
        }
        if (ObjectUtil.isNotEmpty(endDate)){
            sql.append("AND bt_to_char(i.repay_date,'yyyy-MM-dd') <= :endDate ");
            params.put("endDate", endDate);
        }
        sql.append("ORDER BY i.bill_code,i.repay_date ");
        params.put("sendCodes", sendCodes);
        return this.listBySQLAliasToBean(sql.toString(), params, CmsProvideInterestVO.class);
    }
}
