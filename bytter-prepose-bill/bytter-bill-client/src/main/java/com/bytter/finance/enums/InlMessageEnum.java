package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
public enum InlMessageEnum implements IMessageEnum {
    /** 存在发放数据，无法作废 */
    INL_ABOLISH_ERROR1_MSG("0001"),

    /**系统有在途的内部拆借收付款单未完成，无法修改！*/
    INL_SEND_NIS_MSG("0002"),

    /**系统中有审批中的还款计划，审批完成后才可选择*/
    INL_CHANGE_APPROVE_MSG("0003"),

    /**还款计划已被修改，请刷新选择最新的还款计划*/
    INL_CHANGE_DELETE_MSG("0004");

    InlMessageEnum(String code) {
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
