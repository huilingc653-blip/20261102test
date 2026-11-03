package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 资产证券化登记主信息表
 *
 * @author tongqr
 */
public class CmsSecurityInfoVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 资产证券化类型
     */
    @BtCompositeSearch(fieldName = "info.SECURITY_TYPE_ID")
    private String securityTypeId;
    private String securityTypeName;
    /**
     * 境内/境外
     */
    private String isdomestic;
    /**
     * 原始权益人
     */
    @BtCompositeSearch(fieldName = "info.CORP_ID")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 实际融资人
     */
    private String financier;
    /**
     * 证券代码
     */
    @BtCompositeSearch(fieldName = "info.SECURITY_CODE")
    private String securityCode;
    /**
     * 证券简称
     */
    @BtCompositeSearch(fieldName = "info.SECURITY_SNAME")
    private String securitySname;
    /**
     * 证券名称
     */
    @BtCompositeSearch(fieldName = "info.SECURITY_NAME")
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
    private String assetTypeName;
    /**
     * 证券原币币种
     */
    @BtCompositeSearch(fieldName = "info.CUR_ID")
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 计划发行原币总额
     */
    @BtCompositeSearch(fieldName = "info.MONEY")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateDate;
    /**
     * 资产分层信息
     */
    private List<CmsSecurityLevelInfoVO> levelVOList;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSecurityTypeId() {
        return securityTypeId;
    }
    public void setSecurityTypeId(String securityTypeId) {
        this.securityTypeId = securityTypeId;
    }
    public String getSecurityTypeName() {
        return securityTypeName;
    }
    public void setSecurityTypeName(String securityTypeName) {
        this.securityTypeName = securityTypeName;
    }
    public String getIsdomestic() {
        return isdomestic;
    }
    public void setIsdomestic(String isdomestic) {
        this.isdomestic = isdomestic;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getFinancier() {
        return financier;
    }
    public void setFinancier(String financier) {
        this.financier = financier;
    }
    public String getSecurityCode() {
        return securityCode;
    }
    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
    public String getSecuritySname() {
        return securitySname;
    }
    public void setSecuritySname(String securitySname) {
        this.securitySname = securitySname;
    }
    public String getSecurityName() {
        return securityName;
    }
    public void setSecurityName(String securityName) {
        this.securityName = securityName;
    }
    public String getApprovalNumber() {
        return approvalNumber;
    }
    public void setApprovalNumber(String approvalNumber) {
        this.approvalNumber = approvalNumber;
    }
    public String getTrustee() {
        return trustee;
    }
    public void setTrustee(String trustee) {
        this.trustee = trustee;
    }
    public String getAssetTypeCode() {
        return assetTypeCode;
    }
    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }
    public String getAssetTypeName() {
        return assetTypeName;
    }
    public void setAssetTypeName(String assetTypeName) {
        this.assetTypeName = assetTypeName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getRmbMoney() {
        return rmbMoney;
    }
    public void setRmbMoney(BigDecimal rmbMoney) {
        this.rmbMoney = rmbMoney;
    }
    public String getMeasures() {
        return measures;
    }
    public void setMeasures(String measures) {
        this.measures = measures;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public List<CmsSecurityLevelInfoVO> getLevelVOList() {
        return levelVOList;
    }
    public void setLevelVOList(List<CmsSecurityLevelInfoVO> levelVOList) {
        this.levelVOList = levelVOList;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
