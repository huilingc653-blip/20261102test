package com.bytter.system.api.dto;

import java.io.Serializable;

public class BtSwiftInfoDTO implements Serializable {
    private String id;
    /**
     * swift code
     */
    private String swiftCode;
    /**
     * 机构名称
     */
    private String bankName;
    /**
     * 所在国编号
     */
    private String countryCode;
    /**
     * 所在国中文名称
     */
    private String nameZh;
    private String nameEn;
    /**
     * 银行类型名称
     */
    private String bankTypeName;
    /**
     * 所在国英文名称
     */
    private String cityName;
    /**
     * 地址
     */
    private String bankAdd;
    /**
     * 开户银行代码类型
     */
    private String clsFlag;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSwiftCode() {
        return swiftCode;
    }
    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCountryCode() {
        return countryCode;
    }
    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
    public String getNameZh() {
        return nameZh;
    }
    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getNameEn() {
        return nameEn;
    }
    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    public String getBankAdd() {
        return bankAdd;
    }
    public void setBankAdd(String bankAdd) {
        this.bankAdd = bankAdd;
    }
    public String getClsFlag() {
        return clsFlag;
    }
    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
    }
}
