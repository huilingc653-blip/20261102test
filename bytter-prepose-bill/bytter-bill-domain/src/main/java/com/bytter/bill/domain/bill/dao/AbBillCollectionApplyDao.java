package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.api.bill.dto.AbBillCollectionApplyVoDTO;
import com.bytter.bill.domain.bill.vo.AbBillCollectionApplyVo;
import com.bytter.core.base.BaseDao;
import com.bytter.core.common.datatable.DataTablesInput;
import com.bytter.core.common.datatable.DataTablesOutput;
import com.bytter.core.utils.DateUtils;
import com.bytter.core.utils.SqlUtil;
import com.bytter.core.utils.StringUtils;
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
public class AbBillCollectionApplyDao extends BaseDao<AbBillCollectionApplyVo,String>{
	/**
	 * 托收申请列表查询
	 * @param input
	 * @param day10 银票的到期付款时间，用于计算银票是否逾期
	 * @param day11 商票的到期付款时间，用于计算商票是否逾期
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput getDataTablesUnion(DataTablesInput input,String day10,String day11, List<String> corpAuthList, String notInBifCodeSql) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,c.id as id, b.box_status as boxStatus,b.BANK_ACC as bankAcc , ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_020+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" when '");
		sql.append( AccebillConstant.BRIEF_STATUS_021+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append("c.sttlmmk as sttlmmk,c.rmk as rmk, ");
		sql.append(" c.send_sign as sendSign,");
		sql.append(" c.rsn as rsn, ");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
		sql.append(" ba.num as attachmentNum,ba.attachment_id as attachmentId, b.BUS_CORP_NAME as busCorpName, ");
		//		--前手
		sql.append("case ");
		sql.append("when c.brief_id='6001' then c.cn_accrecords ");
		sql.append("when bs.bus_type in (:bus_type_0) then c.rec_nm ");
		Map<String,Object> params = new HashMap<> ();
		params.put("bus_type_0", new String[]{"6001", "9002", "9011", "9017", "9018", "9020", "9023"});
		sql.append("else b.cn_accrecords end as cnAccrecords ");
		//		--前手
		//新一代票据
		sql.append(" , a.NEW_STM_ISSUOFPPR_IND as newStmIssuofpprInd, a.BLGRDBAGCRCL_PRMT_IND as blgrdbagcrclPrmtInd, a.SBBLL_RNG_STRT_SN as sbbllRngStrtSn ,");
		sql.append(" a.SBBLL_RNG_END_SN as sbbllRngEndSn , a.BL_STD_AMT as blStdAmt , a.BL_RMTR_UNN_ID_NO as blRmtrUnnIdNo , a.BILL_MONEY as transMoney");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_discount d on d.record_id = c.id ");
		sql.append(" LEFT JOIN ab_bill_attachment ba on ba.acid in (c.id,c.lastid) and ba.brief_id in ('"+AccebillConstant.BRIEF_STATUS_020+"','"+AccebillConstant.BRIEF_STATUS_021+"') ");
		sql.append(" left join ab_brief_status bs on bs.status=c.brief_id ");
		sql.append(" WHERE b.valid_sign = :validSign1 ");
		sql.append(" and opt_status = (case when a.bill_flag = '11' and c.status = '11' then '16' else '19' end) ");//电票审批中时，opt_status为16
		sql.append(" and a.start_date <= :sysdate ");
		if(StringUtils.isNotEmpty(notInBifCodeSql)){
			//排除不需要做提示付款的银行接口数据
			sql.append(" and ").append(notInBifCodeSql).append(" ");
		}
		//审批状态查询
		if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS11).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign0 and c.status = :status11 and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id in (:briefIds) ");
			input.getFormdata().remove("eqs_statusName");
		//待记账状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS94).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign0 and c.status = :status94 and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id in (:briefIds) ");
			input.getFormdata().remove("eqs_statusName");
		//可申请状态查询
		}else if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS95).equals(input.getFormdata().get("eqs_statusName"))){
			sql.append(" and c.valid_sign = :validSign1 and c.status = :status95 and b.box_status = :boxStatusY ");
            //电票：提示收票签收、背书申请签收、贴现赎回签收、质押解除签收
			sql.append ("and ((a.bill_flag = :billFlag11 and c.brief_id in (:briefIds2) ) ");
			sql.append (" or ");
            //纸票:收票登记来源为质押，票据已到期的票据
			sql.append (" (c.brief_id = :briefId6001 and b.BILL_SOURCE = :billSource12 ");
			sql.append ("  and a.end_date< :sysdate )  ");
			sql.append (" or ");
            //电票: 质押签收到期的票据
            sql.append (" (c.brief_id = :briefId9018 and a.end_date < :sysdate ) ");
			sql.append (" or ");
			//收票登记不为贴现和质押的票据
			sql.append (" (c.brief_id = '6001' and b.bill_source not in ( :billSource12 ,:billSource14) ) ");
			sql.append (" or ");
			//纸票: 质押收回、贴现收回、收票登记中来源不为质押和贴现的票据
			sql.append (" ( (c.brief_id = :briefId6001 and b.BILL_SOURCE <> :billSource12 ");
			sql.append ("  and b.bill_source <> :billSource14) or (c.brief_id in (:briefIds3)) ) ");
			sql.append (" or ");
			//纸票:收票登记中来源为买断式贴现的票据
			sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 and d.rpdmk = :rpdmkRM00) ");
            sql.append (" or ");
            //纸票:收票登记中来源为回购式贴现的票据且已超过赎回截止日
            sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 ");
            sql.append ("  and d.rpdmk = :rpdmkRM01 and d.rpdduedt < :sysdate ) ) ");
			input.getFormdata().remove("eqs_statusName");
		//默认查询
		}else{
			sql.append(" and ( ( ");
			sql.append(" c.valid_sign = :validSign1 and c.status = :status95 and b.box_status = :boxStatusY ");
			//电票：提示收票签收、背书申请签收、贴现赎回签收、质押解除签收
			sql.append ("and ((a.bill_flag = :billFlag11 and c.brief_id in (:briefIds2) ) ");
			sql.append (" or ");
			//纸票: 质押收回、贴现收回
			sql.append (" c.brief_id in (:briefIds3) ");
			sql.append (" or ");
			//电票: 质押签收到期的票据
			sql.append (" (c.brief_id = :briefId9018 and a.end_date < :sysdate ) ");
			sql.append (" or ");
			//收票登记不为贴现和质押的票据
			sql.append (" (c.brief_id = '6001' and b.bill_source not in ( :billSource12,:billSource14) ) ");
			sql.append (" or ");
			//纸票:收票登记来源为质押，票据已到期的票据
			sql.append (" (c.brief_id = :briefId6001 and b.BILL_SOURCE = :billSource12 ");
			sql.append ("  and a.end_date< :sysdate )  ");
			sql.append (" or ");
			//纸票:收票登记中来源为买断式贴现的票据
			sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 and d.rpdmk = :rpdmkRM00) ");
			sql.append (" or ");
			//纸票:收票登记中来源为回购式贴现的票据且已超过赎回截止日
			sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 ");
			sql.append ("  and d.rpdmk = :rpdmkRM01 and d.rpdduedt < :sysdate ) ) ");
			sql.append(" ) or ( ");
			sql.append(" c.valid_sign = :validSign0 and (c.status = :status11 or c.status = :status94 ) and b.box_status = :boxStatusN ");
			sql.append(" and c.brief_id in (:briefIds) )) ");
			//打回状态查询
			if(String.valueOf (AccebillConstant.AB_BILL_BUS_STATUS_1).equals(input.getFormdata().get("eqs_statusName"))) {
				sql.append (" and c.valid_sign = :validSign1 and c.status = :status95 " +
						"and b.box_status = :boxStatusY ");
				sql.append (" and ( b.back_brief_id in (:briefIds))");
				input.getFormdata ().remove ("eqs_statusName");
			}
		}
		//单位权限过滤
		//sql.append(" and ").append(corpAuthSql).append(" ");
		params.put("validSign0",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("status94",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("boxStatusN",AccebillConstant.AB_BOX_STATUS_N);
		params.put("briefIds",new String[]{AccebillConstant.BRIEF_STATUS_020,AccebillConstant.BRIEF_STATUS_021});
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("boxStatusY",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billFlag11",AccebillConstant.AB_BILL_FLAG_ELECT);
		params.put("briefIds2",new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019
		});
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("billSource12",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("briefId9018",AccebillConstant.BRIEF_STATUS_9018);
		params.put("billSource14",AccebillConstant.AB_BILL_SOURCE_DISCOUNT);
		params.put("briefIds3",new String[]{AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6212});
		params.put("rpdmkRM00",AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT);
		params.put("rpdmkRM01",AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK);
		params.put("day10",day10);
		params.put("day11",day11);
		params.put("sysdate",new Date (System.currentTimeMillis()));
		String corpAuthSql= SqlUtil.getInSqlByParams(corpAuthList, "b.APPLY_CORP_ID", params);
		sql.append(" and ").append(corpAuthSql).append(" ");
		if(input.getFormdata().containsKey("accountGrantList")) {
			//添加账户授权控制
			List<String> accountGrantList = input.getFormdata().get("accountGrantList") == null ? null : (List<String>) input.getFormdata().get("accountGrantList");
			sql.append(" and (b.bank_acc is null or ").append(SqlUtil.getInSqlByParams(accountGrantList, "b.bank_acc", "bankAccs", params)).append(")");
		}
		return getDynamicBySQL(input, sql.toString(), AbBillCollectionApplyVo.class, "", false,params);
	}
	/**R
	 * 审批页面查询当前操作员可审批的托收申请数据
	 * @param input
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public DataTablesOutput dataTableApprove(DataTablesInput input,String day10,String day11,List<String> businessList) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus,c.status as status, ");
		sql.append("c.sttlmmk as sttlmmk,c.rmk as rmk,c.id as businessKey,");
		sql.append("(case when c.create_by is not null then BT_CONCAT4('(',c.create_by,')',c.create_name) ");
		sql.append("else null end) as applyName, b.BUS_CORP_NAME as busCorpName,");
		sql.append(" b.APPLY_CORP_ID as applyCorpId,b.APPLY_CORP_NAME as applyCorpName,b.APPLY_CORP_CODE as applyCorpCode,b.BUS_CORP_ID as busCorpId,b.BUS_PERSONNEL as busPersonnel, ");
		//10表示银票否则商票        结果:0未逾期 1逾期
		sql.append("(case when (CASE a.bill_type WHEN 10 THEN a.end_date + :day10 ELSE a.end_date + :day11 END) < :sysdate then '1' else '0' end) as isOverdue");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" WHERE 1=1 ");
		Map<String,Object> params = new HashMap<>();
		String applyName = input.getFormdata().get("applyName").toString();
		if (StringUtils.isNotEmpty(applyName)) {
			sql.append(" and BT_CONCAT4('(',c.create_by,')',c.create_name) like BT_CONCAT3('%',:applyName,'%')");
			params.put("applyName", applyName);
		}
		input.getFormdata().remove("applyName");
		params.put("day10",day10);
		params.put("day11",day11);
		params.put("sysdate",new Date (System.currentTimeMillis()));
		String businessSql = SqlUtil.getInSqlByParams(businessList, "c.id", params);
		sql.append(" and ").append(businessSql).append(" ");
		return getDynamicBySQL(input, sql.toString(), AbBillCollectionApplyVo.class, "", false,params);
	}
	/**
	 * 获取电票到期可以发起托收申请(注意：票据到期的才做自动提示托收申请,即票据日期小于当前日期)
	 * @param day10 银票的到期付款时间，用于计算银票是否逾期
	 * @param day11 商票的到期付款时间，用于计算商票是否逾期
	 * @param autoPayDayNum 电票自动托收申请提前天数
	 * @param isThirdBillAutoCollect 第三方票据是否自动托收申请
	 * 逾期托收申请需要填写逾期原因，故定时器不处理 modify 2017/10/23
	 * 拒签过的托收申请，定时器不再处理
	 */
	public List<AbBillCollectionApplyVoDTO> getAbBillCollectionApplyVoDTOList(String day10, String day11, int autoPayDayNum, String notInBifCodeSql, String isThirdBillAutoCollect) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append(" t.billType as billType,t.billFlag as billFlag,t.payer as payer,t.acceptor as acceptor,");
		sql.append(" t.origNo as origNo,t.billMoney as billMoney,t.startDate as startDate,t.endDate as endDate,");
		sql.append(" t.enterStock as enterStock,t.corpId as corpId,t.corpName as corpName,t.vid as vid,");
		sql.append(" t.vvid as vvid,t.aid as aid,t.boxStatus as boxStatus,t.status as status,t.sttlmmk as sttlmmk,");
		sql.append(" t.rmk as rmk,t.isOverdue as isOverdue, t.sbbllRngStrtSn,t.sbbllRngEndSn,t.bankAcc ");
		sql.append("from ( select a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,");
		sql.append(" a.create_By as createBy,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus, a.SBBLL_RNG_STRT_SN sbbllRngStrtSn,a.SBBLL_RNG_END_SN sbbllRngEndSn, ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_020+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" when '");
		sql.append( AccebillConstant.BRIEF_STATUS_021+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append("c.sttlmmk as sttlmmk,c.rmk as rmk, ");
		//10表示银票否则商票        结果:0未逾期 1逾期
		sql.append("(case when (CASE a.bill_type WHEN "+AccebillConstant.AB_BILL_TYPE_BANK+" THEN a.end_date + :day10 ELSE a.end_date + :day11 END) " +
				"< :sysdate then '1' else '0' end) as isOverdue,b.BANK_ACC as bankAcc");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join AB_ACCRECORDS c1 on c1.lastid = c.id ");//排除拒签过的托收申请
		sql.append("   and c1.brief_id in (:briefIds) and c1.opt_status = :optStatus2 ");

		sql.append(" left join ab_discount d on d.record_id = c.id ");
		//发起方账号关联电票账号方便排查离线登记的在线
//		sql.append(" left join bt_bank_acc ba on ba.electric_bill='1' and ba.circulation_mode='1' and ba.bank_acc=b.bank_acc ");//  electric_bill 票据专户
		//自动业务只发起一次
		sql.append(" left join AB_BILL_MIDDLE m on c.ID=m.bu_id and m.type='").append(AccebillConstant.BILL_MIDDLE_TYPE_020).append("' ");
		sql.append(" WHERE m.id IS NULL and b.BANK_ACC is not null and b.valid_sign = :validSign1 and c.valid_sign = :validSign1 and c.status = :status95 ");
		sql.append(" and c.opt_status = :optStatus and b.box_status = :boxStatusY  ");
		sql.append(" and c1.id is null ");
		//默认查询
		//电票：提示收票签收、背书申请签收、贴现赎回签收、质押解除签收
		sql.append ("and ((a.bill_flag = :billFlag11 and c.brief_id in (:briefIds2) and a.end_date <= :autoPayDay  ) ");
		sql.append (" or ");
		//电票: 质押签收到期的票据
		sql.append (" (c.brief_id = :briefId9018 and a.end_date <= :sysdate )  ");
		sql.append (" or ");
		//线下电票: 账号为线上账号
		sql.append (" (a.bill_flag = :billFlag11 and c.brief_id = :briefId6001  and a.end_date <= :autoPayDay ) ) ");
		if(StringUtils.isNotEmpty(notInBifCodeSql)){
			//排除不需要做提示付款的银行接口数据
			sql.append(" and ").append(notInBifCodeSql).append(" ");
		}
		Map<String,Object> params = new HashMap<>(16);
		//第三方票据是否自动托收
		if ("0".equals(isThirdBillAutoCollect)) {
			sql.append(" and a.bill_type in (:billTypeArr) ");
			params.put("billTypeArr", new String[]{AccebillConstant.AB_BILL_TYPE_BANK, AccebillConstant.AB_BILL_TYPE_MERCH});
		}
		//托收申请排除新一代票据只自动托收旧票
		sql.append(" and (a.new_stm_issuofppr_ind='0' or a.new_stm_issuofppr_ind is null) ");
		//只取非逾期的托收申请票据进行自动托收申请
		sql.append (" ) t where t.isOverdue = :isOverdue ");
		params.put("briefIds",new String[]{AccebillConstant.BRIEF_STATUS_020,
				AccebillConstant.BRIEF_STATUS_021
		});
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("optStatus",AccebillConstant.AB_BILL_OPT_STATUS19);
		params.put("optStatus2",AccebillConstant.AB_BILL_OPT_STATUS_4);
		params.put("boxStatusY",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billFlag11",Integer.parseInt(AccebillConstant.AB_BILL_FLAG_ELECT));
		params.put("briefIds2",new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019
		});
		params.put("briefId9018",AccebillConstant.BRIEF_STATUS_9018);
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("isOverdue",'0');
		params.put("day10",day10);
		params.put("day11",day11);
		Date sysData = new Date(System.currentTimeMillis());
		params.put("sysdate", sysData);
		params.put("autoPayDay", DateUtils.addDay(sysData, autoPayDayNum));
		params.put("autoPayDayNum",autoPayDayNum);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillCollectionApplyVoDTO.class);
	}
	/**
	 * 根据单位id和票据号查询托收申请
	 * @param corpId 单位id
	 * @param origNo 票据号
	 * @param day10 银票的到期付款时间，用于计算银票是否逾期
	 * @param day11 商票的到期付款时间，用于计算商票是否逾期
	 * @return
	 */
	public List<AbBillCollectionApplyVo> getAbBillCollectionApplyVoDTOList(String corpId, String origNo, String day10, String day11, String notInBifCodeSql) {
		StringBuilder sql=new StringBuilder("select ");
		sql.append("a.bill_type as billType,a.bill_flag as billFlag,a.payer as payer,a.acceptor as acceptor,");
		sql.append("a.orig_no as origNo,a.bill_money as billMoney,a.start_date as startDate,");
		sql.append("a.end_date as endDate,b.enter_stock as enterStock,b.corp_id as corpId,b.CORP_CODE as corpCode,b.CORP_NAME as corpName,");
		sql.append("a.id as vid,b.id as vvid,c.id as aid,b.box_status as boxStatus, ");
		sql.append("(case b.back_brief_id when '"+AccebillConstant.BRIEF_STATUS_020+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" when '");
		sql.append( AccebillConstant.BRIEF_STATUS_021+"' then "+AccebillConstant.AB_BILL_BUS_STATUS_1+" else c.status end)  as status,");
		sql.append("c.sttlmmk as sttlmmk,c.rmk as rmk, ");
		//10表示银票否则商票        结果:0未逾期 1逾期
		sql.append("(case when (CASE a.bill_type WHEN "+AccebillConstant.AB_BILL_TYPE_BANK+" THEN a.end_date + :day10 ELSE a.end_date + :day11 END) " +
				"< :sysdate then '1' else '0' end) as isOverdue,c.send_sign as sendSign ");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
//		sql.append(" left join sys_corp s on b.corp_id = s.id ");
		sql.append(" left join ab_discount d on d.record_id = c.id ");
		sql.append(" WHERE b.valid_sign = :validSign1 ");
		sql.append(" and a.start_date <= :sysdate ");
		//默认查询
		sql.append(" and ( ( ");
		sql.append(" c.valid_sign = :validSign1 and c.status = :status95 and b.box_status = :boxStatusY ");
		//电票：提示收票签收、背书申请签收、贴现赎回签收、质押解除签收
		sql.append ("and ((a.bill_flag = :billFlag11 and c.brief_id in (:briefIds2) ) ");
		sql.append (" or ");
		//纸票: 质押收回、贴现收回
		sql.append (" c.brief_id in (:briefIds3) ");
		sql.append (" or ");
		//电票: 质押签收到期的票据
		sql.append (" (c.brief_id = :briefId9018 and a.end_date < :sysdate ) ");
		sql.append (" or ");
		//收票登记不为贴现和质押的票据
		sql.append (" (c.brief_id = '6001' and b.bill_source not in ( :billSource12,:billSource14) ) ");
		sql.append (" or ");
		//纸票:收票登记来源为质押，票据已到期的票据
		sql.append (" (c.brief_id = :briefId6001 and b.BILL_SOURCE = :billSource12 ");
		sql.append ("  and a.end_date< :sysdate )  ");
		sql.append (" or ");
		//纸票:收票登记中来源为买断式贴现的票据
		sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 and d.rpdmk = :rpdmkRM00) ");
		sql.append (" or ");
		//纸票:收票登记中来源为回购式贴现的票据且已超过赎回截止日
		sql.append (" ( c.brief_id = :briefId6001 and b.bill_source = :billSource14 ");
		sql.append ("  and d.rpdmk = :rpdmkRM01 and d.rpdduedt < :sysdate ) ) ");
		sql.append(" ) or ( ");
		sql.append(" c.valid_sign = :validSign0 and (c.status = :status11 or c.status = :status94 ) and b.box_status = :boxStatusN ");
		sql.append(" and c.brief_id in (:briefIds) )) ");
		sql.append(" and a.orig_no=:origNo and b.CORP_ID=:corpId ");
		if(StringUtils.isNotEmpty(notInBifCodeSql)){
			//排除不需要做提示付款的银行接口数据
			sql.append(" and ").append(notInBifCodeSql).append(" ");
		}
		Map<String,Object> params = new HashMap<> ();
		params.put("validSign0",AccebillConstant.AB_BILL_VALID_SIGN_0);
		params.put("validSign1",AccebillConstant.AB_BILL_VALID_SIGN_1);
		params.put("status11",AccebillConstant.AB_BILL_BUS_STATUS11);
		params.put("status94",AccebillConstant.AB_BILL_BUS_STATUS94);
		params.put("boxStatusN",AccebillConstant.AB_BOX_STATUS_N);
		params.put("briefIds",new String[]{AccebillConstant.BRIEF_STATUS_020,AccebillConstant.BRIEF_STATUS_021});
		params.put("status95",AccebillConstant.AB_BILL_BUS_STATUS95);
		params.put("boxStatusY",AccebillConstant.AB_BOX_STATUS_Y);
		params.put("billFlag11",AccebillConstant.AB_BILL_FLAG_ELECT);
		params.put("briefIds2",new String[]{AccebillConstant.BRIEF_STATUS_9003,AccebillConstant.BRIEF_STATUS_9010,
				AccebillConstant.BRIEF_STATUS_9012,AccebillConstant.BRIEF_STATUS_9019
		});
		params.put("briefId6001",AccebillConstant.BRIEF_STATUS_6001);
		params.put("billSource12",AccebillConstant.AB_BILL_SOURCE_PLEDGE);
		params.put("briefId9018",AccebillConstant.BRIEF_STATUS_9018);
		params.put("billSource14",AccebillConstant.AB_BILL_SOURCE_DISCOUNT);
		params.put("briefIds3",new String[]{AccebillConstant.BRIEF_STATUS_6019,AccebillConstant.BRIEF_STATUS_6212});
		params.put("rpdmkRM00",AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT);
		params.put("rpdmkRM01",AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK);
		params.put("day10",day10);
		params.put("day11",day11);
		params.put("sysdate",new Date (System.currentTimeMillis()));
		params.put("origNo",origNo);
		params.put("corpId",corpId);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillCollectionApplyVo.class);
	}
}
