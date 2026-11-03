package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;

/**
* @Title: Dao
* @Description: 营收稽核日志
* @author onlineGenerator
* @date 2019-04-29 15:43:05
* @version 1.0
*
*/
@Repository
public class RasLogDao extends BaseDao<RasLog,String>{
	/**
	 * 查询满足quoteId的日志记录数据
	 * @param quoteIds
	 * @param idsAll
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasLog> listRasLog(String[] quoteIds,String[] idsAll){
		String hql="from RasLog where 1=1 ";
		Map<String,Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(quoteIds)){
			params.put("quoteIds", quoteIds);
			hql+="and quoteId in (:quoteIds) ";
		}
		if(StringUtils.isNotEmpty(idsAll)){
			params.put("idsAll", idsAll);
			hql+="and id in (:idsAll) ";
		}
		hql+=" order by createDate asc ";
		return this.listByHQL(hql, params);
	}
}
