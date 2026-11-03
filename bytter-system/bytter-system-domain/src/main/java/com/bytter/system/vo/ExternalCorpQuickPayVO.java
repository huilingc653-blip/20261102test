package com.bytter.system.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.utils.EmptyUtils;


import java.util.Date;

public class ExternalCorpQuickPayVO implements java.io.Serializable {

    /**
     * 客商ID
     */
    private String id;

    /**
     * 客商ID
     */
    private String externalCorpId;

    /**
     * 资金收付产品ID
     */
    private String proId;

    /**
     * 资金收付产品名称
     */
    private String proName;

    /**
     * 邮箱
     */
    @BtCompositeSearch(fieldName = "sp.email")
    private String email;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 联系电话
     */
    @BtCompositeSearch(fieldName = "sp.tel")
    private String tel;

    /**
     * 联系电话全
     */
    private String phone;

    /**
     * FPS ID
     */
    @BtCompositeSearch(fieldName = "sp.fps_id")
    private String fpsId;

    /**
     * 信用卡号
     */
    @BtCompositeSearch(fieldName = "sp.credit_card")
    private String creditCard;

    /**
     * 香港身份证ID
     */
    @BtCompositeSearch(fieldName = "sp.hk_id")
    private String hkId;

    /**
     * 首选账号
     */
    @Dict(dictGroup = "sf_yn",inject = "isDefaultAccStr")
    private String isDefaultAcc;
    private String isDefaultAccStr;

    /**
     * 创建时间
     */
    private Date createDate;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 更新时间
     */
    private Date updateDate;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 客商编号
     */
    @BtCompositeSearch(fieldName = "ec.code")
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
    @BtCompositeSearch(fieldName="ec.bw_type")
    private String bwType;
    @BtCompositeSearch(fieldName="ec.bw_type")
    private String bwTypeName;

    private String addressEn;

    private String countryId;
    private String countryName;
    private String prov;
    private String provName;
    private String city;
    private String cityName;

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
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

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPhone() {
        if(EmptyUtils.isEmpty(countryCode) || EmptyUtils.isEmpty(tel)){
            return "";
        }
        return countryCode+"-"+tel;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
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

    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
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

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
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
}
