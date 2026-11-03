package com.bytter.biz.erp.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Description: 账号视图
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2026
 *
 * @author bytter
 * @date 2026/1/12
 */
public class BankAccInfo implements Serializable {
    private String bankAccId;
    private String bankAcc;
    private String accName;
    private String prentAcc;
    private String entityAcc;
    private String corpId;
    private String curCode;
    private String curId;
    private String counterAcc;
    private String superCounterAcc;
    private String parentSign;
    private String accType;
    private String bankCode;
    private String bankName;
    private String prov;
    private String provId;
    private String provCode;
    private String city;
    private String cityId;
    private String cityCode;
    private String privateNo;
    private String orgCode;
    private String bifCode;
    private Integer isOnline;
    private String validSign;
    private String bifCodeId;
    private Integer isCapitalPool;
    private String abcSecondaryAccount;
    private String primaryAccount;
    private String basicAccountSign;
    private Integer isDomesticBank;
    private String natureCode;
    private String bankOrgCode;
    private String attributeId;
    private String tenantId;
    private String bankTypeId;

    public String getBankAccId() {
        return bankAccId;
    }

    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getPrentAcc() {
        return prentAcc;
    }

    public void setPrentAcc(String prentAcc) {
        this.prentAcc = prentAcc;
    }

    public String getEntityAcc() {
        return entityAcc;
    }

    public void setEntityAcc(String entityAcc) {
        this.entityAcc = entityAcc;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCounterAcc() {
        return counterAcc;
    }

    public void setCounterAcc(String counterAcc) {
        this.counterAcc = counterAcc;
    }

    public String getSuperCounterAcc() {
        return superCounterAcc;
    }

    public void setSuperCounterAcc(String superCounterAcc) {
        this.superCounterAcc = superCounterAcc;
    }

    public String getParentSign() {
        return parentSign;
    }

    public void setParentSign(String parentSign) {
        this.parentSign = parentSign;
    }

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    public String getProvCode() {
        return provCode;
    }

    public void setProvCode(String provCode) {
        this.provCode = provCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getPrivateNo() {
        return privateNo;
    }

    public void setPrivateNo(String privateNo) {
        this.privateNo = privateNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
    }

    public Integer getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Integer isOnline) {
        this.isOnline = isOnline;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getBifCodeId() {
        return bifCodeId;
    }

    public void setBifCodeId(String bifCodeId) {
        this.bifCodeId = bifCodeId;
    }

    public Integer getIsCapitalPool() {
        return isCapitalPool;
    }

    public void setIsCapitalPool(Integer isCapitalPool) {
        this.isCapitalPool = isCapitalPool;
    }

    public String getAbcSecondaryAccount() {
        return abcSecondaryAccount;
    }

    public void setAbcSecondaryAccount(String abcSecondaryAccount) {
        this.abcSecondaryAccount = abcSecondaryAccount;
    }

    public String getPrimaryAccount() {
        return primaryAccount;
    }

    public void setPrimaryAccount(String primaryAccount) {
        this.primaryAccount = primaryAccount;
    }

    public String getBasicAccountSign() {
        return basicAccountSign;
    }

    public void setBasicAccountSign(String basicAccountSign) {
        this.basicAccountSign = basicAccountSign;
    }

    public Integer getIsDomesticBank() {
        return isDomesticBank;
    }

    public void setIsDomesticBank(Integer isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }

    public String getNatureCode() {
        return natureCode;
    }

    public void setNatureCode(String natureCode) {
        this.natureCode = natureCode;
    }

    public String getBankOrgCode() {
        return bankOrgCode;
    }

    public void setBankOrgCode(String bankOrgCode) {
        this.bankOrgCode = bankOrgCode;
    }

    public String getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(String attributeId) {
        this.attributeId = attributeId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
}
