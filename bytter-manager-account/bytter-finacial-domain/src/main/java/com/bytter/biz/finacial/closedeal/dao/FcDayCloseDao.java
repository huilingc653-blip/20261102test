package com.bytter.biz.finacial.closedeal.dao;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.bytter.biz.finacial.closedeal.vo.FcDayCloseVO;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.utils.DateUtils;
import org.springframework.stereotype.Repository;
import com.bytter.biz.finacial.closedeal.entity.FcDayClose;
import com.bytter.core.base.BaseDao;
import com.bytter.biz.finacial.api.constant.CloseConstant;
/**
* @Title: Dao
* @Description: FC_DAY_CLOSE
* @author onlineGenerator
* @date 2017-07-06 14:51:02
* @version 1.0
*
*/
@Repository
public class FcDayCloseDao extends BaseDao<FcDayClose,String>{
	/**
	 * 功能介绍 : 获取网点的日结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcDayClose getDayCloseDate(String netId) {
		String hql = " from FcDayClose where netCode = :netId and overType = :overType ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("overType", CloseConstant.CarryType.CARRY_TYPE_DAY_OVER.get());
		return (FcDayClose) createQuery(hql, params).uniqueResult();
	}
	
	/**
	 * 功能介绍 : 获取网点的月结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcDayClose getMonthCloseDate(String netId) {
		String hql = " from FcDayClose where netCode = :netId and overType = :overType ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("overType", CloseConstant.CarryType.CARRY_TYPE_MONTH_OVER.get());
		return (FcDayClose) createQuery(hql, params).uniqueResult();
	}
	/**
	 * 功能介绍 : 获取网点的日结日期。<br>
	 * <b>日期：</b>2017年7月6日<br>
	 * @version 1.0
	 * @author liyh
	 */
	public FcDayClose getDayCloseDateByType(String netId, String overType) {
		//TODO 这张表的数据受 sys_corp 的 netCode 影响
		String hql = " from FcDayClose where netCode = :netId and overType = :overType ";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("netId", netId);
		params.put("overType", overType);
		return (FcDayClose) createQuery(hql, params).uniqueResult();
	}
	/**
	 * 跟新日结日期
	 * @param closeDate 当前日结VO对象
	 * @param userId 当前用户id
	 * @param lastCloseDate 上一次日结日期
	 * @param id 主键id
	 * @return 跟新记录数
	 */
	public int updateCloseDate(Date closeDate, String userId, Date lastCloseDate,String id){
		StringBuilder sql = new StringBuilder();
		sql.append("update FC_DAY_CLOSE ");
		sql.append("set CLOSE_DATE=:closeDate, ");
		sql.append("    LAST_CLOSE_DATE = :lastCloseDate, ");
		sql.append("    UPDATE_BY = :updateBy, ");
		sql.append("    UPDATE_TIME = :updateTime ");
		sql.append("where ID = :id ");
		sql.append("  and CLOSE_DATE = :lastCloseDate ");
		Map params = new HashMap();
		params.put("id", id);
		params.put("closeDate", closeDate);
		params.put("lastCloseDate", lastCloseDate);
		params.put("updateBy", userId);
		params.put("updateTime", DateUtils.getDate());
		return createSQLQuery(sql.toString(), params).executeUpdate();
	}
	
}
