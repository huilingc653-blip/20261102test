package com.bytter.biz.transfer.swap.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE_STORE", catalog = "")
public class ErpTemplateStore extends IdEntity implements Serializable{
    private String dataId;
    private String templateId;
    private Timestamp opDate;
    private String voucherTypeCode;
    private String referCode;
    private String voucherPreText;
    private String itemCode;
    private String refer;
    private String abs;
    private String purpose;
    private String payType;
    private String reverse1;
    private String reverse2;
    private String templateName;
    private String typeName;
    @Basic
    @Column(name = "DATA_ID", nullable = true, length = 32)
    public String getDataId() {
        return dataId;
    }
    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
    @Basic
    @Column(name = "TEMPLATE_ID", nullable = true, length = 32)
    public String getTemplateId() {
        return templateId;
    }
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
    @Basic
    @Column(name = "OP_DATE", nullable = true)
    public Timestamp getOpDate() {
        return opDate;
    }
    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }
    @Basic
    @Column(name = "VOUCHER_TYPE_CODE", nullable = true, length = 100)
    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }
    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }
    @Basic
    @Column(name = "REFER_CODE", nullable = true, length = 100)
    public String getReferCode() {
        return referCode;
    }
    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }
    @Basic
    @Column(name = "VOUCHER_PRE_TEXT", nullable = true, length = 100)
    public String getVoucherPreText() {
        return voucherPreText;
    }
    public void setVoucherPreText(String voucherPreText) {
        this.voucherPreText = voucherPreText;
    }
    @Basic
    @Column(name = "ITEM_CODE", nullable = true, length = 100)
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    @Basic
    @Column(name = "REFER", nullable = true, length = 100)
    public String getRefer() {
        return refer;
    }
    public void setRefer(String refer) {
        this.refer = refer;
    }
    @Basic
    @Column(name = "ABS", nullable = true, length = 100)
    public String getAbs() {
        return abs;
    }
    public void setAbs(String abs) {
        this.abs = abs;
    }
    @Basic
    @Column(name = "PURPOSE", nullable = true, length = 100)
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Basic
    @Column(name = "PAY_TYPE", nullable = true, length = 100)
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Basic
    @Column(name = "REVERSE1", nullable = true, length = 100)
    public String getReverse1() {
        return reverse1;
    }
    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
    @Basic
    @Column(name = "REVERSE2", nullable = true, length = 100)
    public String getReverse2() {
        return reverse2;
    }
    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
    @Basic
    @Column(name = "TEMPLATE_NAME", nullable = true, length = 100)
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    @Basic
    @Column(name = "TYPE_NAME", nullable = true, length = 100)
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
