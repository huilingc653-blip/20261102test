package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvVoutypeDataField;
import com.bytter.cashVoucher.vo.BvVoutypeDataFieldVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: BV_VOUTYPE_DATA_FIELD
* @author onlineGenerator
* @date 2017-05-26 20:50:38
* @version 1.0
*
*/
@Repository
public class BvVoutypeDataFieldDao extends BaseDao<BvVoutypeDataField,String>{
	
	
	/**
	 * 根据vouTid获取有效的取值字段明细信息                                       
	 * @return
	 */
	public List<BvVoutypeDataField> listVoutypeDataFieldValidByVouTid(String vouTid){
		Map<String,Object> params=new HashMap<>();
		params.put("vouTid",vouTid);
		params.put("validSign","1");
	  return this.listByHQL("from BvVoutypeDataField where vouTid=:vouTid and validSign=:validSign order by ordernum asc",params);
	}
	
	/**
	 * 根据条件查询凭证种类取值字段信息
	 * @param vouTid
	 * @return
	 */
	public List<BvVoutypeDataFieldVO> listBvVoutypeDataField(String vouTid) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" V.ID as id,V.field_code as fieldCode,V.FIELD_NAME as fieldName,V.DATA_TYPE as dataType,V.IS_SHOW as isShow,V.VALID_SIGN as validSign,V.dict_group as dictGroup ");
		sql.append(" from BV_VOUTYPE_DATA_FIELD V");
		sql.append(" WHERE V.vou_Tid=:vouTid");
		sql.append(" And V.field_code not in (:fieldCodes)");
		sql.append(" order by ordernum asc");
		String[] fieldCodes={"popopcode","popopname","popopdate"};
		params.put("vouTid", vouTid);
		params.put("fieldCodes",fieldCodes);
		List<BvVoutypeDataFieldVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params,BvVoutypeDataFieldVO.class);
		return listInfo;
	}
	
	/**
	 * 根据vou_Tid查询所有凭证种类取值字段信息
	 * @param vouTid
	 * @return
	 */
	public List<BvVoutypeDataFieldVO> listBvVoutypeDataFieldByVouTid(String vouTid) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" V.ID as id,V.field_code as fieldCode,V.FIELD_NAME as fieldName,V.DATA_TYPE as dataType,V.IS_SHOW as isShow,V.VALID_SIGN as validSign,V.dict_group as dictGroup ");
		sql.append(" from BV_VOUTYPE_DATA_FIELD V");
		sql.append(" WHERE V.vou_Tid=:vouTid");
		sql.append(" order by ordernum asc");
		params.put("vouTid", vouTid);
		List<BvVoutypeDataFieldVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params,BvVoutypeDataFieldVO.class);
		return listInfo;
	}
	
	/**
	 * 根据vouTid删除凭证类型取值字段明细
	 * @param vouTid
	 */
	public void deleteByVouTid(String vouTid) {
		Map<String, Object> params = new HashMap<>();
		params.put("vouTid", vouTid);
		String hql = "delete BvVoutypeDataField where vouTid=:vouTid";
		this.createQuery(hql, params).executeUpdate();
	}
	
	
}
