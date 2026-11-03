package com.bytter.finance.domain.provide.vo;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * <br>
 * <b>功能：</b>利息测算VO<br>
 */
public class InterestVO implements java.io.Serializable {
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date sendDate;// 初始发放日
    boolean userNewRate;// 使用启用新浮动利率
    Calendar repayCalendar;// 还款日
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date interestStartDate;// 起息日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date blnInstDate;// 结束日期
    BigDecimal loanMoney;// 本金
    BigDecimal unPayInterest;// 未结利息
    Integer interestDay;// 计息日
    Integer predrawDay;// 预提日
    String interestWay;// 计息方式
    String isStanderd;// 是否标准季/半年/年
    BigDecimal repayMoney;// 预计还款金额
    boolean lastRepayment;// 最后一次还款
    String floatWay;// 浮动方式
    String floatWayNext;// 浮动点数
    String floatWayStanderd;// 浮动是否标准季/半年/年
    BigDecimal floatFixRate;// 浮动方式为固定时的利率
    boolean lastIstWay;// 最后一次计息方式调整
    boolean lastFloat;// 最后一次浮动
    boolean floatTimes;// 多次浮动
    BigDecimal interestRate;// 计息利率
    String interestSign;// 计息标识
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date effectDate;// 浮动方式调整生效日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date lastReapyDate;// 上次计算测算日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date SignRepayDate;// 还款日期标识。标识分段属于这个时间点还款的分段
    /** 生成利息预计的还款计划ID */
    private String repaymentScheduleId;
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public boolean isUserNewRate() {
        return userNewRate;
    }
    public void setUserNewRate(boolean userNewRate) {
        this.userNewRate = userNewRate;
    }
    public Calendar getRepayCalendar() {
        return repayCalendar;
    }
    public void setRepayCalendar(Calendar repayCalendar) {
        this.repayCalendar = repayCalendar;
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
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public BigDecimal getUnPayInterest() {
        return unPayInterest;
    }
    public void setUnPayInterest(BigDecimal unPayInterest) {
        this.unPayInterest = unPayInterest;
    }
    public Integer getInterestDay() {
        if (interestDay == null) {
            interestDay = new Integer(0);
        }
        return interestDay;
    }
    public void setInterestDay(Integer interestDay) {
        this.interestDay = interestDay;
    }
    public Integer getPredrawDay() {
        if (predrawDay == null) {
            predrawDay = new Integer(0);
        }
        return predrawDay;
    }
    public void setPredrawDay(Integer predrawDay) {
        this.predrawDay = predrawDay;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getIsStanderd() {
        return isStanderd;
    }
    public void setIsStanderd(String isStanderd) {
        this.isStanderd = isStanderd;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public boolean isLastRepayment() {
        return lastRepayment;
    }
    public void setLastRepayment(boolean lastRepayment) {
        this.lastRepayment = lastRepayment;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getFloatWayStanderd() {
        return floatWayStanderd;
    }
    public void setFloatWayStanderd(String floatWayStanderd) {
        this.floatWayStanderd = floatWayStanderd;
    }
    public BigDecimal getFloatFixRate() {
        return floatFixRate;
    }
    public void setFloatFixRate(BigDecimal floatFixRate) {
        this.floatFixRate = floatFixRate;
    }
    public boolean isLastIstWay() {
        return lastIstWay;
    }
    public void setLastIstWay(boolean lastIstWay) {
        this.lastIstWay = lastIstWay;
    }
    public boolean isLastFloat() {
        return lastFloat;
    }
    public void setLastFloat(boolean lastFloat) {
        this.lastFloat = lastFloat;
    }
    public boolean isFloatTimes() {
        return floatTimes;
    }
    public void setFloatTimes(boolean floatTimes) {
        this.floatTimes = floatTimes;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public String getInterestSign() {
        return interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    public Date getEffectDate() {
        return effectDate;
    }
    public void setEffectDate(Date effectDate) {
        this.effectDate = effectDate;
    }
    public Date getLastReapyDate() {
        return lastReapyDate;
    }
    public void setLastReapyDate(Date lastReapyDate) {
        this.lastReapyDate = lastReapyDate;
    }
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public Date getSignRepayDate() {
        return SignRepayDate;
    }
    public void setSignRepayDate(Date signRepayDate) {
        SignRepayDate = signRepayDate;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    public InterestVO() {
        super();
        this.userNewRate = false;
        this.lastRepayment = false;
        this.lastIstWay = false;
        this.lastFloat = false;
        this.floatTimes = false;
    }
    public InterestVO(Date interestStartDate, BigDecimal loanMoney, BigDecimal unPayInterest) {
        super();
        this.userNewRate = false;
        this.lastRepayment = false;
        this.interestStartDate = interestStartDate;
        this.loanMoney = loanMoney;
        this.unPayInterest = unPayInterest;
    }
    public InterestVO(Date interestStartDate, Date blnInstDate, BigDecimal loanMoney, BigDecimal unPayInterest) {
        super();
        this.userNewRate = false;
        this.interestStartDate = interestStartDate;
        this.blnInstDate = blnInstDate;
        this.loanMoney = loanMoney;
        this.unPayInterest = unPayInterest;
    }
    public InterestVO(Calendar repayCalendar, Date interestStartDate, Date blnInstDate, BigDecimal loanMoney,
            BigDecimal unPayInterest) {
        super();
        this.repayCalendar = repayCalendar;
        this.interestStartDate = interestStartDate;
        this.blnInstDate = blnInstDate;
        this.loanMoney = loanMoney;
        this.unPayInterest = unPayInterest;
    }
}
