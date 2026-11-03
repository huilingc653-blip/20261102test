package com.bytter.biz.budget.dao;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsBudgetSuperaddDetail;
import com.bytter.biz.budget.vo.FbsBudgetSuperaddDetailVO;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FBS_BUDGET_SUPERADD_DETAIL
* @author onlineGenerator
* @date 2017-07-28 11:36:59
* @version 1.0
*
*/
@Repository
public class FbsBudgetSuperaddDetailDao extends BaseDao<FbsBudgetSuperaddDetail,String>{
	/**
	 * 根据追加Id获取明细列表
	 * @param superaddId
	 * @return
	 */
	public List<FbsBudgetSuperaddDetail> listBySuperaddId(String superaddId) {
		String hql = " from FbsBudgetSuperaddDetail b where b.superaddId = :superaddId";
		HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("superaddId", superaddId);
        return listByHQLAliasToBean(hql, params, FbsBudgetSuperaddDetail.class);
	}
	
	/**
	 * 根据计划追加Id删除明细
	 * @param superaddId
	 */
	public void deleteBySuperaddId(String superaddId) {
		createSQLQuery("delete from fbs_budget_superadd_detail where superadd_id=?",superaddId).executeUpdate();
	}
	
	/**
	 * 根据追加Id获取明细列表
	 * @param superaddId
	 * @return
	 */
	public List<FbsBudgetSuperaddDetailVO> listVOBySuperaddId(String superaddId) {
		StringBuilder sql = new StringBuilder();
		sql.append("select b.id as id,b.superadd_Id as superaddId,b.item_code as itemCode,b.item_name as itemName,b.bsuper_money as bsuperMoney");
		sql.append(" from fbs_budget_superadd_detail b where b.superadd_id = :superaddId order by b.item_code");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("superaddId", superaddId);
		return listBySQLAliasToBean(sql.toString(), params, FbsBudgetSuperaddDetailVO.class);
	}
	
	/**
	 * 根据计划追加Id删除明细
	 * @param entity
	 * 
	 */
	public void updateSuperMoneyBySuperaddId(FbsBudgetSuperaddDetailVO entity) {
		createSQLQuery("update fbs_budget_superadd_detail set bsuper_money=? where id=?", entity.getBsuperMoney(), entity.getId()).executeUpdate();
	}
}
