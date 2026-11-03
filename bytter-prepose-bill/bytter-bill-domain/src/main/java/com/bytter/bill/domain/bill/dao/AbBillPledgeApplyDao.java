package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillPledgeApplyVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据质押申请
* @author zhenghj
* @date 2017-07-07
* @version 1.0
*
*/
@Repository
public class AbBillPledgeApplyDao extends BaseDao<AbBillPledgeApplyVo,String>{
	private  String getQuerySQL(Map<String,Object> params){
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, P.BANK_ACC AS bankAcc, ");
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,S.BRIEF_ID as briefId,");
		sql.append(" (case p.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_018+"' then -1 else S.STATUS end) as status, ");
		sql.append(" ba.num as attachmentNum,ba.attachment_id as attachmentId, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode");
		sql.append(" , P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel, P.BUS_CORP_NAME as busCorpName, ");
		//		--前手
		sql.append(" case ");
		sql.append(" when s.brief_id='6001' then s.cn_accrecords ");
		sql.append(" when bs.bus_type in (:bus_type_0) then s.rec_nm ");
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append(" else P.cn_accrecords end as cnAccrecords, ");
		//		--前手
		sql.append(" v.new_stm_issuofppr_ind as newStmIssuofpprInd, v.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, v.sbbll_rng_strt_sn as sbbllRngStrtSn," );
		sql.append(" v.sbbll_rng_end_sn as sbbllRngEndSn, v.bl_std_amt as blStdAmt, P.SOC_CODE as socCode, v.bill_money as transMoney");
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		//质押申请 出票标志
		if (params.containsKey("eqs_newStmIssuofpprInd")){
			sql.append(" and v.new_stm_issuofppr_ind =:eqs_newStmIssuofpprInd ");
		}
		//票据是否允许分包
		if (params.containsKey("eqs_blgrdbagcrclPrmtInd")){
			sql.append(" and v.blgrdbagcrcl_prmt_ind =:eqs_blgrdbagcrclPrmtInd ");
		}
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" left join ab_bill_attachment ba on ba.acid in (S.id,S.lastid) and ba.brief_id='"+AccebillConstant.BRIEF_STATUS_018+"' ");
		sql.append(" left join ab_brief_status bs on bs.status=S.brief_id ");
		sql.append(" WHERE P.VALID_SIGN=:validSign");
		sql.append(" and (P.BILL_SOURCE<>:billSource or P.BILL_SOURCE is null) ");
		//票据为可再转让
		sql.append(" and BT_NVL_CHAR(S.BANKENDRSMTMK,V.BANENDRSMTMK)=:banendrsmtmk ");
		sql.append(" and S.STATUS<>:status ");
		sql.append(" and ((P.box_status=:boxStatus1"
				+" and S.valid_sign=:validSign1"
				+" and S.opt_status=:optStatus1"
				+" and S.STATUS=:status1"
				+" and S.brief_id in(:briefId1)) ");
		params.put("validSign",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("billSource",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("banendrsmtmk",AccebillConstant.AB_BANEM_Y);
		params.put("status",AccebillConstant.AB_BILL_BUS_STATUS_2 );
		params.put("boxStatus1",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("optStatus1",AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_6001,AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019,AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6212});
		//追加贴现收回流水查询做质押申请功能
		return sql.toString();
	}
	/**
	 * 列表显示应收票据质押申请信息
	 * @param input
	 * @param  corpAuthList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbBillPledgeApply(DataTablesInput input,List<String> corpAuthList){
		Map<String,Object> params=new HashMap<>();
		Map<String, Object> formdata = input.getFormdata();
		//出票标志
		if (formdata.containsKey("eqs_newStmIssuofpprInd")){
			String newStmIssuofpprInd = String.valueOf(formdata.get("eqs_newStmIssuofpprInd"));
			if (StringUtils.isNotBlank(newStmIssuofpprInd)){
				params.put("eqs_newStmIssuofpprInd", newStmIssuofpprInd);
			}
		}
		//票据是否允许分包
		if (formdata.containsKey("eqs_blgrdbagcrclPrmtInd") ){
			String blgrdbagcrclPrmtInd = String.valueOf(formdata.get("eqs_blgrdbagcrclPrmtInd"));
			if (StringUtils.isNotBlank(blgrdbagcrclPrmtInd)){
				params.put("eqs_blgrdbagcrclPrmtInd", formdata.get("eqs_blgrdbagcrclPrmtInd"));
			}
		}
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(" or (P.box_status =:boxStatus2   and S.valid_sign =:validSign2 "
				+" and S.brief_id in (:briefId2))) ");
		params.put("boxStatus2",AccebillConstant.AB_BOX_STATUS_N);
		params.put("validSign2",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("briefId2",new String[]{AccebillConstant.BRIEF_STATUS_018});
		//判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，再把查询拼接的打回清空
		if(input.getFormdata().get("eqi_status")!=null && "-1".equals(input.getFormdata().get("eqi_status"))){
			sql.append(" and P.Back_Brief_Id=:backBriefId ");
			input.getFormdata().put("eqi_status",null);
			params.put("backBriefId",AccebillConstant.BRIEF_STATUS_018);
		}
		//判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
		if(input.getFormdata().get("eqi_status")!=null && "95".equals(input.getFormdata().get("eqi_status"))){
			sql.append(" and P.Back_Brief_Id is null ");
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "p.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(") ");
		}
		String applyDate = DateUtils.date2Str(new Date(),DateUtils.date_sdf());
		sql.append(" and v.banendrsmtmk = 'EM00'");
		//banendrsmtmk is '不得转让标记em00可再转让em01不得转让'
		//质押申请列表查询条件在原查询条件中加上，若为新一代票据，票据状态需为已收票（CS03），流通标志为可流通（TF0301）
		//sql.append(" or (V.bill_status = 'CS03' and V.ebill_bl_crcl_indc = 'TF0301') ");
		DataTablesOutput dataOutInfo = this.getDynamicBySQL(input, sql.toString(),AbBillPledgeApplyVo.class,"",false,params);
		return dataOutInfo;
	}
	/**
	 * 导入根据单位名称、票据号码查询质押申请数据
	 * @param abBillPledgeApplyVo 质押申请vo
	 * @param corpAuthList 查询权限特殊管理单位
	 * @return 返回列表数据
	 */
	public List<AbBillPledgeApplyVo> excelListAbBillPledgeApply(AbBillPledgeApplyVo abBillPledgeApplyVo, List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(")");
		sql.append(" AND P.CORP_CODE=:corpCode AND V.ORIG_NO=:origNo AND V.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn and V.sbbll_Rng_end_Sn=:sbbllRngEndSn ");
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "p.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		params.put("corpCode",abBillPledgeApplyVo.getCorpId());
		params.put("origNo",abBillPledgeApplyVo.getOrigNo());
		params.put("sbbllRngStrtSn",abBillPledgeApplyVo.getSbbllRngStrtSn());
		params.put("sbbllRngEndSn",abBillPledgeApplyVo.getSbbllRngEndSn());
		return this.listBySQLAliasToBean(sql.toString(),params, AbBillPledgeApplyVo.class);
	}
	/**
	 * 根据主表id 从表id查询质押申请数据
	 * @param vid 主表id
	 * @param vvid 从表id
	 * @return 返回列表数据
	 */
	public List<AbBillPledgeApplyVo> listAbBillPledgeApplyVoByVidAndVvid(String vid,String vvid) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(")");
		sql.append(" and p.id = :vvid  ");
		sql.append(" and v.id= :vid  ");
		params.put("vid",vid);
		params.put("vvid",vvid);
		return this.listBySQLAliasToBean(sql.toString(),params, AbBillPledgeApplyVo.class);
	}
	/**
	 * 加载审批页面列表数据信息
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillPledgeApplyVo> dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
		Map<String,Object> param=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, ");
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,");
		sql.append(" S.ID AS businessKey,");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" (case p.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_018+"' then -1 else S.STATUS end) as status,  ");
		sql.append(" V.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, V.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" V.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, V.SBBLL_RNG_END_SN AS sbbllRngEndSn ");

		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		//----------------------移存审批中的数据----------------------
		sql.append(" WHERE 1=1");
		String businessSql = SqlUtil.getInSqlByParams(businessList, "s.id", param);
		sql.append(" and ").append(businessSql).append(" ");
		DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillPledgeApplyVo.class, "", false,param);
		return dataOutInfo;
	}
}
