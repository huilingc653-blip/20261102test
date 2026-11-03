package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbReceiveBillVO;
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
* @Description:AbReceiveBillDao
* @author onlineGenerator
* @date 2017-06-22 10:44:59
* @version 1.0
*
*/
@Repository
public class AbReceiveBillDao  extends BaseDao<AbReceiveBillVO,String>{
	/**
	 *列表显示应收票据登记信息
	 *@param input
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbReceiveBill(DataTablesInput input, List<String> corpAuthList, String userCode){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,S.business_date as businessDate,V.BILL_TYPE AS billType,");
		sql.append(" P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, S.STATUS AS status,");
		// 新一代票据列表查询新增字段 -- start
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ");
		// -- end
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,P.CORP_CODE as corpCode,P.CORP_NAME as corpName,");
		sql.append(" S.brief_id as briefid,ba.num as attachmentNum,ba.attachment_id as attachmentId,S.cn_Accrecords as cnAccrecords, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode");
		sql.append(",P.SOC_CODE as socCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, P.BUS_CORP_NAME as busCorpName ");
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" left join ab_bill_attachment ba on ba.acid = s.id and ba.brief_id=:brief_id_1 ");
		params.put("brief_id_1", AccebillConstant.BRIEF_STATUS_6001);
		//从表validSign=1,从表boxStatus=10
		sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1 AND P.BOX_STATUS=:BOX_STATUS_1 ");
		params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_N);
		//业务为收票登记，暂存，审批中，审批打回，流水validSign=0
		sql.append(" AND S.BRIEF_ID=:BRIEF_ID_2 AND S.VALID_SIGN=:VALID_SIGN_2");
		params.put("BRIEF_ID_2", AccebillConstant.BRIEF_STATUS_6001);
		params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_0);
		sql.append(" AND S.STATUS IN(:STATUS_1)");
		params.put("STATUS_1",new Integer[]{
				AccebillConstant.AB_BILL_BUS_STATUS0,
				AccebillConstant.AB_BILL_BUS_STATUS11,
				AccebillConstant.AB_BILL_BUS_STATUS_1,
				AccebillConstant.AB_BILL_BUS_STATUS94});
		//创建用户才可看到
		sql.append(" and ( s.CREATE_BY=:CREATE_BY or s.CREATE_NAME is null)");
		params.put("CREATE_BY",userCode);
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "P.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(") ");
		}
		return this.dealDynamicBySQL(input, sql.toString(),AbReceiveBillVO.class,"",false,params);
	}
	/**
	 * 显示编辑的应收票据信息
	 * @param accId
	 * @return
	 */
	public AbReceiveBillVO getAbReceiveBillInfo(String accId){
		Map<String,String> params=new HashMap<String,String>();
		StringBuilder sql=new StringBuilder("select ");		
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,V.BILL_FLAG AS billFlag,");
		// 新一代票据列表查询新增字段 -- start
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ");
		// -- end
		sql.append(" P.CORP_ID AS corpId,P.BANK_ACC AS bankAcc,P.BANK_CODE AS bankCode,P.BANK_Name AS bankName,P.ATTACHMENT_ID AS attachmentId,");
		sql.append(" V.PAYER AS payer,V.PAYERACC AS payeracc,V.PAYER_BANK_NAME AS payerBankName,V.PAYERBANKNO AS payerbankno,");
		sql.append(" V.ACCEPTOR AS acceptor,V.ACCEPTORACC AS acceptoracc,V.ACCEPTOR_BANK_NAME AS acceptorBankName,V.ACCEPTORBANKNO AS acceptorbankno,");
		sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,S.BUSINESS_DATE AS businessDate,");
		sql.append(" V.PAYEE AS payee,V.PAYEEACC AS payeeacc,V.PAYEE_BANK_NAME AS payeeBankName,V.PAYEEBANKNO AS payeebankno,");
		sql.append(" S.STATUS AS status,P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,V.BANENDRSMTMK AS banendrsmtmk,S.FEEMONEY as feemoney,");
		sql.append(" V.A_CONTRACT_NO AS acontractNo,V.A_INVCNB AS ainvcnb,P.CN_ACCRECORDS AS cnAccrecords,P.ABS AS abs,P.RMK AS rmk,P.INSIDE_SING,");
		sql.append(" T.DISCOUNT_DATE as discountDate,T.DAYNUM AS daynum,T.INTEREST AS interest ,T.RPDMK AS rpdmk,T.STTLMMK AS sttlmmk,");
		sql.append(" T.PAYDATE AS paydate,T.INTEREST_RATES AS interestRates,T.RPDOPENDT AS rpdopendt,T.RPDDUEDT AS rpdduedt,s.LASTID as lastid,s.trader as trader,");
		sql.append(" V.payer_cdt_ratgs as payerCdtRatgs,V.payer_cdt_ratg_agcy as payerCdtRatgGgcy,V.payer_cdt_ratg_duedt as payerCdtRatgDuedt, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,p.APPLY_CORP_NAME as applyCorpName,p.APPLY_CORP_CODE as applyCorpCode,P.BUS_SOC_CODE as socCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel ");
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" LEFT JOIN AB_DISCOUNT T ON S.ID=T.RECORD_ID");
//		sql.append(" left join sys_corp s2 on p.apply_corp_id = s2.id ");
//		sql.append(" left join sys_corp s3 on P.BUS_CORP_ID = s3.id ");
		sql.append(" where S.ID=:accId");
		params.put("accId",accId);
		AbReceiveBillVO billInfo=this.getBySQLAliasToBean(sql.toString(),params,AbReceiveBillVO.class);
		return billInfo;
	}
	/**
	 * 收票登记信息页面列表查询信息
	 * @param input 查询条件。
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbReceiveBillVO> dataTableApprove(DataTablesInput input,List<String> businessList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,S.business_date as businessDate,S.id as businessKey,");
		sql.append(" P.CORP_ID AS corpId,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,");
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,p.corp_code as corpCode,p.corp_name as corpName,");
		sql.append(" S.brief_id as briefid,ba.num as attachmentNum,ba.attachment_id as attachmentId, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.apply_corp_name as applyCorpName,P.apply_corp_code as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel ");
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" left join ab_bill_attachment ba on ba.acid = s.id and ba.brief_id=:brief_id_1");
		params.put("brief_id_1", AccebillConstant.BRIEF_STATUS_6001);
		sql.append(" WHERE 1=1");
		String businessSql= SqlUtil.getInSqlByParams(businessList, "S.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return this.dealDynamicBySQL(input, sql.toString(),AbReceiveBillVO.class,"",false,params);
	}
	/**
	 * 通过票据号查询票据主信息
	 * @param origNo
	 * @return
	 */
	public AbVoucherinfo getVouInfoByOrigNo(String origNo){
		String hql="from AbVoucherinfo where origNo=:origNo order by createDate desc ";
		Map<String,String> params=new HashMap<String,String>();
		params.put("origNo",origNo);
		@SuppressWarnings("unchecked")
		List<AbVoucherinfo> list=this.listByHQL(hql, params);
		AbVoucherinfo vouInfo=null;
		if(list!=null&&list.size()>0){
			vouInfo=list.get(0);
		}
		return vouInfo;
	}
}
