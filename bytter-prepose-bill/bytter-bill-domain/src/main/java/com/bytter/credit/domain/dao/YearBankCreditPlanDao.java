package com.bytter.credit.domain.dao;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.credit.domain.entity.YearBankCreditPlan;
import com.bytter.credit.domain.vo.BankCreditPlanTraceVO;
import com.bytter.credit.domain.vo.YearBankCreditPlanVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行授信年度计划Dao
 *
 * @author huao
 */
@Repository
public class YearBankCreditPlanDao extends BaseDao<YearBankCreditPlan, String> {

    public DataTablesOutput<YearBankCreditPlanVO> dataTableQuery(DataTablesInput input, List<String> corpAuthList) {
        String sql = "  SELECT " +
                "           t1.id, t1.corp_id AS corpId, t2.name AS corpName, t1.cur_id AS curId, t3.cur_name AS curName, " +
                "           t1.year, t1.bank_id AS bankId, t1.bank_code AS bankCode, t1.bank_name AS bankName, " +
                "           t1.credit_type AS creditType, t1.credit_amount AS creditAmount, t1.status, t1.create_name AS createName, " +
                "           t1.create_time AS createTime " +
                "       FROM" +
                "           year_bank_credit_plan t1 " +
                "           LEFT JOIN sys_corp t2 ON t2.id = t1.corp_id " +
                "           LEFT JOIN bt_currency t3 ON t3.id = t1.cur_id " +
                "       WHERE " +
                "           1 = 1 ";
        Map<String, Object> param = new HashMap<>();
        if (CollUtil.isNotEmpty(corpAuthList)) {
            sql += " AND t1.corp_id IN (:corpIds)";
            param.put("corpIds", corpAuthList);
        }
        return getDynamicBySQL(input, sql, YearBankCreditPlanVO.class, "", false, param);
    }

    public List<YearBankCreditPlanVO> listByCorpAndYear(String corpId, String bankCode, Integer year) {
        String sql = "  SELECT " +
                "           t1.id, t1.corp_id AS corpId, t2.name AS corpName, t1.cur_id AS curId, t3.cur_name AS curName, " +
                "           t1.year, t1.bank_id AS bankId, t1.bank_code AS bankCode, t1.bank_name AS bankName, " +
                "           t1.credit_type AS creditType, t1.credit_amount AS creditAmount, t1.status, t1.create_name AS createName, " +
                "           t1.create_time AS createTime " +
                "       FROM" +
                "           year_bank_credit_plan t1 " +
                "           LEFT JOIN sys_corp t2 ON t2.id = t1.corp_id " +
                "           LEFT JOIN bt_currency t3 ON t3.id = t1.cur_id " +
                "       WHERE " +
                "           1 = 1 " +
                "           AND t1.corp_id = :corpId " +
                "           AND t1.bank_code = :bankCode " +
                "           AND t1.year = " + year;
        Map<String, Object> param = new HashMap<>();
        param.put("corpId", corpId);
        param.put("bankCode", bankCode);
        return listBySQLAliasToBean(sql, param, YearBankCreditPlanVO.class);
    }

    public YearBankCreditPlanVO getById(String id) {
        String sql = "  SELECT " +
                "           t1.id, t1.corp_id AS corpId, t2.name AS corpName, t1.cur_id AS curId, t3.cur_name AS curName, " +
                "           t1.year, t1.bank_id AS bankId, t1.bank_code AS bankCode, t1.bank_name AS bankName, " +
                "           t1.credit_type AS creditType, t1.credit_amount AS creditAmount, t1.status, t1.create_name AS createName, " +
                "           t1.create_time AS createTime " +
                "       FROM" +
                "           year_bank_credit_plan t1 " +
                "           LEFT JOIN sys_corp t2 ON t2.id = t1.corp_id " +
                "           LEFT JOIN bt_currency t3 ON t3.id = t1.cur_id " +
                "       WHERE " +
                "           1 = 1 " +
                "           AND t1.id = :id ";
        Map<String, Object> param = new HashMap<>();
        param.put("id", id);
        return getBySQLAliasToBean(sql, param, YearBankCreditPlanVO.class);
    }

    public List<BankCreditPlanTraceVO> getAmountByDateAndType(String date, String type) {
        String sql = "  SELECT " +
                "           t1.CORP_ID AS corpId, " +
                "           SUM(t2.money) AS flowLoanLimit " +
                "       FROM " +
                "           rat_bkration t1 " +
                "           LEFT JOIN rat_bkration_detail t2 ON t2.RAT_CODE = t1.ID " +
                "           LEFT JOIN rat_item_custom t3 ON t3.ID = t2.ITEM_CODE " +
                "       WHERE " +
                "           1 = 1 " +
                "           AND t1.`STATUS` = 95 " +
                "           AND t1.ENDDATE >= :date " +
                "           AND t3.`NAME` LIKE :type " +
                "           GROUP BY t1.CORP_ID ";
        Map<String, Object> param = new HashMap<>();
        param.put("type", type);
        param.put("date", date);
        return listBySQLAliasToBean(sql, param, BankCreditPlanTraceVO.class);
    }

    public DataTablesOutput<BankCreditPlanTraceVO> ratBkrationQuery(DataTablesInput input, List<String> corpAuthList) {
        Map<String, Object> formdata = input.getFormdata();
        // 默认上月
        String month = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), DatePattern.NORM_MONTH_FORMAT);
        if (formdata.containsKey("date") && CharSequenceUtil.isNotBlank(formdata.get("date").toString())) {
            month = formdata.get("date").toString();
        }
        String nextMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(month, DatePattern.NORM_MONTH_FORMAT), 1), DatePattern.NORM_MONTH_FORMAT) + "-01";
        month += "-01";
        int year = DateUtil.year(DateUtil.parse(month, DatePattern.NORM_MONTH_FORMAT));

        String sql =
                " SELECT  " +
                        year + " AS year, " +
                        "  t1.ID AS id, t.*, t1.MONEY AS planCreditLimit, " +
                        "  CASE WHEN t1.ENDDATE < ' " + month + " ' THEN '到期续签中' ELSE CONCAT(DATE(t1.BEGINDATE), '~', DATE(t1.ENDDATE)) END AS contractStartEndDate," +
                        "  t6.COOPERATION_TYPE AS cooperationType, " +
                        "  t7.YEAR_PURCHASE_AMOUNT AS yearPurchase, " +
                        "  t7.MERGE_INVESTMENT_AMOUNT AS mergeAcquisitionInvest, " +
                        "  (t7.YEAR_PURCHASE_AMOUNT * 0.35 + t7.MERGE_INVESTMENT_AMOUNT * 0.51 * 0.6) AS creditRequire, " +
                        "  t7.SOLVE_MEASURE AS solveMeasure, " +
                        "  t7.EXIST_DIFFICULTY AS existDifficulty " +
                " FROM  " +
                        // 按 corpId、bankCode 进行分组，对额度求和，同时每组拿到最大的授信开始时间
                        "  (SELECT  " +
                        "    candidate.corpId, candidate.bankCode, " +
                        "    MAX(candidate.BEGINDATE) AS beginDate, " +
                        "    SUM(candidate.MONEY) AS bankLimit " +
                        "  FROM  " +
                        // 查询出所有满足条件的授信合同
                        "    (SELECT DISTINCT  " +
                        "      t1.ID AS id,  " +
                        "      t1.CORP_ID AS corpId,  " +
                        "      t1.BANK_CODE AS bankCode,  " +
                        "      t1.BEGINDATE,  " +
                        "      t1.MONEY  " +
                        "    FROM  " +
                        "      rat_bkration t1  " +
                        "      LEFT JOIN rat_bkration_detail t2 ON t2.RAT_CODE = t1.ID  " +
                        "      LEFT JOIN rat_item_custom t3 ON t3.ID = t2.ITEM_CODE  " +
                        "      LEFT JOIN rat_uration t4 ON t4.RAT_CODE = t1.ID  " +
                        "    WHERE  " +
                        "      t1.`STATUS` = 95  " +
                        "      AND " + getSQLIn(corpAuthList, 900, "t1.CORP_ID") +
                        "      AND t3.`NAME` LIKE '银行承兑汇票%'  " +
                        "      AND (  " +
                        //        -- 条件1: 授信在指定日期有效
                        "        (t1.BEGINDATE < ' " + nextMonth + " ' AND t1.ENDDATE > ' " + month + "  ') OR " +
                        //        -- 条件2: 授信已过期但存在有效的占用记录
                        "        (t1.ENDDATE < ' " + month + " ' AND t4.`STATUS` = 2 AND t4.END_DATE >= ' " + month + " ')  " +
                        "      )  " +
                        "    ) candidate   " +
                        "    GROUP BY candidate.corpId, candidate.bankCode  " +
                        "  ) t  " +
                        "  INNER JOIN rat_bkration t1 ON t.corpId = t1.CORP_ID AND t.bankCode = t1.BANK_CODE AND t.beginDate = t1.BEGINDATE  " +
                        "  LEFT JOIN bt_bank t4 ON t4.SYS_BANK_CODE = t1.BANK_CODE " +
                        "  LEFT JOIN RAT_CREDIT_COOPERATION_BANK t6 ON t6.BANK_CODE = t1.BANK_CODE " +
                        "  LEFT JOIN RAT_YEARFIN_PLAN_REGISTER t7 ON t7.CORP_ID = t.corpId AND t7.STATUS = 95 AND t7.years = " + year +
                " WHERE  " +
                        "  1 = 1  " +
                        // 如果授信开始时间一样，则取最大的授信结束记录
                        "  AND NOT EXISTS (SELECT 1 FROM rat_bkration tt WHERE tt.CORP_ID = t1.CORP_ID AND tt.BANK_CODE = t1.BANK_CODE AND tt.BEGINDATE = t1.BEGINDATE AND tt.ENDDATE < t1.ENDDATE ) ";
        return getDynamicBySQL(input, sql, BankCreditPlanTraceVO.class, "", false, new HashMap<>());
    }
    
    public List<BankCreditPlanTraceVO> listRatBkrationDetail(List<String> ratCodeList) {
        String sql = 
                " SELECT  " +
                        "  m.*,  " +
                        "  t4.BANK_NAME AS planCreditBankName,  " +
                        "  t4.BANK_NAME AS creditBankName,  " +
                        "  t5.BOND_RATIO AS marginRatio,  " +
                        "  t5.OPEN_BILL_RATE AS invoiceFee,  " +
                        "  t5.BOND_RATE AS marginRate,  " +
                        "  t5.IS_USED AS isUsed  " +
                " FROM  " +
                        "  (SELECT   " +
                        "    t1.ID AS id,  t1.CORP_ID AS corpId,  t1.CORP_NAME AS corpName,  t1.BANK_CODE AS bankCode,  " +
                        "    CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' WHEN t3.`NAME` = '流动资金贷款' THEN '3' WHEN t3.`NAME` LIKE '%低风险%' THEN '2' ELSE '' END AS creditType,  " +
                        "    t2.MONEY AS money,  t2.MAX_MONEY AS maxMoney " +
                        "  FROM  " +
                        "    rat_bkration t1  " +
                        "    LEFT JOIN rat_bkration_detail t2 ON t2.RAT_CODE = t1.ID  " +
                        "    LEFT JOIN rat_item_custom t3 ON t3.ID = t2.ITEM_CODE  " +
                        "  WHERE  " +
                        "    1 = 1  " +
                        "    AND (t3.`NAME` LIKE '%敞口%' OR t3.`NAME` LIKE '%低风险%' OR t3.`NAME` = '流动资金贷款')  " +
                        "    AND " + getSQLIn(ratCodeList, Integer.MAX_VALUE, "t1.ID") +
                        "   ) m  " +
                        "   LEFT JOIN bt_bank t4 ON t4.SYS_BANK_CODE = m.bankCode  " +
                        "   LEFT JOIN AB_BILL_CREDIT_INFO_MAINTAIN t5 ON t5.CORP_ID = m.corpId AND t5.BANK_TYPE_ID = t4.BANK_TYPE_ID AND m.creditType = t5.CREDIT_TYPE";
        return listBySQLAliasToBean(sql, new HashMap<>(),  BankCreditPlanTraceVO.class);
    }

    public List<BankCreditPlanTraceVO> listRatUration(List<String> ratCodeList) {
        String sql =
                " SELECT  " +
                        "  t.*  " +
                " FROM  " +
                        "  (SELECT  " +
                        "    t1.RAT_CODE AS id,  " +
                        "    t1.MONEY AS money,  " +
                        "    CASE WHEN t2.`NAME` LIKE '%敞口%' THEN '1' WHEN t2.`NAME` = '流动资金贷款' THEN '3' WHEN t2.`NAME` LIKE '%低风险%' THEN '2' ELSE '' END AS creditType,  " +
                        "    t2.`NAME`  " +
                        "  FROM  " +
                        "    rat_uration t1  " +
                        "    LEFT JOIN rat_item_custom t2 ON t2.ID = t1.ITEM_CODE  " +
                        "  WHERE  " +
                        "    1 = 1  " +
                        "    AND t1.`STATUS` = 2  " +
                        "    AND " + getSQLIn(ratCodeList, Integer.MAX_VALUE, "t1.RAT_CODE ") +
                        "  ) t  " +
                " WHERE  " +
                        "  1 = 1  " +
                        "  AND t.creditType != '' ";
        return listBySQLAliasToBean(sql, new HashMap<>(), BankCreditPlanTraceVO.class);
    }

    public List<BankCreditPlanTraceVO> listRatUrationByCorpAndBank(String corpId, String bankCode, String startDate, String endDate) {
        String sql =
                " SELECT  " +
                        "  t.*  " +
                " FROM  " +
                        "  (SELECT  " +
                        "    t1.RAT_CODE AS id,  " +
                        "    t1.MONEY AS money,  " +
                        "    CASE WHEN t2.`NAME` LIKE '%敞口%' THEN '1' WHEN t2.`NAME` = '流动资金贷款' THEN '3' WHEN t2.`NAME` LIKE '%低风险%' THEN '2' ELSE '' END AS creditType,  " +
                        "    t2.`NAME`  " +
                        "  FROM  " +
                        "    rat_uration t1  " +
                        "    LEFT JOIN rat_item_custom t2 ON t2.ID = t1.ITEM_CODE  " +
                        "    LEFT JOIN rat_bkration t3 ON t3.ID = t1.RAT_CODE  " +
                        "  WHERE  " +
                        "    1 = 1  " +
                        "    AND t1.`STATUS` = 2  " +
                        "    AND t1.CORP_ID = '" + corpId + "' " +
                        "    AND t3.BANK_CODE = '" + bankCode + "' " +
                        "    AND t1.END_DATE >= '" + startDate + "' " +
                        "    AND t1.END_DATE < '" + endDate + "' " +
                        "  ) t  " +
                " WHERE  " +
                        "  1 = 1  " +
                        "  AND t.creditType != '' ";
        return listBySQLAliasToBean(sql, new HashMap<>(), BankCreditPlanTraceVO.class);
    }

    public List<BankCreditPlanTraceVO> listBillUrationByCorpAndBank(String corpId, String bankCode, String month) {
        month += "-01";
        String nextMonth = DateUtil.format(DateUtil.offsetMonth(DateUtil.parse(month, DatePattern.NORM_MONTH_FORMAT), 1), DatePattern.NORM_MONTH_FORMAT) + "-01";
        String sql =
                " SELECT  " +
                        "  t.*  " +
                " FROM  " +
                        "  (SELECT  " +
                        "    t1.CORP_ID,  " +
                        "    t5.BANK_CODE,  " +
                        "    t1.MONEY,  " +
                        "    CASE WHEN t4.`NAME` LIKE '%敞口%' THEN '1' WHEN t4.`NAME` = '流动资金贷款' THEN '3' WHEN t4.`NAME` LIKE '%低风险%' THEN '2' ELSE '' END AS creditType  " +
                        "  FROM  " +
                        "    rat_uration t1  " +
                        "    LEFT JOIN ab_voucherinfo_vice t2 ON t1.BILL_ID = t2.ID  " +
                        "    LEFT JOIN ab_voucherinfo t3 ON t3.ID = t2.VID  " +
                        "    LEFT JOIN rat_item_custom t4 ON t4.ID = t1.ITEM_CODE  " +
                        "    LEFT JOIN rat_bkration t5 ON t5.ID = t1.RAT_CODE  " +
                        "  WHERE  " +
                        "    1 = 1  " +
                        "    AND t1.CORP_ID = '" + corpId + "' " +
                        "    AND t5.BANK_CODE = '" + bankCode + "' " +
                        "    AND t3.END_DATE > '" + month + "' AND t3.START_DATE < '" + nextMonth + "' " +
                        "    AND t2.BAIL_RATE = 1  " +
                        "  ) t  " +
                " WHERE  " +
                        "  1 = 1  " +
                        "  AND t.creditType != '' ";
        return listBySQLAliasToBean(sql, new HashMap<>(), BankCreditPlanTraceVO.class);
    }

    public DataTablesOutput<BankCreditPlanTraceVO> traceDataTableQuery(DataTablesInput input, List<String> corpAuthList) {
        String sql = "  SELECT  " +
                "           m.*, " +
                "           :years AS year, " +
                "           m.planCreditBankName AS creditBankName, " +
                "           t6.COOPERATION_TYPE AS cooperationType, " +
                "           t7.YEAR_PURCHASE_AMOUNT AS yearPurchase, " +
                "           t7.MERGE_INVESTMENT_AMOUNT AS mergeAcquisitionInvest, " +
                "           (t7.YEAR_PURCHASE_AMOUNT * 0.35 + t7.MERGE_INVESTMENT_AMOUNT * 0.51 * 0.6) AS creditRequire, " +
                "           t7.SOLVE_MEASURE AS solveMeasure, " +
                "           t7.EXIST_DIFFICULTY AS existDifficulty " +
                "       FROM " +
                "           (SELECT  " +
                "               t1.ID AS id, " +
                "               t1.CORP_ID AS corpId, " +
                "               t1.CORP_NAME AS corpName, " +
                "               t1.BANK_CODE, " +
                "               CONCAT(t4.BANK_NAME, SUBSTRING(t3.`NAME`, 7)) AS planCreditBankName, " +
                "               t3.`NAME`, " +
                "               CASE  " +
                "                   WHEN t1.ENDDATE < :date THEN '到期续签中' " +
                "                   ELSE CONCAT(DATE(t1.BEGINDATE), '~', DATE(t1.ENDDATE)) " +
                "               END AS contractStartEndDate, " +
                "               t2.money AS planCreditLimit, " +
                "               t2.MAX_MONEY AS comprehensiveLimit, " +
                "               CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' ELSE '2' END AS creditType, " +
                "               t5.BOND_RATIO AS marginRatio, " +
                "               t5.OPEN_BILL_RATE AS invoiceFee, " +
                "               t5.BOND_RATE AS marginRate, " +
                "               t5.IS_USED AS isUsed, " +
                "               group_total.totalMoney AS bankLimit" +
                "           FROM " +
                "               rat_bkration t1 " +
                "               LEFT JOIN rat_bkration_detail t2 ON t2.RAT_CODE = t1.ID " +
                "               LEFT JOIN rat_item_custom t3 ON t3.ID = t2.ITEM_CODE " +
                "               LEFT JOIN bt_bank t4 ON t4.SYS_BANK_CODE = t1.BANK_CODE " +
                "               LEFT JOIN AB_BILL_CREDIT_INFO_MAINTAIN t5 ON t5.BANK_TYPE_ID = t4.BANK_TYPE_ID AND (CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' ELSE '2' END) = t5.CREDIT_TYPE " +
                "               LEFT JOIN ( " +
                //          -- 子查询：计算每个分组(按corp_id, bank_code和creditType)的totalMoney "
                "                   SELECT  " +
                "                       t1.CORP_ID,  " +
                "                       t1.BANK_CODE, " +
                "                       CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' ELSE '2' END AS creditType, " +
                "                       SUM(t2.MONEY) as totalMoney " +
                "                   FROM " +
                "                       rat_bkration t1 " +
                "                       LEFT JOIN rat_bkration_detail t2 ON t2.RAT_CODE = t1.ID " +
                "                       LEFT JOIN rat_item_custom t3 ON t3.ID = t2.ITEM_CODE " +
                "                   WHERE " +
                "                       t3.`NAME` LIKE '银行承兑汇票%' " +
                "                       GROUP BY t1.CORP_ID, t1.BANK_CODE, CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' ELSE '2' END " +
                "               ) group_total ON group_total.CORP_ID = t1.CORP_ID AND group_total.BANK_CODE = t1.BANK_CODE " +
                "                    AND group_total.creditType = CASE WHEN t3.`NAME` LIKE '%敞口%' THEN '1' ELSE '2' END " +
                "           WHERE " +
                "               1 = 1 " +
                "               AND t1.`STATUS` = 95 " +
                "               AND " + getSQLIn(corpAuthList, 900, "t1.CORP_ID") +
                "               AND t1.ID IN ( " +
                //          -- 子查询：找出每个corp_id和bank_code分组中BEGINDATE和ENDDATE最大的记录ID " +
                "                   SELECT t1_sub.ID " +
                "                   FROM rat_bkration t1_sub " +
                "                   WHERE (t1_sub.BEGINDATE, t1_sub.ENDDATE) = ( " +
                "                       SELECT MAX(t1_max.BEGINDATE), MAX(t1_max.ENDDATE) " +
                "                       FROM rat_bkration t1_max " +
                "                       WHERE t1_max.CORP_ID = t1_sub.CORP_ID  " +
                "                       AND t1_max.BANK_CODE = t1_sub.BANK_CODE " +
                "                       AND t1_max.BEGINDATE = ( " +
                "                           SELECT MAX(t1_begindate.BEGINDATE) " +
                "                           FROM rat_bkration t1_begindate " +
                "                           WHERE t1_begindate.CORP_ID = t1_sub.CORP_ID  " +
                "                           AND t1_begindate.BANK_CODE = t1_sub.BANK_CODE " +
                "                       ) " +
                "                   ) " +
                "               ) " +
                "               AND t3.`NAME` LIKE '银行承兑汇票%' " +
                "               AND ( " +
                "                   t1.ENDDATE >= :date " +
                "                   OR EXISTS ( " +
                "                       SELECT 1 FROM rat_uration t4  " +
                "                       WHERE t4.RAT_CODE = t1.ID " +
                "                   ) " +
                "               ) " +
                "           ) m " +
                "           LEFT JOIN RAT_CREDIT_COOPERATION_BANK t6 ON t6.BANK_CODE = m.BANK_CODE " +
                "           LEFT JOIN RAT_YEARFIN_PLAN_REGISTER t7 ON t7.CORP_ID = m.corpId AND t7.STATUS = 95 AND t7.years = :years " +
                "           ORDER BY m.corpId, m.ID, m.BANK_CODE ";


        Map<String, Object> paramMap = input.getFormdata();
        // 默认上月
        String month = DateUtil.format(DateUtil.offsetMonth(DateUtil.date(), -1), DatePattern.NORM_MONTH_FORMAT);
        int year = Integer.parseInt(month.substring(0, 4));
        if (paramMap.containsKey("date") && CharSequenceUtil.isNotBlank(paramMap.get("date").toString())) {
            month = paramMap.get("date").toString();
            year = Integer.parseInt(month.substring(0, 4));
        }

        Map<String, Object> param = new HashMap<>();
        param.put("years", year);
        param.put("date", month + "-01");
        return getDynamicBySQL(input, sql, BankCreditPlanTraceVO.class, "", false, param);
    }
}
