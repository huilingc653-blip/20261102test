package com.bytter.bill.domain.bill.dao;
import com.bytter.bill.domain.bill.vo.AbAccrecordsVO;
import com.bytter.bill.api.bill.constant.AccebillConstant;
import com.bytter.bill.domain.bill.vo.AbBillReceivebizVO;
import com.bytter.core.base.BaseDao;
import com.bytter.core.utils.DateUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* @Title: Dao
* @Description: 票据消息推送Dao
* @date 2017-11-14 15:36:09
* @version 1.0
*
*/
@Repository
public class AbBillNotificationDao extends BaseDao<T,String>{
	
	/**
	 * 线上电票提示收票完成，未推送过消息的数据
	 * @return
	 */
	public List<AbAccrecordsVO> listTask003To6101Info(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(" select a.id from AB_ACCRECORDS a  ");
		sql.append(" left join AB_BILL_MIDDLE m on a.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_6);
		//提示收票业务类型
		sql.append(" where m.id IS NULL ");
		sql.append(" and a.brief_Id=:briefId ");
		params.put("briefId", AccebillConstant.BRIEF_STATUS_003);
		//状态为审批通过
		sql.append(" and a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		//指令状态为已完成
		sql.append(" and a.opt_Status=:optStatus ");
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		//流水为有效状态
		sql.append(" and a.valid_Sign=:validSign ");
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		//指令发送标志
		sql.append(" and a.send_Sign=:sendSign ");
		params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	
	/**
	 * 自开票据兑付提醒，票据到期以后提醒推送
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskEmitBillPay(Map<String,Object> params, boolean isEmitMake){
//		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(" select a.id from AB_ACCRECORDS a  ");
		sql.append(" inner join ab_voucherinfo v on v.id=a.vid ");
		sql.append(" left join AB_BILL_MIDDLE m on a.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_5);
		sql.append(" where m.id IS NULL ");
		//系统需要开票补录功能时
		if (isEmitMake) {
			// 业务类型 brief_id -- 6101(	补录)
			sql.append(" and a.brief_id in (:briefId6101) ");
			params.put("briefId6101",new String[]{AccebillConstant.BRIEF_STATUS_6101});
		//系统不需要开票补录功能时
		} else {
			// 指令发送提示收票完成(线上)和开票登记完成(线下)就可以做兑付确认 不需要补录`
			sql.append(" and ( (a.brief_id in (:briefId003) and a.send_sign=:sendSign1 ) or (a.brief_id in (:briefId001) and a.send_sign=:sendSign0 ) or a.brief_id in (:briefId6101)) ");
			params.put("sendSign0",new String[]{AccebillConstant.AB_BILL_SEND_SIGN_N});
			params.put("sendSign1",new String[]{AccebillConstant.AB_BILL_SEND_SIGN_Y});
			params.put("briefId001",new String[]{AccebillConstant.BRIEF_STATUS_001});
			params.put("briefId003",new String[]{AccebillConstant.BRIEF_STATUS_003});
			params.put("briefId6101",new String[]{AccebillConstant.BRIEF_STATUS_6101});
		}
		//状态为审批通过
		sql.append(" and a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		//指令状态为已完成
		sql.append(" and a.opt_Status=:optStatus ");
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS19);
		//流水为有效状态
		sql.append(" and a.valid_Sign=:validSign ");
		params.put("validSign", AccebillConstant.AB_BILL_VALID_SIGN_1);
		//票据已经到结束日期
		sql.append(" and v.end_date<=:endDate ");
//		params.put("endDate", DateUtils.getDate());
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	
	/**
	 * 托收提醒，票据到期自动提醒
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskCollectionApplyDueto(Map<String,Object> params){
//		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder("select ");
		sql.append("c.id ");
		sql.append(" from AB_VOUCHERINFO_VICE b ");
		sql.append(" left join AB_VOUCHERINFO a on a.id = b.vid and (a.OLD_DRAFT_ID is null or a.OLD_DRAFT_ID not like '%-del') ");
		sql.append(" left join AB_ACCRECORDS c on c.vvid = b.id");
		sql.append(" left join ab_discount d on d.record_id = c.id ");
		sql.append(" left join AB_BILL_MIDDLE m on c.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_2);
		sql.append(" where m.id IS NULL ");
		sql.append(" and c.valid_sign = '"+AccebillConstant.AB_BILL_VALID_SIGN_1+"' ");
		sql.append(" and c.status = '"+AccebillConstant.AB_BILL_BUS_STATUS95+"' ");
		sql.append(" and c.opt_status = '"+AccebillConstant.AB_BILL_OPT_STATUS19+"' ");
		sql.append(" and b.box_status = '"+AccebillConstant.AB_BOX_STATUS_Y+"'  ");
		//默认查询
		//电票：提示收票签收、背书申请签收、贴现赎回签收、质押解除签收
		sql.append ("and ((a.bill_flag = '"+AccebillConstant.AB_BILL_FLAG_ELECT+"' and c.brief_id in ('"+AccebillConstant.BRIEF_STATUS_9003+"', '"+AccebillConstant.BRIEF_STATUS_9010+"', ");
		sql.append ("  '"+AccebillConstant.BRIEF_STATUS_9012+"', '"+AccebillConstant.BRIEF_STATUS_9019+"') ) ");
		sql.append (" or ");
		//收票登记不为贴现和质押的票据
		sql.append (" (c.brief_id = '"+AccebillConstant.BRIEF_STATUS_6001+"' and b.bill_source not in ('"+AccebillConstant.AB_BILL_SOURCE_PLEDGE+"','"+AccebillConstant.AB_BILL_SOURCE_DISCOUNT+"') ) ");
		sql.append (" or ");
		//纸票: 质押收回、贴现收回、收票登记中来源不为质押和贴现的票据
		sql.append (" c.brief_id in ('"+AccebillConstant.BRIEF_STATUS_6019+"','"+AccebillConstant.BRIEF_STATUS_6212+"') ");
		sql.append (" or ");
		//纸票:收票登记来源为质押，票据已到期的票据
		sql.append (" (c.brief_id = '"+AccebillConstant.BRIEF_STATUS_6001+"' and b.BILL_SOURCE = '"+AccebillConstant.AB_BILL_SOURCE_PLEDGE+"'");
		sql.append ("  and a.end_date<:today ) ");
		sql.append (" or ");
		//电票: 质押签收到期的票据
		sql.append (" (c.brief_id = '"+AccebillConstant.BRIEF_STATUS_9018+"' and a.end_date < :today ) ");
		sql.append (" or ");
		//纸票:收票登记中来源为买断式贴现的票据
		sql.append (" ( c.brief_id = '"+AccebillConstant.BRIEF_STATUS_6001+"' and b.bill_source = '"+AccebillConstant.AB_BILL_SOURCE_DISCOUNT+"' and d.rpdmk = '"+AccebillConstant.AB_DISCOUNT_RPDMK_BUYOUT+"') ");
		sql.append (" or ");
		//纸票:收票登记中来源为回购式贴现的票据且已超过赎回截止日
		sql.append (" ( c.brief_id = '"+AccebillConstant.BRIEF_STATUS_6001+"' and b.bill_source = '"+AccebillConstant.AB_BILL_SOURCE_DISCOUNT+"'");
		sql.append ("  and d.rpdmk = '"+AccebillConstant.AB_DISCOUNT_RPDMK_BUYBACK+"' and d.rpdduedt < :today ) ) ");
		//票据已经到结束日期
		sql.append(" and a.end_date<=:endDate ");
		params.put("today", DateUtils.getDate());
		
		//如果系统配置了自动提示付款则持有在线的电票 将不再提醒用户，有系统自动发起
		if("true".equals(String.valueOf(params.get("isEAutoPay")))){
			sql.append(" and c.send_Sign!=:sendSign ");
			params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		}
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	
	/**
	 * 托收提醒，票据到期托收拒付提醒
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskCollectionApplyRefuse(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(" select a.id,a.lastid from AB_ACCRECORDS a  ");
		sql.append(" inner join ab_voucherinfo v on v.id=a.vid ");
		sql.append(" left join AB_BILL_MIDDLE m on a.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_3);
		sql.append(" where m.id IS NULL ");
		//票据已补录
		sql.append(" and a.brief_Id in (:briefId) ");
		String[] briefId={
				AccebillConstant.BRIEF_STATUS_020,
				AccebillConstant.BRIEF_STATUS_021
		};
		params.put("briefId", briefId);
		//状态为审批通过
		sql.append(" and a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_BUS_STATUS95);
		//指令状态为已完成
		sql.append(" and a.opt_Status=:optStatus ");
		params.put("optStatus", AccebillConstant.AB_BILL_OPT_STATUS_4);
		//指令发送标志
		sql.append(" and a.send_Sign=:sendSign ");
		params.put("sendSign", AccebillConstant.AB_BILL_SEND_SIGN_Y);
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
	
	/**
	 * 票据待签收提醒，电票收到签收数据推送提醒
	 * @return
	 */
	public List<AbBillReceivebizVO> listTaskSign(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(" select a.id from ab_bill_receivebiz a  ");
		sql.append(" left join AB_BILL_MIDDLE m on a.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_1);
		sql.append(" where m.id IS NULL and a.status in (-2,18) ");
		//签收类型
		sql.append(" and a.msgtype in(:msgtype) ");
		String[] msgtype={
				AccebillConstant.AB_MSGTYPE_ACCEPT,
				AccebillConstant.AB_MSGTYPE_RECBILL,
				AccebillConstant.AB_MSGTYPE_RECITE,
//				AccebillConstant.AB_MSGTYPE_DISCOUNT,
				AccebillConstant.AB_MSGTYPE_DISCOUNT_REDEM,
				AccebillConstant.AB_MSGTYPE_GUARGN,
				AccebillConstant.AB_MSGTYPE_PLEDGE,
				AccebillConstant.AB_MSGTYPE_PLEDGE_RELIEVE,
				AccebillConstant.AB_MSGTYPE_HINT_PAYER,
				AccebillConstant.AB_MSGTYPE_HINT_PAYER_OVERDUE,
				AccebillConstant.AB_MSGTYPE_SEARCHFOR_PAYOFF
		};
		params.put("msgtype", msgtype);
		//状态为待签收
		sql.append(" and a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillReceivebizVO.class);
	}
	
	/**
	 * 追索清偿提醒，电票收到追索数据推送提醒
	 * @return
	 */
	public List<AbBillReceivebizVO> listTaskSearchforManage(){
		Map<String,Object> params=new HashMap<>();
		StringBuilder sql=new StringBuilder(" select a.id from ab_bill_receivebiz a  ");
		sql.append(" left join AB_BILL_MIDDLE m on a.id=m.bu_id and m.type=:mType ");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_4);
		sql.append(" where m.id IS NULL ");
		//追索申请类型值
		sql.append(" and a.msgtype in(:msgtype) ");
		String[] msgtype={
				AccebillConstant.AB_MSGTYPE_SEARCHFOR
		};
		params.put("msgtype", msgtype);
		//状态为待签收
		sql.append(" and a.status=:status ");
		params.put("status", AccebillConstant.AB_BILL_RECEIVEBIZ_STATUS_PENDING);
		return this.listBySQLAliasToBean(sql.toString(), params, AbBillReceivebizVO.class);
	}
	/**
	 * 电票指令失败消息提醒
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskInstructionManage(Map<String,Object> params) {
		StringBuilder sql = new StringBuilder("select l.id");
		sql.append(" from AB_VOUCHERINFO_VICE c ")
				.append(" left join AB_VOUCHERINFO z on z.id=c.vid ")
				.append(" left join AB_ACCRECORDS l on c.id = l.vvid ")
				.append(" left join AB_BILL_MIDDLE m on l.id=m.bu_id and m.type=:mType ")
				.append(" where c.valid_sign='" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "'  ")
				.append(" and z.bill_flag ='" + AccebillConstant.AB_BILL_FLAG_ELECT + "' ")
				//（撤票时前业务valid_sign为2）
				.append(" and l.valid_sign in ('" + AccebillConstant.AB_BILL_VALID_SIGN_2 + "','" + AccebillConstant.AB_BILL_VALID_SIGN_1 + "') ")
				.append(" and l.SEND_SIGN='" + AccebillConstant.AB_BILL_SEND_SIGN_Y + "' ")
				.append(" and l.STATUS='" + AccebillConstant.AB_BILL_BUS_STATUS95 + "' ")
				.append(" and l.OPT_STATUS in ('" + AccebillConstant.AB_BILL_OPT_STATUS_1 + "') ")
				//指令处理排除掉票据池的票据明细指令
				.append(" and l.PD_ID is null ")
				.append(" and m.id IS NULL");
		params.put("mType", AccebillConstant.BILL_MIDDLE_TYPE_7);
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}

	/**
	 * 提前五天及提前两天预警信息
	 * @param params
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskBillExpirationWarning(Map<String,Object> params) {
		StringBuilder sql = new StringBuilder("select c.corp_id corpId,c.BANK_ACC bankAcc,c.BANK_NAME bankName,l.REQ_NM reqNm,count(1) billNums,sum(z.bill_money) billMoney,");
		sql.append("sum(z.bill_money - z.bill_money*(case c.BAIL_PERCENT when null then 0 else c.BAIL_PERCENT end)) releasePaymentMoney,");
		sql.append("z.end_date endDate,max(l.id) id,max(l.opt_status) optStatus ");
		sql.append(" from AB_VOUCHERINFO_VICE c ")
				.append(" inner join AB_VOUCHERINFO z on z.id=c.vid ")
				.append(" inner join AB_ACCRECORDS l on c.id = l.vvid ")
				.append(" left join AB_ACCRECORDS lt on c.id = lt.vvid and lt.brief_id='004' and lt.opt_status='19' ")
				.append(" left join AB_ACCRECORDS laa on c.id = laa.vvid and laa.brief_id='6120' and laa.opt_status='19' ")
				.append(" where c.valid_sign != 3 and l.status = 95 and l.opt_status = 19 ");
		sql.append(" and ((l.send_sign = '1' and l.BRIEF_ID = '003') or (l.send_sign = '0' and l.BRIEF_ID = '001')) and lt.id is null and laa.id is null ");
		sql.append(" and (c.BAIL_PERCENT is null or c.BAIL_PERCENT < 1) and (z.end_date = :endDate5 or z.end_date = :endDate2)")
				.append(" group by c.corp_id,c.BANK_ACC,l.REQ_NM, c.BANK_NAME,z.end_date ");
		params.put("endDate5", DateUtils.addDay(DateUtils.getDateYMD(),5));
		params.put("endDate2", DateUtils.addDay(DateUtils.getDateYMD(),2));
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}

	/**
	 * 承兑账户资金余额不足承兑扣款消息提醒
	 * @param params
	 * @return
	 */
	public List<AbAccrecordsVO> listTaskInsufficientBalanceReminder(Map<String,Object> params, boolean isRangeQuery) {
		StringBuilder sql = new StringBuilder("select c.corp_id corpId,c.BANK_ACC bankAcc,c.BANK_NAME bankName,l.REQ_NM reqNm,count(1) billNums,sum(z.bill_money) billMoney,");
		sql.append("sum(z.bill_money - z.bill_money*(case when c.BAIL_PERCENT is null then 0 else c.BAIL_PERCENT end)) releasePaymentMoney,");
		if(!isRangeQuery){
			sql.append("z.end_date endDate,max(l.id) id,max(l.opt_status) optStatus ");
		}else{
			sql.append("max(l.id) id,max(l.opt_status) optStatus ");
		}
		sql.append(" from AB_VOUCHERINFO_VICE c ")
				.append(" inner join AB_VOUCHERINFO z on z.id=c.vid ")
				.append(" inner join AB_ACCRECORDS l on c.id = l.vvid ")
				.append(" left join AB_ACCRECORDS lt on c.id = lt.vvid and lt.brief_id='004' and lt.opt_status='19' ")
				.append(" left join AB_ACCRECORDS laa on c.id = laa.vvid and laa.brief_id='6120' and laa.opt_status='19' ")
				.append(" where c.valid_sign != 3 and l.status = 95 and l.opt_status = 19 and (c.BAIL_PERCENT is null or c.BAIL_PERCENT < 1  )  " +
						"and ((l.send_sign = '1' and l.BRIEF_ID = '003') or (l.send_sign = '0' and l.BRIEF_ID = '001')) and lt.id is null and laa.id is null ")
				.append(" and (c.BAIL_PERCENT is null or c.BAIL_PERCENT < 1) ");
		if(!isRangeQuery){
			sql.append(" and z.end_date = :endDate1  ");
			sql.append(" group by c.corp_id,c.BANK_ACC,l.REQ_NM, c.BANK_NAME,z.end_date ");
		}else{
			sql.append(" and z.end_date >= :endDate1 and z.end_date <= :endDate2  ");
			sql.append(" group by c.corp_id,c.BANK_ACC,l.REQ_NM, c.BANK_NAME ");
		}
		return this.listBySQLAliasToBean(sql.toString(), params, AbAccrecordsVO.class);
	}
}
