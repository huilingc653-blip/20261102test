package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillInvenFetchVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 应收票据取票
* @author onlineGenerator
* @date 2017-07-06
* @version 1.0
*
*/
@Repository
public class AbBillInvenFetchDao extends BaseDao<AbBillInvenFetchVo,String>{
	/**
	 * 列表显示应收票据取票信息
	 * @param input
	 * @param corpAuthList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillInvenFetchVo> listAbBillInvenFetch(DataTablesInput input,List<String> corpAuthList){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, ");
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,S.BRIEF_ID as briefId,");
		sql.append(" (case p.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6006+"' then -1 else S.STATUS end) as status,BT_NVL_CHAR(p.pool_Status,'0') as poolStatus, ");
		//		--前手
		sql.append("case ");
		sql.append("when s.brief_id='6001' then s.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then s.rec_nm ");
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else P.cn_accrecords end as cnAccrecords, ");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode");
		sql.append(",P.BUS_CORP_ID as busCorpId,P.BUS_CORP_NAME as busCorpName,P.BUS_PERSONNEL as busPersonnel ");
		//		--前手
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
		sql.append(" left join ab_pool_dtl pd on pd.id=s.pd_id ");//针对入池在途的数据可以托管到银行
		sql.append(" left join ab_brief_status bs on bs.status=s.brief_id ");
		if(input != null && !CollectionUtils.isEmpty(input.getFormdata()) && input.getFormdata().get("ins_bankTypeId") != null && !"".equals(input.getFormdata().get("ins_bankTypeId"))){
			sql.append(" left join bt_bank_acc acc on P.bank_acc=acc.BANK_ACC ");
		}
		sql.append(" WHERE 1=1 ");
		sql.append(" and P.BILL_SOURCE<>:billSource");
		sql.append(" and P.Enter_Stock<>:enterStock");
		sql.append(" AND V.BILL_FLAG=:billFlag" );
		params.put("validSign",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("billSource",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("enterStock",AccebillConstant.AB_ENTER_STOCK_CORP);
		params.put("billFlag",AccebillConstant.AB_BILL_FLAG_PAPER );
		sql.append(" and ((P.box_status =:boxStatus1 "
				+" and S.valid_sign =:validSign1 "
				+" and S.opt_status=:optStatus1"
				+" AND S.STATUS=:status1"
				+" and S.brief_id in (:briefId1) and P.VALID_SIGN=:validSign) ");
		//针对出池在途的数据可以取回到单位或者中心
		sql.append(" or (p.pool_Status=:poolStatus and P.box_status =:boxStatus1 and S.valid_sign=:validSign1 "
				+" and S.brief_id in (:briefIdPool) and s.pd_id is not null ) ");
		sql.append("or (P.box_status =:boxStatus2 "
				+" and S.valid_sign =:validSign2 "
				+" and S.brief_id in (:briefId2) and P.VALID_SIGN=:validSign) ");
		sql.append(") and S.STATUS<>:status2 " );
		params.put("boxStatus1",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("optStatus1",AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_6001,AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6212});
		params.put("boxStatus2",AccebillConstant.AB_BOX_STATUS_N);
		params.put("validSign2",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("briefId2",new String[]{AccebillConstant.BRIEF_STATUS_6006});
		params.put("status2",AccebillConstant.AB_BILL_BUS_STATUS_2);
		params.put("poolStatus",AccebillConstant.POOL_STATUS_3);
		params.put("briefIdPool",new String[]{AccebillConstant.AB_POOL_BRIEF_8019});
		//判断是否查询打回数据，是的话得查询从表的打回标识back_brief_id等于当前的状态代码，再把查询拼接的打回清空
		if(input.getFormdata().get("eqi_status")!=null && "-1".equals(input.getFormdata().get("eqi_status"))){
			sql.append(" and P.Back_Brief_Id=:backBriefId ");
			input.getFormdata().put("eqi_status",null);
			params.put("backBriefId",AccebillConstant.BRIEF_STATUS_6006);
		}
		//判断是否查询可操作状态数据，是的话得查询从表的打回标识back_brief_id等于null
		if(input.getFormdata().get("eqi_status")!=null && "95".equals(input.getFormdata().get("eqi_status"))){
			sql.append(" and P.Back_Brief_Id is null  ");
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "P.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (p.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "p.bank_acc", "bankAccs", params)).append(") ");
		}
		return getDynamicBySQL(input, sql.toString(),AbBillInvenFetchVo.class,"",false,params);
	}
	/**
	 * 加载审批页面列表数据信息
	 *
	 * @param dataTablesInput
	 * @param businessList
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillInvenFetchVo> dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" V.ID AS vouId,P.ID AS viceId,S.ID AS accId,V.BILL_TYPE AS billType,");
		sql.append(" V.BILL_FLAG AS billFlag,P.CORP_ID AS corpId,P.CORP_CODE AS corpCode,P.CORP_NAME AS corpName,V.PAYER AS payer,V.ACCEPTOR AS acceptor,V.ORIG_NO AS origNo,");
		sql.append(" V.BILL_MONEY AS billMoney,V.START_DATE AS startDate,V.END_DATE AS endDate,V.PAYEE AS payee, ");
		sql.append(" P.ENTER_STOCK AS enterStock,P.BILL_SOURCE as billSource,");
		sql.append(" S.ID AS businessKey,");
		sql.append(" P.APPLY_CORP_ID as applyCorpId,P.APPLY_CORP_NAME as applyCorpName,P.APPLY_CORP_CODE as applyCorpCode,P.BUS_CORP_ID as busCorpId,P.BUS_PERSONNEL as busPersonnel,");
		sql.append(" (case p.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6006+"' then -1 else S.STATUS end) as status,BT_NVL_CHAR(p.pool_Status,'0') as poolStatus ");
		sql.append(" from AB_VOUCHERINFO_VICE P");
		sql.append(" LEFT JOIN AB_VOUCHERINFO V ON V.ID=P.VID and (V.OLD_DRAFT_ID is null or V.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" LEFT JOIN AB_ACCRECORDS S ON P.ID=S.VVID");
//		sql.append(" LEFT JOIN SYS_CORP C on P.corp_id = C.id");
//		sql.append(" left join sys_corp s2 on p.apply_corp_id = s2.id ");
		//----------------------移存审批中的数据----------------------
		sql.append(" WHERE 1=1");
		String businessSql = SqlUtil.getInSqlByParams(businessList, "s.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		DataTablesOutput dataOutInfo = getDynamicBySQL(dataTablesInput, sql.toString(), AbBillInvenFetchVo.class, "", false,params);
		return  dataOutInfo;
	}
}
