package com.bytter.biz.fundwarn.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.fundwarn.entity.BtWarningLog;
import com.bytter.core.base.BaseDao;
/**
 * @Title: Dao
 * @Description: 预警策略数据操作类
 * @author zhenghj
 * @date 2018-09-27
 * @version 1.0
 *
 */
@Repository
public class BtWarningLogDao extends BaseDao<BtWarningLog,String> {
	
	/**
	 * 查询满足quoteId的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BtWarningLog> listBtWarningLog(String[] quoteIds){
		Map<String,Object> params=new HashMap<>();
		params.put("quoteIds", quoteIds);
		String hql="from BtWarningLog where quoteId in (:quoteIds) order by createDate asc";
		return this.listByHQL(hql, params);
	}
	
}
