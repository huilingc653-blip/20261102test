package com.bytter.guarantee.dto;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
/**
 * Title: Entity Description: 还款计划表
 * 
 * @author onlineGenerator
 * @since 2017-08-01 09:58:44
 * @version 1.0
 *
 */
@SuppressWarnings("serial")
public class RepayPlanDTO extends IdEntity implements java.io.Serializable {
    /** 预计还款日 */
    private String prediceRepayDate;
    /** 还款金额 */
    private java.math.BigDecimal repaymentMoney;
    /** 还款利息 */
    private java.math.BigDecimal repaymentIst;
    /** 本期还款金额 */
    private java.math.BigDecimal currentRepayMoney;
    /** 实际还款日 */
    private String realityRepayDate;
    /** 实际还款本金 */
    private java.math.BigDecimal realityRepayMoney;
    /** 剩余本金 */
    private java.math.BigDecimal surplusMoney;
    /** 归还标识 */
    private String isRepay;
    /** 备注 */
    private String remark;
    /** 还款开始日 */
    private String repaymentBeginDate;
    /** 删除标志 -2:已删除 */
    private String valid_Sign;
    /**
     * A1 -内部贷款录入 A2 -- 内部贷款发放 B1 -银行借款录入 B2 -- 银行借款发放
     */
    private String internalSign;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return java.lang.String 删除标志
     */
    @Column(name = "VALID_SIGN", nullable = true, length = 32)
    public String getValid_Sign() {
        return valid_Sign;
    }
    public void setValid_Sign(String valid_Sign) {
        this.valid_Sign = valid_Sign;
    }
    @Column(name = "INTERNAL_SIGN", nullable = true, length = 32)
    public String getInternalSign() {
        return internalSign;
    }
    public void setInternalSign(String internalSign) {
        this.internalSign = internalSign;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 预计还款日
     */
    @Column(name = "PREDICE_REPAY_DATE", nullable = true)
    public String getPrediceRepayDate() {
        return this.prediceRepayDate;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             预计还款日
     */
    public void setPrediceRepayDate(String prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款金额
     */
    @Column(name = "REPAYMENT_MONEY", nullable = true, scale = 2, length = 15)
    public java.math.BigDecimal getRepaymentMoney() {
        return this.repaymentMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款金额
     */
    public void setRepaymentMoney(java.math.BigDecimal repaymentMoney) {
        this.repaymentMoney = repaymentMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款利息
     */
    @Column(name = "REPAYMENT_IST", nullable = true, scale = 2, length = 15)
    public java.math.BigDecimal getRepaymentIst() {
        return this.repaymentIst;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款利息
     */
    public void setRepaymentIst(java.math.BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 本期还款金额
     */
    @Column(name = "CURRENT_REPAY_MONEY", nullable = true, scale = 2, length = 15)
    public java.math.BigDecimal getCurrentRepayMoney() {
        return this.currentRepayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             本期还款金额
     */
    public void setCurrentRepayMoney(java.math.BigDecimal currentRepayMoney) {
        this.currentRepayMoney = currentRepayMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 实际还款日
     */
    @Column(name = "REALITY_REPAY_DATE", nullable = true)
    public String getRealityRepayDate() {
        return this.realityRepayDate;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             实际还款日
     */
    public void setRealityRepayDate(String realityRepayDate) {
        this.realityRepayDate = realityRepayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 实际还款本金
     */
    @Column(name = "REALITY_REPAY_MONEY", nullable = true, scale = 2, length = 15)
    public java.math.BigDecimal getRealityRepayMoney() {
        return this.realityRepayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             实际还款本金
     */
    public void setRealityRepayMoney(java.math.BigDecimal realityRepayMoney) {
        this.realityRepayMoney = realityRepayMoney;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 剩余本金
     */
    @Column(name = "SURPLUS_MONEY", nullable = true, scale = 2, length = 15)
    public java.math.BigDecimal getSurplusMoney() {
        return this.surplusMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             剩余本金
     */
    public void setSurplusMoney(java.math.BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 归还标识
     */
    @Column(name = "IS_REPAY", nullable = true, length = 1)
    public String getIsRepay() {
        return this.isRepay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             归还标识
     */
    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "REMARK", nullable = true, length = 100)
    public String getRemark() {
        return this.remark;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * 还款开始日
     * 
     * @return
     */
    @Column(name = "REPAYMENT_BEGIN_DATE", nullable = true)
    public String getRepaymentBeginDate() {
        return repaymentBeginDate;
    }
    public void setRepaymentBeginDate(String repaymentBeginDate) {
        this.repaymentBeginDate = repaymentBeginDate;
    }
    @Override
    public String toString() {
        return "RepayPlanDTO [prediceRepayDate=" + prediceRepayDate + ", repaymentMoney=" + repaymentMoney
                + ", repaymentIst=" + repaymentIst + ", currentRepayMoney=" + currentRepayMoney + ", realityRepayDate="
                + realityRepayDate + ", realityRepayMoney=" + realityRepayMoney + ", surplusMoney=" + surplusMoney
                + ", isRepay=" + isRepay + ", remark=" + remark + ", repaymentBeginDate=" + repaymentBeginDate
                + ", valid_Sign=" + valid_Sign + ", internalSign=" + internalSign + "]";
    }
}
