package com.bytter.credit.domain.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
import java.math.BigDecimal;
/**
 * @Title: Entity
 * @Description: 授信录入明细表
 * @author onlineGenerator
 * @date 2017-07-21 14:35:53
 * @version 1.0
 *
 */
@Entity
@Table(name = "RAT_BKRATION_DETAIL", schema = "")
@SuppressWarnings("serial")
public class RatBkrationDetail extends IdEntity implements java.io.Serializable {
    /** 授信主键 */
    private String ratCode;
    /** 品种标识 */
    private String itemFlag;
    /** 授信品种排序 */
    private Integer itemOrder;
    /** 授信品种 */
    private String itemCode;
    /** 授信金额 */
    private BigDecimal money;
    /** 敞口金额 */
    private BigDecimal openMoney;
    /** 已分配授信金额 */
    private BigDecimal superDistributeMoney;
    /** 已分配授信敞口金额 */
    private BigDecimal superDistributeOpenMoney;
    /** 追加金额 */
    private BigDecimal superAddMoney;
    /** 追加敞口金额 */
    private BigDecimal superAddOpenMoney;
    /** 担保条件 */
    private String assurecondition;
    /** 保证金比例 */
    private BigDecimal assurepercent;
    /** 利率 */
    private BigDecimal interestrate;
    /** 总额共享追加金额 */
    private BigDecimal totalMoneyAddMoney;
    /** 总额共享追加敞口额度 */
    private BigDecimal totalMoneyAddOpenMoney;
    /** 最大金额 */
    private BigDecimal maxMoney;
    /**
     * 综合额度使用标识
     */
    private String compSign;
    /** 追加最高额度 */
    private BigDecimal superAddMaxMoney;
    /** 保证金金额 **/
    private BigDecimal bondMoney;
    /** 保证金费率 **/
    private BigDecimal procedureFeeRate;
    /** 保证金费用 **/
    private BigDecimal procedureFee;
    /** 额度类型 0-不循环 1-循环 */
    private String creditType;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信主键
     */
    @Column(name = "RAT_CODE")
    public String getRatCode() {
        return this.ratCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信主键
     */
    public void setRatCode(String ratCode) {
        this.ratCode = ratCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 品种标识
     */
    @Column(name = "ITEM_FLAG")
    public String getItemFlag() {
        return this.itemFlag;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             品种标识
     */
    public void setItemFlag(String itemFlag) {
        this.itemFlag = itemFlag;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 授信品种排序
     */
    @Column(name = "ITEM_ORDER")
    public Integer getItemOrder() {
        return this.itemOrder;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             授信品种排序
     */
    public void setItemOrder(Integer itemOrder) {
        this.itemOrder = itemOrder;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 授信品种
     */
    @Column(name = "ITEM_CODE")
    public String getItemCode() {
        return this.itemCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             授信品种
     */
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信金额
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             授信金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 敞口金额
     */
    @Column(name = "OPEN_MONEY")
    public BigDecimal getOpenMoney() {
        return this.openMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             敞口金额
     */
    public void setOpenMoney(BigDecimal openMoney) {
        this.openMoney = openMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 最大金额
     */
    @Column(name = "MAX_MONEY")
    public BigDecimal getMaxMoney() {
        return this.maxMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             敞口金额
     */
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 已分配授信金额
     */
    @Column(name = "SUPER_DISTRIBUTE_MONEY")
    public BigDecimal getSuperDistributeMoney() {
        return this.superDistributeMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             已分配授信金额
     */
    public void setSuperDistributeMoney(BigDecimal superDistributeMoney) {
        this.superDistributeMoney = superDistributeMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 已分配授信敞口金额
     */
    @Column(name = "SUPER_DISTRIBUTE_OPEN_MONEY")
    public BigDecimal getSuperDistributeOpenMoney() {
        return this.superDistributeOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             已分配授信敞口金额
     */
    public void setSuperDistributeOpenMoney(BigDecimal superDistributeOpenMoney) {
        this.superDistributeOpenMoney = superDistributeOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加金额
     */
    @Column(name = "SUPER_ADD_MONEY")
    public BigDecimal getSuperAddMoney() {
        return this.superAddMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加金额
     */
    public void setSuperAddMoney(BigDecimal superAddMoney) {
        this.superAddMoney = superAddMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加敞口金额
     */
    @Column(name = "SUPER_ADD_OPEN_MONEY")
    public BigDecimal getSuperAddOpenMoney() {
        return this.superAddOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加敞口金额
     */
    public void setSuperAddOpenMoney(BigDecimal superAddOpenMoney) {
        this.superAddOpenMoney = superAddOpenMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 追加最高额度
     */
    @Column(name = "SUPER_ADD_MAX_MONEY")
    public BigDecimal getSuperAddMaxMoney() {
        return this.superAddMaxMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             追加最高额度
     */
    public void setSuperAddMaxMoney(BigDecimal superAddMaxMoney) {
        this.superAddMaxMoney = superAddMaxMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 担保条件
     */
    @Column(name = "ASSURECONDITION")
    public String getAssurecondition() {
        return this.assurecondition;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             担保条件
     */
    public void setAssurecondition(String assurecondition) {
        this.assurecondition = assurecondition;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 保证金比例
     */
    @Column(name = "ASSUREPERCENT")
    public BigDecimal getAssurepercent() {
        return this.assurepercent;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             保证金比例
     */
    public void setAssurepercent(BigDecimal assurepercent) {
        this.assurepercent = assurepercent;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 利率
     */
    @Column(name = "INTERESTRATE")
    public BigDecimal getInterestrate() {
        return this.interestrate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             利率
     */
    public void setInterestrate(BigDecimal interestrate) {
        this.interestrate = interestrate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 总额共享追加金额
     */
    @Column(name = "TOTAL_MONEY_ADD_MONEY")
    public BigDecimal getTotalMoneyAddMoney() {
        return this.totalMoneyAddMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             总额共享追加金额
     */
    public void setTotalMoneyAddMoney(BigDecimal totalMoneyAddMoney) {
        this.totalMoneyAddMoney = totalMoneyAddMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 总额共享追加敞口额度
     */
    @Column(name = "TOTAL_MONEY_ADD_OPEN_MONEY")
    public BigDecimal getTotalMoneyAddOpenMoney() {
        return this.totalMoneyAddOpenMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             总额共享追加敞口额度
     */
    public void setTotalMoneyAddOpenMoney(BigDecimal totalMoneyAddOpenMoney) {
        this.totalMoneyAddOpenMoney = totalMoneyAddOpenMoney;
    }
    /**
     * 方法: 取得String
     * 
     * @param: String
     *             综合额度使用标识
     */
    @Column(name = "COMP_SIGN")
    public String getCompSign() {
        return compSign;
    }
    @Column(name = "BOND_MONEY")
    public BigDecimal getBondMoney() {
        return bondMoney;
    }
    public void setBondMoney(BigDecimal bondMoney) {
        this.bondMoney = bondMoney;
    }
    @Column(name = "PROCEDURE_FEE_RATE")
    public BigDecimal getProcedureFeeRate() {
        return procedureFeeRate;
    }
    public void setProcedureFeeRate(BigDecimal procedureFeeRate) {
        this.procedureFeeRate = procedureFeeRate;
    }
    @Column(name = "PROCEDURE_FEE")
    public BigDecimal getProcedureFee() {
        return procedureFee;
    }
    public void setProcedureFee(BigDecimal procedureFee) {
        this.procedureFee = procedureFee;
    }
    /**
     * 方法: 设置comSign
     * 
     * @return: String 综合额度使用标识
     */
    public void setCompSign(String compSign) {
        this.compSign = compSign;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信额度类型
     */
    @Column(name = "CREDIT_TYPE")
    public String getCreditType() {
        return creditType;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 授信额度类型
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
}
