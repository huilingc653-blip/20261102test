package com.bytter.biz.receipt.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author caop
 * @date 2024/8/2 16:51
 */
public class BisBankReceiptImportVO {
    /**主键*/
    private String id;

    /**银行接口代码*/
    @BtCompositeSearch(fieldName = "t.bif_code_id")
    @Dict(tableName="bis_bif_init",valueFieldName="id",showFiledName="name",inject="bankTypeName")
    private String bifCodeId;

    /**银行类型名称*/
    private String bankTypeName;

    /**单位*/
    @BtCompositeSearch(fieldName = "bba.corp_id")
    private String corpId;
    /**单位*/
    private String corpName;

    /**银行账号*/
    @BtCompositeSearch(fieldName = "t.bank_acc")
    private String bankAcc;

    /**币种*/
    private String curId;

    /**导入时间*/
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @BtCompositeSearch(fieldName = "t.import_date")
    private Date importDate;

    /**附件ID*/
    private String attachmentId;

    /**文件名称*/
    private String fileName;

    /**电子回单ID*/
    private String receiptId;

    /**流水ID*/
    private String accDtlId;

    /**勾对标识0-未勾兑,1-已勾兑,默认0-未勾兑*/
    @BtCompositeSearch(fieldName = "t.blend_sign")
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

    /**交易金额*/

    @BtCompositeSearch(fieldName = "dtl.amt")
    private BigDecimal amt;

    private int isConfirm;

    private String reportText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBifCodeId() {
        return bifCodeId;
    }

    public void setBifCodeId(String bifCodeId) {
        this.bifCodeId = bifCodeId;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public Date getImportDate() {
        return importDate;
    }

    public void setImportDate(Date importDate) {
        this.importDate = importDate;
    }

    public String getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getAccDtlId() {
        return accDtlId;
    }

    public void setAccDtlId(String accDtlId) {
        this.accDtlId = accDtlId;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public int getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(int isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
}
