package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbReceiveBillBackVo;
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
 * @Description: 应收退票
 * @author onlineGenerator
 * @date 2017-06-29 16:53:43
 * @version 1.0
 *
 */
@Repository
public class AbReceiveBillBackDao extends BaseDao<AbVoucherinfo, String> {
	/**
	 * 加载应收退票数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput listAbReceiveBillBack(DataTablesInput dataTablesInput, List<String> corpAuthList) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
		sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,c.CORP_ID as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName,"
						+ "(case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_6004
						+ "' then -1 else l.status end) as status,l.send_sign as sendSign,l.brief_id as briefId, ");
		sql.append(" (case l.Brief_Id when '" + AccebillConstant.BRIEF_STATUS_6004 + "' then 1 "
				+ " else 0 end) as checkboxHide,");
		sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel, ");
		//		--前手
		sql.append("case ");
		sql.append("when l.brief_id='6001' then l.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ");
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else c.cn_accrecords end as cnAccrecords,c.BUS_CORP_NAME as busCorpName ");
		//		--前手
		//新一代票据新增出票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、出票人统一信用代码、业务批次号
		sql.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN");
		sql.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo,c.BATCH_NO as batchNo ");
		sql.append(" from AB_VOUCHERINFO_VICE c ");
		sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
		sql.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
		sql.append(" left join ab_brief_status bs on bs.status=l.brief_id ");
		sql.append(" where c.valid_sign=:C_VALID_SIGN");
		params.put("C_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
		sql.append(" and ( ").append("( ")
				// 纸票可用业务 收票登记非质押来源和质押收回 以及电票第三方的票据类型 也可以退票
				//zhengg 20200602 针对离线电票也是支持退票申请功能（为满足录错数据进行删除功能）
				.append(" (z.bill_flag=:Z_BILL_FLAG or z.bill_type not in (:initBillTypes) or l.send_sign=:SEND_SIGN_N ) ")
				.append(" and l.valid_sign=:L_VALID_SIGN")
				.append(" and l.status=:L_STATUS95").append(" and c.box_status=:C_BOX_STATUS_Y")
				.append(" and l.brief_id in (:L_BRIEF_ID)").append(" and c.BILL_SOURCE <> :C_BILL_SOURCE ")
				.append(" ) or ( ")
				// 审批中数据
				.append(" l.brief_id = :L_BRIEF_ID6004 ").append(" and l.status in (:L_STATUS11)")
				.append(" and c.box_status=:C_BOX_STATUS_N)").append(" ) ");
		params.put("initBillTypes", new Integer[]{Integer.parseInt(AccebillConstant.AB_BILL_TYPE_BANK), Integer.parseInt(AccebillConstant.AB_BILL_TYPE_MERCH)});
		params.put("SEND_SIGN_N", AccebillConstant.AB_BILL_SEND_SIGN_N);
		params.put("Z_BILL_FLAG", AccebillConstant.AB_BILL_FLAG_PAPER);
		params.put("L_VALID_SIGN", AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("L_STATUS95", AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("C_BOX_STATUS_Y", AccebillConstant.AB_BOX_STATUS_Y);
		params.put("L_BRIEF_ID", new String[] { AccebillConstant.BRIEF_STATUS_6019, AccebillConstant.BRIEF_STATUS_6212,
				AccebillConstant.BRIEF_STATUS_6001 });
		params.put("C_BILL_SOURCE", AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("L_BRIEF_ID6004", AccebillConstant.BRIEF_STATUS_6004);
		Integer[] L_STATUS11={AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS94};
		params.put("L_STATUS11", L_STATUS11);
		params.put("C_BOX_STATUS_N", AccebillConstant.AB_BOX_STATUS_N);
		// 条件查询打回数据
		if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1)
				.equals(dataTablesInput.getFormdata().get("eqi_status"))) {
			sql.append(" and c.back_brief_id = :C_BACK_BRIEF_ID");
			params.put("C_BACK_BRIEF_ID", AccebillConstant.BRIEF_STATUS_6004);
			dataTablesInput.getFormdata().remove("eqi_status");
		}
		if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95)
				.equals(dataTablesInput.getFormdata().get("eqi_status"))) {
			sql.append(" and c.back_brief_id is null");
		}
		dataTablesInput.getFormdata().remove("eqs_back");
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		//设置是否过滤票据账号
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbReceiveBillBackVo.class, "", false, params);
	}
	/**
	 * 加载退票审批中数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput, List<String> businessList) {
		Map<String, Object> params = new HashMap<>();
		StringBuilder sql = new StringBuilder("select ");
		sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,"
				+ "z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
		sql.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,"
				+ "c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,l.brief_id as briefId ");
		sql.append(" from AB_VOUCHERINFO_VICE c ");
		sql.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ");
		sql.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
		sql.append(" where 1=1 ");
		String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbReceiveBillBackVo.class, "", false, params);
	}
}
