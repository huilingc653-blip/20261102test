package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_REPAYPLAN_CHANGE", schema = "")
@SuppressWarnings("serial")
public class CmsInlChange extends IdEntity implements Serializable {
    /**
     * 申请信息表
     */
    private String inlInfoId;
    /**
     * 发放ID
     */
    private String inlSendId;
    /**
     * 拆入单位
     */
    private String corpId;
    /**
     * 还款开始日期
     */
    private Date repayStartDate;
    /**
     * 更改还款方式
     */
    private String repayType;
    /**
     * 还款周期
     */
    private String repayCycel;
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
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
    @Column(name = "INL_INFO_ID")
    public String getInlInfoId() {
        return inlInfoId;
    }
    public void setInlInfoId(String inlInfoId) {
        this.inlInfoId = inlInfoId;
    }
    @Column(name = "INL_SEND_ID")
    public String getInlSendId() {
        return inlSendId;
    }
    public void setInlSendId(String inlSendId) {
        this.inlSendId = inlSendId;
    }
    @Column(name = "REPAY_START_DATE")
    public Date getRepayStartDate() {
        return repayStartDate;
    }
    public void setRepayStartDate(Date repayStartDate) {
        this.repayStartDate = repayStartDate;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "REPAY_CYCEL")
    public String getRepayCycel() {
        return repayCycel;
    }
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
}
