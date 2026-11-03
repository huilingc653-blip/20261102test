package com.bytter.credential.domain.international.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
/**信用证付汇表**/
public class LcPaymentVO implements java.io.Serializable, IBusinessKey {
    private String id;
    /**单位编号**/
    private String corpId;
    /**到单ID**/
    private String arrivalId;
    /**信用证登记ID**/
    private String lcId;
    /**付汇流水号**/
    @BtCompositeSearch(fieldName = "lp.payment_no")
    private String paymentNo;
    /**付汇日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lp.payment_date")
    private Date paymentDate;
    /**币别**/
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "lp.cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "lp.cur_name")
    private String curName;
    /**汇率**/
    private BigDecimal exchangeRate;
    /**利率**/
    private BigDecimal interestRate;
    /**到单手续费**/
    private BigDecimal handlingCharge;
    /**付汇金额**/
    @BtCompositeSearch(fieldName = "lp.payment_money")
    private BigDecimal paymentMoney;
    /**信用证付款总额**/
    private BigDecimal paymentMoneyTotal;
    /**剩余金额**/
    private BigDecimal freezMoney;
    /**备注**/
    private String rmk;
    /**状态**/
    @BtCompositeSearch(fieldName = "lp.status")
    private Integer status;
    /**操作前状态**/
    private Integer lastStatus;
    /**创建日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lp.create_date")
    private Date createDate;
    /**创建人登录名称**/
    private String createBy;
    /**更新日期**/
    private Date updateDate;
    /**更新人登录名称**/
    private String updateBy;
    /*信用证类型*/
    @BtCompositeSearch(fieldName = "lc.lc_inter")
    private String lcInter;
    /*信用证单号*/
    private String lcNo;
    /*合同号*/
    @BtCompositeSearch(fieldName = "lc.contract_no")
    private String contractNo;
    /*到单号*/
    @BtCompositeSearch(fieldName = "lba.arrival_no")
    private String arrivalNo;
    /*受益人*/
    @BtCompositeSearch(fieldName = "lc.beneficiary")
    private String beneficiary;
    /*到单日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "lba.payment_date")
    private Date billArrivalDate;
    /*到单银行*/
    @BtCompositeSearch(fieldName = "lba.hand_doc_bank")
    private String handDocBank;
    /*结算汇率*/
    private BigDecimal settleRate;
    /*当日汇率*/
    private BigDecimal dayRate;
    /*信用证号*/
    @BtCompositeSearch(fieldName = "lc.credit_no")
    private String creditNo;
    /**
     * 附件id
     */
    private String attachmentId;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getArrivalId() {
        return arrivalId;
    }
    public void setArrivalId(String arrivalId) {
        this.arrivalId = arrivalId;
    }
    public String getLcId() {
        return lcId;
    }
    public void setLcId(String lcId) {
        this.lcId = lcId;
    }
    public String getPaymentNo() {
        return paymentNo;
    }
    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }
    public Date getPaymentDate() {
        return paymentDate;
    }
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public BigDecimal getHandlingCharge() {
        return handlingCharge;
    }
    public void setHandlingCharge(BigDecimal handlingCharge) {
        this.handlingCharge = handlingCharge;
    }
    public BigDecimal getPaymentMoney() {
        return paymentMoney;
    }
    public void setPaymentMoney(BigDecimal paymentMoney) {
        this.paymentMoney = paymentMoney;
    }
    public BigDecimal getPaymentMoneyTotal() {
        return paymentMoneyTotal;
    }
    public void setPaymentMoneyTotal(BigDecimal paymentMoneyTotal) {
        this.paymentMoneyTotal = paymentMoneyTotal;
    }
    public BigDecimal getFreezMoney() {
        return freezMoney;
    }
    public void setFreezMoney(BigDecimal freezMoney) {
        this.freezMoney = freezMoney;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getLastStatus() {
        return lastStatus;
    }
    public void setLastStatus(Integer lastStatus) {
        this.lastStatus = lastStatus;
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
    public String getLcInter() {
        return lcInter;
    }
    public void setLcInter(String lcInter) {
        this.lcInter = lcInter;
    }
    public String getLcNo() {
        return lcNo;
    }
    public void setLcNo(String lcNo) {
        this.lcNo = lcNo;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }
    public String getArrivalNo() {
        return arrivalNo;
    }
    public void setArrivalNo(String arrivalNo) {
        this.arrivalNo = arrivalNo;
    }
    public String getBeneficiary() {
        return beneficiary;
    }
    public void setBeneficiary(String beneficiary) {
        this.beneficiary = beneficiary;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getBillArrivalDate() {
        return billArrivalDate;
    }
    public void setBillArrivalDate(Date billArrivalDate) {
        this.billArrivalDate = billArrivalDate;
    }
    public String getHandDocBank() {
        return handDocBank;
    }
    public void setHandDocBank(String handDocBank) {
        this.handDocBank = handDocBank;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getSettleRate() {
        return settleRate;
    }
    public void setSettleRate(BigDecimal settleRate) {
        this.settleRate = settleRate;
    }
    public BigDecimal getDayRate() {
        return dayRate;
    }
    public void setDayRate(BigDecimal dayRate) {
        this.dayRate = dayRate;
    }
    public String getCreditNo() {
        return creditNo;
    }
    public void setCreditNo(String creditNo) {
        this.creditNo = creditNo;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
