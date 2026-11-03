package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_ADJUSTRATE", schema = "")
@SuppressWarnings("serial")
public class CmsInlAdjustrate extends IdEntity implements Serializable {
    /**
     * 内部资金拆借申请ID
     */
    private String inlInfoId;
    /**
     * 内部资金拆借发放ID
     */
    private String inlSendId;
    /**
     * 状态
     */
    private String billNo;
    /**
     * 单据号
     */
    private Integer status;
    /**
     * 业务状态
     */
    private Integer bizStatus;
    /**
     * 拆入单位
     */
    private String corpId;
    /**
     * 调整资金拆借利率
     */
    private BigDecimal adjustRate;
    /**
     * 调整展期利率
     */
    private BigDecimal adjustDeferredRate;
    /**
     * 调整逾期利率
     */
    private BigDecimal adjustOverdueRate;
    /**
     * 执行日期
     */
    private Date execDate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件
     */
    private String attachmentId;
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
    @Column(name = "BIZ_STATUS")
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "ADJUST_RATE")
    public BigDecimal getAdjustRate() {
        return adjustRate;
    }
    public void setAdjustRate(BigDecimal adjustRate) {
        this.adjustRate = adjustRate;
    }
    @Column(name = "ADJUST_DEFERRED_RATE")
    public BigDecimal getAdjustDeferredRate() {
        return adjustDeferredRate;
    }
    public void setAdjustDeferredRate(BigDecimal adjustDeferredRate) {
        this.adjustDeferredRate = adjustDeferredRate;
    }
    @Column(name = "ADJUST_OVERDUE_RATE")
    public BigDecimal getAdjustOverdueRate() {
        return adjustOverdueRate;
    }
    public void setAdjustOverdueRate(BigDecimal adjustOverdueRate) {
        this.adjustOverdueRate = adjustOverdueRate;
    }
    @Column(name = "EXEC_DATE")
    public Date getExecDate() {
        return execDate;
    }
    public void setExecDate(Date execDate) {
        this.execDate = execDate;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
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
}
