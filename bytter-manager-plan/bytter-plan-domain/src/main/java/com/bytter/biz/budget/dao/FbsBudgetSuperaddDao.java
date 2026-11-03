package com.bytter.biz.budget.dao;
import java.util.HashMap;
import java.util.List;
import com.bytter.core.constant.BudgetConstant;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsBudgetSuperadd;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FBS_BUDGET_SUPERADD
* @author onlineGenerator
* @date 2017-07-28 11:36:40
* @version 1.0
*
*/
@Repository
public class FbsBudgetSuperaddDao extends BaseDao<FbsBudgetSuperadd,String>{
	
	/**
	 * 根据计划ID获取追加信息
	 * @param budgetId
	 * @return
	 */
	public List<FbsBudgetSuperadd> listByBudgetId(String budgetId){
		String hql = " from FbsBudgetSuperadd b where b.budgetId = :budgetId order by b.createDate ";
		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("budgetId", budgetId);
        return listByHQLAliasToBean(hql, params, FbsBudgetSuperadd.class);
	}
	
	/**
	 * 根据计划ID获取追加未完成信息
	 * @param budgetId
	 * @return
	 */
	public FbsBudgetSuperadd getLastSuperaddByBudgetId(String budgetId){
		String hql = "from FbsBudgetSuperadd b where b.budgetId = :budgetId and b.status<>:status";
		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("budgetId", budgetId);
        params.put("status", BudgetConstant.STATUE_CHECK);
		List<FbsBudgetSuperadd> list=this.listByHQL(hql,params);
		FbsBudgetSuperadd budget=null;
		if(list!=null && list.size()>0){
			budget=list.get(0);
		}
        return budget;
	}
	/**
	 * 根据单位统计追加的资金计划
	 * @param corpId
	 * @param busType del:单位删除 disable:禁用 change:上级变更
	 * @return
	 */
	public int getSuperaddCountByCorpId(String corpId, String busType){
		String sql="select count(*) from fbs_budget_superadd where corp_id=:corpId";
		HashMap<String,Object> params=new HashMap<>();
		params.put("corpId",corpId);
		if(!"del".equals(busType)){
			//0-暂存、11-待审批、-1-审批打回、80-待汇总、81-汇总中、82-汇总审批中、83-汇总审批打回
			sql+=" and status in ('0','11','-1','80','','81','82','83')";
		}
		return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
	}
}
