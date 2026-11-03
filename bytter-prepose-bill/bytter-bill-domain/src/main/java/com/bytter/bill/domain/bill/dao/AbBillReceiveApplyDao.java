package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillReceiveApplyVO;
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
 * @Description: 提示收票处理
 * @author onlineGenerator
 * @date 2017-06-21 17:35:25
 * @version 1.0
 *
 */
@Repository
public class AbBillReceiveApplyDao extends BaseDao<AbBillReceiveApplyVO, String> {
	/**
	 * 获取查询的sql语句
	 * @param corpAuthSql
	 * @param notInBifCodeSql
	 * @param isApp 是否包含审批中数据
	 * @param isAutomat 是否自动业务查询
	 * @return
	 */
	private String getQuerySql(String corpAuthSql,String notInBifCodeSql,boolean isApp,boolean isAutomat){
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
		.append("z.bill_type as billType,z.bill_flag as billFlag,l.rmk as rmk,")
		.append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,c.type,")
		.append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
		.append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
		.append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.business_date as businessDate,l.req_nm as reqNm,l.rec_nm as recNm,")
		.append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,")
		.append("z.create_By as createBy, ")
		//设置业务处理状态
		.append("(case c.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_003+"' then -1 else l.status end) as status,")
		.append("c.corp_id as corpId,c.CORP_CODE as corpCode,c.CORP_NAME as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
		.append(" c.APPLY_CORP_ID as applyCorpId,c.APPLY_CORP_NAME as applyCorpName,c.APPLY_CORP_CODE as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_PERSONNEL as busPersonnel ")
		//新一代票据新增出票标志、是否允许分包、子票区间开始序号、子票区间结束序号、标准金额、业务批次号、票据单位统一识别号码
		.append(", z.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd,z.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd,z.SBBLL_RNG_STRT_SN as sbBllRngStrtSN")
		.append(", z.SBBLL_RNG_END_SN as sbBllRngEndSN,z.BL_STD_AMT as blStdAmt,c.BATCH_NO as batchNo,c.BL_HOLD_UNN_ID_NO as blHoldUnnIdNo,z.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo ")
		.append(", c.BUS_CORP_NAME as busCorpName,c.bank_type_id as bankTypeId ")
		.append(" from AB_VOUCHERINFO_VICE c ")
		.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
		.append(" left join AB_ACCRECORDS l on c.id=l.vvid ");
		if(isAutomat){//自动业务只发起一次
			sql.append(" left join AB_BILL_MIDDLE m on l.ID=m.bu_id and m.type='").append(AccebillConstant.BILL_MIDDLE_TYPE_003).append("' ");
			sql.append(" left join AB_ACCRECORDS ae on ae.vid=z.id and ae.opt_status='19' and ae.brief_id='017' ");
		}
		sql.append(" where c.valid_sign='"+AccebillConstant.AB_BILL_VALID_SIGN_1+"'  ")
		.append(" and c.type='"+AccebillConstant.AB_VOUCHERINFO_OP_TYPE0+"' ")
		.append(" and l.SEND_SIGN='"+AccebillConstant.AB_BILL_SEND_SIGN_Y+"' ")
		.append(" and z.bill_flag ='"+AccebillConstant.AB_BILL_FLAG_ELECT+"' ")
		.append(" and ( ")
		//正常可以做收票业务处理
		.append(" ( ")
		.append(" l.STATUS='"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ")
		.append(" and l.valid_sign ='"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ")
		.append(" and l.OPT_STATUS ='"+AccebillConstant.AB_BILL_OPT_STATUS19+"' ")
		//提示承兑完成业务
		.append(" and l.brief_id ='"+AccebillConstant.BRIEF_STATUS_002+"' "
//				+ "and z.bill_status='" +AccebillConstant.AB_BILL_STATUS_020006+"' "
				+ "and c.box_status='" +AccebillConstant.AB_BOX_STATUS_Y+"' ")
		.append(" ) ");
		if(isApp){
			//提示收票业务审批中可以查看
			sql.append(" or (l.brief_id ='"+AccebillConstant.BRIEF_STATUS_003+"' "
					+ "and l.OPT_STATUS!="+AccebillConstant.AB_BILL_OPT_STATUS_2+" "
					+ "and l.status="+AccebillConstant.AB_BILL_BUS_STATUS11+" ) ");
		}
		sql.append(" ) ");
		if(StringUtils.isNotEmpty(corpAuthSql)){
			//用户满足对应查询权限的单位数据
			sql.append(" and ").append(corpAuthSql).append(" ");
		}
		if(StringUtils.isNotEmpty(notInBifCodeSql)){
			//排除不需要做提示收票的银行接口数据
			sql.append(" and ").append(notInBifCodeSql).append(" ");
		}
		if(isAutomat){//自动业务只发起一次
			sql.append(" and m.id IS NULL ");
			sql.append(" and (c.is_Ensure is null or c.is_Ensure='0' or (c.is_Ensure='1' and ae.id is not null )) ");
		}
		sql.append(") bail where 1=1 ");
		return sql.toString();
	}
	/**
	 * 获取查询的sql语句
	 * @param corpAuthSql
	 * @param notInBifCodeSql
	 * @param isApp 是否包含审批中数据
	 * @return
	 */
	private String getQuerySql(String corpAuthSql,String notInBifCodeSql,boolean isApp){
		return getQuerySql(corpAuthSql,notInBifCodeSql,isApp,false);
	}
	
	/**
	 * 加载AbBillReceiveApply表数据
	 * @param dataTablesInput
	 * @param corpAuthSql
	 * @param notInBifCodeSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput<AbBillReceiveApplyVO> listAbBillReceiveApply(DataTablesInput dataTablesInput,String corpAuthSql, String notInBifCodeSql) {
		StringBuilder sql = new StringBuilder(getQuerySql(corpAuthSql, notInBifCodeSql,true));
		Map<String,Object> params=new HashMap<>();
		if(dataTablesInput.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = dataTablesInput.getFormdata().get("accountGrantList") == null ? null : (List<String>) dataTablesInput.getFormdata().get("accountGrantList");
			sql.append(" and (bail.bankAcc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "bail.bankAcc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillReceiveApplyVO.class, "bail", false, params);
	}
	/**
	 * 加载可以做自动提示的数据
	 * @param notInBifCodeSql 不可以可以做提示承兑银行接口sql
	 * @return 返回符合条件的数据
	 */
	public List<AbBillReceiveApplyVO> listAutoAbBillReceiveApplyInfo(String notInBifCodeSql,String arguments) {
		StringBuilder sql = new StringBuilder(getQuerySql(null, notInBifCodeSql,false,true));
		Map<String,Object> params=new HashMap<>();
		//提示收票延迟推送时间设置（部分银行不支持承兑完成马上发起提示收票要隔几分钟默认3分钟）
		if(StringUtils.isNotEmpty(arguments)){
			try {
				int minute=Integer.parseInt(arguments);
				Date applyDateEnd= DateUtils.addMinute(new Date(),-1*minute);
				sql.append(" and bail.applyDate <=:applyDateEnd ");
				params.put("applyDateEnd",applyDateEnd);
			}catch (Exception e){
				this.logger.error("自动提示收票发起时间转换问题，不影响功能使用！",e);
			}
		}
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillReceiveApplyVO.class);
	}
	/**
	 * 加载审批列表数据
	 * 
	 * @param dataTablesInput
	 * @param businessSql
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput dataTablesInput,String businessSql) {
		StringBuilder sql = new StringBuilder("select * from ( ");
		sql.append("select z.id as vid,c.id as vvid,l.ID as id,l.recid as recid,")
		.append("z.bill_type as billType,z.bill_flag as billFlag,l.rmk as rmk,")
		.append("z.payer as payer,z.acceptor as acceptor,z.orig_no as origNo,")
		.append("z.bill_money as billMoney,z.start_date as startDate,z.end_date as endDate,")
		.append("z.payee as payee,z.bill_status as billStatus,c.inside_sing as insideSing,")
		.append("l.SIGN_UP_FLAG as signUpFlag,l.apply_date as applyDate,l.req_nm as reqNm,l.rec_nm as recNm,")
		.append("l.rec_acct_id as recAcctId,c.bank_acc as bankAcc,'0' as checkboxHide,")
		//设置业务处理状态
		.append("(case c.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_003+"' then -1 else l.status end) as status,")
		.append("c.corp_id as corpId,c.corp_code as corpCode,c.corp_name as corpName,l.opt_status as optStatus,l.brief_id as briefId,")
		.append(" c.APPLY_CORP_ID as applyCorpId,c.apply_corp_name as applyCorpName,c.apply_corp_code as applyCorpCode,c.BUS_CORP_ID as busCorpId,c.BUS_CORP_NAME as busCorpName,c.BUS_PERSONNEL as busPersonnel ")
		.append(" from AB_VOUCHERINFO_VICE c ")
		.append(" left join AB_VOUCHERINFO z on z.id=c.vid and (z.OLD_DRAFT_ID is null or z.OLD_DRAFT_ID not like '%-del')  ")
		.append(" left join AB_ACCRECORDS l on c.id=l.vvid ")
		// .append(" left join sys_corp s on c.corp_id = s.id ")
		// .append(" left join sys_corp s2 on c.apply_corp_id = s2.id ")
		.append(" where 1=1 ")
		.append(" and ").append(businessSql).append(" ")
		.append(") bail ");
		return getDynamicBySQL(dataTablesInput, sql.toString(), AbBillReceiveApplyVO.class, "bail", true);
	}
	
}
