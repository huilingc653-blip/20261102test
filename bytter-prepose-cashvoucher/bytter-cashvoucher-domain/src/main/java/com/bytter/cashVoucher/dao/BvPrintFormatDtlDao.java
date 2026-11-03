package com.bytter.cashVoucher.dao;

import com.bytter.cashVoucher.entity.BvPrintFormatDtl;
import com.bytter.core.base.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证打印格式明细
* @author onlineGenerator
* @date 2017-05-31 15:26:09
* @version 1.0
*
*/
@Repository
public class BvPrintFormatDtlDao extends BaseDao<BvPrintFormatDtl,String>{
	
	/**
	 * 根据打印格式主信息ID
	 * 获取凭证打印格式打印明细信息
	 *
	 * @return list集合
	 */
	public List<BvPrintFormatDtl> listPrintFmtDtlInfoByPrintId(String printId) {
//		String hql="from BvPrintFormatDtl where printId=:printId order by ordernum asc";
//		Map<String,Object> params=new HashMap<>();
//		params.put("printId", printId);
//		List<BvPrintFormatDtl> listInfo=this.list(hql, params);
//		this.getSession().clear();
//		return listInfo;
		StringBuilder sql=new StringBuilder("select ")
				.append("d.id,d.is_print as isPrint,d.code as code,f.field_name as codeName,f.id as codeId,d.name,d.print_coord as printCoord,d.font_size as fontSize,d.font_space as fontSpace,")
				.append("d.char_size_br as charSizeBr,d.prefix_val as prefixVal,d.other_val as otherVal,d.val_print_type as valPrintType,d.val_print_fmt as valPrintFmt ")
				.append("From bv_print_format_dtl d ")
				.append("join bv_print_format p on p.id=d.print_id ") 
				.append("join bv_voutype_data_field f on d.code=f.field_code and f.vou_tid=p.vou_tid ") 
				.append("where d.print_Id=:printId ")
				.append("order by d.ordernum asc ");
		Map<String,Object> params=new HashMap<>();
		params.put("printId", printId);
		List<BvPrintFormatDtl> listInfo= this.listBySQLAliasToBean(sql.toString(), params, BvPrintFormatDtl.class);
		return listInfo;
	}
	/**
	 * 编辑pinrtId批量删除数据
	 * @return
	 */
	public int batchDeleteByPrintIds(Map<String,Object> params){
		String hql="delete from BvPrintFormatDtl where printId in(:pinrtIds)";
		int num=this.batchExecute(hql, params);
		return num;
	}
	
	/**
	 * 根据套打格式ID获取可以打印明细信息
	 * @param printId
	 * @return
	 */
	public List<BvPrintFormatDtl> getIsPrintFmtInfoByPrintId(String printId){
		StringBuilder sql=new StringBuilder("select ")
				.append("d.id,d.is_print as isPrint,d.code as code,f.id as codeId,d.name,d.print_coord as printCoord,d.font_size as fontSize,d.font_space as fontSpace,")
				.append("d.char_size_br as charSizeBr,d.prefix_val as prefixVal,d.other_val as otherVal,d.val_print_type as valPrintType,d.val_print_fmt as valPrintFmt ")
				.append("From bv_print_format_dtl d ")
				.append("join bv_print_format p on p.id=d.print_id ") 
				.append("join bv_voutype_data_field f on d.code=f.field_code and f.vou_tid=p.vou_tid ") 
				.append("where d.is_print=:is_print ")
				.append("and d.print_Id=:printId ")
				.append("order by d.ordernum asc ");
		Map<String,Object> params=new HashMap<>();
		params.put("is_print", "1");
		params.put("printId", printId);
		List<BvPrintFormatDtl> listInfo= this.listBySQLAliasToBean(sql.toString(), params, BvPrintFormatDtl.class);
		return listInfo;
	}
}
