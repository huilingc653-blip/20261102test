package com.bytter.credit.domain.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.credit.dto.RatBkrationDTO;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CreditLimitDao extends BaseDao {
    /**
     * 查询总行历史数据
     *
     * @param corpId         单位id
     * @param scoringEndDate 评价结束时间
     * @param bankTypeList   银行类别
     * @return 授信数据
     */
    public List<RatBkrationDTO> countCreditExposureLimitByBankType(String corpId, String scoringEndDate, List<String> bankTypeList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select sum(a.money) money,b.bank_type as bankType " + " from RAT_BK_STAT_REPORT_HISTORY a left join rat_bkration b on a.rat_code = b.id where " +
                " b.corp_id = :corpId " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date= to_date('" + scoringEndDate + "','yyyy-MM-dd') " +
                " and " + SqlUtil.getInSql(bankTypeList, "b.bank_type", true) +
                " group by b.bank_type ";
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql, params, RatBkrationDTO.class);
    }

    public List<RatBkrationDTO> countCreditExposureLimitByBankCode(String corpId, String scoringEndDate, List<String> bankCodeList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select sum(a.money) money,b.bank_code as bankCode " + " from RAT_BK_STAT_REPORT_HISTORY a left join rat_bkration b on a.rat_code = b.id where " +
                " b.corp_id = :corpId " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date= to_date('" + scoringEndDate + "','yyyy-MM-dd') " +
                " and " + SqlUtil.getInSql(bankCodeList, "b.bank_code", true) +
                " group by b.bank_code ";
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql, params, RatBkrationDTO.class);
    }

    public List<RatBkrationDTO> countCreditLimitChangeByBankType(String corpId, String scoringStartDate, String scoringEndDate, List<String> bankCodeList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select b.money-a.money as money,b.bank_type as bankType from (select sum(a.money) as money,b.bank_type from rat_bk_stat_report_history a   " +
                " left join rat_bkration b on a.rat_code = b.id where b.corp_id = :corpId    " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date = to_date('" + scoringStartDate + "','yyyy-MM-dd') " +
                " and " + SqlUtil.getInSql(bankCodeList, "b.bank_type", true) +
                " group by b.bank_type ) a   " +
                " left join (select sum(a.money) as money,b.bank_type from rat_bk_stat_report_history a left join rat_bkration b on a.rat_code = b.id where " +
                " b.corp_id = :corpId " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date = to_date('" + scoringEndDate + "','yyyy-MM-dd') " +
                " and "+ SqlUtil.getInSql(bankCodeList, "b.bank_type", true) +
                "  group by b.bank_type ) b on a.bank_type = b.bank_type  ";
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql, params, RatBkrationDTO.class);
    }

    public List<RatBkrationDTO> countCreditLimitChangeByBankCode(String corpId, String scoringStartDate, String scoringEndDate, List<String> bankCodeList) {
        Map<String, Object> params = new HashMap<String, Object>();
        String sql = " select b.money-a.money as money,b.bank_code as bankCode from (select sum(a.money) as money,b.bank_code from rat_bk_stat_report_history a   " +
                " left join rat_bkration b on a.rat_code = b.id where b.corp_id = :corpId    " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date = to_date('" + scoringStartDate + "','yyyy-MM-dd') " +
                " and " + SqlUtil.getInSql(bankCodeList, "b.bank_code", true) +
                " group by b.bank_code ) a   " +
                " left join (select sum(a.money) as money,b.bank_code from rat_bk_stat_report_history a left join rat_bkration b on a.rat_code = b.id where " +
                " b.corp_id = :corpId " +
                " and a.status in ('0','1','2','3')" +
                " and a.report_date = to_date('" + scoringEndDate + "','yyyy-MM-dd') " +
                " and " + SqlUtil.getInSql(bankCodeList, "b.bank_code", true) +
                "  group by b.bank_code ) b on a.bank_code = b.bank_code  ";
        params.put("corpId", corpId);
        return listBySQLAliasToBean(sql, params, RatBkrationDTO.class);
    }

    /**
     * 获取某机构在报表日期多个单位的授信额合计
     */
    public BigDecimal getSumCreditBalForReportDate( List<String> corpIds, Date scoringEndDate,String bankTypeId,String bankCode) {
        Map<String, Object> params = new HashMap<String, Object>();
        StringBuilder sql = new StringBuilder();
        sql.append(" select sum(a.money) money");
        sql.append(" from RAT_BK_STAT_REPORT_HISTORY a left join rat_bkration b on a.rat_code = b.id where ");
        sql.append(SqlUtil.getInSql(corpIds, "b.corp_id", true));
        sql.append(" and a.report_date= :reportDate ");
        if(StringUtils.isNotEmpty(bankTypeId)){
            sql.append(" and b.bank_type= :bankTypeId ");
            params.put("bankTypeId", bankTypeId);
        }
        if(StringUtils.isNotEmpty(bankCode)){
            sql.append(" and b.bank_code= :bankCode");
            params.put("bankCode", bankCode);
        }
        params.put("reportDate", scoringEndDate);
        Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
        BigDecimal balance = BigDecimal.ZERO;
        if (!EmptyUtils.isEmpty(result)) {
            balance = (BigDecimal) result;
        }
        return balance;
    }
}
