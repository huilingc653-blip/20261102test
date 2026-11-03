package com.bytter.system.vo;
import com.bytter.core.annotation.*;
import lombok.Data;

import java.io.Serializable;
/**
 * Created by zhoul on 2017/6/21.
 */
@Data
public class ExternalCorpBankAccDetailVO implements Serializable {
    private String id;
    @BtCompositeSearch(fieldName="e.name")
    private String name;
    private String nameEn;
    private String abbreviate;
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    private String province;
    private String city;
    private String address;
    private String addressEn;
    private String linkMan;
    private String tel;
    private String fax;
    private String email;
    private String sourceSystem;
    private Integer status;
    private String code;
    private String externalCorpId;
    private String externalAcc;
    private String bank;
    @BankType(inject = "bankTypeName")
    private String bankType;
    private String bankTypeId;
    private String bankTypeName;
    private String bankSourceCode;
    @Dict(dictGroup = "sf_yn", inject = "isDefaultAccName")
    private String isDefaultAcc;
    private String isDefaultAccName;
    @Region(inject = "accProvinceName")
    private String accProvince;
    private String accProvinceName;
    @Region(inject = "accCityName")
    private String accCity;
    private String accCityName;
    private String agents;
    private String remarks;
    @Dict(dictGroup = "sf_yn", inject = "isSignAccName")
    private String isSignAcc;
    private String isSignAccName;
    /*国家名*/
    private String accProvinceFexName;
    private String accCityFexName;
    /*国外账号所在地址*/
    private String accAddress;
    /*国外供应商国家*/
    private String provinceFexName;
    private String cityFexName;
    /**
     * 客商组件text值
     */
    private String inputExternalText;
    /**
     * 客商组件value值
     */
    private String inputExternalValue;
    /**
     * 客商属性 00一般客商01黑名单02白名单
     */
    @Dict(dictGroup = "SYS_BUS_ENTITY_TYPE", inject = "bwTypeName")
    private String bwType;
    /**
     * 客商属性名称
     */
    private String bwTypeName;
    /**
     * 统一社会信用代码
     */
    private String socCode;

    private String externalType;

    /*public String getSocCode() {
        return socCode;
    }*/

//    public void setSocCode(String socCode) {
//        this.socCode = socCode;
//    }
//    public String getBankTypeId() {
//        return bankTypeId;
//    }
//    public void setBankTypeId(String bankTypeId) {
//        this.bankTypeId = bankTypeId;
//    }
//    public String getBwType() {
//        return bwType;
//    }
//    public void setBwType(String bwType) {
//        this.bwType = bwType;
//    }
//    public String getBwTypeName() {
//        return bwTypeName;
//    }
//    public void setBwTypeName(String bwTypeName) {
//        this.bwTypeName = bwTypeName;
//    }
//    public String getId() {
//        return id;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getAbbreviate() {
//        return abbreviate;
//    }
//    public void setAbbreviate(String abbreviate) {
//        this.abbreviate = abbreviate;
//    }
//    public String getCorpId() {
//        return corpId;
//    }
//    public void setCorpId(String corpId) {
//        this.corpId = corpId;
//    }
//    public String getCorpName() {
//        return corpName;
//    }
//    public void setCorpName(String corpName) {
//        this.corpName = corpName;
//    }
//    public String getProvince() {
//        return province;
//    }
//    public void setProvince(String province) {
//        this.province = province;
//    }
//    public String getCity() {
//        return city;
//    }
//    public void setCity(String city) {
//        this.city = city;
//    }
//    public String getAddress() {
//        return address;
//    }
//    public void setAddress(String address) {
//        this.address = address;
//    }
//    public String getLinkMan() {
//        return linkMan;
//    }
//    public void setLinkMan(String linkMan) {
//        this.linkMan = linkMan;
//    }
//    public String getTel() {
//        return tel;
//    }
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//    public String getFax() {
//        return fax;
//    }
//    public void setFax(String fax) {
//        this.fax = fax;
//    }
//    public String getEmail() {
//        return email;
//    }
//    public void setEmail(String email) {
//        this.email = email;
//    }
//    public String getSourceSystem() {
//        return sourceSystem;
//    }
//    public void setSourceSystem(String sourceSystem) {
//        this.sourceSystem = sourceSystem;
//    }
//    public Integer getStatus() {
//        return status;
//    }
//    public void setStatus(Integer status) {
//        this.status = status;
//    }
//    public String getCode() {
//        return code;
//    }
//    public void setCode(String code) {
//        this.code = code;
//    }
//    public String getExternalCorpId() {
//        return externalCorpId;
//    }
//    public void setExternalCorpId(String externalCorpId) {
//        this.externalCorpId = externalCorpId;
//    }
//    public String getExternalAcc() {
//        return externalAcc;
//    }
//    public void setExternalAcc(String externalAcc) {
//        this.externalAcc = externalAcc;
//    }
//    public String getBank() {
//        return bank;
//    }
//    public void setBank(String bank) {
//        this.bank = bank;
//    }
//    public String getBankType() {
//        return bankType;
//    }
//    public void setBankType(String bankType) {
//        this.bankType = bankType;
//    }
//    public String getBankSourceCode() {
//        return bankSourceCode;
//    }
//    public void setBankSourceCode(String bankSourceCode) {
//        this.bankSourceCode = bankSourceCode;
//    }
//    public String getIsDefaultAcc() {
//        return isDefaultAcc;
//    }
//    public void setIsDefaultAcc(String isDefaultAcc) {
//        this.isDefaultAcc = isDefaultAcc;
//    }
//    public String getAccProvince() {
//        return accProvince;
//    }
//    public void setAccProvince(String accProvince) {
//        this.accProvince = accProvince;
//    }
//    public String getAccCity() {
//        return accCity;
//    }
//    public void setAccCity(String accCity) {
//        this.accCity = accCity;
//    }
//    public String getAgents() {
//        return agents;
//    }
//    public void setAgents(String agents) {
//        this.agents = agents;
//    }
//    public String getRemarks() {
//        return remarks;
//    }
//    public void setRemarks(String remarks) {
//        this.remarks = remarks;
//    }
//    public String getIsSignAcc() {
//        return isSignAcc;
//    }
//    public void setIsSignAcc(String isSignAcc) {
//        this.isSignAcc = isSignAcc;
//    }
//    public String getIsSignAccName() {
//        return isSignAccName;
//    }
//    public void setIsSignAccName(String isSignAccName) {
//        this.isSignAccName = isSignAccName;
//    }
//    public String getIsDefaultAccName() {
//        return isDefaultAccName;
//    }
//    public void setIsDefaultAccName(String isDefaultAccName) {
//        this.isDefaultAccName = isDefaultAccName;
//    }
//    public String getAccProvinceName() {
//        return accProvinceName;
//    }
//    public void setAccProvinceName(String accProvinceName) {
//        this.accProvinceName = accProvinceName;
//    }
//    public String getAccCityName() {
//        return accCityName;
//    }
//    public void setAccCityName(String accCityName) {
//        this.accCityName = accCityName;
//    }
//    public String getBankTypeName() {
//        return bankTypeName;
//    }
//    public void setBankTypeName(String bankTypeName) {
//        this.bankTypeName = bankTypeName;
//    }
//    public String getInputExternalText() {
//        return inputExternalText;
//    }
//    public void setInputExternalText(String inputExternalText) {
//        this.inputExternalText = inputExternalText;
//    }
//    public String getInputExternalValue() {
//        return inputExternalValue;
//    }
//    public void setInputExternalValue(String inputExternalValue) {
//        this.inputExternalValue = inputExternalValue;
//    }
//    public String getAccProvinceFexName() {
//        return accProvinceFexName;
//    }
//    public void setAccProvinceFexName(String accProvinceFexName) {
//        this.accProvinceFexName = accProvinceFexName;
//    }
//    public String getAccCityFexName() {
//        return accCityFexName;
//    }
//    public void setAccCityFexName(String accCityFexName) {
//        this.accCityFexName = accCityFexName;
//    }
//    public String getAccAddress() {
//        return accAddress;
//    }
//    public void setAccAddress(String accAddress) {
//        this.accAddress = accAddress;
//    }
//    public String getProvinceFexName() {
//        return provinceFexName;
//    }
//    public void setProvinceFexName(String provinceFexName) {
//        this.provinceFexName = provinceFexName;
//    }
//    public String getCityFexName() {
//        return cityFexName;
//    }
//    public void setCityFexName(String cityFexName) {
//        this.cityFexName = cityFexName;
//    }
}
