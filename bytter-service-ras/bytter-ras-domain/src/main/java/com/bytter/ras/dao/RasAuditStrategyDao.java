package com.bytter.ras.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.bytter.ras.entity.RasAuditStrategy;
import com.bytter.ras.vo.RasAuditStrategyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;

/**
 * 
* @Title:  营收稽核策略维护数据库操作类
* @author: zhengg   
* @date:   2019-5-15 下午5:56:08   
 */
@Repository
public class RasAuditStrategyDao extends BaseDao<RasAuditStrategy,String>{

	
	/**
	 * 
	* @Title: 稽核规则获取策略信息查询列表  
	* @param  input
	* @return DataTablesOutput    返回类型  
	* @throws
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" ID,NAME,PAY_TYPE as PAYTYPE,SORT_NO as SORTNO,VALID_SIGN as VALIDSIGN,RULES,RMK,CREATE_DATE as CREATEDATE");
		sql.append(" from RAS_AUDIT_STRATEGY ");
		sql.append(" where 1=1 ");
		Map<String,Object> params=new HashMap<String,Object>();
		return getDynamicBySQL(input, sql.toString(),RasAuditStrategyVO.class,"", false,params);
	}
	
	/**
	 * 通过缴款方式和稽核规则获取策略信息
	 * @param payType 缴款方式
	 * @param rules 稽核规则
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<RasAuditStrategy> getRasAuditStrategyListByPayTypeAndRules(String payType,String rules,String id){
		Map<String,Object> params=new HashMap<>();
		String hql="from RasAuditStrategy where payType=:payType and rules=:rules ";
		params.put("payType", payType);
		params.put("rules", rules);
		if(StringUtils.isNotEmpty(id)){
			hql+=" and id!=:id ";
			params.put("id", id);
		}
		return this.listByHQL(hql, params);
	}
	

	/**
	 * 通过查询条件获取对应维护好的策略信息
	 * @param params 查询参数
	 * @return 符合条件的策略列表
	 */
	@SuppressWarnings("unchecked")
	public List<RasAuditStrategy> getRasAuditStrategyList(Map<String,Object> params){
		StringBuilder hql=new StringBuilder("from RasAuditStrategy where 1=1"); 
		if(params.containsKey("wPayTypes")){
			hql.append(" and payType in (:wPayTypes) ");
		}
		if(params.containsKey("wPayType")){
			hql.append(" and payType=:wPayType ");
		}
		if(params.containsKey("wValidSign")){
			hql.append(" and validSign=:wValidSign ");
		}
		hql.append(" order by sortNo asc,createDate desc ");
		return this.listByHQL(hql.toString(), params);
	}
}
