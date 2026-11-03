package com.bytter.report.domain.netbank.dao;
import com.bytter.core.base.BaseDao;
import com.bytter.core.constant.CommonConstant;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.report.domain.netbank.vo.BankAccBalTrendVO;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 银行存款余额趋势Dao
 *
 * @author zuoy 2018-12-05
 */
@Repository
public class BankAccBalTrendDao extends BaseDao<BankAccBalTrendVO, String> {
	/**
	 * 查询数据
	 *
	 * @param corpId     单位ID
	 * @param curId      币别ID
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 * @param curBalDate 当前余额日期
	 * @return
	 */
	public List<BankAccBalTrendVO> listBankAccBalTrendVO(String corpId, String curId, Date beginDate, Date endDate, Date curBalDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select b.corp_id, last_day(b.bal_date) balDate, bt_to_char(b.bal_date, 'MM') month, b.cur_id, bt_nvl_num(sum(b.avail_bal), 0) bal from bis_acc_bal b ");
		sql.append(" where b.status <> -2 and b.bal_date >= :beginDate and b.bal_date <= :endDate and b.bal_date = ( ");
		sql.append("		select max(bal.bal_date) from bis_acc_bal bal where  bal.corp_id = b.corp_id and bal.cur_id = b.cur_id and ( ");
		sql.append("			(bt_to_char(b.bal_date, 'yyyy-MM') <> bt_to_char(:curBalDate, 'yyyy-MM') and bt_to_char(bal.bal_date, 'yyyy-MM') = bt_to_char(b.bal_date, 'yyyy-MM')) ");
		sql.append("			or (bt_to_char(b.bal_date, 'yyyy-MM') = bt_to_char(:curBalDate, 'yyyy-MM') and bal.bal_date <= :curBalDate)   ");
		sql.append("    )) ");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		params.put("curBalDate", curBalDate);
		if (!EmptyUtils.isBlank(corpId)) {
			sql.append(" and ").append(SqlUtil.getInSql(corpId.split(CommonConstant.COMMA_SPLIT), "b.corp_id", true));
		}
		if (!EmptyUtils.isBlank(curId)) {
			sql.append(" and b.cur_id = :curId ");
			params.put("curId", curId);
		}
		sql.append(" group by b.corp_id, last_day(b.bal_date), bt_to_char(b.bal_date, 'MM'), b.cur_id ");
		sql.append(" order by b.corp_id, last_day(b.bal_date), b.cur_id  ");
		return listBySQLAliasToBean(sql.toString(), params, BankAccBalTrendVO.class);
	}
	/**
	 * 查询数据
	 *
	 * @param beginDate  开始日期
	 * @param endDate    结束日期
	 *
	 * @return
	 */
	public Map<String, BigDecimal> collectFbsBudgetResolveYear(Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> params = new HashMap<>();
		sql.append(" select fbs.corp_id, bt_nvl_num(sum(t.b_money_1), 0 ) + bt_nvl_num(sum(t.bsuper_money_1), 0) as budgetMoney1, bt_nvl_num(sum(t.b_money_2), 0 ) + bt_nvl_num(sum(t.bsuper_money_2), 0) as budgetMoney2");
		sql.append(" , bt_nvl_num(sum(t.b_money_3), 0 ) + bt_nvl_num(sum(t.bsuper_money_3), 0) as budgetMoney3, bt_nvl_num(sum(t.b_money_4), 0 ) + bt_nvl_num(sum(t.bsuper_money_4), 0) as budgetMoney4");
		sql.append(" , bt_nvl_num(sum(t.b_money_5), 0 ) + bt_nvl_num(sum(t.bsuper_money_5), 0) as budgetMoney5, bt_nvl_num(sum(t.b_money_6), 0 ) + bt_nvl_num(sum(t.bsuper_money_6), 0) as budgetMoney6");
		sql.append(" , bt_nvl_num(sum(t.b_money_7), 0 ) + bt_nvl_num(sum(t.bsuper_money_7), 0) as budgetMoney7, bt_nvl_num(sum(t.b_money_8), 0 ) + bt_nvl_num(sum(t.bsuper_money_8), 0) as budgetMoney8");
		sql.append(" , bt_nvl_num(sum(t.b_money_9), 0 ) + bt_nvl_num(sum(t.bsuper_money_9), 0) as budgetMoney9, bt_nvl_num(sum(t.b_money_10), 0 ) + bt_nvl_num(sum(t.bsuper_money_10), 0) as budgetMoney10");
		sql.append(" , bt_nvl_num(sum(t.b_money_11), 0 ) + bt_nvl_num(sum(t.bsuper_money_11), 0) as budgetMoney11, bt_nvl_num(sum(t.b_money_12), 0 ) + bt_nvl_num(sum(t.bsuper_money_12), 0) as budgetMoney12");
		sql.append("  from fbs_budget_resolve_year t left join fbs_budget fbs on t.budget_id = fbs.id");
		// 查询年分解余额，已审批完成，期末科目700
		sql.append(" where fbs.status >= 95 and fbs.time_type = 1 and t.item_code='700' ");
		sql.append(" and fbs.begin_date = :beginDate and fbs.end_date = :endDate ");
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		sql.append(" group by fbs.corp_id ");
		List<Map<String, Object>> list = this.listBySQL(sql.toString(), params);
		if(EmptyUtils.isEmptyList(list)){
			return null;
		}
		Map<String, BigDecimal> collectMap = new HashMap<>();
		for(Map<String, Object> map : list){
			String corpId = map.get("CORP_ID").toString();
			collectMap.put(corpId + "_01", new BigDecimal(map.get("BUDGETMONEY1").toString()));
			collectMap.put(corpId + "_02", new BigDecimal(map.get("BUDGETMONEY2").toString()));
			collectMap.put(corpId + "_03", new BigDecimal(map.get("BUDGETMONEY3").toString()));
			collectMap.put(corpId + "_04", new BigDecimal(map.get("BUDGETMONEY4").toString()));
			collectMap.put(corpId + "_05", new BigDecimal(map.get("BUDGETMONEY5").toString()));
			collectMap.put(corpId + "_06", new BigDecimal(map.get("BUDGETMONEY6").toString()));
			collectMap.put(corpId + "_07", new BigDecimal(map.get("BUDGETMONEY7").toString()));
			collectMap.put(corpId + "_08", new BigDecimal(map.get("BUDGETMONEY8").toString()));
			collectMap.put(corpId + "_09", new BigDecimal(map.get("BUDGETMONEY9").toString()));
			collectMap.put(corpId + "_10", new BigDecimal(map.get("BUDGETMONEY10").toString()));
			collectMap.put(corpId + "_11", new BigDecimal(map.get("BUDGETMONEY11").toString()));
			collectMap.put(corpId + "_12", new BigDecimal(map.get("BUDGETMONEY12").toString()));
		}
		return collectMap;
	}
}
