package com.bytter.biz.finacial.closedeal.dao;
import com.bytter.biz.finacial.closedeal.entity.FcDayStart;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.finacial.api.constant.CloseConstant;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.Map;
/**
* @Title: Dao
* @Description: FC_DAY_START
* @author onlineGenerator
* @date 2017-07-06 14:51:02
* @version 1.0
*
*/
@Repository
public class FcDayStartDao extends BaseDao<FcDayStart,String>{
	/**
	 * 功能介绍 : 获取网点的日初日期。<br>
	 */
	public FcDayStart getDayStartDate(String netId) {
		String sql = " from FcDayStart where netCode = :netId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		return (FcDayStart)createQuery(sql,params).uniqueResult();
	}
	
	/**
	 * 功能介绍 : 获取网点的月结日期。<br>
	 */
	public FcDayStart getMonthCloseDate(String netId) {
		String hql = " from FcDayClose where netCode = :netId and overType = :overType ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("overType", CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());
		return (FcDayStart) createQuery(hql, params).uniqueResult();
	}
	/**
	 * 功能介绍 : 获取网点的日初日期。<br>
	 */
	public FcDayStart getDayStartDateByType(String netId) {
		String hql = " from FcDayStart where netCode = :netId ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		return (FcDayStart) createQuery(hql, params).uniqueResult();
	}
	
}
