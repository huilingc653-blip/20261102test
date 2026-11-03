package com.bytter.system.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum PurposeMessageEnum implements IMessageEnum {
    /**
     * 用途所属类别不能为NULL
     */
    BT_PURPOSE_MESSAGE_SYSBUSTYPE_NULL("01001"),
    /**
     * 用途已存在，无法保存
     */
    BT_PURPOSE_MESSAGE_PURPOSE_EXIST("01002"),
    /**
     * 同单位、同类别不能存在同样名称的用途
     */
    BT_PURPOSE_MESSAGE_PURPOSE_REPEAT("01003");
    PurposeMessageEnum(String code) {
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
