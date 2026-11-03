package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 贷款消息提示枚举类
 */
public enum BorrowingMessageEnum implements IMessageEnum {
    /** 类型编号及名称已存在 */
    FIN_BORROWING_TYPE_EXIST("01001");
    BorrowingMessageEnum(String code) {
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
