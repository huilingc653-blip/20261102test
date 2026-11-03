package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvPrintVouOpinfo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证打印操作
* @author onlineGenerator
* @date 2017-06-13 11:18:30
* @version 1.0
*
*/
@Repository
public class BvPrintVouOpinfoDao extends BaseDao<BvPrintVouOpinfo,String>{
	/**
	 * 凭证打印页面列表查询信息
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<Map<String, Object>> listBvPrintVouOpInfo(DataTablesInput input,String oldSql,Map<String,Object> params) {
		StringBuilder sql=new StringBuilder("select * from (");
		sql.append(oldSql);
		sql.append(") a where 1=1 ");
		return (DataTablesOutput<Map<String, Object>>) this.getDynamicMapBySQL(input, sql.toString(), "", false,params);
	}
	
	/**
	 * 根据套打操作类型ID获取数据信息
	 * @param popId
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Map getBvPrintVouOpInfo(String sql,String popId,String dataId,String vouTid,Map<String,Object> params){
		sql="select * from ("+sql+ ") info where 1=1 ";
		if(StringUtils.isEmpty(popId)||"null".equals(popId)){
			sql+=" and info.idpop is null ";
		}else{
			sql+=" and info.idpop=:popId ";
			params.put("popId", popId);
		}
		if(StringUtils.isEmpty(dataId)){
			sql+=" and info.id is null ";
		}else{
			sql+=" and info.id=:dataId ";
			params.put("dataId", dataId);
		}
		return this.getBySQL(sql, params);
	}
	
	/**
	 *  根据数据ID和凭证类型获取打印操作实体
	 * @param dataId
	 * @param vouTid
	 * @return
	 */
	public BvPrintVouOpinfo getBvPrintVouOpinfoByDataIdVouTid(String dataId,String vouTid){
		String hql="from BvPrintVouOpinfo where dataId=:dataId and vouTid=:vouTid";
		Map<String,String> params=new HashMap<String, String>();
		params.put("dataId", dataId);
		params.put("vouTid", vouTid);
		List<BvPrintVouOpinfo> list=this.listByHQL(hql, params);
		if(list!=null&&!list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
}
