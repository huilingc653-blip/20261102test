package com.bytter.system.api.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.bytter.system.api.dto.TbRateMaintenceDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 * 贷款利率维护VO
 *
 * @author lvwl
 *
 */
public class LoanRateMaintenceVO implements Serializable {
    /*** 编辑保存数据 ***/
    List<TbRateMaintenceDTO> mainList = new ArrayList<TbRateMaintenceDTO>();
    private String id;
    /** 基准利率Id */
    @BtCompositeSearch(fieldName = "mai.base_rate_id")
    private String baseRateId;
    /** 基准利率名称 */
    @BtCompositeSearch(fieldName = "base.base_type_name")
    private String baseRateName;
    /** 贷款期限ID */
    @BtCompositeSearch(fieldName = "mai.credit_period_id")
    private String creditPeriodId;
    /** 贷款期限名称 */
    @BtCompositeSearch(fieldName = "per.period_name")
    private String creditPeriodName;
    /** 贷款期限类型 */
    @Dict(dictGroup = "cms.LoanClass", inject = "loanClassName")
    @BtCompositeSearch(fieldName = "per.loan_class")
    private String loanClass;
    /** 贷款期限类型名称 短中长期 */
    @BtCompositeSearch(fieldName = "per.loan_class")
    private String loanClassName;
    /** 贷款期数 */
    @BtCompositeSearch(fieldName = "per.period")
    private Integer period;
    /** 贷款期数类型 */
    @Dict(dictGroup = "cms.periodCycle", inject = "periodCycleName")
    @BtCompositeSearch(fieldName = "per.period_cycle")
    private Integer periodCycle;
    /** 贷款期数类型名称 */
    @BtCompositeSearch(fieldName = "per.period_cycle")
    private String periodCycleName;
    /** 利率获取方式 */
    @BtCompositeSearch(fieldName = "mai.rate_from_way")
    private String rateFromWay;
    /** 基准利率 */
    @BtCompositeSearch(fieldName = "mai.rate")
    private BigDecimal rate;
    /** 利率上限 */
    @BtCompositeSearch(fieldName = "mai.rate_up_limit")
    private BigDecimal rateUpLimit;
    /** 利率下限 */
    @BtCompositeSearch(fieldName = "mai.rate_down_limit")
    private BigDecimal rateDownLimit;
    /** 生效时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "mai.time_of_take_effect")
    private Date timeOfTakeEffect;
    /** 最近修改时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "mai.time_of_last_modifier")
    private Date timeOfLastModifier;
    /** 是否是本位币贷款利率 */
    @BtCompositeSearch(fieldName = "mai.is_base_loan_rate")
    private String isBaseLoanRate;
    /** 状态 */
    @Dict(dictGroup = "LoanRate.Status", inject = "statusName")
    @BtCompositeSearch(fieldName = "mai.status")
    private Integer status;
    /** 状态名称 */
    @BtCompositeSearch(fieldName = "mai.status")
    private String statusName;
    /** 禁用 */
    @Dict(dictGroup = "LoanRate.IsValid", inject = "isValidName")
    @BtCompositeSearch(fieldName = "mai.is_valid")
    private String isValid;
    /** 是否禁用名称 */
    @BtCompositeSearch(fieldName = "mai.is_valid")
    private String isValidName;
    /** 拆借类型 */
    @BtCompositeSearch(fieldName = "mai.borrow_type")
    @Dict(dictGroup = "LoanRate.BorrowType", inject = "borrowTypeName")
    private String borrowType;
    @BtCompositeSearch(fieldName = "mai.borrow_type")
    private String borrowTypeName;
    /** 打回说明 */
    private String returndeScribe;
    /** 币别 */
    @BtCompositeSearch(fieldName = "mai.money_type")
//    @Currency(inject = "moneyTypeName") TODO 跨域访问已失效
    private String moneyType;
    private String moneyTypeName;
    /** 创建人 **/
    @BtCompositeSearch(fieldName = "mai.create_by")
    private String createBy;
    private String code;
    /**
     * 是否央行基准利率
     */
    private String baseRateCode;
    /**
     * 是否人民币利率，1-是，0-否
     */
    private String isBankBaseRate;
    private Map<String, LoanRateMaintenceVO> effectDate2LoanRateMaintence;
    private String[] allEffectDateArray;
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
    public String getBaseRateName() {
        return baseRateName;
    }
    public void setBaseRateName(String baseRateName) {
        this.baseRateName = baseRateName;
    }
    public String getCreditPeriodId() {
        return creditPeriodId;
    }
    public void setCreditPeriodId(String creditPeriodId) {
        this.creditPeriodId = creditPeriodId;
    }
    public String getCreditPeriodName() {
        return creditPeriodName;
    }
    public void setCreditPeriodName(String creditPeriodName) {
        this.creditPeriodName = creditPeriodName;
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
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getIsValid() {
        return isValid;
    }
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    public String getIsValidName() {
        return isValidName;
    }
    public void setIsValidName(String isValidName) {
        this.isValidName = isValidName;
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
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public String getLoanClassName() {
        return loanClassName;
    }
    public void setLoanClassName(String loanClassName) {
        this.loanClassName = loanClassName;
    }
    public Integer getPeriod() {
        return period;
    }
    public void setPeriod(Integer period) {
        this.period = period;
    }
    public Integer getPeriodCycle() {
        return periodCycle;
    }
    public void setPeriodCycle(Integer periodCycle) {
        this.periodCycle = periodCycle;
    }
    public String getPeriodCycleName() {
        return periodCycleName;
    }
    public void setPeriodCycleName(String periodCycleName) {
        this.periodCycleName = periodCycleName;
    }
    public List<TbRateMaintenceDTO> getMainList() {
        return mainList;
    }
    public void setMainList(List<TbRateMaintenceDTO> mainList) {
        this.mainList = mainList;
    }
    public String getBorrowTypeName() {
        return borrowTypeName;
    }
    public void setBorrowTypeName(String borrowTypeName) {
        this.borrowTypeName = borrowTypeName;
    }
    public String getMoneyTypeName() {
        return moneyTypeName;
    }
    public void setMoneyTypeName(String moneyTypeName) {
        this.moneyTypeName = moneyTypeName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getBaseRateCode() {
        return baseRateCode;
    }
    public void setBaseRateCode(String baseRateCode) {
        this.baseRateCode = baseRateCode;
    }
    public Map<String, LoanRateMaintenceVO> getEffectDate2LoanRateMaintence() {
        return effectDate2LoanRateMaintence;
    }
    public void setEffectDate2LoanRateMaintence(Map<String, LoanRateMaintenceVO> effectDate2LoanRateMaintence) {
        this.effectDate2LoanRateMaintence = effectDate2LoanRateMaintence;
    }
    public String[] getAllEffectDateArray() {
        return allEffectDateArray;
    }
    public void setAllEffectDateArray(String[] allEffectDateArray) {
        this.allEffectDateArray = allEffectDateArray;
    }
}
