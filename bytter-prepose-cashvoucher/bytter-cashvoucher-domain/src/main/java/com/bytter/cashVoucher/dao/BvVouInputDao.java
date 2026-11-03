package com.bytter.cashVoucher.dao;

import com.bytter.bill.api.bill.constant.BlankvouConstant;
import com.bytter.cashVoucher.entity.BvVouInput;
import com.bytter.cashVoucher.vo.BvVouInputVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.base.entity.ICurrentUser;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 凭证入库信息表
* @author onlineGenerator
* @date 2017-05-25 17:06:40
* @version 1.0
*
*/
@Repository
public class BvVouInputDao extends BaseDao<BvVouInput,String>{
	
	/**
	 * 查询入库凭证
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<BvVouInputVo> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userId) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b.id as id,b.corp_id as corpId,b.sbox_id as sboxId,b.bank_acc as bankAcc,b.bank_id as bankId,");
		sql.append("b.vou_tid as vouTid,b.rule_no as ruleNo,b.begin_no as beginNo,b.end_no as endNo,b.num_size as numSize,");
		sql.append("b.input_date as inputDate,b.status as status,b.rmk as rmk,b.corp_code as corpCode,b.corp_name as corpName,c.name as storageBoxName,");
		sql.append("v.name as voucherTypeName,b.cur_Id as curId,b.cur_name as curName,b.bank_name as bankName");
		sql.append(" from BV_VOU_INPUT b ");
		sql.append(" LEFT JOIN BV_STORAGE_BOX c ON b.sbox_id = c.id ");
		sql.append(" LEFT JOIN BV_VOUCHER_TYPE v ON b.vou_tid = v.id ");
		sql.append(" WHERE 1=1 ");
		if(input.getFormdata().containsKey("eqs_id")){
			sql.append(" and b.id = '"+input.getFormdata().get("eqs_id")+"'");
		}
		//库箱管理员才能操作
		sql.append(" and c.keeper_uid = :keeperUid ");
		//sql.append(" and ").append(corpAuthSql).append(" ");
		Map<String, Object> params = new HashMap<> ();
		params.put("keeperUid",userId);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), BvVouInputVo.class, "", false,params);
	}
	/**
	 * 凭证所属账户+凭证种类+票据号码确定票据的唯一性，系统限制重复录入重要空白凭证；
	 * 判断方式 首先，主数据BV_VOU_INPUT表判断
	 *     （   1、号码部分有四种重复情况：
	 *		  1 )与数据库的起始号码重复
	 *		      数据库起始号码>=新增数据起始号码 并且 数据库起始号码 <= 新增数据截止号码
	 *		  2) 与数据库的截止号码重复
	 *		     数据库截止号码>=新增数据起始号码 并且 数据库截止号码 <= 新增数据截止号码
	 *		  3) 被数据库起始和截止号码包含
	 *		     数据库起始号码<=新增起始号码     并且 数据库截止号码 >= 新增数据截止号码
	 *		  4) 包含了数据库起始和截止号码
	 *		      数据库起始号码>=新增起始号码     并且 数据库截止号码 <= 新增数据截止号码
	 *       2、编码规则相同
	 *       3、且入库主数据必须是正常的不能是已删除的；
	 *       4、凭证种类和本次的相同；
	 *       5、所属账户本次相同为空的同一类
	 *       6、如果是入库或修改，需要排除本身）；
	 * 如果主数据判断没有数据证明是重复的再次判断明细数据是否有重复的BV_VOU_INFO_DTL表
	 *      （1、本次开始号<= VOU_NO and VOU_NO <=本次结束号；
	 *       2、且入库状态VOU_STATUS不为02已出售；
	 *       3、vou_tid凭证种类和本次的相同；
	 *       4、BANK_ACC所属账户本次相同为空的属于同一类
	 *       5、明细所属的凭证入库信息的编码规则要相同）；
	 */
	public boolean checkVoucherNoUnique(BvVouInput entity,String operate){
		/**判断主数据**/
		StringBuilder sql = new StringBuilder();
		sql.append("select ID from BV_VOU_INPUT where ((begin_no >= :begin_no and begin_no <= :end_no) or")
				.append(" (end_no >= :begin_no and end_no <= :end_no) or (begin_no <= :begin_no and end_no >= :end_no)")
				.append(" or (begin_no >= :begin_no and end_no<= :end_no))")
				.append(" and rule_no = :rule_no and status <> :status and vou_tid = :vou_tid");
		if(entity.getBankAcc() != null && !"".equals(entity.getBankAcc())){
			sql.append(" and bank_acc = :bank_acc");
		}
		if(BlankvouConstant.BILL_OPERATE_STORAGE.equals(operate)||BlankvouConstant.BILL_OPERATE_EDIT.equals(operate)){
			sql.append(" and id <> :id");
		}
		SQLQuery query=createSQLQuery(sql.toString());
		query.setString("begin_no", entity.getBeginNo());
		query.setString("end_no", entity.getEndNo());
		query.setString("rule_no", entity.getRuleNo());
		query.setInteger("status", BlankvouConstant.BVVOUINPUT_STATUS_DELETE);
		query.setString("vou_tid", entity.getVouTid());
		if(entity.getBankAcc() != null && !"".equals(entity.getBankAcc())){
			query.setString("bank_acc", entity.getBankAcc());
		}
		if(BlankvouConstant.BILL_OPERATE_STORAGE.equals(operate)||BlankvouConstant.BILL_OPERATE_EDIT.equals(operate)){
			query.setString("id", entity.getId());
		}
		if(!query.list().isEmpty()){
			return false;
		}
		/**判断明细数据**/
		sql.delete(0, sql.length());//清除StringBuilder对象里的内容
		sql.append("select b.ID from BV_VOU_INFO_DTL b LEFT JOIN BV_VOU_INPUT v ON b.input_id = v.id where (b.VOU_NO >= :begin_no and b.VOU_NO <= :end_no)")
				.append(" and b.VOU_STATUS <> :VOU_STATUS and b.vou_tid = :vou_tid and v.rule_no = :rule_no");
		if(entity.getBankAcc() != null && !"".equals(entity.getBankAcc())){
			sql.append(" and b.bank_acc = :bank_acc");
		}
		query=createSQLQuery(sql.toString());
		query.setString("begin_no", entity.getBeginNo());
		query.setString("end_no", entity.getEndNo());
		query.setString("VOU_STATUS", BlankvouConstant.BVVOUINFODTL_STATUS_SELL);
		query.setString("vou_tid", entity.getVouTid());
		query.setString("rule_no", entity.getRuleNo());
		if(entity.getBankAcc() != null && !"".equals(entity.getBankAcc())){
			query.setString("bank_acc", entity.getBankAcc());
		}
		if(!query.list().isEmpty()){
			return false;
		}
		return true;
	}
	/**
	 * 根据查询条件查询是否存在凭证入库，存在返回false否则返回true                    
	 * @return boolean
	 */
	public boolean checkBvVoucherType(String vouTid){
//		String sql="select id from("
//				+ "select id from BV_VOU_INPUT where status<>-2 and vou_tid = :vou_tid "
//				+ "union all "
//				+ "select id from BV_VOU_INFO_DTL where vou_tid = :vou_tid) a";
//		SQLQuery query=createSQLQuery(sql);
//		query.setString("vou_tid", vouTid);
//		if(!query.list().isEmpty()){
//			return false;
//		}
		String sql="select count(*) from("
				+ "select id from BV_VOU_INPUT where status<>-2 and vou_tid = :vou_tid "
				+ "union all "
				+ "select id from BV_VOU_INFO_DTL where vou_tid = :vou_tid) info ";
		SQLQuery query=createSQLQuery(sql);
		query.setString("vou_tid", vouTid);
		List list=query.list();
		if(list!=null&&list.size()>0){
			int size=Integer.parseInt(list.get(0).toString());
			if(size>0){
				return false;
			}
		}
		return true;
	}
	/**
	 * 根据查询条件查询是否存在凭证入库，存在返回false否则返回true                 
	 * @return boolean
	 */
	public boolean checkBvStorageBox(String sboxId){
		String sql="select count(*) from("
				+ "select id id from BV_VOU_INPUT where status<>-2 and sbox_id = :sbox_id "
				+ "union all "
				+ "select id id from BV_VOU_INFO_DTL where sbox_id = :sbox_id) info ";
		SQLQuery query=createSQLQuery(sql);
		query.setString("sbox_id", sboxId);
		List list=query.list();
		if(list!=null&&list.size()>0){
			int size=Integer.parseInt(list.get(0).toString());
			if(size>0){
				return false;
			}
		}
		return true;
	}
	/**
	 * 根据IDS批量删除数据 逻辑删除设置
	 * @param params
	 * @return
	 */
	public int batchDeleteBvVouInput(Map<String,Object> params,ICurrentUser user){
		String hql="update BvVouInput set status = :status"
				+ ",updateBy = :updateBy "
				+ ",updateName = :updateName "
				+ ",updateDate = :updateDate "
				+ "where id in (:ids)";
		params.put("status", BlankvouConstant.BVVOUINPUT_STATUS_DELETE);
		params.put("updateName", user.getUsername());
		params.put("updateBy", user.getLoginName());
		params.put("updateDate", new Date());
		int num=this.batchExecute(hql, params);
		return num;
	}
	
	 /**
     * 根据单位id获取此单位中是否存在应收应付的票据
     * @param corpId
     * @return 有效用户笔数
     */
    public int getValidBvInputNumByCorpId(String corpId){
        String sql="select count(*) from BV_VOU_INPUT where 1=1  and corp_id=:corpId";
        Map<String,Object> params=new HashMap<>();
        params.put("corpId",corpId);
        return ((Number)this.createSQLQuery(sql,params).uniqueResult()).intValue();
    }
}
