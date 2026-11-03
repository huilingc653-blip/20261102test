package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/6/21.
 */
@Entity
@Table(name = "SYS_EXTERNAL_CORP_BANKACC")
public class ExternalCorpBankAcc extends IdEntity implements Serializable {
    private String externalCorpId;
    /*银行帐号*/
    private String externalAcc;
    /*银行名称*/
    private String bank;
    /*银行类型*/
    private String bankType;
    /*联行号*/
    private String bankSourceCode;
    /*首选帐号*/
    private String isDefaultAcc;
    private String accProvince;
    private String accCity;
    private String agents;
    /*备注*/
    private String remark;
    /*是否签约*/
    private String isSignAcc;


    /*账户识别类型DT01票据账户，DT02银行账户*/
    private String identifyType;

    /**
     * 关联账户名称
     */
    private String externalAccName;

    /**
     * 账户币种
     */
    private String curId;

    /**
     * 支付指令类型 1-标准结算指令
     */
    private String payExcType;

    /**
     * swift Code
     */
    private String swiftCode;

    /**
     * 本地清算号
     */
    private String localClearNo;

    /**
     * 中转行
     */
    private String intermediaryBank;

    /**
     * 中转行SWIFT CODE
     */
    private String intermediaryBankSwiftCode;

    /**
     * 中转行账号
     */
    private String intermediaryBankAcc;

    /**
     * 中转行地址
     */
    private String intermediaryBankAddress;

    /**
     * 银行地址
     */
    private String bankAddr;

    /**交易对手银行对应的国家地区*/
    private String bankCountryId;


    @Column(name = "EXTERNAL_CORP_ID")
    public String getExternalCorpId() {
        return externalCorpId;
    }
    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }
    @Column(name = "EXTERNAL_ACC")
    public String getExternalAcc() {
        return externalAcc;
    }
    public void setExternalAcc(String externalAcc) {
        this.externalAcc = externalAcc;
    }
    @Column(name = "BANK")
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    @Column(name = "BANK_TYPE")
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    @Column(name = "BANK_SOURCECODE")
    public String getBankSourceCode() {
        return bankSourceCode;
    }
    public void setBankSourceCode(String bankSourceCode) {
        this.bankSourceCode = bankSourceCode;
    }
    @Column(name = "IS_DEFAULT_ACC")
    public String getIsDefaultAcc() {
        return isDefaultAcc;
    }
    public void setIsDefaultAcc(String isDefaultAcc) {
        this.isDefaultAcc = isDefaultAcc;
    }
    @Column(name = "ACC_PROVINCE")
    public String getAccProvince() {
        return accProvince;
    }
    public void setAccProvince(String accProvince) {
        this.accProvince = accProvince;
    }
    @Column(name = "ACC_CITY")
    public String getAccCity() {
        return accCity;
    }
    public void setAccCity(String accCity) {
        this.accCity = accCity;
    }
    @Column(name = "AGENTS")
    public String getAgents() {
        return agents;
    }
    public void setAgents(String agents) {
        this.agents = agents;
    }
    @Column(name = "REMARK")
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "IS_SIGN_ACC")
    public String getIsSignAcc() {
        return isSignAcc;
    }
    public void setIsSignAcc(String isSignAcc) {
        this.isSignAcc = isSignAcc;
    }

    @Column(name = "INTERMEDIARY_BANK_ADDRESS")
    public String getIntermediaryBankAddress() {
        return intermediaryBankAddress;
    }

    public void setIntermediaryBankAddress(String intermediaryBankAddress) {
        this.intermediaryBankAddress = intermediaryBankAddress;
    }

    @Column(name = "BANK_ADDR")
    public String getBankAddr() {
        return bankAddr;
    }

    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    @Column(name = "SWIFT_CODE")
    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    @Column(name = "LOCAL_CLEAR_NO")
    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    @Column(name = "EXTERNAL_ACC_NAME")
    public String getExternalAccName() {
        return externalAccName;
    }

    public void setExternalAccName(String externalAccName) {
        this.externalAccName = externalAccName;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "PAY_EXC_TYPE")
    public String getPayExcType() {
        return payExcType;
    }

    public void setPayExcType(String payExcType) {
        this.payExcType = payExcType;
    }

    @Column(name = "INTERMEDIARY_BANK")
    public String getIntermediaryBank() {
        return intermediaryBank;
    }

    public void setIntermediaryBank(String intermediaryBank) {
        this.intermediaryBank = intermediaryBank;
    }

    @Column(name = "INTERMEDIARY_BANK_SWIFT_CODE")
    public String getIntermediaryBankSwiftCode() {
        return intermediaryBankSwiftCode;
    }

    public void setIntermediaryBankSwiftCode(String intermediaryBankSwiftCode) {
        this.intermediaryBankSwiftCode = intermediaryBankSwiftCode;
    }

    @Column(name = "INTERMEDIARY_BANK_ACC")
    public String getIntermediaryBankAcc() {
        return intermediaryBankAcc;
    }

    public void setIntermediaryBankAcc(String intermediaryBankAcc) {
        this.intermediaryBankAcc = intermediaryBankAcc;
    }

    @Column(name = "identify_type")
    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

    @Column(name = "BANK_COUNTRY_ID")
    public String getBankCountryId() {
        return bankCountryId;
    }

    public void setBankCountryId(String bankCountryId) {
        this.bankCountryId = bankCountryId;
    }


}
