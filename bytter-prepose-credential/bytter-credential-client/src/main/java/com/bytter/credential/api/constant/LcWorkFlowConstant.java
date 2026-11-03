package com.bytter.credential.api.constant;
/**
 * <br>
 * <b>类功能：</b>信用证工作流常量类<br>
 */
public class LcWorkFlowConstant {
	/**收到信用证入库申请*/
	public final static String LC_RECEIVE_STORAGE = "lcReceiveStorage";
	/**收到信用证交单申请*/
	public final static String LC_RECEIVE_ARRIVAL = "lcReceiveArrival";
	/**收到信用证到期收款申请*/
	public final static String LC_RECEIVE_INCOMING = "lcReceiveIncoming";
	/**收到信用证出口押汇申请*/
	public final static String LC_RECEIVE_MORTGAGE = "lcReceiveMortgage";
	/*信用证开证申请*/
	public final static String LC_CREATE_CREDIT = "lcCreateCredit";
	/*信用证补录申请*/
	public final static String LC_SUPPLEMENT_CREDIT = "lcSupplementCredit";
	/*付汇申请*/
	public final static String LC_PAYMENT = "lcPayment";
	/*押汇申请*/
	public final static String LC_DEPOSIT = "lcDeposit";
	/*展期申请*/
	public final static String LC_DEPOSIT_DELAY = "lcDepositDelay";
}
