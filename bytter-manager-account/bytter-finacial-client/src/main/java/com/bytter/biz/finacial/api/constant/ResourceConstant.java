package com.bytter.biz.finacial.api.constant;
/**
 * 核算模块资源权限
 * @author liyh
 * @since  2017-7-6
 */
public class ResourceConstant{
	
	
	/**柜台账务查询权限**/
	public static final String FCL_ACCBOOK_COMMON_QUERY = "fcl:accbook:common:query";
	/**柜台账务记账权限**/
	public static final String FCL_ACCBOOK_COMMON_APPLY = "fcl:accbook:common:apply";
	
	/**表外账务查询权限**/
	public static final String FCL_OUTACCBOOK_COMMON_QUERY = "fcl:outaccbook:common:query";
	/**表外账务记账权限**/
	public static final String FCL_OUTACCBOOK_COMMON_APPLY = "fcl:outaccbook:common:apply";
	
	/**期终处理查询权限**/
	public static final String FCL_CLOSE_COMMON_QUERY = "fcl:close:common:query";
	/**期终处理结转权限**/
	public static final String FCL_CLOSE_COMMON_APPLY = "fcl:close:common:apply";
	/**期终处理结转权限**/
	public static final String FCL_CLOSE_POOL_APPLY = "fcl:close:pool:apply";
	/**期终处理结转权限**/
	public static final String FCL_CLOSE_POOLQUERY = "fcl:close:pool:query";
	
    /**期终处理本年利润记账权限**/
    public static final String FCL_CLOSE_FCCARRY_APPLY = "fcl:close:fccarry:apply";
    /**期终处理本年利润复核权限**/
    public static final String FCL_CLOSE_FCCARRY_CHECK = "fcl:close:fccarry:check";
	/**期终处理日初权限**/
	public static final String FCL_START_COMMON_APPLY = "fcl:start:common:apply";
	
	/**利息查询权限**/
	public static final String FCL_INTEREST_COMMON_QUERY = "fcl:interest:common:query";
	/**批量计息权限**/
	public static final String FCL_INTEREST_COMMON_APPLY = "fcl:interest:common:apply";
	/**资金池批量计息权限**/
	public static final String FCL_INTEREST_POOL_APPLY = "fcl:interest:pool:apply";
	/**资金池利息查询**/
	public static final String FCL_INTEREST_POOL_QUERY = "fcl:interest:pool:query";
	
	/**对账查询权限**/
	public static final String FCL_CHECK_COMMON_QUERY = "fcl:check:common:query";
	/**对账结转权限**/
	public static final String FCL_CHECK_COMMON_APPLY = "fcl:check:common:apply";
	
	/**银企接口收款查询权限**/
	public static final String FCL_BT_NET_CHALK_COMMON_QUERY = "fcl:btnetchalk:common:query";
	/**银企接口收款记账权限**/
	public static final String FCL_BT_NET_CHALK_COMMON_APPLY = "fcl:btnetchalk:common:apply";
	/**银企接口收款复核权限**/
	public static final String FCL_BT_NET_CHALK_COMMON_CHECK = "fcl:btnetchalk:common:check";
	
	/**中心申请付款查询权限**/
    public static final String NETBANK_BDLINK_BISEXC_SALEPAYMENTQUERY = "netbank:bdLink:bisExc:salePaymentQuery";
	/**中心申请付款记账权限**/
	public static final String FCL_CENTERPAYMENT_COMMON_APPLY = "fcl:centerpayment:common:apply";

	/**委托贷款记账权限**/
	public static final String FCL_PRECATIVE_TALLY_APPLY = "fcl:precative:tally:apply";
	/**委托贷款记账复核权限**/
	public static final String FCL_PRECATIVE_TALLY_CHECK = "fcl:precative:tally:check";

	/**银行借款记账权限**/
	public static final String FCL_PROVIDE_TALLY_APPLY = "fcl:provide:tally:apply";
	/**内部贷款记账权限**/
	public static final String FCL_LOAN_TALLY_APPLY = "fcl:loan:tally:apply";
	/**资金调拨、单位申请付款记账权限**/
	public static final String FCL_NISALLOCATIONREQ_TALLY_APPLY = "fcl:nisallocationreq:tally:apply";
	/***承兑汇票记账权限***/
	public static final String FINACIAL_ABBILLTALLY_APPLY = "finacial:abbilltally:apply";
	/**网点间来账记账权限**/
	public static final String FCUNIONACCBOOK_FCUNIONACCBOOK_APPLY = "fcunionaccbook:fcunionaccbook:apply";
	/**资金池记账权限**/
	public static final String FCL_POOL_TALLY_APPLY = "fcl:pool:tally:apply";
	/***内部存款记账权限***/
	public static final String FINACIAL_INNERDEPOSIT_APPLY = "finacial:innerdeposit:apply";
	/**对账单打印**/
	public static final String FINACIAL_ACCOUNT_STATEMENT_PRINT = "finacial:account:statement:print";
	/**分户账查询*/
	public static final String FINACIAL_FCLEDGER_QUERY = "finacial:fcledger:query";
	/**明细账查询*/
	public static final String FINACIAL_FCBOOKDTL_QUERY = "finacial:fcbookdtl:query";
	/**流水账明细查询*/
	public static final String FINACIAL_FINACIALDTL_QUERY = "finacial:finacialdtl:query";
	/**手续费结转*/
	public static final String FINACIAL_ACCRUALRECORD_QUERY = "finacial:accrualrecord:query";
	/**损益表查询*/
	public static final String FINACIAL_INCOMSTATEMEN_QUERY = "finacial:IncomStatemen:query";
	/**资产负债表查询*/
	public static final String FINACIAL_BANLANCESHEET_QUERY = "finacial:banlanceSheet:query";
	/**现金流量表查询*/
	public static final String FINACIAL_CASHFLOW_QUERY = "finacial:cashFlow:query";
	/**企业活期存款个别计息-经办*/
	public static final String FINACIAL_INTEREST_SINGLE_DDA_HAND = "finacial:interest:dda:hand";
	/**企业活期存款个别计息-复核*/
	public static final String FINACIAL_INTEREST_SINGLE_DDA_CHECK = "finacial:interest:dda:check";
	/**企业定期存款个别计息-经办*/
	public static final String FINACIAL_INTEREST_SINGLE_TDA_HAND = "finacial:interest:tda:hand";
	/**企业定期存款个别计息-复核*/
	public static final String FINACIAL_INTEREST_SINGLE_TDA_CHECK = "finacial:interest:tda:check";
	/**企业通知存款个别计息-经办*/
	public static final String FINACIAL_INTEREST_SINGLE_NOD_HAND = "finacial:interest:nod:hand";
	/**企业通知存款个别计息-复核*/
	public static final String FINACIAL_INTEREST_SINGLE_NOD_CHECK = "finacial:interest:nod:check";
	/**企业通知存款个别计息-通知预约-查询权限*/
	public static final String FINACIAL_INTEREST_SINGLE_NOD_BOOKING_QUERY = "finacial:interest:nod:booking:query";
	/**企业通知存款个别计息-通知预约-应用权限*/
	public static final String FINACIAL_INTEREST_SINGLE_NOD_BOOKING_APPLY = "finacial:interest:nod:booking:apply";
	/**企业通知存款个别计息-通知预约-取消通知预约*/
	public static final String FINACIAL_INTEREST_SINGLE_NOD_BOOKING_UN_BOOKING = "finacial:interest:nod:booking:unbooking";
	/**企业内部贷款个别计息-经办*/
	public static final String FINACIAL_INTEREST_SINGLE_LOAN_HAND = "finacial:interest:loan:hand";
	/**企业内部贷款个别计息-复核*/
	public static final String FINACIAL_INTEREST_SINGLE_LOAN_CHECK = "finacial:interest:loan:check";
	/**个别计息-利息明细查询*/
	public static final String FINACIAL_INTEREST_SINGLE_QUERY = "finacial:interest:single:query";
	/**银行未达账初始化查询资源*/
	public static final String FINACIAL_CHECK_FCBANKBILLTEMP_APPLY = "finacial:check:fcbankbilltemp:apply";
	/**ERP未达账初始化查询资源*/
	public static final String FINACIAL_CHECK_FCCOMPAREACCTEMP_APPLY = "finacial:check:fcCompareAccTemp:apply";
	/**利息管理参数应用资源*/
	public static final String FINACIAL_BASICDATA_INTEREST_APPLY = "finacial:basicdata:interest:apply";
	/**利科目利率维护应用资源*/
	public static final String FINACIAL_BASICDATA_FCINSTRATE_APPLY = "finacial:basicdata:fcinstrate:apply";
	/**摘要维护应用资源*/
	public static final String FINACIAL_BASICDATA_BTSUMMARY_APPLY = "finacial:basicdata:btsummary:apply";
	/**月结区间应用资源*/
	public static final String FINACIAL_BASICDATA_FCSETMONTH_APPLY = "finacial:basicdata:fcsetmonth:apply";
	/**业务种类管理应用资源*/
	public static final String FINACIAL_BASICDATA_FCBUSRACE_APPLY = "finacial:basicdata:fcbusrace:apply";
	/**记账规则管理应用资源*/
	public static final String FINACIAL_BASICDATA_FCBUSINESSRULEDEFINE_APPLY = "finacial:basicdata:fcbusinessruledefine:apply";
	/**手续费类型管理应用资源*/
	public static final String FINACIAL_BASICDATA_FCCHARGETYPE_APPLY = "finacial:basicdata:fcchargetype:apply";
	/**报表格式定义应用资源*/
	public static final String FINACIAL_BASICDATA_FCREPORTFORMAT_APPLY = "finacial:basicdata:fcreportformat:apply";
	/**核对标准维护应用资源*/
	public static final String FINACIAL_BASICDATA_FCCOMPARECHECKTYPE_APPLY = "finacial:basicdata:fccomparechecktype:apply";
	/**现金流量表项目维护应用资源*/
	public static final String FINACIAL_BASICDATA_CASHITEM_APPLY = "finacial:basicdata:cashItem:apply";
	/**现金流量表项目科目关系维护应用资源*/
	public static final String FINACIAL_BASICDATA_VOUCHERCASH_APPLY = "finacial:basicdata:voucherCash:apply";
	/**日结应用资源*/
	public static final String FINACIAL_CLOSEDEAL_TOPOOLDAYCLOSE_APPLY = "finacial:closedeal:toPoolDayClose:apply";
	/**反日结应用资源*/
	public static final String FINACIAL_CLOSEDEAL_TOREPOOLDAYCLOSE_APPLY = "finacial:closedeal:torePoolDayClose:apply";
}
