package com.bytter.biz.receipt.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
public class EleAllBillVO  {
    @BtCompositeSearch(fieldName = "a.id")
    private String id;
    /**电子回单号码*/
    @BtCompositeSearch(fieldName = "a.billNo")
    private String billNo;
    /**电子回单类型21-委托贷款通知单22-委托贷款还款通知单*/
    @BtCompositeSearch(fieldName = "a.billType")
    @Dict(dictGroup = "eVisaType.billType", inject = "billTypeName")
    private String billType;
    @BtCompositeSearch(fieldName = "a.billType")
    private String billTypeName;
    /**凭证号*/
    @BtCompositeSearch(fieldName = "a.voucherNo")
    private String voucherNo;
    /**原始凭证号*/
    @BtCompositeSearch(fieldName = "a.oldVoucher")
    private String oldVoucher;
    /**tallyDate*/
    @BtCompositeSearch(fieldName = "a.tallyDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**打印次数*/
    private Integer prints;
    /**单位ID*/
    @BtCompositeSearch(fieldName = "a.corpId")
    private String corpId;
    /**状态*/
    @BtCompositeSearch(fieldName = "a.status")
    private Integer status;
    private String oldVoucherId;
    /**
     * 凭证产生方式
     */
    private String generateMode;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getBillType() {
        return billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    public String getBillTypeName() {
        return billTypeName;
    }
    public void setBillTypeName(String billTypeName) {
        this.billTypeName = billTypeName;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getOldVoucher() {
        return oldVoucher;
    }
    public void setOldVoucher(String oldVoucher) {
        this.oldVoucher = oldVoucher;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public Integer getPrints() {
        return prints;
    }
    public void setPrints(Integer prints) {
        this.prints = prints;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getOldVoucherId() {
        return oldVoucherId;
    }
    public void setOldVoucherId(String oldVoucherId) {
        this.oldVoucherId = oldVoucherId;
    }
    public String getGenerateMode() {
        return generateMode;
    }
    public void setGenerateMode(String generateMode) {
        this.generateMode = generateMode;
    }
}
