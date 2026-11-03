package com.bytter.system.api.dto;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/7/6.
 */
public class ExternalCorpBankAccDTO implements Serializable {
    private String id;
    private String externalCorpId;
    private String externalAcc;
    private String externalAccName;
    private String bank;
    private String bankType;
    private String bankTypeName;
    private String bankSourceCode;
    private String isDefaultAcc;
    private String accProvince;
    private String accCity;
    private String agents;
    private String remark;
    private String curId;
    private String payExcType;
    private String localClearNo;
    private String intermediaryBank;
    private String intermediaryBankSwiftCode;
    private String intermediaryBankAcc;
    private String intermediaryBankAddress;
    /**银行地址*/
    private String bankAddr;
    private String isSignAcc;
    /*国家名*/
    private String accProvinceFexName;
    /*账号地址*/
    private String accAddress;
    /**swift Code*/
    private String swiftCode;
    /**交易对手银行对应的国家地区*/
    private String bankCountryId;


    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    public String getExternalAcc() {
        return externalAcc;
    }
    public void setExternalAcc(String externalAcc) {
        this.externalAcc = externalAcc;
    }
    public String getExternalAccName() {
        return externalAccName;
    }
    public void setExternalAccName(String externalAccName) {
        this.externalAccName = externalAccName;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBankSourceCode() {
        return bankSourceCode;
    }
    public void setBankSourceCode(String bankSourceCode) {
        this.bankSourceCode = bankSourceCode;
    }
    public String getIsDefaultAcc() {
        return isDefaultAcc;
    }
    public void setIsDefaultAcc(String isDefaultAcc) {
        this.isDefaultAcc = isDefaultAcc;
    }
    public String getAccProvince() {
        return accProvince;
    }
    public void setAccProvince(String accProvince) {
        this.accProvince = accProvince;
    }
    public String getAccCity() {
        return accCity;
    }
    public void setAccCity(String accCity) {
        this.accCity = accCity;
    }
    public String getAgents() {
        return agents;
    }
    public void setAgents(String agents) {
        this.agents = agents;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getPayExcType() {
        return payExcType;
    }
    public void setPayExcType(String payExcType) {
        this.payExcType = payExcType;
    }
    public String getLocalClearNo() {
        return localClearNo;
    }
    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }
    public String getIntermediaryBank() {
        return intermediaryBank;
    }
    public void setIntermediaryBank(String intermediaryBank) {
        this.intermediaryBank = intermediaryBank;
    }
    public String getIntermediaryBankSwiftCode() {
        return intermediaryBankSwiftCode;
    }
    public void setIntermediaryBankSwiftCode(String intermediaryBankSwiftCode) {
        this.intermediaryBankSwiftCode = intermediaryBankSwiftCode;
    }
    public String getIntermediaryBankAcc() {
        return intermediaryBankAcc;
    }
    public void setIntermediaryBankAcc(String intermediaryBankAcc) {
        this.intermediaryBankAcc = intermediaryBankAcc;
    }
    public String getIntermediaryBankAddress() {
        return intermediaryBankAddress;
    }
    public void setIntermediaryBankAddress(String intermediaryBankAddress) {
        this.intermediaryBankAddress = intermediaryBankAddress;
    }
    public String getBankAddr() {
        return bankAddr;
    }
    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }
    public String getIsSignAcc() {
        return isSignAcc;
    }
    public void setIsSignAcc(String isSignAcc) {
        this.isSignAcc = isSignAcc;
    }
    public String getAccProvinceFexName() {
        return accProvinceFexName;
    }
    public void setAccProvinceFexName(String accProvinceFexName) {
        this.accProvinceFexName = accProvinceFexName;
    }
    public String getAccAddress() {
        return accAddress;
    }
    public void setAccAddress(String accAddress) {
        this.accAddress = accAddress;
    }
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankCountryId() {
        return bankCountryId;
    }

    public void setBankCountryId(String bankCountryId) {
        this.bankCountryId = bankCountryId;
    }

    @Override
    public String toString() {
        return "ExternalCorpBankAccDTO{" +
                "id='" + id + '\'' +
                ", externalCorpId='" + externalCorpId + '\'' +
                ", externalAcc='" + externalAcc + '\'' +
                ", bank='" + bank + '\'' +
                ", bankType='" + bankType + '\'' +
                ", bankTypeName='" + bankTypeName + '\'' +
                ", bankSourceCode='" + bankSourceCode + '\'' +
                ", isDefaultAcc='" + isDefaultAcc + '\'' +
                ", accProvince='" + accProvince + '\'' +
                ", accCity='" + accCity + '\'' +
                ", agents='" + agents + '\'' +
                ", remark='" + remark + '\'' +
                ", isSignAcc='" + isSignAcc + '\'' +
                ", accProvinceFexName='" + accProvinceFexName + '\'' +
                ", accAddress='" + accAddress + '\'' +
                '}';
    }
}
