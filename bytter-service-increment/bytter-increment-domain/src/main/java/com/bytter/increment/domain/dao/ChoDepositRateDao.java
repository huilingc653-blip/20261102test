package com.bytter.increment.domain.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.increment.api.dto.ChoDepositRateDTO;
import com.bytter.increment.api.dto.ChoProductReqQueryDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ChoDepositRateDao extends BaseDao {

    /**
     * 查询存款的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoDepositRateDTO> queryDepositList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select f.INTEREST_RATE as rate from cho_bank_finance f left join bt_bank_acc bba on f.payer_Acc_Id = bba.id where f.BUS_TYPE <> 20 ");//查存款中不为协定利率的数据
        if (!EmptyUtils.isBlank(corpId)){
            sql.append(" and f.corp_id = :corpId ");
            map.put("corpId",corpId);
        }
        if (!EmptyUtils.isEmpty(bankCode)){
            sql.append(" and bba.BANK_CODE=:bankCode ");
            map.put("bankCode",bankCode);
        }
        if (!EmptyUtils.isBlank(bankTypeId)){
            sql.append(" and bba.bank_type_id = :bankTypeId ");
            map.put("bankTypeId",bankTypeId);
        }
        if (!EmptyUtils.isBlank(scoringStartDate)){
            sql.append(" and f.deadline_date >=  to_date('" + scoringStartDate + "','yyyy-MM-dd')  ");
        }
        if (!EmptyUtils.isBlank(scoringEndDate)){
            sql.append(" and f.interest_start_date <= to_date('" + scoringEndDate + "','yyyy-MM-dd')  ");
        }
        sql.append(" union all ");
        //协定利率按照平均利率取值
        sql.append(" select a.rate from ( select fd.CHO_BANK_FINANCE_ID ,AVG(fd.AGREE_RATE) as rate from CHO_BANK_FINANCE_DETAIL fd ");
        sql.append(" left join cho_bank_finance f on fd.CHO_BANK_FINANCE_ID = f.ID left join bt_bank_acc bba on f.payer_Acc_Id = bba.id where f.BUS_TYPE = 20 ");
        if (!EmptyUtils.isBlank(corpId)){
            sql.append(" and f.corp_id = :corpId ");
            map.put("corpId",corpId);
        }
        if (!EmptyUtils.isEmpty(bankCode)){
            sql.append(" and bba.BANK_CODE=:bankCode ");
            map.put("bankCode",bankCode);
        }
        if (!EmptyUtils.isBlank(bankTypeId)){
            sql.append(" and bba.bank_type_id = :bankTypeId ");
            map.put("bankTypeId",bankTypeId);
        }
        if (!EmptyUtils.isBlank(scoringStartDate)){
            sql.append(" and f.deadline_date >= to_date('" + scoringStartDate + "','yyyy-MM-dd') ");
        }
        if (!EmptyUtils.isBlank(scoringEndDate)){
            sql.append(" and f.interest_start_date <= to_date('" + scoringEndDate + "','yyyy-MM-dd') ");
        }
        sql.append(" group by fd.CHO_BANK_FINANCE_ID ) a");
        return listBySQLAliasToBean(sql.toString(),map,ChoDepositRateDTO.class);
    }
    /**
     * 查询理财类型为保本理财的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoProductReqQueryDTO> queryChoList(String corpId, String bankCode, String bankTypeId, String scoringStartDate, String scoringEndDate){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        sql.append(" select cpr.id as id,cpr.REFER_INCOME_RATE as referIncomeRate ");
        sql.append(" from cho_product_req cpr left join cho_product_type pt on pt.product_code = cpr.product_code ");
        sql.append(" where pt.FINANCIAL_TYPE in (0,1) ");
        if (!EmptyUtils.isBlank(corpId)){
            sql.append(" and cpr.corp_id = :corpId ");
            map.put("corpId",corpId);
        }
        if (!EmptyUtils.isEmpty(bankCode)){
           sql.append(" and cpr.bank_info_id=:bankCode");
           map.put("bankCode",bankCode);
        }
        if (!EmptyUtils.isEmpty(bankTypeId)){
            sql.append(" and cpr.bank_type_id=:bankTypeId");
            map.put("bankTypeId",bankTypeId);
        }
        if (!EmptyUtils.isBlank(scoringStartDate)){
            sql.append(" and cpr.deadline_date >= to_date('" + scoringStartDate + "','yyyy-MM-dd')  ");
        }
        if (!EmptyUtils.isBlank(scoringEndDate)){
            sql.append(" and cpr.interest_start_date <= to_date('" + scoringEndDate + "','yyyy-MM-dd')");
        }
        return listBySQLAliasToBean(sql.toString(),map, ChoProductReqQueryDTO.class);
    }
    /**
     * 查询理财类型为非保本理财的利率
     * @param corpId
     * @param bankCode
     * @param bankTypeId
     * @param scoringStartDate
     * @param scoringEndDate
     * @return
     */
    public List<ChoDepositRateDTO> queryNotChoList(String corpId,String bankCode,String bankTypeId,String scoringStartDate,String scoringEndDate){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        //查存款中不为协定利率的数据
        sql.append("select  pn.ACTUAL_INCOME/pn.buy_money as rate from cho_product_req cpr left join cho_product_type pt on pt.product_code = cpr.product_code ");
        sql.append(" right JOIN cho_product_netval pn ON pn.req_id = cpr.id ");
        sql.append(" where pt.FINANCIAL_TYPE = 2 ");
        //过滤利率为0的数据
        sql.append(" and  pn.ACTUAL_INCOME<>0");
        if (!EmptyUtils.isBlank(corpId)){
            sql.append(" and cpr.corp_id = :corpId ");
            map.put("corpId",corpId);
        }
        if (!EmptyUtils.isEmpty(bankCode)){
            sql.append(" and cpr.bank_info_id=:bankCode");
            map.put("bankCode",bankCode);
        }
        if (!EmptyUtils.isEmpty(bankTypeId)){
            sql.append(" and cpr.bank_type_id=:bankTypeId");
            map.put("bankTypeId",bankTypeId);
        }
        if (!EmptyUtils.isBlank(scoringStartDate)){
            sql.append(" and cpr.deadline_date >= to_date('" + scoringStartDate + "','yyyy-MM-dd')  ");
        }
        if (!EmptyUtils.isBlank(scoringEndDate)){
            sql.append(" and cpr.interest_start_date <= to_date('" + scoringEndDate + "','yyyy-MM-dd')");
        }
        return listBySQLAliasToBean(sql.toString(),map, ChoDepositRateDTO.class);
    }

    /**
     * 存款类业务查询剩余本金合计
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getChoFinanceForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sum(cfh.DRAW_BAL) money");
        sql.append(" from cho_bank_finance_history cfh ");
        sql.append(" left join bt_bank_acc acc on cfh.payer_Acc_Id = acc.id ");
        sql.append(" left join bt_currency btcur on cfh.cur_id = btcur.id ");
        sql.append(" where cfh.report_date=:balDate ");
        sql.append(" and btcur.CUR_NAME='人民币' ");
        sql.append(" and "+ SqlUtil.getInSql(corpIds, "cfh.corp_id", true));
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and acc.BANK_TYPE_ID= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and acc.BANK_CODE= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("balDate",balDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }

    /**
     *理财类业务查询待赎回金额合计
     * @param corpIds
     * @param balDate
     * @param bankTypeId
     * @param bankCode
     * @return
     */
    public BigDecimal getChoProductReqForReportDate(List<String> corpIds, Date balDate, String bankTypeId, String bankCode){
        StringBuilder sql = new StringBuilder();
        Map<String, Object> params = new HashMap<String, Object>();
        sql.append(" select sum(cph.WAIT_REDEEM_MONEY) money");
        sql.append(" from cho_product_req_history cph ");
        sql.append(" left join bt_currency btcur on cph.cur_id = btcur.id ");
        sql.append(" where cph.report_date=:balDate ");
        sql.append(" and btcur.CUR_NAME='人民币' ");
        sql.append(" and "+ SqlUtil.getInSql(corpIds, "cph.corp_id", true));
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and cph.BANK_TYPE_ID= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and cph.bank_info_id= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("balDate",balDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }
}
