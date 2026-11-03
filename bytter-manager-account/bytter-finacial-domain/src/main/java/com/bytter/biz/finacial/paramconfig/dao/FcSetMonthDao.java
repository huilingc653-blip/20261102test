package com.bytter.biz.finacial.paramconfig.dao;
import com.bytter.biz.finacial.paramconfig.entity.FcSetMonth;
import com.bytter.biz.finacial.paramconfig.vo.FcSetMonthVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.EmptyUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 月结区间
* @author onlineGenerator
* @date 2017-05-24 14:18:47
* @version 1.0
*
*/
@Repository
public class FcSetMonthDao extends BaseDao<FcSetMonth,String>{
	
	/**
	 * 根据网点和月份查询月结区间
	 */
	public List<FcSetMonthVO> listFcSetMonth(FcSetMonth fsm){
		StringBuilder sb = new StringBuilder();
		/*sb.append("select f.id as id ,f.net_id as netId,f.over_month as overMonth,f.date_start as dateStart,f.date_end as dateEnd,c.name as corpName " );
		sb.append("from fc_set_month f  ");
		sb.append(" left join sys_corp c on f.net_id = c.id where 1 = 1 and c.type=0");*/
		//c.name as corpName；条件 c.type=0
		sb.append("select f.id as id ,f.net_id as netId,f.over_month as overMonth,f.date_start as dateStart,f.date_end as dateEnd ");
		sb.append("from fc_set_month f ");
		sb.append("where 1 = 1 ");
		if(EmptyUtils.isNotEmpty(fsm.getOverMonth())){
			String substring = fsm.getOverMonth().substring(0, 4);
			sb.append(" and f.over_month like '");
			sb.append(substring);
			sb.append("%'");
		}
		if(EmptyUtils.isNotEmpty(fsm.getNetId())){
			sb.append(" and f.net_id ='"+fsm.getNetId()+"'");
		}
		sb.append(" order by f.over_month");
	 	return listBySQLAliasToBean(sb.toString(),null,FcSetMonthVO.class);  
	}
	
	/**
	 * 获取所有的网点
	 * @param netId
	 * @return
	 * 没有被调用。注释
	 */
	/*public Map getAllCorp(String netId){
		StringBuilder sb = new StringBuilder();
		Map<String,String> val = new HashMap<String,String>();
		sb.append("select c.name as name ,c.id as id from sys_corp c where 1 = 1 and c.type=0" );
		if(EmptyUtils.isNotEmpty(netId)){
			sb.append(" and c.id like :id");
			val.put("id",netId );
		}
		sb.append(" order by c.id");
		SQLQuery sq = createSQLQuery(sb.toString(),val);
		sq.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		return (Map) sq.list().get(0);
	}*/
	
	/**
	 * 功能介绍 : 获取查询网点月结月份的月结区间。<br>
	 * <b>日期：</b>2017年7月17日<br>
	 * @version 1.0
	 * @author liyh 
	 */
	public FcSetMonth getNetSetMonth(String netId, String overMonth) {
		StringBuilder hql = new StringBuilder();
		Map<String,String> params = new HashMap<String,String>();
		hql.append(" from FcSetMonth fs where fs.netId = :netId and overMonth = :overMonth ");
		params.put("netId", netId);
		params.put("overMonth", overMonth);
		List<FcSetMonth> list = listByHQL(hql.toString(), params);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
}
