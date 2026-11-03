package com.bytter.finance.domain.lease.vo;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
public class LeaseInterestVo implements Serializable {
    private String id;
    /** 生成利息预计的还款计划ID */
    private String repaymentScheduleId;
    /** 计息时间 */
    private Date interestTime;
    /** 合同号 */
    private String contractCode;
    /** 单据编号 */
    private String billCode;
    /** 业务类型 */
    private String billType;
    /** 单位 */
    private String corpId;
    /** 币别 */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 计息类型 */
    private String interestType;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /** 起息日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date blnInstDate;
    /** 计息天数 */
    private Integer days;
    /** 积数 */
    private java.math.BigDecimal cumulation;
    /** 基准利率 */
    private java.math.BigDecimal rate;
    /** 浮动比例点数 */
    private java.math.BigDecimal floatPoint;
    /** 计息利率 */
    private java.math.BigDecimal interestRate;
    /** 税率 */
    private java.math.BigDecimal rateFax;
    /** 本金 */
    private java.math.BigDecimal loanMoney;
    /** 归还本金 */
    private java.math.BigDecimal repayMoney;
    /** 剩余本金 */
    private java.math.BigDecimal leftMoney;
    /** 归还本金利息 */
    private java.math.BigDecimal interest;
    /** 支付利息 */
    private java.math.BigDecimal payInterest;
    /** 全部利息 */
    private java.math.BigDecimal totalInterest;
    /** 未结利息 */
    private java.math.BigDecimal unPayInterest;
    /** 利息税率 */
    private java.math.BigDecimal interestFax;
    /** 实际付息 */
    private java.math.BigDecimal realInterest;
    /** 计息标识 */
    private String interestSign;
    /** 还款状态 */
    private Integer status;
    /** 备注 */
    private String rmk;
    private Date signRepayDate;
    private Integer count;
    private java.math.BigDecimal countRepayMoney;// 合计还款本金
    private java.math.BigDecimal countPayInterest;// 合计支付利息
    private java.math.BigDecimal countRealInterest;// 合计实付利息
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
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
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
    public java.math.BigDecimal getCumulation() {
        return cumulation;
    }
    public void setCumulation(java.math.BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    public java.math.BigDecimal getRate() {
        return rate;
    }
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    public java.math.BigDecimal getFloatPoint() {
        return floatPoint;
    }
    public void setFloatPoint(java.math.BigDecimal floatPoint) {
        this.floatPoint = floatPoint;
    }
    public java.math.BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(java.math.BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public java.math.BigDecimal getRateFax() {
        return rateFax;
    }
    public void setRateFax(java.math.BigDecimal rateFax) {
        this.rateFax = rateFax;
    }
    public java.math.BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public java.math.BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public java.math.BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(java.math.BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public java.math.BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(java.math.BigDecimal interest) {
        this.interest = interest;
    }
    public java.math.BigDecimal getPayInterest() {
        return payInterest;
    }
    public void setPayInterest(java.math.BigDecimal payInterest) {
        this.payInterest = payInterest;
    }
    public java.math.BigDecimal getTotalInterest() {
        return totalInterest;
    }
    public void setTotalInterest(java.math.BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
    public java.math.BigDecimal getUnPayInterest() {
        return unPayInterest;
    }
    public void setUnPayInterest(java.math.BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }
    public java.math.BigDecimal getInterestFax() {
        return interestFax;
    }
    public void setInterestFax(java.math.BigDecimal interestFax) {
        this.interestFax = interestFax;
    }
    public java.math.BigDecimal getRealInterest() {
        return realInterest;
    }
    public void setRealInterest(java.math.BigDecimal realInterest) {
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
    public Integer getCount() {
        return count;
    }
    public void setCount(Integer count) {
        this.count = count;
    }
    public java.math.BigDecimal getCountRepayMoney() {
        return countRepayMoney;
    }
    public void setCountRepayMoney(java.math.BigDecimal countRepayMoney) {
        this.countRepayMoney = countRepayMoney;
    }
    public java.math.BigDecimal getCountPayInterest() {
        return countPayInterest;
    }
    public void setCountPayInterest(java.math.BigDecimal countPayInterest) {
        this.countPayInterest = countPayInterest;
    }
    public java.math.BigDecimal getCountRealInterest() {
        return countRealInterest;
    }
    public void setCountRealInterest(java.math.BigDecimal countRealInterest) {
        this.countRealInterest = countRealInterest;
    }
    public Date getSignRepayDate() {
        return signRepayDate;
    }
    public void setSignRepayDate(Date signRepayDate) {
        this.signRepayDate = signRepayDate;
    }
}
