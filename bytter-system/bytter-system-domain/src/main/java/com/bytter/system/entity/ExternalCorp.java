package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
/**
 * Created by zhoul on 2017/6/21.
 * !!!更改此表记得更改ExternalCorpDao 的listExternalCorpByIds方法
 */
@Entity
@Table(name = "SYS_EXTERNAL_CORP")
public class ExternalCorp extends IdEntity implements Serializable {
    /**
     * 客商名称
     */
    private String name;
    /**
     * 客商简码
     */
    private String abbreviate;
    /**
     * 所在省
     */
    private String province;
    /**
     * 所在市
     */
    private String city;
    /**
     * 地址
     */
    private String address;
    /**
     * 联系人
     */
    private String linkMan;
    /**
     * 联系电话
     */
    private String tel;
    /**
     * 传真
     */
    private String fax;
    /**
     * email邮件
     */
    private String email;
    /**
     *
     */
    private String sourceSystem;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 审核状态
     */
    private Integer auditStatus;
    /**
     * 客户编码
     */
    private String code;
    /**
     * 是否共享
     */
    private String isCommon;
    /**
     * 是否国内
     */
    private Integer isNative;
    /**
     * 客商类型(0-客户,1-供应商,2-客户/供应商)
     */
    private String externalType;
    /**
     * 客商属性 00一般客商01黑名单02白名单
     */
    private String bwType;
    /**
     * 供应商ID
     */
    private String supplierId;
    /**
     * 附件ID
     */
    private String attachmentId;
    /**
     * 提交客商资料为待审核的人
     */
    private String commitBy;
    /**
     * 统一社会信用代码
     */
    private String socCode;


    /**
     * 客商名称（英文）
     */
    private String nameEn;

    /**
     * 地址（英文）
     */
    private String addressEn;

    /**
     * 地址1
     */
    private String address1;

    /**
     * 地址2
     */
    private String address2;

    /**
     * 所在省/州
     */
    private String provinceState;

    /**
     * 邮编
     */
    private String zipCode;

    @Column(name = "SOC_CODE")
    public String getSocCode() {
        return socCode;
    }

    public void setSocCode(String socCode) {
        this.socCode = socCode;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "ABBREVIATE")
    public String getAbbreviate() {
        return abbreviate;
    }
    public void setAbbreviate(String abbreviate) {
        this.abbreviate = abbreviate;
    }
    @Column(name = "PROVINCE")
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    @Column(name = "CITY")
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    @Column(name = "ADDRESS")
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    @Column(name = "LINK_MAN")
    public String getLinkMan() {
        return linkMan;
    }
    public void setLinkMan(String linkMan) {
        this.linkMan = linkMan;
    }
    @Column(name = "TEL")
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    @Column(name = "FAX")
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    @Column(name = "EMAIL")
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    @Column(name = "SOURCE_SYSTEM")
    public String getSourceSystem() {
        return sourceSystem;
    }
    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "AUDIT_STATUS")
    public Integer getAuditStatus() {
        return auditStatus;
    }
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    @Column(name = "IS_COMMON", nullable = false)
    public String getIsCommon() {
        return isCommon;
    }
    public void setIsCommon(String isCommon) {
        this.isCommon = isCommon;
    }
    @Column(name = "IS_NATIVE")
    public Integer getIsNative() {
        return isNative;
    }
    public void setIsNative(Integer isNative) {
        this.isNative = isNative;
    }
    @Column(name = "EXTERNAL_TYPE")
    public String getExternalType() {
        return externalType;
    }
    public void setExternalType(String externalType) {
        this.externalType = externalType;
    }
    @Column(name = "SUPPLIER_ID")
    public String getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "COMMIT_BY")
    public String getCommitBy() {
        return commitBy;
    }
    public void setCommitBy(String commitBy) {
        this.commitBy = commitBy;
    }
    @Column(name = "BW_TYPE")
    public String getBwType() {
        return bwType;
    }
    public void setBwType(String bwType) {
        this.bwType = bwType;
    }


    @Column(name = "NAME_EN")
    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    @Column(name = "ADDRESS_EN")
    public String getAddressEn() {
        return addressEn;
    }

    public void setAddressEn(String addressEn) {
        this.addressEn = addressEn;
    }

    @Column(name = "ADDRESS1")
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Column(name = "ADDRESS2")
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Column(name = "PROVINCE_STATE")
    public String getProvinceState() {
        return provinceState;
    }

    public void setProvinceState(String provinceState) {
        this.provinceState = provinceState;
    }

    @Column(name = "ZIP_CODE")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    @Override
    public String toString() {
        return "ExternalCorp{" +
                "name='" + name + '\'' +
                ", abbreviate='" + abbreviate + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", linkMan='" + linkMan + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", sourceSystem='" + sourceSystem + '\'' +
                ", status=" + status +
                ", auditStatus=" + auditStatus +
                ", code='" + code + '\'' +
                ", isCommon='" + isCommon + '\'' +
                ", isNative=" + isNative +
                ", externalType='" + externalType + '\'' +
                ", bwType='" + bwType + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", attachmentId='" + attachmentId + '\'' +
                ", commitBy='" + commitBy + '\'' +
                '}';
    }
}
