package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountManagerVO;
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
 * @Description:贴现办理Dao
 * @author onlineGenerator
 * @date 2017-07-05 15:51:08
 * @version 1.0
 */
@Repository
public class AbBillDiscountManagerDao extends BaseDao<AbBillDiscountManagerVO,String>{
	/**
	 * 公共查询sql
	 * @param params 参数
	 * @param isNotDiscountApply 不启用贴现申请功能直接贴现办理
	 * @return
	 */
	public String getSqlQuery(Map<String,Object> params,boolean isNotDiscountApply){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpname,P.BANK_ACC AS bankAcc,P.BANK_CODE AS bankCode," );
		sql.append(" V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
		sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate," );
		sql.append(" V.PAYEE AS payee,V.ACCEPTORBANKNO AS acceptorBankNo,");
		sql.append(" S.BRIEF_ID AS briefId,(case  when P.back_brief_id=:back_brief_id_0 then -1 else S.status end)  AS status,");
		params.put("back_brief_id_0",AccebillConstant.BRIEF_STATUS_011);
		sql.append(" T.PAYDATE AS paydate,T.DEFERDATE AS deferdate,T.DAYNUM AS daynum,T.INTEREST AS interest," );
		sql.append(" T.BUYERINTEREST_MONEY AS buyerinterestMoney,T.BUYERINTEREST_RATIO AS buyerinterestRatio,");
		sql.append(" T.REAL_MONEY AS realMoney,S.BANKENDRSMTMK AS bankendrsmtmk,T.INVC_NB AS invcNb,T.TXL_CTRCT_NB AS txlCtrctNb," );
		sql.append(" P.ENTER_STOCK AS enterStock,T.RPDINTRSTRATE AS rpdintrstRate,");
		sql.append(" T.DISCOUNT_DATE AS discountDate,T.RPDMK as rpdmk,T.INTEREST_RATES as interestRates,T.STTLMMK as sttlmmk," );
		sql.append(" T.AOACCNINF_ID as aoaccninfId,t.AOACCNINF_AC as aoaccninfAc, ");
		sql.append(" T.RPDOPENDT as rpdopendt,T.RPDDUEDT as rpdduedt, ");
		sql.append(" s.REC_NM as recNm,s.REC_ACCTSVCR as recAcctsvcr,s.REC_ACCT_ID as recAcctId,s.send_sign as sendSign,s.REQ_ACCTSVCR as reqAcctsvcr,");
		sql.append(" ba.num as attachmentNum,ba.attachment_id as attachmentId, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,p.APPLY_CORP_NAME as applyCorpName,p.APPLY_CORP_CODE as applyCorpCode," );
		sql.append(" P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
		//新增查询归属日期时间项,贴入机构项
		sql.append(" S.BUSINESS_DATE as businessDate,S.INSIDE_SING as insideSing,S.RMK AS RMK,T.ID as did, ");
		//		--前手
		sql.append("case ");
		sql.append("when s.brief_id='6001' then s.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else P.cn_accrecords end as cnAccrecords ");
		//		--前手
		// 新一代票据列表查询新增字段 -- start
		sql.append(" ,V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo , V.BILL_MONEY as transMoney, P.BUS_CORP_NAME as busCorpName");
		// -- end
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
		sql.append(" left join ab_bill_attachment ba on ba.acid in (s.id,s.lastid) and ba.brief_id=:brief_id_1 ");
		sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
		params.put("brief_id_1", AccebillConstant.BRIEF_STATUS_011);
		//从表validSign=1
		sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
		sql.append(" and (T.id is null or t.id in (select max(d.id) from ab_discount d where d.record_id=s.id) ) ");
		params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		if(isNotDiscountApply){//系统不启用贴现申请功能直接贴现办理功能时
			//第一种业务已完成，或打回的票据
			//流水validSign=1,optStaus=19,status=95,主表boxStatus=11
			sql.append(" AND (((( S.VALID_SIGN=:D_VALID_SIGN_2 AND S.OPT_STATUS=:D_OPT_STATUS_1");
			params.put("D_VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
			params.put("D_OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
			sql.append(" AND S.STATUS=:D_STATUS_1 AND P.BOX_STATUS=:D_BOX_STATUS_1)");
			params.put("D_STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
			params.put("D_BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
			//应收票据登记，纸票
			sql.append(" AND ((S.BRIEF_ID IN (:D_BRIEF_ID_1)");
			params.put("D_BRIEF_ID_1", new String[]{AccebillConstant.BRIEF_STATUS_6001,AccebillConstant.BRIEF_STATUS_6019,
					AccebillConstant.BRIEF_STATUS_6212});
			sql.append(" AND P.BILL_SOURCE <> :D_BILL_SOURCE)");
			params.put("D_BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
			//提示收票签收，背书申请签收，质押解除签收，回购式贴现赎回签收，且为电票
			sql.append(" or (S.BRIEF_ID IN (:D_BRIEF_ID_2) AND S.SEND_SIGN=:D_SEND_SIGN_1)))");
			params.put("D_BRIEF_ID_2", new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,
					AccebillConstant.BRIEF_STATUS_9019,AccebillConstant.BRIEF_STATUS_9012});
			params.put("D_SEND_SIGN_1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
			//票据为可再转让
			sql.append(" and BT_NVL_CHAR(S.BANKENDRSMTMK,V.BANENDRSMTMK)=:D_BANENDRSMTMK) ");
			params.put("D_BANENDRSMTMK", AccebillConstant.AB_BANEM_Y);
			sql.append(" or (");
			//第一种，贴现申请业务完成，或被打回的票据的票据
			//流水validSign=1,optStatus=19,status=95,主表boxStatus=11
			sql.append(" ((S.BRIEF_ID IN (:BRIEF_ID_2) and S.VALID_SIGN=:VALID_SIGN_2");
			params.put("BRIEF_ID_2", AccebillConstant.BRIEF_STATUS_6011);
			params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
			sql.append(" AND S.OPT_STATUS=:OPT_STATUS_1 AND S.STATUS=:STATUS_1");
			params.put("OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
			params.put("STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
			sql.append(" AND P.BOX_STATUS=:BOX_STATUS_1)");
			params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
			//第二种贴现办理处于审批中和待记账的票据
			//流水validSign=0,status=11,主表boxStatus=10
			sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_3) AND S.VALID_SIGN=:VALID_SIGN_3");
			params.put("BRIEF_ID_3",AccebillConstant.BRIEF_STATUS_011);
			params.put("VALID_SIGN_3",AccebillConstant.AB_BILL_VALID_SIGN_0);
			sql.append(" and P.BOX_STATUS=:BOX_STATUS_2 and S.STATUS in (:STATUS_2)))");
			Integer[] status_2={AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS94};
			params.put("BOX_STATUS_2", AccebillConstant.AB_BOX_STATUS_N);
			params.put("STATUS_2", status_2);
			sql.append(" )) ");
		}else{//系统启用贴现申请功能功能时
			//第一种，贴现申请业务完成，或被打回的票据的票据
			//流水validSign=1,optStatus=19,status=95,主表boxStatus=11
			sql.append(" AND ((S.BRIEF_ID IN (:BRIEF_ID_2) and S.VALID_SIGN=:VALID_SIGN_2");
			params.put("BRIEF_ID_2", AccebillConstant.BRIEF_STATUS_6011);
			params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
			sql.append(" AND S.OPT_STATUS=:OPT_STATUS_1 AND S.STATUS=:STATUS_1");
			params.put("OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
			params.put("STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
			sql.append(" AND P.BOX_STATUS=:BOX_STATUS_1)");
			params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
			//第二种贴现办理处于审批中和待记账的票据
			//流水validSign=0,status=11,主表boxStatus=10
			sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_3) AND S.VALID_SIGN=:VALID_SIGN_3");
			params.put("BRIEF_ID_3",AccebillConstant.BRIEF_STATUS_011);
			params.put("VALID_SIGN_3",AccebillConstant.AB_BILL_VALID_SIGN_0);
			sql.append(" and P.BOX_STATUS=:BOX_STATUS_2 and S.STATUS in (:STATUS_2)))");
			Integer[] status_2={AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS94};
			params.put("BOX_STATUS_2", AccebillConstant.AB_BOX_STATUS_N);
			params.put("STATUS_2", status_2);
		}
		return sql.toString();
	}
	/**
	 *列表显示贴现办理信息
	 *@param input
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillDiscountManager(DataTablesInput input,List<String> corpAuthList,boolean isNotDiscountApply){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(getSqlQuery(params,isNotDiscountApply));
		// 判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，清空查询条件
		if (input.getFormdata().get("eqi_status") != null && "-1".equals(input.getFormdata().get("eqi_status"))) {
			sql.append(" and P.Back_Brief_Id=:Back_Brief_Id_1 ");
			params.put("Back_Brief_Id_1",AccebillConstant.BRIEF_STATUS_011);
			input.getFormdata().put("eqi_status", null);
		}
		// 判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
		if (input.getFormdata().get("eqi_status") != null && "95".equals(input.getFormdata().get("eqi_status"))) {
			sql.append(" and (P.Back_Brief_Id is null or P.Back_Brief_Id!=:Back_Brief_Id_1) ");
			params.put("Back_Brief_Id_1", AccebillConstant.BRIEF_STATUS_011);
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "P.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
		}
		return this.getDynamicBySQL(input, sql.toString(),AbBillDiscountManagerVO.class,"",false,params);
	}
	/**
	 * 贴现办理审批列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input,List<String> businessList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" P.CORP_ID AS corpId,p.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,P.BANK_ACC AS bankAcc,P.BANK_CODE AS bankCode,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
		sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee,");
		sql.append(" S.BRIEF_ID AS briefId,S.id as businessKey,");
		sql.append(" T.PAYDATE AS paydate,T.DEFERDATE AS deferdate,T.DAYNUM AS daynum,T.INTEREST AS interest,T.BUYERINTEREST_MONEY AS buyerinterestMoney,T.BUYERINTEREST_RATIO AS buyerinterestRatio,");
		sql.append(" T.REAL_MONEY AS realMoney,S.BANKENDRSMTMK AS bankendrsmtmk,T.INVC_NB AS invcNb,T.TXL_CTRCT_NB AS txlCtrctNb,P.ENTER_STOCK AS enterStock,");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,p.APPLY_CORP_NAME as applyCorpName,p.APPLY_CORP_CODE as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" s.REC_NM as recNm,s.REC_ACCTSVCR as recAcctsvcr,s.REC_ACCT_ID as recAcctId,S.RMK AS RMK,s.REQ_ACCTSVCR as reqAcctsvcr,  ");
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn ");


		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
		sql.append(" WHERE 1=1 ");
		String businessSql= SqlUtil.getInSqlByParams(businessList, "S.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(),AbBillDiscountManagerVO.class,"",false,params);
	}
	public AbBillDiscountManagerVO getAbBillDiscountManagerVoByParams(Map<String,Object> params, String corpAuthSql, boolean isNotDiscountApply){
		StringBuilder sql=new StringBuilder(getSqlQuery(params,isNotDiscountApply));
		sql.append(" and V.orig_no=:origNo and V.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn and V.sbbll_Rng_end_Sn=:sbbllRngEndSn and P.corp_id=:corpId");
		sql.append(" and ").append(corpAuthSql).append(" ");
		return this.getBySQLAliasToBean(sql.toString(),params,AbBillDiscountManagerVO.class);
	}
}
