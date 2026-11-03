package com.bytter.pricing.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
/**
 * @Title: Entity
 * @Description: 存款利率类型
 * @author onlineGenerator
 * @date 2019-05-22
 * @version 1.0
 *
 */
@Entity
@Table(name = "TB_CREDIT_PERIOD", schema = "")
@SuppressWarnings("serial")
public class DepositRateType extends IdEntity implements java.io.Serializable {
    /**类型编码 */
    private String code;
    /**类型名称*/
    private String name;
    /**定期期限/通知类型*/
    private Integer period;
    /**期限周期类型*/
    private Integer periodCycle;
    /**货币类型*/
    private Integer moneyType;
    /**存款的总天数*/
    private Integer depositDays;
    /**存款种类*/
    private String depositClass;
    /**是否为系统预设值*/
    private String isSysvalue;
    /**利率种类 1-存款 0或者""-贷款 */
    private Integer interestRateType;
    /**备注*/
    private String remark;
    /**存款账户标识  1-内部账户存款 2-资金池账户存款 3-银行账户存款 */
    private String depositAccSign;
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  类型编码
     */
    @Column(name ="CODE")
    public String getCode(){
        return this.code;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  类型编码
     */
    public void setCode(String code){
        this.code = code;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  类型名称
     */
    @Column(name ="PERIOD_NAME")
    public String getName() {
        return name;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  类型名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  定期期限
     */
    @Column(name ="PERIOD")
    public Integer getPeriod(){
        return this.period;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  定期期限
     */
    public void setPeriod(Integer period){
        this.period = period;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  期限周期类型
     */
    @Column(name ="PERIOD_CYCLE")
    public Integer getPeriodCycle(){
        return this.periodCycle;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  期限周期类型
     */
    public void setPeriodCycle(Integer periodCycle){
        this.periodCycle = periodCycle;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  货币类型
     */
    @Column(name ="MONEY_TYPE")
    public Integer getMoneyType(){
        return this.moneyType;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  货币类型
     */
    public void setMoneyType(Integer moneyType){
        this.moneyType = moneyType;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  存款的总天数
     */
    @Column(name ="CREDIT_DAYS")
    public Integer getDepositDays() {
        return depositDays;
    }
    /**
     *方法: 设置java.lang.Integer
     *@param: java.lang.Integer  存款的总天数
     */
    public void setDepositDays(Integer depositDays) {
        this.depositDays = depositDays;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  是否为系统预设值
     */
    @Column(name ="IS_SYSVALUE")
    public String getIsSysvalue(){
        return this.isSysvalue;
    }
    /**
     *方法: 设置java.lang.String
     *@param: java.lang.String  是否为系统预设值
     */
    public void setIsSysvalue(String isSysvalue){
        this.isSysvalue = isSysvalue;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  存款种类
     */
    @Column(name ="LOAN_CLASS")
    public String getDepositClass() {
        return depositClass;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  存款种类
     */
    public void setDepositClass(String depositClass) {
        this.depositClass = depositClass;
    }
    /**
     *方法: 取得java.lang.Integer
     *@return: java.lang.Integer  利率种类
     */
    @Column(name ="INTEREST_RATE_TYPE")
    public Integer getInterestRateType() {
        return interestRateType;
    }
    /**
     *方法: 设置java.lang.Integer
     *@return: java.lang.Integer  利率种类
     */
    public void setInterestRateType(Integer interestRateType) {
        this.interestRateType = interestRateType;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  备注
     */
    @Column(name ="REMARK")
    public String getRemark() {
        return remark;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     *方法: 取得java.lang.String
     *@return: java.lang.String  存款账户标识
     */
    @Column(name ="DEPOSIT_ACC_SIGN")
    public String getDepositAccSign() {
        return depositAccSign;
    }
    /**
     *方法: 设置java.lang.String
     *@return: java.lang.String  存款账户标识
     */
    public void setDepositAccSign(String depositAccSign) {
        this.depositAccSign = depositAccSign;
    }
}
