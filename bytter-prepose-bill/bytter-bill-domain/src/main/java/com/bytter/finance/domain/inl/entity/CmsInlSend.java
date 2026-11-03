package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_SEND", schema = "")
@SuppressWarnings("serial")
public class CmsInlSend extends IdEntity implements Serializable {
    /**
     * 申请表id
     */
    private String inlInfoId;
    /**
     * 业务编号
     */
    private String billNo;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 业务状态
     */
    private Integer bizStatus;
    /**
     * 发放编码
     */
    private String sendCode;
    /**
     * 发放类型，一次性发放，多次发放
     */
    private String sendType;
    /**
     * 拆入单位
     */
    private String borrowInCorp;
    /**
     * 拆出单位
     */
    private String borrowOutCorp;
    /**
     * 币种
     */
    private String curId;
    /**
     *
     */
    private BigDecimal sendMoney;
    /**
     * 发放十日
     */
    private Date sendDate;
    /**
     * 到期日期
     */
    private Date endDate;
    /**
     * 还款方式
     */
    private String repayType;
    /**
     * 计息方式
     */
    private String interestWay;
    /**
     * 计息日
     */
    private Integer expiryDate;
    /**
     * 预提日
     */
    private Integer accruedDailyDate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 浮动方式
     */
    private String floatWay;
    /**
     * 利率调整生效日期
     */
    private Date rateEntriesEffectDate;
    /**
     * 基准利率类型
     */
    private String referenceRateType;
    /**
     * 二级浮动方式
     */
    private String floatWayNext;
    /**
     * 拆借周期
     */
    private String repayCycle;
    /**
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /**
     * 点数或者比例值
     */
    private BigDecimal pointsProportionText;
    /**
     * 利息计息基数
     */
    private String rateBase;
    /**
     * 资金拆借利率
     */
    private BigDecimal rate;
    /**
     * 拆借利息
     */
    private BigDecimal interest;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /**
     * 拆入方收本金账户
     */
    private String borrowInAcc;
    /**
     * 拆入方账号id
     */
    private String borrowInAccId;
    /**
     * 拆入方账号
     */
    private String borrowOutAcc;
    /**
     * 拆出方账户id
     */
    private String borrowOutAccId;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否挂资金计划
     */
    private String isBudget;
    /**
     * 是否展期
     */
    private String isDeferred;
    /**
     * 展期日期到期日期
     */
    private Date deferredEndDate;
    /**
     * 实际发放十日
     */
    private Date realSendDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    @Column(name = "INL_INFO_ID")
    public String getInlInfoId() {
        return inlInfoId;
    }
    public void setInlInfoId(String inlInfoId) {
        this.inlInfoId = inlInfoId;
    }
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "SEND_CODE")
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    @Column(name = "SEND_TYPE")
    public String getSendType() {
        return sendType;
    }
    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
    @Column(name = "BORROW_IN_CORP")
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    @Column(name = "BORROW_OUT_CORP")
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "SEND_MONEY")
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    @Column(name = "SEND_DATE")
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "TERM")
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    @Column(name = "RATE_ENTRIES_EFFECT_DATE")
    public Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    @Column(name = "RATE_BASE")
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    @Column(name = "BORROW_IN_ACC")
    public String getBorrowInAcc() {
        return borrowInAcc;
    }
    public void setBorrowInAcc(String borrowInAcc) {
        this.borrowInAcc = borrowInAcc;
    }
    @Column(name = "BORROW_IN_ACCID")
    public String getBorrowInAccId() {
        return borrowInAccId;
    }
    public void setBorrowInAccId(String borrowInAccId) {
        this.borrowInAccId = borrowInAccId;
    }
    @Column(name = "BORROW_OUT_ACC")
    public String getBorrowOutAcc() {
        return borrowOutAcc;
    }
    public void setBorrowOutAcc(String borrowOutAcc) {
        this.borrowOutAcc = borrowOutAcc;
    }
    @Column(name = "BORROW_OUT_ACCID")
    public String getBorrowOutAccId() {
        return borrowOutAccId;
    }
    public void setBorrowOutAccId(String borrowOutAccId) {
        this.borrowOutAccId = borrowOutAccId;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "IS_BUDGET")
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    @Column(name = "IS_DEFERRED")
    public String getIsDeferred() {
        return isDeferred;
    }
    public void setIsDeferred(String isDeferred) {
        this.isDeferred = isDeferred;
    }
    @Column(name = "DEFERRED_END_DATE")
    public Date getDeferredEndDate() {
        return deferredEndDate;
    }
    public void setDeferredEndDate(Date deferredEndDate) {
        this.deferredEndDate = deferredEndDate;
    }
    @Column(name = "REAL_SEND_DATE")
    public Date getRealSendDate() {
        return realSendDate;
    }
    public void setRealSendDate(Date realSendDate) {
        this.realSendDate = realSendDate;
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
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    @Column(name = "EXPIRY_DATE")
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Column(name = "ACCRUED_DAILY_DATE")
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    @Column(name = "BENCHMARK_RATE")
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
}
