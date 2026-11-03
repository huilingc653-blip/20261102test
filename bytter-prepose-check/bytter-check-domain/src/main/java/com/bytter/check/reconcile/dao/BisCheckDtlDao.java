package com.bytter.check.reconcile.dao;

import com.bytter.check.reconcile.entity.BisCheckDtl;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.ListUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Repository
public class BisCheckDtlDao extends BaseDao<BisCheckDtl,String>{
	/**
	 * 根据lastId修改状态
	 * @param params
	 * @return
	 */
	public int updateVoucherStatByIds(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("update BisCheckDtl " 
				+ " set updateDate = :updateDate ");
		if(params.containsKey("updateBy")){
			hql.append(",updateBy = :updateBy ");
		}
		if(params.containsKey("mobileNo")){
			hql.append(",mobileNo = :mobileNo ");
		}
		if(params.containsKey("checkDate")){
			hql.append(",checkDate = :checkDate ");
		}
		if(params.containsKey("rmk")){
			hql.append(",rmk = :rmk ");
		}
		if(params.containsKey("checkStatus")){
			hql.append(",checkStatus = :checkStatus ");
		}
		if(params.containsKey("checkResult")){
			hql.append(",checkResult = :checkResult ");
		}
		if(params.containsKey("reasonId")){
			hql.append(",reasonId = :reasonId ");
		}
		hql.append(",voucherStat = :voucherStat ");
		hql.append(" where id in (:ids) ");
		return this.batchExecute(hql.toString(), params);
	}
	
	public boolean getObjectByResonId(String id){
		Map<String,Object> params=new HashMap<>();
		String hql=" from BisCheckDtl where reasonId in(:reasonId)";
		params.put("reasonId",id);
		@SuppressWarnings("unchecked")
		List<BisCheckDtl> listVo = this.listByHQL(hql, params);
		return ListUtils.isNotNullOrEmpty(listVo)?true:false;
	}
	
	
	
}
