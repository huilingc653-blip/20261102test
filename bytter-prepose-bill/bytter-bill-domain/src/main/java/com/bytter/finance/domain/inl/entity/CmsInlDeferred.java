package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_DEFERRED", schema = "")
@SuppressWarnings("serial")
public class CmsInlDeferred extends IdEntity implements Serializable {
    /**
     * 内部拆借申请id
     */
    private String inlInfoId;
    /**
     * 发放id
     */
    private String inlSendId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 拆入单位
     */
    private String corpId;
    /**
     * 流水号
     */
    private String billNo;
    /**
     * 展期次数
     */
    private Integer deferredCount;
    /**
     * 上次展期日期
     */
    private Date lastDeferredDate;
    /**
     * 当前展期日期
     */
    private Date nowDeferredDate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件id
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
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "DEFERRED_COUNT")
    public Integer getDeferredCount() {
        return deferredCount;
    }
    public void setDeferredCount(Integer deferredCount) {
        this.deferredCount = deferredCount;
    }
    @Column(name = "LAST_DEFERRED_DATE")
    public Date getLastDeferredDate() {
        return lastDeferredDate;
    }
    public void setLastDeferredDate(Date lastDeferredDate) {
        this.lastDeferredDate = lastDeferredDate;
    }
    @Column(name = "NOW_DEFERRED_DATE")
    public Date getNowDeferredDate() {
        return nowDeferredDate;
    }
    public void setNowDeferredDate(Date nowDeferredDate) {
        this.nowDeferredDate = nowDeferredDate;
    }
    @Column(name = "TERM")
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
    @Column(name = "INL_SEND_ID")
    public String getInlSendId() {
        return inlSendId;
    }
    public void setInlSendId(String inlSendId) {
        this.inlSendId = inlSendId;
    }
}
