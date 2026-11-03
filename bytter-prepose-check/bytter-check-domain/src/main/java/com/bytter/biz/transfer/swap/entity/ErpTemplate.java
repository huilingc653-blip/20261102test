package com.bytter.biz.transfer.swap.entity;
import com.bytter.biz.transfer.swap.vo.ErpTemplateVO;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.*;
import java.sql.Timestamp;
/**
 * Created by zhouj on 2018/5/18.
 */
@Entity
@Table(name = "ERP_TEMPLATE", schema = "")
public class ErpTemplate extends IdEntity implements java.io.Serializable{
    private String templateTypeId;
    private String templateCode;
    private String templateName;
    private String rmk;
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
    /**
     * 补录是否编辑 1是0否
     */
    private String enteringIsEdit;
    public ErpTemplate() {
        super();
    }
    public ErpTemplate(ErpTemplateVO templateVO) {
        super();
        this.templateTypeId = templateVO.getTemplateTypeId();
        this.templateCode = templateVO.getTemplateCode();
        this.templateName = templateVO.getTemplateName();
        this.enteringIsEdit = templateVO.getEnteringIsEdit();
        this.rmk = templateVO.getRmk();
        this.opDate = new Timestamp(System.currentTimeMillis());
    }
    public ErpTemplate(ErpTemplateVO templateVO,long time) {
        super();
        this.setId(templateVO.getId());
        this.templateTypeId = templateVO.getTemplateTypeId();
        this.templateCode = templateVO.getTemplateCode();
        this.templateName = templateVO.getTemplateName();
        this.enteringIsEdit = templateVO.getEnteringIsEdit();
        this.rmk = templateVO.getRmk();
        this.opDate = new Timestamp(time);
    }
    @Column(name = "TEMPLATE_TYPE_ID", nullable = true, length = 32)
    public String getTemplateTypeId() {
        return templateTypeId;
    }
    public void setTemplateTypeId(String templateTypeId) {
        this.templateTypeId = templateTypeId;
    }
    @Column(name = "TEMPLATE_CODE", nullable = true, length = 50)
    public String getTemplateCode() {
        return templateCode;
    }
    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
    @Column(name = "TEMPLATE_NAME", nullable = true, length = 100)
    public String getTemplateName() {
        return templateName;
    }
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
    @Column(name = "RMK", nullable = true, length = 500)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "OP_DATE", nullable = true)
    public Timestamp getOpDate() {
        return opDate;
    }
    public void setOpDate(Timestamp opDate) {
        this.opDate = opDate;
    }
    @Column(name = "VOUCHER_TYPE_CODE", nullable = true, length = 100)
    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }
    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }
    @Column(name = "REFER_CODE", nullable = true, length = 100)
    public String getReferCode() {
        return referCode;
    }
    public void setReferCode(String referCode) {
        this.referCode = referCode;
    }
    @Column(name = "VOUCHER_PRE_TEXT", nullable = true, length = 100)
    public String getVoucherPreText() {
        return voucherPreText;
    }
    public void setVoucherPreText(String voucherPreText) {
        this.voucherPreText = voucherPreText;
    }
    @Column(name = "ITEM_CODE", nullable = true, length = 100)
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    @Column(name = "REFER", nullable = true, length = 100)
    public String getRefer() {
        return refer;
    }
    public void setRefer(String refer) {
        this.refer = refer;
    }
    @Column(name = "ABS", nullable = true, length = 100)
    public String getAbs() {
        return abs;
    }
    public void setAbs(String abs) {
        this.abs = abs;
    }
    @Column(name = "PURPOSE", nullable = true, length = 100)
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "PAY_TYPE", nullable = true, length = 100)
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    @Column(name = "REVERSE1", nullable = true, length = 100)
    public String getReverse1() {
        return reverse1;
    }
    public void setReverse1(String reverse1) {
        this.reverse1 = reverse1;
    }
    @Column(name = "REVERSE2", nullable = true, length = 100)
    public String getReverse2() {
        return reverse2;
    }
    public void setReverse2(String reverse2) {
        this.reverse2 = reverse2;
    }
    @Column(name = "ENTERING_ISEDIT")
	public String getEnteringIsEdit() {
		return enteringIsEdit;
	}
	public void setEnteringIsEdit(String enteringIsEdit) {
		this.enteringIsEdit = enteringIsEdit;
	}
    
}
