package com.bytter.bill.api.bill.constant;
/**
 * 票据工作流常量类
 * @author zhengg 2017/08/09
 *
 */
public class BillWorkFlowConstant {

	/**
	 * 应付票据-开票登记
	 */
	public final static String BILL_ACCEBILL_ABEMITBILL = "accebillAbEmitBill";

	/**
	 * 应付票据-开票补录
	 */
	public final static String BILL_ACCEBILL_ABEMITBILLSUPPLY = "accebillAbEmitBillSupply";
	/**
	 * 应付票据-退票申请
	 */
	public final static String BILL_ACCEBILL_ABEMITBILLBACK = "accebillAbEmitBillBack";

	/**
	 * 应付票据-兑付确认
	 */
	public final static String BILL_ACCEBILL_ABEMITBILLPAY = "accebillAbEmitBillPay";
	/**
	 * 票据删除
	 */
	public final static String BILL_ACCEBILL_ABBILLDELETE = "accebillAbBillDelete";




	/**
	 * 应收票据-收票登记
	 */
	public final static String BILL_ACCEBILL_RECEIVEBILL = "accebillReceiveBill";
	/**
	 * 应收票据-票据移存
	 */
	public final static String BILL_ACCEBILL_INVENTRANSFER = "accebillInvenTransfer";

	/**
	 * 应收票据-票据取票
	 */
	public final static String BILL_ACCEBILL_INVENFETCH = "accebillInvenFetch";
	/**
	 * 应收票据-票据背书
	 */
	public final static String BILL_ACCEBILL_ENDORSE = "accebillEndorse";
	/**
	 * 应收票据-背书请求
	 */
	public final static String BILL_ACCEBILL_ENDORSE_REQUEST = "accebillEndorseRequest";
	/**
	 * 应收票据-背书请求应答
	 */
	public final static String BILL_ACCEBILL_ENDORSE_RESPONSE = "accebillEndorseResponse";

	/**
	 * 应收票据-贴现申请
	 */
	public final static String BILL_ACCEBILL_DISCOUNTAPPLY="accebillDiscountApply";

	/**
	 * 应收票据-贴现办理
	 */
	public final static String BILL_ACCEBILL_DISCOUNTMANAGER = "accebillDiscountManager";

	/**
	 * 应收票据-贴现到账
	 */
	public final static String BILL_ACCEBILL_DISCOUNTACCOUNT = "accebillDiscountAccount";

	/**
	 * 应收票据-贴现收回
	 */
	public final static String BILL_ACCEBILL_DISCOUNTCOLLECT = "accebillDiscountCollect";

	/**
	 * 应收票据-贴现赎回申请
	 */
	public final static String BILL_ACCEBILL_DISCOUNTRANSOM = "accebillDiscountRansom";

	/**
	 * 票据划拨-票据上划
	 */
	public final static String BILL_ACCEBILL_ABBILLTRANSFERUPAPPLY = "abTransferUpApply";

	/**
	 * 票据划拨-票据归集
	 */
	public final static String BILL_ACCEBILL_ABBILLTRANSFERCOLLECTION = "abTransferCollection";

	/**
	 * 票据划拨-票据下拨申请
	 */
	public final static String BILL_ACCEBILL_ABBILLTRANSFERDOWNAPPLY = "accebillTransferDownApply";

	/**
	 * 票据划拨-票据下拨
	 */
	public final static String BILL_ACCEBILL_ABBILLTRANSFERDOWNMANAGE = "abTransferDownManage";

	/**
	 * 票据托收-托收申请
	 */
	public final static String BILL_ACCEBILL_ABBILLCOLLECTIONAPPLY = "abbillcollectionapply";

	/**
	 * 票据托收-托收到账
	 */
	public final static String BILL_ACCEBILL_ABBILLCOLLECTIONACCOUNT = "abbillcollectionaccount";

	/**
	 * 应收票据-应收退票
	 */
	public final static String BILL_ACCEBILL_ABRECEIVEBILLBACK = "accebillAbReceiveBillBack";


	/**
	 * 电票业务-票据签收
	 */
	public final static String BILL_ACCEBILL_SIGN = "accebillSign";
	/**
	 * 电票业务-签收补录
	 */
	public final static String BILL_ACCEBILL_SIGNSUPPLY = "accebillSignSupply";

	/**
	 * 电票业务-提示保证
	 */
	public final static String BILL_ACCEBILL_GUARANTEEAPPLY = "accebillGuaranteeApply";

	/**
	 * 电票业务-提示承兑
	 */
	public final static String BILL_ACCEBILL_ACCEPTAPPLY = "accebillAcceptApply";

	/**
	 * 电票业务-提示收票
	 */
	public final static String BILL_ACCEBILL_RECEIVEAPPLY = "accebillReceiveApply";

	/**
	 * 应收票据-票据质押申请
	 */
	public final static String BILL_ACCEBILL_PLEDGEAPPLY = "accebillPledgeApply";

	/**
	 * 应收票据-票据质押解除
	 */
	public final static String BILL_ACCEBILL_PLEDGERELIEVE = "accebillPledgeRelieve";

	/**
	 * 应收票据-票据质押收回
	 */
	public final static String BILL_ACCEBILL_PLEDGECOLLECT = "accebillPledgeCollect";

	/**
	 * 电票业务-通用撤回
	 */
	public final static String BILL_ACCEBILL_RECALL = "accebillRecall";

	/**
	 * 电票业务-追索通知
	 */
	public final static String BILL_ACCEBILL_SEARCHFORAPPLY = "accebillSearchforApply";

	/**
	 * 电票业务-追索清偿
	 */
	public final static String BILL_ACCEBILL_SEARCHFORMANAGE = "accebillSearchforManage";
	/**
	 * 电票业务-不可转让撤销
	 */
	public final static String BILL_NONASSIGNABLE_CANCEL = "nonassignableCancel";

}
