package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: CMS_BOND_REPAY_PLAN
 * @date 2022-06-06 10:07:46
 */
@Entity
@Table(name = "CMS_BOND_REPAY_PLAN", schema = "")
@SuppressWarnings("serial")
public class CmsBondRepayPlan extends IdEntity implements java.io.Serializable {
    /** 所属单位 */
    private String corpId;
    /** 债劵注册id */
    private String registerId;
    /** 债劵发行id */
    private String issueId;
    /** 还款方式 */
    private String repayType;
    /** 支付方式 */
    private String payType;
    /** 是否保存操作 */
    private String isSave;
    /** 还款周期 */
    private String repayCycel;
    /** 还款开始日 */
    private Date repayStartDate;
    /** 状态 */
    private Integer status;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "REGISTER_ID")
    public String getRegisterId() {
        return registerId;
    }
    public void setRegisterId(String registerId) {
        this.registerId = registerId;
    }
    @Column(name = "ISSUE_ID")
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name = "IS_SAVE")
    public String getIsSave() {
        return isSave;
    }
    public void setIsSave(String isSave) {
        this.isSave = isSave;
    }
    @Column(name = "REPAY_CYCEL")
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    @Column(name = "REPAY_START_DATE")
    public Date getRepayStartDate() {
        return repayStartDate;
    }
    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
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
    @Override
    public String toString() {
        return "CmsBondRepayPlan{" + "corpId='" + corpId + '\'' + ", registerId='" + registerId + '\'' + ", issueId='"
                + issueId + '\'' + ", repayType='" + repayType + '\'' + ", payType='" + payType + '\'' + ", isSave='"
                + isSave + '\'' + ", repayCycel='" + repayCycel + '\'' + ", repayStartDate=" + repayStartDate
                + ", status=" + status + ", createBy='" + createBy + '\'' + ", createDate=" + createDate
                + ", updateBy='" + updateBy + '\'' + ", updateDate=" + updateDate + '}';
    }
}
