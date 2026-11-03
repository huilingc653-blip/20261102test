package com.bytter.bill.api.billPool.constant;
/**
 * 票据池模块常量类
 *
 * @author zhenghj
 *
 */
public class AbPoolConstant {
	
	// 保证金存取类型
	/** 保证金存入 1 **/
	public static final String BAIL_TYPE_IN = "1";
	/** 保证金取出 2 **/
	public static final String BAIL_TYPE_OUT = "2";
	
    /*** 状态：0 禁用 */
    public static final String AB_POLL_VALID_SIGN_0 = "0";
    /*** 状态：1 启用 */
    public static final String AB_POLL_VALID_SIGN_1 = "1";
    /*** 状态：2 删除 */
    public static final String AB_POLL_VALID_SIGN_2 = "2";
    /** 是否票据入池 */
    /*** 0 否 */
    public static final String AB_POOL_IS_INTO_0 = "0";
    /*** 1 是 */
    public static final String AB_POOL_IS_INTO_1 = "1";
    /*** 票据池类型 AB_POOL_TYPE*/
    /*** 结算中心 1 */
    public static final String AB_BILL_POOL_BP_TYPE_1 = "1";
    /*** 财务公司 2 */
    public static final String AB_BILL_POOL_BP_TYPE_2 = "2";
    /*** 银行 3 */
    public static final String AB_BILL_POOL_BP_TYPE_3 = "3";
    /*** 保证金存取类型 AB_POOL_BAIL_DTL TYPE*/
    /*** 保证金存入 1 */
    public static final String AB_POOL_BAIL_DTL_TYPE_IN = "1";
    /*** 保证金提取 2 */
    public static final String AB_POOL_BAIL_DTL_TYPE_OUT = "2";
    /*** 审批状态 AB_POOL_STATUS*/
    /** 待处理 */
    public static final Integer AB_POOL_STATUS0 = 0;
    /** 打回 */
    public static final Integer AB_POOL_STATUS_1 = -1;
    /** 删除 */
    public static final Integer AB_POOL_STATUS_2 = -2;
    /** 审批中 */
    public static final Integer AB_POOL_STATUS11 = 11;
    /** 审批完成 */
    public static final Integer AB_POOL_STATUS95 = 95;
    /*** 指令状态  AB_POOL_OPT_STATUS*/
    /** -3 银行交易失败 **/
    public static final String AB_POOL_OPT_STATUS_3 = "-3";
    /** -1 发送失败**/
    public static final String AB_POOL_OPT_STATUS_1 = "-1";
    /** -2 交易打回**/
    public static final String AB_POOL_OPT_STATUS_2 = "-2";
    /** 16 待发送**/
    public static final String AB_POOL_OPT_STATUS16 = "16";
    /** 18 银行处理中**/
    public static final String AB_POOL_OPT_STATUS18 = "18";
    /** 19 银行交易成功**/
    public static final String AB_POOL_OPT_STATUS19 = "19";
    /*** 出入池方式 */
    /** 1 质押 **/
    public static final  String AB_POOL_INOUT_TYPE_1 = "1";
    /** 2 托管 **/
    public static final  String AB_POOL_INOUT_TYPE_2 = "2";
    /*** 操作类型 0终止  1继续 */
    public static final String AB_POOL_OPT_0 = "0";
    public static final String AB_POOL_OPT_1 = "1";
    /*** 合同状态  AB_POOL_CONTRACT_STATUS*/
    /** 0 待确认 **/
    public static final String AB_POOL_CONTRACT_STATUS0 = "0";
    /** 1 已确认 **/
    public static final String AB_POOL_CONTRACT_STATUS1 = "1";
    /** 2 重新生成 **/
    public static final String AB_POOL_CONTRACT_STATUS2 = "2";
    // 票据池操作资源常量
    /*** 票据池接口操作资源 */
    public static final String AB_POLL_POOLIF_RESOURCE_OP = "abpool:poolif:resource:op";
    /*** 票据池操作资源 */
    public static final String AB_POLL_POOL_RESOURCE_OP = "abpool:pool:resource:op";
    /*** 票据池单位操作资源 */
    public static final String AB_POLL_CORP_OP = "abpool:corp:op";
    /*** 票据池保证金存入操作资源 */
    public static final String AB_POLL_BAIL_IN_RESOURCE_OP = "abpool:bail:in:resource:op";
    /*** 票据池保证金提取操作资源 */
    public static final String AB_POLL_BAIL_OUT_RESOURCE_OP = "abpool:bail:out:resource:op";
    /*** 票据池-额度管理-额度分配操作资源 */
    public static final String AB_POLL_LIMITALLOT_RESOURCE_OP = "abpool:limitallot:resource:op";
    /*** 票据池-额度管理-额度分配核准操作资源 */
    public static final String AB_POLL_LIMITALLOT_CHECK_RESOURCE_OP = "abpool:limitallotcheck:resource:op";
    /*** 票据池-查询管理-额度调剂查询资源 */
    public static final String AB_POLL_LIMITALLOT_RESOURCE_QUERYOP = "abpool:limitallot:resource:queryOp";
    /*** 票据池融资申请操作资源 */
    public static final String AB_POLL_EMITFINA_APPLY_RESOURCE_OP = "abpool:emitfina:apply:resource:op";
    /*** 票据池出票申请操作资源 */
    public static final String AB_POLL_EMIT_APPLY_RESOURCE_OP = "abpool:emit:apply:resource:op";
    /*** 票据池提示付款操作资源 */
    public static final String AB_POLL_COLLECTION_APPLY_RESOURCE_OP = "abpool:collectio:apply:resource:op";
    /*** 质押人设置操作资源 */
    public static final String AB_POLL_PLEDGEE_RESOURCE_OP = "abpool:pledgee:resource:op";
    /*** 操作类型资源 */
    public static final String AB_POLL_TYPE_RESOURCE_OP = "abpool:type:resource:op";
    
    /*** 额度申领资源 */
    public static final String AB_POLL_LIMITAPPLY_RESOURCE_OP = "abpool:limitapply:resource:op";
    // 票据池常量
    /*** 票据池单位起始层级 */
    public static final Integer AB_POLL_BUSTYPE_LEVEL_1 = 1;
    /*** 票据池单位禁用 */
    public static final String AB_POLL_CORP_DISABLE = "disable";
    /*** 票据池单位起用 */
    public static final String AB_POLL_CORP_ENABLE = "enable";
    /*** 入池申请操作资源 */
    public static final String AB_POLL_LINTOAPPLY_RESOURCE_OP = "abpool:lintoapply:resource:op";
    /*** 入池撤销操作资源 */
    public static final String AB_POLL_INTOREPEAL_RESOURCE_OP = "abpool:intoRepeal:resource:op";
    /*** 额度申领核准操作资源 */
    public static final String AB_POLL_APPLYCHECK_RESOURCE_OP = "abpool:applycheck:resource:op";
    
    
    /*** 电票签收操作资源 */
    public static final String AB_POLL_SIGN_RESOURCE_OP = "abpool:sign:resource:op";
    /*** 背书转让操作资源 */
    public static final String AB_POLL_ENDORSE_RESOURCE_OP = "abpool:endorse:resource:op";
    /*** 贴现申请操作资源 */
    public static final String AB_POLL_DISCOUNT_RESOURCE_OP = "abpool:discount:resource:op";
    
    
    /*** 出池申请操作资源 */
    public static final String AB_POLL_OUTAPPLY_RESOURCE_OP = "abpool:outApply:resource:op";
    
    
    /*** 质押转换操作资源 */
    public static final String AB_POLL_PDTST_RESOURCE_OP = "abpool:pdTst:resource:op";
    
/***票据池签约信息操作资源	abpool:query:poolsign:op */
    public static final String AB_POLL_QUERY_POOLSIGN_OP = "abpool:query:poolsign:op";
/***票据池额度信息操作资源	abpool:query:limit:op */
    public static final String AB_POLL_QUERY_LIMIT_OP = "abpool:query:limit:op";
/***出入池登记簿操作资源	abpool:query:inout:op */
    public static final String AB_POLL_QUERY_INOUT_OP = "abpool:query:inout:op";
/***质押托管转换登记簿操作资源	abpool:query:pdtst:op */
    public static final String AB_POLL_QUERY_PDTST_OP = "abpool:query:pdtst:op";
/***出票申请登记簿操作资源	abpool:query:emit:op */
    public static final String AB_POLL_QUERY_EMIT_OP = "abpool:query:emit:op";
/***贴现申请登记簿操作资源	abpool:query:discount:op */
    public static final String AB_POLL_QUERY_DISCOUNT_OP = "abpool:query:discount:op";
/***背书申请登记簿操作资源	abpool:query:endorse:op */
    public static final String AB_POLL_QUERY_ENDORSE_OP = "abpool:query:endorse:op";
/***提示付款登记簿操作资源	abpool:query:pay:op */
    public static final String AB_POLL_QUERY_PAY_OP = "abpool:query:pay:op";
/***在池登记簿操作资源	abpool:query:hold:op */
    public static final String AB_POLL_QUERY_HOLD_OP = "abpool:query:hold:op";
/***保证金交易登记簿操作资源	abpool:query:bail:op */
    public static final String AB_POLL_QUERY_BAIL_OP = "abpool:query:bail:op";
    /***出票融资申请登记簿操作资源	abpool:query:financing:op */
    public static final String AB_POLL_QUERY_FINANCING_OP = "abpool:query:financing:op";
    
	//出入池状态 AB_BILL_POOL_STATUS 
	//0	池外
	//1	池内
	//2	入池在途
	//3	出池在途
    //12：入池撤消在途
	public final static String AB_BILL_POOL_STATUS_0="0";
	public final static String AB_BILL_POOL_STATUS_1="1";
	public final static String AB_BILL_POOL_STATUS_2="2";
	public final static String AB_BILL_POOL_STATUS_3="3";
    public final static String AB_BILL_POOL_STATUS_12="12";
	
	
	//出入池方式 1质押入池 2托管入池
	public final static String AB_POOL_INOUT_TYPE1 ="1";
	public final static String AB_POOL_INOUT_TYPE2 ="2";
	
	/** 票据入池日志详细业务类型**/
	/** * 入池申请 01 */
	public static final String AB_POOL_LOG_TYPE_01 = "01";
	/** * 出池申请02 */
	public static final String AB_POOL_LOG_TYPE_02 = "02";
	
	/** 入池银行接口代码**/
	/** 建设银行9018**/
	public static final String AB_POOL_INTO_APPLE_9018 = "9018";
	/** 兴业银行9015 */
	public static final String AB_POOL_INTO_APPLE_9015 = "9015";
	/** 浙商银行9022 */
	public static final String AB_POOL_INTO_APPLE_CZB = "9022";
	/** 农行9009**/
	public static final String AB_POOL_INTO_APPLE_ABC = "9009";
    /** 中信银行 **/
    public static final String BIF_CODE_CITIC_9001 = "9001";
    /** 中国银行 **/
    public static final String BIF_CODE_BOC_9021 = "9021";
	
	/**查询在途类型 ：0=审批，1指令**/
	public static final Integer AB_POOL_CHECK_TYPE_0 = 0;
    public static final Integer AB_POOL_CHECK_TYPE_1 = 1;
    /**查询类型: 1池接口 2票据池 3池单位  4质权人*/
    public static final Integer AB_POOL_CHECK_QUERYTYPE_1 = 1;
    public static final Integer AB_POOL_CHECK_QUERYTYPE_2 = 2;
    public static final Integer AB_POOL_CHECK_QUERYTYPE_3 = 3;
    public static final Integer AB_POOL_CHECK_QUERYTYPE_4 = 4;
    /**业务类型:*/
    /** 1=额度申请、额度申请核准、额度分配、额度分配核准、保证金存入、保证金提取 */
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_1 = 1;
    /** 2=入池申请、出池申请、质押托管转换、提示付款 */
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_2 = 2;
    /** 3=电票签收（选签收入池）*/
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_3 = 3;
    /** 4=背书申请 贴现申请*/
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_4 = 4;
    /** 5=出票申请 */
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_5 = 5;
    /** 6=融资申请管理 */
    public static final Integer AB_POOL_CHECK_BUSINESSTYPE_6 = 6;
    /** 业务类型：8018入池申请；8019出池申请；8118质押转托管；8218托管转质押；001出票申请；8001出票融资申请；010背书申请；011贴现申请；020提示付款*/
    /**  8001出票融资申请 */
    public static final String AB_POOL_BRIEF_8001 = "8001";
    /** 入池申请 */
    public static final String AB_POOL_BRIEF_8018 = "8018";
    /** 票据入池 */
    public static final String AB_POOL_BRIEF_8818 = "8818";
    /** 出池申请 */
    public static final String AB_POOL_BRIEF_8019 = "8019";
    /** 质押转托管 */
    public static final String AB_POOL_BRIEF_8118 = "8118";
    /** 托管转质押 */
    public static final String AB_POOL_BRIEF_8218 = "8218";
    /** 入池撤销 */
    public static final String AB_POOL_BRIEF_8032 = "8032";
    /** 出票申请 */
    public static final String AB_POOL_BRIEF_001 = "001";
    /** 背书申请 */
    public static final String AB_POOL_BRIEF_010 = "010";
    /** 贴现申请 */
    public static final String AB_POOL_BRIEF_011 = "011";
    /** 提示付款 */
    public static final String AB_POOL_BRIEF_020 = "020";
    /** 质押 */
    public static final String AB_POOL_BRIEF_018 = "018";
    /** 质押解除 */
    public static final String AB_POOL_BRIEF_019 = "019";
	
	public final static String BILL_LOG_APP_SUCC = "bill.log.app.succ";
	
	public final static String BILL_LOG_APP_BACK = "bill.log.app.back";
	
	public final static String BILL_LOG_BILLPOOL_LIMIT_APPLY = "billPool.Limit.apply";
	
	public final static String BILL_LOG_BILLPOOL_LIMIT_CHECK = "billPool.Limit.check";
	
	public final static String BILL_LOG_BILLPOOL_TST_CHECK = "billPool.tst.check";
	
	public final static String BILL_LOG_BILLPOOL_OUT = "billPool.limit.out.log";
	public final static String BILL_LOG_BILLPOOL_INTO = "billPool.limit.into.log";
    public final static String BILL_LOG_BILLPOOLTST_INTO = "billPool.pdtst.into.log";
    public final static String BILL_LOG_BILLPOOL_INTO_REPEAL = "billPool.intoRepeal.apply.log";
//	/*** 业务类型类型：poolBail票据池保证金poolLimit票据池额度poolCon票据池融资合同 */
	/*** 业务类型类型：poolBail票据池保证金 */
	public final static String EXC_BUSTYPE_BAIL = "poolBail";
	
	/*** 业务类型类型：poolLimit票据池额度 */
	public final static String EXC_BUSTYPE_LIMIT = "poolLimit";
	
	/*** 业务类型类型：poolCon票据池融资合同 */
	public final static String EXC_BUSTYPE_CON = "poolCon";
//
//	/*** 额度类型：apply	申请applyCheck	申请核准allot	分配 allotCheck	分配核准*/
//	private java.lang.String limitType;
	/*** 额度类型：apply	申请 */
	public final static String EXC_LIMITTYPE_APPLY = "apply";
	
	/*** 额度类型：applyCheck	申请核准 */
	public final static String EXC_LIMITTYPE_APPLYCHECK = "applyCheck";
	
	/*** 额度类型：allot	分配 */
	public final static String EXC_LIMITTYPE_ALLOT = "allot";
	
	/*** 额度类型：allotCheck	分配核准 */
	public final static String EXC_LIMITTYPE_ALLOTCHECK = "allotCheck";
	
	// 合同操作 指令状态：0待确认1已确认2重新生成
	/** 合同操作 指令状态 0待确认 **/
	public static final String CON_STATUS_0 = "0";
	/** 合同操作 指令状态1已确认 **/
	public static final String CON_STATUS_1 = "1";
	/** 合同操作 指令状态 2重新生成 **/
	public static final String CON_STATUS_2 = "2";
	
	
	/**继续**/
	public static final String OPT_INI_Y ="1";
	
	/**终止**/
	public static final String OPT_INI_N ="0";
	
	
	/** 日志信息**/
	/** 继续**/
	public static final String LOG_INF_MSG_CONTINUE="billPool.intoApply.continue";
	
	/** 终止**/
	public static final String LOG_INF_MSG_STOP="billPool.intoApply.stop";
    /** 指令查询类型 票据池额度 01 **/
    public static final String QUERY_EXC_TYPE_01 = "01";
    /** 指令查询类型 出入池登记簿 02 **/
    public static final String QUERY_EXC_TYPE_02 = "02";
    /** 指令查询类型 在池登记簿 03 **/
    public static final String QUERY_EXC_TYPE_03 = "03";
    /** 指令查询类型 保证金交易明细 04 **/
    public static final String QUERY_EXC_TYPE_04 = "04";
    /** 指令查询类型 票据池额度调剂明细 05 **/
    public static final String QUERY_EXC_TYPE_05 = "05";
    /** 票据池调剂方式 pool_limit_adjust_type 字典 **/
    public static final String POOL_LIMIT_ADJUST_TYPE = "pool_limit_adjust_type";
    /** 票据池调剂方式 pool_limit_adjust_type 上划	1 **/
    public static final String POOL_LIMIT_ADJUST_TYPE_1 = "1";
    /** 票据池调剂方式 pool_limit_adjust_type 下拨	2 **/
    public static final String POOL_LIMIT_ADJUST_TYPE_2 = "2";
}
