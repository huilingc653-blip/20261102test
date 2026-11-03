package com.bytter.core.enums;
public enum SystemMessageEnum implements IMessageEnum {
    /** 该条数据已经被引用，不可以进行删除操作! */
    DATA_REFERENCED_EXIST("01312"),
    /**
     * 参数不合法
     */
    ORDER_MESSAGE_PARAM_ERROR("99997");
    private String code;
    SystemMessageEnum(String code) {
        this.code = code;
    }
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
