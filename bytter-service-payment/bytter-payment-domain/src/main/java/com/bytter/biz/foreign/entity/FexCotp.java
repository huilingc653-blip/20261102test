package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 交易对手
 */

@Entity
@Table(name = "FEX_COTP", schema = "")
@SuppressWarnings("serial")
public class FexCotp extends IdEntity implements java.io.Serializable {

    /**
     *交易对手/组名称
     */
    private String cotpName;

    /**
     * 单位id
     */
    private String corpId;

    /**
     *短名称
     */
    private String shortName;

    /**
     *正式名称
     */
    private String officialName;

    /**
     *所属组
     */
    private String parentId;

    /**
     *层级 0-交易对手组 1-交易对手
     */
    private String cotpLevel;

    /**
     * 状态
     */
    private Integer status;

    /**
     *交易对手类别
     */
    private String cotpType;

    /**
     *邮政地址
     */
    private String postAddr;

    /**
     *街道地址(地址1)
     */
    private String streetAddr;

    /**
     *楼栋编号(地址2)
     */
    private String buildingNumber;

    /**
     * 省/州
     */
    private String state;

    /**
     *电话/电报/传真号码
     */
    private String tel;

    /**
     *国家/地区
     */
    private String countryId;

    /**
     *地点
     */
    private String address;

    /**
     *BIC代码
     */
    private String BicCode;

    /**
     *行号
     */
    private String bankCode;

    /**
     * 备注
     */
    private String rmk;

    /**
     *创建人登录名称
     */
    private String createBy;

    /**
     *创建日期
     */
    private Date createDate;

    /**
     *更新人登录名称
     */
    private String updateBy;

    /**
     *更新日期
     */
    private Date updateDate;

    /**
     *交易对手名称（英文）
     */
    private String cotpNameEn;

    /**
     *地址（英文）
     */
    private String addressEn;

    /**
     * 开户银行代码类型
     */
    private String clsFlag;
    /**
     * 本地清算号
     */
    private String localClearNo;

    /**
     * 所属省
     */
    private String prov;
    /**
     * 所属市
     */
    private String city;

    /**
     * 支付产品
     */
    private String productId;

    /**
     * FPS ID
     */
    private String fpsId;

    /**
     * 邮箱
     */
    private String eMail;

    /**
     * 国家代码
     */
    private String isdCode;

    /**
     * 电话号码
     */
    private String phoneCode;
    /**
     * 信用卡号
     */
    private String aiIn;

    /**
     * 香港身份证号
     */
    private String hkId;

    /**
     * 交易对手数据源id
     */
    private String oppId;

    private String provName;

    private String cityName;


    @Column(name ="COTP_NAME")
    public String getCotpName() {
        return cotpName;
    }

    public void setCotpName(String cotpName) {
        this.cotpName = cotpName;
    }

    @Column(name ="SHORT_NAME")
    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    @Column(name ="OFFICIAL_NAME")
    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    @Column(name ="PARENT_ID")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name ="COTP_LEVEL")
    public String getCotpLevel() {
        return cotpLevel;
    }

    public void setCotpLevel(String cotpLevel) {
        this.cotpLevel = cotpLevel;
    }

    @Column(name ="COTP_TYPE")
    public String getCotpType() {
        return cotpType;
    }

    public void setCotpType(String cotpType) {
        this.cotpType = cotpType;
    }

    @Column(name ="POST_ADDR")
    public String getPostAddr() {
        return postAddr;
    }

    public void setPostAddr(String postAddr) {
        this.postAddr = postAddr;
    }

    @Column(name ="STREET_ADDR")
    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    @Column(name ="BUILDING_NUMBER")
    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    @Column(name ="STATE")
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Column(name ="TEL")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name ="COUNTRY_ID")
    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @Column(name ="ADDRESS")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Column(name ="BIC_CODE")
    public String getBicCode() {
        return BicCode;
    }

    public void setBicCode(String bicCode) {
        BicCode = bicCode;
    }

    @Column(name ="BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="COTP_NAME_EN")
    public String getCotpNameEn() {
        return cotpNameEn;
    }

    public void setCotpNameEn(String cotpNameEn) {
        this.cotpNameEn = cotpNameEn;
    }

    @Column(name ="ADDRESS_EN")
    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    @Column(name ="CLS_FLAG")
    public String getClsFlag() {
        return clsFlag;
    }

    public void setClsFlag(String clsFlag) {
        this.clsFlag = clsFlag;
    }

    @Column(name = "LOCAL_CLEAR_NO")
    public String getLocalClearNo() {
        return localClearNo;
    }

    public void setLocalClearNo(String localClearNo) {
        this.localClearNo = localClearNo;
    }

    @Column(name = "PROV")
    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    @Column(name = "CITY")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name = "PRODUCT_ID")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Column(name = "FPS_ID")
    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    @Column(name = "E_MAIL")
    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Column(name = "ISD_CODE")
    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    @Column(name = "PHONE_CODE")
    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    @Column(name = "AIIN")
    public String getAiIn() {
        return aiIn;
    }

    public void setAiIn(String aiIn) {
        this.aiIn = aiIn;
    }

    @Column(name = "HKID")
    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    @Column(name = "OPP_ID")
    public String getOppId() {
        return oppId;
    }

    public void setOppId(String oppId) {
        this.oppId = oppId;
    }

    @Column(name = "PROV_NAME")
    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    @Column(name = "CITY_NAME")
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
