package com.bytter.accountReceivable.writeOff.enums;

import com.bytter.core.enums.IMessageEnum;

public enum WriteOffEnums implements IMessageEnum {

    /** 核销确认列表加载异常：{0} **/
    WRITE_OFF_SURE_CHECK_MSG("00001");

    private WriteOffEnums(String code) {
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
