package com.bytter.finance.domain.bond.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券展期信息VO
 */
public class CmsBondExtendVO implements Serializable, IBusinessKey {
    private String id;
    /** 债劵注册表id */
    private String registerId;
    /** 债劵发行表id */
    @BtCompositeSearch(fieldName = "t.issue_id")
    private String issueId;
    /** 债劵发行表id */
    private String bondIssueNum;
    /** 发行金额 */
    private String issueMoney;
    /** 未还金额 */
    private String outstandingAmount;
    /** 展期申请日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date extendApplyDate;
    /** 展期到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date extendEndDate;
    /** 上次展期/到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date lastExtendDate;
    /**
     * 展期合同编号
     */
    private String extendCode;
    /**
     * 展期期限
     */
    private Integer extendDeadline;
    /**
     * 展期期限单位
     */
    private String deadlineUnit;
    /**
     * 比例%/BP点数
     */
    private BigDecimal basicPoint;
    /**
     * 展期利率
     */
    private BigDecimal extendRate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否删除 默认'0'
     */
    private String isDelete;
    /** 状态 */
    @Dict(dictGroup = "AB_BILL_EMITBILL_STATUS", inject = "statusName")
    private Integer status;
    /**
     * 状态名
     */
    private String statusName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    private  String saveType;

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public void setExtendApplyDate(Date extendApplyDate) {
        this.extendApplyDate = extendApplyDate;
    }
    public Date getExtendEndDate() {
        return extendEndDate;
    }
    public void setExtendEndDate(Date extendEndDate) {
        this.extendEndDate = extendEndDate;
    }
    public Date getLastExtendDate() {
        return lastExtendDate;
    }
    public void setLastExtendDate(Date lastExtendDate) {
        this.lastExtendDate = lastExtendDate;
    }
    public String getExtendCode() {
        return extendCode;
    }
    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }
    public Integer getExtendDeadline() {
        return extendDeadline;
    }
    public void setExtendDeadline(Integer extendDeadline) {
        this.extendDeadline = extendDeadline;
    }
    public String getDeadlineUnit() {
        return deadlineUnit;
    }
    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
    public BigDecimal getBasicPoint() {
        return basicPoint;
    }
    public void setBasicPoint(BigDecimal basicPoint) {
        this.basicPoint = basicPoint;
    }
    public BigDecimal getExtendRate() {
        return extendRate;
    }
    public void setExtendRate(BigDecimal extendRate) {
        this.extendRate = extendRate;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBondIssueNum() {
        return bondIssueNum;
    }
    public void setBondIssueNum(String bondIssueNum) {
        this.bondIssueNum = bondIssueNum;
    }
    public String getIssueMoney() {
        return issueMoney;
    }
    public void setIssueMoney(String issueMoney) {
        this.issueMoney = issueMoney;
    }
    public String getOutstandingAmount() {
        return outstandingAmount;
    }
    public void setOutstandingAmount(String outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
    public Date getExtendApplyDate() {
        return extendApplyDate;
    }
}
