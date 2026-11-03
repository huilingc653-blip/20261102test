package com.bytter.report.domain.netbank.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.netbank.vo.HomeBalCalculateResultVO;
import com.bytter.report.domain.netbank.vo.ReportAccountBalVO;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行存款余额趋势Dao
 *
 * @author zuoy 2018-12-05
 */
@Repository
public class ReportAccountBalDao extends BaseDao<ReportAccountBalVO, String> {

    public List<HomeBalCalculateResultVO> getBankAccountBalByLastDay(List<String> corpIds, Date lastDay) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.cur_id as curId, bt_nvl_num(sum(b.avail_bal), 0) as bal ");
        sql.append(" from bis_acc_bal b ");
        sql.append(" WHERE (BT_NVL(b.status, 95) = 95) ");
        sql.append(" and b.bal_Date >= TO_DATE(:startDate, 'yyyy-MM-dd') AND b.bal_Date < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds, "b.corp_Id", true));
        sql.append(" group by b.cur_id having sum(b.avail_bal) <> 0 ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", DateUtils.formatDate(lastDay));
        params.put("endDate", DateUtils.formatDate(DateUtils.addDay(lastDay, 1)));
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBalCalculateResultVO.class);
    }

    public List<HomeBalCalculateResultVO> getInnerAccountBalByLastDay(List<String> corpIds, Date lastDay) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.cur_id as curId, bt_nvl_num(sum(b.balance), 0) as bal ");
        sql.append(" from fc_acc_balance b ");
        sql.append(" INNER JOIN fc_acc acc ON b.account = acc.blnctr_acc ").append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_Id", true));
        sql.append(" where b.balance_date >= TO_DATE(:startDate, 'yyyy-MM-dd') AND b.balance_date < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" group by b.cur_id having sum(b.balance) <> 0 ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", DateUtils.formatDate(lastDay));
        params.put("endDate", DateUtils.formatDate(DateUtils.addDay(lastDay, 1)));
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBalCalculateResultVO.class);
    }

    public List<HomeBalCalculateResultVO> getInnerAccountBalByMonth(List<String> corpIds, String startDate, String endDate) {
        StringBuilder sql = new StringBuilder();
        sql.append("select b.cur_id as curId, bt_nvl_num(sum(b.balance), 0) as bal ");
        sql.append(" from fc_acc_balance b ");
        sql.append(" left join fc_acc acc on b.account = acc.blnctr_acc ");
        sql.append(" where b.balance_date >= TO_DATE(:startDate, 'yyyy-MM-dd') AND b.balance_date < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" and ").append(SqlUtil.getInSql(corpIds, "acc.corp_Id", true));
        sql.append(" and b.balance_date = (select max(bal.balance_date) from fc_acc_balance bal where bal.account = b.account and bal.cur_id = b.cur_id");
        sql.append("                        and bal.balance_date >= TO_DATE(:startDate, 'yyyy-MM-dd') AND bal.balance_date < TO_DATE(:endDate, 'yyyy-MM-dd') ) ");
        sql.append("  group by b.cur_id having sum(b.balance) <> 0");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBalCalculateResultVO.class);
    }

    /**
     * 获取当日收支金额
     *
     * @param corpIds 单位id集合
     * @return 数据集合
     */
    public List<HomeBalCalculateResultVO> getToDayInOutAmt(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append("select COALESCE(SUM(CASE WHEN dtl.cd_sign = 0 THEN amt ELSE 0 END), 0) AS inBal, ");
        sql.append(" COALESCE(SUM(CASE WHEN dtl.cd_sign = 1 THEN amt ELSE 0 END), 0) AS outBal, dtl.CUR_ID as curId ");
        sql.append(" FROM bis_acc_dtl dtl ");
        // 避免在条件列上使用函数bt_to_char，会导致无法使用trans_time上的索引
        sql.append(" WHERE dtl.trans_time >= TO_DATE(:startDate, 'yyyy-MM-dd') AND dtl.trans_time < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" AND (BT_NVL(dtl.status, 95) = 95)  ");
        sql.append(" AND EXISTS ( SELECT 1 FROM bt_bank_acc acc WHERE dtl.bank_acc = acc.bank_acc ");
        sql.append(" AND ").append(SqlUtil.getInSql(corpIds, "acc.corp_Id", true));
        sql.append(" ) ");
        sql.append(" GROUP BY dtl.CUR_ID ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", DateUtils.formatDate(DateUtils.getDate()));
        params.put("endDate", DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), 1)));
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBalCalculateResultVO.class);
    }

    /**
     * 获取当日余额
     *
     * @param corpIds 单位id集合
     * @return 数据集合
     */
    public List<HomeBalCalculateResultVO> getToDayBal(List<String> corpIds) {
        StringBuilder sql = new StringBuilder();
        sql.append(" SELECT COALESCE(SUM(bal.bal), 0) AS bal, bal.cur_id as curId ");
        sql.append(" FROM Bis_Acc_Bal bal ");
        sql.append(" WHERE bal.bal_Date >= TO_DATE(:startDate, 'yyyy-MM-dd') AND bal.bal_Date < TO_DATE(:endDate, 'yyyy-MM-dd') ");
        sql.append(" AND (BT_NVL(bal.status, 95) = 95) ");
        sql.append(" AND ").append(SqlUtil.getInSql(corpIds, "bal.corp_Id", true));
        sql.append(" GROUP BY bal.cur_id ");

        Map<String, Object> params = Maps.newHashMap();
        params.put("startDate", DateUtils.formatDate(DateUtils.getDate()));
        params.put("endDate", DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(), 1)));
        return this.listBySQLAliasToBean(sql.toString(), params, HomeBalCalculateResultVO.class);
    }
}
