package com.bytter.biz.report.dao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.biz.budget.vo.FbsBudgetVO;
import com.bytter.biz.report.vo.FbsBudgetDetailReportVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.constant.BudgetConstant;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 资金计划上报报表
 *
 * @author zuoy 2018-02-06
 */
@Repository
public class FbsBudgetReportDao extends BaseDao<FbsBudget, String> {
	/**
	 * 根据查询条件查询预算信息
	 *
	 * @param timeType  类型
	 * @param corpId    单位ID
	 * @param beginDate 开始时间
	 * @param endDate   结束时间
	 * @return
	 */
	public List<FbsBudgetVO> listFbsBudget(String timeType, String corpId, Date beginDate, Date endDate) {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder();
		hql.append(" select b.id as id, b.corpId as corpId, b.enrolDate as enrolDate, b.beginDate as beginDate, b.corpName as corpName,b.curName as curName,");
		hql.append(" b.endDate as endDate, b.timeType as timeType, b.checkMoney as checkMoney, b.status as status,b.curId as curId,b.exchangeRate as exchangeRate from FbsBudget b ");
		//hql.append(" left join Corp c on b.corpId = c.id");
		hql.append(" where b.timeType = :timeType  and b.beginDate >= :beginDate and b.endDate <= :endDate ");
		params.put("timeType", timeType);
		if (corpId.indexOf("in") > -1) {
			hql.append(" and ").append(corpId);
		} else {
			hql.append(" and b.corpId = :corpId ");
			params.put("corpId", corpId);
		}
		params.put("beginDate", DateUtils.parseDate(beginDate));
		params.put("endDate", DateUtils.parseDate(endDate));
		hql.append(" order by b.corpCode");
		return this.listByHQLAliasToBean(hql.toString(), params, FbsBudgetVO.class);
	}
	/**
	 * 查询列表
	 *
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableReport(DataTablesInput dataTablesInput,String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select fbs.id as id, fbs.corp_id as corpId,fbs.corp_name as corpName, fbs.cur_name as curName,fbs.enrol_date as enrolDate, fbs.begin_date as beginDate,");
		sql.append(" fbs.end_date as endDate, fbs.time_type as timeType, fbs.status as status,fbs.cur_id as curId ");
		sql.append(" from fbs_budget fbs ");
		sql.append(" where fbs.corp_name is not null ");
		if(EmptyUtils.isNotEmpty(joinSql)){
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetVO.class, "fbs", false);
	}
	/**
	 * 根据条件获取预算汇总金额
	 *
	 * @param timeType  预算类型
	 * @param budgetIds 预算主表ID
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> getCollect(String timeType, String budgetIds) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.item_code as itemCode, sum(t.b_money) as bMoney ").append(" from ").append(tableName).append(" t ");
		sql.append(" where t.item_code is not null");
		sql.append(" and ").append(budgetIds);
		sql.append(" group by t.item_code order by t.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据条件获取合并预算汇总金额
	 *
	 * @param timeType  预算类型
	 * @param budgetIds 预算主表ID
	 * @param moneyMap 合并金额类型
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> getMergeCollect(String timeType, String budgetIds, Map<String, String> moneyMap) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.item_code as itemCode,sum(0 ");
		if(moneyMap.containsKey("1")){
			sql.append(" + (t.b_money*f.exchange_Rate) ");
		}
		if(moneyMap.containsKey("2")){
			sql.append(" + (t.bsuper_money*f.exchange_Rate) ");
		}
		if(moneyMap.containsKey("3")){
			sql.append(" + (t.btemp_money*f.exchange_Rate)");
		}
		sql.append(" ) as bMoney ");
		sql.append(" from ").append(tableName).append(" t ");
		sql.append(" left join fbs_budget f on t.budget_id = f.id");
		sql.append(" where t.item_code is not null");
		sql.append(" and ").append(budgetIds);
		sql.append(" group by t.item_code order by t.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据条件获取合并预算汇总金额
	 *
	 * @param timeType  预算类型
	 * @param budgetIds 预算主表ID
	 * @param moneyMap 合并金额类型
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> getMergeResolveCollect(String timeType, String budgetIds, Map<String, String> moneyMap) {
		String tableName = FbsBudgetDtlAbs.getTableNameByTimeType(timeType);
		StringBuilder sql = new StringBuilder();
		sql.append("select t.item_code as itemCode,sum(0 ");
		if(moneyMap.containsKey("1")){
			sql.append(" + t.b_money*b.exchange_rate ");
		}
		if(moneyMap.containsKey("2")){
			sql.append(" + t.bsuper_money*b.exchange_rate ");
		}
		if(moneyMap.containsKey("3")){
			sql.append(" + t.btemp_money*b.exchange_rate ");
		}
		sql.append(" ) as bMoney");
		if (BudgetConstant.TIME_TYPE_YEAR.equalsIgnoreCase(timeType)) {
			sql.append(" from fbs_budget_resolve_year t ");
		}else {
			sql.append(" from fbs_budget_resolve_month t ");
		}
		sql.append(" left join fbs_budget b on t.budget_id=b.id");
		sql.append(" where t.item_code is not null");
		sql.append(" and ").append(budgetIds);
		sql.append(" group by t.item_code order by t.item_code ");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 计划明细查询列表
	 * TODO 后续需要同步数据 fbs_budget --> 增加字段：corp_name，cur_name
	 * @param dataTablesInput
	 * @return
	 */
	public DataTablesOutput dataTableDetailReport(DataTablesInput dataTablesInput, String tableName, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select b.corp_id as corpId,b.corp_name as corpName,d.item_code as itemCode,d.item_name as itemName,i.id as itemId,(bt_nvl(d.b_money,0)+bt_nvl(d.bsuper_money,0)) as bMoney,");
		sql.append(" b.cur_id as curId,b.cur_name as curName,d.settl_type as settlType,d.corp_name as deptName,d.purpose as purpose,");
		sql.append(" d.wish_payday as wishPayday,d.payer_acc as payerAcc,d.payee_acc_name as payeeAccName,d.payee_acc as payeeAcc,");
		sql.append(" d.payee_bank as payeeBank, d.contract as contract,d.vesting_date as vestingDate");
		sql.append(" from fbs_budget_detail d left join "+tableName+" m on d.detail_id=m.id left join fbs_budget b on b.id=m.budget_id ");
		sql.append(" left join fbs_item i on d.item_code=i.item_code");
		sql.append(" where 1 = 1 ");
		if (StringUtils.isNotEmpty(joinSql)) {
			sql.append(joinSql);
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), FbsBudgetDetailReportVO.class, "d", false);
	}
}
