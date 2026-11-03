package com.bytter.increment.api.enums;
import com.bytter.core.enums.IMessageEnum;
public enum ChoInnerFinanceMessageEnum implements IMessageEnum {
    /**账号{0}已经做过协定且未终止*/
    NIS_INNERAGREEDDEPOSIT_MESSAGE_BLNCTRACC_USE("00001"),
    /**合同号{0}已存在，必须是唯一的*/
    NIS_INNERAGREEDDEPOSIT_MESSAGE_CONTRACTNO_UNIQUE("00002"),
    /**预约金额不允许超过可预约金额*/
    CHO_INNERFINANCE_MESSAGE_BOOKMONEY_EXCEED("00003"),
    /**支取日期应大于网点日结日期*/
    CHO_INNERFINANCE_MESSAGE_BOOKDATE_OVER("00004"),
    /**单号长度不能大于20位*/
    CHO_BANK_FINANCE_BANK_BILL_NO_LENGTH("00005");

    ChoInnerFinanceMessageEnum(String code) {
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
