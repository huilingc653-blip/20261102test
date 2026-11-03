package com.bytter.report.domain.cms.dao;

import com.bytter.report.domain.cms.utils.LoanDailyAnalysisUtils;
import com.bytter.report.domain.cms.vo.LoanDailyAnalysisVO;
import com.bytter.report.domain.cms.vo.LoanDailyParamVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CmsConstant;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author luxy
 * @Title: LoanDailyAnalysisDao
 * @ProjectName T3(3.2)
 * @Description: 日均贷款分析报表数据访问层
 * @date 2020/2/24 14:03
 */
@Repository
public class LoanDailyAnalysisDao extends BaseDao {

    /**
     * 分单位查询日均贷款分析报表数据
     */
    public DataTablesOutput corpDatatable(DataTablesInput input, List<String> userCorps){
        Map<String,Object> params=new HashMap<String,Object>();
        String corps=(String)input.getFormdata().get("corpId");//查询条件
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corps)) {
            corpIds = StringUtils.splitToList(",", corps);
        }
        String loanSign=(String)input.getFormdata().get("loanSign");//融资方式
        String isContain=(String)input.getFormdata().get("isContain");//是否包含在途
        String curId=(String)input.getFormdata().get("curId");//币别
        StringBuilder sql=new StringBuilder();
        sql.append(" select * from ( ");
        sql.append("select curSum.*,BT_NVL_NUM(preSum.preLoanMoney,0) preLoanMoney, BT_NVL_NUM(lastSum.lastLoanMoney,0) lastLoanMoney from( ");
        sql.append(queryLoanByPeriod("cur",corpIds,isContain,loanSign));  //查询本期
        sql.append(" )curSum ");
        sql.append(" left join (");
        sql.append(queryLoanByPeriod("pre",corpIds,isContain,loanSign));  //查询上期
        sql.append(" )preSum ");
        sql.append(" on curSum.corpId=preSum.corpId ")
                .append(" and curSum.curId=preSum.curId");
        sql.append(" left join (");
        sql.append(queryLoanByPeriod("last",corpIds,isContain,loanSign));  //查询上年同期
        sql.append(" )lastSum ");
        sql.append(" on curSum.corpId=lastSum.corpId ")
                .append(" and curSum.curId=lastSum.curId");
        sql.append(" )sortByCorp ")
                .append(" where sortByCorp.corpId is not null ");
        if(EmptyUtils.isNotEmpty(userCorps)){//用户可查询单位
            sql.append(" and ").append(getSQLIn(userCorps, 900, "sortByCorp.corpId"));
        }
        if (!StringUtils.isBlank(curId)) {
            sql.append(" and sortByCorp.curId=:curId ");
            params.put("curId", curId);
        }
        sql.append(" order by sortByCorp.curLoanMoney desc ");
        setParams(input,params);
        return getDynamicBySQL(input, sql.toString(), LoanDailyAnalysisVO.class, "", false,params);
    }
    /**
     * 按贷款机构分类查询日均贷款分析报表数据
     */
    public DataTablesOutput bankDatatable(DataTablesInput input, List<String> userCorps){
        String corps=(String)input.getFormdata().get("corpId");//查询条件
        List<String> corpIds = null;
        if(EmptyUtils.isNotEmpty(corps)) {
            corpIds = StringUtils.splitToList(",", corps);
        }else {
            corpIds=userCorps;
        }
        String netFlag=(String)input.getFormdata().get("netFlag");
        String loanSign=(String)input.getFormdata().get("loanSign");//融资方式
        String isContain=(String)input.getFormdata().get("isContain");//是否包含在途
        String curId=(String)input.getFormdata().get("curId");//币别
        StringBuilder sql=new StringBuilder();
        sql.append(" select * from ( ");
        sql.append("select curSum.*,BT_NVL_NUM(preSum.preLoanMoney,0) preLoanMoney, BT_NVL_NUM(lastSum.lastLoanMoney,0) lastLoanMoney  from( ");
        sql.append(queryLoanByPeriod2("cur",netFlag,corpIds,isContain,loanSign));  //查询本期
        sql.append(" )curSum ");
        sql.append(" left join (");
        sql.append(queryLoanByPeriod2("pre",netFlag,corpIds,isContain,loanSign));  //查询上期
        sql.append(" )preSum ");
        sql.append(" on curSum.bankType=preSum.bankType ")
                .append(" and curSum.curId=preSum.curId");
        sql.append(" left join (");
        sql.append(queryLoanByPeriod2("last",netFlag,corpIds,isContain,loanSign));  //查询上年同期
        sql.append(" )lastSum ");
        sql.append(" on curSum.bankType=lastSum.bankType ")
                .append(" and curSum.curId=lastSum.curId");
        sql.append(" )sortByBank ")
                .append(" where sortByBank.bankType is not null ");
        Map<String,Object> params=new HashMap<String,Object>();
        if (!StringUtils.isBlank(curId)) {
            sql.append(" and sortByBank.curId=:curId ");
            params.put("curId", curId);
        }
        sql.append(" order by sortByBank.curLoanMoney desc ");
        setParams(input,params);
        return getDynamicBySQL(input, sql.toString(), LoanDailyAnalysisVO.class, "", false,params);
    }


    public List<LoanDailyAnalysisVO> balanceListByCorp(LoanDailyParamVO paramVO,String dtlFlag){
        String isContain=paramVO.getIsContain();//是否包含在途
        StringBuilder sql=new StringBuilder();
        if(dtlFlag.equals(CmsConstant.IS_TRUE)){//查询各类贷款的余额情况
            sql.append(" select  loanSum.loan_sign as loanSign,loanSum.corpId,loanSum.curId,loanSum.stayMoney as curLoanMoney  from ( ");
        }else{//各类贷款额度汇总
            sql.append(" select loanSum.corpId,loanSum.curId,BT_NVL_NUM(sum(loanSum.stayMoney), 0) dailyBalance  from ( ");
        }
        if(!StringUtils.isBlank(paramVO.getLoanSign())){
            String loanSign=paramVO.getLoanSign();
            switch (loanSign) {
                case "01"://查询内部贷款
                    sql.append(queryLoanSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
                    break;
                case "02"://查询银行借款
                    sql.append(queryProvideSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
                    break;
            }
        }else{
            sql.append(queryLoanSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
            sql.append("  union all ");
            sql.append(queryProvideSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
            sql.append("  union all ");
            sql.append(queryPreSqlCommon("cur",CmsConstant.IS_FALSE,isContain));
        }
        sql.append(" ) loanSum ")
                .append(" where loanSum.corpId=:corpId ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("corpId",paramVO.getCorpId());
        if(!StringUtils.isBlank(paramVO.getLoanSign())){
            sql.append(" and loanSum.loan_sign=:loanSign ");
            params.put("loanSign",paramVO.getLoanSign());
        }
        if (!StringUtils.isBlank(paramVO.getCurId())) {
            sql.append(" and loanSum.curId=:curId ");
            params.put("curId", paramVO.getCurId());
        }
        if(dtlFlag.equals(CmsConstant.IS_FALSE)){//各类贷款额度汇总
            sql.append("  group by loanSum.corpId,loanSum.curId ");
        }
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 根据银行类型查询各类贷款余额（查询银行借款及委托方为银行的委托贷款数据）
     */
    public List<LoanDailyAnalysisVO> balanceListByBankType(LoanDailyParamVO paramVO,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        if(dtlFlag.equals(CmsConstant.IS_TRUE)){//查询各类贷款的余额情况
            sql.append(" select  loanSum.loan_sign as loanSign,loanSum.bankType,loanSum.curId,loanSum.stayMoney as curLoanMoney  from ( ");
        }else{//各类贷款额度汇总
            sql.append(" select loanSum.bankType,loanSum.curId,BT_NVL_NUM(sum(loanSum.stayMoney), 0) dailyBalance  from ( ");
        }
        if(!StringUtils.isBlank(paramVO.getLoanSign())){
            String loanSign=paramVO.getLoanSign();
            switch (loanSign) {
                case "02"://查询银行借款
                    sql.append(queryProvideSqlCommon2("cur", CmsConstant.IS_FALSE, paramVO.getCorps(), paramVO.getIsContain()));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSqlCommon2("cur",  CmsConstant.IS_FALSE, paramVO.getCorps(), paramVO.getIsContain()));
                    break;
            }
        }else {
            sql.append(queryProvideSqlCommon2("cur", CmsConstant.IS_FALSE, paramVO.getCorps(), paramVO.getIsContain()));
            sql.append("  union all ");
            sql.append(queryPreSqlCommon2("cur",  CmsConstant.IS_FALSE, paramVO.getCorps(), paramVO.getIsContain()));
        }
        sql.append(" ) loanSum ")
                .append(" where loanSum.bankType=:bankType ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("bankType",paramVO.getBankType());
        if(!StringUtils.isBlank(paramVO.getLoanSign())){
            sql.append(" and loanSum.loan_sign=:loanSign ");
            params.put("loanSign",paramVO.getLoanSign());
        }
        if (!StringUtils.isBlank(paramVO.getCurId())) {
            sql.append(" and loanSum.curId=:curId ");
            params.put("curId", paramVO.getCurId());
        }
        if(dtlFlag.equals(CmsConstant.IS_FALSE)){//各类贷款额度汇总
            sql.append("  group by loanSum.bankType,loanSum.curId ");
        }
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 根据结算中心查询各类贷款贷款余额(查询内部贷款及委托方非银行的委托贷款数据)
     */
    public List<LoanDailyAnalysisVO> balanceListByNetId(LoanDailyParamVO paramVO,String dtlFlag){
        StringBuilder sql = new StringBuilder();
        if(dtlFlag.equals(CmsConstant.IS_TRUE)){//查询各类贷款的余额情况
            sql.append(" select  loanSum.loan_sign as loanSign,loanSum.bankType,loanSum.curId,loanSum.stayMoney as curLoanMoney  from ( ");
        }else{//各类贷款额度汇总
            sql.append(" select loanSum.bankType,loanSum.curId,BT_NVL_NUM(sum(loanSum.stayMoney), 0) dailyBalance  from ( ");
        }
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            String loanSign = paramVO.getLoanSign();
            switch (loanSign) {
                case "01"://查询内部贷款
                    sql.append(queryLoanSqlCommon2("cur", paramVO.getCorps(), paramVO.getIsContain()));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSqlCommon2("cur", CmsConstant.IS_TRUE, paramVO.getCorps(), paramVO.getIsContain()));
                    break;
            }
        } else {
            sql.append(queryLoanSqlCommon2("cur", paramVO.getCorps(), paramVO.getIsContain()));
            sql.append(" union all ");
            sql.append(queryPreSqlCommon2("cur",CmsConstant.IS_TRUE, paramVO.getCorps(), paramVO.getIsContain()));
        }
        sql.append(" )loanSum ");
        sql.append(" where loanSum.bankType=:netId ")
                .append(" and loanSum.curId=:curId ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("netId",paramVO.getNetId());
        if(dtlFlag.equals(CmsConstant.IS_FALSE)){//各类贷款额度汇总
            sql.append("  group by loanSum.bankType,loanSum.curId ");
        }
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 设置参数
     */
    public void setParams(DataTablesInput input,Map<String,Object> params){
        String startDate=(String)input.getFormdata().get("startDate");
        String endDate=(String)input.getFormdata().get("endDate");
        String searchFrequency=(String)input.getFormdata().get("searchFrequency");//查询频率 0-按天查询 1-按月查询
        Map<String,Object> dateMap=LoanDailyAnalysisUtils.getDateMap(startDate,endDate,searchFrequency);
        Date searchEndDate=(Date)dateMap.get("searchEndDate");
        Date preEndDate=(Date)dateMap.get("preEndDate");
        Date lastEndDate=(Date)dateMap.get("lastEndDate");//上年同期
        params.put("curEndDate",searchEndDate);
        params.put("preEndDate",preEndDate);
        params.put("lastEndDate",lastEndDate);
    }

    /**
     * 根据条件分日期查询发放额度
     * @param paramVO
     * @return
     */
    public List<LoanDailyAnalysisVO> dailySendMoneyList(LoanDailyParamVO paramVO) {
        String typesOfStatistics = paramVO.getTypesOfStatistics();//统计维度 0-单位 1-贷款机构
        StringBuilder sql = new StringBuilder();
        sql.append(" select *  from ( ");
        if(null!=typesOfStatistics&&typesOfStatistics.equals("1")){
            String netId =paramVO.getNetId();
            if(!StringUtils.isBlank(netId)){//贷款机构为结算中心
                sql.append(dailySendMoneyListByNetId(paramVO));
            }else{//贷款机构为银行
                sql.append(dailySendMoneyListByBankType(paramVO));
            }
        }else{//以单位为条件查询
            sql.append(dailySendMoneyListByCorp(paramVO));
        }
        sql.append(" )sendSum ");
        if(paramVO.getTypesOfStatistics().equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" where sendSum.corpId=:corpId ");
        }else{
            sql.append(" where sendSum.bankType=:bankType ");
        }
        sql.append(" and sendSum.curId=:curId ");
        if(!StringUtils.isBlank(paramVO.getStartDate())){
            if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){
                sql.append(" and sendSum.balanceDate>=:searchStartDate ");//按月查询时包含开始日期
            }else{
                sql.append(" and sendSum.balanceDate>:searchStartDate ");
            }
        }
        sql.append(" order by sendSum.balanceDate ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("corpId",paramVO.getCorpId());
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 根据单位查询发放额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailySendMoneyListByCorp(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "01"://查询内部贷款
                    sql.append(queryLoanSendSqlCommon("cur", paramVO.getTypesOfStatistics(),null,null));
                    break;
                case "02"://查询银行借款
                    sql.append(queryProSendSqlCommon("cur",paramVO.getTypesOfStatistics(),null,null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),null,null));
                    break;
            }
        } else {
            sql.append(queryLoanSendSqlCommon("cur",paramVO.getTypesOfStatistics(),null,null));
            sql.append(" union all ");
            sql.append(queryProSendSqlCommon("cur", paramVO.getTypesOfStatistics(),null,null));
            sql.append(" union all ");
            sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),null,null));
        }
        return sql;
    }

    /**
     * 根据银行类型查询发放额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailySendMoneyListByBankType(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "02"://查询银行借款
                    sql.append(queryProSendSqlCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
            }
        } else {
            sql.append(queryProSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
        }
        return sql;
    }

    /**
     * 根据结算中心查询发放额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailySendMoneyListByNetId(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "01"://查询内部贷款
                    sql.append(queryLoanSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
            }
        } else {
            sql.append(queryLoanSendSqlCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
            sql.append(" union all ");
            sql.append(queryPreSendSqlCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
        }
        return sql;
    }
    /**
     * 根据条件分日期查询已还款额度
     * @param paramVO
     * @return
     */
    public List<LoanDailyAnalysisVO> dailyRepayMoneyList(LoanDailyParamVO paramVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select * from ( ");
        String typesOfStatistics = paramVO.getTypesOfStatistics();//统计维度 0-单位 1-贷款机构
        if(null!=typesOfStatistics&&typesOfStatistics.equals("1")){
            String netId =paramVO.getNetId();
            if(!StringUtils.isBlank(netId)){//贷款机构为结算中心
                sql.append(dailyRepayMoneyListByNetId(paramVO));
            }else{//贷款机构为银行
                sql.append(dailyRepayMoneyListByBankType(paramVO));
            }
        }else{//以单位为条件查询
            sql.append(dailyRepayMoneyListByCorp(paramVO));
        }
        sql.append(" )repaySum ");
        if(paramVO.getTypesOfStatistics().equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" where repaySum.corpId=:corpId ");
        }else{
            sql.append(" where repaySum.bankType=:bankType ");
        }
        sql.append(" and repaySum.curId=:curId ");
        if(!StringUtils.isBlank(paramVO.getStartDate())){
            if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){
                sql.append(" and repaySum.repayDate>=:searchStartDate ");//按月查询时包含开始日期
            }else{
                sql.append(" and repaySum.repayDate>:searchStartDate ");
            }
        }
        sql.append(" order by repaySum.repayDate ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("corpId",paramVO.getCorpId());
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 根据单位查询已还款额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayMoneyListByCorp(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "01"://查询内部贷款
                    sql.append(queryLoanRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "02"://查询银行借款
                    sql.append(queryProRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
            }
        } else {
            sql.append(queryLoanRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryProRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
        }
        return sql;
    }
    /**
     * 根据银行类型查询已还款额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayMoneyListByBankType(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "02"://查询银行借款
                    sql.append(queryProRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
            }
        } else {
            sql.append(queryProRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
        }
        return sql;
    }

    /**
     * 根据结算中心查询已还款额度
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayMoneyListByNetId(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            switch (paramVO.getLoanSign()) {
                case "01"://查询内部贷款
                    sql.append(queryLoanRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
            }
        } else {
            sql.append(queryLoanRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
            sql.append(" union all ");
            sql.append(queryPreRepayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
        }
        return sql;
    }

    /**
     * 根据条件分日期查询还款在途额度变化
     * @param paramVO
     * @return
     */
    public List<LoanDailyAnalysisVO> dailyRepayAwayMoneyList(LoanDailyParamVO paramVO) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select *  from ( ");
        String typesOfStatistics = paramVO.getTypesOfStatistics();//统计维度 0-单位 1-贷款机构
        if(null!=typesOfStatistics&&typesOfStatistics.equals("1")){
            String netId =paramVO.getNetId();
            if(!StringUtils.isBlank(netId)){//贷款机构为结算中心
                sql.append(dailyRepayAwayMoneyListByNetId(paramVO));
            }else{//贷款机构为银行
                sql.append(dailyRepayAwayMoneyListByBankType(paramVO));
            }
        }else{//以单位为条件查询
            sql.append(dailyRepayAwayMoneyListByCorp(paramVO));
        }
        sql.append(" )repayAwaySum ");
        if(paramVO.getTypesOfStatistics().equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" where repayAwaySum.corpId=:corpId ");
        }else{
            sql.append(" where repayAwaySum.bankType=:bankType ");
        }

        sql.append(" and repayAwaySum.curId=:curId ");
        if(!StringUtils.isBlank(paramVO.getStartDate())){
            if(paramVO.getSearchFrequency().equals(CmsConstant.SEARCH_BY_MONTH)){
                sql.append(" and repayAwaySum.sendDate>=:searchStartDate ");//按月查询时包含开始日期
            }else{
                sql.append(" and repayAwaySum.sendDate>:searchStartDate ");
            }
        }
        sql.append(" order by repayAwaySum.repayAwayDate ");
        Map<String,Object> params=new HashMap<String,Object>();
        params.put("corpId",paramVO.getCorpId());
        setParams(paramVO,params);
        return listBySQLAliasToBean(sql.toString(),params,LoanDailyAnalysisVO.class);
    }

    /**
     * 根据单位查询还款在途
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayAwayMoneyListByCorp(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            String loanSign = paramVO.getLoanSign();
            switch (loanSign) {
                case "01"://查询内部贷款
                    sql.append(queryLoanRepayAwayCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "02"://查询银行借款
                    sql.append(queryProRepayAwayCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
            }
        } else {
            sql.append(queryLoanRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryProRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
        }
        return sql;
    }
    /**
     * 根据银行类型查询还款在途
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayAwayMoneyListByBankType(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            String loanSign = paramVO.getLoanSign();
            switch (loanSign) {
                case "02"://查询银行借款
                    sql.append(queryProRepayAwayCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
                    break;
            }
        } else {
            sql.append(queryProRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
            sql.append(" union all ");
            sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),null));
        }
        return sql;
    }
    /**
     * 根据结算中心查询还款在途
     * @param paramVO
     * @return
     */
    public StringBuilder dailyRepayAwayMoneyListByNetId(LoanDailyParamVO paramVO) {
        StringBuilder sql=new StringBuilder();
        if (!StringUtils.isBlank(paramVO.getLoanSign())) {
            String loanSign = paramVO.getLoanSign();
            switch (loanSign) {
                case "01"://查询内部贷款
                    sql.append(queryLoanRepayAwayCommon("cur",paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
                    break;
            }
        } else {
            sql.append(queryLoanRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
            sql.append(" union all ");
            sql.append(queryPreRepayAwayCommon("cur", paramVO.getTypesOfStatistics(),paramVO.getCorps(),CmsConstant.IS_TRUE));
        }
        return sql;
    }
    /**
     * 设置参数
     */
    public void  setParams(LoanDailyParamVO paramVO,Map<String,Object> params){
        String endDate=paramVO.getEndDate();
        if(!StringUtils.isBlank(paramVO.getEndDate())){
            params.put("curEndDate",DateUtils.str2Date(endDate,DateUtils.date_sdf()));
        }
        if(!StringUtils.isBlank(paramVO.getStartDate())){
            params.put("searchStartDate",DateUtils.str2Date(paramVO.getStartDate(),DateUtils.date_sdf()));
        }

        if(!StringUtils.isBlank(paramVO.getBankType())){
            params.put("bankType",paramVO.getBankType());
        }
        params.put("curId",paramVO.getCurId());
    }
    /**
     * 根据期限查询贷款余额(统计维度为单位)
     * @return
     */
    public StringBuilder queryLoanByPeriod(String dataFlag,List<String> corpIds,String isContain,String loanSign){
        StringBuilder sql=new StringBuilder();
        sql.append(" select  loanSum.corpId,loanSum.curId,")
                .append(" BT_NVL_NUM(sum(loanSum.stayMoney), 0) "+dataFlag+"LoanMoney ")
                .append(" from( ");
        if(!StringUtils.isBlank(loanSign)){
            switch (loanSign) {
                case "01"://查询内部贷款
                    sql.append(queryLoanSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));
                    break;
                case "02"://查询银行借款
                    sql.append(queryProvideSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));
                    break;
                case "03"://查询委托贷款
                    sql.append(queryPreSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));
                    break;
            }
        }else{
            sql.append(queryLoanSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));//不查明细
            sql.append(" union all ");
            sql.append(queryProvideSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));
            sql.append(" union all ");
            sql.append(queryPreSqlCommon(dataFlag,CmsConstant.IS_FALSE,isContain));
        }
        sql.append("  ) loanSum ");
        if(EmptyUtils.isNotEmpty(corpIds)){
            sql.append(" where 1=1 and ").append(getSQLIn(corpIds,900,"loanSum.corpId"));
        }
        sql.append("  group by loanSum.corpId,loanSum.curId ");
        return sql;
    }

    /**
     * 根据期限查询贷款余额(统计维度为贷款机构)
     * @return
     */
    public StringBuilder queryLoanByPeriod2(String dataFlag,String netFlag,List<String> corpIds,String isContain,String loanSign){
        StringBuilder sql=new StringBuilder();
        sql.append(" select loanSum.bankType, ")
                .append(" loanSum.curId, ")
                .append(" BT_NVL_NUM(sum(loanSum.stayMoney), 0) "+dataFlag+"LoanMoney ")
                .append(" from( ");
        if(!StringUtils.isBlank(loanSign)){
            if(loanSign.equals(CmsConstant.LOAN_SIGN_03)){//委托贷款不分是结算中心还是银行类型都要查询
                sql.append(queryPreSqlCommon2(dataFlag,netFlag,corpIds,isContain));
            }else{
                if(netFlag!=null&&netFlag.equals("1")) {//以结算中心作为贷款机构,需要查询内部贷款的数据
                    if(loanSign.equals(CmsConstant.LOAN_SIGN_01)){
                        sql.append(queryLoanSqlCommon2(dataFlag,corpIds,isContain));
                    }
                }else{
                    if(loanSign.equals(CmsConstant.LOAN_SIGN_02)){//查询银行借款
                        sql.append(queryProvideSqlCommon2(dataFlag,CmsConstant.IS_FALSE,corpIds,isContain));
                    }
                }
            }
        }else{
            if(netFlag!=null&&netFlag.equals("1")){//以结算中心作为贷款机构,需要查询内部贷款及委托方非银行的数据
                sql.append(queryLoanSqlCommon2(dataFlag,corpIds,isContain));
                sql.append(" union all ");
            }else{//以银行作为贷款机构，查询银行借款以及委托方为银行的委托贷款
                sql.append(queryProvideSqlCommon2(dataFlag,CmsConstant.IS_FALSE,corpIds,isContain));
                sql.append(" union all ");
            }
            sql.append(queryPreSqlCommon2(dataFlag,netFlag,corpIds,isContain));
        }
        sql.append("  ) loanSum ")
                .append("  group by loanSum.bankType,loanSum.curId");
        return sql;
    }

    /**
     * 按单位时内部贷款公共查询语句
     */
    public StringBuilder queryLoanSqlCommon(String dataFlag,String dtlFlag,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '01' as loan_sign,loan.corpId, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney  ");
        sql.append(" from ( ")
                .append(" select send.corpId, ")
                .append(" send.curId, ")
                .append(" send.sendMoney, ")
                .append(" repay.repayMoney as repayMoney, ");
        if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
        } else {//包含在途，余额=放款金额-已还款金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
        }
        sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                .append(" from ( ")
                .append(querySendMoney(CmsConstant.LOAN_SIGN_01,dataFlag,null))//发放额度
                .append(" ) send ")
                .append("  left join  ")
                .append(queryRepayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,null))//已还款
                .append("  on send.corpId = repay.corpId ")
                .append("  and send.curId = repay.curId ")
                .append("  left join ")
                .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,null))//还款在途
                .append("  on send.corpId = repayAway.corpId ")
                .append("  and send.curId = repayAway.curId ")
                .append(" ) loan  ")
                .append(" group by loan.corpId, loan.curId ");
        return sql;
    }

    /**
     * 按贷款机构统计时内部贷款公共查询语句
     */
    public StringBuilder queryLoanSqlCommon2(String dataFlag,List<String> corpIds,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '01' as loan_sign,loan.bankType, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney ");
        sql.append("  from (  ")
                .append(" select send.*, ")
                .append(" repay.repayMoney as repayMoney, ");
        if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
        } else {//包含在途，余额=放款金额-已还款金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
        }
        sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                .append(" from ( ")
                .append(querySendMoney(CmsConstant.LOAN_SIGN_01,dataFlag,corpIds,null,null))//发放额度
                .append("  ) send ")
                .append(" left join ")
                .append(queryRepayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,corpIds,null,null))//已还款额度
                .append(" on send.bankType = repay.bankType ")
                .append(" and send.curId = repay.curId ")
                .append(" left join ")
                .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,corpIds,null,null))//还款在途
                .append(" on send.bankType = repayAway.bankType ")
                .append(" and send.curId = repayAway.curId ");
        sql.append(" ) loan  ");
        sql.append(" group by loan.bankType, loan.curId ");
        return sql;
    }

    /**
     * 按单位查询时银行借款公共查询语句
     */
    public StringBuilder queryProvideSqlCommon(String dataFlag,String dtlFlag,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '02' as loan_sign,loan.corpId, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney ");
        sql.append(" from ( ")
                .append(" select send.corpId, ")
                .append(" send.curId, ")
                .append(" send.sendMoney, ")
                .append(" repay.repayMoney as repayMoney, ");
        if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
        } else {//包含在途，余额=放款金额-已还款金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
        }
        sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                .append(" from ( ")
                .append(querySendMoney(CmsConstant.LOAN_SIGN_02,dataFlag,null))//发放额度
                .append(" ) send  ")
                .append("  left join  ")
                .append(queryRepayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,null))//已还款
                .append("  on send.corpId = repay.corpId ")
                .append("  and send.curId = repay.curId ")
                .append("  left join ")
                .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,null))//还款在途
                .append("  on send.corpId = repayAway.corpId ")
                .append("  and send.curId = repayAway.curId ")
                .append("  ) loan ")
                .append(" group by loan.corpId, loan.curId ");
        return sql;
    }

    /**
     * 按贷款机构查询时银行借款公共查询语句(根据银行类型分类)
     */
    public StringBuilder queryProvideSqlCommon2(String dataFlag,String dtlFlag,List<String> corpIds,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '02' as loan_sign,loan.bankType, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney  ");
        sql.append(" from ( ")
                .append(" select bt.id  as bankType, ")
                .append(" send.curId, ")
                .append(" send.sendMoney as sendMoney, ")
                .append(" repay.repayMoney as repayMoney, ");
        if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
        } else {//包含在途，余额=放款金额-已还款金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
        }
        sql.append("  repayAway.repayAwayMoney as repayAwayMoney ")
                .append(" from ( ")
                .append(querySendMoney(CmsConstant.LOAN_SIGN_02,dataFlag,corpIds,null,null))//发放额度
                .append(" ) send ")
                .append(" left join  ")
                .append(queryRepayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,corpIds,null,null))//已还款
                .append(" on send.bankType = repay.bankType ")
                .append(" and send.curId = repay.curId ")
                .append(" left join  ")
                .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,corpIds,null,null))//还款在途
                .append(" on send.bankType = repayAway.bankType ")
                .append(" and send.curId = repayAway.curId ")
                .append(" inner join bt_bank bank ")
                .append(" on send.bankType = bank.sys_bank_code ")
                .append(" inner join bt_bank_type bt ")
                .append(" on bank.bank_prefix = bt.bank_prefix ")
                .append("  ) loan ")
                .append(" group by loan.bankType, loan.curId ");
        return sql;
    }

    /**
     * 按单位查询时委托贷款公共查询语句
     */
    public StringBuilder queryPreSqlCommon(String dataFlag,String dtlFlag,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '03' as loan_sign,loan.corpId, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney  ");
        sql.append(" from ( ")
                .append(" select send.corpId, ")
                .append(" send.curId, ")
                .append(" send.sendMoney, ")
                .append(" repay.repayMoney as repayMoney, ");
        if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
        } else {//包含在途，余额=放款金额-已还款金额
            sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
        }
        sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                .append(" from ( ")
                .append(querySendMoney(CmsConstant.LOAN_SIGN_03,dataFlag,null))//发放额度
                .append(" ) send ")
                .append("  left join  ")
                .append(queryRepayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,null))//已还款
                .append("  on send.corpId = repay.corpId ")
                .append("  and send.curId = repay.curId ")
                .append("  left join ")
                .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,null))//还款在途
                .append("  on send.corpId = repayAway.corpId ")
                .append("  and send.curId = repayAway.curId ")
                .append("  ) loan ")
                .append(" group by loan.corpId, loan.curId ");
        return sql;
    }

    /**
     * 按贷款机构查询时委托贷款公共查询语句(委托贷款委托贷款受托人是银行时取银行类型，非银行时取结算中心)
     */
    public StringBuilder queryPreSqlCommon2(String dataFlag,String netFlag,List<String> corpIds,String isContain){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '03' as loan_sign,loan.bankType, loan.curId,BT_NVL_NUM(sum(loan.stayMoney), 0) stayMoney ");
        sql.append(" from ( ");
                if(netFlag!=null&&netFlag.equals("1")){//取结算中心
                     sql.append(" select send.*, ")
                             .append(" repay.repayMoney as repayMoney, ");
                    if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
                        sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
                    } else {//包含在途，余额=放款金额-已还款金额
                        sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
                    }
                    sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                            .append(" from( ")
                            .append(querySendMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_TRUE,null))
                            .append(" ) send ")
                            .append(" left join  ")
                            .append(queryRepayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_TRUE,null))
                            .append(" on send.bankType = repay.bankType ")
                            .append(" and send.curId = repay.curId ")
                            .append(" left join  ")
                            .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_TRUE,null))
                            .append(" on send.bankType = repayAway.bankType ")
                            .append(" and send.curId = repayAway.curId ");
                }else{//取银行类型
                    sql.append(" select bt.id  as bankType, ")
                            .append(" send.curId, ")
                            .append(" send.sendMoney as sendMoney, ")
                            .append(" repay.repayMoney as repayMoney, ");
                    if (null!=isContain&&isContain.equals(CmsConstant.IS_FALSE)) {//不包含在途，余额=放款金额-已还款金额-还款在途金额
                        sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0) - BT_NVL_NUM(repayAway.repayAwayMoney, 0)) as stayMoney, ");
                    } else {//包含在途，余额=放款金额-已还款金额
                        sql.append(" (send.sendMoney - BT_NVL_NUM(repay.repayMoney, 0)) as stayMoney, ");
                    }
                    sql.append(" repayAway.repayAwayMoney as repayAwayMoney ")
                            .append(" from ( ")
                            .append(querySendMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_FALSE,null))
                            .append(" ) send ")
                            .append(" left join  ")
                            .append(queryRepayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_FALSE,null))
                            .append(" on send.bankType = repay.bankType ")
                            .append(" and send.curId = repay.curId ")
                            .append(" left join  ")
                            .append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,CmsConstant.IS_FALSE,null))
                            .append(" on send.bankType = repayAway.bankType ")
                            .append(" and send.curId = repayAway.curId ")
                            .append(" inner join bt_bank bank ")
                            .append(" on send.bankType = bank.sys_bank_code ")
                            .append(" inner join bt_bank_type bt ")
                            .append(" on bank.bank_prefix = bt.bank_prefix ");
                }
                sql.append("  ) loan ")
                .append(" group by loan.bankType, loan.curId ");
        return sql;
    }

    /**
     * 按单位时内部贷款公共查询语句
     */
    public StringBuilder queryLoanSendSqlCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)){//根据单位查询
            sql.append(" select '01' as loanSign,loanSend.corpId,loanSend.curId,loanSend.sendDate as balanceDate,loanSend.sendMoney as dailyBalance ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_01,dataFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) loanSend ");
        }else{
            sql.append(" select '01' as loanSign,loanSend.bankType,loanSend.curId,loanSend.sendDate as balanceDate,loanSend.sendMoney as dailyBalance ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_01,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) loanSend ");
        }
        return sql;
    }
    public StringBuilder queryProSendSqlCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '02' as loanSign,proSend.corpId,proSend.curId,proSend.sendDate as balanceDate,proSend.sendMoney as dailyBalance  ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_02,dataFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) proSend ");
        }else{
            sql.append(" select '02' as loanSign,bt.id as bankType,proSend.curId,proSend.sendDate as balanceDate,proSend.sendMoney as dailyBalance  ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_02,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) proSend ");
            sql.append(" inner join bt_bank bank ")
                    .append(" on proSend.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
        }

        return sql;
    }
    public StringBuilder queryPreSendSqlCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '03' as loanSign,preSend.corpId,preSend.curId,preSend.sendDate as balanceDate,preSend.sendMoney as dailyBalance  ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_03,dataFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) preSend ");
        }else{
            sql.append(" select '03' as loanSign,bt.id as bankType,preSend.curId,preSend.sendDate as balanceDate,preSend.sendMoney as dailyBalance  ");
            sql.append(" from ( ")
                    .append(querySendMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE))//发放额度
                    .append(" ) preSend ");
            sql.append(" inner join bt_bank bank ")
                    .append(" on preSend.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
        }

        return sql;
    }

    public StringBuilder queryLoanRepayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '01' as loanSign,repay.*  from  ");
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,CmsConstant.IS_TRUE));//已还款
        }else{
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_01,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE));//已还款
        }

        return sql;
    }

    public StringBuilder queryProRepayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '02' as loanSign,repay.*  from");
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,CmsConstant.IS_TRUE));//已还款
        }else{
            sql.append(" select '02' as loanSign,repay.repayDate,bt.id as bankType,repay.curId,repay.repayMoney  from  ");
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_02,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE));//已还款
            sql.append(" inner join bt_bank bank ")
                    .append(" on repay.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
        }
        return sql;
    }

    public StringBuilder queryPreRepayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if(typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '03' as loanSign,repay.* from  ");
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,CmsConstant.IS_TRUE));//已还款
        }else{
            sql.append(" select '03' as loanSign,repay.repayDate,bt.id as bankType,repay.curId,repay.repayMoney from ");
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_03,dataFlag,corpIds,netFlag,CmsConstant.IS_TRUE));//已还款
            sql.append(" inner join bt_bank bank ")
                    .append(" on repay.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");

        }

        return sql;
    }

    public StringBuilder queryLoanRepayAwayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        sql.append(" select '01' as loanSign,repayAway.* ")
                .append(" from ");
        if (typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_01, dataFlag,CmsConstant.IS_TRUE));//还款在途
        } else {
            sql.append(queryRepayMoney(CmsConstant.LOAN_SIGN_01, dataFlag, corpIds, netFlag,CmsConstant.IS_TRUE));//还款在途
        }

        return sql;
    }

    public StringBuilder queryProRepayAwayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if (typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '02' as loanSign,repayAway.* from  ");
            sql.append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_02, dataFlag,CmsConstant.IS_TRUE));//还款在途
        } else {
            sql.append(" select '02' as loanSign,repayAway.repayAwayDate,bt.id as bankType,repayAway.curId,repay.repayAwayMoney  from ");
            sql.append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_02, dataFlag, corpIds, netFlag,CmsConstant.IS_TRUE));//还款在途
            sql.append(" inner join bt_bank bank ")
                    .append(" on repayAway.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
        }

        return sql;
    }

    public StringBuilder queryPreRepayAwayCommon(String dataFlag,String typesOfStatistics,List<String> corpIds,String netFlag){
        StringBuilder sql=new StringBuilder();
        if (typesOfStatistics.equals(CmsConstant.SORT_TYPE_0)) {//根据单位查询
            sql.append(" select '03' as loanSign,repayAway.* from ");
            sql.append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_03, dataFlag,CmsConstant.IS_TRUE));//还款在途
        } else {
            sql.append(" select '03' as loanSign,repayAway.repayAwayDate,bt.id as bankType,repayAway.curId,repay.repayAwayMoney  from ");
            sql.append(queryRepayAwayMoney(CmsConstant.LOAN_SIGN_03, dataFlag, corpIds, netFlag,CmsConstant.IS_TRUE));//还款在途
            sql.append(" inner join bt_bank bank ")
                    .append(" on repayAway.bankType = bank.sys_bank_code ")
                    .append(" inner join bt_bank_type bt ")
                    .append(" on bank.bank_prefix = bt.bank_prefix ");
        }
        return sql;
    }

    /**
     * 查询贷款发放额度(按单位查询)
     */
    public StringBuilder querySendMoney(String loanSign,String dataFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   send.sendDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                        sql.append(" a.corp_id as corpId,a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(send.loan_money), 0) sendMoney ")
                        .append(" from (select s.loan_money, s.loan_code,s.loan_date as sendDate ")
                        .append(" from cms_loan_send s ")
                        .append(" where s.status in (102, 103, 104, 110) ")
                        .append(" and s.loan_date <=:" + dataFlag + "EndDate ")
                        .append(" ) send ")
                        .append(" left join cms_loan_info a ")
                        .append(" on send.loan_code = a.bill_code ")
                        .append(" group by a.corp_id, a.cur_code ");
                break;
            case "02":
                sql.append(" a.corp_id as corpId,a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(send.money), 0) sendMoney ")
                        .append(" from (select s.money, s.loan_code,s.send_date as sendDate")
                        .append(" from cms_provide_loan_send s ")
                        .append(" where s.status in (102, 103, 104, 110) ")
                        .append(" and s.send_date <=:" + dataFlag + "EndDate ")
                        .append(" ) send ")
                        .append(" left join cms_provide_loan_info a ")
                        .append(" on send.loan_code = a.bill_code ")
                        .append(" group by a.corp_id, a.cur_id ");
                break;
            case "03":
                sql.append(" a.loan_opp as corpId,a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(send.money), 0) sendMoney ")
                        .append(" from (select s.money, s.father_code, s.send_date as sendDate")
                        .append(" from cms_precative_loan_send s ")
                        .append(" where s.status in (102, 103, 104, 110) ")
                        .append(" and s.send_date <=:" + dataFlag + "EndDate ")
                        .append(" ) send ")
                        .append(" left join cms_precative_loan_info a ")
                        .append(" on send.father_code = a.bill_code ")
                        .append(" group by a.loan_opp, a.cur_id ");
                break;
        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" ,send.sendDate ");
        }
        return sql;
    }

    /**
     * 查询贷款发放额度(按贷款机构查询)
     */
    public StringBuilder querySendMoney(String loanSign,String dataFlag,List<String> corpIds,String netFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   send.sendDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                sql.append(" a.net_code as bankType, a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(send.loan_money), 0) sendMoney ")
                        .append(" from (select s.loan_money, s.loan_code, s.loan_date as sendDate")
                        .append(" from cms_loan_send s ")
                        .append(" where s.status in (102, 103, 104, 110) ")
                        .append(" and s.loan_date <=:" + dataFlag + "EndDate ")
                        .append(" ) send ")
                        .append(" left join cms_loan_info a ")
                        .append(" on send.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by a.net_code, a.cur_code ");
                break;
            case "02":
                sql.append(" a.bank_id as bankType,a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(send.money), 0) sendMoney ")
                        .append(" from (select s.money, s.loan_code, s.send_date as sendDate")
                        .append(" from cms_provide_loan_send s ")
                        .append(" where s.status in (102, 103, 104, 110) ")
                        .append(" and s.send_date <=:" + dataFlag + "EndDate ")
                        .append(" ) send ")
                        .append(" left join cms_provide_loan_info a ")
                        .append(" on send.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by a.bank_id, a.cur_id ");
                break;
            case "03":
                if(netFlag!=null&&netFlag.equals(CmsConstant.IS_TRUE)) {//取结算中心
                    sql.append(" a.corp_id as bankType,a.cur_id as curId, ")
                            .append(" BT_NVL_NUM(sum(send.money), 0) sendMoney ")
                            .append(" from (select s.money, s.father_code, s.send_date as sendDate")
                            .append(" from cms_precative_loan_send s ")
                            .append(" where s.status in (102, 103, 104, 110) ")
                            .append(" and s.send_date <=:" + dataFlag + "EndDate ")
                            .append(" ) send ")
                            .append(" left join cms_precative_loan_info a ")
                            .append(" on send.father_code = a.bill_code ")
                            .append(" and a.is_bank = 0 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append(" group by a.corp_id, a.cur_id ");
                }else{
                    sql.append(" a.bank_code as bankType,a.cur_id as curId, ")
                            .append(" BT_NVL_NUM(sum(send.money), 0) sendMoney ")
                            .append(" from (select s.money, s.father_code, s.send_date as sendDate ")
                            .append(" from cms_precative_loan_send s ")
                            .append(" where s.status in (102, 103, 104, 110) ")
                            .append(" and s.send_date <=:" + dataFlag + "EndDate ")
                            .append("  ) send ")
                            .append(" left join cms_precative_loan_info a ")
                            .append(" on send.father_code = a.bill_code ")
                            .append(" and a.is_bank = 1 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append(" group by a.bank_code, a.cur_id ");
                }
                break;
        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" ,send.sendDate ");
        }
        return sql;
    }

    /**
     * 查询贷款已还款额度(按单位查询)
     */
    public StringBuilder queryRepayMoney(String loanSign,String dataFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append("( ");
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   repayLoan.repay_date as repayDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                sql.append("  a.corp_id as corpId, ")
                        .append(" a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_loan_repay clr ")
                        .append(" where clr.status >= 102 ")
                        .append(" and clr.status <> 120 ")
                        .append(" and clr.repay_date<=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayLoan ")
                        .append(" left join cms_loan_send s ")
                        .append(" on s.bill_code = repayLoan.send_code ")
                        .append(" left join cms_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ")
                        .append(" group by a.corp_id, a.cur_code ");
                break;
            case "02":
                sql.append("  a.corp_id as corpId, ")
                        .append("  a.cur_id as curId, ")
                        .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                        .append("  from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                        .append("  clr.send_code, ")
                        .append("  clr.repay_date ")
                        .append("  from cms_provide_loan_repay clr ")
                        .append("  where clr.status >= 102 ")
                        .append("  and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append("  group by clr.send_code, clr.repay_date) repayLoan ")
                        .append("  left join cms_provide_loan_send s ")
                        .append("  on s.bill_code = repayLoan.send_code ")
                        .append("  left join cms_provide_loan_info a ")
                        .append("  on s.loan_code = a.bill_code ")
                        .append("  group by a.corp_id, a.cur_id ");
                break;
            case "03":
                sql.append("  a.loan_opp as corpId, ")
                        .append(" a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_precative_loan_repay clr ")
                        .append(" where clr.status >= 102 ")
                        .append(" and clr.status <> 120 ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayLoan ")
                        .append(" left join cms_precative_loan_send s ")
                        .append(" on s.bill_code = repayLoan.send_code ")
                        .append(" left join cms_precative_loan_info a ")
                        .append(" on s.father_code = a.bill_code ")
                        .append(" group by a.loan_opp, a.cur_id ");
                break;
        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" ,repayLoan.repay_date ");
        }
        sql.append(" ) repay ");
        return sql;
    }

    /**
     * 查询贷款已还款额度(按贷款机构查询)
     */
    public StringBuilder queryRepayMoney(String loanSign,String dataFlag,List<String> corpIds,String netFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append("( ");
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   repayLoan.repay_date as repayDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                sql.append("  a.net_code as bankType, ")
                        .append(" a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_loan_repay clr ")
                        .append(" where clr.status >= 102 ")
                        .append(" and clr.status <> 120 ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code,clr.repay_date) repayLoan ")
                        .append(" left join cms_loan_send s ")
                        .append(" on s.bill_code = repayLoan.send_code ")
                        .append(" left join cms_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by  a.net_code, a.cur_code ");
                break;
            case "02":
                sql.append("  a.bank_id as bankType, ")
                        .append("  a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_provide_loan_repay clr ")
                        .append(" where clr.status >= 102 ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayLoan ")
                        .append(" left join cms_provide_loan_send s ")
                        .append(" on s.bill_code = repayLoan.send_code ")
                        .append(" left join cms_provide_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by a.bank_id, a.cur_id ");
                break;
            case "03":
                if(netFlag!=null&&netFlag.equals("1")) {//取结算中心
                    sql.append("  a.corp_id as bankType, ")
                            .append("  a.cur_id as curId, ")
                            .append("  BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                            .append("  from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                            .append("  clr.send_code, ")
                            .append("  clr.repay_date ")
                            .append("  from cms_precative_loan_repay clr ")
                            .append("  where clr.status >= 102 ")
                            .append("  and clr.repay_date <=:" + dataFlag + "EndDate ")
                            .append("  group by clr.send_code, clr.repay_date) repayLoan ")
                            .append("  left join cms_precative_loan_send s ")
                            .append("  on s.bill_code = repayLoan.send_code ")
                            .append("  left join cms_precative_loan_info a ")
                            .append("  on s.father_code = a.bill_code ")
                            .append("  and a.is_bank = 0 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append("  group by a.corp_id, a.cur_id ");
                }else{
                    sql.append("  a.bank_code as bankType, ")
                            .append(" a.cur_id as curId, ")
                            .append(" BT_NVL_NUM(sum(repayLoan.repayMoney), 0) repayMoney ")
                            .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayMoney, ")
                            .append(" clr.send_code, ")
                            .append(" clr.repay_date ")
                            .append(" from cms_precative_loan_repay clr ")
                            .append(" where clr.status >= 102 ")
                            .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                            .append(" group by clr.send_code, clr.repay_date) repayLoan ")
                            .append(" left join cms_precative_loan_send s ")
                            .append(" on s.bill_code = repayLoan.send_code ")
                            .append(" left join cms_precative_loan_info a ")
                            .append(" on s.father_code = a.bill_code ")
                            .append(" and a.is_bank = 1 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append(" group by a.bank_code, a.cur_id ");
                }
                break;
        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" ,repayLoan.repay_date ");
        }
        sql.append(" ) repay ");
        return sql;
    }
    /**
     * 查询还款在途的额度(按单位查询)
     * @param loanSign
     * @param dataFlag
     * @return
     */
    public StringBuilder queryRepayAwayMoney(String loanSign,String dataFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append("(");
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   repayAwayLoan.repay_date as repayAwayDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                sql.append("  a.corp_id as corpId, ")
                        .append(" a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_loan_repay clr ")
                        .append(" where clr.status != -2 ")
                        .append(" and (clr.status < 102 or clr.status = 120) ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                        .append(" left join cms_loan_send s ")
                        .append(" on s.bill_code = repayAwayLoan.send_code ")
                        .append(" left join cms_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ")
                        .append(" group by a.corp_id, a.cur_code ");
                break;
            case "02":
                sql.append("  a.corp_id as corpId, ")
                        .append(" a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_provide_loan_repay clr ")
                        .append(" where clr.status >= -1 ")
                        .append(" and clr.status < 102 ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                        .append(" left join cms_provide_loan_send s ")
                        .append(" on s.bill_code = repayAwayLoan.send_code ")
                        .append(" left join cms_provide_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ")
                        .append(" group by a.corp_id, a.cur_id ");
                break;
            case"03":
                sql.append("  a.loan_opp as corpId, ")
                        .append(" a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_precative_loan_repay clr ")
                        .append(" where clr.status != -2 ")
                        .append(" and (clr.status < 102 or clr.status = 120) ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                        .append(" left join cms_precative_loan_send s ")
                        .append(" on s.bill_code = repayAwayLoan.send_code ")
                        .append(" left join cms_precative_loan_info a ")
                        .append(" on s.father_code = a.bill_code ")
                        .append(" group by a.loan_opp, a.cur_id ");
                break;

        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" , repayAwayLoan.repay_date ");
        }
        sql.append("  ) repayAway ");
        return sql;
    }

    /**
     * 查询还款在途的额度(按贷款机构查询)
     * @param loanSign
     * @param dataFlag
     * @return
     */
    public StringBuilder queryRepayAwayMoney(String loanSign,String dataFlag,List<String> corpIds,String netFlag,String dtlFlag){
        StringBuilder sql=new StringBuilder();
        sql.append("( ");
        sql.append(" select ");
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append("   repayAwayLoan.repay_date as repayAwayDate,  ");
        }
        switch (loanSign) {
            case "01"://内部贷款
                sql.append("   a.net_code as bankType, ")
                        .append(" a.cur_code as curId, ")
                        .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_loan_repay clr ")
                        .append(" where clr.status != -2 ")
                        .append(" and (clr.status < 102 or clr.status = 120) ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code,clr.repay_date) repayAwayLoan ")
                        .append(" left join cms_loan_send s ")
                        .append(" on s.bill_code = repayAwayLoan.send_code ")
                        .append(" left join cms_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by  a.net_code, a.cur_code ");
                break;
            case "02":
                sql.append("  a.bank_id as bankType, ")
                        .append(" a.cur_id as curId, ")
                        .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                        .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                        .append(" clr.send_code, ")
                        .append(" clr.repay_date ")
                        .append(" from cms_provide_loan_repay clr ")
                        .append(" where clr.status >= -1 ")
                        .append(" and clr.status < 102 ")
                        .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                        .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                        .append(" left join cms_provide_loan_send s ")
                        .append(" on s.bill_code = repayAwayLoan.send_code ")
                        .append(" left join cms_provide_loan_info a ")
                        .append(" on s.loan_code = a.bill_code ");
                if (EmptyUtils.isNotEmpty(corpIds)) {
                    sql.append(" where ").append(getSQLIn(corpIds, 900, "a.CORP_ID"));
                }
                        sql.append(" group by a.bank_id, a.cur_id ");
                break;
            case"03":
                if(netFlag!=null&&netFlag.equals("1")) {//取结算中心
                    sql.append("  a.corp_id as bankType, ")
                            .append(" a.cur_id as curId, ")
                            .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                            .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                            .append(" clr.send_code, ")
                            .append(" clr.repay_date ")
                            .append(" from cms_precative_loan_repay clr ")
                            .append(" where clr.status >= -1 ")
                            .append(" and clr.status < 102 ")
                            .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                            .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                            .append(" left join cms_precative_loan_send s ")
                            .append(" on s.bill_code = repayAwayLoan.send_code ")
                            .append(" left join cms_precative_loan_info a ")
                            .append(" on s.father_code = a.bill_code ")
                            .append(" and a.is_bank = 0 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append(" group by a.corp_id, a.cur_id ");
                }else{
                    sql.append("  a.bank_code as bankType, ")
                            .append(" a.cur_id as curId, ")
                            .append(" BT_NVL_NUM(sum(repayAwayLoan.repayAwayMoney), 0) repayAwayMoney ")
                            .append(" from (select BT_NVL_NUM(sum(repay_money), 0) repayAwayMoney, ")
                            .append(" clr.send_code, ")
                            .append(" clr.repay_date ")
                            .append(" from cms_precative_loan_repay clr ")
                            .append(" where clr.status >= -1 ")
                            .append(" and clr.status < 102 ")
                            .append(" and clr.repay_date <=:" + dataFlag + "EndDate ")
                            .append(" group by clr.send_code, clr.repay_date) repayAwayLoan ")
                            .append(" left join cms_precative_loan_send s ")
                            .append(" on s.bill_code = repayAwayLoan.send_code ")
                            .append(" left join cms_precative_loan_info a ")
                            .append(" on s.father_code = a.bill_code ")
                            .append(" and a.is_bank = 1 ");
                    if (EmptyUtils.isNotEmpty(corpIds)) {
                        sql.append(" where ").append(getSQLIn(corpIds, 900, "a.LOAN_OPP"));
                    }
                            sql.append(" group by a.bank_code, a.cur_id ");
                }
                break;
        }
        if(null!=dtlFlag&&dtlFlag.equals(CmsConstant.IS_TRUE)){
            sql.append(" , repayAwayLoan.repay_date ");
        }
        sql.append("  ) repayAway ");
        return sql;
    }
}
