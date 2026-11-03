package com.bytter.biz.budget.dao;
import java.math.BigDecimal;
import java.util.*;
import cn.hutool.core.collection.CollectionUtil;
import com.bytter.core.constant.BudgetConstant;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsBudgetDtlAbs;
import com.bytter.biz.budget.vo.FbsBudgetDtlAbsVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FBS_BUDGET_DTL_ABS
 * @date 2017-07-18 11:25:46
 */
@Repository
public class FbsBudgetDtlAbsDao extends BaseDao<FbsBudgetDtlAbs, String> {
	/**
	 * 根据计划Id获取明细列表
	 *
	 * @param tableName
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetId(String tableName, String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.budget_id as budgetId,b.item_code as itemCode,");
		sql.append(" b.b_money as bMoney,b.bsuper_money as bsuperMoney,b.btemp_money as btempMoney,");
		sql.append(" b.fact_Money as factMoney,b.diff_Reason as diffReason,b.analyse_Status as analyseStatus,b.rmk,");
		//增加科目表关联
		sql.append(" c.id as itemId,c.item_name as itemName,c.super_Item_Code as superItemCode,c.sign as sign,");
		sql.append(" c.is_Internal as isInternal,c.is_Control as isControl,c.is_Write as isWrite,c.item_relation as itemRelation,");
		sql.append(" c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend");
		sql.append(" from " + tableName + " b left join fbs_item c on b.item_code = c.item_code where b.budget_id = :budgetId order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetId);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据计划Id获取明细列表
	 *
	 * @param tableName
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listSumByBudgetIds(String tableName, List<String> budgetIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.item_code as itemCode,b.item_name as itemName,");
		sql.append(" sum(b.b_money) as bMoney,sum(b.bsuper_money) as bsuperMoney,sum(b.btemp_money) as btempMoney,");
		sql.append(" sum(b.fact_Money) as factMoney");
		sql.append(" from " + tableName + " b left join fbs_item c on b.item_code = c.item_code");
		sql.append(" where ").append(SqlUtil.getInSql(budgetIds, "b.budget_id", true));
		sql.append(" group by b.item_code,b.item_name");
		sql.append(" order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据计划Id获取明细列表
	 *
	 * @param tableName
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetIds(String tableName, List<String> budgetIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.budget_id as budgetId,b.item_code as itemCode,b.item_name as itemName,");
		sql.append(" b.b_money as bMoney,b.bsuper_money as bsuperMoney,b.btemp_money as btempMoney,");
		sql.append(" b.fact_Money as factMoney,b.diff_Reason as diffReason,b.analyse_Status as analyseStatus,b.rmk,");
		//增加科目表关联
		sql.append(" c.id as itemId,c.super_Item_Code as superItemCode,c.sign as sign,");
		sql.append(" c.is_Internal as isInternal,c.is_Control as isControl,c.is_Write as isWrite,c.item_relation as itemRelation,");
		sql.append(" c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend");
		sql.append(" from " + tableName + " b left join fbs_item c on b.item_code = c.item_code where b.budget_id in (:budgetIds) order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetIds", budgetIds);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据计划Id获取明细列表（折算本位币）
	 *
	 * @param tableName
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetIdConvertCur(String tableName, String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.budget_id as budgetId,b.item_code as itemCode,b.item_name as itemName,");
		sql.append(" b.b_money*f.exchange_Rate as bMoney,b.bsuper_money*f.exchange_Rate as bsuperMoney,b.btemp_money*f.exchange_Rate as btempMoney,");
		sql.append(" b.fact_Money*f.exchange_Rate as factMoney,b.diff_Reason as diffReason,b.analyse_Status as analyseStatus,b.rmk,");
		//增加科目表关联
		sql.append(" c.id as itemId,c.super_Item_Code as superItemCode,c.sign as sign,");
		sql.append(" c.is_Internal as isInternal,c.is_Control as isControl,c.is_Write as isWrite,c.item_relation as itemRelation,");
		sql.append(" c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend");
		sql.append(" from " + tableName + " b left join fbs_budget f on f.id=b.budget_id left join fbs_item c on b.item_code = c.item_code where b.budget_id = :budgetId order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetId);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据计划Id获取明细列表（折算本位币）
	 *
	 * @param tableName
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> listByBudgetIdConvertCur(String tableName, Set<String> budgetIds) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.budget_id as budgetId,b.item_code as itemCode,b.item_name as itemName,");
		sql.append(" b.b_money*f.exchange_Rate as bMoney,b.bsuper_money*f.exchange_Rate as bsuperMoney,b.btemp_money*f.exchange_Rate as btempMoney,");
		sql.append(" b.fact_Money*f.exchange_Rate as factMoney,b.diff_Reason as diffReason,b.analyse_Status as analyseStatus,b.rmk,");
		//增加科目表关联
		sql.append(" c.id as itemId,c.super_Item_Code as superItemCode,c.sign as sign,");
		sql.append(" c.is_Internal as isInternal,c.is_Control as isControl,c.is_Write as isWrite,c.item_relation as itemRelation,");
		sql.append(" c.is_Begin_Period as isBeginPeriod,c.is_End_Period as isEndPeriod,c.over_Per_Spend as overPerSpend");
		sql.append(" from " + tableName + " b left join fbs_budget f on f.id=b.budget_id left join fbs_item c on b.item_code = c.item_code where b.budget_id in (:budgetIds) order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetIds", budgetIds);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据明细Id获取明细
	 *
	 * @param entityName
	 * @param id
	 * @return
	 */
	public FbsBudgetDtlAbs getById(String entityName, String id) {
		String hql = " from " + entityName + " b where b.id = :id";
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("id", id);
		return (FbsBudgetDtlAbs) this.createQuery(hql, params).uniqueResult();
	}
	/**
	 * 根据计划主表ID删除明细
	 *
	 * @param budgetId
	 * @param tableName
	 */
	public void deleteByBudgetId(String tableName, String budgetId) {
		createSQLQuery("delete from " + tableName + " where budget_id=?", budgetId).executeUpdate();
	}
	/**
	 * 根据计划主表ID更新明细上报金额
	 *
	 * @param dtlId
	 * @param tableName
	 */
	public void updateByDtlId(String tableName, String dtlId,BigDecimal money) {
		createSQLQuery("update  " + tableName + " set b_money = ? where id=?", money,dtlId).executeUpdate();
	}
	/**
	 * 清除上报计划的上报金额
	 *
	 * @param tableName
	 * @param budgetId
	 */
	public void clearInputMoney(String tableName, String budgetId) {
		createSQLQuery("update " + tableName + " set b_money=0 where budget_id=?", budgetId).executeUpdate();
	}
	/**
	 * 清除本次计划追加金额
	 *
	 * @param tableName
	 * @param budgetId
	 */
	public void clearSuperaddTempMoney(String tableName, String budgetId) {
		createSQLQuery("update " + tableName + " set btemp_money=0 where budget_id=?", budgetId).executeUpdate();
	}
	/**
	 * 设置追加金额
	 *
	 * @param tableName
	 * @param budgetId
	 */
	public void setSuperaddMoney(String tableName, String budgetId) {
		createSQLQuery("update " + tableName + " set bsuper_money=(bsuper_money+btemp_money), btemp_money=0 where budget_id=?", budgetId).executeUpdate();
	}
	/**
	 * 汇总计划上报明细
	 *
	 * @param tableName
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> collectByBudgetIds(String tableName, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.item_code as itemCode,sum(b.b_money*f.exchange_rate) as bMoney,");
		sql.append(" sum(b.bsuper_money*f.exchange_rate) as bsuperMoney, sum(b.btemp_money*f.exchange_rate) as btempMoney");
		sql.append(" from " + tableName + " b left join fbs_budget f on b.budget_id = f.id where ");
		sql.append(joinSql);
		sql.append(" group by b.item_code order by item_code");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 汇总计划上报明细（不折算）
	 *
	 * @param tableName
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetDtlAbsVO> collectByBudgetIdsNormal(String tableName, String joinSql) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.item_code as itemCode,sum(b.b_money) as bMoney,");
		sql.append(" sum(b.bsuper_money) as bsuperMoney, sum(b.btemp_money) as btempMoney");
		sql.append(" from " + tableName + " b left join fbs_budget f on b.budget_id = f.id where ");
		sql.append(joinSql);
		sql.append(" group by b.item_code order by item_code");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 根据条件获取明细
	 *
	 * @param entityName
	 * @param budgetId
	 * @param itemCode
	 * @return
	 * @author zuoy 2015-04-18
	 */
	public FbsBudgetDtlAbs getByBudgetId(String entityName, String budgetId, String itemCode) {
		StringBuilder hql = new StringBuilder();
		HashMap<String, String> params = new HashMap<String, String>();
		hql.append(" from ").append(entityName).append(" b where b.budgetId = :budgetId and itemCode = :itemCode ");
		params.put("budgetId", budgetId);
		params.put("itemCode", itemCode);
		return (FbsBudgetDtlAbs) this.createQuery(hql.toString(), params).uniqueResult();
	}
	/**
	 * 汇总同级单位某科目上报金额
	 * 改为使用 collectMoneySameLevelCorp2 方法
	 * @param itemCode
	 * @param parentCorpId
	 * @return
	 */
	/*public BigDecimal collectMoneySameLevelCorp(String itemCode, String parentCorpId, String tableName, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder("select sum(t.b_money) from fbs_budget f");
		sql.append(" left join " + tableName + " t on t.budget_id = f.id  where f.status>="+ BudgetConstant.STATUE_CHECK);
		sql.append(" and t.item_code=:itemCode and f.corp_id in (select c.id from sys_corp c where c.parent_id = :parentCorpId)");
		sql.append(" and f.begin_date = :beginDate and f.end_date=:endDate");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode);
		params.put("parentCorpId", parentCorpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		Object bMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal collMoney = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(bMoney)) {
			collMoney = (BigDecimal) bMoney;
		}
		return collMoney;
	}*/
	/**
	 * 汇总同级单位某科目上报金额
	 * 修改后
	 * @param itemCode
	 * @param corpIds
	 * @return
	 */
	public BigDecimal collectMoneySameLevelCorp2(String itemCode, List<String> corpIds, String tableName, Date beginDate, Date endDate) {
		BigDecimal collMoney = BigDecimal.ZERO;
		if (!CollectionUtil.isEmpty(corpIds) && corpIds.size() <= 1000){
			collMoney = this.collectMoney(itemCode, corpIds, tableName, beginDate, endDate);
		}else if (!CollectionUtil.isEmpty(corpIds) && corpIds.size() > 1000) {
			List<List<String>> partition = Lists.partition(corpIds, 1000);
			for (List<String> list : partition) {
				BigDecimal bigDecimal = this.collectMoney(itemCode, list, tableName, beginDate, endDate);
				collMoney = collMoney.add(bigDecimal);
			}
		}
		return collMoney;
	}
	private BigDecimal collectMoney(String itemCode, List<String> corpIds, String tableName, Date beginDate, Date endDate) {
		BigDecimal lumpMoney = BigDecimal.ZERO;
		StringBuilder sql = new StringBuilder("select sum(t.b_money) from fbs_budget f");
		sql.append(" left join " + tableName + " t on t.budget_id = f.id  where f.status>="+ BudgetConstant.STATUE_CHECK);
		sql.append(" and t.item_code=:itemCode and f.corp_id in (:corpIds)");
		sql.append(" and f.begin_date = :beginDate and f.end_date=:endDate");
		HashMap<String,Object> params = new HashMap<>();
		params.put("itemCode", itemCode);
		params.put("corpIds", corpIds);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		Object bMoney = this.createSQLQuery(sql.toString(), params).uniqueResult();
		if (!EmptyUtils.isEmpty(bMoney)) {
			lumpMoney = (BigDecimal) bMoney;
		}
		return lumpMoney;
	}
	/*public SQLQuery createSQLQuery(String queryString, final Map<String, ?> values) {
		SQLQuery sqlQuery = createSQLQuery(queryString);
		if (values != null) {
			sqlQuery.setProperties(values);
		}
		sqlQuery.setParameterList();
		return sqlQuery;
	}*/
	/**
	 * 汇总同级单位明细
	 * @return
	 */
	/*public List<FbsBudgetDtlAbsVO> collectListSameLevelCorp(String itemCode, String parentCorpId, String tableName, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder("select t.id as id, t.item_code as itemCode, t.item_name as itemName from " + tableName + " t");
		sql.append(" left join fbs_budget f on t.budget_id = f.id  where f.status>="+ BudgetConstant.STATUE_CHECK);
		sql.append(" and t.item_code=:itemCode and f.corp_id in (select c.id from sys_corp c where c.parent_id = :parentCorpId)");
		sql.append(" and f.begin_date = :beginDate and f.end_date=:endDate");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode);
		params.put("parentCorpId", parentCorpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}*/
	public List<FbsBudgetDtlAbsVO> collectListSameLevelCorp2(String itemCode, List<String> corpIds, String tableName, Date beginDate, Date endDate) {
		List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOS = CollectionUtil.newArrayList();
		if (!CollectionUtil.isEmpty(corpIds) && corpIds.size() <= 1000){
			fbsBudgetDtlAbsVOS = this.collectCorp(itemCode, corpIds, tableName, beginDate, endDate);
		}else if (!CollectionUtil.isEmpty(corpIds) && corpIds.size() > 1000) {
			List<List<String>> partition = Lists.partition(corpIds, 1000);
			for (List<String> list : partition) {
				List<FbsBudgetDtlAbsVO> fbsBudgetDtlAbsVOS1 = this.collectCorp(itemCode, list, tableName, beginDate, endDate);
				fbsBudgetDtlAbsVOS.addAll(fbsBudgetDtlAbsVOS1);
			}
		}
		return fbsBudgetDtlAbsVOS;
	}
	private List<FbsBudgetDtlAbsVO> collectCorp(String itemCode, List<String> corpIds, String tableName, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder("select t.id as id, t.item_code as itemCode, t.item_name as itemName from " + tableName + " t");
		sql.append(" left join fbs_budget f on t.budget_id = f.id  where f.status>="+ BudgetConstant.STATUE_CHECK);
		sql.append(" and t.item_code=:itemCode and f.corp_id in (:corpIds)");
		sql.append(" and f.begin_date = :beginDate and f.end_date=:endDate");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode);
		params.put("corpIds", corpIds);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return this.listBySQLAliasToBean(sql.toString(), params, FbsBudgetDtlAbsVO.class);
	}
	/**
	 * 查询上报累计净额
	 *
	 * @param budgetId
	 * @return
	 */
	public BigDecimal getNetAmount(String budgetId, String tableName) {
		StringBuilder sql = new StringBuilder("select ((select bt_nvl(f.b_money, 0) from " + tableName + " f where f.budget_id = t.id and f.item_code = '"+ BudgetConstant.ITEM_CODE_200+"') -");
		sql.append(" (select bt_nvl(f.b_money, 0) from " + tableName + " f where f.budget_id = t.id and f.item_code = '"+ BudgetConstant.ITEM_CODE_400+"')) as netAmount");
		sql.append(" from fbs_budget t where t.id = :budgetId");
		HashMap params = new HashMap();
		params.put("budgetId", budgetId);
		Object result = this.createSQLQuery(sql.toString(), params).uniqueResult();
		BigDecimal netAmount = BigDecimal.ZERO;
		if (!EmptyUtils.isEmpty(result)) {
			netAmount = (BigDecimal) result;
		}
		return netAmount;
	}
}
