package com.bytter.biz.budget.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.budget.entity.FbsBudgetPeriod;
import com.bytter.core.base.BaseDao;
/**
* @Title: Dao
* @Description: FBS_BUDGET_PERIOD
* @author onlineGenerator
* @date 2017-07-07 16:09:08
* @version 1.0
*
*/
@Repository
public class FbsBudgetPeriodDao extends BaseDao<FbsBudgetPeriod,String>{
	/**
	 * 得到表格的值
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<FbsBudgetPeriod> getBtTable(String year){
		String hql = " from FbsBudgetPeriod where year = :year order by beginDate ";
		Map params= new HashMap();
		params.put("year", year);
		return this.getSession().createQuery(hql).setProperties(params).list();
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FbsBudgetPeriod getBy(Date date){
		FbsBudgetPeriod fbsBudgetPeriod = new FbsBudgetPeriod();
		String hql = " from FbsBudgetPeriod where  beginDate <=:date and endDate>=:date";
		Map params= new HashMap();
		params.put("date", date);
		fbsBudgetPeriod = (FbsBudgetPeriod)this.getSession().createQuery(hql).setProperties(params).uniqueResult();
		return fbsBudgetPeriod;
	}
	/**
	 * 根据开始时间结束时间得到周期集合
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	public List<FbsBudgetPeriod> getByForList(Date beginDate,Date endDate){
		String hql = " from FbsBudgetPeriod where  beginDate >=:beginDate and endDate<=:endDate order by beginDate";
		Map params= new HashMap();
		params.put("beginDate", beginDate);
		params.put("endDate", endDate);
		return  this.getSession().createQuery(hql).setProperties(params).list();
	}
}
