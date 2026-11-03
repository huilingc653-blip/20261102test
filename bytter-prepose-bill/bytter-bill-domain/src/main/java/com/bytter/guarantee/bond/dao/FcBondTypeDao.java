package com.bytter.guarantee.bond.dao;

import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.StringUtils;
import com.bytter.guarantee.bond.entity.FcBondType;
import com.bytter.guarantee.vo.FcBondTypeVO;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
/**
* @Title: Dao
* @Description: 保证金类型维护列表
*/
@Repository
public class FcBondTypeDao extends BaseDao<FcBondType,String>{
	public DataTablesOutput<FcBondTypeVO> fcBondTypeList(DataTablesInput input){
		StringBuilder sql=new StringBuilder();
		sql.append(" select b.*  ");
		sql.append(" from fc_bond_type b ");
		return dealDynamicBySQL(input,sql.toString(), FcBondTypeVO.class,"",true);
	}
	
	/**
	 * 根据id获取详情
	 * @param id
	 * @return
	 */
	public FcBondTypeVO getBondType(String id){
		StringBuilder sql=new StringBuilder();
		sql.append(" select * from fc_bond_type b ");
		sql.append("where b.id = :id");
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		return getBySQLAliasToBean(sql.toString(),params,FcBondTypeVO.class);
	}
	/**根据code查询类型*/
	public Boolean queryBondTypeByCode(String typeCode,String id){
		HashMap<String, Object> params = new HashMap<String, Object>();
		StringBuilder sql=new StringBuilder();
		sql.append(" select * from fc_bond_type b ");
		sql.append("where b.bond_type_code = :typeCode");
		if(StringUtils.isNotEmpty(id)){
			sql.append(" and b.id <> :id ");
			params.put("id", id);
		}
		params.put("typeCode", typeCode);
		FcBondTypeVO vo =  getBySQLAliasToBean(sql.toString(),params,FcBondTypeVO.class);
		if(vo != null){
			return false;
		}
		return true;
	}
	/**
	 * 获取有效保证金类型
	 */
	public List<FcBondTypeVO> getAllBondType(){
		StringBuilder sql=new StringBuilder();
		sql.append(" select * from fc_bond_type b ");
		sql.append("where b.valid_sign = '1' ");
		return listBySQLAliasToBean(sql.toString(),null,FcBondTypeVO.class);
	}
}
