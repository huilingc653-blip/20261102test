package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.entity.AbBillTransfer;
import com.bytter.bill.domain.bill.vo.AbBillTransferDownManageVo;
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
* @Description: 票据下拨
* @author onlineGenerator
* @date 2017-07-19 09:43:56
* @version 1.0
*
*/
@Repository
public class AbBillTransferDownManageDao extends BaseDao<AbBillTransfer,String>{
	
	/**
	 * 票据票据下拨列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillTransferDownManageVo> getDataTablesUnion(DataTablesInput input, List<String> corpCenterIds) {
		Map<String,Object> params = new HashMap<> ();
		String corpAuthSql = SqlUtil.getInSqlByParams(corpCenterIds, "b.record_corp_id", params);
		String corpAuthSql2 = SqlUtil.getInSqlByParams(corpCenterIds, "b1.recevie_corp_id", params);
		StringBuilder sql=new StringBuilder("select ");
		//提交的申请单查询
		sql.append (" t.trid as trid, t.aid as aid, t.businessDate as businessDate, t.allocatedCode as allocatedCode,");
		sql.append (" t.applyCode as applyCode, t.recordCorpId as recordCorpId, t.recordCorpCode as recordCorpCode, t.recordCorpName as recordCorpName,");
		sql.append (" t.recevieCorpId as recevieCorpId, t.recevieCorp as recevieCorpCode, t.recevieCorpName as recevieCorpName, t.recevieBankacc as recevieBankacc,");
		sql.append (" t.allocationMoney as allocationMoney, t.applyMoney as applyMoney, t.allocationReason as allocationReason,");
		sql.append (" t.applyReason as applyReason, t.status as status, t.recordDate as recordDate, t.billCurrency as billCurrency,");
		sql.append (" t.enclosure as enclosure, t.briefId as briefId, t.recordBankacc as recordBankacc, t.lastid as lastid, t.busCorpName as busCorpName");
		sql.append("  from (select b.id as trid,a.id as aid,a.business_date as businessDate,null as allocatedCode,");
		sql.append(" b.allocated_code as applyCode,null as recordCorpId,null as recordCorpCode,null as recordCorpName,b.record_corp_id as recevieCorpId,");
		sql.append(" b.record_corp_name as recevieCorpName,b.RECEVIE_CORP_CODE as recevieCorp,b.recevie_bankacc as recevieBankacc,null as allocationMoney,");
		sql.append(" b.allocation_money as applyMoney,null as allocationReason,b.allocation_reason as applyReason,"+AccebillConstant.AB_BILL_BUS_STATUS0+" as status,");
		sql.append(" null as recordDate,b.bill_currency as billCurrency,b.enclosure as enclosure,a.brief_id as briefId, ");
		sql.append(" b.record_bankacc as recordBankacc,a.lastid as lastid, vv.bus_corp_name as busCorpName");
		sql.append(" from ab_accrecords a left join AB_BILL_TRANSFER b on a.trid = b.id  ");
		sql.append(" left join ab_voucherinfo_vice vv on vv.id=a.vvid ");
		sql.append(" where a.valid_sign = :validSign1 and a.status = :status95 and a.brief_id = :briefId6203 ");
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (vv.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv.bank_acc", "bankAccs", params)).append(")");
		}
		//下拨单查询(下拨单没有对应的申请单)
		sql.append("union ");
		sql.append("select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode, ");
		sql.append(" b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.RECORD_CORP_CODE as recordCorpCode,b1.record_corp_name as recordCorpName,");
		sql.append(" b1.recevie_corp_id as recevieCorpId,b1.recevie_corp_name as recevieCorpName,b1.RECEVIE_CORP_CODE as recevieCorp,b1.recevie_bankacc as recevieBankacc,");
		sql.append(" b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append(" b2.allocation_reason as applyReason,a1.status as status,");
		sql.append(" b1.record_date as recordDate,");
		sql.append(" b2.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append(" null as lastid, vv1.bus_corp_name as busCorpName");
		sql.append(" from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" left join ab_voucherinfo_vice vv1 on vv1.id=a1.vvid");
		sql.append(" where  a1.status in (:statuss)");
		sql.append(" and a1.brief_id = :briefId6204 and a1.lastid is null ");
		sql.append(" and ").append(corpAuthSql2).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (vv1.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv1.bank_acc", "bankAccs", params)).append(")");
		}
		//下拨单查询(基于申请单的下拨单)
		sql.append("union ");
		sql.append("select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode, ");
		sql.append(" b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.RECORD_CORP_CODE as recordCorpCode,b1.record_corp_name as recordCorpName,");
		sql.append(" b1.recevie_corp_id as recevieCorpId,b1.recevie_corp_name as recevieCorpName,b1.RECEVIE_CORP_CODE as recevieCorp,b1.recevie_bankacc as recevieBankacc,");
		sql.append(" b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append(" b2.allocation_reason as applyReason,a1.status as status,");
		sql.append(" b1.record_date as recordDate,");
		sql.append(" b2.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append(" a1.lastid as lastid , vv2.bus_corp_name as busCorpName");
		sql.append(" from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" left join ab_voucherinfo_vice vv2 on vv2.id=a1.vvid");
		sql.append(" where  a1.status in (:statuss)" );
		sql.append(" and a1.brief_id = :briefId6204 and a1.lastid is not null ");
		sql.append(" and ").append(corpAuthSql2);
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (vv2.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "vv2.bank_acc", "bankAccs", params)).append(")");
		}
		sql.append(" ) t  where 1=1  ");
		//打回状态查询
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and t.status = :status1 ");
			input.getFormdata().remove("eqs_statusName");
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))){
			//审批状态查询
			sql.append(" and t.status = :status11 ");
			input.getFormdata().remove("eqs_statusName");
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS0).equals(input.getFormdata().get("eqs_statusName"))){
			//暂存状态查询
			sql.append(" and t.status = :status0 ");
			input.getFormdata().remove("eqs_statusName");
		}else{
			//默认查询
			sql.append(" and t.status in (:statuss) ");
		}
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("briefId6203",AccebillConstant.BRIEF_STATUS_6203);
		params.put("statuss",new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS0,
				AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS_1
		});
		params.put("briefId6204",AccebillConstant.BRIEF_STATUS_6204);
		params.put("status0",AccebillConstant.AB_BILL_BUS_STATUS0);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS_1);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		return getDynamicBySQL(input, sql.toString(), AbBillTransferDownManageVo.class, "", false,params);
	}
	
	/**
	 * 根据ID查询票据下拨单
	 * @param trid
	 * @return
	 */
	public List<AbBillTransferDownManageVo> getDataById(String trid, String aid) {
		//提交的申请单查询
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b1.id as trid,a.id as aid,a.business_date as businessDate,null as allocatedCode,");
		sql.append("b1.allocated_code as applyCode,null as recordCorpId,null as recordCorpName,b1.record_corp_id as recevieCorpId,");
		sql.append("b1.record_corp_name as recevieCorpName,b1.recevie_bankacc as recevieBankacc,null as allocationMoney,");
		sql.append("b1.allocation_money as applyMoney,null as allocationReason,b1.allocation_reason as applyReason,"+AccebillConstant.AB_BILL_BUS_STATUS0+" as status,");
		sql.append("null as recordDate,b1.bill_currency as billCurrency,b1.enclosure as enclosure,a.brief_id as briefId, ");
		sql.append("b1.record_bankacc as recordBankacc,a.lastid as lastid ");
		sql.append("from ab_accrecords a left join AB_BILL_TRANSFER b1 on a.trid = b1.id  ");
		sql.append("where a.valid_sign = :validSign1 and a.status = :status95 and a.brief_id = :briefId6203 ");
		sql.append(" and a.id = :aid and b1.id = :trid ");
		//下拨单查询(下拨单没有对应的申请单)
		sql.append("union ");
		sql.append("select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode,");
		sql.append("b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.record_corp_name as recordCorpName,");
		sql.append("b1.recevie_corp_id as recevieCorpId,b1.recevie_corp_name as recevieCorpName,b1.recevie_bankacc as recevieBankacc,");
		sql.append("b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append("b2.allocation_reason as applyReason,a1.status as status,");
		//sql.append("(select wm_concat(a.orig_no) from ab_bill_transfer_dtl dt left join ab_accrecords c on dt.bill_aid = c.id  ");
		//sql.append(" left join ab_voucherinfo a on c.vid = a.id where dt.transfer_id = b1.id) as billNo, ");
		sql.append("b1.record_date as recordDate,");
		sql.append("b1.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append("null as lastid ");
		sql.append("from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" where  a1.status in (:statuss)");
		sql.append(" and a1.brief_id = :briefId6204 and a1.lastid is null ");
		sql.append(" and a1.id = :aid and b1.id = :trid ");
		//下拨单查询(基于申请单的下拨单)
		sql.append("union ");
		sql.append("select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode, ");
		sql.append("b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.record_corp_name as recordCorpName,");
		sql.append("b1.recevie_corp_id as recevieCorpId,b1.recevie_corp_name as recevieCorpName,b1.recevie_bankacc as recevieBankacc,");
		sql.append("b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append("b2.allocation_reason as applyReason,a1.status as status,");
		//sql.append("(select wm_concat(a.orig_no) from ab_bill_transfer_dtl dt left join ab_accrecords c on dt.bill_aid = c.id  ");
		//sql.append(" left join ab_voucherinfo a on c.vid = a.id where dt.transfer_id = b1.id) as billNo, ");
		sql.append("b1.record_date as recordDate,");
		sql.append("b2.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append("a1.lastid as lastid ");
		sql.append("from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" where  a1.status in (:statuss)" );
		sql.append(" and a1.brief_id = :briefId6204 and a1.lastid is not null ");
		sql.append(" and a1.id = :aid and b1.id = :trid ");
		Map<String, Object> params=new HashMap<>();
		params.put("validSign1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status95", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("briefId6203", AccebillConstant.BRIEF_STATUS_6203);
		params.put("statuss", new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS0,
				AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS_1
		});
		params.put("briefId6204", AccebillConstant.BRIEF_STATUS_6204);
		params.put("trid", trid);
		params.put("aid", aid);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillTransferDownManageVo.class);
	}
	/**
	 * 审批页面查询当前操作员可审批的票据下拨单数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append (" t.trid as trid,t.aid as aid,t.businessDate as businessDate,t.allocatedCode as allocatedCode,");
		sql.append (" t.applyCode as applyCode,t.recordCorpId as recordCorpId,t.recordCorpCode as recordCorpCode,t.recordCorpName as recordCorpName,");
		sql.append (" t.recevieCorpId as recevieCorpId,t.recevieCorpCode as recevieCorpCode,t.recevieCorpName as recevieCorpName,t.recevieBankacc as recevieBankacc,");
		sql.append (" t.allocationMoney as allocationMoney,t.applyMoney as applyMoney,t.allocationReason as allocationReason,");
		sql.append (" t.applyReason as applyReason,t.status as status,t.recordDate as recordDate,t.billCurrency as billCurrency,");
		sql.append (" t.enclosure as enclosure,t.briefId as briefId,t.recordBankacc as recordBankacc,t.lastid as lastid,t.businesskey as businesskey ");
		//下拨单查询(下拨单没有对应的申请单)
		sql.append(" from (select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode, ");
		sql.append("b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.RECORD_CORP_CODE as recordCorpCode,b1.record_corp_name as recordCorpName,");
		sql.append("b1.recevie_corp_id as recevieCorpId,b1.RECEVIE_CORP_CODE as recevieCorpCode,b1.recevie_corp_name as recevieCorpName,b1.recevie_bankacc as recevieBankacc,");
		sql.append("b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append("b2.allocation_reason as applyReason,a1.status as status,");
		//sql.append("(select wm_concat(a.orig_no) from ab_bill_transfer_dtl dt left join ab_accrecords c on dt.bill_aid = c.id  ");
		//sql.append(" left join ab_voucherinfo a on c.vid = a.id where dt.transfer_id = b1.id) as billNo, ");
		sql.append("b1.record_date as recordDate,");
		sql.append("b2.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append("null as lastid,a1.id as businessKey ");
		sql.append("from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" where a1.lastid is null ");
		//下拨单查询(基于申请单的下拨单)
		sql.append("union ");
		sql.append("select b1.id as trid,a1.id as aid,a1.business_date as businessDate,b1.allocated_code as allocatedCode, ");
		sql.append("b2.allocated_code as applyCode,b1.record_corp_id as recordCorpId,b1.RECORD_CORP_CODE as recordCorpCode,b1.record_corp_name as recordCorpName,");
		sql.append("b1.recevie_corp_id as recevieCorpId,b1.RECEVIE_CORP_CODE as recevieCorpCode,b1.recevie_corp_name as recevieCorpName,b1.recevie_bankacc as recevieBankacc,");
		sql.append("b1.allocation_money as allocationMoney,b2.allocation_money as applyMoney,b1.allocation_reason as allocationReason,");
		sql.append("b2.allocation_reason as applyReason,a1.status as status,");
		//sql.append("(select wm_concat(a.orig_no) from ab_bill_transfer_dtl dt left join ab_accrecords c on dt.bill_aid = c.id  ");
		//sql.append(" left join ab_voucherinfo a on c.vid = a.id where dt.transfer_id = b1.id) as billNo, ");
		sql.append("b1.record_date as recordDate,");
		sql.append("b2.bill_currency as billCurrency,b1.enclosure as enclosure,a1.brief_id as briefId,b1.record_bankacc as recordBankacc,");
		sql.append("a1.lastid as lastid,a1.id as businessKey ");
		sql.append("from ab_accrecords a1 left join AB_BILL_TRANSFER b1 on a1.trid = b1.id ");
		sql.append(" left join ab_accrecords a2 on a1.lastid = a2.id ");
		sql.append(" left join AB_BILL_TRANSFER b2 on a2.trid = b2.id ");
		sql.append(" where a1.lastid is not null ");
		sql.append(" ) t ");
//		sql.append(" left join sys_corp sc1 on t.recordCorpId=sc1.id  ");
//		sql.append(" left join sys_corp sc2 on t.recevieCorpId=sc2.id where 1=1  ");
		sql.append (" where t.recevieCorpId is not null ");
		//sql.append(" and ").append(businessSql).append(" ");
		Map<String,Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "t.aid", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbBillTransferDownManageVo.class, "", false,params);
	}
	
	
	
}
