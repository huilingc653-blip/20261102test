package com.bytter.cashVoucher.dao;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.entity.BvVouInfoDtl;
import com.bytter.cashVoucher.vo.BvVouInfoDtlVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 凭证入库明细表
 * @author onlineGenerator
 * @date 2017-06-07 15:42:20
 * @version 1.0
 *
 */
@Repository
public class BvVouInfoDtlDao extends BaseDao<BvVouInfoDtl,String>{
	/**
	 * 查询入库凭证明细
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BvVouInfoDtlVo> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b.id as id,b.input_id as inputId,b.corp_id as corpId,b.sbox_id as sboxId,b.bank_acc as bankAcc,b.bank_name as bankName,");
		sql.append("b.bank_id as bankId,b.vou_tid as vouTid,b.vou_no as vouNo,b.input_date as inputDate,b.vou_status as vouStatus,b.cur_Id as curId,b.cur_name as curName,");
		sql.append("b.rmk as rmk,b.create_name as createName,b.corp_code as corpCode,b.corp_name as corpName,c.name as storageBoxName,v.name as voucherTypeName");
		sql.append(" from BV_VOU_INFO_DTL b ");
		sql.append(" LEFT JOIN BV_STORAGE_BOX c ON b.sbox_id = c.id ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE v ON b.vou_tid = v.id ");
		sql.append(" WHERE 1=1 ");
		if(input.getFormdata().containsKey("eqs_id")){
			sql.append(" and b.id = '"+input.getFormdata().get("eqs_id")+"'");
		}
		//库箱管理员才能操作
		sql.append(" and c.keeper_uid = :keeperUid ");
		//已作废和已出售的不显示
		sql.append(" and b.vou_status <> :vouStatus06 ");
		sql.append(" and b.vou_status <> :vouStatus02 ");
		Map<String, Object> params = new HashMap<> ();
		params.put("keeperUid",userId);
		params.put("vouStatus06",BlankvouConstant.BVVOUINFODTL_STATUS_TOVOID);
		params.put("vouStatus02",BlankvouConstant.BVVOUINFODTL_STATUS_SELL);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), BvVouInfoDtlVo.class, "", false,params);
	}
	/**
	 * 根据ID查询空白凭证明细
	 * @param id
	 * @return
	 */
	public List<BvVouInfoDtlVo> getDataTablesRow(String id) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b.id as id,b.input_id as inputId,b.corp_id as corpId,b.sbox_id as sboxId,b.bank_acc as bankAcc,b.bank_name as bankName,");
		sql.append("b.bank_id as bankId,b.vou_tid as vouTid,b.vou_no as vouNo,b.input_date as inputDate,b.vou_status as vouStatus,b.cur_Id as curId,b.cur_name as curName,");
		sql.append("b.rmk as rmk,b.create_name as createName,b.corp_name as corpName,c.name as storageBoxName,v.name as voucherTypeName");
		sql.append(" from BV_VOU_INFO_DTL b ");
		sql.append(" LEFT JOIN BV_STORAGE_BOX c ON b.sbox_id = c.id ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE v ON b.vou_tid = v.id ");
		sql.append(" WHERE b.id = :id ");
		Map<String, Object> params=new HashMap<>();
		params.put("id", id);
		List<BvVouInfoDtlVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, BvVouInfoDtlVo.class);
		return listInfo;
	}
	/**
	 * 空白凭证-批量操作
	 * @param params
	 * @return
	 */
	public int batchDeleteBvVouInfoDtl(Map<String,Object> params,ICurrentUser user){
		String hql="update BvVouInfoDtl set updateBy = :updateBy "
				+ ",updateName = :updateName "
				+ ",updateDate = :updateDate "
				+ ",vouStatus = :vouStatus ";
		if(params.get("reason")!=null){
			hql = hql+",reason = :reason ";
		}
		hql = hql + "where id in (:ids)";
		params.put("updateName", user.getUsername());
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", new Date());
		return this.batchExecute(hql, params);
	}
	/**
	 * 空白凭证-批量移库
	 * @param params
	 * @return
	 */
	public int batchTransfBvVouInfoDtl(Map<String,Object> params,ICurrentUser user){
		String hql="update BvVouInfoDtl set updateBy = :updateBy "
				+ ",updateName = :updateName "
				+ ",updateDate = :updateDate "
				+ ",sboxId = :sboxId "
				+ "where id in (:ids)";
		params.put("updateName", user.getUsername());
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", new Date());
		int num=this.batchExecute(hql, params);
		return num;
	}
	/**
	 * 判断用户有管理权限的库箱里是否有对应凭证号
	 *
	 * @param loginId 登录用户ID
	 * @param vouTid  凭证类型ID
	 * @param vouNo   凭证号
	 * @return boolean
	 */
	//TODO SYS服务提供
	public boolean checkVouNo(String loginId, String vouTid, String vouNo){
		String sql="select b.id from BV_VOU_INFO_DTL b "
				+ "left join bv_storage_box s on b.sbox_id = s.id "
				+"where s.valid_sign<>2 "
				+"and s.keeper_uid = :login_id "
				+"and b.vou_tid = :vou_tid "
				+"and b.vou_no = :vou_no "
				+"and b.vou_status = '01' ";
		SQLQuery query=createSQLQuery(sql);
		query.setString("login_id", loginId);
		query.setString("vou_tid", vouTid);
		query.setString("vou_no", vouNo);
		if(!query.list().isEmpty()){
			return true;
		}
		return false;
	}
	/**
	 * 获取这个用户有管理权限的库箱里面对应的凭证类型的凭证号,按照升序获取，如果传递了凭证号需要判断凭证号需要大于这个凭证号
	 * @param loginId    登录用户名
	 * @param vouTid       凭证类型ID
	 * @param vouNo        凭证号
	 * @param vouNum       凭证号数量
	 * @param bankTypeName 所属银行
	 * @param bankAcc      所属账号
	 * @param corpId       单位id
	 * @return boolean
	 */
	@SuppressWarnings("rawtypes")
	//TODO 整体改造
	public List<BvVouInfoDtl> getBvVouInfoDtls(String loginId, String vouTid, String vouNo, int vouNum,String bankTypeName,String bankAcc,String corpId){
		StringBuilder sql= new StringBuilder("select id,inputId,corpId,sboxId,bankAcc,bankId,")
				.append("vouTid,vouNo,inputDate,vouStatus,rmk,reason from (select ")
				.append("b.id as id,b.input_id as inputId,b.corp_id as corpId,b.sbox_id as sboxId, ")
				.append("b.bank_acc as bankAcc,b.bank_id as bankId,b.vou_tid as vouTid,b.vou_no as vouNo, ")
				.append("b.input_date as inputDate,b.vou_status as vouStatus,b.rmk as rmk,b.reason as reason ")
				.append("from BV_VOU_INFO_DTL b ")
				.append("left join bv_storage_box s on b.sbox_id = s.id ")
				.append("where 1=1 ")
				.append("and s.keeper_uid = :login_id ")
				.append("and b.vou_tid = :vou_tid ");
		if(StringUtils.isNotEmpty(bankTypeName)){
			sql.append("and b.type_name = :type_name ");
		}
		if(StringUtils.isNotEmpty(bankAcc)){
			sql.append("and (b.bank_acc = :bank_acc or b.bank_acc is null )");
		}
		if(StringUtils.isNotEmpty(vouNo)){
			sql.append("and b.vou_no >= :vou_no ");
		}
		if(StringUtils.isNotEmpty(corpId)){
			sql.append("and b.corp_id = :corp_id ");
		}
		sql.append("and b.vou_status = '01' order by ");
		sql.append(" b.vou_no asc ");
		if(StringUtils.isNotEmpty(bankAcc)){
			sql.append(" ,b.bank_Acc desc ");
		}
		sql.append(" ) info ");
		Map<String, Object> params=new HashMap<>();
		params.put("login_id", loginId);
		params.put("vou_tid", vouTid);
		params.put("type_name", bankTypeName);
		params.put("bank_acc", bankAcc);
		params.put("vou_no", vouNo);
		params.put("corp_id", corpId);
		List list=this.createSQLQuery(sql.toString(), params)
				.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
				.setMaxResults(vouNum).list();
		List<BvVouInfoDtl> listInfo=copyListAndInject(list, BvVouInfoDtl.class);
		return listInfo;
	}
	/**
	 * 凭证查询--只能查询此用户有管理权限的单位以及自己管理库箱的数据
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	//TODO 整体改造  这里包含sys_corp冗余字段
	public DataTablesOutput<BvVouInfoDtlVo> queryList(DataTablesInput input,String userId,List<String> corpAuthList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b.id as id,b.input_id as inputId,b.corp_id as corpId,b.sbox_id as sboxId,b.bank_acc as bankAcc,b.bank_name as bankName,");
		sql.append("b.bank_id as bankId,b.vou_tid as vouTid,b.vou_no as vouNo,b.input_date as inputDate,b.vou_status as vouStatus,b.cur_Id as curId,b.cur_name as curName,");
		sql.append("b.rmk as rmk,b.create_name as createName,b.corp_name as corpName,c.name as storageBoxName,v.name as voucherTypeName");
		sql.append(" from BV_VOU_INFO_DTL b ");
		sql.append(" LEFT JOIN BV_STORAGE_BOX c ON b.sbox_id = c.id ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE v ON b.vou_tid = v.id ");
		sql.append(" WHERE 1=1 and c.keeper_uid = :userId ");
		Map<String, Object> params = new HashMap<> ();
		params.put("userId",userId);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), BvVouInfoDtlVo.class, "b", false,params);
	}
}
