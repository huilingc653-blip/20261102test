package com.bytter.report.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum BiRemainTermMessageEnum implements IMessageEnum {
    /**期限名称重复*/
    TERM_NAME_REPEAT("66660");
    BiRemainTermMessageEnum(String code) {
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
