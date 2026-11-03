package com.bytter.biz.creditRating.entity;


import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * 机构数据
 */
@Entity
@Table(name = "INSTITUTIONAL_RAW_DATA",schema = "")
public class CrInstitutionalRawData extends IdEntity implements Serializable {
    /**
     * 机构类别
     */
    private String institutionType;
    /**
     * 机构名称
     */
    private String institutionName;

    private String institutionNameCode;
    /**
     * 机构编码
     */
    private String institutionCode;
    /**
     * 银行种类:001-中央银行;002-国有商业银行;003-股份制商业银行;004-政策性银行;005-城市商业银行;006-农村商业银行;007-外资银行;008-其他
     */
    private String bankTypeCode;

    private String bankType;

    /**
     * 金融许可证机构编码
     */
    private String financialLicenseInstCode;
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
    private BigInteger employeeCount;
    /**
     * 经营状态
     */
    private String businessStatus;

    /**
     * 数据流转状态：000-未准入;001-准入审批中;095-已准入; -1-打回；-2-已删除;
     */
    private String status;
    /**
     * 原始数据id
     */
    private String rawDataId;

    /**
     * 银行类型表Id
     */
    private String bankTypeId;

    /**
     *支行开户行行号
     */
    private String bankCode;
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

    @Column(name ="INSTITUTION_TYPE", length=32)
    public String getInstitutionType() {
        return institutionType;
    }

    public void setInstitutionType(String institutionType) {
        this.institutionType = institutionType;
    }

    @Column(name ="INSTITUTION_NAME", length=256)
    public String getInstitutionName() {
        return institutionName;
    }

    public void setInstitutionName(String institutionName) {
        this.institutionName = institutionName;
    }

    @Column(name ="INSTITUTION_NAME_CODE", length=128)
    public String getInstitutionNameCode() {
        return institutionNameCode;
    }

    public void setInstitutionNameCode(String institutionNameCode) {
        this.institutionNameCode = institutionNameCode;
    }

    @Column(name ="INSTITUTION_CODE", length=128)
    public String getInstitutionCode() {
        return institutionCode;
    }

    public void setInstitutionCode(String institutionCode) {
        this.institutionCode = institutionCode;
    }

    @Column(name ="BANK_TYPE_CODE", length=4)
    public String getBankTypeCode() {
        return bankTypeCode;
    }

    public void setBankTypeCode(String bankTypeCode) {
        this.bankTypeCode = bankTypeCode;
    }

    @Column(name ="FINANCIAL_LICENSE_INST_CODE", length=200)
    public String getFinancialLicenseInstCode() {
        return financialLicenseInstCode;
    }

    public void setFinancialLicenseInstCode(String financialLicenseInstCode) {
        this.financialLicenseInstCode = financialLicenseInstCode;
    }


    @Column(name ="UNIFIED_SOCIAL_CREDIT_CODE", length=100)
    public String getUnifiedSocialCreditCode() {
        return unifiedSocialCreditCode;
    }

    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
    }

    @Column(name ="REGIONAL_CODE", length=4)
    public String getRegionalCode() {
        return regionalCode;
    }

    public void setRegionalCode(String regionalCode) {
        this.regionalCode = regionalCode;
    }

    @Column(name ="PLACE", length=512)
    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    @Column(name ="PROV", length=32)
    private String prov;

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
    }

    @Column(name ="CITY", length=32)
    private String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Column(name ="REGISTERED_CAPITAL", length=12)
    public BigDecimal getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(BigDecimal registeredCapital) {
        this.registeredCapital = registeredCapital;
    }

    @Column(name ="START_BUSINESS_PERIOD")
    public Date getStartBusinessPeriod() {
        return startBusinessPeriod;
    }

    public void setStartBusinessPeriod(Date startBusinessPeriod) {
        this.startBusinessPeriod = startBusinessPeriod;
    }

    @Column(name ="END_BUSINESS_PERIOD")
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

    @Column(name ="EMPLOYEE_COUNT", length = 128)
    public BigInteger getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(BigInteger employeeCount) {
        this.employeeCount = employeeCount;
    }

    @Column(name ="BUSINESS_STATUS", length = 4)
    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    @Column(name ="STATUS", length = 4)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name ="RAW_DATA_ID", length = 4)
    public String getRawDataId() {
        return rawDataId;
    }

    public void setRawDataId(String rawDataId) {
        this.rawDataId = rawDataId;
    }

    @Column(name ="BANK_TYPE_ID", length = 4)
    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    @Column(name ="BANK_CODE", length = 4)
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
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
        return "CrInstitutionalRawData{" +
                "institutionType='" + institutionType + '\'' +
                ", institutionName='" + institutionName + '\'' +
                ", institutionCode='" + institutionCode + '\'' +
                ", bankTypeCode='" + bankTypeCode + '\'' +
                ", financialLicenseInstCode='" + financialLicenseInstCode + '\'' +
                ", unifiedSocialCreditCode='" + unifiedSocialCreditCode + '\'' +
                ", regionalCode='" + regionalCode + '\'' +
                ", place='" + place + '\'' +
                ", registeredCapital=" + registeredCapital +
                ", startBusinessPeriod=" + startBusinessPeriod +
                ", endBusinessPeriod=" + endBusinessPeriod +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", employeeCount=" + employeeCount +
                ", businessStatus='" + businessStatus + '\'' +
                ", status='" + status + '\'' +
                ", rawDataId='" + rawDataId + '\'' +
                ", bankTypeId='" + bankTypeId + '\'' +
                ", bankCode='" + bankCode + '\'' +
                ", rmk='" + rmk + '\'' +
                ", creator='" + creator + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
