package com.bytter.biz.report.dao;
import com.bytter.biz.report.vo.FundsBalanceStatisticsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import com.bytter.core.constant.CommonConstant;
import com.bytter.system.api.constant.BtCurrencyConstant;
import com.bytter.system.api.dto.BtCurrencyDTO;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @author zuoy
 * @Description 资金余额统计表
 */
@Repository
public class FundsBalanceStatisticsDao extends BaseDao<FundsBalanceStatisticsVO, String> {
	/**
	 * 资金余额统计列表
	 * @param input
	 * @param joinSql
	 * @return
	 */
	public DataTablesOutput getFundsBalanceStatisticsDtl(DataTablesInput input,String joinSql){
		String startDate = (String)input.getFormdata().get("ged_queryDate");
		String endDate = (String)input.getFormdata().get("led_queryDate");
		StringBuilder sql = new StringBuilder();
		sql.append("select corpId,corpName,curId,curName,accType,balance from ( ");
		sql.append(" select a.corp_id as corpId,s.name as corpName,a.cur_id as curId,cur.cur_name as curName,");
		sql.append(" b.sys_subject_code as accType,sum(nvl(c.check_balance, 0)) as balance");
		sql.append(" from fc_acc a, sys_corp s, bt_currency cur, fc_subject b, fc_online_ledger c ");
		sql.append(" where a.subject_id = b.id and a.blnctr_acc = c.account and a.corp_id = s.id and a.cur_id = cur.id");
		sql.append(" and b.sys_subject_code in ('1010', '2120', '2130', '2140') ");
		sql.append(" and a.acc_state not in ('T', 'W', 'B', 'D') ");//过滤掉开户暂存，开户待复核，开户复核打回，注销
		sql.append(" group by a.corp_id, s.name, a.cur_id,cur.cur_name, b.sys_subject_code");
		sql.append(" union all ");
		sql.append(" select t.corpId, t.corpName, t.curId, t.curName, t.accType, sum(ba.bal) from ( ");
		sql.append(" select a.corp_id as corpId, a.bank_acc as bankAcc, 	s.name as corpName, b.cur_id as curId,");
		sql.append(" cur.cur_name as curName,a.acc_type as accType,max(b.bal_date) as balDate");
		sql.append(" from bt_bank_acc a, sys_corp s, bis_acc_bal b, bt_currency cur");
		sql.append(" where a.bank_acc = b.bank_acc and b.cur_id = cur.id and a.corp_id = s.id ");
		sql.append(" and a.valid_sign = '1' and a.status <> -2");//过滤状态不为删除的和必须是有效的
		sql.append(" and b.bal_date >= bt_to_date(:startDate, 'yyyy-MM-dd')");
		sql.append(" and b.bal_date <= bt_to_date(:endDate, 'yyyy-MM-dd')");
		sql.append(" group by a.corp_id,a.bank_acc,s.name,b.cur_id,cur.cur_name,a.acc_type) t");
		sql.append(" left join bis_acc_bal ba on t.bankAcc = ba.bank_acc and t.curId = ba.cur_id and t.balDate = ba.bal_date");
		sql.append(" group by t.corpId, t.corpName, t.curId, t.curName, t.accType");
		sql.append(" ) f  where 1=1 ");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append("  and ");
			sql.append(joinSql);
		}
		HashMap<String,Object> params = new HashMap<>();
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		return getDynamicBySQL(input, sql.toString(), FundsBalanceStatisticsVO.class, "f", false,params);
	}

	//TODO SYS服务提供
	public List<BtCurrencyDTO> getBtCurrency() {
		Map<String, Object> params = new HashMap<>();
		String hql = " from BtCurrency where validSign = :validSign order by naturalSign desc,curCode ";
		params.put("validSign", BtCurrencyConstant.VALID_SIGN_YES);
		return listByHQL(hql, params, true);
	}
	
	//TODO SYS服务提供
	public List<FundsBalanceStatisticsVO> getData(String curId, String corpId,String startDate,String endDate,Integer unit, String userId,String joinSql){
		/*StringBuilder sql = new StringBuilder();
		sql.append("select corpId as corpId,corpName,curId as curId,curName,accType,balance/"+unit+" as balance from ( ");
		sql.append(" select a.corp_id as corpId,s.name as corpName,a.cur_id as curId,cur.cur_name as curName,");
		sql.append(" b.sys_subject_code as accType,sum(nvl(c.check_balance, 0)) as balance");
		sql.append(" from fc_acc a, sys_corp s, bt_currency cur, fc_subject b, fc_online_ledger c ");
		sql.append(" where a.subject_id = b.id and a.blnctr_acc = c.account and a.corp_id = s.id and a.cur_id = cur.id");
		sql.append(" and b.sys_subject_code in ('1010', '2120', '2130', '2140') ");
		sql.append(" and a.acc_state not in ('T', 'W', 'B', 'D') ");//过滤掉开户暂存，开户待复核，开户复核打回，注销
		sql.append(" and a.id in (select DISTINCT account_id from SYS_ACCOUNT_GRANT where user_id = :userId) ");
		sql.append(" group by a.corp_id, s.name, a.cur_id,cur.cur_name, b.sys_subject_code");
		sql.append(" union all ");
		sql.append(" select t.corpId, t.corpName, t.curId, t.curName, t.accType, sum(ba.bal) from ( ");
		sql.append(" select a.corp_id as corpId, a.bank_acc as bankAcc, 	s.name as corpName, b.cur_id as curId,");
		sql.append(" cur.cur_name as curName,a.acc_type as accType,max(b.bal_date) as balDate");
		sql.append(" from bt_bank_acc a, sys_corp s, bis_acc_bal b, bt_currency cur");
		sql.append(" where a.bank_acc = b.bank_acc and b.cur_id = cur.id and a.corp_id = s.id ");
		sql.append(" and a.valid_sign = '1' and a.status <> -2");//过滤状态不为删除的和必须是有效的
		sql.append(" and a.id in (select DISTINCT account_id from SYS_ACCOUNT_GRANT where user_id = :userId) ");
		sql.append(" and b.bal_date > bt_to_date(:startDate, 'yyyy-MM-dd')");
		sql.append(" and b.bal_date <= bt_to_date(:endDate, 'yyyy-MM-dd')");
		if(StringUtils.isNotBlank(corpId)){
//			sql.append(" and s.id = :corpId");
			sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "s.id", true));
		}
		if(StringUtils.isNotBlank(curId)){
			sql.append(" and cur.id = :curId");
		}
		sql.append(" group by a.corp_id,a.bank_acc,s.name,b.cur_id,cur.cur_name,a.acc_type) t");
		sql.append(" left join bis_acc_bal ba on t.bankAcc = ba.bank_acc and t.curId = ba.cur_id and t.balDate = ba.bal_date");
		sql.append(" group by t.corpId, t.corpName, t.curId, t.curName, t.accType");
		sql.append(" ) f  where 1=1 ");
		if(StringUtils.isNotBlank(corpId)){
//			sql.append(" and f.corpId in (:corpId) ");
			sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "f.corpId", true));
		}
		if(StringUtils.isNotBlank(curId)){
			sql.append(" and f.curId = :curId  ");
		}
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(" and ");
			sql.append(joinSql);
		}
		sql.append(" order by f.corpName desc ");
		HashMap<String,Object> params = new HashMap<>();
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		params.put("userId",userId);
		if(StringUtils.isNotBlank(corpId)){
			params.put("corpId",corpId);
		}
		if(StringUtils.isNotBlank(curId)){
			params.put("curId",curId);
		}*/
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select ");
		sql.append(" f.corpId as corpId, ");
		sql.append(" f.curId as curId, ");
		sql.append(" c.name as corpName, ");
		sql.append(" cur.cur_name as curName, ");
		sql.append(" f.accType as accType, ");
//		sql.append(" f.balance /").append(unit).append(" as balance");
		sql.append(" f.balance as balance");
		sql.append(" from ");
		sql.append(" ( ");
		sql.append(" select ");
		sql.append(" t1.corpId, ");
		sql.append(" t1.curId, ");
		sql.append(" t1.accType, ");
		sql.append(" t1.balance ");
		sql.append(" from ");
		sql.append(" ( ");
		sql.append(" select ");
		sql.append(" t.corpId, ");
		sql.append(" t.curId, ");
		sql.append(" t.accType, ");
		sql.append(" sum(BT_NVL_NUM(ba.balance, 0)) as balance ");
		sql.append(" from ");
		sql.append(" ( ");
		sql.append(" select ");
		sql.append(" a.corp_id as corpId, ");
		sql.append(" a.blnctr_acc as bankAcc, ");
		sql.append(" a.cur_id as curId, ");
		sql.append(" b.sys_subject_code as accType, ");
		sql.append(" max(c.balance_date) as balDate ");
		sql.append(" from ");
		sql.append(" fc_acc a, ");
		sql.append(" fc_subject b, ");
		sql.append(" fc_acc_balance c ");
		sql.append(" where ");
		sql.append(" a.subject_id = b.id ");
		sql.append(" and a.blnctr_acc = c.account ");
		sql.append(" and b.sys_subject_code in ('1010','2120','2130','2140') ");
		sql.append(" and a.acc_state not in ('T','W','B','D') ");
//		sql.append(" and a.id in (select DISTINCT account_id from SYS_ACCOUNT_GRANT where user_id = :userId) ");
		sql.append(" and a.corp_id in (select CORP_ID from SYS_CORP_USER_RESOURCE where USER_ID  = :userId) ");
		sql.append(" and c.balance_date > bt_to_date(:startDate,'yyyy-MM-dd') ");
		sql.append(" and c.balance_date <= bt_to_date(:endDate,'yyyy-MM-dd') ");
		sql.append(" group by ");
		sql.append(" a.corp_id, ");
		sql.append(" a.blnctr_acc, ");
		sql.append(" a.cur_id, ");
		sql.append(" b.sys_subject_code) t ");
		sql.append(" left join fc_acc_balance ba on ");
		sql.append(" t.bankAcc = ba.account ");
		sql.append(" and t.curId = ba.cur_id ");
		sql.append(" and t.balDate = ba.balance_date ");
		sql.append(" where ");
		sql.append(" t.curId is not null ");
		sql.append(" group by ");
		sql.append(" t.corpId, ");
		sql.append(" t.curId, ");
		sql.append(" t.accType) t1 ");
		sql.append(" group by ");
		sql.append(" t1.corpId, ");
		sql.append(" t1.curId, ");
		sql.append(" t1.accType, ");
		sql.append(" t1.balance");
		sql.append(" union all ");

		sql.append(" select ");
		sql.append(" t1.corpId, ");
		sql.append(" t1.curId, ");
		sql.append(" t1.accType, ");
		sql.append(" t1.bal ");
		sql.append(" from ");
		sql.append(" ( ");
		sql.append(" select ");
		sql.append(" t.corpId, ");
		sql.append(" t.curId, ");
		sql.append(" t.accType, ");
		sql.append(" sum(ba.bal) bal ");
		sql.append(" from ");
		sql.append(" ( ");
		sql.append(" select ");
		sql.append(" a.corp_id as corpId, ");
		sql.append(" a.bank_acc as bankAcc, ");
		sql.append(" b.cur_id as curId, ");
		sql.append(" a.acc_type as accType, ");
		sql.append(" max(b.bal_date) as balDate ");
		sql.append(" from ");
		sql.append(" bt_bank_acc a, ");
		sql.append(" bis_acc_bal b ");
		sql.append(" where ");
		sql.append(" a.bank_acc = b.bank_acc ");
		sql.append(" and a.valid_sign = '1' ");
		sql.append(" and a.status <> -2 ");
//		sql.append(" and a.id in (select DISTINCT account_id from SYS_ACCOUNT_GRANT where user_id = :userId) ");
		sql.append(" and a.corp_id in (select CORP_ID from SYS_CORP_USER_RESOURCE where USER_ID  = :userId) ");
		sql.append(" and b.bal_date > bt_to_date(:startDate,'yyyy-MM-dd') ");
		sql.append(" and b.bal_date <= bt_to_date(:endDate,'yyyy-MM-dd') ");
		sql.append(" group by ");
		sql.append(" a.corp_id, ");
		sql.append(" a.bank_acc, ");
		sql.append(" b.cur_id, ");
		sql.append(" a.acc_type) t ");
		sql.append(" left join bis_acc_bal ba on ");
		sql.append(" t.bankAcc = ba.bank_acc ");
		sql.append(" and t.curId = ba.cur_id ");
		sql.append(" and t.balDate = ba.bal_date ");
		sql.append(" where ");
		sql.append(" t.curId is not null ");
		sql.append(" group by ");
		sql.append(" t.corpId, ");
		sql.append(" t.curId, ");
		sql.append(" t.accType) t1 ");
		sql.append(" group by ");
		sql.append(" t1.corpId, ");
		sql.append(" t1.accType, ");
		sql.append(" t1.curId ) f ");
		sql.append(" left join sys_corp c on ");
		sql.append(" f.corpId = c.id ");
		sql.append(" left join bt_currency cur on ");
		sql.append(" f.curId = cur.id ");
		sql.append(" where ");
		sql.append(" 1 = 1 ");
		if(StringUtils.isNotBlank(curId)){
			sql.append(" and f.curId = :curId ");
			params.put("curId",curId);
		}
		if(StringUtils.isNotBlank(corpId)){
			sql.append(" and f.corpId = :corpId ");
			params.put("corpId",corpId);
		}
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(" and ");
			sql.append(joinSql);
		}
		sql.append(" group by ");
		sql.append(" f.corpId, ");
		sql.append(" f.curId, ");
		sql.append(" c.name, ");
		sql.append(" cur.cur_name, ");
		sql.append(" f.accType, ");
		sql.append(" f.balance ");
		sql.append(" order by ");
		sql.append(" c.name desc ");
		params.put("userId",userId);
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		return listBySQLAliasToBean(sql.toString(), params, FundsBalanceStatisticsVO.class);
	}
	/**
	 * 资金余额统计列表
	 * @param input
	 * @param joinSql
	 * @return
	 */
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,String joinSql){
		String startDate = (String)input.getFormdata().get("ged_queryDate");
		String endDate = (String)input.getFormdata().get("led_queryDate");
		String curId = (String) input.getFormdata().get("curId");
		StringBuilder sql = new StringBuilder();
		sql.append("select f.corpId as corpId, f.curId as curId,sum(f.accType01) as accType01,sum(f.accType02) as accType02, ");
		sql.append("sum(f.accType03) as accType03,sum(f.accType04) as accType04,sum(f.accType05) as accType05,sum(f.accType06) as accType06, ");
		sql.append(" sum(f.accType07) as accType07,sum(f.accType08) as accType08,sum(f.accType09) as accType09,sum(f.accType10) as accType10, ");
		sql.append(" sum(f.accType11) as accType11,sum(f.accType12) as accType12,sum(f.accType13) as accType13,sum(f.accType14) as accType14, ");
		sql.append(" sum(BT_NVL_NUM(f.accType11,0) + BT_NVL_NUM(f.accType12,0) + BT_NVL_NUM(f.accType13,0) + BT_NVL_NUM(f.accType14,0)) fcTotal,");
		sql.append(" sum(BT_NVL_NUM(f.accType02,0) + BT_NVL_NUM(f.accType01,0) + BT_NVL_NUM(f.accType09,0) + BT_NVL_NUM(f.accType07,0) + ");
		sql.append(" BT_NVL_NUM(f.accType06,0) + BT_NVL_NUM(f.accType05,0) + BT_NVL_NUM ( f.accType10, 0 ) +BT_NVL_NUM(f.accType04,0) + BT_NVL_NUM(f.accType03,0)) bankTotal");
		sql.append(" from ( ");
		sql.append("select t1.corpId, t1.curId,  ");
		sql.append(" 0 AccType01, 0 AccType02, 0 AccType03, 0 AccType04, 0 AccType05, 0 AccType06, 0 AccType07, 0 AccType08, 0 AccType09, 0 AccType10,");
		sql.append(" max(case when t1.accType ='1010' then t1.balance end) AccType11, max(case when t1.accType ='2120' then t1.balance end) AccType12, ");
		sql.append(" max(case when t1.accType ='2130' then t1.balance end) AccType13, max(case when t1.accType ='2140' then t1.balance end) AccType14 ");
		sql.append(" from ( ");
		sql.append(" select a.corp_id as corpId, a.cur_id as curId, b.sys_subject_code as accType, sum(BT_NVL_NUM(c.check_balance, 0)) as balance");
		sql.append(" from fc_acc a, fc_subject b, fc_online_ledger c  where a.subject_id = b.id");
		sql.append(" and a.blnctr_acc = c.account and b.sys_subject_code in ('1010', '2120', '2130', '2140') ");
		sql.append(" and a.acc_state not in ('T', 'W', 'B', 'D') ");//过滤掉开户暂存，开户待复核，开户复核打回，注销
		sql.append(" group by a.corp_id,a.cur_id, b.sys_subject_code) t1  group by t1.corpId, t1.curId");
		sql.append(" union all ");
		sql.append(" select t1.corpId,t1.curId,");
		sql.append(" max(case when t1.accType='01' then t1.bal end) AccType01, max(case when t1.accType ='02' then  t1.bal end) AccType02, ");
		sql.append(" max(case when t1.accType ='03' then t1.bal end) AccType03, max(case when t1.accType ='04' then  t1.bal end) AccType04, ");
		sql.append(" max(case when t1.accType ='05' then t1.bal end) AccType05, max(case when t1.accType ='06' then  t1.bal end) AccType06, ");
		sql.append(" max(case when t1.accType ='07' then t1.bal end) AccType07, max(case when t1.accType ='08' then  t1.bal end) AccType08, ");
		sql.append(" max(case when t1.accType ='09' then t1.bal end) AccType09, max(case when t1.accType ='10' then  t1.bal end) AccType10, ");
		sql.append(" 0 AccType11, 0 AccType12, 0 AccType13, 0 AccType14");
		sql.append(" from ( ");
		sql.append(" select t.corpId, t.curId, t.accType, sum(ba.bal) bal from ( ");
		sql.append(" select a.corp_id as corpId, a.bank_acc as bankAcc, b.cur_id as curId, a.acc_type as accType, max(b.bal_date) as balDate ");
		sql.append(" from bt_bank_acc a, bis_acc_bal b where a.bank_acc = b.bank_acc and a.valid_sign = '1' and a.status <> -2 ");//过滤状态不为删除的和必须是有效的
		sql.append(" and b.bal_date >= bt_to_date(:startDate, 'yyyy-MM-dd') and b.bal_date <= bt_to_date(:endDate, 'yyyy-MM-dd') ");
		sql.append(" group by a.corp_id, a.bank_acc, b.cur_id, a.acc_type) t ");
		sql.append(" left join bis_acc_bal ba on t.bankAcc = ba.bank_acc and t.curId = ba.cur_id and t.balDate = ba.bal_date ");
		sql.append(" where t.curId is not null group by t.corpId, t.curId,t.accType) t1 ");
		sql.append(" group by t1.corpId,t1.curId ");
		sql.append(" ) f where 1 = 1 ");
		if(StringUtils.isNotEmpty(joinSql)){
			sql.append(" and ");
			sql.append(joinSql);
		}
		HashMap<String,Object> params = new HashMap<>();
		if (StringUtils.isNotEmpty(curId)){
			sql.append(" and f.curId = :curId ");
			params.put("curId",curId);
		}
		params.put("startDate",startDate);
		params.put("endDate",endDate);
		sql.append("  group by f.corpId, f.curId");
		return getDynamicBySQL(input, sql.toString(), FundsBalanceStatisticsVO.class, "f", false,params);
	}
	/**
	 * 资金余额统计列表
	 * @param input
	 * @return
	 */
	public DataTablesOutput getDynamicDataTablesUnion(DataTablesInput input,StringBuilder sql){
		String endDate = (String)input.getFormdata().get("led_queryDate");
		String curId = (String) input.getFormdata().get("curId");
		HashMap<String,Object> params = new HashMap<>();
		if (StringUtils.isNotEmpty(curId)){
			sql.append(" and f.curId = :curId ");
			params.put("curId",curId);
		}
		params.put("endDate",endDate);
		sql.append(" group by f.corpId, f.curId,c.name,cur.cur_name");
		sql.append(" order by c.name desc");
		input.getFormdata().remove("led_queryDate");
		return this.getDynamicMapBySQL(input, sql.toString(), "f", false,params);
	}
}
