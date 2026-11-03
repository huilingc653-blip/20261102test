package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvLog;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 空白凭证日志
* @author onlineGenerator
* @date 2017-05-31 15:43:54
* @version 1.0
*
*/
@Repository
public class BvLogDao extends BaseDao<BvLog,String>{
	
	/**
	 * 查询满足quoteId的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	public List<BvLog> listBvLog(String[] quoteIds){
		Map<String,Object> params=new HashMap<>();
		params.put("quoteIds", quoteIds);
		String hql="from BvLog where quoteId in (:quoteIds) order by createDate asc";
		return this.listByHQL(hql, params);
	}
	
}
