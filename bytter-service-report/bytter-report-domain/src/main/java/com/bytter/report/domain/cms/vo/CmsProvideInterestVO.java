package com.bytter.report.domain.cms.vo;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description: 借款利息预计明细VO
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/12/30
 */
public class CmsProvideInterestVO implements Serializable {
    /**主键*/
    private String id;
    /**生成利息预计的还款计划ID*/
    private String repaymentScheduleId;
    /**计息时间*/
    private Date interestTime;
    /**合同号*/
    private String contractCode;
    /**单据编号*/
    private String billCode;
    /**业务类型*/
    private String billType;
    /**网点*/
    private String netId;
    /**单位*/
    private String corpId;
    /**币别*/
    private String curId;
    /**计息类型*/
    private String interestType;
    /**还款日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**起息日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**结束日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date blnInstDate;
    /**计息天数*/
    private Integer days;
    /**积数*/
    private BigDecimal cumulation;
    /**基准利率*/
    private BigDecimal rate;
    /**浮动比例点数*/
    private BigDecimal floatPoint;
    /**计息利率*/
    private BigDecimal interestRate;
    /**税率*/
    private BigDecimal rateFax;
    /**本金*/
    private BigDecimal loanMoney;
    /**归还本金*/
    private BigDecimal repayMoney;
    /**剩余本金*/
    private BigDecimal leftMoney;
    /**归还本金利息*/
    private BigDecimal interest;
    /**支付利息*/
    private BigDecimal payInterest;
    /**全部利息*/
    private BigDecimal totalInterest;
    /**未结利息*/
    private BigDecimal unPayInterest;
    /**利息税率*/
    private BigDecimal interestFax;
    /**实际付息*/
    private BigDecimal realInterest;
    /**计息标识*/
    private String interestSign;
    /**还款状态*/
    private Integer status;
    /**备注*/
    private String rmk;
    /**还款日期标识。标识分段属于这个时间的还款*/
    private Date signRepayDate;
    /**还款到期提醒标志**/
    private String repaySign;

    /**归还金额*/
    private BigDecimal payMoney;

    /**实际归还金额*/
    private BigDecimal realPayMoney;

    /**直融-手续费*/
    private BigDecimal serviceCharge;

    /**直融-实际手续费*/
    private BigDecimal realServiceCharge;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }

    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }

    public Date getInterestTime() {
        return interestTime;
    }

    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public Date getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public Date getBlnInstDate() {
        return blnInstDate;
    }

    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public BigDecimal getCumulation() {
        return cumulation;
    }

    public void setCumulation(BigDecimal cumulation) {
        this.cumulation = cumulation;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getFloatPoint() {
        return floatPoint;
    }

    public void setFloatPoint(BigDecimal floatPoint) {
        this.floatPoint = floatPoint;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getRateFax() {
        return rateFax;
    }

    public void setRateFax(BigDecimal rateFax) {
        this.rateFax = rateFax;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getPayInterest() {
        return payInterest;
    }

    public void setPayInterest(BigDecimal payInterest) {
        this.payInterest = payInterest;
    }

    public BigDecimal getTotalInterest() {
        return totalInterest;
    }

    public void setTotalInterest(BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }

    public BigDecimal getUnPayInterest() {
        return unPayInterest;
    }

    public void setUnPayInterest(BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }

    public BigDecimal getInterestFax() {
        return interestFax;
    }

    public void setInterestFax(BigDecimal interestFax) {
        this.interestFax = interestFax;
    }

    public BigDecimal getRealInterest() {
        return realInterest;
    }

    public void setRealInterest(BigDecimal realInterest) {
        this.realInterest = realInterest;
    }

    public String getInterestSign() {
        return interestSign;
    }

    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public Date getSignRepayDate() {
        return signRepayDate;
    }

    public void setSignRepayDate(Date signRepayDate) {
        this.signRepayDate = signRepayDate;
    }

    public String getRepaySign() {
        return repaySign;
    }

    public void setRepaySign(String repaySign) {
        this.repaySign = repaySign;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public BigDecimal getRealPayMoney() {
        return realPayMoney;
    }

    public void setRealPayMoney(BigDecimal realPayMoney) {
        this.realPayMoney = realPayMoney;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getRealServiceCharge() {
        return realServiceCharge;
    }

    public void setRealServiceCharge(BigDecimal realServiceCharge) {
        this.realServiceCharge = realServiceCharge;
    }
}
