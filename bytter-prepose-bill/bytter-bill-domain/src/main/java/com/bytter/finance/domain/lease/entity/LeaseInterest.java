package com.bytter.finance.domain.lease.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 租赁利息预计明细
 * @author mof
 * @date 2022-04-20 15:50:26
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_LEAS_INTEREST", schema = "")
@SuppressWarnings("serial")
public class LeaseInterest extends IdEntity implements java.io.Serializable {
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
    private String curId;
    /** 计息类型 */
    private String interestType;
    /** 还款日期 */
    private Date repayDate;
    /** 起息日期 */
    private Date interestStartDate;
    /** 结束日期 */
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
    /** 还款日期标识。标识分段属于这个时间的还款 */
    private Date signRepayDate;
    /** 还款到期提醒标志 **/
    private String repaySign;
    @Column(name = "REPAY_SIGN")
    public String getRepaySign() {
        return repaySign;
    }
    public void setRepaySign(String repaySign) {
        this.repaySign = repaySign;
    }
    @Column(name = "INTEREST_TIME")
    public Date getInterestTime() {
        return this.interestTime;
    }
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    @Column(name = "BILL_TYPE")
    public String getBillType() {
        return this.billType;
    }
    public void setBillType(String billType) {
        this.billType = billType;
    }
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return this.curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "INTEREST_TYPE")
    public String getInterestType() {
        return this.interestType;
    }
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return this.repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "INTEREST_START_DATE")
    public Date getInterestStartDate() {
        return this.interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    @Column(name = "BLN_INST_DATE")
    public Date getBlnInstDate() {
        return this.blnInstDate;
    }
    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }
    @Column(name = "DAYS")
    public Integer getDays() {
        return this.days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }
    @Column(name = "CUMULATION")
    public java.math.BigDecimal getCumulation() {
        return this.cumulation;
    }
    public void setCumulation(java.math.BigDecimal cumulation) {
        this.cumulation = cumulation;
    }
    @Column(name = "RATE")
    public java.math.BigDecimal getRate() {
        return this.rate;
    }
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "FLOAT_POINT")
    public java.math.BigDecimal getFloatPoint() {
        return this.floatPoint;
    }
    public void setFloatPoint(java.math.BigDecimal floatPoint) {
        this.floatPoint = floatPoint;
    }
    @Column(name = "INTEREST_RATE")
    public java.math.BigDecimal getInterestRate() {
        return this.interestRate;
    }
    public void setInterestRate(java.math.BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name = "RATE_FAX")
    public java.math.BigDecimal getRateFax() {
        return this.rateFax;
    }
    public void setRateFax(java.math.BigDecimal rateFax) {
        this.rateFax = rateFax;
    }
    @Column(name = "LOAN_MONEY")
    public java.math.BigDecimal getLoanMoney() {
        return this.loanMoney;
    }
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    @Column(name = "REPAY_MONEY")
    public java.math.BigDecimal getRepayMoney() {
        return this.repayMoney;
    }
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "LEFT_MONEY")
    public java.math.BigDecimal getLeftMoney() {
        return this.leftMoney;
    }
    public void setLeftMoney(java.math.BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    @Column(name = "INTEREST")
    public java.math.BigDecimal getInterest() {
        return this.interest;
    }
    public void setInterest(java.math.BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "PAY_INTEREST")
    public java.math.BigDecimal getPayInterest() {
        return this.payInterest;
    }
    public void setPayInterest(java.math.BigDecimal payInterest) {
        this.payInterest = payInterest;
    }
    @Column(name = "TOTAL_INTEREST")
    public java.math.BigDecimal getTotalInterest() {
        return this.totalInterest;
    }
    public void setTotalInterest(java.math.BigDecimal totalInterest) {
        this.totalInterest = totalInterest;
    }
    @Column(name = "UN_PAY_INTEREST")
    public java.math.BigDecimal getUnPayInterest() {
        return this.unPayInterest;
    }
    public void setUnPayInterest(java.math.BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }
    @Column(name = "INTEREST_FAX")
    public java.math.BigDecimal getInterestFax() {
        return this.interestFax;
    }
    public void setInterestFax(java.math.BigDecimal interestFax) {
        this.interestFax = interestFax;
    }
    @Column(name = "REAL_INTEREST")
    public java.math.BigDecimal getRealInterest() {
        return this.realInterest;
    }
    public void setRealInterest(java.math.BigDecimal realInterest) {
        this.realInterest = realInterest;
    }
    @Column(name = "INTEREST_SIGN")
    public String getInterestSign() {
        return this.interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "REPAYMENT_SCHEDULE_ID")
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    @Column(name = "SIGN_REPAY_DATE")
    public Date getSignRepayDate() {
        return signRepayDate;
    }
    public void setSignRepayDate(Date signRepayDate) {
        this.signRepayDate = signRepayDate;
    }
}
