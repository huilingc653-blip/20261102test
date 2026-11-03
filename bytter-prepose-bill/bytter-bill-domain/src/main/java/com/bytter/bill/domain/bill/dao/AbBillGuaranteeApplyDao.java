package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillGuaranteeApplyVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.EmptyUtils;
import com.bytter.core.utils.SqlUtil;
import org.springframework.stereotype.Repository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Title: Dao
 * @Description: 提示保证处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Repository
public class AbBillGuaranteeApplyDao extends BaseDao<AbBillGuaranteeApplyVO, String> {
	/**
	 * 加载AbBillGuaranteeApply表数据
	 *
	 * @param dataTablesInput
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillGuaranteeApplyVO> listAbBillGuaranteeApply(DataTablesInput dataTablesInput,List<String> corpAuthList) {
		Map<String, Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(this.getQuerySql(corpAuthList,params));
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (bail.bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "bail.bankAcc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillGuaranteeApplyVO.class, "bail", false, params);
	}
	/**
	 * 获取查询列表界面的sql
	 * @param corpAuthList 操作用户授权的单位权限
	 * @return
	 */
	private String getQuerySql(List<String> corpAuthList,Map<String, Object> params){
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
		.append("z.bill_type as billType,z.bill_flag as billFlag,c.type,")
		.append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
		.append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
		.append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
		.append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
		.append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,")
		//票据保证类型
		.append("(case "
				+ "when l.bail_type is not null then l.bail_type "
				+ "when l.brief_id='"+AccebillConstant.BRIEF_STATUS_001+"' then '"+AccebillConstant.AB_BAIL_TYPE_ADD+"' "
				+ "when l.brief_id='"+AccebillConstant.BRIEF_STATUS_9002+"' then '"+AccebillConstant.AB_BAIL_TYPE_ACCEPT+"' "
				+ "else '"+AccebillConstant.AB_BAIL_TYPE_RECITE+"' end) as bailType,");
		//设置业务处理状态
		sql.append("(case c.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_017+"' then -1 else l.status end) as status,")
		.append("c.corp_id as corpId,s.code as corpCode,s.name as corpName,l.opt_status as optStatus,l.brief_id as briefId, ")
		//		--前手
		.append("case ")
		.append("when l.brief_id='6001' then l.cn_accrecords ")
		.append("when l.brief_id in ('001','002','9002','003','004') then ''")
		.append("when bs.bus_type in (:bus_type_0) then l.rec_nm ")
		.append("else c.cn_accrecords end as cnAccrecords,")
		.append(" c.APPLY_CORP_ID as applyCorpId,s2.name as applyCorpName,s2.code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
		//		--前手
		// 新一代票据 start
		.append(", z.new_stm_issuofppr_ind as newStmIssuofpprInd, z.blgrdbagcrcl_prmt_ind as blgrdbagcrclPrmtInd, z.sbbll_rng_strt_sn as sbbllRngStrtSn")
		.append(" , z.sbbll_rng_end_sn as sbbllRngEndSn, z.bl_std_amt as blStdAmt, z.bl_rmtr_unn_id_no as blRmtrUnnIdNo ,z.bill_money as transMoney")
        .append(" ,c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,c.BATCH_NO AS batchNo, c.BUS_CORP_NAME as busCorpName,c.bank_type_id as bankTypeId ")
		//新一代票据 end
		.append(" from AB_VOUCHERINFO_VICE c ")
		.append(" left join AB_VOUCHERINFO z on z.id=c.vid  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ")
		.append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
		.append(" left join ab_brief_status bs on bs.status=l.brief_id ")
		.append(" left join sys_corp s on c.corp_id = s.id ")
		.append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
		.append(" where c.valid_sign='"+AccebillConstant.AB_BILL_VALID_SIGN_1+"'  ")
		.append(" and l.SEND_SIGN='"+AccebillConstant.AB_BILL_SEND_SIGN_Y+"' ")
		.append(" and z.bill_flag ='"+AccebillConstant.AB_BILL_FLAG_ELECT+"' ")
		.append(" and ( ");
		//正常可以做保证业务处理
		sql.append(" ( ")
		.append(" l.STATUS='"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ")
		.append(" and l.valid_sign ='"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ")
		.append(" and l.OPT_STATUS ='"+AccebillConstant.AB_BILL_OPT_STATUS19+"' ");
		//开票保证
		sql.append(" and ( (l.brief_id ='"+AccebillConstant.BRIEF_STATUS_001+"' "
				+ "and c.type='"+AccebillConstant.AB_VOUCHERINFO_OP_TYPE0+"' "
				+ "and (z.bill_status='" +AccebillConstant.AB_BILL_STATUS_010004+"' or (z.bill_status = 'CS01' and z.EBILL_BL_CRCL_INDC = 'TF0101')) "
				+ "and c.box_status='" +AccebillConstant.AB_BOX_STATUS_Y+"') ");
		//承兑保证
		sql.append(" or (l.brief_id ='"+AccebillConstant.BRIEF_STATUS_9002+"' "
				+ "and c.type='"+AccebillConstant.AB_VOUCHERINFO_OP_TYPE1+"' "
				+ "and (z.bill_status='" +AccebillConstant.AB_BILL_STATUS_020006+"' or (z.bill_status = 'CS02' and z.EBILL_BL_CRCL_INDC = 'TF0101'))  "
				+ "and c.box_status='" +AccebillConstant.AB_BOX_STATUS_Y+"') ");
		//背书保证
		sql.append(" or (l.brief_id in ('"
				+AccebillConstant.BRIEF_STATUS_9003+"','"
				+AccebillConstant.BRIEF_STATUS_9010+"','"
				+AccebillConstant.BRIEF_STATUS_9012+"','"
				+AccebillConstant.BRIEF_STATUS_9019+"')"
				+ "and c.type='"+AccebillConstant.AB_VOUCHERINFO_OP_TYPE1+"' "
				+ "and c.box_status='" +AccebillConstant.AB_BOX_STATUS_Y+"' )")
		.append(" )) ");
		//提示保证业务审批中可以查看
		sql.append(" or (l.brief_id ='"+AccebillConstant.BRIEF_STATUS_017+"' "
				+ "and l.OPT_STATUS!="+AccebillConstant.AB_BILL_OPT_STATUS_2+" "
				+ "and l.status="+AccebillConstant.AB_BILL_BUS_STATUS11+" ) ")
		.append(" ) ");
		params.put("bus_type_0", new String[]{"9002", "9011", "9017", "9018", "9020", "9023"});
		String corpAuthSql = SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
		//用户满足对应查询权限的单位数据
		sql.append(" and ").append(corpAuthSql).append(" ")
		.append(") bail where 1=1 ");
		return sql.toString();
	}
	/**
	 * 加载审批列表数据
	 * @param dataTablesInput 列表入参数据
	 * @param businessList 业务数据ID
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
		Map<String, Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
		.append("z.bill_type as billType,z.bill_flag as billFlag,c.type,")
		.append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
		.append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
		.append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
		.append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,")
		.append("l.REQ_ROLE as reqRole,l.req_nm as reqNm,l.req_acct_id as reqAcctId,")
		.append("l.REC_ROLE as recRole,l.rec_nm as recNm,l.rec_acct_id as recAcctId,")
		.append("c.bank_acc as bankAcc,'0' as checkboxHide,")
		//票据保证类型
		.append("(case "
				+ "when l.bail_type is not null then l.bail_type "
				+ "when l.brief_id='"+AccebillConstant.BRIEF_STATUS_001+"' then '"+AccebillConstant.AB_BAIL_TYPE_ADD+"' "
				+ "when l.brief_id='"+AccebillConstant.BRIEF_STATUS_9002+"' then '"+AccebillConstant.AB_BAIL_TYPE_ACCEPT+"' "
				+ "else '"+AccebillConstant.AB_BAIL_TYPE_RECITE+"' end) as bailType,")
		//设置业务处理状态
		.append("(case c.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_017+"' then -1 else l.status end) as status,")
		.append("c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
		.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ")
		.append(" from AB_VOUCHERINFO_VICE c ")
		.append(" left join AB_VOUCHERINFO z on z.id=c.vid  and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ")
		.append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
		.append(" where 1=1 ");
		if(EmptyUtils.isNotEmpty(businessList)){
			String businessSql=SqlUtil.getInSqlByParams(businessList, "l.id", params);
			sql.append(" and ").append(businessSql).append(" ");
		}
		sql.append(") bail where 1=1  ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillGuaranteeApplyVO.class, "bail", false,params);
	}
	/**
	 * 根据参数获取票据可保证数据
	 * @param params 查询参数
	 * @param corpAuthList 授权单位
	 * @return 返回数据对象
	 */
	public AbBillGuaranteeApplyVO getAbBillGuaranteeApplyVOByParams(Map<String,Object> params,List<String> corpAuthList){
		if(params==null){
			return null;
		}
		StringBuilder sql = new StringBuilder(getQuerySql(corpAuthList,params));
		if(params.containsKey("origNo")){
			String origNoAndSbblNo = params.get("origNo").toString();
			String[] origNoAndSbblNoArr = origNoAndSbblNo.split("&");
			String origNo = origNoAndSbblNoArr[0];
			String sbbllRngStrtSn = origNoAndSbblNoArr[1];
			String sbbllRngEndSn = origNoAndSbblNoArr[2];
			params.put("origNo",origNo);
			params.put("sbbllRngStrtSn",sbbllRngStrtSn);
			params.put("sbbllRngEndSn",sbbllRngEndSn);
			sql.append(" and bail.origNo =:origNo ");
			sql.append(" and bail.sbbllRngStrtSn =:sbbllRngStrtSn ");
			sql.append(" and bail.sbbllRngEndSn =:sbbllRngEndSn ");
		}
		if(params.containsKey("corpId")){
			sql.append(" and bail.corpId =:corpId ");
		}
		AbBillGuaranteeApplyVO info=this.getBySQLAliasToBean(sql.toString(), params, AbBillGuaranteeApplyVO.class);
		return info;
	}
}
