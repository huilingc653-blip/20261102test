package com.bytter.bill.domain.billbank.tool;
import com.bytter.bill.domain.billbank.util.EDraftConstant;
/**
 * <br>
 * <b>类名：</b>类型转换，包括报文类型，业务类型，票据类型等<br>
 * <b>作者：</b>李杰<br>
 * <b>日期：</b> 2013-08-08 <br>
 */
public class TypeConverter {
	/**
	 * <br>
	 * <b>类名：</b>报文类型转换<br>
	 * <b>作者：</b>李杰<br>
	 * <b>日期：</b> 2013-08-08 <br>
	 */
	public static String converterMsgType(String msgType) {
		String msgStr = null;
		if (EDraftConstant.MSG_TYPE_REGISTER_REQUEST_001.equals(msgType)) { // 出票申请
			msgStr = "出票申请";
		} else if (EDraftConstant.MSG_TYPE_ACCEPTANCE_REQUEST_002
				.equals(msgType)) { // 提示承兑
			msgStr = "提示承兑";
		} else if (EDraftConstant.MSG_TYPE_ISSUANCE_REQUEST_003.equals(msgType)) { // 提示收票
			msgStr = "提示收票";
		} else if (EDraftConstant.MSG_TYPE_DESTRUCTION_REQUEST_004
				.equals(msgType)) { // 撤票申请
			msgStr = "撤票";
		} else if (EDraftConstant.MSG_TYPE_ENDORSEMENT_REQUEST_010
				.equals(msgType)) { // 背书转让
			msgStr = "背书转让";
		} else if (EDraftConstant.MSG_TYPE_DISCOUNT_REQUEST_011.equals(msgType)) { // 贴现申请
			msgStr = "贴现";
		} else if (EDraftConstant.MSG_TYPE_REPURCHASED_DISCOUNT_REQUEST_012
				.equals(msgType)) { // 回购式贴现赎回申请
			msgStr = "回购式贴现赎回";
		} else if (EDraftConstant.MSG_TYPE_GUARANTEE_REQUEST_017
				.equals(msgType)) { // 保证申请
			msgStr = "保证";
		} else if (EDraftConstant.MSG_TYPE_COLLATERALIZATION_REQUEST_018
				.equals(msgType)) { // 质押
			msgStr = "质押";
		} else if (EDraftConstant.MSG_TYPE_REPURCHASED_COLLATERALIZATION_REQUEST_019
				.equals(msgType)) { // 解除质押
			msgStr = "解除质押";
		} else if (EDraftConstant.MSG_TYPE_PRESENTATION_REQUEST_020
				.equals(msgType)) { // 提示付款申请
			msgStr = "提示付款";
		} else if (EDraftConstant.MSG_TYPE_OVER_DUE_PRESENTATION_REQUEST_021
				.equals(msgType)) { // 逾期付款
			msgStr = "逾期付款";
		} else if (EDraftConstant.MSG_TYPE_RECOURSE_NOTIFICATION_022
				.equals(msgType)) { // 追索
			msgStr = "追索";
		} else if (EDraftConstant.MSG_TYPE_RECOURSE_AGREEMENT_REQUEST_023
				.equals(msgType)) { // 追索同意清偿
			msgStr = "追索同意清偿";
		} else if (EDraftConstant.MSG_TYPE_COMMON_SIGN_UP_031.equals(msgType)) { // 通用回复(签收)
			msgStr = "签收";
		} else if (EDraftConstant.MSG_TYPE_CANCELLATION_REQUEST_032
				.equals(msgType)) { // 同意撤回
			msgStr = "同意撤回";
		}
		return msgStr;
	}
	/**
	 * <br>
	 * <b>类名：</b>票据类型转换<br>
	 * <b>作者：</b>李杰<br>
	 * <b>日期：</b> 2013-08-08 <br>
	 */
	public static String converterbillType(String billType) {
		String billTypeStr = null;
		if (EDraftConstant.BILL_TYPE_AC01.equals(billType)) { // 银票
			billTypeStr = "银承";
		} else if (EDraftConstant.BILL_TYPE_AC02.equals(billType)) { // 商票
			billTypeStr = "商承";
		}
		return billTypeStr;
	}
	public static String converterBriefId(String briefId) {
		String briefStr = null;
		if (EDraftConstant.BUSINESS_001.equals(briefId)) {
			briefStr = "出票登记";
		} else if (EDraftConstant.BUSINESS_002.equals(briefId)) {
			briefStr = "提示承兑";
		} else if (EDraftConstant.BUSINESS_9002.equals(briefId)) {
			briefStr = "承兑签收回复";
		} else if (EDraftConstant.BUSINESS_003.equals(briefId)) {
			briefStr = "提示收票";
		} else if (EDraftConstant.BUSINESS_9003.equals(briefId)) {
			briefStr = "收票签收回复";
		} else if (EDraftConstant.BUSINESS_004.equals(briefId)) {
			briefStr = "撤票申请";
		} else if (EDraftConstant.BUSINESS_0171.equals(briefId)) {
			briefStr = "开票提示保证";
		} else if (EDraftConstant.BUSINESS_0172.equals(briefId)) {
			briefStr = "承兑提示保证";
		} else if (EDraftConstant.BUSINESS_0173.equals(briefId)) {
			briefStr = "背书提示保证";
		} else if (EDraftConstant.BUSINESS_9017.equals(briefId)) {
			briefStr = "保证签收";
		} else if (EDraftConstant.BUSINESS_031.equals(briefId)) {
			briefStr = "通用回复";
		} else if (EDraftConstant.BUSINESS_010.equals(briefId)) {
			briefStr = "背书申请";
		} else if (EDraftConstant.BUSINESS_9010.equals(briefId)) {
			briefStr = "背书签收回复";
		} else if (EDraftConstant.BUSINESS_011.equals(briefId)) {
			briefStr = "贴现申请";
		} else if (EDraftConstant.BUSINESS_9011.equals(briefId)) {
			briefStr = "贴现签收回复";
		} else if (EDraftConstant.BUSINESS_020.equals(briefId)) {
			briefStr = "提示付款";
		} else if (EDraftConstant.BUSINESS_9020.equals(briefId)) {
			briefStr = "提示付款签收";
		} else if (EDraftConstant.BUSINESS_018.equals(briefId)) {
			briefStr = "质押";
		} else if (EDraftConstant.BUSINESS_019.equals(briefId)) {
			briefStr = "质押解除";
		} else if (EDraftConstant.BUSINESS_9018.equals(briefId)) {
			briefStr = "质押签收回复";
		} else if (EDraftConstant.BUSINESS_9019.equals(briefId)) {
			briefStr = "质押解除签收回复";
		} else if (EDraftConstant.BUSINESS_022.equals(briefId)) {
			briefStr = "追索";
		} else if (EDraftConstant.BUSINESS_9022.equals(briefId)) {
			briefStr = "追索签收回复";
		} else if (EDraftConstant.BUSINESS_021.equals(briefId)) {
			briefStr = "逾期提示付款";
		} else if (EDraftConstant.BUSINESS_9021.equals(briefId)) {
			briefStr = "逾期提示付款签收";
		} else if (EDraftConstant.BUSINESS_032.equals(briefId)) {
			briefStr = "通用撤回";
		} else if (EDraftConstant.BUSINESS_023.equals(briefId)) {
			briefStr = "同意清偿";
		}
		return briefStr;
	}
	/**
	 * 把账户类型转化为字符串，便于页面显示
	 * 
	 * @param payeeAccType
	 * @return
	 */
	public static String convertAccTypeStr(String accType) {
		String accTypeStr = null;
		if (accType != null && accType.trim().length() > 0) {
			// 0:往来客户账户,1:单位客户账户,其他:授权账户
			if (EDraftConstant.ACCT_TYPE_CUSTOMER.equals(accType)) {
				accTypeStr = "往来客户";
			} else if (EDraftConstant.ACCT_TYPE_COMPANY.equals(accType)) {
				accTypeStr = "单位客户账户";
			} else if (EDraftConstant.ACCT_TYPE_AUTHORIZE.equals(accType)) {
				accTypeStr = "授权账户";
			} else {
				accTypeStr = "银行";
			}
		}
		return accTypeStr;
	}
	public static String convertBillTypeStr(String billType) {
		String billTypeStr = null;
		if (billType != null) {
			if (EDraftConstant.BILL_TYPE_AC01.equals(billType)) {
				billTypeStr = "银票";
			} else {
				billTypeStr = "商票";
			}
		}
		return billTypeStr;
	}
	public static String convertBillTypeStr(Integer billType) {
		String billTypeStr = null;
		if (billType != null) {
			if (billType == EDraftConstant.BILL_TYPE_10) {
				billTypeStr = "银票";
			} else {
				billTypeStr = "商票";
			}
		}
		return billTypeStr;
	}
	public static String convertStatusStr(Integer status) {
		String statusStr = null;
		if (status != null) {
			if (status == EDraftConstant.OPERATE_STATUS_TEMPSAVE_10) {
				statusStr = "暂存";
			} else if (status == EDraftConstant.OPERATE_STATUS_PRECORPAPPROVE_11 
					|| status == EDraftConstant.OPERATE_STATUS_CORPAPPROVING_12
					|| status == EDraftConstant.OPERATE_STATUS_PREFCCONFRIM_13
					|| status == EDraftConstant.OPERATE_STATUS_PREFCAPPROVE_14
					|| status == EDraftConstant.OPERATE_STATUS_FCAPPROVING_15) {
				statusStr = "审批中";
			} else if (status == EDraftConstant.OPERATE_STATUS_PRESEND) {
				statusStr = "审批完成待发送";
			} else if (status == EDraftConstant.OPERATE_STATUS_SENDED_17) {
				statusStr = "已发送待回应";
			} else if (status == EDraftConstant.OPERATE_STATUS_SENDCONFIRM_18) {
				statusStr = "待签收";
			} else if (status == EDraftConstant.OPERATE_STATUS_BIZFINISH_19) {
				statusStr = "业务完成";
			} else if (status == EDraftConstant.OPERATE_STATUS_ACCEPTING_30) {
				statusStr = "待提示承兑";
			} else if (status == EDraftConstant.OPERATE_STATUS_ISSUANCE_31) {
				statusStr = "待提示收票";
			} else if (status == EDraftConstant.OPERATE_STATUS_SENDING) {
				statusStr = "发送中";
			} else if (status == EDraftConstant.OPERATE_STATUS_FAILED) {
				statusStr = "失败";
			} else if (status == EDraftConstant.OPERATE_STATUS_ROLLBACK) {
				statusStr = "审批打回";
			} else if (status == EDraftConstant.OPERATE_STATUS_WITHDRAW) {
				statusStr = "已撤回";
			} else if (status == EDraftConstant.OPERATE_STATUS_REFUSE) {
				statusStr = "拒绝签收";
			}
		}
		return statusStr;
	}
	public static String convertBanendrsmtmkStr(String banendrsmtmk) {
		String result = null;
		if (EDraftConstant.BAN_ENDRSMT_MK_EM00.equalsIgnoreCase(banendrsmtmk)) {
			result = "可再转让";
		} else {
			result = "不可转让";
		}
		return result;
	}
	/**
	 * <br>
	 * <b>类名：</b>票据类型转换<br>
	 * <b>作者：</b>李杰<br>
	 * <b>日期：</b> 2013-08-08 <br>
	 */
	public static int converterBillTypeAC(String billType) {
		if (EDraftConstant.BILL_TYPE_AC01.equals(billType)) { // 银票
			return EDraftConstant.BILL_TYPE_10;
		} else if (EDraftConstant.BILL_TYPE_AC02.equals(billType)) { // 商票
			return EDraftConstant.BILL_TYPE_11;
		} else {
			return 0;
		}
	}
	public static String convertBillType(Integer billType) {
		if (EDraftConstant.BILL_TYPE_10 == billType.intValue()) {
			return EDraftConstant.BILL_TYPE_AC01;
		} else {
			return EDraftConstant.BILL_TYPE_AC02;
		}
	}
	public static String converterBillStatus(String billStatus) {
		String billStatusComm = null;
		if (EDraftConstant.BILL_STATUS_010004.equals(billStatus)) {
			billStatusComm = "出票已登记";
		} else if (EDraftConstant.BILL_STATUS_020001.equals(billStatus)) {
			billStatusComm = "提示承兑待签收";
		} else if (EDraftConstant.BILL_STATUS_020006.equals(billStatus)) {
			billStatusComm = "提示承兑已签收";
		} else if (EDraftConstant.BILL_STATUS_030001.equals(billStatus)) {
			billStatusComm = "提示收票待签收";
		} else if (EDraftConstant.BILL_STATUS_030006.equals(billStatus)) {
			billStatusComm = "提示收票已签收";
		} else if (EDraftConstant.BILL_STATUS_000002.equals(billStatus)) {
			billStatusComm = "结束已作废";
		} else if (EDraftConstant.BILL_STATUS_100001.equals(billStatus)) {
			billStatusComm = "背书待签收";
		} else if (EDraftConstant.BILL_STATUS_100006.equals(billStatus)) {
			billStatusComm = "背书已签收";
		} else if (EDraftConstant.BILL_STATUS_110101.equals(billStatus)) {
			billStatusComm = "买断式贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_110103.equals(billStatus)) {
			billStatusComm = "买断式贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_110105.equals(billStatus)) {
			billStatusComm = "买断式贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_110106.equals(billStatus)) {
			billStatusComm = "买断式贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_110201.equals(billStatus)) {
			billStatusComm = "回购式贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_110203.equals(billStatus)) {
			billStatusComm = "回购式贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_110205.equals(billStatus)) {
			billStatusComm = "回购式贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_110206.equals(billStatus)) {
			billStatusComm = "回购式贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_110216.equals(billStatus)) {
			billStatusComm = "回购式贴现已至赎回开放日";
		} else if (EDraftConstant.BILL_STATUS_110218.equals(billStatus)) {
			billStatusComm = "回购式贴现已逾赎回截止日";
		} else if (EDraftConstant.BILL_STATUS_120001.equals(billStatus)) {
			billStatusComm = "回购式贴现赎回待签收";
		} else if (EDraftConstant.BILL_STATUS_120003.equals(billStatus)) {
			billStatusComm = "回购式贴现赎回已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_120005.equals(billStatus)) {
			billStatusComm = "回购式贴现赎回已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_120006.equals(billStatus)) {
			billStatusComm = "回购式贴现赎回已签收";
		} else if (EDraftConstant.BILL_STATUS_130101.equals(billStatus)) {
			billStatusComm = "买断式转贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_130103.equals(billStatus)) {
			billStatusComm = "买断式转贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_130105.equals(billStatus)) {
			billStatusComm = "买断式转贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_130106.equals(billStatus)) {
			billStatusComm = "买断式转贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_130201.equals(billStatus)) {
			billStatusComm = "回购式转贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_130203.equals(billStatus)) {
			billStatusComm = "回购式转贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_130205.equals(billStatus)) {
			billStatusComm = "回购式转贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_130206.equals(billStatus)) {
			billStatusComm = "回购式转贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_130216.equals(billStatus)) {
			billStatusComm = "回购式转贴现已至赎回开放日";
		} else if (EDraftConstant.BILL_STATUS_130218.equals(billStatus)) {
			billStatusComm = "回购式转贴现已逾赎回截止日";
		} else if (EDraftConstant.BILL_STATUS_140001.equals(billStatus)) {
			billStatusComm = "回购式转贴现赎回待签收";
		} else if (EDraftConstant.BILL_STATUS_140003.equals(billStatus)) {
			billStatusComm = "回购式转贴现赎回已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_140005.equals(billStatus)) {
			billStatusComm = "回购式转贴现赎回已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_140006.equals(billStatus)) {
			billStatusComm = "回购式转贴现赎回已签收";
		} else if (EDraftConstant.BILL_STATUS_150101.equals(billStatus)) {
			billStatusComm = "买断式再贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_150103.equals(billStatus)) {
			billStatusComm = "买断式再贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_150105.equals(billStatus)) {
			billStatusComm = "买断式再贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_150106.equals(billStatus)) {
			billStatusComm = "买断式再贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_150201.equals(billStatus)) {
			billStatusComm = "回购式再贴现待签收";
		} else if (EDraftConstant.BILL_STATUS_150203.equals(billStatus)) {
			billStatusComm = "回购式再贴现已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_150205.equals(billStatus)) {
			billStatusComm = "回购式再贴现已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_150206.equals(billStatus)) {
			billStatusComm = "回购式再贴现已签收";
		} else if (EDraftConstant.BILL_STATUS_150216.equals(billStatus)) {
			billStatusComm = "回购式再贴现已至赎回开放日";
		} else if (EDraftConstant.BILL_STATUS_150218.equals(billStatus)) {
			billStatusComm = "回购式再贴现已逾赎回截止日";
		} else if (EDraftConstant.BILL_STATUS_160001.equals(billStatus)) {
			billStatusComm = "回购式再贴现赎回待签收";
		} else if (EDraftConstant.BILL_STATUS_160003.equals(billStatus)) {
			billStatusComm = "回购式再贴现赎回已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_160005.equals(billStatus)) {
			billStatusComm = "回购式再贴现赎回已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_160006.equals(billStatus)) {
			billStatusComm = "回购式再贴现赎回已签收";
		} else if (EDraftConstant.BILL_STATUS_180001.equals(billStatus)) {
			billStatusComm = "质押待签收";
		} else if (EDraftConstant.BILL_STATUS_180006.equals(billStatus)) {
			billStatusComm = "质押已签收";
		} else if (EDraftConstant.BILL_STATUS_180020.equals(billStatus)) {
			billStatusComm = "质押已至票据到期日";
		} else if (EDraftConstant.BILL_STATUS_190001.equals(billStatus)) {
			billStatusComm = "质押解除待签收";
		} else if (EDraftConstant.BILL_STATUS_190006.equals(billStatus)) {
			billStatusComm = "质押解除已签收";
		} else if (EDraftConstant.BILL_STATUS_170001.equals(billStatus)) {
			billStatusComm = "保证待签收";
		} else if (EDraftConstant.BILL_STATUS_170002.equals(billStatus)) {
			billStatusComm = "保证已签收";
		} else if (EDraftConstant.BILL_STATUS_200001.equals(billStatus)) {
			billStatusComm = "提示付款待签收";
		} else if (EDraftConstant.BILL_STATUS_200003.equals(billStatus)) {
			billStatusComm = "提示付款已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_200005.equals(billStatus)) {
			billStatusComm = "提示付款已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_000000.equals(billStatus)) {
			billStatusComm = "票据已结清";
		} else if (EDraftConstant.BILL_STATUS_200312.equals(billStatus)) {
			billStatusComm = "提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）";
		} else if (EDraftConstant.BILL_STATUS_200412.equals(billStatus)) {
			billStatusComm = "提示付款已拒付（可拒付追索，可以追所有人）";
		} else if (EDraftConstant.BILL_STATUS_200512.equals(billStatus)) {
			billStatusComm = "提示付款已拒付（不可进行拒付追索）";
		} else if (EDraftConstant.BILL_STATUS_210001.equals(billStatus)) {
			billStatusComm = "逾期提示付款待签收";
		} else if (EDraftConstant.BILL_STATUS_210003.equals(billStatus)) {
			billStatusComm = "逾期提示付款已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_210005.equals(billStatus)) {
			billStatusComm = "逾期提示付款已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_210312.equals(billStatus)) {
			billStatusComm = "逾期提示付款已拒付（可拒付追索，只能追出票人，承兑人及其保证人）";
		} else if (EDraftConstant.BILL_STATUS_210412.equals(billStatus)) {
			billStatusComm = "逾期提示付款已拒付（可拒付追索，可以追所有人）";
		} else if (EDraftConstant.BILL_STATUS_220607.equals(billStatus)) {
			billStatusComm = "拒付追索待清偿";
		} else if (EDraftConstant.BILL_STATUS_230601.equals(billStatus)) {
			billStatusComm = "拒付追索同意清偿待签收";
		} else if (EDraftConstant.BILL_STATUS_230606.equals(billStatus)) {
			billStatusComm = "拒付追索同意清偿已签收";
		} else if (EDraftConstant.BILL_STATUS_220707.equals(billStatus)) {
			billStatusComm = "非拒付追索待清偿";
		} else if (EDraftConstant.BILL_STATUS_220710.equals(billStatus)) {
			billStatusComm = "非拒付追索已撤销";
		} else if (EDraftConstant.BILL_STATUS_230701.equals(billStatus)) {
			billStatusComm = "非拒付追索同意清偿待签收";
		} else if (EDraftConstant.BILL_STATUS_230706.equals(billStatus)) {
			billStatusComm = "非拒付追索同意清偿已签收";
		} else if (EDraftConstant.BILL_STATUS_250001.equals(billStatus)) {
			billStatusComm = "央行卖票待签收";
		} else if (EDraftConstant.BILL_STATUS_250003.equals(billStatus)) {
			billStatusComm = "央行卖票已签收待清算";
		} else if (EDraftConstant.BILL_STATUS_250005.equals(billStatus)) {
			billStatusComm = "央行卖票已签收已排队";
		} else if (EDraftConstant.BILL_STATUS_250006.equals(billStatus)) {
			billStatusComm = "央行卖票已签收";
		} else if (EDraftConstant.BILL_STATUS_000026.equals(billStatus)) {
			billStatusComm = "已逾票据权利失效日";
		} else {
			billStatusComm = billStatus;
		}
		return billStatusComm;
	}
	/**
	 * <br>
	 * <b>描述：</b>通过票据状态得到业务流水类型<br>
	 * <b>作者：</b>李杰<br>
	 * <b>日期：</b> 2013-08-08 <br>
	 */
	public static String converterBillStatusToBritfId(String billStatus) {
		String billStatusComm = null;
		if (EDraftConstant.BILL_STATUS_020001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_002;
		} else if (EDraftConstant.BILL_STATUS_030001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_003;
		} else if (EDraftConstant.BILL_STATUS_000002.equals(billStatus)) {
			billStatusComm = "结束已作废";
		} else if (EDraftConstant.BILL_STATUS_100001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_010;
		} else if (EDraftConstant.BILL_STATUS_110101.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_011;
		} else if (EDraftConstant.BILL_STATUS_110201.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_011;
		} else if (EDraftConstant.BILL_STATUS_180001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_018;
		} else if (EDraftConstant.BILL_STATUS_190001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_019;
		} else if (EDraftConstant.BILL_STATUS_170001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_017;
		} else if (EDraftConstant.BILL_STATUS_200001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_020;
		} else if (EDraftConstant.BILL_STATUS_210001.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_021;
		} else if (EDraftConstant.BILL_STATUS_220607.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_022;
		} else if (EDraftConstant.BILL_STATUS_230601.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_023;
		} else if (EDraftConstant.BILL_STATUS_220707.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_022;
		} else if (EDraftConstant.BILL_STATUS_230701.equals(billStatus)) {
			billStatusComm = EDraftConstant.BUSINESS_023;
		} else {
			billStatusComm = billStatus;
		}
		return billStatusComm;
	}
	
	
	
	public static String convertPayTypeToStr(String payType) {
		String str = null;
		if (EDraftConstant.PAY_TYPE_SELL.equals(payType)) {
			str = "卖方付息";
		} else if (EDraftConstant.PAY_TYPE_BUY.equals(payType)) {
			str = "买方付息";
		} else if (EDraftConstant.PAY_TYPE_DISCUSS.equals(payType)) {
			str = "协议付息";
		} else {
			str = payType;
		}
		
		return str;
	}
	
	public static String convertDiscountTypeToStr(String discountType) {
		String str = null;
		if (EDraftConstant.RPDMK_RM00.equals(discountType)) {
			str = "买断式";
		} else if (EDraftConstant.RPDMK_RM01.equals(discountType)) {
			str = "回购式";
		} else {
			str = discountType;
		}
		
		return str;
	}
	
	public static String convertDiscountTypeToCeb(String discountType) {
		String str = null;
		if (EDraftConstant.RPDMK_RM00.equals(discountType)) {
			str = "1";
		} else if (EDraftConstant.RPDMK_RM01.equals(discountType)) {
			str = "2";
		} else {
			str = discountType;
		}
		
		return str;
	}
	
	public static boolean isSignByBillStatus(String billStatus) {
		if (EDraftConstant.BILL_STATUS_020006.equals(billStatus)
				|| EDraftConstant.BILL_STATUS_030006.equals(billStatus)
				|| EDraftConstant.BILL_STATUS_100006.equals(billStatus)
				|| EDraftConstant.BILL_STATUS_120006.equals(billStatus)
				|| EDraftConstant.BILL_STATUS_180006.equals(billStatus)
				|| EDraftConstant.BILL_STATUS_190006.equals(billStatus)) {
			return true;
		}
		return false;
	}
}
