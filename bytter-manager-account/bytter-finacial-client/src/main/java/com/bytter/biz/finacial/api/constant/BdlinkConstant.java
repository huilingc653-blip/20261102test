package com.bytter.biz.finacial.api.constant;
/**
 * 银企直连常量类
 *
 * @author huangx
 * @since 2017/6/29.
 */
public class BdlinkConstant {
	/**
	 * 交易状态
	 */
	public final static String SUCCESS = "0";//交易成功
    public final static String WAIT_RESULT = "1";//等待结果
    public final static String BANK_FAILED = "2";//银行处理失败
    public final static String PRE_SEND = "3";//准备发送
    public final static String WAIT_FILL = "6";//待发送
    public final static String INVALIDATE = "7";//指令无效
    public final static String PRINTED = "8";//已打印支票
    public final static String INTERFACE_FAILED = "9";//接口处理失败
    public final static String WAIT_APPROVE = "w";//待审批
    public final static String PASS = "p";//审批通过
    public final static String BACK = "B";//指令退回
    public final static String PASS_BACK = "R";//审批打回
    public final static String DELETE = "D";//指令删除
	public final static String SUSPECTED = "T";//可疑状态
    
    /**
	 * 交易模式
	 */
	public final static String TRANS_MODE_PUBLIC = "3";//对公
	public final static String TRANS_MODE_PRIVATE = "4";//对私
	
    /**
	 * 付款指令-业务类型
	 */
    public final static Integer BUSORITYPE_10 = 10;//柜台付款
	public final static Integer BUSORITYPE_30 = 30;//单位付款
	public final static Integer BUSORITYPE_32 = 32;//申请下拨
	public final static Integer BUSORITYPE_33 = 33;//主动下拨
	public final static Integer BUSORITYPE_34 = 34;//头寸调拨
	public final static Integer BUSORITYPE_37 = 37;//企业贷款放款
	public final static Integer BUSORITYPE_48 = 48;//中心付款
	public final static Integer BUSORITYPE_53 = 53;//利息管理
	/**
	 * 收/付款指令来源
	 */
	public final static String FROM_NIS_PAY = "1";//付款
	public final static String FROM_ALLOCATION_TYPE_DOWN = "2";//下拨
	public final static String FROM_ALLOCATION_TYPE_ASK = "3";//请款
	public final static String FROM_ALLOCATION_TYPE_POS = "4";//头寸调拨
	public final static String FROM_ALLOCATION_TYPE_UP = "5";//上划
	public final static String FROM_ALLOCATION_TYPE_TURN = "6";//上缴
	public final static String FROM_ALLOCATION_TYPE_AUTO_DOWN = "7";//自动下拨
	public final static String FROM_ALLOCATION_TYPE_AUTO_UP = "8";//自动归集
	public final static String FROM_COUNTER_TALLY = "9";//柜台手工记账
	public final static String FROM_NIS_WAGE = "10";//代发工资
	public final static String FROM_BIS_EXC_PAY = "11";//自营付款
	/**
	 * 对私付款指令单据来源XX
	 */
	public final static String VOUCHER_FROM_PRIVATE_0 = "0";//付款办理
	public final static String VOUCHER_FROM_PRIVATE_1 = "1";//费用报销
	/**
	 * 根据指令来源获取业务service
	 * @param voucherFrom
	 * @return  业务service
	 */
	public static String getServiceNameByVoucherFrom(String voucherFrom){
		if(FROM_NIS_PAY.equals(voucherFrom)){
			return "billheadBisExcBackServiceImpl";
		}else if(FROM_ALLOCATION_TYPE_DOWN.equals(voucherFrom)
				|| FROM_ALLOCATION_TYPE_ASK.equals(voucherFrom)
				|| FROM_ALLOCATION_TYPE_POS.equals(voucherFrom)){
			return "allocationBisExcBackServiceImpl";
		}else if(FROM_NIS_WAGE.equals(voucherFrom) ){
			return "wageBisExcBackServiceImpl";
		}
		return "";
	}
	
	/**
	 * 收款指令-业务类型
	 */
    public final static Integer BUSORITYPE_ALLOCATION_UP = 10;//上划
	public final static Integer BUSORITYPE_ALLOCATION_TURN = 14;//上缴
	/**
	 * 指令类型
	 */
	public final static String CMDTYPE_MANUAL = "0";//手动
	public final static String CMDTYPE_AUTO = "1";//自动
	
	/**
	 * 状态
	 */
	public final static Integer STATUE_TEMP = 0;//暂存
    public final static Integer STATUE_BACK = -1;//打回
    public final static Integer STATUE_DEL = -2;//删除
    public final static Integer STATUE_WAIT_CHECK = 11;//待审批
    public final static Integer STATUE_CHECK = 95;//审批通过
    
    /**
     * 查询指令的交易类型
     */
    /** 查询实时余额 */
    public final static String NOW_BALANCE = "900010";
    /** 查询当日明细 */
    public final static String INTRADAY_DTL = "900020";
    /** 查询历史明细 */
    public final static String HISTORY_DTL = "900030";
    /** 查询历史余额 */
    public final static String HISTORY_BALANCE = "900040";
	/**
	 * 英文状态下#作为分隔符
	 */
	public static final String ADDWORD_SPLIT = "#";
}
