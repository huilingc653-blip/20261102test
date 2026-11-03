package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsBudgetResolveMonth;
import com.bytter.biz.budget.vo.FbsBudgetResolveMonthVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
/**
* @Title: Dao
* @Description: FBS_BUDGET_RESOLVE_MONTH
* @author onlineGenerator
* @date 2017-08-04 10:03:40
* @version 1.0
*
*/
@Repository
public class FbsBudgetResolveMonthDao extends BaseDao<FbsBudgetResolveMonth,String>{
	/**
	 * 根据计划ID获取分解信息
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetResolveMonth> listByBudgetId(String budgetId){
		String hql = " from FbsBudgetResolveMonth f where f.budgetId = :budgetId order by f.itemCode";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetId);
		return (List<FbsBudgetResolveMonth>)this.createQuery(hql, params).list();
	}
	/**
	 * 根据计划ID获取分解信息
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetResolveMonth> listByBudgetIds(Set<String> budgetIds){
		String hql = " from FbsBudgetResolveMonth f where f.budgetId in (:budgetId) order by f.itemCode";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetIds);
		return (List<FbsBudgetResolveMonth>)this.createQuery(hql, params).list();
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetIds
	 * @return
	 */
	public List<FbsBudgetResolveMonth> listByBudgetIds(List<String> budgetIds) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from FbsBudgetResolveMonth f where ");
		hql.append(SqlUtil.getInSql(budgetIds, "f.budgetId", true));
		hql.append(" order by f.itemCode");
		return (List<FbsBudgetResolveMonth>) this.createQuery(hql.toString()).list();
	}
	/**
	 * 根据计划ID获取分解信息
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetResolveMonthVO> listVOByBudgetId(String budgetId){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.item_code as itemCode,b.item_name as itemName,i.id as itemId,");
		sql.append(" b.b_money as bMoney,");
		sql.append(" b.b_money_1 as bMoney1, b.b_money_2 as bMoney2, b.b_money_3 as bMoney3,");
		sql.append(" b.b_money_4 as bMoney4, b.b_money_5 as bMoney5, b.b_money_6 as bMoney6,");
		sql.append(" b.bsuper_Money as bsuperMoney,");
		sql.append(" b.bsuper_Money_1 as bsuperMoney1, b.bsuper_Money_2 as bsuperMoney2, b.bsuper_Money_3 as bsuperMoney3,");
		sql.append(" b.bsuper_Money_4 as bsuperMoney4, b.bsuper_Money_5 as bsuperMoney5, b.bsuper_Money_6 as bsuperMoney6,");
		sql.append(" b.btemp_money as btempMoney,");
		sql.append(" b.btemp_money_1 as btempMoney1, b.btemp_money_2 as btempMoney2, b.btemp_money_3 as btempMoney3,");
		sql.append(" b.btemp_money_4 as btempMoney4, b.btemp_money_5 as btempMoney5, b.btemp_money_6 as btempMoney6");
		sql.append(" from fbs_budget_resolve_month b left join fbs_item i on b.item_code= i.item_code ");
		sql.append(" where b.budget_Id = :budgetId order by b.item_Code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetId);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetResolveMonthVO.class);
	}
	/**
	 * 根据计划ID获取分解信息
	 *
	 * @param budgetId
	 * @param itemCode
	 * @return
	 */
	public FbsBudgetResolveMonth getByBudgetIdAndItemCode(String budgetId, String itemCode) {
		String hql = " from FbsBudgetResolveMonth f where f.budgetId = :budgetId and f.itemCode = :itemCode order by f.itemCode";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("budgetId", budgetId);
		params.put("itemCode", itemCode);
		return (FbsBudgetResolveMonth)this.createQuery(hql, params).uniqueResult();
	}
	
	/**
	 * 根据计划主表ID删除
	 * @param budgetId
	 */
	public void deleteByBudgetId( String budgetId) {
		createSQLQuery("delete from fbs_budget_resolve_month where budget_id=?",budgetId).executeUpdate();
	}
	
	/**
	 * 获取下级单位计划分解信息
	 * @param joinSql
	 * @return
	 */
	public List<FbsBudgetResolveMonth> collectByBudgetIds(String joinSql){
		StringBuilder sql = new StringBuilder();
		sql.append("select b.item_code as itemCode,b.item_name as itemName,sum(b.b_money*f.exchange_rate) as bMoney,");
		sql.append(" sum(b.b_money_1*f.exchange_rate) as bMoney1, sum(b.b_money_2*f.exchange_rate) as bMoney2, sum(b.b_money_3*f.exchange_rate) as bMoney3,");
		sql.append(" sum(b.b_money_4*f.exchange_rate) as bMoney4, sum(b.b_money_5*f.exchange_rate) as bMoney5, sum(b.b_money_6*f.exchange_rate) as bMoney6,");
		sql.append(" sum(b.btemp_money*f.exchange_rate) as btempMoney,");
		sql.append(" sum(b.btemp_money_1*f.exchange_rate) as btempMoney1, sum(b.btemp_money_2*f.exchange_rate) as btempMoney2, sum(b.btemp_money_3*f.exchange_rate) as btempMoney3,");
		sql.append(" sum(b.btemp_money_4*f.exchange_rate) as btempMoney4, sum(b.btemp_money_5*f.exchange_rate) as btempMoney5, sum(b.btemp_money_6*f.exchange_rate) as btempMoney6");
		sql.append(" from fbs_budget_resolve_month b left join fbs_budget f on f.id= b.budget_id where ");
		sql.append(joinSql);
		sql.append(" group by b.item_code, b.item_name order by item_code");
		return this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetResolveMonth.class);
	}
	
	/**
	 * 根据计划主表清空上报金额
	 * @param budgetId
	 */
	public void clearInputMoney(String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append("update fbs_budget_resolve_month set b_money = 0,");
		sql.append(" b_money_1=0, b_money_2=0, b_money_3=0,");
		sql.append(" b_money_4=0, b_money_5=0, b_money_6=0");
		sql.append(" where budget_id=?");
		createSQLQuery(sql.toString(),budgetId).executeUpdate();
	}
	/**
	 * 清除本次计划追加金额
	 *
	 * @param budgetId
	 */
	public void clearSuperaddTempMoney(String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append("update fbs_budget_resolve_month set btemp_money = 0,");
		sql.append(" btemp_money_1=0, btemp_money_2=0, btemp_money_3=0,");
		sql.append(" btemp_money_4=0, btemp_money_5=0, btemp_money_6=0");
		sql.append(" where budget_id=?");
		createSQLQuery(sql.toString(), budgetId).executeUpdate();
	}
	/**
	 * 设置追加金额
	 *
	 * @param budgetId
	 */
	public void setSuperaddMoney(String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update fbs_budget_resolve_month set ");
		sql.append(" bsuper_money=(bt_nvl(bsuper_money, 0)+btemp_money),bsuper_money_1=(bt_nvl(bsuper_money_1, 0)+btemp_money_1),bsuper_money_2=(bt_nvl(bsuper_money_2, 0)+btemp_money_2),");
		sql.append(" bsuper_money_3=(bt_nvl(bsuper_money_3, 0)+btemp_money_3),bsuper_money_4=(bt_nvl(bsuper_money_4, 0)+btemp_money_4),bsuper_money_5=(bt_nvl(bsuper_money_5, 0)+btemp_money_5),");
		sql.append(" bsuper_money_6=(bt_nvl(bsuper_money_6, 0)+btemp_money_6),");
		sql.append(" btemp_money = 0,btemp_money_1=0, btemp_money_2=0, btemp_money_3=0,");
		sql.append(" btemp_money_4=0, btemp_money_5=0, btemp_money_6=0");
		sql.append(" where budget_id=?");
		createSQLQuery(sql.toString(), budgetId).executeUpdate();
	}
	/**
	 * 汇总下级单位某科目上报金额
	 * collectMoneySameLevelCorp 方法没有发现使用，先注释
	 * @param itemCode
	 * @param parentCorpId
	 * @return
	 */
	/*public FbsBudgetResolveMonthVO collectMoneySameLevelCorp(String itemCode, String parentCorpId, Date beginDate, Date endDate) {
		StringBuilder sql = new StringBuilder("select bt_nvl(sum(m.b_money),0) as bMoney,bt_nvl(sum(m.b_money_1),0) as bMoney1,bt_nvl(sum(m.b_money_2),0) as bMoney2,bt_nvl(sum(m.b_money_3),0) as bMoney3,");
		sql.append(" bt_nvl(sum(m.b_money_4),0) as bMoney4,bt_nvl(sum(m.b_money_5),0) as bMoney5,bt_nvl(sum(m.b_money_6),0) as bMoney6");
		sql.append(" from fbs_budget_resolve_month m");
		sql.append(" left join fbs_budget f on m.budget_id = f.id  where f.status>="+ BudgetConstant.STATUE_CHECK);
		sql.append(" and m.item_code=:itemCode and f.corp_id in (select c.id from sys_corp c where c.parent_id = :parentCorpId)");
		sql.append(" and f.begin_date = :beginDate and f.end_date=:endDate");
		HashMap params = new HashMap();
		params.put("itemCode", itemCode);
		params.put("parentCorpId", parentCorpId);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return getBySQLAliasToBean(sql.toString(), params, FbsBudgetResolveMonthVO.class);
	}*/
}
