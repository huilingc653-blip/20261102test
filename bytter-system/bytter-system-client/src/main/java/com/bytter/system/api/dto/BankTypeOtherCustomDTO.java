package com.bytter.system.api.dto;

import java.io.Serializable;

/**
 *
 * @author huangx
 * @version 1.0
 * @Title: DTO
 * @Description: 银行类型自定义配置项
 * @date 2024-03-30 17:07:12
 */
public class BankTypeOtherCustomDTO implements Serializable {
    /**
     * 银行类型ID
     */
    private String bankTypeId;

    /**
     * 接口代码ID
     */
    private String bifCode;

    /**
     * 应用范围 1-外汇买卖交易 2-资金调拨交易 3-资金收付交易 4-外币汇款申请 5-付款办理
     */
    private String applayScope;

    /**
     * 适用国家
     */
    private String countryId;


    /**汇款类型 0-境内付款 1-境外付款*/
    private String remitType;

    /**
     * 所需字段
     */
    private String needField;

    /**
     * 必填字段
     */
    private String requiredField;

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

    public String getApplayScope() {
        return applayScope;
    }

    public void setApplayScope(String applayScope) {
        this.applayScope = applayScope;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getRemitType() {
        return remitType;
    }

    public void setRemitType(String remitType) {
        this.remitType = remitType;
    }

    public String getNeedField() {
        return needField;
    }

    public void setNeedField(String needField) {
        this.needField = needField;
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }
}
