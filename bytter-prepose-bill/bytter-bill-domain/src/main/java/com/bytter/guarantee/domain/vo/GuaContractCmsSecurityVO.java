package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class GuaContractCmsSecurityVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 证券发行编号
     */
//    @BtCompositeSearch(fieldName = "iss.SECURITY_ISSUE_CODE")
    private String securityIssueCode;
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
     * 发行金额
     */
    private BigDecimal issueMoney;
    /**
     * 发行日期
     */
    private Date issueDate;
    /**
     * 预期到期日
     */
    private Date expectEndDate;
    /**
     * 使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "gco.status")
    @Dict(dictGroup = "Contract.UseStatus", inject = "statusName")
    private Integer status;
    private String statusName;

    /**
     * 担保合同ID
     */
    @BtCompositeSearch(fieldName = "gco.contract_id")
    private String contractId;

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
    public BigDecimal getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(BigDecimal issueMoney) {
        this.issueMoney = issueMoney;
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
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getContractId() {
        return contractId;
    }

    public void setContractId(String contractId) {
        this.contractId = contractId;
    }
}
