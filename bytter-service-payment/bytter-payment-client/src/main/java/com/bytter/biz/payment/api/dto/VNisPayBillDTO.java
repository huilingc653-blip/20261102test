package com.bytter.biz.payment.api.dto;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author qijh
 */
public class VNisPayBillDTO implements Serializable {
    private String id;
    /**付款单号*/
    private String billNo;
    /**单位*/
    private String corpId;
    /**单位名称*/
    private String corpName;
    /**付款方式名称*/
    private String voucherName;
    /**付款账号*/
    private String payerAcc;
    /**付款账号名称*/
    private String payerAccName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    /**币别名称*/
    private String curName;
    /**金额*/
    private BigDecimal payMoney;
    /**收款账号*/
    private String payeeAcc;
    /**收款账号名称*/
    private String payeeAccName;
    /**期望支付日期*/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date wishPayday;
    /**用途*/
    private String purpose;
    /**业务类别*/
    private String busType;
    /**业务类别名称*/
    private String busTypeName;
    /**状态*/
    private Integer status;
    /**银行流水号**/
    private String bisDtlNo;
    /**数据来源**/
    private String isFrom;

    private String isOnline;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date transTime;

    @BtCompositeSearch(fieldName = "n.blendSign")
    private String blendSign;
    private String blendSignName;
    /**银行流水号**/
    private String bankSerialNo;

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

    public String getBisDtlNo() {
        return bisDtlNo;
    }

    public void setBisDtlNo(String bisDtlNo) {
        this.bisDtlNo = bisDtlNo;
    }

    public String getIsFrom() {
        return isFrom;
    }

    public void setIsFrom(String isFrom) {
        this.isFrom = isFrom;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        this.isOnline = isOnline;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getBlendSign() {
        return blendSign;
    }

    public void setBlendSign(String blendSign) {
        this.blendSign = blendSign;
    }

    public String getBlendSignName() {
        return blendSignName;
    }

    public void setBlendSignName(String blendSignName) {
        this.blendSignName = blendSignName;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
}
