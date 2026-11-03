package com.bytter.basic.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author huangx
 * @version 1.0
 * @Title: ENTITY
 * @Description: 银行类型自定义配置项
 * @date 2024-03-30 17:07:12
 */
@Entity
@Table(name = "BT_BANK_TYPE_CUSTOM")
public class BankTypeCustom extends IdEntity implements java.io.Serializable  {

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

    /**
     * 自定义类型
     */
    private String customType;


    private String isDeleted    ;

    @Column(name = "BANK_TYPE_ID")
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name = "BIF_CODE")
    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    @Column(name = "COUNTRY_ID")
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Column(name = "BANK_PAY_TYPE")
    public String getBankPayType() {
        return bankPayType;
    }

    public void setBankPayType(String bankPayType) {
        this.bankPayType = bankPayType;
    }

    @Column(name = "CUSTOM_CODE")
    public String getCustomCode() {
        return customCode;
    }

    public void setCustomCode(String customCode) {
        this.customCode = customCode;
    }

    @Column(name = "CUSTOM_NAME")
    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    @Column(name = "CUSTOM_TYPE")
    public String getCustomType() {
        return customType;
    }

    public void setCustomType(String customType) {
        this.customType = customType;
    }

    @Column(name = "IS_DELETED")
    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}
