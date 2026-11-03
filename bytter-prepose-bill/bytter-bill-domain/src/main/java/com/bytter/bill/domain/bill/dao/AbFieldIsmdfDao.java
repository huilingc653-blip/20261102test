package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbFieldIsmdf;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**   
 * @Title: Entity
 * @Description: 票据业务字段是否可以修改配置DAO
 * @author zhengg
 * @date 2020-04-26 14:29:10
 * @version V1.0   
 *
 */
@Repository
public class AbFieldIsmdfDao extends BaseDao<AbFieldIsmdf,String>{
	
	/**
	 * 根据业务Id获取是否允许编辑配置信息信息
	 * @param busId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<AbFieldIsmdf> getAbFieldIsmdfByBusId(String busId){
		if(StringUtils.isEmpty(busId)){
			return null;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("busId", busId);
		String hql="from AbFieldIsmdf where busId=:busId order by createTime";
		return this.listByHQL(hql, params);
	}
	
	/**
	 * 根据busId删除所有信息
	 * @param busId
	 * @return
	 */
	public int deleteAbFieldIsmdfByBusId(String busId){
		if(StringUtils.isEmpty(busId)){
			return 0;
		}
		Map<String,Object> params=new HashMap<>();
		params.put("busId", busId);
		return this.deleteAbFieldIsmdfByParams(params);
	}
	
	/**
	 * 根据params删除所有信息
	 * @param params(busId,fieldCode)
	 * @return
	 */
	public int deleteAbFieldIsmdfByParams(Map<String,Object> params){
		if(params==null||params.isEmpty()||params.containsKey("busId")){
			return 0;
		}
		StringBuffer sql=new StringBuffer(" delete from AB_FIELD_ISMDF where bus_id=:busId ");
		if(params.containsKey("fieldCode")){
			sql.append(" and field_Code=:fieldCode ");
		}
		return this.batchSQLExecute(sql.toString(), params);
	}
}
