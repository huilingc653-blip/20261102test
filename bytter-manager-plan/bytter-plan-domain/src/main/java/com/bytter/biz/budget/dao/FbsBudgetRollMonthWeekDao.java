package com.bytter.biz.budget.dao;
import com.bytter.biz.budget.entity.FbsBudget;
import com.bytter.biz.budget.entity.FbsBudgetResolveYear;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
/**
 * @author qijh
 * @date 2017-08-03
 */
@Repository
public class FbsBudgetRollMonthWeekDao extends BaseDao<FbsBudget, String> {
	/**
	 * 获取时间段对应类型的集合
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudget> listCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		String hql = " from FbsBudget b where b.corpId = :corpId and b.timeType = :timeType "
				+ "and b.beginDate >= :beginDate and b.endDate <= :endDate ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return this.getSession().createQuery(hql).setProperties(params).list();
	}
	/**
	 * 获取时间段对应计划
	 * @param corpId
	 * @param timeType
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public FbsBudget getCorpBudget(String corpId, String timeType, Date beginDate, Date endDate) {
		String hql = " from FbsBudget b where b.corpId = :corpId and b.timeType = :timeType "
				+ "and b.beginDate = :beginDate and b.endDate = :endDate ";
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("corpId", corpId);
		params.put("timeType", timeType);
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		List<FbsBudget> list = this.getSession().createQuery(hql).setProperties(params).list();
		if (EmptyUtils.isEmptyList(list)) {
			return null;
		}
		return list.get(0);
	}
}
