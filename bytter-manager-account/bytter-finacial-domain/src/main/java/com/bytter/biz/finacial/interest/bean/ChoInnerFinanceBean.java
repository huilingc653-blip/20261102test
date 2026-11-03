package com.bytter.biz.finacial.interest.bean;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class ChoInnerFinanceBean implements Serializable {
    /**业务单号*/
    private String billNo;
    /**单位ID*/
    private String corpId;
    /**业务类型 10-活期转定期 11-定期转活期 12-活期转通知 13-通知转活期 14-通知预约 15-取消通知预约*/
    private Integer busType;
    /**付款账号ID*/
    private String payerAccId;
    /**付款账号*/
    private String payerAcc;
    /**收款账号ID*/
    private String payeeAccId;
    /**收款账号*/
    private String payeeAcc;
    /**本金存入账号ID*/
    private String capitalAccId;
    /**利息存入账号ID*/
    private String interestAccId;
    /**存款类型*/
    private String depositRateType;
    /**币别*/
    private String curId;
    /**金额*/
    private BigDecimal payMoney;
    /**期限（活期转定期）：3个月、6个月、1年、2年、3年、5年*/
    private Integer dayLimit;
    /**日期单位 Y-年、M-月、D-日*/
    private String dateUnit;
    /**到期处理方式 0-自动终止、1-本金续存、2-本息续存*/
    private Integer certificateWay;
    /**起息日期*/
    private Date interestStartDate;
    /**到期日期*/
    private Date deadlineDate;
    /**用途*/
    private String purpose;
    /**备注*/
    private String addWord;
    /**利率*/
    private BigDecimal interestRate;
    /**取款日期*/
    private Date payDate;
    /**关联单据ID*/
    private String relatedBillId;
    /**状态*/
    private Integer status;
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
    public Integer getBusType() {
        return busType;
    }
    public void setBusType(Integer busType) {
        this.busType = busType;
    }
    public String getPayerAccId() {
        return payerAccId;
    }
    public void setPayerAccId(String payerAccId) {
        this.payerAccId = payerAccId;
    }
    public String getPayerAcc() {
        return payerAcc;
    }
    public void setPayerAcc(String payerAcc) {
        this.payerAcc = payerAcc;
    }
    public String getPayeeAccId() {
        return payeeAccId;
    }
    public void setPayeeAccId(String payeeAccId) {
        this.payeeAccId = payeeAccId;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getCapitalAccId() {
        return capitalAccId;
    }
    public void setCapitalAccId(String capitalAccId) {
        this.capitalAccId = capitalAccId;
    }
    public String getInterestAccId() {
        return interestAccId;
    }
    public void setInterestAccId(String interestAccId) {
        this.interestAccId = interestAccId;
    }
    public String getDepositRateType() {
        return depositRateType;
    }
    public void setDepositRateType(String depositRateType) {
        this.depositRateType = depositRateType;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    public Integer getDayLimit() {
        return dayLimit;
    }
    public void setDayLimit(Integer dayLimit) {
        this.dayLimit = dayLimit;
    }
    public String getDateUnit() {
        return dateUnit;
    }
    public void setDateUnit(String dateUnit) {
        this.dateUnit = dateUnit;
    }
    public Integer getCertificateWay() {
        return certificateWay;
    }
    public void setCertificateWay(Integer certificateWay) {
        this.certificateWay = certificateWay;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getAddWord() {
        return addWord;
    }
    public void setAddWord(String addWord) {
        this.addWord = addWord;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public String getRelatedBillId() {
        return relatedBillId;
    }
    public void setRelatedBillId(String relatedBillId) {
        this.relatedBillId = relatedBillId;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
}
