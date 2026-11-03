package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.service.LoanBalanceTrendService;
import com.bytter.report.domain.cms.vo.LoanBalanceTrendVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName LoanBalanceTrendDao
 * @Description TODO
 * @Author fusql
 * @Date 2020/1/16 14:47
 * @Version 1.0
 **/
@Repository
public class LoanBalanceTrendDao extends BaseDao {
    @Autowired
    LoanBalanceTrendService loanBalanceTrendService;

    /**
    *@Description 内部贷款查询SQL
    *@Param paramsVO
    *@Param corpAuth
    *@return java.lang.String
    *@Date 2020/1/17 13:39
    *@Author fusb
    */
    public String innerLoanSql(LoanBalanceTrendVO paramsVO,String startYears,String endYears,String trendDate){
        StringBuilder sql = new StringBuilder();
        sql.append("   select '01' as loan_sign, ");
        sql.append("          '"+trendDate+"' as trend_date,");
        sql.append("        cms.corp_id as corp_id,");
        sql.append("        cms.corp_name as corp_name,");
        sql.append("        cms.cur_name as cur_name,");
        sql.append("        cms.cur_code as cur_code,");
        sql.append("        cms.send_money as send_money,");
        sql.append("        cms.repay_money as repay_money,");
        sql.append("        cms.repay_away_money as repay_away_money,");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("   from (select info.corp_id as corp_id,");
        sql.append("        info.corp_name as corp_name,");
        sql.append("        info.cur_name as cur_name,");
        sql.append("        info.cur_code as cur_code,");
        sql.append("        bt_nvl_num(sum(send.loan_money),0) as send_money,");
        sql.append("        bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("        bt_nvl_num(sum(repay_away.repay_away_money),0) as repay_away_money ");
        sql.append("        from cms_loan_info info inner join cms_loan_send send on ");
        sql.append("        send.loan_code=info.bill_code left join ");
        // 已还款额度
        sql.append("        (select re.send_code,sum(re.repay_money) as repay_money ");
        sql.append("        from cms_loan_repay re where re.status >= 102 and re.status <> 120 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code  ");
        // 还款在途额度
        sql.append("       left join (select re.send_code,bt_nvl_num(sum(repay_money), 0) repay_away_money ");
        sql.append("       from cms_loan_repay re where re.status<>-2 and (re.status < 102 or re.status = 120) ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("       group by re.send_code) repay_away on repay_away.send_code=send.bill_code ");
        sql.append("       where send.status >= 102 and send.status < 110 and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.loan_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by info.corp_id,info.cur_code , info.corp_name, info.cur_name) cms ");
        return sql.toString();
    }


    /**
    *@Description 银行借款查询SQL
    *@Param paramsVO
    *@Param corpAuth
    *@return java.lang.String
    *@Date 2020/1/17 13:39
    *@Author fusb
    */
    public String provideLoanSql(LoanBalanceTrendVO paramsVO,String startYears,String endYears,String trendDate){
        StringBuilder sql = new StringBuilder();
        sql.append("    select '02' as loan_sign, ");
        sql.append("          '"+trendDate+"' as trend_date,");
        sql.append("           cms.corp_id as corp_id,");
        sql.append("           cms.corp_name as corp_name,");
        sql.append("           cms.cur_name as cur_name,");
        sql.append("           cms.cur_id as cur_code,");
        sql.append("           cms.send_money as send_money,");
        sql.append("           cms.repay_money as repay_money,");
        sql.append("           cms.repay_away_money as repay_away_money, ");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("    from   (select info.corp_id as corp_id,");
        sql.append("           info.corp_name as corp_name,");
        sql.append("           info.cur_name as cur_name,");
        sql.append("           info.cur_id as cur_id,");
        sql.append("           bt_nvl_num(sum(send.money),0) as send_money,");
        sql.append("           bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("           bt_nvl_num(sum(repay_away.repay_away_money),0) as repay_away_money ");
        sql.append("           from cms_provide_loan_info info inner join cms_provide_loan_send send ");
        sql.append("           on send.loan_code = info.bill_code left join ");
        // 已还金额
        sql.append("           (select re.send_code,sum(re.repay_money) as repay_money ");
        sql.append("           from cms_provide_loan_repay re where re.status >= 102 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code  ");
        // 还款在途金额
        sql.append("       left join (select re.send_code,sum(re.repay_money) as repay_away_money ");
        sql.append("        from cms_provide_loan_repay re where re.status < 102 and status >= -1 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay_away on repay_away.send_code=send.bill_code  ");
        sql.append("       where send.status >= 102 and send.status < 110 and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.send_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by info.corp_id,info.cur_id , info.corp_name, info.cur_name) cms ");
        return sql.toString();
    }

    /**
    *@Description 委托贷款查询SQL
    *@Param paramsVO
    *@Param corpAuth
    *@return java.lang.String
    *@Date 2020/1/17 13:40
    *@Author fusb
    */
    public String precativeLoanSql(LoanBalanceTrendVO paramsVO,String startYears,String endYears,String trendDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("    select '03' as loan_sign, ");
        sql.append("          '"+trendDate+"' as trend_date,");
        sql.append("           cms.corp_id as corp_id,");
        sql.append("           cms.corp_name as corp_name,");
        sql.append("           cms.cur_name as cur_name,");
        sql.append("           cms.cur_id as cur_code,");
        sql.append("           cms.send_money as send_money,");
        sql.append("           cms.repay_money as repay_money,");
        sql.append("           cms.repay_away_money as repay_away_money, ");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("    from  (select info.loan_opp as corp_id,");
        sql.append("           info.corp_name as  corp_name,");
        sql.append("           info.cur_name as  cur_name,");
        sql.append("           info.cur_id as  cur_id,");
        sql.append("           bt_nvl_num(sum(send.money),0) as send_money,");
        sql.append("           bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("           bt_nvl_num(sum(repay_away_money),0) as repay_away_money ");
        sql.append("           from cms_precative_loan_info info inner join cms_precative_loan_send send ");
        sql.append("           on send.father_code=info.bill_code ");
        // 还款金额
        sql.append("           left join (select re.send_code as send_code,bt_nvl_num(sum(re.repay_money),0) as repay_money ");
        sql.append("           from cms_precative_loan_repay re where re.status >= 102 and bt_nvl(re.repay_type, 'sss') <> '4' ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code left join ");
        // 还款在途金额
        sql.append("        (select re.send_code,sum(re.repay_money) as repay_away_money ");
        sql.append("        from cms_precative_loan_repay re where re.status < 102 and status <> -2 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay_away on repay_away.send_code=send.bill_code  ");
        sql.append("        where send.status >= 102 and send.status < 110  and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.send_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("       group by info.loan_opp,info.cur_id , info.corp_name, info.cur_name) cms ");
        return sql.toString();
    }

    /**
    *@Description 贷款余额趋势分析报表
    *@Param
    *@return java.util.List<com.bytter.report.domain.cms.vo.LoanBalanceTrendVO>
    *@Date 2020/1/17 13:40
    *@Author fusb
    */
    public List<LoanBalanceTrendVO> loanBalanceTrendVOList (LoanBalanceTrendVO paramsVO,List<String> monthList,List<String> corpAuth) {
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        String startYears = monthList.get(0);
        String endYears = "";
        String trendDate = "";
        sql.append(" select  bt.loan_sign as loanSign,");
        sql.append("         bt.corp_id as corpId,");
        sql.append("         bt.corp_name as corpName,");
        sql.append("         bt.cur_name as curName,");
        sql.append("         bt.cur_code as curCode,");
        sql.append("         bt.trend_date as trendDate,");
        sql.append("         bt.send_money as sendMoney,");
        sql.append("         bt.repay_money as repayMoney,");
        sql.append("         bt.repay_away_money as repayAwayMoney,");
        sql.append("         bt.bal_money as balMoney ");
        sql.append(" from ( " );
        for (int i = 0;i < monthList.size();i++) {
            endYears = loanBalanceTrendService.lastDateOfMonth(monthList.get(i));
            trendDate = monthList.get(i);
            if (i >= 1) {
                sql.append(" union all ");
            }
            sql.append(this.innerLoanSql(paramsVO,startYears,endYears,trendDate)) ;
            sql.append(" union all ");
            sql.append(this.provideLoanSql(paramsVO,startYears,endYears,trendDate));
            sql.append(" union all ");
            sql.append(this.precativeLoanSql(paramsVO,startYears,endYears,trendDate));
        }
        sql.append(" ) bt where 1= 1 ");
        if (StringUtils.isNotEmpty(paramsVO.getCorpId())){
            String[] corpArr = paramsVO.getCorpId().split(",");
            sql.append(" and "+ SqlUtil.getInSql(corpArr, "bt.corp_id", true));
        } else {
            sql.append("    and ").append(getSQLIn(corpAuth, 900, "bt.corp_id")).append(" ");
        }
        if (StringUtils.isNotEmpty(paramsVO.getCurCode())) {
            sql.append("    and bt.cur_code=:curCode ");
        }
        sql.append("       order by bt.loan_sign ");
        params.put("curCode",paramsVO.getCurCode());
        return this.listBySQLAliasToBean(sql.toString(),params,LoanBalanceTrendVO.class);
    }

    public List<LoanBalanceTrendVO> loanBalanceChart(LoanBalanceTrendVO paramsVO,List<String> monthList,List<String> corpAuth){
        Map<String,Object> params = new HashMap<String,Object>();
        StringBuilder sql = new StringBuilder();
        String startYears = monthList.get(0);
        String endYears = "";
        String trendDate = "";
        sql.append(" select  trend.loan_sign as loanSign,");
        sql.append("         trend.cur_code as curCode,");
        sql.append("         trend.trend_date as trendDate,");
        sql.append("         sum(trend.send_money) as sendMoney,");
        sql.append("         sum(trend.repay_money) as repayMoney,");
        sql.append("         sum(trend.repay_away_money) as repayAwayMoney,");
        sql.append("         sum(trend.bal_money) as balMoney ");
        sql.append("    from ( ");
        sql.append("    select  bt.loan_sign as loan_sign,");
        sql.append("         bt.corp_id as corp_id,");
        sql.append("         bt.cur_code as cur_code,");
        sql.append("         bt.trend_date as trend_date,");
        sql.append("         bt.send_money as send_money,");
        sql.append("         bt.repay_money as repay_money,");
        sql.append("         bt.repay_away_money as repay_away_money,");
        sql.append("         bt.bal_money as bal_money ");
        sql.append(" from ( " );
        for (int i = 0;i < monthList.size();i++) {
            endYears = loanBalanceTrendService.lastDateOfMonth(monthList.get(i));
            trendDate = monthList.get(i);
            if (i >= 1) {
                sql.append(" union all ");
            }
            sql.append(this.innerLoanSql(paramsVO,startYears,endYears,trendDate)) ;
            sql.append(" union all ");
            sql.append(this.provideLoanSql(paramsVO,startYears,endYears,trendDate));
            sql.append(" union all ");
            sql.append(this.precativeLoanSql(paramsVO,startYears,endYears,trendDate));
        }
        sql.append(" ) bt where 1= 1 ");
        if (StringUtils.isNotEmpty(paramsVO.getCorpId())){
            String[] corpArr = paramsVO.getCorpId().split(",");
            sql.append(" and " + SqlUtil.getInSql(corpArr, "bt.corp_id", true));
        } else {
            sql.append("    and ").append(getSQLIn(corpAuth, 900, "bt.corp_id")).append(" ");
        }
        if (StringUtils.isNotEmpty(paramsVO.getCurCode())) {
            sql.append("    and bt.cur_code=:curCode ");
            params.put("curCode",paramsVO.getCurCode());
        }
        sql.append("       order by bt.loan_sign,bt.trend_date ");
        sql.append("      ) trend ");
        sql.append(" group by trend.loan_sign,trend.cur_code,trend.trend_date");
        return this.listBySQLAliasToBean(sql.toString(),params,LoanBalanceTrendVO.class);
    }


    public List<LoanBalanceTrendVO> getinnerLoanMobileData(LoanBalanceTrendVO paramsVO, String endYears, String trendDate, String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("   select '01' as loanSign, ");
        sql.append("          '" + trendDate + "' as trendDate,");
        sql.append("        cms.corp_id as corpId,");
        sql.append("        cms.cur_code as curCode,");
        sql.append("        cms.send_money as sendMoney,");
        sql.append("        cms.repay_money as repayMoney,");
        sql.append("        cms.repay_away_money as repayAwayMoney,");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("   from (select info.corp_id as corp_id,");
        sql.append("        info.cur_code as cur_code,");
        sql.append("        bt_nvl_num(sum(send.loan_money),0) as send_money,");
        sql.append("        bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("        bt_nvl_num(sum(repay_away.repay_away_money),0) as repay_away_money ");
        sql.append("        from cms_loan_info info inner join cms_loan_send send on ");
        sql.append("        send.loan_code=info.bill_code left join ");
        // 已还款额度
        sql.append("        (select re.send_code,sum(re.repay_money) as repay_money ");
        sql.append("        from cms_loan_repay re where re.status >= 102 and re.status <> 120 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('" + endYears + "','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code  ");
        // 还款在途额度
        sql.append("       left join (select re.send_code,bt_nvl_num(sum(repay_money), 0) repay_away_money ");
        sql.append("       from cms_loan_repay re where re.status<>-2 and (re.status < 102 or re.status = 120) ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('" + endYears + "','yyyy-MM-dd') ");
        }
        sql.append("       group by re.send_code) repay_away on repay_away.send_code=send.bill_code ");
        sql.append("       where send.status >= 102 and send.status < 110 and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.loan_date <= BT_TO_DATE('" + endYears + "','yyyy-MM-dd') ");
        }
        sql.append(" and info.corp_id = '" + corpId + "' ");
        sql.append(" and info.cur_code = '" + paramsVO.getCurCode() + "' ");
        sql.append("        group by info.corp_id,info.cur_code ) cms ");
        return this.listBySQLAliasToBean(sql.toString(), new HashMap<String, Object>(), LoanBalanceTrendVO.class);
    }

    public List<LoanBalanceTrendVO> getProvideLoanMobileData(LoanBalanceTrendVO paramsVO,String endYears,String trendDate,String corpId){
        StringBuilder sql = new StringBuilder();
        sql.append("    select '02' as loanSign, ");
        sql.append("          '"+trendDate+"' as trendDate,");
        sql.append("           cms.corp_id as corpId,");
        sql.append("           cms.cur_id as curCode,");
        sql.append("           cms.send_money as sendMoney,");
        sql.append("           cms.repay_money as repayMoney,");
        sql.append("           cms.repay_away_money as repayAwayMoney, ");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("    from   (select info.corp_id as corp_id,");
        sql.append("           info.cur_id as cur_id,");
        sql.append("           bt_nvl_num(sum(send.money),0) as send_money,");
        sql.append("           bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("           bt_nvl_num(sum(repay_away.repay_away_money),0) as repay_away_money ");
        sql.append("           from cms_provide_loan_info info inner join cms_provide_loan_send send ");
        sql.append("           on send.loan_code = info.bill_code left join ");
        // 已还金额
        sql.append("           (select re.send_code,sum(re.repay_money) as repay_money ");
        sql.append("           from cms_provide_loan_repay re where re.status >= 102 ");

        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code  ");
        // 还款在途金额
        sql.append("       left join (select re.send_code,sum(re.repay_money) as repay_away_money ");
        sql.append("        from cms_provide_loan_repay re where re.status < 102 and status >= -1 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay_away on repay_away.send_code=send.bill_code  ");
        sql.append("       where send.status >= 102 and send.status < 110 and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.send_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append(" and info.corp_id = '" + corpId + "' ");
        sql.append(" and info.cur_id = '" + paramsVO.getCurCode() + "' ");
        sql.append("        group by info.corp_id,info.cur_id ) cms ");
        return this.listBySQLAliasToBean(sql.toString(),new HashMap<String, Object>(),LoanBalanceTrendVO.class);
    }


    public List<LoanBalanceTrendVO> getPrecativeLoanMobileData(LoanBalanceTrendVO paramsVO,String endYears,String trendDate,String corpId) {
        StringBuilder sql = new StringBuilder();
        sql.append("    select '03' as loanSign, ");
        sql.append("          '"+trendDate+"' as trendDate,");
        sql.append("           cms.corp_id as corpId,");
        sql.append("           cms.cur_id as curCode,");
        sql.append("           cms.send_money as sendMoney,");
        sql.append("           cms.repay_money as repayMoney,");
        sql.append("           cms.repay_away_money as repayAwayMoney, ");
        if ("0".equals(paramsVO.getIncludeFreezeMoney())) { // 余额包含在途金额
            sql.append("      (cms.send_money-cms.repay_money-cms.repay_away_money) as bal_money ");
        } else {
            sql.append("      (cms.send_money-cms.repay_money) as bal_money ");
        }
        sql.append("    from  (select info.loan_opp as corp_id,");
        sql.append("           info.cur_id as  cur_id,");
        sql.append("           bt_nvl_num(sum(send.money),0) as send_money,");
        sql.append("           bt_nvl_num(sum(repay.repay_money),0) as repay_money,");
        sql.append("           bt_nvl_num(sum(repay_away_money),0) as repay_away_money ");
        sql.append("           from cms_precative_loan_info info inner join cms_precative_loan_send send ");
        sql.append("           on send.father_code=info.bill_code ");
        // 还款金额
        sql.append("           left join (select re.send_code as send_code,bt_nvl_num(sum(re.repay_money),0) as repay_money ");
        sql.append("           from cms_precative_loan_repay re where re.status >= 102 and bt_nvl(re.repay_type, 'sss') <> '4' ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay on repay.send_code=send.bill_code left join ");
        // 还款在途金额
        sql.append("        (select re.send_code,sum(re.repay_money) as repay_away_money ");
        sql.append("        from cms_precative_loan_repay re where re.status < 102 and status <> -2 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and re.repay_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append("        group by re.send_code ) repay_away on repay_away.send_code=send.bill_code  ");
        sql.append("        where send.status >= 102 and send.status < 110  and info.status > 95 ");
        if (StringUtils.isNotEmpty(endYears)) {
            sql.append("    and send.send_date <= BT_TO_DATE('"+endYears+"','yyyy-MM-dd') ");
        }
        sql.append(" and info.corp_id = '" + corpId + "' ");
        sql.append(" and info.cur_id = '" + paramsVO.getCurCode() + "' ");
        sql.append("       group by info.loan_opp,info.cur_id ) cms ");
        return this.listBySQLAliasToBean(sql.toString(),new HashMap<String, Object>(),LoanBalanceTrendVO.class);
    }
}
