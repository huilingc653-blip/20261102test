package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.entity.CmsLoanInfo;
import com.bytter.report.domain.cms.vo.LoanBalanceAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoanBalanceAnalysisDao extends BaseDao<CmsLoanInfo,String> {
    
    public String getLoanSql(String isContain,String viewSql,String term,String term1){
        StringBuilder sql = new StringBuilder();
        //内部贷款
        sql.append("   select  '01' as loanSign, a.corp_id as corpId, a.cur_code as curId, BT_NVL_NUM(a.loan_money,0) as loanMoney,");
        sql.append("   BT_NVL_NUM(s.loan_money,0) as sendMoney, BT_NVL_NUM(repayLoan.repayMoney ,0)as repayMoney,");
        if(CmsConstant.IS_TRUE.equals(isContain)) {
            //选择是，则余额=放款金额-已还款金额
            sql.append("  BT_NVL_NUM( (s.loan_money - BT_NVL_NUM(repayLoan.repayMoney, 0)), 0) as currentBalance,");
        }else {
            //选择否，则余额=放款金额-已还款金额-还款在途金额
            sql.append("   BT_NVL_NUM( (s.loan_money - BT_NVL_NUM(repayLoan.repayMoney, 0) - BT_NVL_NUM(repayAwayLoan.repayAwayMoney, 0)) , 0)  as currentBalance,");
        }
        sql.append(viewSql);
        sql.append("   BT_NVL_NUM( repayAwayLoan.repayAwayMoney,0) as repayAwayMoney,  tb.period_name as periodName,a.repay_cycle as repayCycle  ");
        sql.append("   from cms_loan_info a");
        sql.append("   left join cms_loan_send s on a.bill_code = s.loan_code ");
        sql.append("   left join cms_loan_type t on t.status != -2 and t.code = a.loan_genre and t.type = 1 ");
        sql.append("   inner join  tb_credit_period tb  on a.repay_cycle = tb.id ");
        sql.append(term);
        
        sql.append("   left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney,clr.send_code ");
        sql.append("   from cms_loan_repay clr  where clr.status >= 102  and clr.status <> 120 ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append("   group by clr.send_code ");
        sql.append("   ) repayLoan on s.bill_code = repayLoan.send_code ");

        sql.append("   left join (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney,  clr.send_code ");
        sql.append("     from cms_loan_repay clr  where clr.status != -2  and (clr.status < 102 or clr.status = 120) ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append("   group by clr.send_code ");
        sql.append("   ) repayAwayLoan  on s.bill_code = repayAwayLoan.send_code ");
        sql.append(" where 1 = 1 and s.status =102  and a.status != -2");
        
        return sql.toString();
    }
    
    public String getProvideSql(String isContain,String viewSql,String term,String term1){
        StringBuilder sql = new StringBuilder();
        //银行借款
        sql.append("  select  '02' as loanSign, info.corp_id as corpId,info.cur_id as curId, BT_NVL_NUM(info.pl_money, 0)as loanMoney,");
        sql.append("   BT_NVL_NUM(send.money,0) as sendMoney, BT_NVL_NUM(repay.sum_repay_money ,0) as repayMoney, ");
        if(CmsConstant.IS_TRUE.equals(isContain)) {
            //选择是，则余额=放款金额-已还款金额
            sql.append("  BT_NVL_NUM( (BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0)),0) as currentBalance,");
        }else {
            //选择否，则余额=放款金额-已还款金额-还款在途金额
            sql.append("  BT_NVL_NUM( (BT_NVL_NUM(send.money, 0) - BT_NVL_NUM(repay.sum_repay_money, 0) - BT_NVL_NUM(repayPassage.sum_repay_passage_money, 0)),0) as currentBalance,");
        }
        sql.append(viewSql);
        sql.append("  BT_NVL_NUM(repayPassage.sum_repay_passage_money, 0) as repayAwayMoney, tb.period_name as periodName,info.repay_cycle as repayCycle ");
        sql.append("  from cms_provide_loan_info info ");
        sql.append("  left join cms_provide_loan_send send on send.loan_code = info.bill_code ");
        sql.append("  left join bt_bank bank  on bank.sys_bank_code = info.bank_id ");
        sql.append("  left join cms_loan_type type  on type.code = info.pl_class");
        sql.append("  inner join  tb_credit_period tb on info.repay_cycle = tb.id ");
        sql.append(term);
        sql.append("  and type.status != -2 and type = '2'");

        sql.append("  left join (select send_code, sum(repay_money) as sum_repay_money, max(repay_date) as repay_date ");
        sql.append("  from cms_provide_loan_repay  where status >= 102  ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append(" group by send_code ");
        sql.append("  ) repay  on repay.send_code = send.bill_code ");

        sql.append(" left join (select send_code, sum(repay_money) as sum_repay_passage_money ");
        sql.append("  from cms_provide_loan_repay where status < 102 and status >= -1 ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append(" group by send_code ");
        sql.append(" ) repayPassage on repayPassage.send_code = send.bill_code ");
        sql.append(" and send.status = 102  and info.status != -2");
        return sql.toString();
    }
    
    public String getPrecativeSql(String isContain,String viewSql,String term,String term1){
        StringBuilder sql = new StringBuilder();
        //委托贷款
        sql.append("  select '03' as loanSign, info.loan_opp as loanOpp,info.cur_Id as curId, BT_NVL_NUM(info.money,0) as loanMoney,");
        sql.append("   BT_NVL_NUM(send.money ,0) as sendMoney, BT_NVL_NUM(repay.repayMoney ,0) as repayMoney,  ");
        
        if(CmsConstant.IS_TRUE.equals(isContain)) {
            //选择是，则余额=放款金额-已还款金额
            sql.append("  BT_NVL_NUM( (send.money - BT_NVL_NUM(repay.repayMoney, 0)) ,0) as currentBalance,");
        }else {
            //选择否，则余额=放款金额-已还款金额-还款在途金额
            sql.append("  BT_NVL_NUM( (send.money - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(away_repay.passageMoney, 0)),0) as currentBalance,");
        }
        
        sql.append(viewSql);
        
        sql.append(" BT_NVL_NUM(away_repay.passageMoney,0) as repayAwayMoney, tb.period_name as periodName,info.repay_cycle as repayCycle ");
        sql.append("  from cms_precative_loan_info info ");
        sql.append("  left join cms_precative_loan_send send  on info.bill_code = send.father_code ");
        sql.append("  inner join  tb_credit_period tb  on info.repay_cycle = tb.id ");
        sql.append(term);
        
        sql.append("  left join (select BT_NVL_NUM(sum(plr.repay_money), 0) repayMoney, plr.send_code ");
        sql.append("   from cms_precative_loan_repay plr ");
        sql.append("   left join cms_precative_loan_send pls  on plr.send_code = pls.bill_code ");
        sql.append("   where 1 = 1 and plr.status >= 102  and BT_NVL(plr.repay_type, 'sss') <> '4' ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append(" group by plr.send_code ");
        sql.append(" ) repay  on repay.send_code = send.bill_code ");

        sql.append(" left join (select BT_NVL_NUM(sum(plr.repay_money), 0) passageMoney, plr.send_code ");
        sql.append("  from cms_precative_loan_repay plr ");
        sql.append("  left join cms_precative_loan_send pls on plr.send_code = pls.bill_code");
        sql.append("  where 1 = 1  and ((plr.status != -2) and plr.status < 102) ");
        if(term1 != null) {
            sql.append(term1);
        }
        sql.append(" group by plr.send_code ");
        sql.append(" ) away_repay  on away_repay.send_code = send.bill_code");
        sql.append("  where send.status = 102 and info.status != -2");
        return sql.toString();
    }
    
    
    public DataTablesOutput getDataTable(DataTablesInput input,List<String> corpIds){
        Map<String, Object> inputs = input.getFormdata();
        String isContain = (String)inputs.get("isContain");//包含在途
        String typesOfStatistics = (String)inputs.get("typesOfStatistics");//统计维度
        StringBuilder sql = new StringBuilder();
        sql.append(" select  z.loanSign, z.corpId,z.curId,sum(z.loanMoney) as loanMoney,sum(z.sendMoney) as sendMoney,");
        sql.append(" sum(z.repayMoney) as repayMoney,sum(nvl(z.currentBalance,0)) as currentBalance,sum(z.repayAwayMoney) as repayAwayMoney ");
        if("2".equals(typesOfStatistics)) {
            sql.append(" ,z.periodName,z.repayCycle ");//贷款期限
        }
        sql.append(" from ( ");

        String term1 = "";
        String term_1 = "";
        if(inputs.get("monthDate") != null){
            term1 = " and s.loan_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')" ;
            term_1 = " and clr.repay_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')" ;
        }
        sql.append(getLoanSql(isContain,"",term1,term_1));//内部
       
        
        sql.append(" union all ");

        String term2 = "";
        String term_2 = "";
        if(inputs.get("monthDate") != null){
            term2 = " and send.send_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')";
            term_2 = " and repay_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')" ;
        }
        sql.append(getProvideSql(isContain,"",term2,term_2));//银行
       
        
        sql.append("  union all  ");

        String term3 = "";
        String term_3 = "";
        if(inputs.get("monthDate") != null){
            term3 = " and send.send_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')";
            term_3 = " and plr.repay_date <= to_date('"+ inputs.get("monthDate") + "','yyyy-MM-dd')" ;
        }
        sql.append(getPrecativeSql(isContain,"",term3,term_3));//委托
        
        sql.append(" ) z  where 1=1 ");
        
        if(corpIds != null){//单位
            sql.append(" and ").append(getSQLIn(corpIds, 900, "z.corpId"));
        }
        
        if(EmptyUtils.isNotEmpty((String)inputs.get("curId"))){
            sql.append("  and z.curId = " + "'" + (String)inputs.get("curId") + "'");
        }
        
        sql.append(" group by z.loanSign, z.corpId,z.curId ");
        if("2".equals(typesOfStatistics)) {
            sql.append(" ,z.periodName,z.repayCycle ");//贷款期限
        }
        sql.append(" order by sum(z.currentBalance) desc ");//having sum(z.currentBalance) <> 0
        return getDynamicBySQL(input, sql.toString(), LoanBalanceAnalysisVO.class, null, false);
    }
    
    public List<LoanBalanceAnalysisVO> getList(String curId,List<String> corpIds,Date date,String isContain,String typesOfStatistics){
        String date1 = DateUtils.formatDate(date);
        StringBuilder sql = new StringBuilder();
        sql.append(" select  z.loanSign, z.corpId,z.curId,sum(z.loanMoney) as loanMoney,sum(z.sendMoney) as sendMoney,");
        sql.append(" sum(z.repayMoney) as repayMoney,sum(nvl(z.currentBalance,0)) as currentBalance,sum(z.repayAwayMoney) as repayAwayMoney ");
        if("2".equals(typesOfStatistics)) {
            sql.append(" ,z.periodName ");//贷款期限
        }
        sql.append(" from ( ");
        
        String term1 = "";
        String term_1 = "";
        if(date != null){
            term1 = " and s.loan_date <= to_date('"+ date1 + "','yyyy-MM-dd')" ;
            term_1 = " and clr.repay_date <= to_date('"+ date1 + "','yyyy-MM-dd')" ;
        }
        sql.append(getLoanSql(isContain,"",term1,term_1));//内部
       

        sql.append(" union all ");

        String term2 = "";
        String term_2 = "";
        if(date != null){
            term2 = " and send.send_date <= to_date('"+ date1 + "','yyyy-MM-dd')";
            term_2 = " and repay_date <= to_date('"+ date1 + "','yyyy-MM-dd')" ;
        }
        sql.append(getProvideSql(isContain,"",term2,term_2));//银行
       

        sql.append("  union all  ");

        String term3 = "";
        String term_3 = "";
        if(date != null){
            term3 = " and send.send_date <= to_date('"+ date1 + "','yyyy-MM-dd')";
            term_3 = " and plr.repay_date <= to_date('"+ date1 + "','yyyy-MM-dd')" ;
        }
        sql.append(getPrecativeSql(isContain,"",term3,term_3));//委托
        
        sql.append(" ) z  where 1=1 ");

        if(corpIds != null && corpIds.size() > 0){//单位
            sql.append(" and ").append(getSQLIn(corpIds, 900, "z.corpId"));
        }

        if(EmptyUtils.isNotEmpty(curId)){
            sql.append("  and z.curId = " + "'" + curId + "'");
        }

        sql.append(" group by z.loanSign, z.corpId,z.curId ");
        if("2".equals(typesOfStatistics)) {
            sql.append(" ,z.periodName ");
        }
        sql.append("  order by sum(z.currentBalance) desc "); //having sum(z.currentBalance) <> 0
        
        return listBySQLAliasToBean(sql.toString(), null, LoanBalanceAnalysisVO.class);
    }

    public List<LoanBalanceAnalysisVO> viewLoanInfo(String corpId, String curId,String loanSign,Date monthDate, String isContain,String repayCycle){
        Map<String,Object> param = new HashMap<>();
        StringBuilder sql = new StringBuilder();
        if("01".equals(loanSign)){//内部贷款
            String term = " and s.loan_date <= to_date(:monthDate,'yyyy-MM-dd') and a.corp_id = :corpId and a.cur_code = :curId ";
            if(StringUtils.isNotEmpty(repayCycle.trim()) && !repayCycle.equals("undefined")){
                term += " and a.repay_cycle = :repayCycle ";
            }
            sql.append(getLoanSql(isContain,getLoanInfo(),term,null));
        }
        if("02".equals(loanSign)){//银行借款
            String term = " and send.send_date <= to_date(:monthDate,'yyyy-MM-dd')  and info.corp_id = :corpId  and info.cur_id = :curId  ";
            if(StringUtils.isNotEmpty(repayCycle.trim()) && !repayCycle.equals("undefined")){
                term += " and send.repay_cycle = :repayCycle ";
            }
            sql.append(getProvideSql(isContain,getProvideInfo(),term,null));
        }
        if("03".equals(loanSign)){//委托贷款
            String term = "  and send.send_date <= to_date(:monthDate,'yyyy-MM-dd') and info.loan_opp = :corpId  and info.cur_id = :curId  ";
            if(StringUtils.isNotEmpty(repayCycle.trim()) && !repayCycle.equals("undefined")){
                term += " and send.repay_cycle = :repayCycle ";
            }
            sql.append(getPrecativeSql(isContain,getPrecativeInfo(),term,null));
        }
        param.put("monthDate",DateUtils.formatDate(monthDate));
        param.put("corpId",corpId);
        param.put("curId",curId);
        param.put("repayCycle",repayCycle);
        return  listBySQLAliasToBean(sql.toString(), param, LoanBalanceAnalysisVO.class);
    }
    
    
    public String getLoanInfo(){
        String sql = " a.bill_code as infoBillCode,a.Contract_code as contractCode,s.bill_Code as sendBillCode,a.create_date as createDate," +
                "a.tern_Unit as ternUnit,a.con_id as conId,a.loan_date as loanDate,a.repay_date as repayDate,a.repay_type as repayType," +
                "a.LOAN_CLASS as loanClass, s.loan_acc as loanAcc, a.loan_type as loanType," +
                "a.net_code as netCode,";
        return sql;
    }
    
    public String getProvideInfo(){
        String sql = " bank.bank_name as bankName, info.bill_code as infoBillCode, send.bill_code as sendBillCode, info.pl_account as plAccount," +
                " send.contract_code as contractCode,info.con_id as conId,info.create_date as createDate,BT_NVL_NUM(repay.sum_repay_money,0) as sumRepayMoney," +
                " BT_NVL_NUM(send.pl_rate, info.pl_rate) as plRate, send.send_date as sendDate, send.repay_date as repayDate," +
                "repay.repay_date as upRepayDate,send.repay_type as repayType, type.name as plClassName,send.pl_term as plTerm," +
                "info.tern_unit as ternUnitName,info.consultant_fee as consultantFee," ;
        return sql;
    }
    
    public String getPrecativeInfo(){
        String sql = " info.bill_code as infoBillCode, info.precative_id as precativeId,info.poundage_rate as poundageRate, info.corp_id as corpId," +
                " info.term as plTerm,info.tern_Unit as ternUnitName,send.bill_code as sendBillCode,info.pl_type as plType,send.father_code as fatherCode," +
                "info.start_date as startDate, send.send_date as sendDate, info.end_date as endDate,send.deadline_date as deadlineDate," +
                "info.contract_code as infoContractCode,send.contract_code as sendContractCode,info.rate as rate,send.repay_type as repayType," +
                "info.con_id as conId," ;
        return sql;
    }

    public List<LoanBalanceAnalysisVO> getAppList(LoanBalanceAnalysisVO vo,List<String> corpIds){
        String monthDate = DateUtils.format(vo.getMonthDate(),"yyyy-MM-dd");
        String isContain = vo.getIsContain();//包含在途
        StringBuilder sql = new StringBuilder();
        sql.append(" select  z.loanSign, z.corpId,z.curId,sum(z.loanMoney) as loanMoney,sum(z.sendMoney) as sendMoney,");
        sql.append(" sum(z.repayMoney) as repayMoney,sum(nvl(z.currentBalance,0)) as currentBalance,sum(z.repayAwayMoney) as repayAwayMoney ");
        if("2".equals(vo.getTypesOfStatistics())) {
            sql.append(" ,z.periodName,z.repayCycle ");//贷款期限
        }
        sql.append(" from ( ");

        String term1 = "";
        String term_1 = "";
        if(vo.getMonthDate() != null){
            term1 = " and s.loan_date <= to_date('"+ monthDate + "','yyyy-MM-dd')" ;
            term_1 = " and clr.repay_date <= to_date('"+ monthDate + "','yyyy-MM-dd')" ;
        }
        sql.append(getLoanSql(isContain,"",term1,term_1));//内部


        sql.append(" union all ");

        String term2 = "";
        String term_2 = "";
        if(vo.getMonthDate() != null){
            term2 = " and send.send_date <= to_date('"+ monthDate + "','yyyy-MM-dd')";
            term_2 = " and repay_date <= to_date('"+ monthDate + "','yyyy-MM-dd')" ;
        }
        sql.append(getProvideSql(isContain,"",term2,term_2));//银行


        sql.append("  union all  ");

        String term3 = "";
        String term_3 = "";
        if(vo.getMonthDate() != null){
            term3 = " and send.send_date <= to_date('"+ monthDate + "','yyyy-MM-dd')";
            term_3 = " and plr.repay_date <= to_date('"+ monthDate + "','yyyy-MM-dd')" ;
        }
        sql.append(getPrecativeSql(isContain,"",term3,term_3));//委托

        sql.append(" ) z  where 1=1 ");

        //if(corpIds != null && corpIds.size() > 0){//单位
            sql.append(" and ").append(getSQLIn(corpIds, 900, "z.corpId"));
        //}

        if(EmptyUtils.isNotEmpty(vo.getCurId())){
            sql.append("  and z.curId = " + "'" + vo.getCurId() + "'");
        }

        sql.append(" group by z.loanSign, z.corpId,z.curId ");
        if("2".equals(vo.getTypesOfStatistics())) {
            sql.append(" ,z.periodName,z.repayCycle ");//贷款期限
        }
        sql.append(" order by sum(z.currentBalance) desc ");//having sum(z.currentBalance) <> 0
        return listBySQLAliasToBean(sql.toString(), null, LoanBalanceAnalysisVO.class);
    }
   
}
