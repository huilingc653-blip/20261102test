package com.bytter.finance.domain.bond.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author mof
 * @version 1.0
 * @Title: Vo
 * @Description: 债券利息预计明细表
 * @date 2022-06-08 10:07:46
 */
@SuppressWarnings("serial")
public class CmsBondInterestVo implements Serializable {
    /** 所属单位 */
    private String corpId;
    /** 债券发行id */
    private String issueId;
    /** 债券还款支付表id */
    private String payId;
    /** 计息时间 */
    private Date interestTime;
    /** 计息类型 */
    private String interestType;
    /** 支付日期 */
    private Date repayDate;
    /** 起息日期 */
    private Date startDate;
    /** 结束日期 */
    private Date endDate;
    /** 计息天数 */
    private String days;
    /** 积数 */
    private String cumulation;
    /** 基准利率 */
    private BigDecimal baseRate;
    /** 浮动比例点数 */
    private Date floatPoint;
    /** 计息利率 */
    private BigDecimal interestRate;
    /** 税率 */
    private BigDecimal rateFax;
    /** 本金 */
    private BigDecimal repayMoney;
    /** 剩余本金 */
    private BigDecimal remianMoney;
    /** 归还本金利息 */
    private BigDecimal interest;
    /** 测算利息 */
    private BigDecimal calcuInterest;
    /** 利息税率 */
    private BigDecimal interestFax;
    /** 实际付息 */
    private BigDecimal realInterest;
    /** 计息标识 */
    private String interestSign;
    /** 还款状态 */
    private String status;
    /** 备注 */
    private String rmk;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private Date createDate;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    public Date getInterestTime() {
        return interestTime;
    }
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
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
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }
    public String getCumulation() {
        return cumulation;
    }
    public void setCumulation(String cumulation) {
        this.cumulation = cumulation;
    }
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    public Date getFloatPoint() {
        return floatPoint;
    }
    public void setFloatPoint(Date floatPoint) {
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
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRemianMoney() {
        return remianMoney;
    }
    public void setRemianMoney(BigDecimal remianMoney) {
        this.remianMoney = remianMoney;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public BigDecimal getCalcuInterest() {
        return calcuInterest;
    }
    public void setCalcuInterest(BigDecimal calcuInterest) {
        this.calcuInterest = calcuInterest;
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
