package com.bytter.system.api.dto;
import com.bytter.core.annotation.Dict;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class TbRateMaintenceDTO implements Serializable {
    private String id;
    /** 基准利率 */
    private String baseRateId;
    /** 贷款期限 */
    private String creditPeriodId;
    /** 利率获取方式 */
    private String rateFromWay;
    /** 基准利率 */
    private BigDecimal rate;
    /** 利率上限 */
    private BigDecimal rateUpLimit;
    /** 利率下限 */
    private BigDecimal rateDownLimit;
    /** 生效时间 */
    private Date timeOfTakeEffect;
    /** 最近修改时间 */
    private Date timeOfLastModifier;
    /** 是否是本位币贷款利率 */
    private String isBaseLoanRate;
    /** 状态 */
    private Integer status;
    /** 禁用 */
    private String isValid;
    /** 拆借类型 */
    private String borrowType;
    /** 打回说明 */
    private String returndeScribe;
    /** 币别 */
    private String moneyType;
    private String creditPreionName;
    /** 创建人 **/
    private String createBy;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBaseRateId() {
        return baseRateId;
    }
    public void setBaseRateId(String baseRateId) {
        this.baseRateId = baseRateId;
    }
    public String getCreditPeriodId() {
        return creditPeriodId;
    }
    public void setCreditPeriodId(String creditPeriodId) {
        this.creditPeriodId = creditPeriodId;
    }
    public String getRateFromWay() {
        return rateFromWay;
    }
    public void setRateFromWay(String rateFromWay) {
        this.rateFromWay = rateFromWay;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getRateUpLimit() {
        return rateUpLimit;
    }
    public void setRateUpLimit(BigDecimal rateUpLimit) {
        this.rateUpLimit = rateUpLimit;
    }
    public BigDecimal getRateDownLimit() {
        return rateDownLimit;
    }
    public void setRateDownLimit(BigDecimal rateDownLimit) {
        this.rateDownLimit = rateDownLimit;
    }
    public Date getTimeOfTakeEffect() {
        return timeOfTakeEffect;
    }
    public void setTimeOfTakeEffect(Date timeOfTakeEffect) {
        this.timeOfTakeEffect = timeOfTakeEffect;
    }
    public Date getTimeOfLastModifier() {
        return timeOfLastModifier;
    }
    public void setTimeOfLastModifier(Date timeOfLastModifier) {
        this.timeOfLastModifier = timeOfLastModifier;
    }
    public String getIsBaseLoanRate() {
        return isBaseLoanRate;
    }
    public void setIsBaseLoanRate(String isBaseLoanRate) {
        this.isBaseLoanRate = isBaseLoanRate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getBorrowType() {
        return borrowType;
    }
    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }
    public String getReturndeScribe() {
        return returndeScribe;
    }
    public void setReturndeScribe(String returndeScribe) {
        this.returndeScribe = returndeScribe;
    }
    public String getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
    public String getCreditPreionName() {
        return creditPreionName;
    }
    public void setCreditPreionName(String creditPreionName) {
        this.creditPreionName = creditPreionName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
