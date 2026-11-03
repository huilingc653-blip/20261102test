package com.bytter.biz.payment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum AllocationMessageEnum implements IMessageEnum {
	/** 调拨策略代码必须唯一 */
    NIS_ALLOCATION_MESSAGE_GATHERPOLICYCODE_UNIQUE("01001"),
	
    /** 金额不能为空且大于0 */
	NIS_ALLOCATION_MESSAGE_MONEY_NULL("01020"),
    /** 银行账号余额不足 */
	NIS_ALLOCATION_MESSAGE_MONEY_GACCBALANCE("01021"),
	/** 至少其中一个账户的金额大于0 */
	NIS_ALLOCATION_MESSAGE_MONEYALL_GZERO("01022"),
    /** 单据状态已改变，请重新获取列表 */
    NIS_ALLOCATION_MESSAGE_STATUS_DELETE("01023"),
	/**付款账号{0}与收款账号相同*/
    NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEQUALPAYEENO("01024"),
    /**付款账号{0}重复*/
	NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOREAPEAT("01050"),
	 /**付款账号为{0}，收款账号为{1}的数据已存在*/
	NIS_ALLOCATION_MESSAGE_EXCRULESMANAGE_PAYERNOEXIST("01051");
	
	
	
	
	AllocationMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
