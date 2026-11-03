package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbLog;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据日志
* @author onlineGenerator
* @date 2017-06-21 15:43:05
* @version 1.0
*
*/
@Repository
public class AbLogDao extends BaseDao<AbLog,String>{
	/**
	 * 查询满足quoteId的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	public List<AbLog> listAbLog(String[] quotezIds,String[] quoteIds,String[] idsAll){
		String hql="from AbLog where logType != '95'";
		Map<String,Object> params=new HashMap<>();
		if(StringUtils.isNotEmpty(quotezIds)){
			params.put("quotezIds", quotezIds);
			hql+="and quotezId in (:quotezIds) ";
		}
		if(StringUtils.isNotEmpty(quoteIds)){
			params.put("quoteIds", quoteIds);
			hql+="and quoteId in (:quoteIds) ";
		}
		if(StringUtils.isNotEmpty(idsAll)){
			params.put("idsAll", idsAll);
			hql+="and (quoteId in (:idsAll) or quotezId in (:idsAll) ) ";
		}
		hql+=" order by createDate asc ";
		return this.listByHQL(hql, params);
	}
}
