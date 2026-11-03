package com.bytter.guarantee.bond.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.guarantee.bond.entity.FcBondBal;
import com.bytter.guarantee.vo.FcBondBalVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
/**
 * 
 * @author luxy
 * @version 1.0
 * @Title:FcBondBalDao
 * @Description:
 * @date  2018年9月12日 下午1:39:06
 */
@Repository
public class FcBondBalDao extends BaseDao<FcBondBal,String>{
	
	/**
	 * 根据bondId获取保证金余额数据
	 */
	public FcBondBalVO getBalByBondId(String bondId){
		StringBuilder sql=new StringBuilder();
		sql.append("select * from fc_bond_bal where bond_id=:bondId");
		Map<String,String> params = new HashMap<String,String>();
		params.put("bondId", bondId);
		return getBySQLAliasToBean(sql.toString(), params, FcBondBalVO.class);
	}
	/**
	 * 根据bondId获取保证金余额数据
	 */
	public FcBondBal getBalById(String bondId){
		StringBuilder sql=new StringBuilder();
		sql.append("select * from fc_bond_bal where bond_id=:bondId");
		Map<String,String> params = new HashMap<String,String>();
		params.put("bondId", bondId);
		return getBySQLAliasToBean(sql.toString(), params, FcBondBal.class);
	}
	public void deleteByBondId(String bondId){
		String sql = " delete from fc_bond_bal where bond_id = :bondId ";
		Map<String,Object> param = new HashMap<>();
		param.put("bondId",bondId);
		createSQLQuery(sql,param).executeUpdate();
	}
}
