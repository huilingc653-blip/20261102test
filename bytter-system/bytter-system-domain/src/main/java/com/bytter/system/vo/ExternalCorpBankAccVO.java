package com.bytter.system.vo;

import com.bytter.core.annotation.*;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhoul on 2017/6/21.
 */
public class ExternalCorpBankAccVO extends ExcelModel implements Serializable {
    private String id;

    @BtCompositeSearch(fieldName = "t.external_corp_id")
    private String externalCorpId;

    /*银行帐号-账户*/
    @BtCompositeSearch(fieldName = "t.external_acc")
    @Excel(name = "*账户")
    @NotNull(message = "sys.externalCorpBankAcc.message.externalAcc.null")
    private String externalAcc;

    /*银行名称-开户行*/
    @BtCompositeSearch(fieldName = "t.bank")
    @Excel(name = "开户行")
    private String bank;

    /*银行类型*/
    private String bankType;

    private String bankTypeName;

    /*联行号*/
    @Excel(name = "*联行号")
    @NotNull(message = "sys.externalCorpBankAcc.message.bankSourceCode.null")
    private String bankSourceCode;

    /*首选帐号*/
    @Excel(name = "首选账号")
    @Dict(dictGroup = "sf_yn",inject = "isDefaultAccStr")
    private String isDefaultAcc;
    private String isDefaultAccStr;

    @Region(inject = "accProvName")
    private String accProvince;

    /**国外客商所在国*/
    private String accProvinceFexName;

    /*账号所在省*/
    @Excel(name = "账号所在省")
    private String accProvName;

    @Region(inject = "accCityName")
    private String accCity;
    /*账号所在市*/
    @Excel(name = "账号所在市")
    private String accCityName;
    /**国外客商所在城市*/
    private String accCityFexName;

    private String agents;

    /*备注*/
    @Excel(name = "备注")
    private String remark;

    /*是否签约*/
    private String isSignAcc;

    /*备注*/
    @Excel(name = "账户识别类型")
    private String identifyType;

    /**
     * 统一社会信用代码
     */
    private String socCode;

    /**
     * 关联账户名称
     */
    @BtCompositeSearch(fieldName = "t.external_acc_name")
    private String externalAccName;

    /**
     * 账户币种
     */
    @BtCompositeSearch(fieldName = "t.cur_id")
    @Currencys(
            {
                    @Currency(inject="curName"),
                    @Currency(inject="englishCode", field = "englishCode")
            }
    )
    private String curId;

    /**
     * 账户币种名称
     */
    private String curName;

    private String englishCode;

    /**
     * 支付指令类型 1-标准结算指令
     */
    private String payExcType;

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
     * swift Code
     */
    private String swiftCode;

    /**
     * 本地清算号
     */
    private String localClearNo;

    /**客商字段 */

    /**
     * 客商编号
     */
    @BtCompositeSearch(fieldName="ec.code")
    private String ecCode;

    /**
     * 客商名称
     */
    @BtCompositeSearch(fieldName = "ec.name")
    private String ecName;

    /**
     * 客商名称(英文)
     */
    private String ecNameEn;

    /**
     * 客商属性 00一般客商01黑名单02白名单
     * */
    @Dict(dictGroup = "SYS_BUS_ENTITY_TYPE", inject = "bwTypeName")
    private String bwType;
    @BtCompositeSearch(fieldName="ec.bw_type")
    private String bwTypeName;

    /*是否国内*/
    @BtCompositeSearch(fieldName="ec.is_Native")
    private Integer isNative;

    /**
     * 地址（组件回写）
     */
    private String address;
    private String address1;
    private String externalAddress;

    /** 地区ID */
    private String countryId;

    /**
     * 客商国家地区
     */
    private String country;

    /**银行信息*/

    /**
     * 银行国家地区
     */
    private String bankCountry;

    /**
     * 银行地址
     */
    private String bankAddress;

    /**
     * 中转行地址
     */
    private String intermediaryBankAddress;

    /** 交易对手所在省 */
    @Region(inject = "externalProName")
    private String externalPro;

    /** 交易对手所在市 */
    @Region(inject = "externalCityName")
    private String externalCity;

    private String externalProName;
    private String externalCityName;

    private String outExternalPro;
    private String outExternalCity;
    private String outExternalProName;
    private String outExternalCityName;

    private String bankOutCountryId;
    private String bankOutExternalPro;
    private String bankOutExternalCity;
    private String bankOutExternalProName;
    private String bankOutExternalCityName;

    /**交易对手银行对应的国家地区*/
    private String bankCountryId;

    public String getIntermediaryBankAddress() {
        return intermediaryBankAddress;
    }

    public void setIntermediaryBankAddress(String intermediaryBankAddress) {
        this.intermediaryBankAddress = intermediaryBankAddress;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getEnglishCode() {
        return englishCode;
    }

    public void setEnglishCode(String englishCode) {
        this.englishCode = englishCode;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }


    public String getExternalAccName() {
        return externalAccName;
    }

    public void setExternalAccName(String externalAccName) {
        this.externalAccName = externalAccName;
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

    public String getIdentifyType() {
        return identifyType;
    }

    public void setIdentifyType(String identifyType) {
        this.identifyType = identifyType;
    }

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

    public String getIsDefaultAccStr() {
        return isDefaultAccStr;
    }

    public void setIsDefaultAccStr(String isDefaultAccStr) {
        this.isDefaultAccStr = isDefaultAccStr;
    }

    public String getAccProvince() {
        return accProvince;
    }

    public void setAccProvince(String accProvince) {
        this.accProvince = accProvince;
    }

    public String getAccProvName() {
        return accProvName;
    }

    public void setAccProvName(String accProvName) {
        this.accProvName = accProvName;
    }

    public String getAccCity() {
        return accCity;
    }

    public void setAccCity(String accCity) {
        this.accCity = accCity;
    }

    public String getAccCityName() {
        return accCityName;
    }

    public void setAccCityName(String accCityName) {
        this.accCityName = accCityName;
    }

    public String getAccCityFexName() {
        return accCityFexName;
    }

    public void setAccCityFexName(String accCityFexName) {
        this.accCityFexName = accCityFexName;
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

    public String getIsSignAcc() {
        return isSignAcc;
    }

    public void setIsSignAcc(String isSignAcc) {
        this.isSignAcc = isSignAcc;
    }

    @Override
    public String getBeanName() {
        return "externalCorpBankAccService";
    }

    public String getAccProvinceFexName() {
        return accProvinceFexName;
    }

    public void setAccProvinceFexName(String accProvinceFexName) {
        this.accProvinceFexName = accProvinceFexName;
    }

    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }

    public String getEcCode() {
        return ecCode;
    }

    public void setEcCode(String ecCode) {
        this.ecCode = ecCode;
    }

    public String getEcName() {
        return ecName;
    }

    public void setEcName(String ecName) {
        this.ecName = ecName;
    }

    public String getEcNameEn() {
        return ecNameEn;
    }

    public void setEcNameEn(String ecNameEn) {
        this.ecNameEn = ecNameEn;
    }

    public String getBwType() {
        return bwType;
    }

    public void setBwType(String bwType) {
        this.bwType = bwType;
    }

    public String getBwTypeName() {
        return bwTypeName;
    }

    public void setBwTypeName(String bwTypeName) {
        this.bwTypeName = bwTypeName;
    }

    public Integer getIsNative() {
        return isNative;
    }

    public void setIsNative(Integer isNative) {
        this.isNative = isNative;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBankCountry() {
        return bankCountry;
    }

    public void setBankCountry(String bankCountry) {
        this.bankCountry = bankCountry;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getExternalPro() {
        return externalPro;
    }

    public void setExternalPro(String externalPro) {
        this.externalPro = externalPro;
    }

    public String getExternalCity() {
        return externalCity;
    }

    public void setExternalCity(String externalCity) {
        this.externalCity = externalCity;
    }

    public String getExternalProName() {
        return externalProName;
    }

    public void setExternalProName(String externalProName) {
        this.externalProName = externalProName;
    }

    public String getExternalCityName() {
        return externalCityName;
    }

    public void setExternalCityName(String externalCityName) {
        this.externalCityName = externalCityName;
    }

    public String getOutExternalPro() {
        return outExternalPro;
    }

    public void setOutExternalPro(String outExternalPro) {
        this.outExternalPro = outExternalPro;
    }

    public String getOutExternalCity() {
        return outExternalCity;
    }

    public void setOutExternalCity(String outExternalCity) {
        this.outExternalCity = outExternalCity;
    }

    public String getOutExternalProName() {
        return outExternalProName;
    }

    public void setOutExternalProName(String outExternalProName) {
        this.outExternalProName = outExternalProName;
    }

    public String getOutExternalCityName() {
        return outExternalCityName;
    }

    public void setOutExternalCityName(String outExternalCityName) {
        this.outExternalCityName = outExternalCityName;
    }

    public String getBankOutCountryId() {
        return bankOutCountryId;
    }

    public void setBankOutCountryId(String bankOutCountryId) {
        this.bankOutCountryId = bankOutCountryId;
    }

    public String getBankOutExternalPro() {
        return bankOutExternalPro;
    }

    public void setBankOutExternalPro(String bankOutExternalPro) {
        this.bankOutExternalPro = bankOutExternalPro;
    }

    public String getBankOutExternalCity() {
        return bankOutExternalCity;
    }

    public void setBankOutExternalCity(String bankOutExternalCity) {
        this.bankOutExternalCity = bankOutExternalCity;
    }

    public String getBankOutExternalProName() {
        return bankOutExternalProName;
    }

    public void setBankOutExternalProName(String bankOutExternalProName) {
        this.bankOutExternalProName = bankOutExternalProName;
    }

    public String getBankOutExternalCityName() {
        return bankOutExternalCityName;
    }

    public void setBankOutExternalCityName(String bankOutExternalCityName) {
        this.bankOutExternalCityName = bankOutExternalCityName;
    }

    public String getBankCountryId() {
        return bankCountryId;
    }

    public void setBankCountryId(String bankCountryId) {
        this.bankCountryId = bankCountryId;
    }

    public String getExternalAddress() {
        return externalAddress;
    }

    public void setExternalAddress(String externalAddress) {
        this.externalAddress = externalAddress;
    }
}
