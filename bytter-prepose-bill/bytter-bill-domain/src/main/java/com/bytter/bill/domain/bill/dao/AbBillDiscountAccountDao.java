package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillDiscountAccountVO;
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
* @Description:贴现到账Dao
* @author onlineGenerator
* @date 2017-7-21 13:51:15
* @version 1.0
*
*/
@Repository
public class AbBillDiscountAccountDao extends BaseDao<AbBillDiscountAccountVO,String>{
	/**
	 *列表显示贴现到账信息
	 *@param input
	 *@return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillDiscountAccount(DataTablesInput input,List<String> corpAuthList, String isApply){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,S.BUSINESS_DATE as businessDate,");
		sql.append(" P.CORP_ID AS corpId,p.corp_code AS corpCode,p.corp_name AS corpName,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
		sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,S.SIGNEROK AS signerok,");
		sql.append(" S.BRIEF_ID AS briefId,(case  when P.back_brief_id=:back_brief_id_0 then -1 else S.status end)  AS status,");
		params.put("back_brief_id_0",AccebillConstant.BRIEF_STATUS_6012);
		sql.append(" T.PAYDATE AS paydate,T.DEFERDATE AS deferdate,T.DAYNUM AS daynum,T.INTEREST AS interest,T.BUYERINTEREST_MONEY AS buyerinterestMoney,");
		sql.append(" T.REAL_MONEY AS realMoney,S.BANKENDRSMTMK AS banendrsmtmk,T.INVC_NB AS invcNb,T.TXL_CTRCT_NB AS txlCtrctNb,P.ENTER_STOCK AS enterStock,");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,p.apply_corp_name as applyCorpName,p.apply_corp_code as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
		//		--前手
		sql.append("case ");
		sql.append("when s.brief_id='6001' then s.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else P.cn_accrecords end as cnAccrecords ,");
		//		--前手
		// 新一代票据列表查询新增字段 -- start
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" V.BL_STD_AMT AS blStdAmt, V.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" V.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, V.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" P.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo , P.BUS_CORP_NAME as busCorpName");
		// -- end
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" INNER JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
		sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
		//从表validSign=1
		sql.append(" WHERE P.VALID_SIGN=:VALID_SIGN_1");
		params.put("VALID_SIGN_1", AccebillConstant.AB_BILL_VALID_SIGN_1);
		//第一种应收贴现办理已完成或贴现到账打回
		//流水validSign=1,optStatus=19,status=95,主表boxStatus=11
		sql.append(" AND ((S.BRIEF_ID IN (:BRIEF_ID_1) and S.VALID_SIGN=:VALID_SIGN_2 ");
		params.put("BRIEF_ID_1", AccebillConstant.BRIEF_STATUS_011);
		params.put("VALID_SIGN_2", AccebillConstant.AB_BILL_VALID_SIGN_1);
		sql.append(" AND S.OPT_STATUS=:OPT_STATUS_1 AND S.STATUS=:STATUS_1");
		params.put("OPT_STATUS_1", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("STATUS_1", AccebillConstant.AB_BILL_BUS_STATUS95);
		sql.append(" AND P.BOX_STATUS=:BOX_STATUS_1 ");
		params.put("BOX_STATUS_1", AccebillConstant.AB_BOX_STATUS_Y);
		//如果是收款认领来的数据是可以显示线上清算数据
		if(AccebillConstant.ACCEBILL_IS_APPLY.equals(isApply)){
			sql.append(" )");
		}else{
			//只对线下清算或离线模式的票据进行到账确认
			sql.append(" and (s.sttlmmk = :sttlmmkSM01 or s.send_sign = :sendSignN ) )");
			params.put("sttlmmkSM01", AccebillConstant.AB_STTLMMK_N);
			params.put("sendSignN", AccebillConstant.AB_BILL_SEND_SIGN_N);
		}
		//第二种贴现到账处于审批中
		//流水validSign=0,status=11,主表boxStatus=10
		sql.append(" or (S.BRIEF_ID IN (:BRIEF_ID_2) and S.VALID_SIGN=:VALID_SIGN_3");
		params.put("BRIEF_ID_2", AccebillConstant.BRIEF_STATUS_6012);
		params.put("VALID_SIGN_3", AccebillConstant.AB_BILL_VALID_SIGN_0);
		sql.append(" and P.BOX_STATUS=:BOX_STATUS_2 AND S.STATUS in (:STATUS_2)))");
		params.put("BOX_STATUS_2", AccebillConstant.AB_BOX_STATUS_N);
		Integer[] status_2={ AccebillConstant.AB_BILL_BUS_STATUS11, AccebillConstant.AB_BILL_BUS_STATUS94};
		params.put("STATUS_2", status_2);
		// 判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，清空查询条件
		if (input.getFormdata().get("eqi_status") != null && "-1".equals(input.getFormdata().get("eqi_status"))) {
			sql.append(" and P.Back_Brief_Id=:Back_Brief_Id_1 ");
			params.put("Back_Brief_Id_1", AccebillConstant.BRIEF_STATUS_6012);
			input.getFormdata().put("eqi_status", null);
		}
        // 判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
		if (input.getFormdata().get("eqi_status") != null && "95".equals(input.getFormdata().get("eqi_status"))) {
			sql.append(" and P.Back_Brief_Id is null  ");
		}
        if(AccebillConstant.ACCEBILL_IS_APPLY.equals(isApply)){
			sql.append(" and  S.BRIEF_ID != :BRIEF_ID_2 ");
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "P.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(")");
		}
		return this.getDynamicBySQL(input, sql.toString(),AbBillDiscountAccountVO.class,"",false,params);
	}
	/**
	 * 贴现到账审批列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillDiscountAccountVO> dataTableApprove(DataTablesInput input,List<String> businessList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" P.CORP_ID AS corpId,p.corp_code AS corpCode,V.BILL_FLAG AS billFlag,V.PAYER AS payer,V.ACCEPTOR AS acceptor,");
		sql.append(" V.ORIG_NO AS origNo,V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,S.SIGNEROK AS signerok,");
		sql.append(" S.BRIEF_ID AS briefId,S.id as businessKey,");
		sql.append(" T.PAYDATE AS paydate,T.DEFERDATE AS deferdate,T.DAYNUM AS daynum,T.INTEREST AS interest,T.BUYERINTEREST_MONEY AS buyerinterestMoney,");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,p.apply_corp_name as applyCorpName,p.apply_corp_code as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" T.REAL_MONEY AS realMoney,S.BANKENDRSMTMK AS banendrsmtmk,T.INVC_NB AS invcNb,T.TXL_CTRCT_NB AS txlCtrctNb,P.ENTER_STOCK AS enterStock");
		sql.append(" , P.BUS_CORP_NAME as busCorpName, ");
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn ");

		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID  and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" LEFT JOIN AB_DISCOUNT T ON T.RECORD_ID=S.ID");
		sql.append(" left join sys_corp s2 on p.apply_corp_id = s2.id ");
		//从表validSign=1
		sql.append(" WHERE 1=1 ");
		String businessSql= SqlUtil.getInSqlByParams(businessList, "S.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return this.getDynamicBySQL(input, sql.toString(),AbBillDiscountAccountVO.class,"",false,params);
	}
}
