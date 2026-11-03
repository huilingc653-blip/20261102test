package com.bytter.bill.domain.billbank.pool.utils;
/**
 * 票据池常量
 * 
 * @author zhengg
 */
public class BillPoolConstant {
	/** 是否启用票据池功能 **/ 
	public static boolean IS_RUN_POOL = false;
	 
	//银行接口代码
	/** 兴业银行 **/
	public static final String BIF_CODE_CIB = "9015";
	/** 建设银行 **/
	public static final String BIF_CODE_CCB = "9018";
	/** 浙商银行**/
	public static final String BIF_CODE_CZB = "9022";
	/** 农业银行 **/
	public static final String BIF_CODE_ABC = "9009";
	/** 中信银行 **/
	public static final String BIF_CODE_CITIC = "9001";
	/** 中国银行 **/
	public static final String BIF_CODE_BOC = "9021";
	/** 平安银行 **/
	public static final String BIF_CODE_PINGAN = "9501";
	// 指令状态(交易失败-1，指令待发送16，交易处理中18，交易成功19，交易打回-2)
	/** 指令状态 交易失败-1 **/
	public static final String optStatus_1 = "-1";
	/** 指令状态 指令待发送16 **/
	public static final String optStatus_16 = "16";
	/** 指令状态 交易处理中18 **/
	public static final String optStatus_18 = "18";
	/** 指令状态 交易成功19 **/
	public static final String optStatus_19 = "19";
	/** 指令状态 交易打回-2 **/
	public static final String optStatus_2 = "-2";
	/** 指令状态 交易撤回-3 **/
	public static final String optStatus_3 = "-3";
	// 银行接口RE_CODE状态(待发送3，队列中S，失败F)
	/** 银行接口RE_CODE 完成1 **/
	public static final String RE_CODE_1 = "1";
	/** 银行接口RE_CODE 待发送3 **/
	public static final String RE_CODE_3 = "3";
	/** 银行接口RE_CODE 待发送S **/
	public static final String RE_CODE_S = "S";
	/** 银行接口RE_CODE 待发送F **/
	public static final String RE_CODE_F = "F";
	
	// 额度操作类型
	/** 额度申请 **/
	public static final String LIMIT_TYPE_APPLY = "apply";
	/** 额度申请复核 **/
	public static final String LIMIT_TYPE_APPLYCHECK = "applyCheck";
	/** 额度分配 **/
	public static final String LIMIT_TYPE_ALLOT = "allot";
	/** 额度分配复核 **/
	public static final String LIMIT_TYPE_ALLOTCHECK = "allotCheck";
	// 保证金存取类型
	/** 保证金存入 1 **/
	public static final String BAIL_TYPE_IN = "1";
	/** 保证金取出 2 **/
	public static final String BAIL_TYPE_OUT = "2";
	// 合同操作 指令状态：0待确认1已确认2重新生成
	/** 合同操作 指令状态 0待确认 **/
	public static final String CON_STATUS_0 = "0";
	/** 合同操作 指令状态1已确认 **/
	public static final String CON_STATUS_1 = "1";
	/** 合同操作 指令状态 2重新生成 **/
	public static final String CON_STATUS_2 = "2";
	// 票据池流水类型
	/** 8018 入池申请 **/
	public static final String BRIEF_TYPE_IN = "8018";
	/** 018 入池质押申请 **/
	public static final String BRIEF_TYPE_INPLEDGE = "018";
	/** 8018_DE 入池申请删除申请流水业务 **/
	public static final String BRIEF_TYPE_IN_DEL = "8018_DE";
	/** 8019 出池申请 **/
	public static final String BRIEF_TYPE_OUT = "8019";
	/** 019 出池申请-入池质押解除 **/
	public static final String BRIEF_TYPE_OUT_REPLEDGE = "019";
	/** 8118,8218 质押托管转换 **/
	public static final String BRIEF_TYPE_PLEDGE_HOSTING = "8118,8218";
	/** 8118 质押转托管 **/
	public static final String BRIEF_TYPE_TO_HOSTING = "8118";
	/** 8218 托管转质押 **/
	public static final String BRIEF_TYPE_TO_PLEDGE = "8218";
	/** 001 出票 **/
	public static final String BRIEF_TYPE_EMIT = "001";
	/** 010 背书 **/
	public static final String BRIEF_TYPE_ENDORSE = "010";
	/** 011 贴现 **/
	public static final String BRIEF_TYPE_DISCOUNT = "011";
	/** 8818 票据入池 **/
	public static final String BRIEF_TYPE_INOP = "8818";
	/** 8818_1 票据入池托管 **/
	public static final String BRIEF_TYPE_INOP_HOSTING = "8818_1";
	/** 8818_2 票据入池质押**/
	public static final String BRIEF_TYPE_INOP_PLEDGE = "8818_2";
	/** 020 提示付款 **/
	public static final String BRIEF_TYPE_ISPAY = "020";
	/** 8001 出票融资申请 **/
	public static final String BRIEF_TYPE_LIMITAPPLY = "8001";
	/** 8032 票据入池撤销 **/
	public static final String BRIEF_TYPE_IN_REPEAL = "8032";
	///** 出入池方式：1质押2托管 */
	/**  出入池方式：1质押 **/
	public static final String INOUTTYPE_IN = "1";
	/**  出入池方式：2托管 **/
	public static final String INOUTTYPE_OUT = "2";
	
	///** 托管质押转换方式：01-质押转托管 02-托管转质押 */
	/** 托管质押转换方式:01-质押转托管 **/
	public static final String PDTSTTYPE_TO_HOSTING = "01";
	
	/** 托管质押转换方式:02-托管转质押 **/
	public static final String PDTSTTYPE_TO_PLEDGE = "02";
	
	///** 票据池状态空或0池外1池内2入池在途3出池在途 */
	/** 票据池状态空或0池外 **/
	public static final String POOL_STATUS_0 = "0";
	
	/** 票据池状态1池内 **/
	public static final String POOL_STATUS_1 = "1";
	
	/** 票据池状态2入池在途 **/
	public static final String POOL_STATUS_2 = "2";
	
	/** 票据池状态3出池在途 **/
	public static final String POOL_STATUS_3 = "3";
	
	/*** 有效标志：0否/者处理中*/
	public static final String VALID_SIGN_0 = "0";
	/*** 有效标志：1是 */
	public static final String VALID_SIGN_1 = "1";
	/*** 有效标志：2已删除/业务已结束 */
	public static final String VALID_SIGN_2 = "2";
	
	//查询类型（票据池额度01\出入池登记簿02\在池登记簿03\保证金交易明细04）
	/*** 查询业务指令queryType 01 票据池额度*/
	public static final String POOL_EXC_QUERY_TYPE_LIMIT = "01";
	/*** 查询业务指令queryType 出入池登记簿02*/
	public static final String POOL_EXC_QUERY_TYPE_INOUT = "02";
	/*** 查询业务指令queryType 在池登记簿03*/
	public static final String POOL_EXC_QUERY_TYPE_HOLD = "03";
	/*** 查询业务指令queryType 保证金交易明细04*/
	public static final String POOL_EXC_QUERY_TYPE_BAIL = "04";
	/*** 查询业务指令queryType 票据池额度交易明细05*/
	public static final String POOL_EXC_QUERY_TYPE_LIMITDTL = "05";
	/**
	 * 质押转托管确认状态:-1质押转托管申请失败
	 */
	public static final String POOL_PLEDGE_STATUS_FAIL = "-1";
	/**
	 * 质押转托管确认状态: 0质押转托管确认失败
	 */
	public static final String POOL_PLEDGE_STATUS_0 = "0";
	/**
	 * 质押转托管确认状态:1质押转托管确认在途
	 */
	public static final String POOL_PLEDGE_STATUS_1 = "1";
}
