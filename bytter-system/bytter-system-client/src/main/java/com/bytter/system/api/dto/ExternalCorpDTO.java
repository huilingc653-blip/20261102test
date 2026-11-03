package com.bytter.system.api.dto;
import java.io.Serializable;
import java.util.List;
/**
 * Created by zhoul on 2017/6/21.
 */
public class ExternalCorpDTO implements Serializable {
    private String id;
    private String name;
    private String abbreviate;
    private String corpId;
    private String province;
    private String city;
    private String address;
    private String nameEn;
    private String addressEn;
    private String provinceState;
    private String linkMan;
    private String tel;
    private String fax;
    private String email;
    private String sourceSystem;
    private Integer status;
    private String code;
    private String isCommon; //是否保存为共享客商
    /**
     * 是否国内
     */
    private Integer isNative;

    /**
     * 地址1
     */
    private String address1;

    /**
     * 地址2
     */
    private String address2;


    /**
     * 客商类型(0-客户,1-供应商,2-客户/供应商)
     */
    private String externalType;


    private String cityName;
    private String provName;


    /**
     * 国家/地区
     */
    private String countryId;
    private String countryName;
    private String countryNameEn;

    private List<ExternalCorpBankAccDTO> externalCorpBankAccList;
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
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
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
    public String getProvinceState() {
        return provinceState;
    }
    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
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
    public List<ExternalCorpBankAccDTO> getExternalCorpBankAccList() {
        return externalCorpBankAccList;
    }
    public void setExternalCorpBankAccList(List<ExternalCorpBankAccDTO> externalCorpBankAccList) {
        this.externalCorpBankAccList = externalCorpBankAccList;
    }
    public String getIsCommon() {
        return isCommon;
    }
    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }
    public Integer getIsNative() {
        return isNative;
    }
    public void setIsNative(Integer isNative) {
        this.isNative = isNative;
    }

    public String getExternalType() {
        return externalType;
    }

    public void setExternalType(String externalType) {
        this.externalType = externalType;
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

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryNameEn() {
        return countryNameEn;
    }

    public void setCountryNameEn(String countryNameEn) {
        this.countryNameEn = countryNameEn;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    @Override
    public String toString() {
        return "ExternalCorpDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", abbreviate='" + abbreviate + '\'' +
                ", corpId='" + corpId + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", sourceSystem='" + sourceSystem + '\'' +
                ", status=" + status +
                ", code='" + code + '\'' +
                ", isCommon='" + isCommon + '\'' +
                ", externalCorpBankAccList=" + externalCorpBankAccList +
                '}';
    }
}
