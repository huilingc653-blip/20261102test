package com.bytter.biz.ttpay.dao;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Repository;
import com.bytter.biz.ttpay.entity.TpLog;
import com.bytter.core.base.BaseDao;
/**
 * @Title: Dao
 * @Description: 第三方支付日志Dao
 * @author zhengg
 * @date 2019-09-24 15:43:54
 * @version 1.0
 *
 */
@Repository
public class TpLogDao extends BaseDao<TpLog,String>{
	/**
	 * 查询满足quoteId的日志记录数据
	 * @param quoteIds
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<TpLog> listTpLog(String[] quoteIds){
		Map<String,Object> params=new HashMap<>();
		params.put("quoteIds", quoteIds);
		String hql="from TpLog where quoteId in (:quoteIds) order by createDate desc";
		return this.listByHQL(hql, params);
	}
}
