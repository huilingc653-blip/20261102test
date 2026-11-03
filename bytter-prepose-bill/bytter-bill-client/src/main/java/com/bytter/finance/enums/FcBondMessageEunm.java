package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
/**
 * 保证金管理消息提示枚举类
 * 
 * @author liusy
 */
public enum FcBondMessageEunm implements IMessageEnum {
    /** 保证金占用金额不能大于可用余额 */
    FCBOND_APPLYMONEY_CHECK("03001");
    FcBondMessageEunm(String code) {
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
