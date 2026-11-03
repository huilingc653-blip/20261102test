package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillTransferUpApplyVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.sql.Date;
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
public class AbBillTransferUpApplyDao extends BaseDao<AbBillTransferUpApplyVo,String>{
	/**
	 * 票据上划列表查询
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input, List<String> corpAuthList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus, ");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode");
		sql.append(",b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel,b.BUS_CORP_NAME as busCorpName, ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_6201+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.STATUS end) as status, ");
		//		--前手
		sql.append("case ");
		sql.append("when c.brief_id='6001' then c.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then c.rec_nm ");
		Map<String,Object> params = new HashMap<> ();
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else b.cn_accrecords end as cnAccrecords, a.BILL_MONEY as transMoney,  ");
		sql.append(" a.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, a.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" a.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, a.SBBLL_RNG_END_SN AS sbbllRngEndSn  ");
		//		--前手
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");

		sql.append(" left join ab_brief_status bs on bs.status=c.brief_id ");
		//只有单位库存可以上划
		sql.append(" WHERE b.valid_sign = :validSign1 and b.enter_stock = :enterStock10 ");
		sql.append(" and a.banendrsmtmk = :banendrsmtmkEM00 and c.opt_status = :optStatus19 ");
		//没有上级网点不能上划
		sql.append(" and b.net_id is not null and b.corp_id<>b.net_id ");
		//审批状态查询
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign0 and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id = :briefId6201 and c.status = :status11 ");
			input.getFormdata().remove("eqs_statusName");
		//待记账状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS94).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign0 and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id = :briefId6201 and c.status = :status94 ");
			input.getFormdata().remove("eqs_statusName");
		//可上划状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS95).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign1 and b.box_status = :boxStatusY ");
			//离线电票也可以划拨
			sql.append(" and ((a.bill_flag = :billFlag11 and a.end_date >= :sysdate ");
			sql.append(" and  c.brief_id in (:briefIds)) ");
			sql.append(" or (c.SEND_SIGN = :sendSign and ((c.brief_id = :briefId6001 ");
			sql.append(" and b.BILL_SOURCE <> :billSource12) or c.brief_id in (:billSources)))) ");
			sql.append(" and c.status = :status95 ");
			input.getFormdata().remove("eqs_statusName");
		//默认查询
		}else{
			sql.append(" and ( ( ");
			sql.append(" c.valid_sign = :validSign1 and b.box_status = :boxStatusY ");
			//离线电票也可以划拨
			sql.append(" and ((a.bill_flag = :billFlag11 and a.end_date >= :sysdate ");
			sql.append(" and  c.brief_id in (:briefIds)) ");
			sql.append(" or (c.SEND_SIGN = :sendSign and ((c.brief_id = :briefId6001 ");
			sql.append(" and b.BILL_SOURCE <> :billSource12) or c.brief_id in (:billSources)))) ");
			sql.append(" and c.status = :status95 ");
			sql.append(" ) or ( ");
			sql.append(" c.valid_sign = :validSign0 and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id = :briefId6201 and (c.status = :status11 or c.status = :status94) )) ");
			//打回状态查询
			if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))) {
				sql.append (" and c.valid_sign = :validSign1 and b.box_status = :boxStatusY ");
				sql.append (" and b.back_brief_id = :briefId6201 and c.status = :status95 ");
				input.getFormdata ().remove ("eqs_statusName");
			}
		}
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("enterStock10",AccebillConstant.AB_ENTER_STOCK_CORP);
		params.put("banendrsmtmkEM00",AccebillConstant.AB_BANEM_Y);
		params.put("optStatus19",AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("status94",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("validSign0",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("boxStatusN",AccebillConstant.AB_BOX_STATUS_N);
		params.put("briefId6201",AccebillConstant.BRIEF_STATUS_6201);
		params.put("boxStatusY",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billFlag11",AccebillConstant.AB_BILL_FLAG_ELECT);
		params.put("briefIds",new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019});
		params.put("sendSign",AccebillConstant.AB_BILL_SEND_SIGN_N);
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("billSource12",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("billSources",new String[]{AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6212});
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("null",' ');
		params.put("sysdate",new Date(System.currentTimeMillis()));
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(input, sql.toString(), AbBillTransferUpApplyVo.class, "", false,params);
	}
	/**
	 * 审批页面查询当前操作员可审批的票据上划数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillTransferUpApplyVo> dataTableApprove(DataTablesInput input,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,c.id as businessKey,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.corp_code as corpCode,b.corp_name as corpName,");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_CORP_NAME as busCorpName,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus,c.status as status ");
		sql.append("from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbBillTransferUpApplyVo.class, "", false,params);
	}
}
