package com.bytter.biz.receipt.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author caop
 * @date 2024/8/2 15:24
 */
@Entity
@Table(name = "BIS_BANK_RECEIPT_IMPORT", schema = "")
public class BisBankReceiptImport extends IdEntity implements java.io.Serializable {

    /**银行接口代码*/
    private String bifCodeId;
    private String bankAcc;

    /**币种*/
    private String curId;

    /**导入时间*/
    private Date importDate;

    /**附件ID*/
    private String attachmentId;

    /**电子回单ID*/
    private String receiptId;

    /**流水ID*/
    private String accDtlId;

    /**勾对标识0-未勾兑,1-已勾兑,默认0-未勾兑*/
    private String blendSign;

    /**状态*/
    private Integer status;

    /**创建时间*/
    private Date createDate;

    /**创建人*/
    private String createBy;

    /**更新时间*/
    private Date updateDate;

    /**更新人*/
    private String updateBy;

    /**附件类型*/
    private String type;

    /**附件地址*/
    private String path;

    /**
     * 回单文件解析文本
     */
    private String reportText;

    @Transient
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Transient
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Column(name ="BIF_CODE_ID")
    public String getBifCodeId() {
        return bifCodeId;
    }

    public void setBifCodeId(String bifCodeId) {
        this.bifCodeId = bifCodeId;
    }

    @Column(name ="BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="IMPORT_DATE")
    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    @Column(name ="ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @Column(name ="RECEIPT_ID")
    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    @Column(name ="ACC_DTL_ID")
    public String getAccDtlId() {
        return accDtlId;
    }

    public void setAccDtlId(String accDtlId) {
        this.accDtlId = accDtlId;
    }

    @Column(name ="BLEND_SIGN")
    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="REPORT_TEXT")
    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }
}
