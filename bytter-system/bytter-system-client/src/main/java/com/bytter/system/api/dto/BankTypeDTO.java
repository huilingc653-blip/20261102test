package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * Created by zhangxb on 2017/5/22 0022.
 */
public class BankTypeDTO implements Serializable {
    private String id;
    private String bankType;
    private String typeName;
    private String bankPrefix;
    private String validSign;
    private String isDomesticBank;
    /**
     * 开户行名称前缀，针对313和402开头的银行
     */
    private String icon;
    /**
     * 将变色
     */
    private String gradient;
    /**
     * 开户行名称前缀，针对313和402开头的银行
     */
    private String bankNamePrefix;
    /**
     * 汇款方式
     */
    private String remitMethod;
    /**
     * 授信合同是否必须
     */
    private String creditContractRequired;
    /**
     * 是否为第三方平台
     */
    private String isThirdParty;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    public String getBankPrefix() {
        return bankPrefix;
    }
    public void setBankPrefix(String bankPrefix) {
        this.bankPrefix = bankPrefix;
    }
    public String getValidSign() {
        return validSign;
    }
    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public String getGradient() {
        return gradient;
    }
    public void setGradient(String gradient) {
        this.gradient = gradient;
    }
    public String getIsDomesticBank() {
        return isDomesticBank;
    }
    public void setIsDomesticBank(String isDomesticBank) {
        this.isDomesticBank = isDomesticBank;
    }
    public String getBankNamePrefix() {
        return bankNamePrefix;
    }
    public void setBankNamePrefix(String bankNamePrefix) {
        this.bankNamePrefix = bankNamePrefix;
    }
    public String getRemitMethod() {
        return remitMethod;
    }
    public void setRemitMethod(String remitMethod) {
        this.remitMethod = remitMethod;
    }
    public String getCreditContractRequired() {
        return creditContractRequired;
    }
    public void setCreditContractRequired(String creditContractRequired) {
        this.creditContractRequired = creditContractRequired;
    }

    public String getIsThirdParty() {
        return isThirdParty;
    }

    public void setIsThirdParty(String isThirdParty) {
        this.isThirdParty = isThirdParty;
    }
}
