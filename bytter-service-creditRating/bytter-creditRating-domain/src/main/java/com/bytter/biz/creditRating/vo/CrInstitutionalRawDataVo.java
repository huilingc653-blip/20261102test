package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class CrInstitutionalRawDataVo implements Serializable, IBusinessKey {
    private Integer orderNum;

    private String id;

    @Dict(dictGroup = "INSTITUTIONA_TYPE", inject = "institutionTypeName")
    private String institutionType;


    private String institutionTypeName;
    /**
     * 机构名称
     */
    private String institutionName;

    /**
     * 机构名称
     */
    private String institutionNameCode;
    /**
     * 机构编码
     */
    private String institutionCode;
    /**
     * 银行种类:001-中央银行;002-国有商业银行;003-股份制商业银行;004-政策性银行;005-城市商业银行;006-农村商业银行;007-外资银行;008-其他
     */
    @Dict(dictGroup = "sys.bankType", inject = "bankTypeName")
    private String bankType;

    private String bankTypeName;
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
     * 省
     */
    private String prov;

    /**
     * 市
     */
    private String city;

    /**
     * 注册资本
     */
    private BigDecimal RegisteredCapital;
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
     * 经营状态中文描述
     */
    private String businessStatusName;

    /**
     * 数据流转状态：000-未准入; -1 准入打回;001-准入审批中;095-已准入；-2 已删除;
     */
    @Dict(dictGroup = "institution_status", inject = "statusName")
    private String status;


    private String statusName;

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

    @BtCompositeSearch(fieldName = "ird.create_date")
    private Date createDate;

    @Override
    public String getBusinessKey() {
        return getId();
    }

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getInstitutionType() {
//        return institutionType;
//    }
//
//    public void setInstitutionType(String institutionType) {
//        this.institutionType = institutionType;
//    }
//
//    public String getInstitutionTypeName() {
//        return institutionTypeName;
//    }
//
//    public void setInstitutionTypeName(String institutionTypeName) {
//        this.institutionTypeName = institutionTypeName;
//    }
//
//    public String getInstitutionName() {
//        return institutionName;
//    }
//
//    public void setInstitutionName(String institutionName) {
//        this.institutionName = institutionName;
//    }
//
//    public String getInstitutionCode() {
//        return institutionCode;
//    }
//
//    public void setInstitutionCode(String institutionCode) {
//        this.institutionCode = institutionCode;
//    }
//
//    public String getBankType() {
//        return bankType;
//    }
//
//    public void setBankType(String bankType) {
//        this.bankType = bankType;
//    }
//
//    public String getBankTypeName() {
//        return bankTypeName;
//    }
//
//    public void setBankTypeName(String bankTypeName) {
//        this.bankTypeName = bankTypeName;
//    }
//
//    public String getFinancialLicenseInstitutionCode() {
//        return financialLicenseInstitutionCode;
//    }
//
//    public void setFinancialLicenseInstitutionCode(String financialLicenseInstitutionCode) {
//        this.financialLicenseInstitutionCode = financialLicenseInstitutionCode;
//    }
//
//    public String getUnifiedSocialCreditCode() {
//        return unifiedSocialCreditCode;
//    }
//
//    public void setUnifiedSocialCreditCode(String unifiedSocialCreditCode) {
//        this.unifiedSocialCreditCode = unifiedSocialCreditCode;
//    }
//
//    public String getRegionalCode() {
//        return regionalCode;
//    }
//
//    public void setRegionalCode(String regionalCode) {
//        this.regionalCode = regionalCode;
//    }
//
//    public String getPlace() {
//        return place;
//    }
//
//    public void setPlace(String place) {
//        this.place = place;
//    }
//
//    public BigDecimal getRegisteredCapital() {
//        return RegisteredCapital;
//    }
//
//    public void setRegisteredCapital(BigDecimal registeredCapital) {
//        RegisteredCapital = registeredCapital;
//    }
//
//    public Date getStartBusinessPeriod() {
//        return startBusinessPeriod;
//    }
//
//    public void setStartBusinessPeriod(Date startBusinessPeriod) {
//        this.startBusinessPeriod = startBusinessPeriod;
//    }
//
//    public Date getEndBusinessPeriod() {
//        return endBusinessPeriod;
//    }
//
//    public void setEndBusinessPeriod(Date endBusinessPeriod) {
//        this.endBusinessPeriod = endBusinessPeriod;
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public BigInteger getEmployeeCount() {
//        return employeeCount;
//    }
//
//    public void setEmployeeCount(BigInteger employeeCount) {
//        this.employeeCount = employeeCount;
//    }
//
//    public String getBusinessStatus() {
//        return businessStatus;
//    }
//
//    public void setBusinessStatus(String businessStatus) {
//        this.businessStatus = businessStatus;
//    }
//
//    public String getBusinessStatusName() {
//        return businessStatusName;
//    }
//
//    public void setBusinessStatusName(String businessStatusName) {
//        this.businessStatusName = businessStatusName;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getStatusName() {
//        return statusName;
//    }
//
//    public void setStatusName(String statusName) {
//        this.statusName = statusName;
//    }
//
//    public String getRawDataId() {
//        return rawDataId;
//    }
//
//    public void setRawDataId(String rawDataId) {
//        this.rawDataId = rawDataId;
//    }
//
//    public String getRmk() {
//        return rmk;
//    }
//
//    public void setRmk(String rmk) {
//        this.rmk = rmk;
//    }
//
//    public String getCreator() {
//        return creator;
//    }
//
//    public void setCreator(String creator) {
//        this.creator = creator;
//    }
//
//    public Date getCreateDate() {
//        return createDate;
//    }
//
//    public void setCreateDate(Date createDate) {
//        this.createDate = createDate;
//    }
}
