package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownApplyVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: AB_VOUCHERINFO
* @author onlineGenerator
* @date 2017-06-21 15:30:56
* @version 1.0
*
*/
@Repository
public class AbBillTransferDownApplyDao extends BaseDao<AbBillTransfer,String>{
	
	/**
	 * 票据下拨申请列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillTransferDownApplyVo> getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("t.allocated_code as allocatedCode,t.record_corp_id as recordCorpId,t.RECORD_CORP_CODE as recordCorpCode,t.record_corp_name as recordCorpName,");
		sql.append("t.recevie_bankacc as recevieBankacc,t.allocation_money as allocationMoney,");
		sql.append("t.record_date as recordDate,t.bill_currency as billCurrency,t.enclosure as enclosure,");
		sql.append("t.id as trid,c.id as aid,t.recevie_corp_id as recevieCorpId,");
		sql.append("t.allocation_reason as allocationReason,c.status as status, vv.BUS_CORP_NAME as busCorpName");
		sql.append(" from ab_accrecords c ");
		sql.append(" inner join ab_bill_transfer t on t.id = c.trid ");
		sql.append(" left join ab_voucherinfo_vice vv on vv.id=c.vvid");
		sql.append(" where c.brief_id = :briefId6203 ");
		//打回状态查询
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.status = :status1 ");
			input.getFormdata().remove("eqs_statusName");
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))){
			//审批状态查询
			sql.append(" and c.status = :status11 ");
			input.getFormdata().remove("eqs_statusName");
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS0).equals(input.getFormdata().get("eqs_statusName"))){
			//暂存状态查询
			sql.append(" and c.status = :status0 ");
			input.getFormdata().remove("eqs_statusName");
		}else{
			sql.append(" and c.status in (:status0,:status11,:status1) ");//查询暂存，审批中，打回的数据
		}
		Map<String,Object> params = new HashMap<>();
		params.put("briefId6203",AccebillConstant.BRIEF_STATUS_6203);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS_1);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("status0",AccebillConstant.AB_BILL_BUS_STATUS0);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "t.record_corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (vv.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(input, sql.toString(), AbBillTransferDownApplyVo.class, "", false,params);
	}
	
	/**
	 * 根据ID查询下拨申请单
	 * @param trid
	 * @return
	 */
	public List<AbBillTransferDownApplyVo> getDataById(String trid, String aid) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("t.allocated_code as allocatedCode,t.record_corp_id as recordCorpId,t.record_corp_code as recordCorpCode,t.record_corp_name as recordCorpName,");
		sql.append("t.recevie_bankacc as recevieBankacc,t.allocation_money as allocationMoney,");
		sql.append("t.record_date as recordDate,t.bill_currency as billCurrency,t.enclosure as enclosure,");
		sql.append("t.id as trid,c.id as aid,t.recevie_corp_id as recevieCorpId,");
		sql.append("t.allocation_reason as allocationReason,c.status as status ");
		sql.append(" from ab_accrecords c inner join ab_bill_transfer t on t.id = c.trid ");
		sql.append(" and c.brief_id = :briefId6203 ");
		sql.append(" WHERE t.id = :trid and c.id = :aid ");
		Map<String, Object> params=new HashMap<>();
		params.put("trid", trid);
		params.put("briefId6203",AccebillConstant.BRIEF_STATUS_6203);
		params.put("aid", aid);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillTransferDownApplyVo.class);
	}
	/**
	 * 批量删除下拨申请单(暂存和打回状态的下拨申请单物理删除)
	 * @param params
	 * @return
	 */
	public int batchDelete(Map<String, Object> params) {
		StringBuilder hql=new StringBuilder("DELETE AbBillTransfer where id in (:tridsArray) ");
		int num1 = this.batchExecute(hql.toString(), params);
		hql=new StringBuilder("DELETE AbBillTransfer where id in (:aidsArray) ");
		int num2 = this.batchExecute(hql.toString(), params);
		num1 = num1==num2?num1:0;
		return num1;
	}
	
	/**
	 * 审批页面查询当前操作员可审批的票据下拨申请单数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("t.allocated_code as allocatedCode,t.record_corp_id as recordCorpId,t.record_corp_code as recordCorpCode,t.record_corp_name as recordCorpName,");
		sql.append("t.recevie_bankacc as recevieBankacc,t.allocation_money as allocationMoney,");
		sql.append("t.record_date as recordDate,t.bill_currency as billCurrency,t.enclosure as enclosure,");
		sql.append("t.id as trid,c.id as aid,t.recevie_corp_id as recevieCorpId,");
		sql.append("t.allocation_reason as allocationReason,c.status as status,c.id as businessKey ");
		sql.append(" from ab_accrecords c inner join ab_bill_transfer t on t.id = c.trid ");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbBillTransferDownApplyVo.class, "", false,params);
	}
	
	
	
}
