package com.bytter.biz.report.dao;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.report.vo.IncomeAndExpenditureOfFundsVo;
import com.bytter.biz.report.vo.IncomeExpenditureReportVO;
import com.bytter.cloud.common.core.util.DateUtil;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.system.api.dto.CorpDTO;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author zhoup
 * @version 1.0
 * @Title: Dao
 * @Description: 资金收支表Dao
 * @date 2024-5-10 16:31:56
 */
@Repository
public class IncomeAndExpenditureOfFundsDao extends BaseDao<IncomeAndExpenditureOfFundsVo, String> {

    public List<IncomeAndExpenditureOfFundsVo> getDataTablesUnion(List<String> corpIdList1,String corpIdStr,Date startDate,Date endDate, String regionId,String equityMaintenance,List<String> accGroup) {
        StringBuilder sql = new StringBuilder();
        Map<String, Object> map = new HashMap<>();
        // 日期全部转为字符串
        if (startDate!=null){
            map.put("startDate",DateUtils.formatDate(startDate));
            //开始日期-1
            map.put("beginDate", DateUtils.formatDate((DateUtils.addDay(startDate,-1))));
        }else{
            map.put("startDate", DateUtil.getCurrentWeekDay(1));
            map.put("beginDate", DateUtils.formatDate(DateUtils.addDay(DateUtil.parse(DateUtil.getCurrentWeekDay(1),"yyyy-MM-dd"),-1)));
        }
        if (endDate!=null){
            map.put("endDate", DateUtils.format(endDate,"yyyy-MM-dd"));
            map.put("endDate1", DateUtils.format(DateUtils.addDay(endDate,1),"yyyy-MM-dd"));
        }else{
            map.put("endDate", DateUtil.getCurrentWeekDay(7));
            map.put("endDate1", DateUtils.format(DateUtils.addDay(DateUtil.parse(DateUtil.getCurrentWeekDay(7),"yyyy-MM-dd"),1),"yyyy-MM-dd"));
        }

        sql.append("select sc.id as corpId, sc.name as corpName,sc.parent_Ids as parentIds, cp.id as regionId, ");
        sql.append("cp.name as regionName, sc.equityMaintenance_ID as equityMaintenanceId, b.maxRate as maxRate, ");
        sql.append(" ba.beginBal as beginBal, iexp.income as income, iexp.expenditure as expenditure ,");
        sql.append(" ba1.realEndBal as realEndBal, cpr1.manageMoney as manageMoney,sc.code as corpCode,sc.province_id as provinceId   ");
        sql.append(" from sys_corp sc ");
        sql.append(" left join sys_corp cp on sc.parent_id = cp.id ");
        sql.append(" left join ( select bba.corp_id as corpId, max(bba.rates ) as maxRate  ");
                    sql.append(" from bt_bank_acc bba  ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("where bba.fc_acc_group_code in (:accGroup) ");
                        map.put("accGroup",accGroup);
                    }
                    sql.append(" group by bba.corp_id ");
        sql.append("  ) b on b.corpId = sc.id ");
        sql.append(" left join ( select bal.corp_id as corpId, sum(bal.bal) as beginBal   ");
                    sql.append(" from bis_acc_bal bal  ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("join bt_bank_acc bba on bal.bank_acc = bba.bank_acc and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append(" where bal.bal_date >= BT_TO_DATE(:beginDate, 'yyyy-MM-dd') and bal.bal_date < BT_TO_DATE(:startDate, 'yyyy-MM-dd') ");
                    sql.append(" group by bal.corp_id ");
        sql.append(" ) ba on ba.corpId = sc.id ");
        sql.append(" left join ( select bal.corp_id as corpId, sum(bal.bal) as realEndBal ");
                    sql.append(" from bis_acc_bal bal  ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("join bt_bank_acc bba on bal.bank_acc = bba.bank_acc and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append(" where bal.bal_date >= BT_TO_DATE(:endDate, 'yyyy-MM-dd') and bal.bal_date < BT_TO_DATE(:endDate1, 'yyyy-MM-dd') ");
                    sql.append(" group by bal.corp_id ");
        sql.append(" ) ba1 on ba1.corpId = sc.id  ");
        sql.append(" left join ( select bba.corp_id as corpId, sum(case when bad.`CD_SIGN` = '0' then bad.amt else 0 end) as income, sum(case when bad.`CD_SIGN` = '1' then bad.amt else 0 end) as expenditure ");
                    sql.append(" from bis_acc_dtl bad   ");
                    sql.append(" left join bt_bank_acc bba on bba.bank_acc = bad.bank_acc ");
                    sql.append(" where bad.trans_time >= BT_TO_DATE(:startDate, 'yyyy-MM-dd') and bad.trans_time < BT_TO_DATE(:endDate1, 'yyyy-MM-dd') ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append(" group by bba.corp_id  ");
        sql.append(" ) iexp on iexp.corpId = sc.id ");
        sql.append(" left join ( select cpr.CORP_ID as corpId,sum(cpr.BUY_MONEY) - sum(BT_NVL_NUM(cprd.redeem_money,0)) as manageMoney ");
                sql.append(" from cho_product_req cpr ");
                sql.append(" left join cho_product_redeem cprd on cpr.id = cprd.req_id ");
                if (ObjectUtil.isNotEmpty(accGroup)){
                    sql.append("join bt_bank_acc bba on cpr.bank_acc = bba.bank_acc and bba.fc_acc_group_code in (:accGroup) ");
                }
                sql.append(" where cpr.DEADLINE_DATE >= BT_TO_DATE(:endDate, 'yyyy-MM-dd') ");
                sql.append(" group by cpr.corp_id  ");
        sql.append(" ) cpr1 on cpr1.corpId = sc.id ");
        //sql.append(" where sc.PARENT_ID in (SELECT id FROM sys_corp WHERE UNIT_ATTRIBUTE = '02' and UNIT_CM = :regionId ) ");
        sql.append(" where sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') AND sc.`STATUS` = 1 ");
        map.put("regionId",regionId);
        if (EmptyUtils.isNotEmpty(equityMaintenance)){
            sql.append(" and sc.equityMaintenance_ID in (:equityMaintenanceList) ");
            String[] strings = equityMaintenance.split(",");
            map.put("equityMaintenanceList", Arrays.asList(strings));
        }
        if (EmptyUtils.isNotEmpty(corpIdStr)){
            List<String> corpIdList = Arrays.asList(corpIdStr.split(","));
            if (corpIdList.size()!=0){
                sql.append(" and "+getSQLIn(corpIdList,900,"sc.id"));
            }
        }else{
            if (!CollectionUtils.isEmpty(corpIdList1)){
                sql.append(" and "+getSQLIn(corpIdList1,900,"sc.id"));
            }
        }
        sql.append(" order by sc.equityMaintenance_id asc,sc.code asc ");
        return listBySQLAliasToBean(sql.toString(),map,IncomeAndExpenditureOfFundsVo.class);
    }

    public CorpDTO selectRegionByCorpIds(List<String> parentPath,String regionId) {
        StringBuilder sql = new StringBuilder();
        sql.append(" select sc.id as id, sc.name as name ");
        sql.append(" from sys_corp sc where 1=1 ");
        sql.append(" and "+getSQLIn(parentPath,900,"sc.id"));
        sql.append(" and sc.unit_cm = :regionId");
        Map<String, Object> map = new HashMap<>();
        map.put("regionId",regionId);
        List<CorpDTO> corpDTOS = listBySQLAliasToBean(sql.toString(), map, CorpDTO.class);
        if (CollectionUtils.isEmpty(corpDTOS)){
            return null;
        }
        return corpDTOS.get(0);
    }

    public List<IncomeExpenditureReportVO> queryMultiLeveData(List<String> corpIds, Date startDate, Date endDate, List<String> accGroup,String equityMaintenance) {
        Map<String,Object> params = new HashMap<>();
        params.put("startDate", DateUtils.formatDate(startDate));
        params.put("endDate", DateUtils.formatDate(endDate));
        // 前一天日期
        Date beforeDate = DateUtils.addDay(startDate,-1);
        params.put("beforeDate",  DateUtils.formatDate(beforeDate));

        StringBuilder sql = new StringBuilder(128);
        sql.append("SELECT sc.id corpId,sc.`CODE` corpCode,sc.`NAME` corpName,sc.PARENT_ID parentId,sc.PARENT_IDS parentIds,sc.sort,sc.report_display reportDisplay,");
        sql.append("sc.province_id provinceId,cm.cell_code cellCode,cm.cell_name cellName,sem.equity_code equityCode,sem.equity_name equityName,");
        sql.append("b.maxRate,");
        sql.append("BT_NVL_NUM(ba.beginBal,0) beginBal,");
        sql.append("BT_NVL_NUM(iexp.income,0) income,BT_NVL_NUM(iexp.expenditure,0) expenditure,");
        // 期末余额= 期初余额+收入-支出
        sql.append("BT_NVL_NUM(ba.beginBal,0) + BT_NVL_NUM(iexp.income,0) - BT_NVL_NUM(iexp.expenditure,0) endBal,");
        sql.append("BT_NVL_NUM(ba1.realEndBal,0) realEndBal,");
        // 差异金额= 实际期末余额-期末余额
        sql.append("BT_NVL_NUM(ba1.realEndBal,0) - (BT_NVL_NUM(ba.beginBal,0) + BT_NVL_NUM(iexp.income,0) - BT_NVL_NUM(iexp.expenditure,0)) diffMoney, ");
        sql.append("BT_NVL_NUM(cpr1.choMoney,0) choMoney ");
        sql.append("from sys_corp sc ");
        sql.append("join sys_corp net on sc.NET_ID = net.ID AND net.`CODE` = '100'  ");
        sql.append("LEFT JOIN sys_cell_manage cm ON sc.UNIT_CM = cm.id  ");
        sql.append("JOIN SYS_EQUITY_MAINTENANCE sem ON sc.equityMaintenance_ID = sem.Equity_code AND sem.Equity_code <> '0003' ");
        sql.append("left join ( select bba.corp_id as corpId, max(bba.rates ) maxRate  ");
                    sql.append("from bt_bank_acc bba ");
                    sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
                    sql.append("where bba.status <> -2 ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append(" and bba.fc_acc_group_code in (:accGroup) ");
                        params.put("accGroup",accGroup);
                    }
                    sql.append("group by bba.corp_id ");
        sql.append(") b on b.corpId = sc.id ");
        sql.append("left join ( SELECT bal.corp_id AS corpId, sum(bal.bal) AS beginBal ");
                    sql.append("from bis_acc_bal bal ");
                    sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc and bba.status <> -2 ");
                    sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
                    sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :beforeDate ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append("group by bal.corp_id  ");
        sql.append(") ba on ba.corpId = sc.id  ");
        sql.append("left join ( SELECT bal.corp_id AS corpId, sum(bal.bal) AS realEndBal ");
                    sql.append("from bis_acc_bal bal ");
                    sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc  and bba.status <> -2 ");
                    sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
                    sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :endDate ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append("group by bal.corp_id  ");
        sql.append(") ba1 on ba1.corpId = sc.id  ");
        sql.append("LEFT JOIN ( SELECT bba.corp_id AS corpId,");
                    sql.append("sum( CASE WHEN bad.`CD_SIGN` = '0' THEN bad.amt ELSE 0 END ) AS income, ");
                    sql.append("sum( CASE WHEN bad.`CD_SIGN` = '1' THEN bad.amt ELSE 0 END ) AS expenditure ");
                    sql.append("FROM bis_acc_dtl bad  ");
                    sql.append("JOIN bt_bank_acc bba ON bba.bank_acc = bad.bank_acc and bba.status <> -2 ");
                    sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
                    sql.append("where BT_TO_CHAR(bad.trans_time, 'yyyy-MM-dd') >= :startDate ");
                    sql.append("  and BT_TO_CHAR(bad.trans_time, 'yyyy-MM-dd') <= :endDate ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append(" GROUP BY bba.corp_id  ");
        sql.append("  ) iexp ON iexp.corpId = sc.id ");
        sql.append("left join ( select cpr.CORP_ID as corpId,sum(cpr.BUY_MONEY) - sum(BT_NVL_NUM(cprd.redeem_money,0)) as choMoney ");
                    sql.append("from cho_product_req cpr ");
                    sql.append("LEFT JOIN ( SELECT rdd.REQ_ID,sum(rdd.redeem_money) redeem_money ");
                                sql.append("  FROM  cho_product_redeem rd ");
                                sql.append("  JOIN cho_product_redeem_detail rdd ON rd.ID = rdd.CPR_ID ");
                                sql.append("  WHERE rd.`STATUS` = 95 and BT_TO_CHAR(rdd.REDEEM_DATE, 'yyyy-MM-dd') <= :endDate  ");
                                sql.append("  GROUP BY rdd.REQ_ID ");
                                sql.append(") cprd ON cpr.id = cprd.REQ_ID ");
                    sql.append("JOIN bt_bank_acc bba on cpr.bank_acc = bba.bank_acc ");
                    sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
                    sql.append("where cpr.`STATUS` >= 95 AND BT_TO_CHAR(cpr.INTEREST_START_DATE, 'yyyy-MM-dd') <= :endDate ");
                    if (ObjectUtil.isNotEmpty(accGroup)){
                        sql.append("and bba.fc_acc_group_code in (:accGroup) ");
                    }
                    sql.append("group by cpr.corp_id  ");
        sql.append(") cpr1 on cpr1.corpId = sc.id  ");
        sql.append("where sc.`STATUS` = 1 ");
        if (!CollectionUtils.isEmpty(corpIds)){
            sql.append(" and "+getSQLIn(corpIds,900,"sc.id"));
        }
        if (ObjectUtil.isNotEmpty(equityMaintenance)){
            sql.append(" and sc.equityMaintenance_ID in ( :equityMaintenance ) ");
            params.put("equityMaintenance",equityMaintenance.split( ","));
        }

        sql.append("ORDER BY sc.parent_ids ");

        return listBySQLAliasToBean(sql.toString(),params, IncomeExpenditureReportVO.class);
    }

    public List<IncomeExpenditureReportVO> queryProvince(List<String> provinceIds) {
        Map<String,Object> params = new HashMap<>();
        params.put("provinceIds",provinceIds);
        StringBuilder sql = new StringBuilder(128);
        sql.append("SELECT p.id provinceId,p.PROVINCE_NAME provinceName ");
        sql.append("FROM sys_province p ");
        sql.append("WHERE 1=1 ");
        if (!CollectionUtils.isEmpty(provinceIds)){
            sql.append("and "+getSQLIn(provinceIds,900,"p.id"));
        }
        return listBySQLAliasToBean(sql.toString(),params, IncomeExpenditureReportVO.class);
    }
}
