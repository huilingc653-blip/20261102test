package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 债券展期表
 *
 * @author wangf
 */
@Entity
@Table(name = "CMS_BOND_EXTEND")
public class CmsBondExtend extends IdEntity implements Serializable {
    /**
     * 债劵发行表id
     */
    private String issueId;
    /**
     * 展期申请日期
     */
    private Date extendApplyDate;
    /**
     * 展期到期日期
     */
    private Date extendEndDate;
    /**
     * 上次展期/到期日期
     */
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
    /**
     * 展期状态
     */
    private Integer status;
    /**
     * 创建人登录名称
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
    @Column(name = "ISSUE_ID")
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    @Column(name = "EXTEND_APPLY_DATE")
    public Date getExtendApplyDate() {
        return extendApplyDate;
    }
    public void setExtendApplyDate(Date extendApplyDate) {
        this.extendApplyDate = extendApplyDate;
    }
    @Column(name = "EXTEND_END_DATE")
    public Date getExtendEndDate() {
        return extendEndDate;
    }
    public void setExtendEndDate(Date extendEndDate) {
        this.extendEndDate = extendEndDate;
    }
    @Column(name = "LAST_EXTEND_DATE")
    public Date getLastExtendDate() {
        return lastExtendDate;
    }
    public void setLastExtendDate(Date lastExtendDate) {
        this.lastExtendDate = lastExtendDate;
    }
    @Column(name = "EXTEND_CODE")
    public String getExtendCode() {
        return extendCode;
    }
    public void setExtendCode(String extendCode) {
        this.extendCode = extendCode;
    }
    @Column(name = "EXTEND_DEADLINE")
    public Integer getExtendDeadline() {
        return extendDeadline;
    }
    public void setExtendDeadline(Integer extendDeadline) {
        this.extendDeadline = extendDeadline;
    }
    @Column(name = "DEADLINE_UNIT")
    public String getDeadlineUnit() {
        return deadlineUnit;
    }
    public void setDeadlineUnit(String deadlineUnit) {
        this.deadlineUnit = deadlineUnit;
    }
    @Column(name = "BASIC_POINT")
    public BigDecimal getBasicPoint() {
        return basicPoint;
    }
    public void setBasicPoint(BigDecimal basicPoint) {
        this.basicPoint = basicPoint;
    }
    @Column(name = "EXTEND_RATE")
    public BigDecimal getExtendRate() {
        return extendRate;
    }
    public void setExtendRate(BigDecimal extendRate) {
        this.extendRate = extendRate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "IS_DELETE")
    public String getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
}
