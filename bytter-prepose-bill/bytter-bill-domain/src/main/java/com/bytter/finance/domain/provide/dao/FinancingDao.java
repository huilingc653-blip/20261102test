package com.bytter.finance.domain.provide.dao;

import cn.hutool.core.util.ObjectUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FinancingDao extends BaseDao {

    /**
     * 获取贷款余额之和
     * @param corpId   单位
     * @param bankCodes  机构对应的银行
     * @param endDate    结束时间
     * @return
     */
    public Map<String,BigDecimal> getSumProvideBal(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date endDate){
        Map<String,BigDecimal> resultMap=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sum(SEND_BALANCE_MONEY) as sumbal,");
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" bank.sys_bank_code as bankkey");
        }else {
            sql.append(" bankType.id as bankkey");
        }
        sql.append(" from cms_provide_history  his");
        sql.append(" left join bt_bank bank on bank.bank_name = his.BANK_NAME");
        sql.append(" left join bt_bank_type bankType on bank.bank_prefix=bankType.bank_prefix");
        sql.append(" left join bt_currency cur on cur.id=his.CUR_ID");
        sql.append(" where his.REPORT_DATE=:endDate");
        params.put("endDate",endDate);
        sql.append(" and cur.cur_name='人民币'");
        if(!EmptyUtils.isEmpty(corpId)){
            sql.append(" and his.corp_Id=:corpId");
            params.put("corpId",corpId);
        }
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" and").append(getSQLIn(bankCodes, 900, "bank.sys_bank_code"));
            sql.append(" group by bank.sys_bank_code");
        }else {
            sql.append(" and").append(getSQLIn(bankTypeIds, 900, "bankType.id"));
            sql.append(" group by bankType.id");
        }
        List<Map<String,Object>> list = this.listMapBySQL(sql.toString(), params);
        if (ObjectUtil.isNotEmpty(list)){
            for (Map<String,Object> map : list){
                if (ObjectUtil.isNotEmpty(map.get("bankkey")) && ObjectUtil.isNotEmpty(map.get("sumbal"))){
                    resultMap.put( map.get("bankkey").toString(), new BigDecimal(map.get("sumbal").toString()));
                }
            }
        }
        return resultMap;
    }
//    public Map<String,BigDecimal> getSumProvideBal(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date endDate){
//        Map<String,BigDecimal> resultMap=new HashMap<>();
//        StringBuilder sql = new StringBuilder();
//        Map<String, Object> params = new HashMap<String, Object>();
//        sql.append(" select sum(send.money-bt_nvl_num(repay.sum_repay_money,0)) as sumbal,");
//        if(!EmptyUtils.isEmpty(bankCodes)){
//            sql.append(" bank.sys_bank_code as bankkey");
//        }else {
//            sql.append(" bank.BANK_TYPE_ID as bankkey");
//        }
//        sql.append(" from cms_provide_loan_send  send");
//        sql.append(" left join cms_provide_loan_info info on send.loan_code = info.bill_code");
//        sql.append(" left join (select send_code,sum(repay_money) as sum_repay_money,sum(ONLINE_REALITY_INTEREST) as sum_repay_interest ");
//        sql.append(" from cms_provide_loan_repay where status >= 102 and REPAY_DATE<=:endDate  group by send_code) repay on repay.send_code = send.bill_code");
//        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
//        sql.append(" left join bt_currency cur on cur.id=send.CUR_CODE");
////        sql.append(" left join cms_provide_loan_abolish abolish on abolish.pli_code = info.bill_code and abolish.status > 0 and abolish.status < 110");
//        sql.append(" where send.status>=102 and info.status>=95");
//        sql.append(" and send.SEND_DATE<:endDate");
//        params.put("endDate",endDate);
//        sql.append(" and cur.cur_name='人民币'");
//        if(!EmptyUtils.isEmpty(corpId)){
//            sql.append(" and info.corp_Id=:corpId");
//            params.put("corpId",corpId);
//        }
//        if(!EmptyUtils.isEmpty(bankCodes)){
//            sql.append(" and").append(getSQLIn(bankCodes, 900, "bank.sys_bank_code"));
//            sql.append(" group by bank.sys_bank_code");
//        }else {
//            sql.append(" and").append(getSQLIn(bankTypeIds, 900, "bank.BANK_TYPE_ID"));
//            sql.append(" group by bank.BANK_TYPE_ID");
//        }
//        List<Map<String,Object>> list = this.listMapBySQL(sql.toString(), params);
//        if (ObjectUtil.isNotEmpty(list)){
//            for (Map<String,Object> map : list){
//                if (ObjectUtil.isNotEmpty(map.get("bankkey")) && ObjectUtil.isNotEmpty(map.get("sumbal"))){
//                    resultMap.put( map.get("bankkey").toString(), new BigDecimal(map.get("sumbal").toString()));
//                }
//            }
//        }
//        return resultMap;
//    }

    /**
     * 获取一段时间内总行与支行的发放总额
     * @param corpId
     * @param bankCodes
     * @param bankTypeIds
     * @param StartDate
     * @param endDate
     * @return
     */
    public Map<String,BigDecimal> getSumProvideSendMoney(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date StartDate,Date endDate){
        Map<String,BigDecimal> resultMap=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sum(send.money) as sumsendmoney,");
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" bank.sys_bank_code as bankkey");
        }else {
            sql.append(" bankType.id as bankkey");
        }
        sql.append(" from cms_provide_loan_send  send");
        sql.append(" left join cms_provide_loan_info info on send.loan_code = info.bill_code");
        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
        sql.append(" left join bt_bank_type bankType on bank.bank_prefix=bankType.bank_prefix");
        sql.append(" left join bt_currency cur on cur.id=send.CUR_CODE");
        sql.append(" where send.status>=102 and info.status>=95");
        sql.append(" and cur.cur_name='人民币'");
        if(!EmptyUtils.isEmpty(StartDate)) {
            sql.append(" and send.SEND_DATE>=:StartDate");
            params.put("StartDate", StartDate);
        }
        if(!EmptyUtils.isEmpty(endDate)) {
            sql.append(" and send.SEND_DATE<=:endDate");
            params.put("endDate", endDate);
        }
        if(!EmptyUtils.isEmpty(corpId)){
            sql.append(" and info.corp_Id=:corpId");
            params.put("corpId",corpId);
        }
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" and").append(getSQLIn(bankCodes, 900, "bank.sys_bank_code"));
            sql.append(" group by bank.sys_bank_code");
        }else {
            sql.append(" and").append(getSQLIn(bankTypeIds, 900, "bankType.id"));
            sql.append(" group by bankType.id");
        }
        List<Map<String,Object>> list = this.listMapBySQL(sql.toString(), params);
        if (ObjectUtil.isNotEmpty(list)){
            for (Map<String,Object> map : list){
                if (ObjectUtil.isNotEmpty(map.get("bankkey")) && ObjectUtil.isNotEmpty(map.get("sumsendmoney"))){
                    resultMap.put( map.get("bankkey").toString(), new BigDecimal(map.get("sumsendmoney").toString()));
                }
            }
        }
        return resultMap;
    }

    /**
     * 获取一段时间内总行与支行的平均发放利率
     * @param corpId
     * @param bankCodes
     * @param bankTypeIds
     * @param StartDate
     * @param endDate
     * @return
     */
    public Map<String,BigDecimal> getAvgProvideSendRate(String corpId, List<String> bankCodes,List<String> bankTypeIds,Date StartDate,Date endDate){
        Map<String,BigDecimal> resultMap=new HashMap<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select avg(send.PL_RATE) as avgsendrate,");
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" bank.sys_bank_code as bankkey");
        }else {
            sql.append(" bankType.id as bankkey");
        }
        sql.append(" from cms_provide_loan_send  send");
        sql.append(" left join cms_provide_loan_info info on send.loan_code = info.bill_code");
        sql.append(" left join bt_bank bank on bank.sys_bank_code = info.bank_id");
        sql.append(" left join bt_bank_type bankType on bank.bank_prefix=bankType.bank_prefix");
        sql.append(" left join bt_currency cur on cur.id=send.CUR_CODE");
//        sql.append(" left join cms_provide_loan_abolish abolish on abolish.pli_code = info.bill_code and abolish.status > 0 and abolish.status < 110");
        sql.append(" where send.status>=0 and info.status>=95");
        sql.append(" and cur.cur_name='人民币'");
        if(!EmptyUtils.isEmpty(StartDate)) {
            sql.append(" and send.REPAY_DATE>=:StartDate");
            params.put("StartDate", StartDate);
        }
        if(!EmptyUtils.isEmpty(endDate)) {
            sql.append(" and send.SEND_DATE<=:endDate");
            params.put("endDate", endDate);
        }
        if(!EmptyUtils.isEmpty(corpId)){
            sql.append(" and info.corp_Id=:corpId");
            params.put("corpId",corpId);
        }
        if(!EmptyUtils.isEmpty(bankCodes)){
            sql.append(" and").append(getSQLIn(bankCodes, 900, "bank.sys_bank_code"));
            sql.append(" group by bank.sys_bank_code");
        }else {
            sql.append(" and").append(getSQLIn(bankTypeIds, 900, "bankType.id"));
            sql.append(" group by bankType.id");
        }
        List<Map<String,Object>> list = this.listMapBySQL(sql.toString(), params);
        if (ObjectUtil.isNotEmpty(list)){
            for (Map<String,Object> map : list){
                if (ObjectUtil.isNotEmpty(map.get("bankkey")) && ObjectUtil.isNotEmpty(map.get("avgsendrate"))){
                    resultMap.put( map.get("bankkey").toString(), new BigDecimal(map.get("avgsendrate").toString()));
                }
            }
        }
        return resultMap;
    }

    /**
     * 获取机构子啊多个单位的，报表日期的借款余额合计
     * @param corpIds
     * @param reportDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getProvideBalForReportDate(List<String> corpIds, Date reportDate,String bankTypeId,String bankCode){
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(his.SEND_BALANCE_MONEY) money");
        sql.append(" from cms_provide_history his");
        sql.append(" left join bt_bank bank on bank.bank_name = his.BANK_NAME");
        sql.append(" left join bt_bank_type bankType on bank.bank_prefix=bankType.bank_prefix");
        sql.append(" where his.REPORT_DATE=:reportDate ");
        sql.append(" and "+ SqlUtil.getInSql(corpIds, "his.corp_id", true));
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and bankType.id= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and bank.sys_bank_code= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("reportDate",reportDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }
}
