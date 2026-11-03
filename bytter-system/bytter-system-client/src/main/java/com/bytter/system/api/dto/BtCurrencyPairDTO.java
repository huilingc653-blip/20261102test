package com.bytter.system.api.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 币别对
 */
public class BtCurrencyPairDTO implements Serializable {

    /**
     * ID
     */
    private String id;
    /**
     * 币种1
     */
    private String curId1;
    private String englishCode1;
    private String curName1;
    /**
     * 币种2
     */
    private String curId2;
    private String englishCode2;
    private String curName2;
    /**
     * 标准货币
     */
    private String baseCurId;
    private String baseEnglishCode;
    private String baseCurName;
    /**
     * 汇率示例
     */
    private BigDecimal exchangeRate;
    /**
     * 有效标志
     */
    private String validSign;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    /**
     * 货币对
     */
    private String currencyPair;

    /**
     * 货币对ID
     */
    private String pairId;

    /**
     * 货币对Code
     */
    private String pairCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCurId1() {
        return curId1;
    }

    public void setCurId1(String curId1) {
        this.curId1 = curId1;
    }

    public String getEnglishCode1() {
        return englishCode1;
    }

    public void setEnglishCode1(String englishCode1) {
        this.englishCode1 = englishCode1;
    }

    public String getCurName1() {
        return curName1;
    }

    public void setCurName1(String curName1) {
        this.curName1 = curName1;
    }

    public String getCurId2() {
        return curId2;
    }

    public void setCurId2(String curId2) {
        this.curId2 = curId2;
    }

    public String getEnglishCode2() {
        return englishCode2;
    }

    public void setEnglishCode2(String englishCode2) {
        this.englishCode2 = englishCode2;
    }

    public String getCurName2() {
        return curName2;
    }

    public void setCurName2(String curName2) {
        this.curName2 = curName2;
    }

    public String getBaseCurId() {
        return baseCurId;
    }

    public void setBaseCurId(String baseCurId) {
        this.baseCurId = baseCurId;
    }

    public String getBaseEnglishCode() {
        return baseEnglishCode;
    }

    public void setBaseEnglishCode(String baseEnglishCode) {
        this.baseEnglishCode = baseEnglishCode;
    }

    public String getBaseCurName() {
        return baseCurName;
    }

    public void setBaseCurName(String baseCurName) {
        this.baseCurName = baseCurName;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getCurrencyPair() {
        return currencyPair;
    }

    public void setCurrencyPair(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    public String getPairId() {
        return pairId;
    }

    public void setPairId(String pairId) {
        this.pairId = pairId;
    }

    public String getPairCode() {
        return pairCode;
    }

    public void setPairCode(String pairCode) {
        this.pairCode = pairCode;
    }
}
