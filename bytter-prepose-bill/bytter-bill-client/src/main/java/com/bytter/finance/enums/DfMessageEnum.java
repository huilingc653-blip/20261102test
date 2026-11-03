package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
public enum DfMessageEnum implements IMessageEnum {
    /** 存在未完成业务，无法新增 */
    DF_REPAY_APPLY_MSG("01101");
    DfMessageEnum(String code) {
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
