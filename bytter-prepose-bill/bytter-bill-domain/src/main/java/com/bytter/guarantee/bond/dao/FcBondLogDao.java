package com.bytter.guarantee.bond.dao;


import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.guarantee.bond.entity.FcBondLog;
import com.bytter.guarantee.vo.FcBondLogVO;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author fusb
 *	@version 1.0
 *  @Title FcBondLogDao 
 *  @Description 保证进行日志DAO
 *  @date 2018年9月12日
 */
@Repository
public class FcBondLogDao extends BaseDao<FcBondLog, String>{
	/**
	 * 通过id查询得到日志列表
	 * @param bondIds 
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listMsgData(String bondIds,DataTablesInput input){
		Map<String,String> params = new  HashMap<String,String>();
		String[] bondId = bondIds.split(",");
		List<String> listBondId = Arrays.asList(bondId);
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT BT.ID,BT.BOND_ID,BT.MSG_TYPE,BT.MONEY,BT.CREATE_BY,BT.CREATEBY_NAME as CREATEBYNAME,");
		sql.append("BT.CREATE_DATE,BT.MESSAGE FROM FC_BOND_LOG  BT  WHERE  1=1 AND  ");
		sql.append(getSQLIn(listBondId,900,"BOND_ID"));
		sql.append("  ORDER BY  CREATE_DATE DESC ");
		return getDynamicBySQL(input, sql.toString(), FcBondLog.class, "", false,params);
	}
	
	 /**
	   * 根据保证金id,从日志表FC_BOND_LOG中得到相应的操作信息
	   * @param id
	   * @return
	   */
	  public  List<FcBondLogVO> getListOpMesageByBondId(String id){
		  StringBuilder sql = new StringBuilder();				 
		  sql.append("SELECT ID AS id,BOND_ID AS bond_id,MSG_TYPE AS msgType,MONEY AS money,MESSAGE AS message, ");
		  sql.append("CREATE_BY AS createBy,CREATEBY_NAME AS createByName,CREATE_DATE AS createDate FROM FC_BOND_LOG  ");
		  sql.append(" WHERE BOND_ID=:id  ORDER BY  CREATE_DATE  DESC ");
		  Map<String,String> params = new HashMap<String,String>();
		  params.put("id", id);
		  return listBySQLAliasToBean(sql.toString(), params, FcBondLogVO.class);
	  }
}
