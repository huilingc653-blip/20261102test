package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 客商快捷支付信息实体
 */
@Entity
@Table(name = "SYS_EXTERNAL_CORP_QUICK_PAY")
public class ExternalCorpQuickPay extends IdEntity implements Serializable {
    /**
     * 客商ID
     */
    private String externalCorpId;

    /**
     * 资金收付产品ID
     */
    private String proId;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 联系电话
     */
    private String tel;

    /**
     * FPS ID
     */
    private String fpsId;

    /**
     * 信用卡号
     */
    private String creditCard;

    /**
     * 香港身份证ID
     */
    private String hkId;

    /**
     * 首选账号
     */
    private String isDefaultAcc;

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

    @Column(name = "EXTERNAL_CORP_ID")
    public String getExternalCorpId() {
        return externalCorpId;
    }

    public void setExternalCorpId(String externalCorpId) {
        this.externalCorpId = externalCorpId;
    }

    @Column(name = "PRO_ID")
    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "COUNTRY_CODE")
    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    @Column(name = "TEL")
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name = "FPS_ID")
    public String getFpsId() {
        return fpsId;
    }

    public void setFpsId(String fpsId) {
        this.fpsId = fpsId;
    }

    @Column(name = "CREDIT_CARD")
    public String getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(String creditCard) {
        this.creditCard = creditCard;
    }

    @Column(name = "HK_ID")
    public String getHkId() {
        return hkId;
    }

    public void setHkId(String hkId) {
        this.hkId = hkId;
    }

    @Column(name = "IS_DEFAULT_ACC")
    public String getIsDefaultAcc() {
        return isDefaultAcc;
    }

    public void setIsDefaultAcc(String isDefaultAcc) {
        this.isDefaultAcc = isDefaultAcc;
    }

    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
}
