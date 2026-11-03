package com.bytter.report.domain.cms.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 贷款期限表
 * @author onlineGenerator
 * @date 2017-07-26 17:50:25
 * @version 1.0
 *
 */
@Entity
@Table(name = "TB_CREDIT_PERIOD", schema = "")
@SuppressWarnings("serial")
public class TbCreditPeriod extends IdEntity implements java.io.Serializable {
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
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款周期的编码
     */
    @Column(name = "CODE")
    public String getCode() {
        return this.code;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款周期的编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款周期的名称
     */
    @Column(name = "PERIOD_NAME")
    public String getPeriodName() {
        return this.periodName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款周期的名称
     */
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 贷款期数
     */
    @Column(name = "PERIOD")
    public Integer getPeriod() {
        return this.period;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             贷款期数
     */
    public void setPeriod(Integer period) {
        this.period = period;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 贷款期数类型
     */
    @Column(name = "PERIOD_CYCLE")
    public Integer getPeriodCycle() {
        return this.periodCycle;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             贷款期数类型
     */
    public void setPeriodCycle(Integer periodCycle) {
        this.periodCycle = periodCycle;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 货币类型
     */
    @Column(name = "MONEY_TYPE")
    public Integer getMoneyType() {
        return this.moneyType;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             货币类型
     */
    public void setMoneyType(Integer moneyType) {
        this.moneyType = moneyType;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 贷款的总天数
     */
    @Column(name = "CREDIT_DAYS")
    public Integer getCreditDays() {
        return this.creditDays;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             贷款的总天数
     */
    public void setCreditDays(Integer creditDays) {
        this.creditDays = creditDays;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 是否为系统预设值
     */
    @Column(name = "IS_SYSVALUE")
    public String getIsSysvalue() {
        return this.isSysvalue;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             是否为系统预设值
     */
    public void setIsSysvalue(String isSysvalue) {
        this.isSysvalue = isSysvalue;
    }
    @Column(name = "LOAN_CLASS")
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 利率种类
     */
    @Column(name = "INTEREST_RATE_TYPE")
    public Integer getInterestRateType() {
        return interestRateType;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @return: java.lang.Integer 利率种类
     */
    public void setInterestRateType(Integer interestRateType) {
        this.interestRateType = interestRateType;
    }
}
