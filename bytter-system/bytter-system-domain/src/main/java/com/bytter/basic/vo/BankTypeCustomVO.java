package com.bytter.basic.vo;

import java.io.Serializable;

/**
 *
 * @author huangx
 * @version 1.0
 * @Title: VO
 * @Description: 银行类型自定义配置项
 * @date 2024-03-30 17:07:12
 */
public class BankTypeCustomVO implements Serializable {
    private String id;

    /**
     * 银行类型ID
     */
    private String bankTypeId;

    /**
     * 接口代码ID
     */
    private String bifCode;

    /**
     * 适用国家
     */
    private String countryId;

    /**
     * 支付类型
     */
    private String bankPayType;

    /**
     * 自定义上送代码
     */
    private String customCode;

    /**
     * 自定义名称
     */
    private String customName;
    private String   bifName;
    private String   bankTypeName;
    private String   bankPayTypeDisplay;

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBifName() {
        return bifName;
    }

    public void setBifName(String bifName) {
        this.bifName = bifName;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }


    public String getBankPayTypeDisplay() {
        return bankPayTypeDisplay;
    }

    public void setBankPayTypeDisplay(String bankPayTypeDisplay) {
        this.bankPayTypeDisplay = bankPayTypeDisplay;
    }
}
