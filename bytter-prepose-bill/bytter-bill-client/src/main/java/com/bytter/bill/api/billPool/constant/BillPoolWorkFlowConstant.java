package com.bytter.bill.api.billPool.constant;
/**
 * 票据池工作流常量类
 * @author zhenghj 2020/01/13
 * 
 */
public class BillPoolWorkFlowConstant {
	
	/**  票据池--额度管理--额度分配 */
	public final static String AB_POOL_LIMITALLOT = "abPoolLimitAllot";
	/** 票据池--额度管理--额度分配核准  */
	public final static String AB_POOL_LIMITALLOT_CHECK = "abPoolLimitAllotCheck";
	/** 票据管理-电票签收审批流程 */
	public final static String AB_POLL_SIGN_APPLY = "abPoolSignApply";
	/** 票据管理-背书申请审批流程 */
	public final static String AB_POLL_ENDORSE_APPLY = "abPoolEndorseApply";
	/** 票据管理-贴现申请审批流程 */
	public final static String AB_POLL_DISCOUNT_APPLY = "abPoolDiscountApply";
	/** 保证金存入审批流 */
	public static final String AB_POOL_BAIL_DTL_IN_APPLY = "abPoolBailDtlInApply";
	/** 保证金提取审批流 */
	public static final String AB_POOL_BAIL_DTL_OUT_APPLY = "abPoolBailDtlOutApply";
	/** 融资申请审批流 */
	public static final String AB_POOL_EMITFINA_APPLY = "abPoolEmitfinaApply";
	/** 出票申请审批流 */
	public static final String AB_POOL_EMIT_APPLY = "abPoolEmitApply";
	/** 提示付款审批流 */
	public static final String AB_POOL_COLLECTION_APPLY = "abPoolCollectionApply";
	
	/*** 额度申领审批 */
	public final static String AB_POOL_LIMIT_APPLY = "abPoolLimitApply";
	/*** 出池审批申请流程 */
	public final static String AB_POLL_OUTAPPLY = "abPoolOutApply";
	/*** 额度申领核准审批流程定义 */
	public final static String AB_POLL_IMITAPPLY_CHECK = "abPoolLimitApplyCheck";
	/*** 入池申请审批流程定义 */
	public final static String AB_POLL_INTOAPPLY = "abPollIntoapply";
	/*** 入池撤销审批流程定义 */
	public final static String AB_POLL_INTOREPEAL = "abPollIntoRepeal";
	/*** 质押申请转换流程定义 */
	public final static String AB_POLL_PDSTS = "abPoolPdsTs";
}
