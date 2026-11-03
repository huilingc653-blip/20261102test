package com.bytter.bill.domain.bill.dao;
import cn.hutool.core.util.StrUtil;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.*;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @Title: Dao
 * @Description: AB_VOUCHERINFO
 * @author onlineGenerator
 * @date 2017-06-21 15:30:56
 * @version 1.0
 */
@Repository
public class AbEmitBillDao extends BaseDao<AbVoucherinfo,String>{
	/**
	 * 开票登记列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList, String userName) {
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" a.bill_type as billType, a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd," );
		sql.append(" a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,a.SBBLL_RNG_END_SN as sbbllRngEndSn,a.BL_STD_AMT as blStdAmt, "  );
		sql.append(" a.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,a.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,a.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd, "  );
		sql.append(" a.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,a.OLD_DRAFT_ID as oldDraftId,a.BL_BSN_TPCD as blBsnTpcd," );
		sql.append(" a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,a.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo,");
		sql.append(" a.cooperative_bank_no as cooperativeBankNo,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,");
		sql.append(" a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,");
		sql.append(" a.bill_status as billStatus,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,b.source as source,");
		sql.append(" a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append(" a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append(" a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,b.app_no as appNo,b.batch_no as batchNo,");
		sql.append(" a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as aContractNo,");
		sql.append(" a.a_invcnb as aInvcnb,a.accptncagrmtnb as accptncagrmtnb,c.status as status,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append(" a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append(" c.apply_date as applyDate,b.abs as abs,b.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append(" c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,c.adr as adr, ");
		sql.append(" c.cn_accrecords as cnAccrecords,c.brief_id as briefid,ba.num as attachmentNum,ba.attachment_id as attachmentId,c.send_sign as sendSign,c.create_by as createBy,");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel,");
		sql.append(" b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,b.INSIDE_RAT_MODEL as insideRatModel,b.is_Ensure as isEnsure,b.voucher_id as payId, ");
		sql.append(" b.bus_corp_name as busCorpName,c.exc_id as excId,");
		sql.append(" ric.name as ratItemName,b.IS_OPEN_RAT as isOpenRat,rb.contract_no as contractNo, ");
		sql.append(" nb.bursar_bill_no as bursarBillNo,nb.bill_no as payNo,b.bank_acc as bankAcc,c.batch_dtl_no as batchDtlNo ");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_bill_attachment ba on ba.vvid = b.id ");
		sql.append(" left join rat_uration ru on ru.bill_id = b.id  ");
		sql.append(" left join rat_bkration rb on ru.rat_code = rb.id ");
		sql.append(" left join rat_item_custom ric on ric.id = ru.item_code ");
		sql.append(" left join nis_billhead nb on nb.id = b.voucher_id ");
		sql.append(" WHERE b.valid_sign = :valid_sign and (c.exc_id is null or c.exc_id != '2')");
		sql.append(" and b.type = :type ");
		sql.append(" and (c.create_by = :userName or c.create_by is null)" );//登记人操作限制：开票登记只能由登记人处理
		//业务状态条件查询
		sql.append (" and c.valid_sign = :valid_sign2 ");
		sql.append (" and c.brief_id = :brief_id ");
		String statusName =  input.getFormdata().get("eqs_statusName")==null?null:input.getFormdata().get("eqs_statusName").toString();
		if(statusName!=null && StringUtils.isEmpty(statusName.trim())){
			//默认查询 暂存、审批中、审批打回  的数据
			sql.append(" and c.status in (:status) ");
		}
		Map<String, Object> params = new HashMap<>();
		params.put("valid_sign",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("type",AccebillConstant.AB_VOUCHERINFO_OP_TYPE0);
		params.put("userName",userName);
		params.put("valid_sign2",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("brief_id",AccebillConstant.BRIEF_STATUS_001);
		params.put("status",new Integer[]{AccebillConstant.AB_BILL_BUS_STATUS0,
				AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS_1});
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
		}
		String payImportOrigNo=(String)input.getFormdata().get("payImportOrigNo");
		if(StrUtil.isNotEmpty(payImportOrigNo)){
			String payImportOrigNoSql= SqlUtil.getInSqlByParams(StrUtil.split(payImportOrigNo,","), "a.orig_no","payImportOrigNo", params);
			sql.append(" and ").append(payImportOrigNoSql).append(" ");
		}
//		List<String> bursarBillNoList = new ArrayList<>();
//		if (!EmptyUtils.isEmpty(input.getFormdata().get("likes_bursarBillNo"))){
//			String bursarBillNo = input.getFormdata().get("likes_bursarBillNo").toString();
//			bursarBillNoList = Arrays.asList(bursarBillNo.split(" "));
//			if (bursarBillNoList.size()>=2){
//				sql.append(" and ").append(SqlUtil.getInSql(bursarBillNoList,"nb.bursar_Bill_No",true));
//				input.getFormdata().remove("likes_bursarBillNo");
//			}
//		}
		if(input.getFormdata().containsKey("bursarBillNoList")){
			sql.append(" and ").append(SqlUtil.getInSqlByParams((List<String>) input.getFormdata().get("bursarBillNoList"),"nb.bursar_Bill_No","bursarBillNoList",params));
		}
		return getDynamicBySQL(input, sql.toString(), AbEmitBillVo.class, "", false,params);
	}
	/**
	 * 根据ID查询查询开票登记信息
	 * @param aid
	 * @return
	 */
	public List<AbEmitBillVo> getAbEmitBillVoById(String aid) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("b.use_type useType,a.bill_type as billType,a.cooperative_bank_no as cooperativeBankNo,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,a.cmb_bail_flag as cmbBailFlag,");
		sql.append("a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd," +
				"a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,a.SBBLL_RNG_END_SN as sbbllRngEndSn,a.BL_STD_AMT as blStdAmt,a.BL_RMTR_UNN_ID_NO " +
				"as blRmtrUnnIdNo,a.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,a.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,a.EBILL_BL_CRCL_INDC as " +
				"ebillBlCrclIndc,a.OLD_DRAFT_ID as oldDraftId,a.BL_BSN_TPCD as blBsnTpcd,a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,a.BL_RCVPYMTPS_UNN_ID_NO "+
				"as blRcvpymtpsUnnIdNo,a.risk_contract_number riskContractNumber,a.accptncagrmt_Branch accptncagrmtBranch,a.accptncagrmt_Branch_No accptncagrmtBranchNo," +
				"a.cvm_Limit_No cvmLimitNo,a.CMB_BNKBILNEG_ID cmbBnkbilnegId,a.CMB_CRLCON_ID cmbCrlConId, ");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,b.app_no as appNo,b.batch_no as batchNo,");
		sql.append("a.bill_status as billStatus,b.corp_id as corpId,b.CORP_NAME as corpName,b.CORP_CODE as corpCode,b.source as source,b.rat_model as ratmodel,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,b.type_name as bankTypeName,");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as acontractNo,");
		sql.append("a.a_invcnb as ainvcnb,a.accptncagrmtnb as accptncagrmtnb,c.status as status,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append("c.apply_date as applyDate,b.abs as abs,b.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,c.adr as adr,c.cn_accrecords as cnAccrecords, ");
		sql.append("c.brief_id as briefid,c.create_By as createBy,c.create_name as createname,b.bank_Acc as bankAcc, b.voucher_id as voucherId,c.send_sign as sendSign, ");
		sql.append("c.rmk as dtlRmk,c.id,b.bail_contract as bailContract,b.is_Ensure as isEnsure, b.bail_type as bailType,");
		sql.append("b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel,");
		sql.append("b.IS_OPEN_RAT as isOpenRat,b.BEAR_MODEL as isHangGuarantee,a.BILL_MONEY as transMoney, ");
		sql.append("b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,b.INSIDE_RAT_MODEL as insideRatModel,b.RAT_BANK_CORP_TYPE as ratBankCorpType,b.RAT_BANK_CORP_ID as ratBankCorpId ");
		sql.append(",b.RAT_BANK_TYPE as ratBankType, b.IS_CREDIT_DRAW as isCreditDraw,c.bif_code bifCode,c.feemoney_Scale as feemoneyScale ");
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" WHERE c.id = :aid ");
		Map<String, Object> params=new HashMap<>();
		params.put("aid", aid);
		List<AbEmitBillVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillVo.class);
		return listInfo;
	}
	/**
	 * 根据ID查询查询开票登记信息
	 * @param aid
	 * @return
	 */
	public List<AbEmitBillVo> getAbEmitBillVoByIdForPay(String aid) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.cooperative_bank_no as cooperativeBankNo,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,a.cmb_bail_flag as cmbBailFlag, ");
		sql.append("a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd, ");
		sql.append("a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn,a.SBBLL_RNG_END_SN as sbbllRngEndSn,a.BL_STD_AMT as blStdAmt,a.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo, " );
		sql.append("a.BL_ACPTR_ID_TPCD as blAcptrIdTpcd,a.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,a.EBILL_BL_CRCL_INDC as ebillBlCrclIndc, " );
		sql.append("a.OLD_DRAFT_ID as oldDraftId,a.BL_BSN_TPCD as blBsnTpcd,a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,a.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, ");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,b.app_no as appNo,b.batch_no as batchNo, ");
		sql.append("a.bill_status as billStatus,b.corp_id as corpId,b.CORP_NAME as corpName,b.CORP_CODE as corpCode,b.source as source,b.rat_model as ratmodel, ");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc, " +
				"a.risk_contract_number riskContractNumber,a.accptncagrmt_Branch accptncagrmtBranch,a.accptncagrmt_Branch_No accptncagrmtBranchNo,a.cvm_Limit_No cvmLimitNo," +
				"a.CMB_BNKBILNEG_ID cmbBnkbilnegId,a.CMB_CRLCON_ID cmbCrlConId,a.BILL_MONEY as transMoney, ");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName, ");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,b.TYPE_NAME as bankTypeName, ");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as acontractNo, ");
		sql.append("a.a_invcnb as ainvcnb,a.accptncagrmtnb as accptncagrmtnb,c.status as status,c.opt_status as optStatus,b.inside_sing as insideSing, ");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt, ");
		sql.append("c.apply_date as applyDate,b.abs as abs,b.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney, ");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,c.adr as adr,c.cn_accrecords as cnAccrecords, ");
		sql.append("c.brief_id as briefid,c.create_By as createBy,c.create_name as createname,b.bank_Acc as bankAcc,c.emit_sttlmmk as emitSttlmmk, ");
		sql.append("c.rmk as dtlRmk,c.id,e.corp_id as payCorpId,e.CORP_NAME as payCorpName,e.payee_acc as payPayeeacc,e.payee_name as payPayeeName, ");
		sql.append("e.payee_bank_no as payPayeeBankNo,e.payee_bank_name as payPayeeBankName,e.pay_money as payMoney,e.purpose as payUse,e.remark as payAbs, ");
//		sql.append("r.parent_id as prov,r.prov_input_name as provName,r.id as city,r.name as cityName,b.bail_contract as bailContract ");
		sql.append("e.city as city, b.bail_contract as bailContract, ");
		sql.append("b.IS_OPEN_RAT as isOpenRat,b.BEAR_MODEL as isHangGuarantee,b.is_Ensure as isEnsure, b.bail_type as bailType, ");
		sql.append("b.RAT_BANK_CORP_TYPE as ratBankCorpType,b.RAT_BANK_CORP_ID as ratBankCorpId, ");
		sql.append("b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode, ");
		sql.append("b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append("b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType,c.bif_code bifCode,c.feemoney_Scale as feemoneyScale ");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_emitbill_pay e on e.aid = b.id ");
//		sql.append(" left join SYS_REGION r on r.id = e.city ");
		sql.append(" WHERE c.id = :aid ");
		Map<String, Object> params = new HashMap<>();
//		params.put("valid_sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("aid", aid);
//		List<AbEmitBillVo> listInfo = this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillVo.class);
		return this.listBySQLAliasToBean(sql.toString(), params, AbEmitBillVo.class);
	}
	/**
	 * 审批页面查询当前操作员可审批的开票登记数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn," );
		sql.append("a.SBBLL_RNG_END_SN as sbbllRngEndSn,a.BL_STD_AMT as blStdAmt,a.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,a.BL_ACPTR_ID_TPCD as blAcptrIdTpcd" );
		sql.append(",a.BL_RCVPYMTPS_ID_TPCD as blRcvpymtpsIdTpcd,a.EBILL_BL_CRCL_INDC as ebillBlCrclIndc,a.OLD_DRAFT_ID as oldDraftId,a.BL_BSN_TPCD as blBsnTpcd" );
		sql.append(",a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo,a.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo" );
		sql.append(",a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,a.orig_no as origNo,");
		sql.append("a.bill_money as billMoney,a.start_date as startDate,a.end_date as endDate,a.payee as payee,");
		sql.append("a.bill_status as billStatus,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,b.app_no as appNo,b.batch_no as batchNo, ");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,a.banendrsmtmk as banendrsmtmk,b.box_status as boxStatus,a.payeracc as payeracc,");
		sql.append("a.payer_bank_name as payerBankName,a.payerbankno as payerbankno,a.payeeacc as payeeacc,a.payee_bank_name as payeeBankName,");
		sql.append("a.payeebankno as payeebankno,a.acceptoracc as acceptoracc,a.acceptorbankno as acceptorbankno,c.id as businessKey,");
		sql.append("a.acceptor_bank_name as acceptorBankName,a.acceptorbankaddress as acceptorbankaddress,a.a_contract_no as aContractNo,");
		sql.append("a.a_invcnb as aInvcnb,a.accptncagrmtnb as accptncagrmtnb,c.status as status,c.opt_status as optStatus,b.inside_sing as insideSing,");
		sql.append("a.payer_cdt_ratgs as payerCdtRatgs,a.payer_cdt_ratg_agcy as payerCdtRatgGgcy,a.payer_cdt_ratg_duedt as payerCdtRatgDuedt,");
		sql.append("c.apply_date as applyDate,b.abs as abs,b.rmk as rmk,c.credit_code as creditCode,c.bail_acc as bailAcc,c.bailmoney as bailmoney,");
		sql.append("c.bailpercent as bailpercent,c.bailrate as bailrate,c.feemoney as feemoney,c.business_date as businessDate,c.adr as adr,c.cn_accrecords as cnAccrecords, ");
		sql.append("ba.num as attachmentNum,ba.attachment_id as attachmentId ");
		sql.append(",b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel");
		sql.append(",b.EMIT_GUANDIS_SIGN as emitGuandisSign,b.EMIT_TYPE as emitType, b.BUS_CORP_NAME as busCorpName ");
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_bill_attachment ba on ba.vvid = b.id ");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbEmitBillVo.class, "", false,params);
	}

	/**
	 * 招行合作银行号查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<CmbBnkbilnegVo> dataTableCooperativeBankNumber(DataTablesInput input) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("ID as id,TEXTNO as textno,ORGNAME as orgname,BRNNBR as brnnbr from CMB_BNKBILNEG " );
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		return getDynamicBySQL(input, sql.toString(), CmbBnkbilnegVo.class, "", false,params);
	}

	/**
	 * 添加招行风险合同信息表
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableCMBCRLCONInfo(DataTablesInput input) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("* from CMB_CRLCON " );
		sql.append(" WHERE 1=1 ");
		sql.append(" and CREATE_DATE >= bt_to_date(:createDate,'yyyy-MM-dd HH24:mi:ss') ");
		Map<String,Object> params = new HashMap<>();
		params.put("createDate",DateUtils.formatDate(DateUtils.addDay(DateUtils.getDate(),-210),"yyyy-MM-dd") +" 00:00:00");
		return getDynamicBySQL(input, sql.toString(), CmbCrlconVo.class, "", false,params);
	}

	/**
	 * 招行协议文本信息查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public CmbCrlinfoVo selectCmbCrlinfoByCVMNo(String cvmNO) {
		if(cvmNO == null){
			return null;
		}
		StringBuilder sql=new StringBuilder("select ");
		sql.append("ID as id,creditlineid as creditlineid,contracttextno as contracttextno,creditlinetype as creditlinetype,crlnum as crlnum from CMB_CRLINFO " );
		sql.append(" WHERE 1=1 and creditlineid=:cvmNO");
		Map<String,Object> params = new HashMap<>();
		params.put("cvmNO",cvmNO);
		List<CmbCrlinfoVo> cmbCrlinfoVos = this.listBySQLAliasToBean(sql.toString(), params, CmbCrlinfoVo.class);
		return CollectionUtils.isEmpty(cmbCrlinfoVos) ?null:cmbCrlinfoVos.get(0);
	}

	/**
	 * 根据单位id获取此单位中是否存在应付未结束或应收持有的票据
	 * @param corpId
	 * @return 有效用户笔数
	 */
	public int getValidBillDisbleNumByCorpId(String corpId, String type){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder()
				.append(" select count(*) from")
				.append(" ab_voucherinfo_vice vv")
				.append(" left join ab_accrecords c on vv.id=c.vvid")
				.append(" where 1=1  and vv.corp_id=:corpId and vv.TYPE=:type and c.VALID_SIGN in (:validSign) ");
		if(AccebillConstant.AB_VOUCHERINFO_OP_TYPE0.equals(type)){
			// 没做退票和兑付的票据不能禁用。
			sql.append(" and (c.BRIEF_ID not in  (:briefId) or (c.status<:status and c.status>:status2) ) ");
			params.put("briefId",new String[]{AccebillConstant.BRIEF_STATUS_004,AccebillConstant.BRIEF_STATUS_6120});
			params.put("status2", AccebillConstant.AB_BILL_BUS_STATUS_2);
		}else {
			// 应收票据持有票据查询
			sql.append(" and c.BRIEF_ID in (:briefId) and c.status=:status and c.opt_Status=:optStatus  ");
			params.put("briefId", new String[]{
					AccebillConstant.BRIEF_STATUS_9003,
					AccebillConstant.BRIEF_STATUS_9010,
					AccebillConstant.BRIEF_STATUS_9012,
					AccebillConstant.BRIEF_STATUS_9018,
					AccebillConstant.BRIEF_STATUS_9019,
					AccebillConstant.BRIEF_STATUS_6001,
					AccebillConstant.BRIEF_STATUS_6212,
					AccebillConstant.BRIEF_STATUS_6019});
			params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		}
		params.put("corpId",corpId);
		params.put("type",type);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("validSign",new String[]{AccebillConstant.AB_BILL_VALID_SIGN_0, AccebillConstant.AB_BILL_VALID_SIGN_1});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}
	/**
	 * 根据单位id获取此单位中是否存在暂存、审批中的应收票据
	 * @param corpId
	 * @return 有效用户笔数
	 */
	public int getApproveBillDisbleNumByCorpId(String corpId, String type){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder()
				.append(" select count(*) from")
				.append(" ab_voucherinfo_vice vv")
				.append(" left join ab_accrecords c on vv.id=c.vvid")
				.append(" where 1=1  and vv.corp_id=:corpId and vv.type=:type and c.VALID_SIGN in (:validSign) ")
				.append(" and status>:status1 and status<:status2 ");
		params.put("corpId",corpId);
		params.put("type",type);
		params.put("status1", AccebillConstant.AB_BILL_BUS_STATUS_2);
		params.put("status2", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("validSign",new String[]{AccebillConstant.AB_BILL_VALID_SIGN_0, AccebillConstant.AB_BILL_VALID_SIGN_1});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}
	/**
	 * 根据单位id获取此单位中是否存在指令中的票据
	 * @param corpId
	 * @return 有效用户笔数
	 */
	public int getElectBillDisbleNumByCorpId(String corpId){
		StringBuilder sql = new StringBuilder()
				.append(" select count(*) from")
				.append(" ab_voucherinfo_vice vv")
				.append(" left join ab_accrecords c on vv.id=c.vvid")
				.append(" where 1=1  and vv.corp_id=:corpId ")
				.append(" and c.status=:status and c.OPT_STATUS in (:optStatus) ");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("corpId",corpId);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS95);
		// 指令状态：待发送、发送中、已发送待回应、已回应待签收
		params.put("optStatus",new String[]{
				AccebillConstant.AB_BILL_OPT_STATUS18,
				AccebillConstant.AB_BILL_OPT_STATUS16,
				AccebillConstant.AB_BILL_OPT_STATUS15,
				AccebillConstant.AB_BILL_OPT_STATUS17,
		});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}
	/**
	 * 根据单位id获取此单位中是否存在贴现在途数据
	 *
	 * @param corpId
	 * @return 有效用户笔数
	 */
	public int getDiscuntNumByCorpId(String corpId){
		StringBuilder sql = new StringBuilder()
				.append(" select count(*) from")
				.append(" ab_voucherinfo_vice vv")
				.append(" left join ab_accrecords c on vv.id=c.vvid")
				.append(" where 1=1  and vv.corp_id=:corpId and c.VALID_SIGN in (:validSign) ")
				.append(" and c.brief_id=:briefId and c.opt_status=:optStatus and c.status=:status");
		Map<String,Object> params=new HashMap<String,Object>();
		params.put("corpId",corpId);
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("briefId", AccebillConstant.BRIEF_STATUS_6011);
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("validSign",new String[]{AccebillConstant.AB_BILL_VALID_SIGN_0, AccebillConstant.AB_BILL_VALID_SIGN_1});
		return ((Number)this.createSQLQuery(sql.toString(),params).uniqueResult()).intValue();
	}

	/**
	 * 招行保证金账号查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getZhBondAccDataTables(DataTablesInput input) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("c.ID as id,c.curacc as curacc,c.ACCNBR as accnbr,c.ACCNAM as accnam,c.HMGTYP as hmgtyp,c.ACCBRN as accbrn,c.CCYNBR as ccynbr,c.OPNDAT as opndat,c.VALDAT as valdat,c.MUTDAT as mutdat,c.AVLBAL as avlbal,fb.id as bondId,fb.INTEREST_RATE as bondRate from CMB_BAIL_ACCOUNT_INFO c " );
		sql.append(" left join fc_bond fb on fb.bank_acc = c.accnbr and fb.status=95 ");
		// 批量设置票据信息时，招行保证金列表界面不显示已到期（到期日<=当天）保证金账号
		String nowStr = DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
		sql.append(" WHERE 1=1 and BT_TO_CHAR(c.MUTDAT, 'yyyy-MM-dd') > :nowStr ");
		Map<String,Object> params = new HashMap<>();
		params.put("nowStr", nowStr);
		return getDynamicBySQL(input, sql.toString(), CmbBailAccountInfoVo.class, "", false,params);
	}

	/**
	* @Description 根据账号查询招行保证金信息
	* @author zhous
	* @date 2024/9/9 10:27
	*/
	public CmbBailAccountInfoVo getZhBondByAcc(String acc) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("c.ID as id,c.ACCNBR as accnbr,c.ACCNAM as accnam,c.HMGTYP as hmgtyp,c.ACCBRN as accbrn,c.CCYNBR as ccynbr,c.OPNDAT as opndat,c.VALDAT as vatdat,c.MUTDAT as mutdat,c.AVLBAL as avlbal from CMB_BAIL_ACCOUNT_INFO c " );
		sql.append(" WHERE c.accnbr=:acc ");
		Map<String,Object> params = new HashMap<>();
		params.put("acc",acc);
		List<CmbBailAccountInfoVo> vos = this.listBySQLAliasToBean(sql.toString(), params, CmbBailAccountInfoVo.class);
		return CollectionUtils.isEmpty(vos) ?null:vos.get(0);
	}

	/**
	 * 民生银行保证金账号查询
	 * @param input
	 * @return
	 */
	public DataTablesOutput<CmbcBailAccountInfoVo> getMsBondAccDataTables(DataTablesInput input) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("c.ID as id,c.acct_no as acctNo,c.acct_name as acctName,c.cur as cur,c.open_date as openDate,c.expire_date as expireDate,c.dept_name as deptName,c.rate as rate,c.tim_type as timType,c.bal as bal,c.avail_bal as availBal,fb.id as bondId from CMBC_BAIL_ACCOUNT_INFO c " );
		sql.append(" left join fc_bond fb on fb.bank_acc = c.acct_no and fb.status=95 ");
		// 批量设置票据信息时，招行保证金列表界面不显示已到期（到期日<=当天）保证金账号
		String nowStr = DateUtils.format(DateUtils.getDate(), DateUtils.PATTERN_CLASSICAL_SIMPLE);
		sql.append(" WHERE 1=1 and (BT_TO_CHAR(c.expire_date, 'yyyy-MM-dd') > :nowStr or c.expire_date is null) ");
		Map<String,Object> params = new HashMap<>();
		params.put("nowStr", nowStr);
		String timType = (String) input.getFormdata().get("eqs_timType");
		if(StringUtils.isNotEmpty(timType)){
			sql.append(" and c.tim_type = :timType ");
			params.put("timType", AccebillConstant.TIMTYPE_HQ_0.equals(timType)?"活期":"定期");
			input.getFormdata().remove("eqs_timType");
		}
		return getDynamicBySQL(input, sql.toString(), CmbcBailAccountInfoVo.class, "", false,params);
	}


	public CmbcBailAccountInfoVo getMsBondByAcc(String acc) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("c.ID as id,c.acct_no as acctNo,c.acct_name as acctName,c.cur as cur,c.open_date as openDate,c.expire_date as expireDate,c.dept_name as deptName,c.rate as rate,c.tim_type as timType,c.bal as bal,c.avail_bal as availBal from CMBC_BAIL_ACCOUNT_INFO c " );
		sql.append(" WHERE c.acct_no=:acc ");
		Map<String,Object> params = new HashMap<>();
		params.put("acc",acc);
		List<CmbcBailAccountInfoVo> vos = this.listBySQLAliasToBean(sql.toString(), params, CmbcBailAccountInfoVo.class);
		return CollectionUtils.isEmpty(vos) ?null:vos.get(0);
	}
}
