package com.bytter.bill.domain.bill.dao;

import cn.hutool.core.util.StrUtil;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbVoucherinfoDTO;
import com.bytter.bill.domain.bill.entity.AbVoucherinfo;
import com.bytter.bill.domain.bill.vo.AbBillEndorseVo;
import com.bytter.biz.bdlink.api.constant.NetbankConstant;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.ListUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * @Title: Dao
 * @Description: 票据背书
 * @author onlineGenerator
 * @date 2017-07-05 17:53:54
 * @version 1.0
 *
 */
@Repository
public class AbBillEndorseDao extends BaseDao<AbVoucherinfo, String> {
	private  String getQuerySQL(Map<String,Object> params){
		StringBuilder sql = new StringBuilder("select c.bank_Acc as bankAcc,c.type_name as bankTypeName,c.bank_Name as bankName,c.BANK_TYPE_ID as bankTypeId,");
		sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.bill_money as transMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
		sql.append(" z.bill_status as billStatus,c.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,c.corp_id as corpId,");
		sql.append(" c.CORP_CODE as corpCode,c.CORP_NAME as corpIdName, ");
		sql.append(" (case c.back_brief_id when '" + AccebillConstant.BRIEF_STATUS_010 + "' then -1 else l.status end) as status,l.brief_id as briefId, ");
		sql.append(" (case l.status when " + AccebillConstant.AB_BILL_BUS_STATUS11
				+ " then 1 when " + AccebillConstant.AB_BILL_BUS_STATUS94
				+ " then 1 else 0 end) as checkboxHide,l.send_sign as sendSign,");
		// 新一代票据列表查询新增字段 -- start
		sql.append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");
		sql.append(" z.BL_STD_AMT AS blStdAmt, z.BL_RMTR_UNN_ID_NO AS blRmtrUnnIdNo, ");
		sql.append(" z.BL_RCVPYMTPS_UNN_ID_NO as blRcvpymtpsUnnIdNo, z.BL_ACPTR_UNN_ID_NO as blAcptrUnnIdNo, ");
		sql.append(" c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo, ");
		// -- end
		sql.append("ba.num as attachmentNum,ba.attachment_id as attachmentId,");
		sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode," );
		sql.append(	"c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" case when c.CN_ACCRECORDS_NAME is not null then c.CN_ACCRECORDS_NAME else c.CN_ACCRECORDS end as cnAccrecords, c.bus_corp_name as busCorpName");
		sql.append(" from ab_voucherinfo_vice c");
		sql.append(" join ab_voucherinfo z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join ab_accrecords l on c.id=l.vvid");
		sql.append(" left join ab_bill_attachment ba on ba.acid in (l.id,l.lastid) and ba.brief_id='"+AccebillConstant.BRIEF_STATUS_010+"' ");
		
		sql.append(" where  BT_NVL_CHAR(l.BANKENDRSMTMK,z.BANENDRSMTMK)=:banendrsmtmk ");
		sql.append(" and c.valid_sign=:validSign");
		sql.append(" and c.ENTER_STOCK in (:enterStock) ");
		sql.append(" and ( ")
				//线上电票可以业务
				.append("(l.SEND_SIGN=:sendSign1 ")
				.append(" and l.valid_sign=:validSign1 ")
				.append(" and l.status=:status1 ")
				.append(" and l.opt_status=:optStatus ")
				.append(" and c.box_status=:boxStatus1 ")
				.append(" and z.bill_flag =:billFlag ")
				.append(" and l.brief_id in (:briefId1)")
				//.append(" and z.end_date>=:end_date ")
				.append(" ) or ( ")
				//纸票或者线下电票可用业务 收票登记非质押来源和质押收回
//				.append(" l.SEND_SIGN=:sendSign2 and ")
				.append(" l.valid_sign=:validSign2 ")
				.append(" and l.status=:status2 ")
				.append(" and l.opt_status=:optStatus ")
				.append(" and c.box_status=:boxStatus2 ")
				.append(" and l.brief_id in (:briefId2)")
				.append(" and c.BILL_SOURCE <>:billSource ")
				.append(" ) ");
		params.put("banendrsmtmk",AccebillConstant.AB_BANEM_Y);
		params.put("end_date", DateUtils.getDateYMD());
		params.put("validSign",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("enterStock",new String[]{AccebillConstant.AB_ENTER_STOCK_CORP,AccebillConstant.AB_ENTER_STOCK_NET});
		params.put("sendSign1",AccebillConstant.AB_BILL_SEND_SIGN_Y);
		params.put("status1",AccebillConstant.AB_BILL_BUS_STATUS95 );
		params.put("boxStatus1",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billFlag",AccebillConstant.AB_BILL_FLAG_ELECT);
		params.put("briefId1", new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019});
		params.put("sendSign2",AccebillConstant.AB_BILL_SEND_SIGN_N);
		params.put("boxStatus2",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("validSign2",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("briefId2",new String[]{AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6001,AccebillConstant.BRIEF_STATUS_6212});
		params.put("status2",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("billSource",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("optStatus",AccebillConstant.AB_BILL_OPT_STATUS19);
		return sql.toString();
	}
	/**
	 * 加载背书数据
	 *
	 * @param dataTablesInput 查询条件
	 * @param corpAuthList 查询权限特殊管理单位
	 * @return 返回列表数据
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillEndorseVo> listabBillEndorse(DataTablesInput dataTablesInput, List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(" or ( ")
				//审批中数据
				.append(" l.brief_id in (:briefId3) ")
				.append(" and l.status in(:status3) ")
				.append(" and c.box_status=:boxStatus3) ")
				.append(" ) ");
		Integer[] status3={AccebillConstant.AB_BILL_BUS_STATUS11,AccebillConstant.AB_BILL_BUS_STATUS94};
		params.put("briefId3",AccebillConstant.BRIEF_STATUS_010);
		params.put("status3",status3);
		params.put("boxStatus3",AccebillConstant.AB_BOX_STATUS_N);
		// 条件查询打回数据
		if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS_1)
				.equals(dataTablesInput.getFormdata().get("eqi_status"))) {
			sql.append(" and c.back_brief_id =:backBriefId ");
			dataTablesInput.getFormdata().remove("eqi_status");
			params.put("backBriefId",AccebillConstant.BRIEF_STATUS_010);
		}
		if (String.valueOf(AccebillConstant.AB_BILL_BUS_STATUS95)
				.equals(dataTablesInput.getFormdata().get("eqi_status"))) {
			sql.append(" and c.back_brief_id is null ");
		}
		if("1".equals(dataTablesInput.getFormdata().get("isSelectBill"))){
			String billAccids=dataTablesInput.getFormdata().get("billAccids")==null?null:String.valueOf(dataTablesInput.getFormdata().get("billAccids"));
			if(StrUtil.isBlank(billAccids)){
				sql.append(" and 1!=:noShow ");
				params.put("noShow",1);
			}else{
				sql.append(" and ").append(SqlUtil.getInSqlByParams(billAccids.split(","), "l.ID","billAccid", params));
			}
			dataTablesInput.getFormdata().remove("isSelectBill");
			dataTablesInput.getFormdata().remove("billAccids");
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "c.apply_corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (c.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "c.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillEndorseVo.class,"",false,params);
	}
	/**
	 * 导入根据单位名称、票据号码查询背书数据
	 * @param abBillEndorseVo 背书vo
	 * @param corpAuthList 查询权限特殊管理单位
	 * @return 返回列表数据
	 */
	public List<AbBillEndorseVo> excelListAbBillEndorse(AbBillEndorseVo abBillEndorseVo, List<String> corpAuthList) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(")");
		sql.append(" AND c.CORP_CODE=:corpCode AND Z.ORIG_NO=:origNo");
		params.put("origNo", abBillEndorseVo.getOrigNo());
		if ("0".equals(abBillEndorseVo.getSbbllRngStrtSn()) && abBillEndorseVo.getBillMoney()!=null) {
			sql.append(" and (z.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn or z.sbbll_Rng_Strt_Sn=' ' )");
			params.put("sbbllRngStrtSn", abBillEndorseVo.getSbbllRngStrtSn());
			sql.append(" and z.bill_money=:billMoney");
			params.put("billMoney", abBillEndorseVo.getBillMoney());
		}else {
			if (StrUtil.isNotEmpty(abBillEndorseVo.getSbbllRngStrtSn())) {
				sql.append(" and z.sbbll_Rng_Strt_Sn=:sbbllRngStrtSn");
				params.put("sbbllRngStrtSn", abBillEndorseVo.getSbbllRngStrtSn());
			}
			if (StrUtil.isNotEmpty(abBillEndorseVo.getSbbllRngEndSn())) {
				sql.append(" and z.sbbll_Rng_end_Sn=:sbbllRngEndSn");
				params.put("sbbllRngEndSn", abBillEndorseVo.getSbbllRngEndSn());
			}
		}
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "c.corp_id", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		params.put("corpCode",abBillEndorseVo.getCorpId());
		return this.listBySQLAliasToBean(sql.toString(),params, AbBillEndorseVo.class);
	}
	/**
	 * 根据主表id 从表id查询背书数据
	 * @param vid 主表id
	 * @param vvid 从表id
	 * @return 返回列表数据
	 */
	public List<AbBillEndorseVo> listAbBillEndorseVobyVidAndVvid(String vid,String vvid) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(")");
		sql.append(" and c.id = :vvid  ");
		sql.append(" and z.id= :vid  ");
		params.put("vid",vid);
		params.put("vvid",vvid);
		return this.listBySQLAliasToBean(sql.toString(),params, AbBillEndorseVo.class);
	}
	/**
	 * 加载审批列表数据
	 *
	 * @param dataTablesInput 查询条件
	 * @param businessList 审批中流水
	 * @return 返回列表数据
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillEndorseVo> dataTableApprove(DataTablesInput dataTablesInput,List<String> businessList) {
		Map<String,Object> param=new HashMap<>();
		StringBuilder sql = new StringBuilder("select c.type_name as bankTypeName,c.bank_type_id as bankTypeId,");
		sql.append(" z.id as vid,c.id as vvid,l.ID as id,z.bill_type as billType,"
				+ "z.bill_flag as billFlag,z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,");
		sql.append(" z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,z.payee as payee,");
		sql.append(" z.bill_status as billStatus,l.inside_sing as insideSing,c.ENTER_STOCK AS enterStock,l.bankendrsmtmk,"
				+ "c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpIdName,l.brief_id as briefId,"
				+ "l.rec_nm as recNm,l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,");
		sql.append(" z.NEW_STM_ISSUOFPPR_IND AS newStmIssuofpprInd, z.BLGRDBAGCRCL_PRMT_IND AS blgrdbagcrclPrmtInd, ");
		sql.append(" z.SBBLL_RNG_STRT_SN AS sbbllRngStrtSn, z.SBBLL_RNG_END_SN AS sbbllRngEndSn, ");

		sql.append("(case  when l.create_by is not null then BT_CONCAT4('(',l.create_by,')',l.create_name) else null end) as applyName,");
		sql.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ");
		sql.append(" from ab_voucherinfo_vice c");
		sql.append(" left join ab_voucherinfo z on z.id=c.vid");
		sql.append(" left join ab_accrecords l on c.id=l.vvid");
		sql.append(" where 1=1 ");
		String applyName = dataTablesInput.getFormdata().get("applyName").toString();
		if(StringUtils.isNotEmpty(applyName)){
			sql.append(" and BT_CONCAT4('(',l.create_by,')',l.create_name) like BT_CONCAT3('%',:applyName,'%')");
			param.put("applyName", applyName);
		}
		dataTablesInput.getFormdata().remove("applyName");
		String businessSql = SqlUtil.getInSqlByParams(businessList, "l.id", param);
		sql.append(" and ").append(businessSql).append(" ");
		DataTablesOutput dataOutInfo=this.getDynamicBySQL(dataTablesInput, sql.toString(), AbBillEndorseVo.class, "", false,param);
		return dataOutInfo;
	}
	
	
	/**
	 * 校验重复背书方法
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<String> checkRepeatEndorse(Map<String,Object> params){
		StringBuilder sql = new StringBuilder();
		sql.append(" select t.orig_no as origNo");
		sql.append(" from AB_BACKBILLINFO t");
		sql.append(" where 1=1");
		sql.append(" and t.Bustype in (003, 010)");
		sql.append(" and t.orig_no =:origNos");
		sql.append("  and ( t.req_nm =:reqNm or t.rec_nm=:recNm)");
		List<String> strList = this.listBySQL(sql.toString(), params);
		return ListUtils.isNotNullOrEmpty(strList)?strList:null;
	}
	
	public String getBankTypeId(Map<String,Object> params,Integer interiorApply){
		StringBuilder sql = new StringBuilder();
		if(interiorApply==1){
			 sql.append(" select  bt.id")
				.append(" from bt_input_bank_info bi")
				.append(" left join bt_bank_type bt")
				.append(" on bt.bank_prefix = bi.bank_prefix")
				.append(" and (bt.bank_name_prefix is null or")
				.append(" bi.bank_name like bt_concat2(bt.bank_name_prefix, '%'))")
				.append(" where bt.id is not null")
				.append(" and bi.sys_bank_code =:bankCode");
		}else{
			 sql.append(" select banktype.id from  bt_bank_acc acc")
				.append(" left join bt_bank_type banktype on acc.bank_type_id = banktype.id")
				.append(" where acc.bank_acc =:pledgeeAcc");
		}
		@SuppressWarnings("unchecked")
		List<String> bankTypeIds =this.getSession().createSQLQuery(sql.toString()).setProperties(params).list();
		return ListUtils.isNullOrEmpty(bankTypeIds)?null:bankTypeIds.get(0);
	}
	
	
	/**
	 * 导入根据单位名称、票据号码查询背书数据
	 */
	public List<AbBillEndorseVo> checkRepeatEndorseQuery(String orgins) {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder(getQuerySQL(params));
		sql.append(")");
		String[] idArray = orgins.split(",");
		params.put("origNos", Arrays.asList(idArray));
		sql.append(" and Z.Orig_No in (:origNos)");
		return this.listBySQLAliasToBean(sql.toString(),params, AbBillEndorseVo.class);
	}
	/**
	 * 获取付款背书失败信息返回给结算系统
	 */
	public List<AbVoucherinfoDTO> queryBillEndorsePayErrorInfo() {
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("select a.vvid as vvid,b.pay_id as payId,b.bill_id as id,v.bill_money as billMoney,a.opt_status as optStatus from nis_bill b ");
		sql.append("inner join ab_accrecords a on a.id=b.bill_id  ");
		sql.append("inner join ab_voucherinfo v on v.id=a.vid ");
		sql.append("inner join nis_billhead n on b.pay_id=n.id ");
		sql.append("inner join fc_voucher_type t on t.id=n.voucher_type_id ");
		sql.append("where t.sys_voucher_code=:sysEndorsePayType and n.status=:status and b.pay_id in ( ");
		sql.append("select b.pay_id from nis_bill b  ");
		sql.append("inner join ab_accrecords a on a.id=b.bill_id  ");
		sql.append("left join AB_BILL_MIDDLE m on b.bill_id=m.bu_id  ");
		sql.append("where a.brief_id=:briefIdEndorse and m.id is null and a.opt_status in (:errorOptStatus)) ");
		sql.append("order by b.pay_id,a.opt_status");
		params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
		params.put("status", NetbankConstant.STATUE_CHECK);
		params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
		params.put("errorOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS_2,AccebillConstant.AB_BILL_OPT_STATUS_3,AccebillConstant.AB_BILL_OPT_STATUS_4});
		return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
	}
	/**
	 * 获取付款背书信息返回给结算系统。
	 * @param listId 流水ID。
	 * @return 背书信息。
	 */
	public List<AbVoucherinfoDTO> queryBillEndorsePayInfo(List<String> listId) {
		String sql = "select a.vvid as vvid,a.billhead_id as payId,a.id as id,v.bill_money as billMoney,a.opt_status as optStatus " +
				" from ab_accrecords a LEFT JOIN ab_voucherinfo v on v.id=a.vid " +
				" where a.id in(:listId)";
		Map<String, Object> params = new HashMap<>();
		params.put("listId", listId);
		return this.listBySQLAliasToBean(sql, params, AbVoucherinfoDTO.class);
	}
	/**
	 * 获取付款背书状态信息获取对应流水ID。
	 * @param success 背书状态。
	 * @return 流水ID集合。
	 */
	@SuppressWarnings("unchecked")
	public List<String> queryBillIdByStatus(boolean success){
		Map<String,Object> params=new HashMap<>();
		params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
		if (success){
			params.put("optStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS19});
		}else {
			params.put("optStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS_2,AccebillConstant.AB_BILL_OPT_STATUS_3,AccebillConstant.AB_BILL_OPT_STATUS_4});
		}
		String sql = "select a.id from ab_accrecords a left join AB_BILL_MIDDLE m on a.id=m.bu_id " +
				" where a.brief_id=:briefIdEndorse and m.id is null and a.opt_status in (:optStatus)";
		return this.getSession().createSQLQuery(sql).setProperties(params).list();
	}
//
//	/**
//	 * 获取付款背书成功信息返回给结算系统
	//todo 黑的  目前没人调用
//	 */
//	public List<AbVoucherinfoDTO> queryBillEndorsePaySuccessInfo() {
//		Map<String,Object> params=new HashMap<>();
//		StringBuilder sql = new StringBuilder();
//		sql.append("select a.vvid as vvid,b.pay_id as payId,b.bill_id as id,v.bill_money as billMoney,a.opt_status as optStatus from nis_bill b ");
//		sql.append("inner join ab_accrecords a on a.id=b.bill_id  ");
//		sql.append("inner join ab_voucherinfo v on v.id=a.vid ");
//		sql.append("inner join nis_billhead n on b.pay_id=n.id ");
//		sql.append("inner join fc_voucher_type t on t.id=n.voucher_type_id ");
//		sql.append("where t.sys_voucher_code=:sysEndorsePayType and n.status=:status and b.pay_id in ( ");
//		sql.append("select b.pay_id from nis_bill b  ");
//		sql.append("inner join ab_accrecords a on a.id=b.bill_id  ");
//		sql.append("left join AB_BILL_MIDDLE m on b.bill_id=m.bu_id  ");
//		sql.append("where a.brief_id=:briefIdEndorse and m.id is null and a.opt_status in (:successOptStatus)) ");
//		sql.append("order by b.pay_id,a.opt_status");
//		params.put("sysEndorsePayType", NetbankConstant.PAY_TYPE_50);
//		params.put("status", NetbankConstant.STATUE_CHECK);
//		params.put("briefIdEndorse", AccebillConstant.BRIEF_STATUS_010);
//		params.put("successOptStatus", new String[]{AccebillConstant.AB_BILL_OPT_STATUS19});
//		return this.listBySQLAliasToBean(sql.toString(),params, AbVoucherinfoDTO.class);
//	}

	/**
	 * @Description 查询线上账户对内背书流水id
	 * @author zhous
	 * @date 2023/10/7 11:24
	 * @param startDate 背书申请开始时间
	 */
	public List<String> queryInsideEndorseOnlineToOfflineAccrecordId(Date startDate){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT AA.ID ");
		sql.append("FROM AB_VOUCHERINFO_VICE AVV ");
		sql.append("LEFT JOIN AB_VOUCHERINFO AV ON AVV.VID = AV.ID ");
		sql.append("LEFT JOIN AB_ACCRECORDS AA ON AVV.ID = AA.VVID ");
		sql.append("LEFT JOIN BT_BANK_ACC ACC ON AA.REC_ACCT_ID = ACC.BANK_ACC ");
		sql.append("WHERE AA.BRIEF_ID =:BRIEF_ID AND AA.SEND_SIGN =:SEND_SIGN AND AA.INSIDE_SING =:INSIDE_SING ");
		sql.append("AND AA.APPLY_DATE >=:START_DATE AND AA.INSIDE_ENDORSE_AUTO_REG_SIGN IS NULL ");
		sql.append("AND AA.STATUS =:STATUS AND AA.OPT_STATUS =:OPT_STATUS ");
		sql.append("AND ACC.VALID_SIGN = '1' AND ACC.STATUS = 95 AND ACC.ELECTRIC_BILL = '1' AND  ACC.CIRCULATION_MODE ='0' ");
		params.put("BRIEF_ID", AccebillConstant.AB_BUSTYPE_RECITE);
		params.put("SEND_SIGN", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		params.put("INSIDE_SING", AccebillConstant.AB_INSIDE_SING_Y);
		params.put("STATUS", AccebillConstant.ABLOG_LOGTYPE_APP);
		params.put("OPT_STATUS", AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("START_DATE", startDate);
		return createSQLQuery(sql.toString(), params).list();
	}
}
