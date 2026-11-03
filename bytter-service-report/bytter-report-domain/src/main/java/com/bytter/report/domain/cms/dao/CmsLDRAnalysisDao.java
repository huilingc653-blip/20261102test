package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.vo.CmsLDRAnalysisVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author luxy
 * @Title: CmsLDRAnalysisDao
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/1/16 15:50
 */
@Repository
public class CmsLDRAnalysisDao extends BaseDao<CmsLDRAnalysisVO,String> {

    /**
     * 统计维度为融资单位
     * @param input
     * @param corps
     * @return
     */
    public List<CmsLDRAnalysisVO> corpDatatable(DataTablesInput input, List<String> corps,String period){
        Map<String,Object> params=new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder();
        String curId=(String)input.getFormdata().get("curId");
        String isContain=(String)input.getFormdata().get("isContain");//是否包含在途
        if(null==isContain||StringUtils.isEmpty("isContain")){
            isContain= CmsConstant.IS_TRUE;//默认包含在途
        }
        StringBuilder comSql=new StringBuilder();
        comSql.append(" select BT_NVL(m.corpId, n.corpId) corpId, ")
                .append(" BT_NVL(m.curId, n.curId) curId, ")
                .append(" BT_NVL_NUM(m.totalDeposits"+period+", 0) totalDeposits"+period+",")
                .append(" BT_NVL_NUM(n.totalLoans"+period+", 0) totalLoans"+period+" ");

        //mysql中没有full join,使用union来达到两张表全连接的目的
        StringBuilder depositSumSql=new StringBuilder();//存款公共语句
        StringBuilder loansSumSql=new StringBuilder();//贷款公共语句
        comSql.append(" from( ");
        sql.append(" select k.* from (select * from ( ");
        sql.append(comSql);
        sql.append(this.queryDepositsSumSql(depositSumSql,period,curId));
        sql.append(")m ")
                .append(" left join ( ");
        sql.append(this.queryLoansSumSql(loansSumSql,period,isContain,curId));
        sql.append(")n ")
                .append(" on m.corpId=n.corpId and m.curId=n.curId ");
        sql.append(" union ")
                .append(comSql);
        sql.append(depositSumSql);
        sql.append(")m ")
                .append(" right join ( ");
        sql.append(loansSumSql);
        sql.append(")n ")
                .append(" on m.corpId=n.corpId and m.curId=n.curId ");
        sql.append(" )corpSum  where 1=1 ");
        if(!EmptyUtils.isEmptyList(corps)){
            sql.append(" and ").append(getSQLIn(corps, 900, "corpSum.corpId"));
            params.put("corps",corps);
        }
        if(!StringUtils.isBlank(curId)){
            params.put("curId",curId);
        }
        sql.append("  )k ");
        sql.append(" left join sys_corp corp on k.corpId=corp.id order by corp.code ");
        this.setparams(input,params,period);
        return listBySQLAliasToBean(sql.toString(),params, CmsLDRAnalysisVO.class);
    }


    /**
     * 统计维度为融资银行
     * @param input
     * @param corps
     * @return
     */
    public  List<CmsLDRAnalysisVO> getBankAnalysisList(DataTablesInput input, List<String> corps,String period){
        Map<String,Object> params=new HashMap<String,Object>();
        StringBuilder sql=new StringBuilder();
        String isContain=(String)input.getFormdata().get("isContain");//是否包含在途
        String netFlag=(String)input.getFormdata().get("netFlag");//当统计维度为银行时取结算中心作为银行的标记
        String dtlFlag=(String)input.getFormdata().get("dtlFlag");
        String bankType=(String)input.getFormdata().get("bankType");
        String curId=(String)input.getFormdata().get("curId");
        Boolean hasBank=false;
        if(null!=bankType&&StringUtils.isNotEmpty(bankType)){
            hasBank=true;
        }
        if(null==dtlFlag||StringUtils.isEmpty("dtlFlag")){
            dtlFlag=CmsConstant.IS_FALSE;//默认不查询明细
        }

        if(null==isContain||StringUtils.isEmpty("isContain")){
            isContain= CmsConstant.IS_TRUE;//默认包含在途
        }
        StringBuilder comSql=new StringBuilder();
        if(dtlFlag.equals("1")){
            comSql.append("  select BT_NVL(m.bankCode, n.bankCode) bankCode, ");
        }else{
            comSql.append("  select BT_NVL(m.bankType, n.bankType) bankType, ");
        }
        comSql.append(" BT_NVL(m.curId, n.curId) curId, ")
                .append(" BT_NVL_NUM(m.totalDeposits"+period+", 0) totalDeposits"+period+",")
                .append(" BT_NVL_NUM(n.totalLoans"+period+", 0) totalLoans"+period+" ");


        //mysql中没有full join,使用union来达到两张表全连接的目的
        StringBuilder depositSumSql=new StringBuilder();
        StringBuilder loansSumSql=new StringBuilder();
        if(dtlFlag.equals("1")){
            comSql.append(" from( ");
            sql.append("  select * from ( ");
            sql.append(comSql);
            sql.append(this.queryDepositsSumSql2(depositSumSql,period,netFlag,dtlFlag,hasBank,curId));
            sql.append(")m ")
                    .append(" left join ( ");
            sql.append(this.queryLoansSumSql2(loansSumSql,period,isContain,netFlag,dtlFlag,hasBank,curId));
            sql.append(")n ")
                    .append(" on m.bankCode=n.bankCode and m.curId=n.curId ");
            sql.append(" union ")
                    .append(comSql);
            sql.append(depositSumSql);
            sql.append(")m ")
                    .append(" right join ( ");
            sql.append(loansSumSql);
            sql.append(")n ")
                    .append(" on m.bankCode=n.bankCode and m.curId=n.curId ");
        }else{
            comSql.append(" from( ");
            sql.append(" select * from ( ");
            sql.append(comSql);
            sql.append(this.queryDepositsSumSql2(depositSumSql,period,netFlag,dtlFlag,hasBank,curId));
            sql.append(")m ")
                    .append(" left join ( ");
            sql.append(this.queryLoansSumSql2(loansSumSql,period,isContain,netFlag,dtlFlag,hasBank,curId));
            sql.append(")n ")
                    .append(" on m.bankType=n.bankType and m.curId=n.curId ");
            sql.append(" union ")
                    .append(comSql);
            sql.append(depositSumSql);
            sql.append(")m ")
                    .append(" right join ( ");
            sql.append(loansSumSql);
            sql.append(")n ")
                    .append(" on m.bankType=n.bankType and m.curId=n.curId ");
        }
        sql.append(" )bankSum  where 1=1 ");
        if(!StringUtils.isBlank(curId)){
            params.put("curId",curId);
        }
        this.setparams(input,params,period);
        DataTablesInput input1=new DataTablesInput();
        input1.setStart(0);
        input1.setLength(1000);
        return listBySQLAliasToBean(sql.toString(),params, CmsLDRAnalysisVO.class);
    }

    /**
     * 设置参数
     */
    public void setparams(DataTablesInput input,Map<String,Object> params,String period){
        String endDate=(String)input.getFormdata().get("endDate");
        String bankType=(String)input.getFormdata().get("bankType");
        String netId=(String)input.getFormdata().get("netId");
        Date endDateD= DateUtils.str2Date(endDate,DateUtils.date_sdf());//查询截止日期
        params.put("endDateD",endDateD);

        switch (period){
            case"M":
                Date endDateM=DateUtils.getLastMonthDate(DateUtils.rollDate(endDateD,0,-1,0));//查询条件的截止查询日期的上月月末最后一天--较上月末
                params.put("endDateM",endDateM);
                break;
            case"S":
                Date endDateS=DateUtils.getLastQuarterDate(DateUtils.addQuarter(endDateD,-1));//查询条件的截止查询日期的上季度末最后一天--较上季末
                params.put("endDateS",endDateS);
                break;
            case"L":
                Date endDateL=DateUtils.getLastMonthDate(DateUtils.rollDate(endDateD,-1,0,0));//查询条件的截止查询日期的去年相同月份最后一天--较去年同期
                params.put("endDateL",endDateL);
                break;
            case"Y":
                Date endDateY=DateUtils.getYearFirst(DateUtils.getYear(endDateD));//查询条件的截止查询日期的所在年份的年初第一天---较年初
                params.put("endDateY",endDateY);
                break;

        }
        params.put("bankType",bankType);
        params.put("netId",netId);
    }

    /**
     * 贷款合计汇总sql
     */
    public StringBuilder queryLoansSumSql(StringBuilder loansSumSql,String period,String isContain,String curId){
        //贷款余额查询sql
        loansSumSql.append(" select loanBalance.corp_id corpId,loanBalance.cur_id curId, ")
                .append(" BT_NVL_NUM(loanBalance.balance, 0) as totalLoans"+period+"");
        loansSumSql.append(" from ");
        loansSumSql.append(this.queryLoansComSql(period,isContain,curId));
        loansSumSql.append(" loanBalance ")
                .append(" where loanBalance.balance<>0 ");

        return loansSumSql;
    }

    /**
     * 贷款合计汇总sql2
     */
    public StringBuilder queryLoansSumSql2(StringBuilder loansSumSql,String period,String isContain,String netFlag,String dtlFlag,Boolean hasBank,String curId){
        //贷款余额查询sql
        if(dtlFlag.equals("1")){
            loansSumSql.append(" select loanBalance.bankCode,loanBalance.cur_id curId, ")
                    .append(" BT_NVL_NUM(loanBalance.balance, 0) as totalLoans"+period+"");
        }else{
            loansSumSql.append(" select loanBalance.bank_type bankType,loanBalance.cur_id curId, ")
                    .append(" BT_NVL_NUM(loanBalance.balance, 0) as totalLoans"+period+"");
        }

        loansSumSql.append(" from ");
        loansSumSql.append(this.queryLoansComSql2(period,isContain,netFlag,dtlFlag,hasBank,curId));
        loansSumSql.append(" loanBalance ")
                .append(" where loanBalance.balance<>0 ");

        return loansSumSql;
    }

    /**
     * 贷款查询公共sql
     * @param cycleType 查询周期
     * @param isContain 是否包含在途
     * @return
     */
    public StringBuilder queryLoansComSql(String cycleType,String isContain,String curId) {
        StringBuilder loansComSql=new StringBuilder();
        loansComSql.append(" (select totalLoans.corp_id, ")
                .append(" totalLoans.cur_id, ");
        if(isContain.equals(CmsConstant.IS_FALSE)){//不包含在途:余额=放款金额-已还款金额-还款在途金额
            loansComSql.append(" (totalLoans.send_money - totalLoans.repay_money-totalLoans.repay_on_money) balance ");
        }else if(isContain.equals(CmsConstant.IS_TRUE)){//包含在途:余额=放款金额-已还款金额
            loansComSql.append(" (totalLoans.send_money - totalLoans.repay_money) balance ");
        }
        loansComSql.append(" from ( ")
                .append(" select loansTable.corp_id, ")
                .append("  loansTable.cur_id, ")
                .append("  BT_NVL_NUM(sum(loansTable.send_money), 0) as send_money,")
                .append("  BT_NVL_NUM(sum(loansTable.repay_money), 0) as repay_money, ")
                .append("  BT_NVL_NUM(sum(loansTable.repay_on_money), 0) as repay_on_money")
                .append("  from ( ")
                //内部贷款
                .append("  select info.corp_id as corp_id,")
                .append("  info.cur_code as cur_id, ")
                .append("  BT_NVL_NUM(sum(send.loan_money), 0) as send_money,")
                .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                .append("  BT_NVL_NUM(sum(repayAwayLoan.repayingMoney), 0) as repay_on_money ")
                .append("  from cms_loan_info info ")
                .append("  inner join cms_loan_send send ")
                .append("  on send.loan_code = info.bill_code ")
                .append("  and send.status in(102,103,104,110) ")//参与放款金额统计的放款，取放款已生成合同的数据
                .append("  and send.loan_date <=:endDate"+cycleType+"")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, a.send_code ")
                .append("  from cms_loan_repay a ")
                .append("  where a.status >= 102 ")//已还款金额，统计还款申请贷款利息确认完成的数据
                .append("  and a.status <> 120 ")
                .append("  and a.repay_date <= :endDate"+cycleType+"")
                .append("  group by a.send_code) repayLoan ")
                .append("  on send.bill_code = repayLoan.send_code ")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayingMoney, ")
                .append("  b.send_code ")
                .append("  from cms_loan_repay b ")
                .append("  where (b.status <-2 or b.status >-2) ")//还款在途金额,还款提交，记账完成前
                .append(" and (b.status < 102 or b.status = 120) ")
                .append("  and b.repay_date <= :endDate"+cycleType+"")
                .append("  group by b.send_code) repayAwayLoan ")
                .append("  on send.bill_code = repayAwayLoan.send_code ")
                .append("  group by info.corp_id, info.cur_code ")
                .append("  union all ")
                //银行借款
                .append("  select info.corp_id as corp_id, ")
                .append("  info.cur_id as cur_id, ")
                .append("  BT_NVL_NUM(sum(send.money), 0) as send_money, ")
                .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                .append("  BT_NVL_NUM(sum(repayAwayLoan.repayingMoney), 0) as repay_on_money ")
                .append("  from cms_provide_loan_info info ")
                .append("  inner join cms_provide_loan_send send ")
                .append("  on send.loan_code = info.bill_code ")
                .append("  and send.status in(102,103,104,110) ")//参与银行借款的放款金额统计的放款，必须是记账模式下记账复核通过的放款，不记账模式下，银行借款放款审批通过的数据
                .append("  and send.send_date <= :endDate"+cycleType+"")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, a.send_code ")
                .append("  from cms_provide_loan_repay a ")
                .append("  where a.status >= 102 ")//已还款金额，线下还款，在不记账的情况下，统计还款补录完成的数据；在记账的情况下，统计还款记账复核通过的数据；线上还款，统计在还款跟踪确认已扣款的数据
                .append("  and a.repay_date <= :endDate"+cycleType+"")
                .append("  group by a.send_code) repayLoan ")
                .append("  on send.bill_code = repayLoan.send_code ")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayingMoney, ")
                .append("  b.send_code ")
                .append("  from cms_provide_loan_repay b ")
                .append("  where b.status >=-1 ")//还款在途金额，统计还款申请提交后，还款申请待审批、审批中、线下还款的审批通过待补录、待记账、记账待复核、记账复核打回、线下还款的待扣款状态
                .append("  and b.status <102  ")
                .append("  and b.repay_date <= :endDate"+cycleType+"")
                .append("  group by b.send_code) repayAwayLoan ")
                .append("  on send.bill_code = repayAwayLoan.send_code ")
                .append("  group by info.corp_id, info.cur_id ")
                .append("  union all ")
                //委托贷款
                .append("  select info.loan_opp as corp_id, ")//借款单位委托贷款的余额
                .append("  info.cur_id as cur_id, ")
                .append("  BT_NVL_NUM(sum(send.money), 0) as send_money, ")
                .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                .append("  BT_NVL_NUM(sum(repayAwayLoan.repayingMoney), 0) as repay_on_money ")
                .append("  from cms_precative_loan_info info ")
                .append("  inner join cms_precative_loan_send send ")
                .append("  on send.father_code = info.bill_code ")
                .append("  and send.status in(102,103,104,110) ")//参与委托贷款放款金额统计的放款，必须是记账模式下记账复核通过的放款，不记账模式下，委托贷款放款审批通过的数据
                .append("  and send.send_date <= :endDate"+cycleType+"")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, a.send_code ")
                .append("  from cms_precative_loan_repay a ")
                .append("  where a.status >= 102 ")//已还款金额，记账模式下统计记账复核通过的数据，不记账模式下统计委托贷款利息确认的数据
                .append("  and a.repay_date <= :endDate"+cycleType+"")
                .append("  group by a.send_code) repayLoan ")
                .append("  on send.bill_code = repayLoan.send_code ")
                .append("  left join (select BT_NVL_NUM(sum(repay_money), 0) repayingMoney, ")
                .append("  b.send_code ")
                .append("  from cms_precative_loan_repay b ")
                .append("  where 1=1 ")//还款在途金额，统计委托贷款还款申请提交后，待审批、审批中、审批通过待记账、记账复核通过待确认、记账复核打回等状态的数据
                .append("  and (b.status <-2 or b.status >-2)  ")
                .append("  and b.status < 102 ")
                .append("  and b.repay_date <= :endDate"+cycleType+"")
                .append("  group by b.send_code) repayAwayLoan ")
                .append("  on send.bill_code = repayAwayLoan.send_code ")
                .append("  group by info.loan_opp, info.cur_id) loansTable ");
        if(!StringUtils.isBlank(curId)){
            loansComSql.append("  where loansTable.cur_id=:curId ");
        }
        loansComSql.append("  group by loansTable.corp_id, loansTable.cur_id ) totalLoans) ");
        return loansComSql;
    }
    /**
     * 贷款查询公共sql(按银行类别查询时)
     */
    public StringBuilder queryLoansComSql2(String cycleType,String isContain,String netFlag,String dtlFlag,Boolean hasBank,String curId) {
        StringBuilder loansComSql=new StringBuilder();
        if(dtlFlag.equals("1")){
            loansComSql.append(" (select totalLoans.bankCode, ")
                    .append(" totalLoans.cur_id, ");
        }else{
            loansComSql.append(" (select totalLoans.bank_type, ")
                    .append(" totalLoans.cur_id, ");
        }

        if(isContain.equals(CmsConstant.IS_FALSE)){//不包含在途
            loansComSql.append(" (totalLoans.send_money - totalLoans.repay_money-totalLoans.repay_on_money) balance ");
        }else if(isContain.equals(CmsConstant.IS_TRUE)){//包含在途
            loansComSql.append(" (totalLoans.send_money - totalLoans.repay_money) balance ");
        }
        loansComSql.append(" from ( ");
        if(dtlFlag.equals("1")){
            loansComSql.append("select loansTable.bankCode, ");
        }else{
            loansComSql.append("select loansTable.bank_type, ");
        }
        loansComSql.append("  loansTable.cur_id, ")
                .append("  BT_NVL_NUM(sum(loansTable.send_money), 0) as send_money,")
                .append("  BT_NVL_NUM(sum(loansTable.repay_money), 0) as repay_money, ")
                .append("  BT_NVL_NUM(sum(loansTable.repay_on_money), 0) as repay_on_money")
                .append("  from ( ");
        if(netFlag!=null&&netFlag.equals("1")){
            //内部贷款无银行，取结算中心
            if(dtlFlag.equals("1")) {//查询明细
                loansComSql.append(" select info.corp_id as bankCode, ");
            }else{
                loansComSql.append(" select info.net_code as bank_type, ");
            }
            loansComSql.append(" info.cur_code as cur_id, ")
                    .append(" BT_NVL_NUM(sum(send.loan_money), 0) as send_money, ")
                    .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                    .append(" BT_NVL_NUM(sum(repayAwayLoan.repayingMoney),0) as repay_on_money ")
                    .append(" from cms_loan_info info ")
                    .append(" inner join cms_loan_send send ")
                    .append(" on send.loan_code = info.bill_code ");
            if(dtlFlag.equals("1")) {//查询明细
                loansComSql.append(" and info.net_code=:netId ");
            }
            loansComSql.append(" and send.status in(102,103,104,110) ")//参与放款金额统计的放款，取放款已生成合同的数据
                    .append(" and send.loan_date <= :endDate" + cycleType + "")
                    .append(" left join (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                    .append(" a.send_code ")
                    .append(" from cms_loan_repay a ")
                    .append(" where a.status >=102 ")
                    .append(" and a.status <> 120 ")
                    .append(" and a.repay_date <= :endDate" + cycleType + "")
                    .append(" group by a.send_code) repayLoan ")
                    .append(" on send.bill_code = repayLoan.send_code ")
                    .append(" left join ")
                    .append(" (select BT_NVL_NUM(sum(repay_money), 0) repayingMoney, ")
                    .append(" b.send_code ")
                    .append(" from cms_loan_repay b ")
                    .append(" where (b.status <-2 or b.status >-2) ")
                    .append(" and (b.status < 102 or b.status = 120) ")
                    .append(" and b.repay_date <= :endDate" + cycleType + "")
                    .append(" group by b.send_code) repayAwayLoan ")
                    .append(" on send.bill_code = repayAwayLoan.send_code ");
            if(dtlFlag.equals("1")) {//查询明细
                loansComSql.append(" group by info.corp_id, info.cur_code ")
                        .append(" union all ")
                        //委托贷款受托人非银行时取结算中心
                        .append(" select info.loan_opp as bank_type, ");//借款人单位
            }else{
                loansComSql.append(" group by info.net_code, info.cur_code ")
                        .append(" union all ")
                        //委托贷款受托人非银行时取结算中心
                        .append(" select info.corp_id as bank_type, ");
            }
            loansComSql.append(" info.cur_id as cur_id, ")
                    .append(" BT_NVL_NUM(sum(send.money), 0) as send_money, ")
                    .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                    .append(" BT_NVL_NUM(sum(repayAwayLoan.repayingMoney),0) as repay_on_money ")
                    .append(" from cms_precative_loan_info info ")
                    .append(" inner join cms_precative_loan_send send ")
                    .append(" on send.father_code = info.bill_code ")
                    .append(" and send.status in(102,103,104,110) ")
                    .append(" and send.send_date <=:endDate"+cycleType+"")
                    .append(" and info.is_bank = 0 ");
            if(dtlFlag.equals("1")) {//查询明细
                loansComSql.append(" and info.corp_id=:netId ");
            }
            loansComSql.append(" left join  ")
                    .append(" (select BT_NVL_NUM(sum(repay_money),0) repayMoney,")
                    .append(" a.send_code ")
                    .append(" from cms_precative_loan_repay a ")
                    .append(" where a.status >=102 ")//委托贷款已还款
                    .append(" and a.repay_date <= :endDate" + cycleType + "")
                    .append(" group by a.send_code) repayLoan ")
                    .append(" on send.bill_code = repayLoan.send_code ")
                    .append(" left join  ")
                    .append(" (select BT_NVL_NUM(sum(repay_money),0) repayingMoney, ")
                    .append(" b.send_code ")
                    .append(" from cms_precative_loan_repay b ")
                    .append(" where 1=1 ")
                    .append(" and (b.status <-2 or b.status >-2)")//委托贷款还款在途
                    .append(" and b.status < 102 ")
                    .append(" and b.repay_date <=:endDate" + cycleType + "")
                    .append(" group by b.send_code) repayAwayLoan ")
                    .append(" on send.bill_code = repayAwayLoan.send_code ");
            if(dtlFlag.equals("1")) {//查询明细
                loansComSql.append("  group by info.loan_opp, info.cur_id )loansTable ");
                if(!StringUtils.isBlank(curId)){
                    loansComSql.append("  where loansTable.cur_id=:curId ");
                }
                loansComSql.append(" group by loansTable.bankCode, loansTable.cur_id ) totalLoans) ");
            }else{
                loansComSql.append("  group by info.corp_id, info.cur_id )loansTable ");
                if(!StringUtils.isBlank(curId)){
                    loansComSql.append("  where loansTable.cur_id=:curId ");
                }
                loansComSql.append("  group by loansTable.bank_type, loansTable.cur_id ) totalLoans) ");
            }

        }else{
            //银行借款
            if(dtlFlag.equals("1")){
                loansComSql.append("  select bank.sys_bank_code as bankCode, ");
            }else{
                loansComSql.append("  select bt.id  as bank_type, ");
            }
            loansComSql.append("  info.cur_id as cur_id, ")
                    .append("  BT_NVL_NUM(sum(send.money), 0) as send_money, ")
                    .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                    .append("  BT_NVL_NUM(sum(repayAwayLoan.repayingMoney), 0) as repay_on_money ")
                    .append("  from cms_provide_loan_info info ")
                    .append("  inner join cms_provide_loan_send send ")
                    .append("  on send.loan_code = info.bill_code ")
                    .append("  and send.status in(102,103,104,110) ")
                    .append("  and send.send_date <= :endDate"+cycleType+"")
                    .append("  left join ")
                    .append("  (select BT_NVL_NUM(sum(repay_money),0) repayMoney, ")
                    .append("  a.send_code ")
                    .append("  from cms_provide_loan_repay a ")
                    .append("  where a.status >= 102 ")//银行借款已还款
                    .append("  and a.repay_date <=:endDate"+cycleType+"")
                    .append("  group by a.send_code) repayLoan ")
                    .append("  on send.bill_code = repayLoan.send_code ")
                    .append("  left join  ")
                    .append("  (select BT_NVL_NUM(sum(repay_money),0) repayingMoney, ")
                    .append("   b.send_code ")
                    .append("  from cms_provide_loan_repay b ")
                    .append(" where b.status <102 ")//银行借款还款在途
                    .append(" and b.status >= -1 ")
                    .append("  and b.repay_date <=:endDate"+cycleType+"")
                    .append("  group by b.send_code) repayAwayLoan ")
                    .append("  on send.bill_code = repayAwayLoan.send_code ")
                    .append("  inner join bt_bank bank ")
                    .append("  on info.bank_Id = bank.sys_bank_code ")
                    .append("  inner join bt_bank_type bt ")
                    .append("  on bank.bank_prefix = bt.bank_prefix ");
            //委托贷款受托人是银行
            if (dtlFlag.equals("1")) {
                loansComSql.append(" and bt.id=:bankType ")
                        .append(" group by bank.sys_bank_code, info.cur_id")
                        .append("  union all ")
                        .append(" select bank.sys_bank_code as bankCode, ");
            } else {
                if(hasBank){
                    loansComSql.append(" and bt.id=:bankType ");
                }
                loansComSql.append(" group by bt.id, info.cur_id")
                        .append("  union all ")
                        .append("  select bt.id  as bank_type, ");
            }
            loansComSql.append("  info.cur_id as cur_id, ")
                    .append("  BT_NVL_NUM(sum(send.money), 0) as send_money, ")
                    .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) as repay_money, ")
                    .append("  BT_NVL_NUM(sum(repayAwayLoan.repayingMoney), ")
                    .append("  0) as repay_on_money ")
                    .append("  from cms_precative_loan_info info ")
                    .append("  inner join cms_precative_loan_send send ")
                    .append("  on send.father_code = info.bill_code ")
                    .append("  and send.status in(102,103,104,110) ")
                    .append("  and send.send_date <= :endDate"+cycleType+"")
                    .append("  and info.is_bank = 1 ")
                    .append("  left join (select BT_NVL_NUM(sum(repay_money), ")
                    .append("  0) repayMoney, ")
                    .append("   a.send_code ")
                    .append("  from cms_precative_loan_repay a ")
                    .append("  where a.status >= 102 ")//委托贷款已还款
                    .append("  and a.repay_date <= :endDate"+cycleType+"")
                    .append("  group by a.send_code) repayLoan ")
                    .append("  on send.bill_code = repayLoan.send_code ")
                    .append("  left join (select BT_NVL_NUM(sum(repay_money), ")
                    .append("  0) repayingMoney, ")
                    .append("  b.send_code ")
                    .append("  from cms_precative_loan_repay b ")
                    .append("  where 1=1 ")
                    .append("  and (b.status <-2 or b.status >-2) ")//委托贷款还款在途
                    .append("  and b.status < 102 ")
                    .append("  and b.repay_date <= :endDate"+cycleType+"")
                    .append("  group by b.send_code) repayAwayLoan ")
                    .append("  on send.bill_code = repayAwayLoan.send_code ")
                    .append("  inner join bt_bank bank ")
                    .append("  on info.bank_code = bank.sys_bank_code ")
                    .append("  inner join bt_bank_type bt ")
                    .append("   on bank.bank_prefix = bt.bank_prefix ");
            if(dtlFlag.equals("1")){
                loansComSql.append(" and bt.id=:bankType ")
                        .append("  group by bank.sys_bank_code, info.cur_id )loansTable ");
                if(!StringUtils.isBlank(curId)){
                    loansComSql.append("  where loansTable.cur_id=:curId ");
                }
                loansComSql.append("  group by loansTable.bankCode, loansTable.cur_id ) totalLoans) ");
            }else{
                if(hasBank){
                    loansComSql.append(" and bt.id=:bankType ");
                }
                loansComSql.append("  group by bt.id, info.cur_id )loansTable ");
                if(!StringUtils.isBlank(curId)){
                    loansComSql.append("  where loansTable.cur_id=:curId ");
                }
                loansComSql.append("  group by loansTable.bank_type, loansTable.cur_id ) totalLoans) ");
            }

        }
        return loansComSql;
    }
    /**
     * 存款合计汇总sql
     */
    public StringBuilder queryDepositsSumSql(StringBuilder depositSumSql,String period,String curId){
        //贷款余额查询sql
        depositSumSql.append("  select depositsBalance.corp_id corpId,depositsBalance.cur_id curId, ")
                .append(" BT_NVL_NUM(depositsBalance.balance, 0) as totalDeposits"+period+" ");
        depositSumSql.append(" from ");
        depositSumSql.append(this.queryDepositsComSql(period,curId));
        depositSumSql.append(" depositsBalance ")
                .append(" where depositsBalance.balance <>0 ");

        return depositSumSql;
    }
    /**
     * 存款合计汇总sql
     */
    public StringBuilder queryDepositsSumSql2(StringBuilder depositSumSql,String compCycle,String netFlag,String dtlFlag,Boolean hasBank,String curId){
        if(dtlFlag.equals("1")) {//查询明细
            depositSumSql.append("  select  depositsBalance.bankCode,depositsBalance.cur_id curId, ")
                    .append(" BT_NVL_NUM(depositsBalance.balance, 0) as totalDeposits"+compCycle+" ");
        }else{
            depositSumSql.append("  select  depositsBalance.bank_type bankType,depositsBalance.cur_id curId, ")
                    .append(" BT_NVL_NUM(depositsBalance.balance, 0) as totalDeposits"+compCycle+"");
        }

        depositSumSql.append(" from ");
        depositSumSql.append(this.queryDepositsComSql2("D",netFlag,dtlFlag,hasBank,curId));
        depositSumSql.append(" depositsBalance ")
                .append(" where depositsBalance.balance <>0 ");

        return depositSumSql;
    }

    /**
     * 存款查询公共sql
     */
    public StringBuilder queryDepositsComSql(String cycleType,String curId){
        StringBuilder depositComSql=new StringBuilder();
        //内部存款包含开户种类是企业活期存款(01)、企业定期存款(05)、企业通知(08)、企业协定存款的余额(16)
        depositComSql.append(" (SELECT a.corp_id, ")
                .append(" a.cur_id, ")
                .append(" sum(b.balance) as balance ")
                .append(" from fc_acc a ")
                .append(" join (select bal.account, bal.cur_id, bal.balance ")
                .append(" from fc_acc_balance bal, ")
                .append(" (select t.account, ")
                .append(" t.cur_id, ")
                .append(" max(t.balance_date) maxdate ")
                .append(" from fc_acc_balance t ")
                .append(" where t.balance_date <=:endDate"+cycleType+" ");
        if(!StringUtils.isBlank(curId)){
            depositComSql.append("  and t.cur_id=:curId ");
        }
        depositComSql.append(" group by t.account, t.cur_id) abc ")
                .append(" where bal.account = abc.account ")
                .append(" and bal.cur_id = abc.cur_id ")
                .append(" and bal.balance_date = abc.maxdate ")
                .append(" group by bal.account, bal.cur_id, bal.balance) b ")
                .append(" on b.account = a.blnctr_acc ")
                .append(" and b.cur_id = a.cur_id ")
                .append(" and a.acc_type in ('01', '05', '08', '16') ")
                .append(" group by a.corp_id, a.cur_id ")
                .append(" union ")
                //银行真实存款的余额包含在线和离线账号，只要进入银行账号表的数据，都根据查询条件的日期统计
                .append(" select d.corp_id, e.cur_id, sum(e.bal) as bal ")
                .append(" from bt_bank_acc d ")
                .append(" join (select bal.bank_acc, bal.cur_id, bal.bal ")
                .append(" from bis_acc_bal bal, ")
                .append(" (select bank_acc, cur_id, max(bal_date) maxdate ")
                .append(" from bis_acc_bal ")
                .append(" where bal_date <=:endDate"+cycleType+" ");
        if(!StringUtils.isBlank(curId)){
            depositComSql.append("  and cur_id=:curId ");
        }
        depositComSql.append(" group by bank_acc, cur_id) abc ")
                .append(" where bal.bank_acc = abc.bank_acc ")
                .append(" and bal.bal_date = abc.maxdate ")
                .append(" and bal.cur_id = abc.cur_id ")
                .append(" and (bal.status is null or bal.status >= 95) ")
                .append(" group by bal.bank_acc, bal.cur_id, bal.bal) e ")
                .append(" ON d.bank_acc = e.bank_acc ")
                .append(" group by d.corp_id, e.cur_id )");
        return depositComSql;
    }

    /**
     * 存款查询公共sql
     */
    public StringBuilder queryDepositsComSql2(String cycleType,String netFlag,String dtlFlag,Boolean hasBank,String curId){
        StringBuilder depositComSql=new StringBuilder();
        if(netFlag!=null&&netFlag.equals("1")){//统计维度为融资银行时，以结算中心作为银行类型
            //内部存款没有银行，取结算中心作为银行类型
            if(dtlFlag.equals("1")){//查询明细
                depositComSql.append("( select deposit.corp_id  as bankCode,deposit.cur_id,  ")
                        .append(" BT_NVL_NUM(sum(deposit.balance), 0) as balance ")
                        .append(" from (SELECT a.corp_id, ");
            }else{
                depositComSql.append("( select deposit.net_id  as bank_type,deposit.cur_id,  ")
                        .append(" BT_NVL_NUM(sum(deposit.balance), 0) as balance ")
                        .append(" from (SELECT a.net_id as net_id, ");
            }
            depositComSql.append(" a.cur_id, ")
                    .append(" sum(b.balance) as balance ")
                    .append(" from fc_acc a ")
                    .append(" join (select bal.account, bal.cur_id, bal.balance ")
                    .append(" from fc_acc_balance bal, ")
                    .append(" (select t.account, ")
                    .append(" t.cur_id, ")
                    .append(" max(t.balance_date) maxdate ")
                    .append(" from fc_acc_balance t ")
                    .append(" where t.balance_date <=:endDate"+cycleType+" ");
            if(!StringUtils.isBlank(curId)){
                depositComSql.append("  and t.cur_id=:curId ");
            }
            depositComSql.append(" group by t.account, t.cur_id) abc ")
                    .append("  where bal.account = abc.account ")
                    .append(" and bal.cur_id = abc.cur_id ")
                    .append(" and bal.balance_date = abc.maxdate ")
                    .append(" group by bal.account, bal.cur_id, bal.balance) b ")
                    .append(" on b.account = a.blnctr_acc ")
                    .append(" and b.cur_id = a.cur_id ")
                    .append(" and a.acc_type in ('01', '05', '08', '16') ");
            if(dtlFlag.equals("1")){//查询明细
                depositComSql.append(" and a.net_id=:netId ")
                        .append(" group by a.corp_id, a.cur_id) deposit ")
                        .append(" group by deposit.corp_id, deposit.cur_id )");
            }else{
                depositComSql.append(" group by a.net_id, a.cur_id) deposit ")
                        .append(" group by deposit.net_id, deposit.cur_id )");
            }

        }else{
            //查银行存款，直接取银行类型
            if(dtlFlag.equals("1")){//查询明细
                depositComSql.append(" (select deposit.bankCode, deposit.cur_id, BT_NVL_NUM(sum(deposit.balance), 0) as balance ")
                        .append(" from ( ")
                        .append("select bank.sys_bank_code as bankCode,");
            }else{
                depositComSql.append(" (select deposit.bank_type, deposit.cur_id, BT_NVL_NUM(sum(deposit.balance), 0) as balance ")
                        .append(" from ( ")
                        .append("select bt.id as bank_type,");
            }
            depositComSql.append("  e.cur_id, ")
                    .append(" sum(e.bal) as balance ")
                    .append(" from bt_bank_acc d ")
                    .append(" join (select bal.bank_acc, bal.cur_id, bal.bal ")
                    .append(" from bis_acc_bal bal, ")
                    .append(" (select bank_acc, ")
                    .append(" cur_id, ")
                    .append(" max(bal_date) maxdate ")
                    .append(" from bis_acc_bal ")
                    .append(" where bal_date <=:endDate" + cycleType + " ");
            if(!StringUtils.isBlank(curId)){
                depositComSql.append("  and cur_id=:curId ");
            }
//                    .append("  and cur_id =:curId ")
            depositComSql.append(" group by bank_acc, cur_id) abc ")
                    .append(" where bal.bank_acc = abc.bank_acc ")
                    .append(" and bal.bal_date = abc.maxdate ")
                    .append(" and bal.cur_id = abc.cur_id ")
                    .append(" and (bal.status is null or bal.status >= 95) ")
                    .append(" group by bal.bank_acc, bal.cur_id, bal.bal) e ")
                    .append(" ON d.bank_acc = e.bank_acc ")
                    .append(" inner join bt_bank bank ")
                    .append(" on d.bank_code = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
            if (dtlFlag.equals("1")) {
                depositComSql.append(" and bt.id=:bankType ")
                        .append(" group by bank.sys_bank_code,e.cur_id)deposit ")
                        .append(" group by deposit.bankCode, deposit.cur_id) ");
            } else {
                if(hasBank){
                    depositComSql.append(" and bt.id=:bankType ");
                }
                depositComSql.append(" group by bt.id,e.cur_id)deposit ")
                        .append(" group by deposit.bank_type, deposit.cur_id)");
            }
        }
        return depositComSql;
    }


}
