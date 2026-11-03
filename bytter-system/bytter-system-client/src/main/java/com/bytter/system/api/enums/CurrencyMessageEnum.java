package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum CurrencyMessageEnum implements IMessageEnum {
    /**
     * 币别编码必须唯一
     */
    BASIC_BTCURRENCY_MESSAGE_CURCODE_UNIQUE("01001"),
    /**
     * 只能提交暂存或打回状态的货币汇率维护
     */
    FC_EXCRATE_ADJUST_MESSAGE_STATUS("02001"),
    /**
     * 执行日期不能小于或者等于上次的执行日期
     */
    FC_EXCRATE_ADJUST_MESSAGE_PRETIME("02002"),
    /**
     * 类型名称必须唯一
     */
    BTRATETYPE_MESSAGE_TYPENAME_UNIQUE("02003"),
    /**
     * 货币对已存在
     */
    BASIC_BTCURRENCY_PAIRS_ALREADY_EXIST("02004"),
    /**
     * 货币已被调用，无法修改英文简码和人行编码
     */
    BASIC_BTCURRENCY_ASSOCIATED_FOREIGN("02005"),
    /**
     * 货币汇率不存在
     */
    BASIC_BTCURRENCY_EXCRATE_ADJUST_NULL("02006"),
    /**
     * 货币当前汇率不能小于0
     */
    BASIC_BTCURRENCY_EXCRATE_ADJUST_ZERO("02007"),

    /**
     * 系统币种已被引用,请重新选择！
     */
    BT_CURRENCY_MESSAGE_SYSCURID_UNIQUE("02008");
    ;
    CurrencyMessageEnum(String code) {
        this.code = code;
    }
    private String code;
    public void setCode(String code) {
        this.code = code;
    }
    @SuppressWarnings("rawtypes")
    @Override
    public Enum getEnum() {
        return this;
    }
    @Override
    public String getCode() {
        return code;
    }
}
