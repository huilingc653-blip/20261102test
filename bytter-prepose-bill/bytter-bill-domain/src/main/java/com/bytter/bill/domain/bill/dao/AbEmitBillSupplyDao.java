package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbEmitBillSupplyVo;
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
public class AbEmitBillSupplyDao extends BaseDao<AbEmitBillSupplyVo,String>{
	/**
	 * 开票补录列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,");
		sql.append("a.bill_status as billStatus,b.CORP_ID as corpId,s.code as corpCode,s.name as corpName,b.app_no as appNo, ");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as aContractNo,");
		sql.append("a.a_invcnb as aInvcnb,a.accptncagrmtnb as accptncagrmtnb,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append("c.apply_date as applyDate,c.abs as abs,c.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,");
		sql.append("c.adr as adr,c.cn_accrecords as cnAccrecords,c.brief_id as briefId, b.BUS_CORP_NAME as busCorpName,");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6101+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.STATUS end) as status, ");
		sql.append("ba.num as attachmentNum,ba.attachment_id as attachmentId,c.send_sign as sendSign,");
		sql.append("b.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel,");
		sql.append("b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,b.INSIDE_RAT_MODEL as insideRatModel ");

		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join sys_corp s on b.corp_id = s.id ");
		sql.append(" left join sys_corp s2 on b.apply_corp_id = s2.id ");
		sql.append(" left join ab_bill_attachment ba on ba.vvid = b.id ");
		sql.append(" WHERE b.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");

		//纸票
		sql.append(" and ((c.brief_id = '"+AccebillConstant.BRIEF_STATUS_001+"' and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS95+"'");
		sql.append(" and b.box_status = '"+AccebillConstant.AB_BOX_STATUS_Y+"' and SEND_SIGN = '"+AccebillConstant.AB_BILL_SEND_SIGN_N+"' and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");

		sql.append(" ) ");
		//电票
		sql.append(" or (c.brief_id = '"+AccebillConstant.BRIEF_STATUS_003+"'  and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ");
		sql.append("  and c.opt_status='"+AccebillConstant.AB_BILL_OPT_STATUS19+"' and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");
//		sql.append("  and a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' and a.bill_status = '"+AccebillConstant.AB_BILL_STATUS_030006+"' ) ");
		sql.append("  and a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' ) ");
		//----------------------补录审批中的数据----------------------
		sql.append(" or (c.brief_id ='"+AccebillConstant.BRIEF_STATUS_6101+"' and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS11+"'");
		sql.append("  and b.box_status = '"+AccebillConstant.AB_BOX_STATUS_N+"' and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_0+"' ) ) ");
		//条件查询打回数据
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and b.back_brief_id = '"+AccebillConstant.BRIEF_STATUS_6101+"'");
			input.getFormdata().remove("eqs_statusName");
		}
		Map<String,Object> params = new HashMap<>();
		params.put("null",' ');
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
				&&"false".equals(String.valueOf(input.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)))){
			sql.append(" and 1<>1 ");
			input.getFormdata().remove(AccebillConstant.AB_CONFIG_ISEMITMAKE);
		}
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(input, sql.toString(), AbEmitBillSupplyVo.class, "", false,params);
	}
	/**
	 * 审批页面查询当前操作员可审批的开票补录数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbEmitBillSupplyVo> dataTableApprove(DataTablesInput input,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,");
		sql.append("a.bill_status as billStatus,b.corp_id as corpId,b.corp_code as corpCode,b.corp_name as corpName, b.app_no as appNo,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,c.id as businessKey,");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as aContractNo,");
		sql.append("a.a_invcnb as aInvcnb,a.accptncagrmtnb as accptncagrmtnb,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append("c.apply_date as applyDate,c.abs as abs,c.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,");
		sql.append("c.adr as adr,c.cn_accrecords as cnAccrecords,c.brief_id as briefId, ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6101+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.STATUS end) as status, ");
		sql.append("ba.num as attachmentNum,ba.attachment_id as attachmentId,");
		sql.append("b.APPLY_CORP_ID as applyCorpId,b.apply_corp_name as applyCorpName,b.apply_corp_code as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel,");
		sql.append("b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,b.INSIDE_RAT_MODEL as insideRatModel ");
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_bill_attachment ba on ba.vvid = b.id ");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbEmitBillSupplyVo.class, "", false,params);
	}
	/**
	 * 导入时通过开票登记单号获取待补录的票据信息
	 * @param appNo
	 * @return
	 */
	public AbEmitBillSupplyVo getAbVoucherinfoViceByAppNo(String appNo) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,");
		sql.append("a.bill_status as billStatus,b.corp_id as corpId,b.corp_code as corpCode,b.corp_name as corpName,b.app_no as appNo, ");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as aContractNo,");
		sql.append("a.a_invcnb as aInvcnb,a.accptncagrmtnb as accptncagrmtnb,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append("c.apply_date as applyDate,c.abs as abs,c.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,");
		sql.append("c.adr as adr,c.cn_accrecords as cnAccrecords, ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6101+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.STATUS end) as status,");
		sql.append("b.APPLY_CORP_ID as applyCorpId,b.apply_corp_name as applyCorpName,b.apply_corp_code as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel,");
		sql.append("b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,b.INSIDE_RAT_MODEL as insideRatModel ");
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" WHERE b.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");
		//纸票
		sql.append(" and ((c.brief_id = '"+AccebillConstant.BRIEF_STATUS_001+"' and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS95+"'");
		sql.append(" and b.box_status = '"+AccebillConstant.AB_BOX_STATUS_Y+"' and SEND_SIGN = '"+AccebillConstant.AB_BILL_SEND_SIGN_N+"' and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");
		sql.append(" and ((a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_PAPER+"' and a.bill_status is null ) or ");
		sql.append(" (a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' and a.bill_status = '"+AccebillConstant.AB_BILL_STATUS_030006+"') )) ");
		//电票
		sql.append(" or (c.brief_id = '"+AccebillConstant.BRIEF_STATUS_003+"'  and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ");
		sql.append("  and c.opt_status='"+AccebillConstant.AB_BILL_OPT_STATUS19+"' and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");
//		sql.append("  and a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' and a.bill_status in ('"+AccebillConstant.AB_BILL_STATUS_030006+"' ))) ");
		sql.append("  and a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' and a.bill_status in ( '"+AccebillConstant.AB_BILL_STATUS_030006+"'," +" '"+AccebillConstant.NEWBILL_STATUS_CS03+"'))) ");
		sql.append (" and b.app_no = :appNo ");
		Map<String, Object> params=new HashMap<> ();
		params.put("appNo", appNo);
		List<AbEmitBillSupplyVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillSupplyVo.class);
		if(listInfo!=null && !listInfo.isEmpty ()){
			return listInfo.get (0);
		}
		return null;
	}
}
