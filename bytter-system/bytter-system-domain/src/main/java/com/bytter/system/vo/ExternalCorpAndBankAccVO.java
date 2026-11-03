package com.bytter.system.vo;

import com.bytter.core.annotation.*;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 客商资料导入VO
 *
 * @author zhoul on 2017/6/22.
 */
@ExcelTarget("ExternalCorpAndBankAccVO")
public class ExternalCorpAndBankAccVO extends ExcelModel implements Serializable {

    private String id;

    /**
     * 数据来源
     */
    private String sourceSystem;

    @Excel(name = "*数据来源")
    private String sourceSystemStr;

    /**
     * 客户名称
     */
    @Excel(name = "*客商名称")
    @Length(max = 100, message = "sys.externalCorp.name.maxLength")
    @NotNull(message = "sys.externalCorp.message.name.null")
    private String name;

    /**
     * 客商类型(0-客户,1-供应商,2-客户/供应商)
     */
    private String externalType;
    @Excel(name = "*客商类型")
    private String externalTypeStr;

    /**
     * 供应商ID
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    @Excel(name = "供应商类别")
    private String supplierName;

    /*是否国内*/
    private Integer isNative;
    @Excel(name = "*是否国内")
    private String isNativeStr;

    /**
     * 是否共享
     */
    private String isCommon;
    @Excel(name = "*是否共享")
    private String isCommonStr;


    /**
     * 客户编号
     */
    @Excel(name = "客户编号")
    @Pattern(regexp = "^\\w{0,32}+$", message = "sys.externalCorp.code.pattern")
    private String code;

    /**
     * 客户简称
     */
    @Excel(name = "客户简称")
    @Length(max = 100, message = "sys.externalCorp.maxLength")
    private String abbreviate;

    @Corps({
            @Corp(inject = "corpName"),
            @Corp(inject = "corpCode", field = "code")
    })
    private String corpId;
    @NotNull(message = "sys.externalCorp.import.externalCorp.null")
    @Excel(name = "*所属单位")
    private String corpName;
    private String corpCode;

    @Region(inject = "provName")
    private String province;
    @Excel(name = "所在省/国家/地区")
    private String provName;

    @Region(inject = "cityName")
    private String city;
    @Excel(name = "所在市/城市")
    private String cityName;

    @Excel(name = "地址/地址全称")
    @Length(max = 50, message = "sys.externalCorp.address.maxLength")
    private String address;

    @Excel(name = "联系人")
    @Length(max = 5, message = "sys.externalCorp.linkMan.maxLength")
    private String linkMan;

    @Excel(name = "联系电话")
    @Length(max = 15, message = "")
    private String tel;

    @Excel(name = "传真")
    @Length(max = 15, message = "sys.externalCorp.fax.maxLength")
    private String fax;

    @Excel(name = "email")
    @Length(max = 30, message = "sys.externalCorp.email.maxLength")
    private String email;

    @Excel(name = "*是否有效")
    private String statusString;

    private Integer status;

    /**关联银行账号的相关信息*/
    /**
     * 银行帐号-账户
     */
    @Excel(name = "*账户")
    @Length(max = 30, message = "sys.externalCorp.import.externalAcc.length")
    private String externalAcc;

    /**
     * 银行名称-开户行
     */
    @Excel(name = "开户行/机构名称")
    private String bank;

    /**
     * 联行号
     */
    @Excel(name = "联行号")
    private String bankSourceCode;

    /**
     * SWIFT CODE
     */
    @Excel(name = "SWIFT CODE")
    private String swiftCode;

    /**
     * 首选帐号
     */
    @Excel(name = "首选账号")
    private String isDefaultAccString;

    private String isDefaultAcc;

    @Region(inject = "accProvName")
    private String accProvince;
    /**
     * 账号所在省
     */
    @Excel(name = "账号所在省/国家/地区")
    private String accProvName;

    @Region(inject = "accCityName")
    private String accCity;
    /*账号所在市*/
    @Excel(name = "账号所在市/城市")
    private String accCityName;

    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /*是否签约*/
    private String isSignAcc;

    @Country(inject = "accProvinceFexName")
    private String accProvinceFex;
    /*国家名*/
    private String accProvinceFexName;
    /*账号地址*/
    private String accAddress;

    /**
     * 客商属性 00一般客商01黑名单02白名单
     */
    @Dict(dictGroup = "SYS_BUS_ENTITY_TYPE", inject = "bwTypeName")
    private String bwType;

    /**
     * 客商属性名称
     */
    @Excel(name = "客商属性")
    private String bwTypeName;

    /**
     * 客商名称（英文）
     */
    @Excel(name = "客商名称（英文）")
    private String nameEn;

    /**
     * 地址（英文）
     */
    @Excel(name = "地址（英文）")
    private String addressEn;

    /**
     * 地址1
     */
    @Excel(name = "地址1")
    private String address1;

    /**
     * 地址2
     */
    @Excel(name = "地址2")
    private String address2;

    /**
     * 所在省/州
     */
    @Excel(name = "所在省/州")
    private String provinceState;

    /**
     * 邮编
     */
    @Excel(name = "邮编")
    private String zipCode;

    /**
     * 关联账户名称
     */
    @Excel(name = "账户名称")
    private String externalAccName;

    /**
     * 账户币种
     */
    private String curId;

    @Excel(name = "*账户币种")
    private String curName;

    /**
     * 支付指令类型 1-标准结算指令
     */
    @Excel(name = "支付指令类型")
    private String payExcTypeName;

    private String payExcType;

    /**
     * 中转行
     */
    @Excel(name = "中转行")
    private String intermediaryBank;

    /**
     * 中转行SWIFT CODE
     */
    @Excel(name = "中转行SWIFT CODE")
    private String intermediaryBankSwiftCode;

    /**
     * 中转行账号
     */
    @Excel(name = "在中转行的银行账户")
    private String intermediaryBankAcc;

    /**
     * 本地清算号
     */
    @Excel(name = "本地清算号")
    private String localClearNo;

    /**
     * 资金收付产品
     */
    private String proId;
    @Excel(name = "资金收付产品")
    private String proName;

    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    @Length(max = 30, message = "sys.externalCorp.email.maxLength")
    private String quickPayEmail;

    /**
     * 国家代码
     */
    @Excel(name = "国家代码")
    private String countryCode;

    /**
     * 电话号码
     */
    @Excel(name = "电话号码")
    @Length(max = 15, message = "")
    private String quickPayTel;

    /**
     * FPSID
     */
    @Excel(name = "FPSID")
    private String fpsId;

    /**
     * 信用卡号
     */
    @Excel(name = "信用卡号")
    private String creditCard;

    /**
     * 香港身份证号
     */
    @Excel(name = "香港身份证号")
    private String hkId;

    /**
     * 首选账号
     */
    private String quickPayIsDefaultAcc;

    @Excel(name = "首选账号标识")
    private String quickPayisDefaultAccStr;

    public String getIsCommonStr() {
        return isCommonStr;
    }

    public void setIsCommonStr(String isCommonStr) {
        this.isCommonStr = isCommonStr;
    }

    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getQuickPayEmail() {
        return quickPayEmail;
    }

    public void setQuickPayEmail(String quickPayEmail) {
        this.quickPayEmail = quickPayEmail;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getQuickPayTel() {
        return quickPayTel;
    }

    public void setQuickPayTel(String quickPayTel) {
        this.quickPayTel = quickPayTel;
    }

    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    public String getQuickPayIsDefaultAcc() {
        return quickPayIsDefaultAcc;
    }

    public void setQuickPayIsDefaultAcc(String quickPayIsDefaultAcc) {
        this.quickPayIsDefaultAcc = quickPayIsDefaultAcc;
    }

    public String getQuickPayisDefaultAccStr() {
        return quickPayisDefaultAccStr;
    }

    public void setQuickPayisDefaultAccStr(String quickPayisDefaultAccStr) {
        this.quickPayisDefaultAccStr = quickPayisDefaultAccStr;
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

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getPayExcTypeName() {
        return payExcTypeName;
    }

    public void setPayExcTypeName(String payExcTypeName) {
        this.payExcTypeName = payExcTypeName;
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

    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviate() {
        return abbreviate;
    }

    public void setAbbreviate(String abbreviate) {
        this.abbreviate = abbreviate;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpCode() {
        return corpCode;
    }

    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLinkMan() {
        return linkMan;
    }

    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceSystemStr() {
        return sourceSystemStr;
    }

    public void setSourceSystemStr(String sourceSystemStr) {
        this.sourceSystemStr = sourceSystemStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public String getStatusString() {
        return statusString;
    }

    public void setStatusString(String statusString) {
        this.statusString = statusString;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDefaultAccString() {
        return isDefaultAccString;
    }

    public void setIsDefaultAccString(String isDefaultAccString) {
        this.isDefaultAccString = isDefaultAccString;
    }

    public String getExternalType() {
        return externalType;
    }

    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }

    public String getExternalTypeStr() {
        return externalTypeStr;
    }

    public void setExternalTypeStr(String externalTypeStr) {
        this.externalTypeStr = externalTypeStr;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }


    public String getIsSignAcc() {
        return isSignAcc;
    }

    public void setIsSignAcc(String isSignAcc) {
        this.isSignAcc = isSignAcc;
    }

    public Integer getIsNative() {
        return isNative;
    }

    public void setIsNative(Integer isNative) {
        this.isNative = isNative;
    }

    public String getIsNativeStr() {
        return isNativeStr;
    }

    public void setIsNativeStr(String isNativeStr) {
        this.isNativeStr = isNativeStr;
    }

    public String getAccProvinceFex() {
        return accProvinceFex;
    }

    public void setAccProvinceFex(String accProvinceFex) {
        this.accProvinceFex = accProvinceFex;
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

/*    public String getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }

    public String getIsCommonStr() {
        return isCommonStr;
    }

    public void setIsCommonStr(String isCommonStr) {
        this.isCommonStr = isCommonStr;
    }*/

    @Override
    public String getBeanName() {
        return "externalCorpService";
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
}
