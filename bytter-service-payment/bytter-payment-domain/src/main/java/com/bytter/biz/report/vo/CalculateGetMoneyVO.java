package com.bytter.biz.report.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author pzc  2023/10/26
 * @since 1.0.0
 */
public class CalculateGetMoneyVO implements Serializable {

    public final static String BEGINANDENDFLAG_BEGIN="0";
    public final static String BEGINANDENDFLAG_END="1";

    //开始和结束标识，0开始；1结束
    private String beginAndEndFlag;

    private String cdSign;
    private Integer numbers;
    private BigDecimal money;
    private String curId;
    private String bankName;

    private String bankTypeId;

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBeginAndEndFlag() {
        return beginAndEndFlag;
    }

    public void setBeginAndEndFlag(String beginAndEndFlag) {
        this.beginAndEndFlag = beginAndEndFlag;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public Integer getNumbers() {
        return numbers;
    }

    public void setNumbers(Integer numbers) {
        this.numbers = numbers;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
}
