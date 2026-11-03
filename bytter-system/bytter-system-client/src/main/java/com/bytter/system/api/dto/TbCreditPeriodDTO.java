package com.bytter.system.api.dto;
import java.io.Serializable;
public class TbCreditPeriodDTO implements Serializable {
    private String id;
    /** 贷款周期的编码 */
    private String code;
    /** 贷款周期的名称 */
    private String periodName;
    /** 贷款期数 */
    private Integer period;
    /** 贷款期数类型 */
    private Integer periodCycle;
    /** 货币类型 */
    private Integer moneyType;
    /** 贷款的总天数 */
    private Integer creditDays;
    /** 贷款种类 */
    private String loanClass;
    /** 是否为系统预设值 */
    private String isSysvalue;
    /** 利率种类 1-存款 0或者""-贷款 */
    private Integer interestRateType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getPeriodName() {
        return periodName;
    }
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
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
    public Integer getMoneyType() {
        return moneyType;
    }
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }
    public Integer getCreditDays() {
        return creditDays;
    }
    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public String getIsSysvalue() {
        return isSysvalue;
    }
    public void setIsSysvalue(String isSysvalue) {
        this.isSysvalue = isSysvalue;
    }
    public Integer getInterestRateType() {
        return interestRateType;
    }
    public void setInterestRateType(Integer interestRateType) {
        this.interestRateType = interestRateType;
    }
}
