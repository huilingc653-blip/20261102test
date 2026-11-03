package com.bytter.ras.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasAuditInter;
import com.bytter.core.base.BaseDao;

/**
 * @author zhengg
 * @version 1.0
 * @Title: dao
 * @Description: 自动稽核时使用稽核表和营收实收对应中间表数据库操作类
 * @date 2019-06-19
 */
@Repository
public class RasAuditInterDao extends BaseDao<RasAuditInter, String> {
	
	/**
	 * 根据操作唯一ID删除中间表数据
	 */
	public int deleteByUnid(String unId){
		String hql="delete from RasAuditInter where unId=:unId";
		Map<String,String> params=new HashMap<>();
		params.put("unId", unId);
		return this.batchExecute(hql, params);
	}

	/**
	 * 根据操作唯一ID删除中间表数据
	 */
	public int getCountNumByUnid(String unId){
		String sql="select count(*) as num from RAS_AUDIT_INTER where un_Id=:unId";
		Map<String,String> params=new HashMap<>();
		params.put("unId", unId);
		@SuppressWarnings("rawtypes")
		Map map=this.getBySQL(sql, params);
		if(map!=null){
			if(map.containsKey("num")){
				if(map.get("num") instanceof BigDecimal){
					return ((BigDecimal)map.get("num")).intValue();
				}else if(map.get("num") instanceof Double){
					return ((Double)map.get("num")).intValue();
				}else{
					return Integer.parseInt(String.valueOf(map.get("num")));
				}
			}else if(map.containsKey("NUM")){
				if(map.get("NUM") instanceof BigDecimal){
					return ((BigDecimal)map.get("NUM")).intValue();
				}else if(map.get("NUM") instanceof Double){
					return ((Double)map.get("NUM")).intValue();
				}else{
					return Integer.parseInt(String.valueOf(map.get("NUM")));
				}
			}
		}
		return 0;
	}

}




















