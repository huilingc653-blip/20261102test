package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvPrintMatchRule;
import com.bytter.cashVoucher.vo.BvPrintMatchRuleVo;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证打印格式匹配规则
* @author onlineGenerator
* @date 2017-05-31 15:25:46
* @version 1.0
*
*/
@Repository
public class BvPrintMatchRuleDao extends BaseDao<BvPrintMatchRule,String>{
	/**
	 * 根据打印格式主信息ID
	 * 获取凭证打印格式匹配规则信息
	 *
	 * @return list集合
	 */
	public List<BvPrintMatchRule> listPrintMatchRuleInfoByPrintId(String printId) {
//		String hql="from BvPrintMatchRule where printId=:printId order by ordernum asc";
//		Map<String,Object> params=new HashMap<>();
//		params.put("printId", printId);
//		List<BvPrintMatchRule> list=this.list(hql, params);
//		this.getSession().clear();
//		return list;
		StringBuilder sql=new StringBuilder("select m.id as id,m.print_Id as printId,m.valid_Sign as validSign,m.code as code,d.id as codeId,d.field_name as codeName,")
				.append("m.match_val as matchVal,vt.name as vouTidName,m.match_rule as matchRule ")
				.append("from bv_print_match_rule m ")
				.append("join bv_print_format p on p.id=m.print_id ")
				.append("left join bv_voutype_data_field d on m.code=d.field_code and d.vou_tid=p.vou_tid ") 
				.append("LEFT JOIN bv_voucher_type vt on vt.id = m.match_val ")
				.append("where m.print_Id=:printId ")
				.append("order by m.ordernum asc ");
		Map<String,Object> params=new HashMap<>();
		params.put("printId", printId);
		List<BvPrintMatchRule> listInfo= this.listBySQLAliasToBean(sql.toString(), params, BvPrintMatchRule.class);
		return listInfo;
	}
	
	/**
	 * 根据pinrtId批量删除数据
	 * @return
	 */
	public int batchDeleteByPrintIds(Map<String,Object> params){
		String hql="delete from BvPrintMatchRule where printId in(:pinrtIds)";
		int num=this.batchExecute(hql, params);
		return num;
	}
	/**
	 * 根据pinrtId批量逻辑删除数据
	 * @return
	 */
	public int batchLogicDeleteByPrintIds(Map<String,Object> params){
		String hql="update BvPrintMatchRule set validSign=:validSign where id in(:ids)";
		params.put("validSign", "2");
		int num=this.batchExecute(hql, params);
		return num;
	}
	
	/**
	 * 根据套打格式ID获取可以匹配的匹配规则数据
	 * @param printId
	 * @return
	 */
	public List<BvPrintMatchRuleVo> getCanMatchInfoByPrintId(String printId){
		StringBuilder sql=new StringBuilder("select m.code as code,d.id as codeId,m.match_val as matchVal,m.match_rule as matchRule ")
				.append("from bv_print_match_rule m ")
				.append("join bv_print_format p on p.id=m.print_id ") 
				.append("join bv_voutype_data_field d on m.code=d.field_code and d.vou_tid=p.vou_tid ") 
				.append("where m.valid_sign=:valid_sign ")
				.append("and m.print_Id=:printId ")
				.append("order by m.ordernum asc ");
		Map<String,Object> params=new HashMap<>();
		params.put("valid_sign", "1");
		params.put("printId", printId);
		List<BvPrintMatchRuleVo> listInfo= this.listBySQLAliasToBean(sql.toString(), params, BvPrintMatchRuleVo.class);
		return listInfo;
	}
	
}
