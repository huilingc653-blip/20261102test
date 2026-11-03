package com.bytter.biz.payment.api.constant;
import java.util.List;
/**
 * 第三方支付常量类
 *
 * @author zhengg
 * 20190926
 **/
public class TtPayConstant {
	// 第三方支付操作资源常量类
	/***	商户维护	操作资源		ttpay:merchant:op  **/
	public static final String TTPAY_RESOURCE_MERCHANT_OP = "ttpay:merchant:op";
	/***	参数设置	操作资源		ttpay:merchant:op  **/
	public static final String TTPAY_CONFIG_OPERATION = "ttpay:config:operation";
	/***	商户类别维护	查看资源		  **/
	public static final String TTPAY_SHOPTYPE_VIEW = "ttpay:shop:type:view";
	/***	商户类别维护	编辑资源		  **/
	public static final String TTPAY_SHOPTYPE_EDIT = "ttpay:shop:type:edit";
	/***	商户类别维护	删除资源		  **/
	public static final String TTPAY_SHOPTYPE_DEL = "ttpay:shop:type:del";
	/***	商户类别维护	新增资源		  **/
	public static final String TTPAY_SHOPTYPE_ADD = "ttpay:shop:type:add";
	/***	商户维护	查看资源		  **/
	public static final String TTPAY_SHOP_BUSSNISS_VIEW = "ttpay:shop:bussniss:view";
	/***	商户维护	查看资源		  **/
	public static final String TTPAY_QUERY_SHOP_OP = "ttpay:query:shop:op";
	/***	商户维护	编辑资源		  **/
	public static final String TTPAY_SHOP_BUSSNISS_EDIT = "ttpay:shop:bussniss:edit";
	/***	商户维护	删除资源		  **/
	public static final String TTPAY_SHOP_BUSSNISS_DEL = "ttpay:shop:bussniss:del";
	/***	商户维护	新增资源		  **/
	public static final String TTPAY_SHOP_BUSSNISS_ADD = "ttpay:shop:bussniss:add";
	/***	对账单查询	操作资源		ttpay:billjob:op  **/
	public static final String TTPAY_RESOURCE_BILLJOB_OP = "ttpay:billjob:op";
	/***	流水查询	操作资源		ttpay:billjob:op  **/
	public static final String TTPAY_RESOURCE_PAYDTL_OP = "ttpay:paydtl:op";
	/***	余额查询	操作资源		ttpay:billjob:op  **/
	public static final String TTPAY_RESOURCE_PAYBAL_OP = "ttpay:paybal:op";
	/***	退款申请	操作资源		ttpay:refundapply:op  **/
	public static final String TTPAY_RESOURCE_REFUNDAPPLY_OP = "ttpay:refundapply:op";
	/***	退款确认	操作资源		ttpay:refundcheck:op  **/
	public static final String TTPAY_RESOURCE_REFUNDCHECK_OP = "ttpay:refundcheck:op";
	/***	退款查询	操作资源		ttpay:refundquery:op  **/
	public static final String TTPAY_RESOURCE_REFUNDQUERY_OP = "ttpay:refundquery:op";
	/***	退款查询	重新发起和打回		ttpay.refundquery.relaunch  **/
	public static final String TTPAY_RESOURCE_REFUNDQUERY_RELAUNCH = "ttpay.refundquery.relaunch";
	/***	商户类别维护	查看资源		  **/
	public static final String TTPAY_PAYTYPE_VIEW = "tp:shop:type:view";
	/***	商户类别维护	编辑资源		  **/
	public static final String TTPAY_PAYTYPE_EDIT = "tp:shop:type:edit";
	/***	商户类别维护	删除资源		  **/
	public static final String TTPAY_PAYTYPE_DEL = "tp:shop:type:del";
	/***	商户类别维护	新增资源		  **/
	public static final String TTPAY_PAYTYPE_ADD = "tp:shop:type:add";
	/***	限额管理	操作资源		ttpay:quota:op  **/
	public static final String TTPAY_RESOURCE_QUOTA_OP = "ttpay:quota:op";
	/***	限额管理	查询资源		ttpay:quota:query  **/
	public static final String TTPAY_RESOURCE_QUOTA_QUERY = "ttpay:quota:query";
	/***	限额查询管理	查询资源		ttpay:quota:query  **/
	public static final String TTPAY_RESOURCE_QUOTAQUERY_QUERY = "ttpay:quotaQuery:query";
//第三方支付 流水认领资源
	/*** 第三方支付 查看资源  **/
	public static final String TTPAY_RESOURCE_BISDTLCLAIM_VIEW = "ttpay:bisdtl:claim:view";
	/*** 第三方支付 操作资源  **/
	public static final String TTPAY_RESOURCE_BISDTLCLAIM_OP = "ttpay:bisdtl:claim:op";
	//第三方支付 资源
	/*** 第三方支付 查询资源  **/
	public static final String TTPAY_RESOURCE_PAYMENT_QUERY = "ttpay:tpPayment:query";
	/*** 第三方支付 制单资源  **/
	public static final String TTPAY_RESOURCE_PAYMENT_ADD = "ttpay:tpPayment:add";
	/*** 第三方支付 修改资源  **/
	public static final String TTPAY_RESOURCE_PAYMENT_UPDATE = "ttpay:tpPayment:update";
	/*** 第三方支付 删除资源  **/
	public static final String TTPAY_RESOURCE_PAYMENT_DELETE = "ttpay:tpPayment:delete";
	/*** 第三方支付 批量提交资源  **/
	public static final String TTPAY_RESOURCE_PAYMENT_SUBMITBATCH = "ttpay:tpPayment:submitBatch";
	/*** 第三方支付查询 查询资源  **/
	public static final String TTPAY_RESOURCE_PAYMENTQUERY_QUERY = "ttpay:tpPaymentQuery:query";
	/*** 第三方支付查询 退回资源  **/
	public static final String TTPAY_RESOURCE_PAYMENTQUERY_RECALL = "ttpay:tpPaymentQuery:recall";
	// TTPAY_PAY_TYPE 第三方支付缴款方式
	/** 第三方支付方式**/
	public static final String TTPAY_PAYTYPE_DICT= "TTPAY_PAY_TYPE";
	/*** 第三方支付缴款方式 002 支付宝 **/
	public static final String TTPAY_PAYTYPE_002 = "002";
	/*** 第三方支付缴款方式 003 微信 **/
	public static final String TTPAY_PAYTYPE_003 = "003";
	// TTPAY_BILLJOB_STATUS 第三方支付支付账单同步状态
	/** 第三方支付支付账单同步状态**/
	public static final String TTPAY_BILLJOB_STATUS_DICT= "TTPAY_BILLJOB_STATUS";
	/*** 第三方支付支付账单同步状态 -1	未开始 **/
	public static final String TTPAY_BILLJOB_STATUS_F1 = "-1";
	/*** 第三方支付支付账单同步状态0	执行失败 **/
	public static final String TTPAY_BILLJOB_STATUS_0 = "0";
	/*** 第三方支付支付账单同步状态1	执行成功 **/
	public static final String TTPAY_BILLJOB_STATUS_1 = "1";
//	是否具备直连接口 1是0否
	/*** 是否具备直连接口 1是 **/
	public static final String TTPAY_ISONLINE1 = "1";
	/*** 是否具备直连接口 0否 **/
	public static final String TTPAY_ISONLINE0 = "0";
//	有效无效标记
	/*** 有效无效标记 1是 **/
	public static final String TTPAY_VALIDSIGN1 = "1";
	/*** 有效无效标记 0否 **/
	public static final String TTPAY_VALIDSIGN0 = "0";
//	字段用途：1列表2查看明细3合计多个用,隔开
	/*** 字段用途 1列表 **/
	public static final String TTPAY_BILLVIEWDTL_FIELDTYPE1 = "1";
	/*** 字段用途 2查看明细 **/
	public static final String TTPAY_BILLVIEWDTL_FIELDTYPE2 = "2";
	/*** 字段用途 3合计**/
	public static final String TTPAY_BILLVIEWDTL_FIELDTYPE3 = "3";
	/*** 字段用途 0公共列表**/
	public static final String TTPAY_BILLVIEWDTL_FIELDTYPE0 = "0";
//	第三方支付日志类型 TP_LOG_TYPE
	/*** 01	新增 **/
	public static final String TTPAY_LOGTYPE_ADD = "01";
	/*** 02	修改 **/
	public static final String TTPAY_LOGTYPE_UPDATE = "02";
	/*** 03	删除 **/
	public static final String TTPAY_LOGTYPE_DELETE = "03";
	/*** 04	启用 **/
	public static final String TTPAY_LOGTYPE_START = "04";
	/*** 05	停用 **/
	public static final String TTPAY_LOGTYPE_STOP = "05";
	/*** 07	导入 **/
	public static final String TTPAY_LOGTYPE_IMP = "07";
	/*** -1	打回 **/
	public static final String TTPAY_LOGTYPE_RETURN = "-1";
	/*** -3	撤回 **/
	public static final String TTPAY_LOGTYPE_WITHDRAW = "-3";
	/*** -4	退回 **/
	public static final String TTPAY_LOGTYPE_RECALL = "-4";
	/*** 30	提交 **/
	public static final String TTPAY_LOGTYPE_SUBMIT = "30";
	/*** 95 审批 **/
	public static final String TTPAY_LOGTYPE_APPROVE = "95";
	/*** 00 确认 **/
	public static final String TTPAY_LOGTYPE_CHECK = "00";
	// TTPAY_BILLJOB_STATUS 退款审批状态
	/** 退款审批状态**/
	public static final String TTP_REFUND_APPROVE_STATUS_DICT= "TTP_REFUND_APPROVE_STATUS";
	/*** 退款审批状态 0	未申请 **/
	public static final String TTP_REFUND_APPROVE_STATUS_0 = "0";
	/*** 退款审批状态	11 审批中 **/
	public static final String TTP_REFUND_APPROVE_STATUS_11 = "11";
	// TTPAY_BILLJOB_STATUS 退款审批状态
	/** 退款确认状态**/
	public static final String TTPAY_REFUND_CHECKSTATUS_DICT= "TTPAY_REFUND_CHECKSTATUS";
	/*** 退款确认状态 00	待处理 **/
	public static final String TTPAY_REFUND_CHECKSTATUS_00 = "00";
	/*** 退款确认状态	-1 确认打回 **/
	public static final String TTPAY_REFUND_CHECKSTATUS_F1 = "-1";
	/*** 退款确认状态 95	确认通过 **/
	public static final String TTPAY_REFUND_CHECKSTATUS_95 = "95";
	/** 新增成功！**/
	public static final String COMMON_MESSAGE_ADDSUCCESS = "common.message.add.success";
	/** 导入成功！**/
	public static final String COMMON_MESSAGE_IMPSUCCESS = "common.message.imp.success";
	/** 提交成功！**/
	public static final String COMMON_MESSAGE_SUBMITSUCCESS = "common.message.submit.success";
	/** 审批通过！**/
	public static final String COMMON_MESSAGE_APPROVESUCCESS = "common.message.approve.success";
	/** 审批打回！**/
	public static final String COMMON_MESSAGE_APPROVEBACK = "common.message.approve.back";
	/** * 撤回！ */
	public static final String COMMON_MESSAGE_WITHDRAW = "nis.log.withdraw.succ";
	/** * 退回！ */
	public static final String COMMON_MESSAGE_RECALL = "common.message.recall";
	/** 确认打回！**/
	public static final String COMMON_MESSAGE_CHECKBACK = "common.message.checkback";
	/** 确认通过！**/
	public static final String COMMON_MESSAGE_CHECKPASS = "common.message.checkpass";
	/** 失败打回！**/
	public static final String COMMON_MESSAGE_FAILBACK = "common.message.failback";
	/** 指令重新发起！**/
	public static final String COMMON_MESSAGE_RELAUNCH = "common.message.relaunch";
	/** 修改成功！**/
	public static final String COMMON_MESSAGE_UPDATESUCCESS = "common.message.update.success";
	/** 修改在线离线标记成功！**/
	public static final String COMMON_MESSAGE_ISONLINE_UPDATESUCCESS = "common.message.isonline.update.success";
	/** 删除成功！**/
	public static final String COMMON_MESSAGE_DELETESUCCESS = "common.message.delete.success";
	/** * 所属单位代码和所属单位不能同时为空！*/
	public static final String TTPAY_MESSAGE_CORP_CODEANDNAME_NOTNULL= "ttpay.message.corp.codeandname.notnull";
	/** 所属单位代码不能为空 */
	public static final String TTPAY_EXCEL_ERRORMSG_CORPCODE_NULL = "ttpay.message.corpCode.null";
	/** 所属单位代码错误 */
	public static final String TTPAY_EXCEL_ERRORMSG_CORPCODE_ERROR = "ttpay.message.corpCode.error";
	/** 没有此错误的操作权限！ */
	public static final String TTPAY_EXCEL_ERRORMSG_CORPAUTH_ERROR = "ttpay.message.corpAuth.error";
	/** 所属单位代码与所属单位名称不匹配 */
	public static final String TTPAY_EXCEL_ERRORMSG_CORPNAME_ISNOT = "ttpay.bill.message.corpName.isnot";
	/*** 所属单位不存在 */
	public static final String TTPAY_MESSAGE_CORPID_ISNOTEXIST = "ttpay.message.corpid.isnotexist";
	/*** 主商户号不存在 */
	public static final String TTPAY_MESSAGE_MERCHANTNO_ISNOTEXIST = "ttpay.message.merchantno.isnotexist";
	/*** 商户号重复 */
	public static final String TTPAY_MESSAGE_MERCHANTNO_REP = "ttpay.message.merchantno.rep";
	/*** 第三方支付方式不存在 */
	public static final String TTPAY_MESSAGE_PAYTYPE_ISNOTEXIST = "ttpay.message.paytype.isnotexist";
	/** 商户号不能为空 */
	public static final String TTPAY_EXCEL_ERRORMSG_MERCHANTNO_NULL = "ttpay.message.merchantno.null";
	/** 第三方支付不能为空 */
	public static final String TTPAY_EXCEL_ERRORMSG_PAYTYPE_NULL = "ttpay.message.paytype.null";
	public static String TTPAY_MAP_KEYSEPARATORCHARS=";,;";
	/**
	 * ","分隔符拼接
	 */
	public static String TTPAY_SEPARATORCHARS=",";
	// 退款申请审批状态
	/** 未申请 */
	public static final String TTPAY_REFUND_APPLY_STATUS0 = "0";
	/** 打回 */
	public static final String TTPAY_REFUND_APPLY_STATUS_1 = "-1";
	/** 审批中 */
	public static final String TTPAY_REFUND_APPLY_STATUS11 = "11";
	/** 审批完成 */
	public static final String TTPAY_REFUND_APPLY_STATUS95 = "95";
	// 退款复核状态
	/** 待处理 */
	public static final String TTPAY_REFUND_CHECK_STATUS00 = "00";
	/** 已打回 */
	public static final String TTPAY_REFUND_CHECK_STATUS_1 = "-1";
	/** 复核完成 */
	public static final String TTPAY_REFUND_CHECK_STATUS95 = "95";
	// 银行接口发送状态
	/** 审批打回 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS_R = "R";
	/** 审批中 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS11 = "11";
	/** 复核打回 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS_2 = "-2";
	/** 待复核 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS12 = "12";
	/** 处理失败 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS2 = "2";
	/** 处理成功 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS0 = "0";
	/** 待发送 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS3 = "3";
	/** 处理中 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS_S = "S";
	/** 指令打回 */
	public static final String TTPAY_REFUND_VOUCHER_STATUS_1 = "-1";
	// 确认操作
	/** 确认通过 */
	public static final String TTPAY_REFUND_OPTYPE_CHECK = "check";
	/** 确认打回 */
	public static final String TTPAY_REFUND_OPTYPE_BACK = "back";
	/** 重新发起 */
	public static final String TTPAY_REFUND_OPTYPE_RELAUNCH = "reLaunch";
	/** 系统内 */
	public static final String TTPAY_REFUND_APPLY_BFS = "BFS";
	/** 退款申请审批流 */
	public static final String TP_REFUND_ORDER_APPLY = "tpRefundOrderApply";
	/** 应用id参数代码集合: 微信和支付宝 */
	public static final String[] APPIDS = {"aliPay.appId", "weixinPay.appId"};
	//查询规则（日01\月02）
	/*** 查询规则 01 日*/
	public static final String TTPAY_QUERY_TYPE_DAY = "01";
	/*** 查询规则 月*/
	public static final String TTPAY_QUERY_TYPE_MONTH = "02";
	/** 第三方支付参数 所属类型（tp 第三方支付） */
	public static final String TP_CONFIG_TYPE="tp";
	/** 第三方支付参数 是否挂接计划 1 是 */
	public static final String TP_CONFIG_BUDGETSEL = "budgetSel";
	/** 第三方支付参数  业务类别 */
	public static final String TP_PAY_TYPE = "payType";
	/*** 第三方支付参数：payItem-付款默认走的计划科目  */
	public static final String TP_CONFIG_PAYITEM = "payItem";
	/*** 第三方支付参数：incItem-收款方式默认走的计划科目  */
	public static final String TP_CONFIG_INCITEM = "incItem";
	/*** 第三方支付参数：bankaccOver-银行账户余额不足  */
	public static final String TP_CONFIG_BANKACCOVER = "bankaccOver";
	/*** 支付结算参数配置：balControlType-付款余额控制方式  */
	public static final String TP_CONFIG_BALCONTROLTYPE = "balControlType";
	/*** 支付结算参数配置：bankaccOverApprove-付款时银行账户余额不足  */
	public static final String TP_CONFIG_BANKACCOVER_APPROVE = "bankaccOverApprove";
}
