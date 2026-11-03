package com.bytter.finance.domain.bond.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: CMS_BOND_REGISTER_INFO
 * @author mof
 * @date 2022-06-08 10:07:46
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_BOND_INTEREST", schema = "")
@SuppressWarnings("serial")
public class CmsBondInterest extends IdEntity implements java.io.Serializable {
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
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "ISSUE_ID")
    public String getIssueId() {
        return issueId;
    }
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }
    @Column(name = "PAY_ID")
    public String getPayId() {
        return payId;
    }
    public void setPayId(String payId) {
        this.payId = payId;
    }
    @Column(name = "INTEREST_TIME")
    public Date getInterestTime() {
        return interestTime;
    }
    public void setInterestTime(Date interestTime) {
        this.interestTime = interestTime;
    }
    @Column(name = "INTEREST_TYPE")
    public String getInterestType() {
        return interestType;
    }
    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "DAYS")
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }
    @Column(name = "CUMULATION")
    public String getCumulation() {
        return cumulation;
    }
    public void setCumulation(String cumulation) {
        this.cumulation = cumulation;
    }
    @Column(name = "BASE_RATE")
    public BigDecimal getBaseRate() {
        return baseRate;
    }
    public void setBaseRate(BigDecimal baseRate) {
        this.baseRate = baseRate;
    }
    @Column(name = "FLOAT_POINT")
    public Date getFloatPoint() {
        return floatPoint;
    }
    public void setFloatPoint(Date floatPoint) {
        this.floatPoint = floatPoint;
    }
    @Column(name = "INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name = "RATE_FAX")
    public BigDecimal getRateFax() {
        return rateFax;
    }
    public void setRateFax(BigDecimal rateFax) {
        this.rateFax = rateFax;
    }
    @Column(name = "REPAY_MONEY")
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    @Column(name = "REMIAN_MONEY")
    public BigDecimal getRemianMoney() {
        return remianMoney;
    }
    public void setRemianMoney(BigDecimal remianMoney) {
        this.remianMoney = remianMoney;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "CALCU_INTEREST")
    public BigDecimal getCalcuInterest() {
        return calcuInterest;
    }
    public void setCalcuInterest(BigDecimal calcuInterest) {
        this.calcuInterest = calcuInterest;
    }
    @Column(name = "INTEREST_FAX")
    public BigDecimal getInterestFax() {
        return interestFax;
    }
    public void setInterestFax(BigDecimal interestFax) {
        this.interestFax = interestFax;
    }
    @Column(name = "REAL_INTEREST")
    public BigDecimal getRealInterest() {
        return realInterest;
    }
    public void setRealInterest(BigDecimal realInterest) {
        this.realInterest = realInterest;
    }
    @Column(name = "INTEREST_SIGN")
    public String getInterestSign() {
        return interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    @Column(name = "STATUS")
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
