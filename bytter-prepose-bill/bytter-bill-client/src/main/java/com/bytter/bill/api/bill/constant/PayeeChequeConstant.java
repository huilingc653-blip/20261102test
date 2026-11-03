package com.bytter.bill.api.bill.constant;
/**
 *
 * @Title: PayeeChequeConstant
 * @Description:  支票模块常量类
 * @author Jinyong.Cao
 * @date 2018-06-13 13:06:40
 * @version 1.0
 *
 */
public class PayeeChequeConstant {
	/*** 支票收款登记查询资源*/
	public static final String AB_BILL_RESOURCE_BVPAYEECHEQUE_QUERY = "bill:blankvou:bvPayeeCheque:query";
	/*** 支票复核查询资源*/
	public static final String AB_BILL_RESOURCE_BVPAYEECHEQUECHECK_QUERY = "bill:blankvou:bvPayeeChequeCheck:query";
	/*** 支票台账查询资源*/
	public static final String AB_BILL_RESOURCE_BVPAYEECHEQUEMANAGE_QUERY = "bill:blankvou:bvPayeeChequeManage:query";
	/*** 支票收款登记资源--已登记(REGISTERED)*/
	public static final int BVVOUINPUT_STATUS_REGISTERED = 0;
	/*** 支票收款登记资源--复核中(RECHECK)*/
	public static final int BVVOUINPUT_STATUS_RECHECK = 1;
	/*** 支票收款登记操作-修改(edit)*/
	public static final String BILL_OPERATE_EDIT = "edit";
	/*** 支票收款登记操作-新增(INPUT)*/
	public static final String BVLOG_LOGTYPE_INPUT = "INPUT";
	/*** 支票收款登记操作-新增(INPUT)*/
	public static final String BVLOG_LOGTYPE_CHECK = "CHECK";
	/*** 支票收款管理日志-入库(06)*/
	public static final String PAYEE_CHEQUE_INPUT="06";
	/*** 支票收款管理-03删除*/
	public static final String BVLOG_LOGTYPE_DELETE="03";
	/*** 复核打回  */
	public static final String BV_CHEQUE_TYPE_BACK="BACK";
	/*** 复核通过  */
	public static final String BV_CHEQUE_TYPE_CHECK="CHECK";
	/*** 批量打回  */
	public static final String BV_CHEQUE_TYPE_BATHBACK="BATH_BACK";
	/*** 批量复核通过  */
	public static final String BV_CHEQUE_TYPE_BATHCHECK="BATH_CHECK";
	/*** 支票台账-作废  */
	public static final String BV_CHEQUE_TYPE_TOVOID="TOVOID";
	/*** 支票台账-批量作废  */
	public static final String BV_CHEQUE_TYPE_BATHTOVOID="BATHTOVOID";
	/*** 支票台账-退票*/
	public static final String BV_CHEQUE_TYPE_RETREAT="RETREAT";
	/*** 支票台账-批量退票  */
	public static final String BV_CHEQUE_TYPE_BATHRETREAT="BATHRETREAT";
	/*** 支票台账-托收*/
	public static final String BV_CHEQUE_TYPE_COLLECTION="COLLECTION";
	/*** 支票台账-到账*/
	public static final String BV_CHEQUE_TYPE_ARRIVALACCOUNT="ARRIVALACCOUNT";
	/*** 支票台账-批量到账  */
	public static final String BV_CHEQUE_TYPE_BATHARRIVALACCOUNT="BATHARRIVALACCOUNT";
	/*** 国际化取值(**/
	/**单位代码不存在！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_CORPCODE_ISERROR="blankvou.bvPayeeCheque.corpcode.message.iserror";
	/** 票据号码不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_ORIGNO_ISERROR="blankvou.bvPayeeCheque.origno.message.iserror";
	/** 票据金额不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_BILLMONEY_ISERROR="accebill.message.error.billmoney.isnotnull";
	/** 购入日期不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_INPUTDATE_ISERROR="blankvou.bvvouinput.inputdate.message.isnotnull";
	/** 付款人不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYER_ISERROR="blankvou.bvPayeeCheque.payer.message.iserror";
	/** 付款人账户不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYERACC_ISERROR="blankvou.bvPayeeCheque.payerAcc.message.iserror";
	/** 付款人银行账户不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYERBANKNO_ISERROR="blankvou.bvPayeeCheque.payerBankNo.message.iserror";
	/** 付款人开户行名称不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYERBANKNAME_ISERROR="blankvou.bvPayeeCheque.payerBankName.message.iserror";
	/** 收款人不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYEE_ISERROR="blankvou.bvPayeeCheque.payee.message.iserror";
	/** 收款人账户不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYEEACC_ISERROR="blankvou.bvPayeeCheque.payeeAcc.message.iserror";
	/** 收款人银行账户不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYEEBANKNO_ISERROR="blankvou.bvPayeeCheque.payeeBankNo.message.iserror";
	/** 收款人开户行名称不能为空！*/
	public static final String BLANKVOU_BVPAYEECHEQUE_PAYEEBANKNAME_ISERROR="blankvou.bvPayeeCheque.payeeBankName.message.iserror";
	/**复核成功！*/
	public static final String LANG_LOG_CHECK_SUCC="bill.log.check.succ";
	/**复核打回！*/
	public static final String LANG_LOG_BACK_SUCC="bill.log.back.succ";
}
