package com.bytter.finance.domain.security.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author tongqr
 * @version 1.0
 * @Title: Entity
 * @Description: 资产证券化登记主信息表
 * @date 2022-08-09
 */
@Entity
@Table(name = "CMS_SECURITY_INFO", schema = "")
public class CmsSecurityInfo extends IdEntity implements java.io.Serializable {
    /**
     * 资产证券化类型
     */
    private String securityTypeId;
    /**
     * 境内/境外
     */
    private String isdomestic;
    /**
     * 原始权益人
     */
    private String corpId;
    /**
     * 实际融资人
     */
    private String financier;
    /**
     * 证券代码
     */
    private String securityCode;
    /**
     * 证券简称
     */
    private String securitySname;
    /**
     * 证券名称
     */
    private String securityName;
    /**
     * 批准文号
     */
    private String approvalNumber;
    /**
     * 受托机构
     */
    private String trustee;
    /**
     * 基础资产类型
     */
    private String assetTypeCode;
    /**
     * 证券原币币种
     */
    private String curId;
    /**
     * 计划发行原币总额
     */
    private BigDecimal money;
    /**
     * 计划发行人名币总额
     */
    private BigDecimal rmbMoney;
    /**
     * 增信措施
     */
    private String measures;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 原始权益名称
     */
    private String corpName;
    /**
     * 修改时间
     */
    private String curName;
    @Column(name = "SECURITY_TYPE_ID")
    public String getSecurityTypeId() {
        return securityTypeId;
    }
    public void setSecurityTypeId(String securityTypeId) {
        this.securityTypeId = securityTypeId;
    }
    @Column(name = "ISDOMESTIC")
    public String getIsdomestic() {
        return isdomestic;
    }
    public void setIsdomestic(String isdomestic) {
        this.isdomestic = isdomestic;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "FINANCIER")
    public String getFinancier() {
        return financier;
    }
    public void setFinancier(String financier) {
        this.financier = financier;
    }
    @Column(name = "SECURITY_CODE")
    public String getSecurityCode() {
        return securityCode;
    }
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    @Column(name = "SECURITY_SNAME")
    public String getSecuritySname() {
        return securitySname;
    }
    public void setSecuritySname(String securitySname) {
        this.securitySname = securitySname;
    }
    @Column(name = "SECURITY_NAME")
    public String getSecurityName() {
        return securityName;
    }
    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }
    @Column(name = "APPROVAL_NUMBER")
    public String getApprovalNumber() {
        return approvalNumber;
    }
    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }
    @Column(name = "TRUSTEE")
    public String getTrustee() {
        return trustee;
    }
    public void setTrustee(String trustee) {
        this.trustee = trustee;
    }
    @Column(name = "ASSET_TYPE_CODE")
    public String getAssetTypeCode() {
        return assetTypeCode;
    }
    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "RMB_MONEY")
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    @Column(name = "MEASURES")
    public String getMeasures() {
        return measures;
    }
    public void setMeasures(String measures) {
        this.measures = measures;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
