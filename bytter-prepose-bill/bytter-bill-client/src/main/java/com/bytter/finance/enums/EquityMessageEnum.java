package com.bytter.finance.enums;
import com.bytter.core.enums.IMessageEnum;
public enum EquityMessageEnum implements IMessageEnum {
    /** 缴付金额不能大于剩余金额 */
    CMS_EQUITY_REPAY_MONEY_CHECK("9001"),
    /** 回购金额不能大于剩余回购金额 */
    CMS_EQUITY_REPO_MONEY_CHECK("9002"),
    /**
     * 股权类型编码和名称不能重复!
     */
    CMS_BASEDATA_EQUITYTYPE_EXISTENCE("9003"),;
    EquityMessageEnum(String code) {
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
