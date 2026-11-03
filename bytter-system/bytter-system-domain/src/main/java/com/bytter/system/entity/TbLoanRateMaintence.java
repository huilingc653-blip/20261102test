package com.bytter.system.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @Title: Entity
 * @Description: 贷款利率维护
 * @author onlineGenerator
 * @date 2017-07-26 18:38:18
 * @version 1.0
 *
 */
@Entity
@Table(name = "TB_LOAN_RATE_MAINTENCE", schema = "")
@SuppressWarnings("serial")
public class TbLoanRateMaintence extends IdEntity implements java.io.Serializable {
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
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 基准利率
     */
    @Column(name = "BASE_RATE_ID")
    public String getBaseRateId() {
        return this.baseRateId;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             基准利率
     */
    public void setBaseRateId(String baseRateId) {
        this.baseRateId = baseRateId;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 贷款期限
     */
    @Column(name = "CREDIT_PERIOD_ID")
    public String getCreditPeriodId() {
        return this.creditPeriodId;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             贷款期限
     */
    public void setCreditPeriodId(String creditPeriodId) {
        this.creditPeriodId = creditPeriodId;
    }
    @Column(name = "RATE_FROM_WAY")
    public String getRateFromWay() {
        return rateFromWay;
    }
    public void setRateFromWay(String rateFromWay) {
        this.rateFromWay = rateFromWay;
    }
    /**
     * 方法: 取得BigDecimal
     * 
     * @return: BigDecimal 基准利率
     */
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return this.rate;
    }
    /**
     * 方法: 设置BigDecimal
     * 
     * @param: BigDecimal
     *             基准利率
     */
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 利率上限
     */
    @Column(name = "RATE_UP_LIMIT")
    public BigDecimal getRateUpLimit() {
        return this.rateUpLimit;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             利率上限
     */
    public void setRateUpLimit(BigDecimal rateUpLimit) {
        this.rateUpLimit = rateUpLimit;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 利率下限
     */
    @Column(name = "RATE_DOWN_LIMIT")
    public BigDecimal getRateDownLimit() {
        return this.rateDownLimit;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             利率下限
     */
    public void setRateDownLimit(BigDecimal rateDownLimit) {
        this.rateDownLimit = rateDownLimit;
    }
    /**
     * 方法: 取得Date
     * 
     * @return: Date 生效时间
     */
    @Column(name = "TIME_OF_TAKE_EFFECT")
    public Date getTimeOfTakeEffect() {
        return this.timeOfTakeEffect;
    }
    /**
     * 方法: 设置Date
     * 
     * @param: Date
     *             生效时间
     */
    public void setTimeOfTakeEffect(Date timeOfTakeEffect) {
        this.timeOfTakeEffect = timeOfTakeEffect;
    }
    /**
     * 方法: 取得Date
     * 
     * @return: Date 最近修改时间
     */
    @Column(name = "TIME_OF_LAST_MODIFIER")
    public Date getTimeOfLastModifier() {
        return this.timeOfLastModifier;
    }
    /**
     * 方法: 设置Date
     * 
     * @param: Date
     *             最近修改时间
     */
    public void setTimeOfLastModifier(Date timeOfLastModifier) {
        this.timeOfLastModifier = timeOfLastModifier;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 是否是人民贷款利率
     */
    @Column(name = "IS_BASE_LOAN_RATE")
    public String getIsBaseLoanRate() {
        return this.isBaseLoanRate;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             是否是人民贷款利率
     */
    public void setIsBaseLoanRate(String isBaseLoanRate) {
        this.isBaseLoanRate = isBaseLoanRate;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 禁用
     */
    @Column(name = "IS_VALID")
    public String getIsValid() {
        return this.isValid;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             禁用
     */
    public void setIsValid(String isValid) {
        this.isValid = isValid;
    }
    /**
     * 方法: 取得Integer
     * 
     * @return: Integer 拆借类型
     */
    @Column(name = "BORROW_TYPE")
    public String getBorrowType() {
        return this.borrowType;
    }
    /**
     * 方法: 设置Integer
     * 
     * @param: Integer
     *             拆借类型
     */
    public void setBorrowType(String borrowType) {
        this.borrowType = borrowType;
    }
    /**
     * 方法: 取得String
     * 
     * @return: String 打回说明
     */
    @Column(name = "RETURNDE_SCRIBE")
    public String getReturndeScribe() {
        return this.returndeScribe;
    }
    /**
     * 方法: 设置String
     * 
     * @param: String
     *             打回说明
     */
    public void setReturndeScribe(String returndeScribe) {
        this.returndeScribe = returndeScribe;
    }
    /**
     * 方法: 取得String
     * 
     * @return: String 币别
     */
    @Column(name = "MONEY_TYPE")
    public String getMoneyType() {
        return this.moneyType;
    }
    /**
     * 方法: 设置String
     * 
     * @param: String
     *             币别
     */
    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }
    @Transient
    public String getCreditPreionName() {
        return creditPreionName;
    }
    public void setCreditPreionName(String creditPreionName) {
        this.creditPreionName = creditPreionName;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
