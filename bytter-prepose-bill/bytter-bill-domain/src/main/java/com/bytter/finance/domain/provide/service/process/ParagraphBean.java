package com.bytter.finance.domain.provide.service.process;
import com.bytter.finance.domain.basedata.entity.AccrualType;
import com.bytter.finance.domain.basedata.entity.RepayType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 分段类
 *
 * @author lvwl
 *
 */
public class ParagraphBean implements Serializable {
    private Date startDate;// 开始日期
    private Date endDate;// 结束日期
    private BigDecimal money;// 本金
    private BigDecimal repayMoney;// 还款金额
    private BigDecimal rate;// 基准利率
    private BigDecimal plRate;// 借款利率
    private BigDecimal deferredRate;// 展期利率
    private BigDecimal overdueRate;// 预期利率
    private boolean isDeferred;// 是否展期
    private boolean isOverdue;// 是否展期
    private String interestWay;// 计息方式
    private Integer expiryDate;// 计息日
    private Integer accruedDailyDate;// 预提日
    /**
     * 还款方式
     */
    private RepayType repayType;
    /**
     * 计提方式
     */
    private AccrualType accrualType;
    private String isStanderd;// 是否标准季、半年、年
    private boolean isFloatWay;// 是否浮动
    private String floatWay;// 浮动方式
    private String floatWayNext;// 具体浮动方式
    private String interestSign;// 计息标识
    private String repaymentScheduleId;// 还款计划ID
    private Date signRepayDate;// 还款日期标识。标识分段属于这个时间的还款
    private boolean isRepayCorpus;// 是否归还本金
    private boolean isRepayInt;// 是否归还利息
    private String newBaseRateData;// 是否基准利率新增分段
    private BigDecimal pointsText;// 点数或浮动比例
    //手续费
    private BigDecimal serviceCharge;
    /**
     * 1=旧版计提计息，2新版计提计息
     */
    private String oldOrNew;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public String getInterestSign() {
        return interestSign;
    }
    public void setInterestSign(String interestSign) {
        this.interestSign = interestSign;
    }
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    public Date getSignRepayDate() {
        return signRepayDate;
    }
    public void setSignRepayDate(Date signRepayDate) {
        this.signRepayDate = signRepayDate;
    }
    public boolean isDeferred() {
        return isDeferred;
    }
    public void setDeferred(boolean deferred) {
        isDeferred = deferred;
    }
    public boolean isOverdue() {
        return isOverdue;
    }
    public void setOverdue(boolean overdue) {
        isOverdue = overdue;
    }
    public RepayType getRepayType() {
        return repayType;
    }
    public void setRepayType(RepayType repayType) {
        this.repayType = repayType;
    }
    public AccrualType getAccrualType() {
        return accrualType;
    }
    public void setAccrualType(AccrualType accrualType) {
        this.accrualType = accrualType;
    }
    public boolean isFloatWay() {
        return isFloatWay;
    }
    public void setFloatWay(boolean floatWay) {
        isFloatWay = floatWay;
    }
    public boolean isRepayCorpus() {
        return isRepayCorpus;
    }
    public void setRepayCorpus(boolean repayCorpus) {
        isRepayCorpus = repayCorpus;
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
    public String getIsStanderd() {
        return isStanderd;
    }
    public void setIsStanderd(String isStanderd) {
        this.isStanderd = isStanderd;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public boolean getIsRepayCorpus() {
        return isRepayCorpus;
    }
    public void setIsRepayCorpus(boolean isRepayCorpus) {
        this.isRepayCorpus = isRepayCorpus;
    }
    public boolean getIsRepayInt() {
        return isRepayInt;
    }
    public void setIsRepayInt(boolean repayInt) {
        isRepayInt = repayInt;
    }
    public boolean getIsFloatWay() {
        return isFloatWay;
    }
    public void setIsFloatWay(boolean isFloatWay) {
        this.isFloatWay = isFloatWay;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public boolean getIsDeferred() {
        return isDeferred;
    }
    public void setIsDeferred(boolean isDeferred) {
        this.isDeferred = isDeferred;
    }
    public boolean getIsOverdue() {
        return isOverdue;
    }
    public void setIsOverdue(boolean isOverdue) {
        this.isOverdue = isOverdue;
    }
    public String getNewBaseRateData() {
        return newBaseRateData;
    }
    public void setNewBaseRateData(String newBaseRateData) {
        this.newBaseRateData = newBaseRateData;
    }
    public BigDecimal getPointsText() {
        return pointsText;
    }
    public void setPointsText(BigDecimal pointsText) {
        this.pointsText = pointsText;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    public String getOldOrNew() {
        return oldOrNew;
    }
    public void setOldOrNew(String oldOrNew) {
        this.oldOrNew = oldOrNew;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }
}
