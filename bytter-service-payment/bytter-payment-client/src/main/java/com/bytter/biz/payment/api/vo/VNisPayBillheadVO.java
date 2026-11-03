package com.bytter.biz.payment.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author zhupw
 */
public class VNisPayBillheadVO implements Serializable {
    private String id;
    /**付款单号*/
    @BtCompositeSearch(fieldName = "n.billNo")
    private String billNo;
    /**单位*/
    @BtCompositeSearch(fieldName = "n.corpId")
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**付款方式名称*/
    private String voucherName;
    /**付款账号*/
    @BtCompositeSearch(fieldName = "n.payerAcc")
    private String payerAcc;
    /**付款账号名称*/
    @BtCompositeSearch(fieldName = "n.payerAccName")
    private String payerAccName;
    /**币别*/
    @BtCompositeSearch(fieldName = "n.curId")
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    @BtCompositeSearch(fieldName = "n.curId")
    private String curName;
    /**金额*/
    @BtCompositeSearch(fieldName = "n.payMoney")
    private BigDecimal payMoney;
    /**收款账号*/
    @BtCompositeSearch(fieldName = "n.payeeAcc")
    private String payeeAcc;
    /**收款账号名称*/
    @BtCompositeSearch(fieldName = "n.payeeAccName")
    private String payeeAccName;
    /**期望支付日期*/
    @BtCompositeSearch(fieldName = "n.wishPayday")
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date wishPayday;
    /**用途*/
    @BtCompositeSearch(fieldName = "n.purpose")
    private String purpose;
    /**业务类别*/
    @BtCompositeSearch(fieldName = "n.busType")
    private String busType;
    /**业务类别名称*/
    private String busTypeName;
    /**状态*/
    @BtCompositeSearch(fieldName = "n.status")
    private Integer status;
    /**银行流水号**/
    private String bankSerialNo;
    /**数据来源**/
    private String isFrom;

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

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getVoucherName() {
        return voucherName;
    }

    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

    public String getPayerAcc() {
        return payerAcc;
    }

    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }

    public String getPayerAccName() {
        return payerAccName;
    }

    public void setPayerAccName(String payerAccName) {
        this.payerAccName = payerAccName;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    public String getPayeeAccName() {
        return payeeAccName;
    }

    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }

    public Date getWishPayday() {
        return wishPayday;
    }

    public void setWishPayday(Date wishPayday) {
        this.wishPayday = wishPayday;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getBusTypeName() {
        return busTypeName;
    }

    public void setBusTypeName(String busTypeName) {
        this.busTypeName = busTypeName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }
}
