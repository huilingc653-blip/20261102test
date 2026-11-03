package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsBudgetDetail;
import com.bytter.biz.budget.vo.FbsBudgetDetailVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.*;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Dao
 * @Description: FBS_BUDGET_DETAIL
 * @date 2017-07-19 14:59:31
 */
@Repository
public class FbsBudgetDetailDao extends BaseDao<FbsBudgetDetail, String> {
	/**
	 * 得到明细实体
	 *
	 * @param id
	 * @param entityName
	 * @return
	 */
	public Object getDtlById(String id, String entityName) {
		String hql = " from " + entityName + " where id=:id";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", id);
		return this.getSession().createQuery(hql).setProperties(map).uniqueResult();
	}
	/**
	 * 根据计划明细id得到科目所有明细
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FbsBudgetDetail> getDetailListById(String detailId) {
		List<FbsBudgetDetail> list = new ArrayList<FbsBudgetDetail>();
		String hql = " from FbsBudgetDetail where detailId = :detailId ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detailId", detailId);
		list = this.getSession().createQuery(hql).setProperties(map).list();
		return list;
	}
	/**
	 * 得到预算的所有明细
	 * @param budgetId
	 * @param entityName
	 * @return
	 */
	public List<FbsBudgetDetail> getDetailListByBudgetId(String budgetId, String entityName) {
		List<FbsBudgetDetail> list = new ArrayList<FbsBudgetDetail>();
		String hql = " from FbsBudgetDetail where detailId in (select id  from  " + entityName + " where budgetId=:budgetId) ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("budgetId", budgetId);
		list = this.getSession().createQuery(hql).setProperties(map).list();
		return list;
	}
	/**
	 * 得到明细实体
	 *
	 * @param itemCode
	 * @param budgetId
	 * @param entityName
	 * @return
	 */
	public Object getDtlByItemCode(String itemCode, String budgetId, String entityName) {
		String hql = " from " + entityName + " where budgetId=:budgetId and itemCode = :itemCode";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("budgetId", budgetId);
		map.put("itemCode", itemCode);
		return this.getSession().createQuery(hql).setProperties(map).uniqueResult();
	}
	/**
	 * 删除计划明细
	 *
	 * @param budgetId  计划
	 * @param tableName 计划明细表名
	 */
	public void delColloctDetail(String budgetId, String tableName) {
		String sql = " delete from fbs_budget_detail where detail_id in (select id  from  " + tableName + " where budget_id = '" + budgetId + "') ";
		this.createSQLQuery(sql).executeUpdate();
	}
	/**
	 * 删除计划明细录入
	 *
	 * @param detailId 明细ID
	 */
	public void delByDetailId(String detailId) {
		String sql = " delete from fbs_budget_detail where detail_id  = '" + detailId + "'";
		this.createSQLQuery(sql).executeUpdate();
	}
	/**
	 * 根据计划明细id得到科目所有明细
	 *
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<FbsBudgetDetail> getDetailListById(String detailId, Date beginDate, Date endDate) {
		List<FbsBudgetDetail> list = new ArrayList<FbsBudgetDetail>();
		String hql = " from FbsBudgetDetail where detailId = :detailId and vestingDate >= :beginDate and vestingDate <= :endDate";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("detailId", detailId);
		map.put("beginDate", beginDate);
		map.put("endDate", endDate);
		list = this.getSession().createQuery(hql).setProperties(map).list();
		return list;
	}
	/**
	 * 根据下级单位明细删除汇总单位明细id
	 *
	 * @param childId
	 */
	public void delDetailByChildId(String childId) {
		String sql = " delete from fbs_budget_detail where CHILD_CORP_DTL_ID = :childId ";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("childId", childId);
		this.createSQLQuery(sql).setProperties(map).executeUpdate();
	}
	/**
	 * 挂接计划执行时，加载科目明细数据
	 *
	 * @param params
	 * @return
	 * @author zuoy 2018-11-16
	 */
	public List<FbsBudgetDetailVO> getProjectDetailInfo(Map<String, Object> params, String tableName, String budgetId) {
		StringBuilder sql = new StringBuilder();
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append("select * from fbs_budget_detail f where 1 = 1 ");
		sql.append(" and f.detail_id in( ");
		sql.append("select fbs.id from ").append(tableName).append(" fbs where fbs.budget_id = :budgetId ) ");
		map.put("budgetId", budgetId);
		// 过滤只审批通过的
		sql.append(" and (f.b_money is not null or f.bsuper_money is not null) ");
		sql.append(" and f.item_code = :itemCode ");
		map.put("itemCode", params.get("itemCode"));
		if (params.containsKey("curId") && !EmptyUtils.isEmpty(params.get("curId")) && StringUtils.isNotBlank(params.get("curId").toString())) {
			sql.append(" and (f.cur_id = :curId or f.cur_id is null) ");
			map.put("curId", params.get("curId"));
		}
		if (params.containsKey("settlType") && !EmptyUtils.isEmpty(params.get("settlType")) && StringUtils.isNotBlank(params.get("settlType").toString())) {
			sql.append(" and (f.settl_type = :settlType or trim(f.settl_type) is null) ");
			map.put("settlType", params.get("settlType"));
		}
		if (params.containsKey("corpName") && !EmptyUtils.isEmpty(params.get("corpName")) && StringUtils.isNotBlank(params.get("corpName").toString())) {
			sql.append(" and (f.corp_name = :corpName or f.corp_name is null) ");
			map.put("corpName", params.get("corpName"));
		}
		if (params.containsKey("purpose") && !EmptyUtils.isEmpty(params.get("purpose")) && StringUtils.isNotBlank(params.get("purpose").toString())) {
			sql.append(" and (f.purpose = :purpose or f.purpose is null or f.purpose = '') ");
			map.put("purpose", params.get("purpose"));
		}
		if (params.containsKey("wishPayday") && !EmptyUtils.isEmpty(params.get("wishPayday")) && StringUtils.isNotBlank(params.get("wishPayday").toString())) {
			sql.append(" and (f.wish_payday = :wishPayday or f.wish_payday is null) ");
			map.put("wishPayday", DateUtils.parseDate(params.get("wishPayday").toString()));
		}
		if (params.containsKey("payerAcc") && !EmptyUtils.isEmpty(params.get("payerAcc")) && StringUtils.isNotBlank(params.get("payerAcc").toString())) {
			sql.append(" and (f.payer_acc = :payerAcc or f.payer_acc is null or f.payer_acc = '') ");
			map.put("payerAcc", params.get("payerAcc"));
		}
		if (params.containsKey("payeeAccName") && !EmptyUtils.isEmpty(params.get("payeeAccName")) && StringUtils.isNotBlank(params.get("payeeAccName").toString())) {
			sql.append(" and (f.payee_acc_name = :payeeAccName or f.payee_acc_name is null or f.payee_acc_name = '') ");
			map.put("payeeAccName", params.get("payeeAccName"));
		}
		if (params.containsKey("payeeAcc") && !EmptyUtils.isEmpty(params.get("payeeAcc")) && StringUtils.isNotBlank(params.get("payeeAcc").toString())) {
			sql.append(" and (f.payee_acc = :payeeAcc or f.payee_acc is null or f.payee_acc = '') ");
			map.put("payeeAcc", params.get("payeeAcc"));
		}
		if (params.containsKey("payeeBank") && !EmptyUtils.isEmpty(params.get("payeeBank")) && StringUtils.isNotBlank(params.get("payeeBank").toString())) {
			sql.append(" and (f.payee_bank = :payeeBank or f.payee_bank is null or f.payee_bank = '') ");
			map.put("payeeBank", params.get("payeeBank"));
		}
		if (params.containsKey("contract") && !EmptyUtils.isEmpty(params.get("contract")) && StringUtils.isNotBlank(params.get("contract").toString())) {
			sql.append(" and (f.contract = :contract or f.contract is null) ");
			map.put("contract", params.get("contract"));
		}
		return listBySQLAliasToBean(sql.toString(), map, FbsBudgetDetailVO.class);
	}
	/**
	 * 设置追加金额
	 *
	 * @param tableName
	 * @param budgetId
	 */
	public void setSuperaddMoney(String tableName, String budgetId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" update fbs_budget_detail set bsuper_money=(bt_nvl_num(bsuper_money, 0) + btemp_money), btemp_money=0 ");
		sql.append(" ,bsuper_natual_money=(bt_nvl_num(bsuper_natual_money, 0) + btemp_natual_money), btemp_natual_money=0 ");
		sql.append(" where detail_id in( ");
		sql.append("select fbs.id from ").append(tableName).append(" fbs where fbs.budget_id = ? ) ");
		createSQLQuery(sql.toString(), budgetId).executeUpdate();
	}
	/**
	 * 删除新追加的录入明细-计划追加
	 *
	 * @param budgetId
	 */
	public void delColloctDetailSuper(String budgetId, String tableName) {
		// 将新追加的录入明细删除
		String delSql = " delete from fbs_budget_detail where b_money is null and bsuper_money is null and detail_id in (select id  from  " + tableName + " where budget_id = '" + budgetId + "') ";
		this.createSQLQuery(delSql).executeUpdate();
		// 将已上报或已追加的暂存金额变为0
		String updateSql = " update fbs_budget_detail set btemp_money = 0, btemp_natual_money =0 where btemp_money is not null and detail_id in (select id  from  " + tableName + " where budget_id = '" + budgetId + "') ";
		this.createSQLQuery(updateSql).executeUpdate();
	}
	/**
	 * 获取汇总明细科目的折合人民币金额
	 *
	 * @param budgetId  计划ID
	 * @param tableName 表名
	 * @param groupType 归属日期分组类型
	 * @return
	 */
	public List<FbsBudgetDetail> collectBudgetDetail(String budgetId, String tableName, String groupType) {
		StringBuilder sql = new StringBuilder();
		sql.append(" select d.detail_id,d.item_code,sum(bt_nvl_num(d.b_money, 0)) b_money,sum(bt_nvl_num(d.btemp_money, 0)) btemp_money ");
		if ("1".equals(groupType)) {
			sql.append(", bt_to_date(concat(bt_to_char(d.vesting_date,'yyyy-MM'),'-01'),'yyyy-MM-dd') VESTING_DATE");
		} else if ("2".equals(groupType)) {
			sql.append(",d.vesting_date ");
		}
		sql.append(" from fbs_budget_detail d  where d.detail_id in (select id from ").append(tableName).append(" where budget_id = '").append(budgetId).append("') ");
		sql.append(" group by d.detail_id,d.item_code ");
		if ("1".equals(groupType)) {
			sql.append(",  bt_to_date(concat(bt_to_char(d.vesting_date,'yyyy-MM'),'-01'),'yyyy-MM-dd') ");
		} else if ("2".equals(groupType)) {
			sql.append(",d.vesting_date ");
		}
		List<FbsBudgetDetail> list = this.listBySQLAliasToBean(sql.toString(), null, FbsBudgetDetail.class);
		return list;
	}
}
