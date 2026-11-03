package com.bytter.system.entity;

import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 租户实体类
 *
 * @author wuxc
 * @date 2026-01-29
 */
@Entity
@Table(name = "SYS_TENANT_MANAGER")
public class SysTenantManager extends IdEntity implements Serializable {
    /**
     * 企业客户号
     */
    private String tenantCode;
    /**
     * 企业名称
     */
    private String tenantName;
    /**
     * 企业名称（英文）
     */
    private String tenantEnglishName;
    /**
     * 邮政编码
     */
    private String postCode;
    /**
     * 企业证件类型
     */
    private String tenantCertType;
    /**
     * 企业证件号码
     */
    private String tenantCertCode;
    /**
     * 企业证件有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tenantCertIndate;
    /**
     * 企业联系电话
     */
    private String tenantPhone;
    /**
     * 企业地址
     */
    private String tenantAddr;
    /**
     * 法人/负责人
     */
    private String legalPersonName;
    /**
     * 法人/负责人联系方式
     */
    private String legalPersonMobile;
    /**
     * 法人/负责人证件类型：01-居民身份证、02-户口簿、03-护照
     */
    private String legalPersonCertType;
    /**
     * 法人/负责人证件号码
     */
    private String legalPersonCertCode;
    /**
     * 法人/负责人证件有效期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date legalPersonCertIndate;
    /**
     * 企业经办人
     */
    private String attn;
    /**
     * 经办人联系电话
     */
    private String attnPhone;
    /**
     * 经办人邮箱
     */
    private String attnEmail;
    /**
     * 签约机构id
     */
    private String corpId;
    /**
     * 签约渠道：01-柜面、02-线上网银
     */
    private String signChannels;
    /**
     * 是否同分行：0-否，1-是
     */
    private String isSameSubBank;
    /**
     * 客户经理
     */
    private String amUserId;
    /**
     * 经办用户
     */
    private String attnUserId;
    /**
     * 客户状态：1-未签约，2-已签约，3-禁用，4-已解约
     */
    private Integer tenantStatus;
    /**
     * 签约日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date signDate;
    /**
     * 启用日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date enableDate;
    /**
     * 单据状态
     */
    private Integer status;
    /**
     * 租户id；默认与签约客户的企业客户号相同
     */
    private String tenantId;
    /**
     * 双管理员模式: 0：否 1：是
     */
    private String chargeMode;

    /***
     * 附件ID
     */
    private String attachmentId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建者
     */
    private String createBy;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 修改者
     */
    private String updateBy;


    /**
     * 单位数量
     */
    private Integer corpNumber;

    /**
     * 用户数量
     */
    private Integer userNumber;

    /**
     * 银行账号数量
     */
    private Integer accountNumber;

    @Column(name = "corp_number", nullable = false, length = 5)
    public Integer getCorpNumber() {
        return corpNumber;
    }

    public void setCorpNumber(Integer corpNumber) {
        this.corpNumber = corpNumber;
    }

    @Column(name = "user_number", nullable = false, length = 5)
    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    @Column(name = "account_number", nullable = false, length = 5)
    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Column(name = "TENANT_CODE", nullable = false, length = 32)
    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    @Column(name = "TENANT_NAME", nullable = false, length = 300)
    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    @Column(name = "TENANT_ENGLISH_NAME", nullable = true, length = 300)
    public String getTenantEnglishName() {
        return tenantEnglishName;
    }

    public void setTenantEnglishName(String tenantEnglishName) {
        this.tenantEnglishName = tenantEnglishName;
    }

    @Column(name = "POST_CODE", nullable = true, length = 20)
    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    @Column(name = "TENANT_CERT_TYPE", nullable = false, length = 2)
    public String getTenantCertType() {
        return tenantCertType;
    }

    public void setTenantCertType(String tenantCertType) {
        this.tenantCertType = tenantCertType;
    }

    @Column(name = "TENANT_CERT_CODE", nullable = false, length = 50)
    public String getTenantCertCode() {
        return tenantCertCode;
    }

    public void setTenantCertCode(String tenantCertCode) {
        this.tenantCertCode = tenantCertCode;
    }

    @Column(name = "TENANT_CERT_INDATE", nullable = false)
    public Date getTenantCertIndate() {
        return tenantCertIndate;
    }

    public void setTenantCertIndate(Date tenantCertIndate) {
        this.tenantCertIndate = tenantCertIndate;
    }

    @Column(name = "TENANT_PHONE", nullable = true, length = 20)
    public String getTenantPhone() {
        return tenantPhone;
    }

    public void setTenantPhone(String tenantPhone) {
        this.tenantPhone = tenantPhone;
    }

    @Column(name = "TENANT_ADDR", nullable = true, length = 300)
    public String getTenantAddr() {
        return tenantAddr;
    }

    public void setTenantAddr(String tenantAddr) {
        this.tenantAddr = tenantAddr;
    }

    @Column(name = "LEGAL_PERSON_NAME", nullable = false, length = 100)
    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    @Column(name = "LEGAL_PERSON_MOBILE", nullable = false, length = 11)
    public String getLegalPersonMobile() {
        return legalPersonMobile;
    }

    public void setLegalPersonMobile(String legalPersonMobile) {
        this.legalPersonMobile = legalPersonMobile;
    }

    @Column(name = "LEGAL_PERSON_CERT_TYPE", nullable = false, length = 2)
    public String getLegalPersonCertType() {
        return legalPersonCertType;
    }

    public void setLegalPersonCertType(String legalPersonCertType) {
        this.legalPersonCertType = legalPersonCertType;
    }

    @Column(name = "LEGAL_PERSON_CERT_CODE", nullable = false, length = 50)
    public String getLegalPersonCertCode() {
        return legalPersonCertCode;
    }

    public void setLegalPersonCertCode(String legalPersonCertCode) {
        this.legalPersonCertCode = legalPersonCertCode;
    }

    @Column(name = "LEGAL_PERSON_CERT_INDATE", nullable = false)
    public Date getLegalPersonCertIndate() {
        return legalPersonCertIndate;
    }

    public void setLegalPersonCertIndate(Date legalPersonCertIndate) {
        this.legalPersonCertIndate = legalPersonCertIndate;
    }

    @Column(name = "ATTN", nullable = true, length = 100)
    public String getAttn() {
        return attn;
    }

    public void setAttn(String attn) {
        this.attn = attn;
    }

    @Column(name = "ATTN_PHONE", nullable = true, length = 20)
    public String getAttnPhone() {
        return attnPhone;
    }

    public void setAttnPhone(String attnPhone) {
        this.attnPhone = attnPhone;
    }

    @Column(name = "ATTN_EMAIL", nullable = true, length = 100)
    public String getAttnEmail() {
        return attnEmail;
    }

    public void setAttnEmail(String attnEmail) {
        this.attnEmail = attnEmail;
    }

    @Column(name = "CORP_ID", nullable = false, length = 32)
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "SIGN_CHANNELS", nullable = false, length = 2)
    public String getSignChannels() {
        return signChannels;
    }

    public void setSignChannels(String signChannels) {
        this.signChannels = signChannels;
    }

    @Column(name = "IS_SAME_SUB_BANK", nullable = true, length = 2)
    public String getIsSameSubBank() {
        return isSameSubBank;
    }

    public void setIsSameSubBank(String isSameSubBank) {
        this.isSameSubBank = isSameSubBank;
    }

    @Column(name = "AM_USER_ID", nullable = false, length = 32)
    public String getAmUserId() {
        return amUserId;
    }

    public void setAmUserId(String amUserId) {
        this.amUserId = amUserId;
    }

    @Column(name = "ATTN_USER_ID", nullable = true, length = 32)
    public String getAttnUserId() {
        return attnUserId;
    }

    public void setAttnUserId(String attnUserId) {
        this.attnUserId = attnUserId;
    }

    @Column(name = "TENANT_STATUS", nullable = false)
    public Integer getTenantStatus() {
        return tenantStatus;
    }

    public void setTenantStatus(Integer tenantStatus) {
        this.tenantStatus = tenantStatus;
    }

    @Column(name = "SIGN_DATE", nullable = false)
    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    @Column(name = "ENABLE_DATE", nullable = false)
    public Date getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(Date enableDate) {
        this.enableDate = enableDate;
    }

    @Column(name = "STATUS", nullable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "TENANT_ID", nullable = true, length = 32)
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "CHARGE_MODE", nullable = false, length = 2)
    public String getChargeMode() {
        return chargeMode;
    }

    public void setChargeMode(String chargeMode) {
        this.chargeMode = chargeMode;
    }

    @Column(name = "ATTACHMENT_ID", nullable = true, length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name = "CREATE_BY", nullable = true, length = 32)
    public String getCreateBy() {
        return this.createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name = "CREATE_TIME", nullable = true)
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "UPDATE_BY", nullable = true, length = 32)
    public String getUpdateBy() {
        return this.updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name = "UPDATE_TIME", nullable = true)
    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
