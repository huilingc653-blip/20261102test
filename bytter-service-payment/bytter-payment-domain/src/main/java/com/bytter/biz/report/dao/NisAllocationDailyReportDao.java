package com.bytter.biz.report.dao;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.util.CollectionUtils;
import com.bytter.biz.payment.api.dto.BisExcDTO;
import com.bytter.biz.report.vo.FundDailyReportVO;
import com.bytter.biz.report.vo.IncomeAndExpenditureOfFundsVo;
import com.bytter.biz.report.vo.NisAllocationReportVO;
import com.bytter.biz.report.vo.NisAllocationReqVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.system.api.dto.BtBankAccConstant;
import com.bytter.system.api.dto.CorpDTO;
import com.bytter.system.api.dto.OperationAreaDTO;
import com.bytter.system.api.vo.BtBankAccVO;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: 资金日报表
 * @date 2024-05-14
 */
@Repository
public class NisAllocationDailyReportDao extends BaseDao<BisExcDTO, String> {

	public List<CorpDTO> queryData(List<String> corpIds, Date queryDate, String equityMaintenance,List<String> accGroup) {
		Map<String,Object> params = new HashMap<>();
		params.put("queryDate", DateUtils.formatDate(queryDate));
		// 前一天日期
		Date beforeDate = DateUtils.addDay(queryDate,-1);
		params.put("beforeDate",  DateUtils.formatDate(beforeDate));

		StringBuilder sql = new StringBuilder(128);
		sql.append("select sc.id, sc.name,sc.code as corpCode, cp.id as parentId,sc.PARENT_IDS as parentIds, ");
		sql.append("cp.name as supCorpName, sc.equityMaintenance_ID as equityMaintenanceId, ");
		sql.append("sem.Equity_NAME equityMaintenanceName,cm.cell_code as unitCm,b.maxRate,");
		sql.append("b.minRate,ROUND(b.sumRate / b.accNum, 6) avgRate,sc.province_id as provinceId,");
		sql.append("BT_NVL_NUM(ba.beforeDayBalance,0) beforeDayBalance,");
		sql.append("BT_NVL_NUM(ba1.currentBalance,0) currentBalance, ");
		sql.append("BT_NVL_NUM(ba1.availBal,0) availBal,BT_NVL_NUM(ba1.frzBal,0) frzBal,");
		sql.append("BT_NVL_NUM(cpr1.finaMoney,0) finaMoney ");
		sql.append("from sys_corp sc ");
		sql.append("left join sys_corp cp on sc.parent_id = cp.id  ");
		sql.append("LEFT JOIN sys_cell_manage cm ON sc.UNIT_CM = cm.id ");
		sql.append("LEFT JOIN SYS_EQUITY_MAINTENANCE sem ON sc.equityMaintenance_ID = sem.Equity_code ");
		sql.append("left join ( select bba.corp_id as corpId, max(bba.rates ) maxRate ,MIN(bba.RATES) minRate ,SUM(bba.RATES) sumRate,COUNT(1) accNum  ");
					sql.append("from bt_bank_acc bba ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("where bba.fc_acc_group_code in (:accGroup) ");
						params.put("accGroup",accGroup);
					}
					sql.append("group by bba.corp_id ");
		sql.append(") b on b.corpId = sc.id ");
		sql.append("left join ( select bal.corp_id as corpId, sum(bal.bal) as beforeDayBalance ");
					sql.append("from bis_acc_bal bal ");
					sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :beforeDate ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("and bba.fc_acc_group_code in (:accGroup) ");
					}
					sql.append("group by bal.corp_id  ");
		sql.append(") ba on ba.corpId = sc.id  ");
		sql.append("left join ( select bal.corp_id as corpId, sum(bal.bal) as currentBalance,SUM(bal.AVAIL_BAL) availBal,SUM(bal.FRZ_BAL) frzBal ");
					sql.append("from bis_acc_bal bal ");
					sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :queryDate ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("and bba.fc_acc_group_code in (:accGroup) ");
					}
					sql.append("group by bal.corp_id  ");
		sql.append(") ba1 on ba1.corpId = sc.id  ");
		sql.append("left join ( select cpr.CORP_ID as corpId,sum(cpr.BUY_MONEY) - sum(BT_NVL_NUM(cprd.redeem_money,0)) as finaMoney ");
					sql.append("from cho_product_req cpr ");
					sql.append("left join cho_product_redeem cprd on cpr.id = cprd.req_id ");
					sql.append("JOIN bt_bank_acc bba on cpr.bank_acc = bba.bank_acc ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where BT_TO_CHAR(cpr.DEADLINE_DATE, 'yyyy-MM-dd') = :queryDate  ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("and bba.fc_acc_group_code in (:accGroup) ");
					}
					sql.append("group by cpr.corp_id  ");
		sql.append(") cpr1 on cpr1.corpId = sc.id  ");
		sql.append("where sc.NET_ID = (SELECT id FROM sys_corp WHERE CODE = '100') AND sc.`STATUS` = 1 ");
		if (EmptyUtils.isNotEmpty(equityMaintenance)){
			sql.append(" and sc.equityMaintenance_ID in (:equityMaintenanceList) ");
			String[] strings = equityMaintenance.split(",");
			params.put("equityMaintenanceList", Arrays.asList(strings));
		}
		if (!CollectionUtils.isEmpty(corpIds)){
			sql.append(" and "+getSQLIn(corpIds,900,"sc.id"));
		}

		sql.append("order by cp.code,sc.code ");

		return listBySQLAliasToBean(sql.toString(),params, CorpDTO.class);
	}

	public Map<String, String> querySupCorp() {
		StringBuilder sql = new StringBuilder("select ");
		sql.append("c.id,c.name ");
		sql.append("FROM sys_corp c  ");
		sql.append("JOIN sys_cell_manage m ON c.UNIT_CM = m.id AND m.cell_code = '6000' ");
		sql.append("WHERE c.`STATUS` = 1 AND c.UNIT_ATTRIBUTE = '02' ");
		sql.append("ORDER BY c.sort,c.CODE ");
		List<CorpDTO> list = listBySQLAliasToBean(sql.toString(), null, CorpDTO.class);
		if (ObjectUtil.isNotEmpty(list)){
			Map<String,String> map = new LinkedHashMap<>();
			for (CorpDTO corp : list){
				map.put(corp.getId(),corp.getName());
			}
			return map;
		}else{
			return null;
		}
	}

	public List<FundDailyReportVO> queryMultiLeveData(List<String> corpIds, Date queryDate, List<String> accGroup,String equityMaintenance) {
		Map<String,Object> params = new HashMap<>();
		params.put("queryDate", DateUtils.formatDate(queryDate));
		// 前一天日期
		Date beforeDate = DateUtils.addDay(queryDate,-1);
		params.put("beforeDate",  DateUtils.formatDate(beforeDate));

		StringBuilder sql = new StringBuilder(128);
		sql.append("SELECT sc.id corpId,sc.`CODE` corpCode,sc.`NAME` corpName,sc.PARENT_ID parentId,sc.PARENT_IDS parentIds,sc.sort,sc.report_display reportDisplay,");
		sql.append("sc.province_id provinceId,cm.cell_code cellCode,cm.cell_name cellName,sem.equity_code equityCode,sem.equity_name equityName,");
		sql.append("b.maxRate,b.minRate,ROUND(b.sumRate / b.accNum, 6) avgRate,");
		sql.append("BT_NVL_NUM(ba.beforeDayBalance,0) beforeBal,");
		sql.append("BT_NVL_NUM(ba1.currentBalance,0) currentBal, ");
		sql.append("BT_NVL_NUM(ba1.availBal,0) availBal,BT_NVL_NUM(ba1.frzBal,0) frzBal,");
		sql.append("BT_NVL_NUM(cpr1.choMoney,0) choMoney ");
		sql.append("from sys_corp sc ");
		sql.append("join sys_corp net on sc.NET_ID = net.ID AND net.`CODE` = '100'  ");
		sql.append("LEFT JOIN sys_cell_manage cm ON sc.UNIT_CM = cm.id  ");
		sql.append("JOIN SYS_EQUITY_MAINTENANCE sem ON sc.equityMaintenance_ID = sem.Equity_code AND sem.Equity_code <> '0003' ");
		sql.append("left join ( select bba.corp_id as corpId, max(bba.rates ) maxRate ,MIN(bba.RATES) minRate ,SUM(bba.RATES) sumRate,COUNT(1) accNum  ");
					sql.append("from bt_bank_acc bba ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where bba.status <> -2 ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append(" and bba.fc_acc_group_code in (:accGroup) ");
						params.put("accGroup",accGroup);
					}
					sql.append("group by bba.corp_id ");
		sql.append(") b on b.corpId = sc.id ");
		sql.append("left join ( select bal.corp_id as corpId, sum(bal.bal) as beforeDayBalance ");
					sql.append("from bis_acc_bal bal ");
					sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc and bba.status <> -2 ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :beforeDate ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("and bba.fc_acc_group_code in (:accGroup) ");
					}
					sql.append("group by bal.corp_id  ");
		sql.append(") ba on ba.corpId = sc.id  ");
		sql.append("left join ( select bal.corp_id as corpId, sum(bal.bal) as currentBalance,SUM(bal.AVAIL_BAL) availBal,SUM(bal.FRZ_BAL) frzBal ");
					sql.append("from bis_acc_bal bal ");
					sql.append("JOIN bt_bank_acc bba on bal.bank_acc = bba.bank_acc  and bba.status <> -2 ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where BT_TO_CHAR(bal.bal_date, 'yyyy-MM-dd') = :queryDate ");
					if (ObjectUtil.isNotEmpty(accGroup)){
						sql.append("and bba.fc_acc_group_code in (:accGroup) ");
					}
					sql.append("group by bal.corp_id  ");
		sql.append(") ba1 on ba1.corpId = sc.id  ");
		sql.append("left join ( select cpr.CORP_ID as corpId,sum(cpr.BUY_MONEY) - sum(BT_NVL_NUM(cprd.redeem_money,0)) as choMoney ");
					sql.append("from cho_product_req cpr ");
					sql.append("LEFT JOIN ( SELECT rdd.REQ_ID,sum(rdd.redeem_money) redeem_money ");
								sql.append("  FROM  cho_product_redeem rd ");
								sql.append("  JOIN cho_product_redeem_detail rdd ON rd.ID = rdd.CPR_ID ");
								sql.append("  WHERE rd.`STATUS` = 95 and BT_TO_CHAR(rdd.REDEEM_DATE, 'yyyy-MM-dd') <= :queryDate  ");
								sql.append("  GROUP BY rdd.REQ_ID ");
					sql.append(") cprd ON cpr.id = cprd.REQ_ID ");
					sql.append("JOIN bt_bank_acc bba on cpr.bank_acc = bba.bank_acc ");
					sql.append("JOIN bt_bank_type bt ON bba.BANK_TYPE_ID = bt.id  AND bt.BANK_TYPE <> '8500' ");
					sql.append("where cpr.`STATUS` >= 95 AND BT_TO_CHAR(cpr.INTEREST_START_DATE, 'yyyy-MM-dd') <= :queryDate ");
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
			params.put("equityMaintenance",equityMaintenance.split(","));
		}

		sql.append("ORDER BY sc.parent_ids ");

		return listBySQLAliasToBean(sql.toString(),params, FundDailyReportVO.class);
	}

	public List<FundDailyReportVO> queryProvince(List<String> provinceIds) {
		Map<String,Object> params = new HashMap<>();
		params.put("provinceIds",provinceIds);
		StringBuilder sql = new StringBuilder(128);
		sql.append("SELECT p.id provinceId,p.PROVINCE_NAME provinceName ");
		sql.append("FROM sys_province p ");
		sql.append("WHERE 1=1 ");
		if (!CollectionUtils.isEmpty(provinceIds)){
			sql.append("and "+getSQLIn(provinceIds,900,"p.id"));
		}
		return listBySQLAliasToBean(sql.toString(),params, FundDailyReportVO.class);
	}
}
