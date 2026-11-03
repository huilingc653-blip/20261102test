package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.domain.bill.vo.AbEmitBillBackVo;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
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
 * @Description: 未用退票
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Repository
public class AbEmitBillBackDao extends BaseDao<AbVoucherinfo, String> {
	
	/**
	 * 加载未用表数据
	 * 
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbEmitBillBack(DataTablesInput dataTablesInput, List<String> corpAuthList) {
		boolean isEmitMake= dataTablesInput.getFormdata().containsKey(AccebillConstant.AB_CONFIG_ISEMITMAKE)
                && "true".equals(String.valueOf(dataTablesInput.getFormdata().get(AccebillConstant.AB_CONFIG_ISEMITMAKE)));
        Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(
				" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,c.source as source,");
		sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,"
				+ "(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_004
				+ "' then -1 else l.status end) as status,l.brief_id as briefId, ");
		sql.append(" (case l.status when "+AccebillConstant.AB_BILL_BUS_STATUS11+" then 1 else 0 end) as checkboxHide,l.send_sign as sendSign, ");
		sql.append("c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
		//新增出票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码、业务批次号
		sql.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN");
		sql.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,c.BATCH_NO as batchNo ");
		sql.append(" from ab_voucherinfo_vice c ");
		sql.append(" inner join ab_voucherinfo z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
		sql.append(" left join ab_accrecords l on c.id=l.vvid ");
//		sql.append(" left join sys_corp s on c.corp_id = s.id ");
//		sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
		sql.append(" where c.valid_sign= :C_VALID_SIGN and ");
		sql.append(" (c.is_pay is null or c.is_pay='0') and ");
		params.put("C_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
		// 从表 :处理状态 box_status -- 处理完成 11
		// 流水表 :处理标记 valid_sign -- 可用 1
		// 业务类型 brief_id -- (001,002,6101)
		// 当为6101(补录)时，主表BILL_FLAG(流通介质)为10(纸张)
		// 操作状态 opt_status -- 已签收19
 		sql.append("(");
 		sql.append("(c.box_status=:C_BOX_STATUS "
 				+ " and l.valid_sign= :L_Valid_Sign "
 				+ " and l.status= :L_STATUS95 "
 				+ " and l.opt_status=:L_OPT_STATUS "
 				+ " and ( l.brief_id in(:L_BRIEF_ID) ");
 		//zhengg 20200602 针对离线电票也是支持退票申请功能（为满足录错数据进行删除功能）
// 		if(!isEmitMake){//不启用补录模式时针线下电票不支持做退票功能
// 			sql.append(" and (z.bill_flag = :Z_BILL_FLAG "
// 					+ "or z.bill_type not in (:initBillTypes) "
// 					+ "or (z.bill_flag = :Z_BILL_FLAGELECT and l.send_sign=:SEND_SIGN_Y ) ) ");
// 		}
 				//针对纸票或者第三方票据类型支持补录完成以后的退票功能
 		sql.append(" or (l.brief_id=:L_BRIEF_ID6101 "
						+ " and (z.bill_flag = :Z_BILL_FLAG or z.bill_type not in (:initBillTypes) or l.send_sign=:SEND_SIGN_N ))"
				+ " )) ");
 		params.put("C_BOX_STATUS", AccebillConstant.AB_BOX_STATUS_Y);
 		params.put("L_Valid_Sign", AccebillConstant.AB_BILL_VALID_SIGN_1);
//		params.put("SEND_SIGN_Y", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		params.put("SEND_SIGN_N", AccebillConstant.AB_BILL_SEND_SIGN_N);
		params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("L_OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("L_BRIEF_ID", new String[]{AccebillConstant.BRIEF_STATUS_001, AccebillConstant.BRIEF_STATUS_002 });
		params.put("L_BRIEF_ID6101", AccebillConstant.BRIEF_STATUS_6101);
		params.put("Z_BILL_FLAG", AccebillConstant.AB_BILL_FLAG_PAPER);
		params.put("Z_BILL_FLAGELECT", AccebillConstant.AB_BILL_FLAG_ELECT);
		params.put("initBillTypes", new Integer[]{Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK), Integer.parseInt(AccebillConstant.AB_BILL_TYPE_MERCH)});
		// 业务类型 :brief_id -- 应付退票 004
		// 审批处理状态 :status -- 审批中 11
		sql.append(" or (l.brief_id = :L_BRIEF_ID004 "
						+ "and l.status= :L_STATUS11 ) ");
		params.put("L_BRIEF_ID004", AccebillConstant.BRIEF_STATUS_004);
		params.put("L_STATUS11", AccebillConstant.AB_BILL_BUS_STATUS11);
		sql.append(") ");
		//条件查询打回数据
		if(String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1).equals(dataTablesInput.getFormdata().get("eqi_status"))){
			sql.append(" and c.back_brief_id = :C_BACK_BRIEF_ID");
			params.put("C_BACK_BRIEF_ID", AccebillConstant.BRIEF_STATUS_004);
			dataTablesInput.getFormdata().remove("eqi_status");
		}
		if(String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95).equals(dataTablesInput.getFormdata().get("eqi_status"))){
			sql.append(" and (c.back_brief_id is null or c.back_brief_id = '') ");
		}						
		String corpAuthSql=SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillBackVo.class, "", false,params);
	}
	
	/**
	 * 加载退票审批中数据
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
		Map <String, Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(
				" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
		sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,l.brief_id as briefId,");
		sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" c.BUS_CORP_NAME AS busCorpName ");
		sql.append(" from ab_voucherinfo_vice c ");
		sql.append(" left join ab_voucherinfo z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
		sql.append(" left join ab_accrecords l on c.id=l.vvid ");
//		sql.append(" left join sys_corp s on c.corp_id = s.id ");
//		sql.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ");
		sql.append(" where 1=1 ");
		String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbEmitBillBackVo.class, "", false,params);
	}
	
	/**
	 *打回时候把当前票据从表可用状态修改成2不可用 
	 */
	
	public int updateViceValidSign(String vid){
		StringBuilder sql = new StringBuilder();
		sql.append(" update AbVoucherinfoVice");
		sql.append(" set validSign=2");
		sql.append(" where vid=:vid");
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("vid", vid);
		return this.batchExecute(sql.toString(), params);
	}
	/**
	 * 获取付款开票未用退票成功信息返回给结算系统
	 */
	public List<AbVoucherinfoDTO> queryBillBackSuccessInfo() {
		Map<String,Object> params=new HashMap<>(5);
		StringBuilder sql = new StringBuilder();
		sql.append(" select a.vvid as vvid,vv.voucher_id as payId,a.vvid as id,v.bill_money as billMoney,a.opt_status as optStatus ");
		sql.append(" from ab_accrecords a ");
		sql.append(" inner join ab_voucherinfo v on v.id=a.vid ");
		sql.append(" inner join ab_voucherinfo_vice vv on vv.id=a.vvid ");
		// sql.append(" inner join nis_billhead n on n.id=vv.voucher_id ");
		// sql.append(" inner join fc_voucher_type t on t.id=n.voucher_type_id  ");
		sql.append(" left join AB_BILL_MIDDLE m on m.bu_id=vv.id  ");
		// sql.append("where t.sys_voucher_code=:sysBackType and n.status=:status and  ");
		sql.append(" where a.brief_id=:briefId004 and m.id is null and a.opt_status in (:successOptStatus) and a.send_sign=:sendSign1 ");
		sql.append(" and vv.voucher_id is not null ");
		sql.append("order by a.opt_status");
		params.put("sysBackType", NetbankConstant.PAY_TYPE_49);
		params.put("status", NetbankConstant.STATUE_BILL_COMPLETE);
		params.put("briefId004", AccebillConstant.BRIEF_STATUS_004);
		params.put("sendSign1", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		params.put("successOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS19});
		return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
	}
}
