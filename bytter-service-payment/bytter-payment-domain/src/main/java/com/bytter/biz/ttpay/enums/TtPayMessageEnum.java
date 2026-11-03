package com.bytter.biz.ttpay.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 第三方支付提醒信息
 * @author zhengg
 *
 */
public enum TtPayMessageEnum implements IMessageEnum {
    /** 商户号+单位+支付方式不允许重复!*/
    TPQUOTAVO_MESSAGE_CODE_REP("90010"),
    ;
    private String code;
    TtPayMessageEnum(String code) {
        this.code = code;
    }
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
