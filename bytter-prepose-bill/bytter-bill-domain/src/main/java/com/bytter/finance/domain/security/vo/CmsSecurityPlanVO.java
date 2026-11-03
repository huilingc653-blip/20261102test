package com.bytter.finance.domain.security.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class CmsSecurityPlanVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 证券发行编号
     */
    @BtCompositeSearch(fieldName = "plan.SECURITY_ISSUE_CODE")
    private String securityIssueCode;
    /**
     * 单位
     */
    private String corpId;
    /**
     * 发行金额
     */
    private BigDecimal issueMoney;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 主承销商
     */
    private String underwriter;
    /**
     * 批次
     */
    private String batchNo;
    /**
     * 层级
     */
    private String levelName;
    /**
     * 发行日期
     */
    private Date issueDate;
    /**
     * 预期到期日
     */
    private Date expectEndDate;
    /**
     * 备注
     */
    private String rmk;
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
     * 计划明细
     */
    private List<CmsSecurityPlanDetailVO> detailDtl;
    @Override
    public String getBusinessKey() {
        return id;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSecurityIssueCode() {
        return securityIssueCode;
    }
    public void setSecurityIssueCode(String securityIssueCode) {
        this.securityIssueCode = securityIssueCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getUnderwriter() {
        return underwriter;
    }
    public void setUnderwriter(String underwriter) {
        this.underwriter = underwriter;
    }
    public String getBatchNo() {
        return batchNo;
    }
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
    public String getLevelName() {
        return levelName;
    }
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    public Date getIssueDate() {
        return issueDate;
    }
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }
    public Date getExpectEndDate() {
        return expectEndDate;
    }
    public void setExpectEndDate(Date expectEndDate) {
        this.expectEndDate = expectEndDate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
    public List<CmsSecurityPlanDetailVO> getDetailDtl() {
        return detailDtl;
    }
    public void setDetailDtl(List<CmsSecurityPlanDetailVO> detailDtl) {
        this.detailDtl = detailDtl;
    }
}
