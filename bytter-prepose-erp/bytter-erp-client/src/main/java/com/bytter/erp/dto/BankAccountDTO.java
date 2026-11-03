package com.bytter.erp.dto;

import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 账户同步对象
 * @author zhoul
 */
public class BankAccountDTO implements Serializable {
    /**
     * 银行账户
     */
    @NotNull
    private String bankAccount;
    /**
     * 账户名称
     */
    private String accountName;
    /**
     * 单位编码
     */
    private String corpCode;
    /**
     * 开户行行号
     */
    private String bankCode;
    /**
     * 开户日期
     */
    private String registryDate;
    /**
     * 是否上线
     */
    @Range(min = 0, max = 1, message = "是否上线只能输入0或1")
    private String isOnline;
    /**
     * 是否资金池账号
     */
    @Range(min = 0, max = 1, message = "是否资金池账号只能输入0或1")
    private String isCapitalPoll;
    /**
     * 备注
     */
    private String remark;
    /**
     * 有效标志
     */
    @Range(min = 0, max = 1, message = "有效标志只能输入0或1")
    private String validSign;
    /**
     * 客户编号id
     */
    @NotNull
    private String customerCode;

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }


    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getRegistryDate() {
        return registryDate;
    }

    public void setRegistryDate(String registryDate) {
        this.registryDate = registryDate;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public String getIsCapitalPoll() {
        return isCapitalPoll;
    }

    public void setIsCapitalPoll(String isCapitalPoll) {
        this.isCapitalPoll = isCapitalPoll;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getValidSign() {
        return validSign;
    }

    public void setValidSign(String validSign) {
        this.validSign = validSign;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    @Override
    public String toString() {
        return "BankAccountDTO{" +
                "bankAccount='" + bankAccount + '\'' +
                ", accountName='" + accountName + '\'' +
                ", corpCode='" + corpCode + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", registryDate='" + registryDate + '\'' +
                ", isOnline='" + isOnline + '\'' +
                ", isCapitalPoll='" + isCapitalPoll + '\'' +
                ", remark='" + remark + '\'' +
                ", validSign='" + validSign + '\'' +
                ", customerCode='" + customerCode + '\'' +
                '}';
    }
}
