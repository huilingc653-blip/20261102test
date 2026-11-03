package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvVoutypeScene;
import com.bytter.cashVoucher.vo.BvPrintFormatVo;
import com.bytter.cashVoucher.vo.BvVoutypeSceneVO;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证类型打印场景
* @author zhengg
* @date 2020-04-03 20:50:38
* @version 1.0
*
*/
@Repository
public class BvVoutypeSceneDao extends BaseDao<BvVoutypeScene,String>{
	
	
	/**
	 * 根据vouTid获取有效的打印场景明细信息                                       
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<BvVoutypeScene> listVoutypeSceneValidByVouTid(String vouTid){
		Map<String,Object> params=new HashMap<>();
		params.put("vouTid",vouTid);
		params.put("validSign","1");
	  return this.listByHQL("from BvVoutypeScene where vouTid=:vouTid and validSign=:validSign order by ordernum asc",params);
	}
	
	/**
	 * 根据条件查询凭证种类打印场景信息
	 * @param vouTid
	 * @return
	 */
	public List<BvVoutypeSceneVO> listBvVoutypeSceneByVouTid(String vouTid) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" V.ID as id,V.name,V.rmk,V.VALID_SIGN as validSign ");
		sql.append(" from BV_VOUTYPE_SCENE V");
		sql.append(" WHERE V.vou_Tid=:vouTid");
		sql.append(" order by ordernum asc");
		params.put("vouTid", vouTid);
		List<BvVoutypeSceneVO> listInfo = this.listBySQLAliasToBean(sql.toString(), params,BvVoutypeSceneVO.class);
		return listInfo;
	}
	
	/**
	 * 根据vouTid删除凭证类型打印场景明细
	 * @param vouTid
	 */
	public void deleteByVouTid(String vouTid) {
		Map<String, Object> params = new HashMap<>();
		params.put("vouTid", vouTid);
		String hql = "delete BvVoutypeScene where vouTid=:vouTid";
		this.createQuery(hql, params).executeUpdate();
	}
	
	public List<BvVoutypeScene> listBvPrintFormatVoById(String vouTid) {
		StringBuilder sql=new StringBuilder();
		sql.append(" select * from BV_VOUTYPE_SCENE V where V.vou_tid=:vouTid");
		Map<String, String> params=new HashMap<>();
		params.put("vouTid", vouTid);
		List<BvVoutypeScene> listInfo=this.listBySQLAliasToBean(sql.toString(), params, BvVoutypeScene.class);
		return listInfo;
	}
	
}
