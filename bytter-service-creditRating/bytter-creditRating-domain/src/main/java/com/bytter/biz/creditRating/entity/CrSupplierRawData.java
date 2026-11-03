package com.bytter.biz.creditRating.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 供应商数据
 */
@Entity
@Table(name = "SUPPLIER_RAW_DATA", schema ="")
public class CrSupplierRawData extends IdEntity implements Serializable {

    /**
     * 供应商类别
     */
    private String supplierType;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 供应商编码
     */
    private String supplierCode;
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    /**
     * 国际/地区编码
     */
    private String regionalCode;
    /**
     * 地点
     */
    private String place;
    /**
     * 注册资本
     */
    private BigDecimal registeredCapital;
    /**
     * 开始营业期限
     */
    private Date startBusinessPeriod;
    /**
     * 结束营业期限
     */
    private Date endBusinessPeriod;
    /**
     * 电话
     */
    private String tel;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 人员规模
     */
    private Integer employeeCount;
    /**
     * 经营状态
     */
    private String supplierStatus;
    /**
     * 原始数据id
     */
    private String rawDataId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人
     */
    private String creator;
    /**
     * 创建日期
     */
    private Date createDate;

    @Column(name ="SUPPLIER_TYPE", length = 32)
    public String getSupplierType() {
        return supplierType;
    }

    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }

    @Column(name ="SUPPLIER_NAME", length = 256)
    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Column(name ="SUPPLIER_CODE", length = 128)
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Column(name ="UNIFIED_SOCIAL_CREDIT_CODE", length = 100)
    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    @Column(name ="REGIONAL_CODE", length = 4)
    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
    }

    @Column(name ="PLACE", length = 512)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Column(name ="REGISTERED_CAPITAL", length = 12)
    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    @Column(name ="START_BUSINESS_PERIOD", length = 12)
    public Date getStartBusinessPeriod() {
        return startBusinessPeriod;
    }

    public void setStartBusinessPeriod(Date startBusinessPeriod) {
        this.startBusinessPeriod = startBusinessPeriod;
    }

    @Column(name ="END_BUSINESS_PERIOD", length = 12)
    public Date getEndBusinessPeriod() {
        return endBusinessPeriod;
    }

    public void setEndBusinessPeriod(Date endBusinessPeriod) {
        this.endBusinessPeriod = endBusinessPeriod;
    }

    @Column(name ="TEL", length = 64)
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Column(name ="EMAIL", length = 128)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name ="EMPLOYEE_COUNT")
    public Integer getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Integer employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Column(name ="SUPPLIER_STATUS", length = 32)
    public String getSupplierStatus() {
        return supplierStatus;
    }

    public void setSupplierStatus(String supplierStatus) {
        this.supplierStatus = supplierStatus;
    }

    @Column(name ="RAW_DATA_ID", length = 32)
    public String getRawDataId() {
        return rawDataId;
    }

    public void setRawDataId(String rawDataId) {
        this.rawDataId = rawDataId;
    }

    @Column(name ="RMK", length = 300)
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="CREATOR", length = 100)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Column(name ="CREATE_DATE", length = 100)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "CrSupplierRawData{" +
                "supplierType='" + supplierType + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", supplierCode='" + supplierCode + '\'' +
                ", unifiedSocialCreditCode='" + unifiedSocialCreditCode + '\'' +
                ", regionalCode='" + regionalCode + '\'' +
                ", place='" + place + '\'' +
                ", registeredCapital=" + registeredCapital +
                ", startBusinessPeriod=" + startBusinessPeriod +
                ", endBusinessPeriod=" + endBusinessPeriod +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", employeeCount=" + employeeCount +
                ", supplierStatus='" + supplierStatus + '\'' +
                ", rawDataId='" + rawDataId + '\'' +
                ", rmk='" + rmk + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
