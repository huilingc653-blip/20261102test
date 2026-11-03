package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountRansomVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import com.bytter.system.api.constant.CorpConstant;
import org.springframework.stereotype.Repository;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据贴现赎回申请
* @author luop
* @date 2017-09-07
* @version 1.0
*
*/
@Repository
public class AbBillDiscountRansomDao extends BaseDao<AbBillDiscountRansomVo,String>{
	/**
	 * 列表显示应收票据贴现赎回申请信息
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillDiscountRansom(DataTablesInput input,List<String> corpAuthList){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,");
		sql.append(" b.corp_id as corpid,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,a.bill_flag as billflag,a.payer as payer,a.payee as payee,a.acceptor as acceptor,");
		sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,a.payeracc,a.PAYER_BANK_NAME as payerBankName,");
		sql.append(" a.payeeacc,a.PAYEE_BANK_NAME as payeeBankName,a.acceptoracc,a.ACCEPTOR_BANK_NAME as acceptorBankName, ");
		sql.append(" c.brief_id as briefid,(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_012+"' " +
				"then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestMoney,");
		sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
		sql.append(" b.bank_acc as bankAcc,b.bank_name as bankName, b.BUS_CORP_NAME as busCorpName ,");
		sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,");
		sql.append(" c.business_date as businessDate, ");

		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.CORP_NAME as applyCorpName, b.CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_acct_id ELSE c.rec_acct_id END) as frontHandAcc, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_nm ELSE c.rec_nm END) as frontHand, ");
		// 新一代票据列表查询新增字段 -- start
		sql.append(" a.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, a.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" a.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, a.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" a.BL_STD_AMT AS blStdAmt, a.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" a.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, a.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" b.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,  ");
		// -- end
		/*贴现赎回申请返还利息 returnInterest
		 发起贴现赎回申请前，返还利息在VO中计算生成。当前流水关联的是贴现办理保存的贴现明细
		 贴现收回办理中 返还利息从贴现明细中获取。当前流水关联的是贴现赎回申请保存的贴现明细，贴现利息需要通过上一流水来关联贴现办理明细中获取
		 */
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN null ELSE t1.interest END) as returnInterest ");
		sql.append(" from ab_voucherinfo_vice b ");
		sql.append("  left join ab_voucherinfo a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append("  left join ab_accrecords c on b.id = c.vvid");
		sql.append("  left join ab_accrecords c1 on c1.id = c.lastid");
		sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6001 THEN c.id ELSE c1.id END) ");
		sql.append("  left join ab_discount t1 on t1.record_id = (CASE c.brief_id WHEN :briefId6001 THEN null ELSE c.id END)  ");
		//结算中心回购式贴入的纸据(应收登记状态，来源:贴现)
		sql.append(" where b.valid_sign=:validSign1 and b.CORP_TYPE = :corpType0 ");
		//审批状态查询
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :valid_sign0 and c.status = :status11 and c.brief_id=:briefId012 ");
			input.getFormdata().remove("eqs_statusName");
		//待记账状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS94).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :valid_sign0 and c.status = :status94 and c.brief_id=:briefId012 ");
			input.getFormdata().remove("eqs_statusName");
		//待申请状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS95).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and t.rpdmk = :rpdmkRM01 and a.bill_flag = :billFlag10");
			sql.append(" and c.valid_sign=:validSign1 and c.brief_id = :briefId6001 ");
			sql.append(" and c.status=:status95 and b.box_status = :boxStatusY " );
			sql.append(" and b.bill_source = :billSource14 and b.back_brief_id is null ");
			//日期条件,纸票不做贴现赎回截止日限制
			sql.append(" and :sysdate >= t.rpdopendt  ");
			sql.append(" and :sysdate <= case a.bill_flag when :billFlag11 "+
					" then t.rpdduedt else :sysdate end ");
		//默认查询
		}else{
			sql.append(" and ((t.rpdmk = :rpdmkRM01 and a.bill_flag = :billFlag10");
			sql.append(" and c.valid_sign=:validSign1 and c.brief_id = :briefId6001 ");
			sql.append(" and c.status=:status95 and b.box_status = :boxStatusY" );
			sql.append(" and b.bill_source = :billSource14 ");
			//日期条件,纸票不做贴现赎回截止日限制
			sql.append(" and :sysdate >= t.rpdopendt  ");
			sql.append(" and :sysdate <= case a.bill_flag when :billFlag11 "+
					" then t.rpdduedt else :sysdate end ");
            sql.append(" ) or ( ");
			sql.append(" c.valid_sign = :valid_sign0 and (c.status =:status11 or c.status =:status94 )and c.brief_id=:briefId012)) ");
			//打回状态查询
			if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))) {
				sql.append (" and b.back_brief_id = :briefId012 and t.id is not null ");
				input.getFormdata ().remove ("eqs_statusName");
			}
		}
		//sql.append(" and ").append(corpAuthSql).append(" ");
		Map<String,Object> params = new HashMap<>();
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("corpType0", CorpConstant.CORP_TYPE_CODE_NET);
		params.put("valid_sign0",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("briefId012",AccebillConstant.BRIEF_STATUS_012);
		params.put("rpdmkRM01",AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK);
		params.put("billFlag10",AccebillConstant.AB_BILL_FLAG_PAPER);
		params.put("status94",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("boxStatusY",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billSource14",AccebillConstant.AB_BILL_SOURCE_DISCOUNT);
		params.put("billFlag11",Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
		params.put("sysdate",new Date (System.currentTimeMillis()));
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
		}
		return this.getDynamicBySQL(input, sql.toString(),AbBillDiscountRansomVo.class,"",false,params);
	}
	
	
	/**
	 * 加载审批页面列表数据信息
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillDiscountRansomVo> dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,");
		sql.append(" b.corp_id as corpId,b.corp_code as corpCode,b.corp_name as corpName,a.bill_flag as billflag,a.payer as payer,a.payee as payee,a.acceptor as acceptor,");
		sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,");
		sql.append(" c.brief_id as briefid,(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_012+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestmoney,");
		sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
		sql.append(" b.bank_acc as bankAcc,b.bank_name as bankName, ");
		sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,");
		sql.append(" c.business_date as businessDate, c.id as businessKey, ");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_acct_id ELSE c.rec_acct_id END) as frontHandAcc, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_nm ELSE c.rec_nm END) as frontHand, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN null ELSE t1.interest END) as returnInterest");
		sql.append(" from ab_voucherinfo_vice b ");
		sql.append("  left join ab_voucherinfo a on a.id = b.vid");
		sql.append("  left join ab_accrecords c on b.id = c.vvid");
		sql.append("  left join ab_accrecords c1 on c1.id = c.lastid");
		sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6001 THEN c.id ELSE c1.id END) ");
		sql.append("  left join ab_discount t1 on t1.record_id = (CASE c.brief_id WHEN :briefId6001 THEN null ELSE c.id END)  ");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return this.getDynamicBySQL(dataTablesInput, sql.toString(),AbBillDiscountRansomVo.class,"",false,params);
	}
	
	/**
	 * 加载审批详细页面
	 * @param aid
	 * @return
	 */
	public AbBillDiscountRansomVo getAbBillDiscountRansomVoById(String aid) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" a.id as vid,b.id as vvid,c.id as aid,t.id as tid,a.bill_type as billtype,a.banendrsmtmk as banendrsmtmk,");
		sql.append(" b.corp_id as corpid,a.bill_flag as billflag,a.payer as payer,a.payee as payee,a.acceptor as acceptor,");
		sql.append(" a.orig_no as origno,a.bill_money as billmoney,a.start_date as startdate,a.end_date as enddate,");
		sql.append("  a.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd,a.payeracc,a.PAYER_BANK_NAME as payerBankName,");
		sql.append(" a.payeeacc,a.PAYEE_BANK_NAME as payeeBankName,a.acceptoracc,a.ACCEPTOR_BANK_NAME as acceptorBankName, ");
		sql.append(" c.brief_id as briefid,(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_012+
				"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append(" t.paydate as paydate,t.deferdate as deferdate,t.daynum as daynum,t.interest as interest,t.buyerinterest_money as buyerinterestmoney,");
		sql.append(" t.real_money as realmoney,c.bankendrsmtmk as bankendrsmtmk,t.invc_nb as invcnb,t.txl_ctrct_nb as txlctrctnb,b.enter_stock as enterstock,");
		sql.append(" b.bank_acc as bankAcc,b.bank_name as bankName, ");
		sql.append(" cast(t.rpdopendt as DATE) as rpdopendt,cast(t.rpdduedt as DATE) as rpdduedt,t.interest_rates as interestrates,");
		sql.append(" c.business_date as businessDate, c.id as businessKey,c.apply_date as applyDate, ");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_acct_id ELSE c.rec_acct_id END) as frontHandAcc, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN c.req_nm ELSE c.rec_nm END) as frontHand, ");
		sql.append(" (CASE c.brief_id WHEN '"+AccebillConstant.BRIEF_STATUS_6001+"' THEN null ELSE t1.interest END) as returnInterest ");
		sql.append(" from ab_voucherinfo_vice b ");
		sql.append("  left join ab_voucherinfo a on a.id = b.vid");
		sql.append("  left join ab_accrecords c on b.id = c.vvid");
		sql.append("  left join ab_accrecords c1 on c1.id = c.lastid");
		sql.append("  left join ab_discount t on t.record_id = (CASE c.brief_id WHEN :briefId6001 THEN c.id ELSE c1.id END) ");
		sql.append("  left join ab_discount t1 on t1.record_id = (CASE c.brief_id WHEN :briefId6001 THEN null ELSE c.id END)  ");
		sql.append(" WHERE c.id = :aid ");
		Map<String, Object> params=new HashMap<String, Object>();
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("aid", aid);
		List<AbBillDiscountRansomVo> listInfo=this.listBySQLAliasToBean(sql.toString(), params, AbBillDiscountRansomVo.class);
		if(listInfo!=null&&listInfo.size()>0){
			return listInfo.get(0);
		}
		return new AbBillDiscountRansomVo();
	}
	
	
	
}
