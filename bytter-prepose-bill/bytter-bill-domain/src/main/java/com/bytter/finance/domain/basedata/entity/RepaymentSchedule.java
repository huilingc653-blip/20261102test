package com.bytter.finance.domain.basedata.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 还款计划明细表
 * @author onlineGenerator
 * @date 2017-08-01 09:58:44
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_REPAYMENT_SCHEDULEDETAILED", schema = "")
@SuppressWarnings("serial")
public class RepaymentSchedule extends IdEntity implements java.io.Serializable {
    /** 业务单据号 */
    private String billCode;
    /** 还款周期 */
    private String repayCycel;
    /** 预计还款日 */
    private java.util.Date prediceRepayDate;
    /** 还款金额 */
    private java.math.BigDecimal repaymentMoney;
    /** 还款利息 */
    private java.math.BigDecimal repaymentIst;
    /** 本期还款金额 */
    private java.math.BigDecimal currentRepayMoney;
    /** 实际还款日 */
    private java.util.Date realityRepayDate;
    /** 实际还款本金 */
    private java.math.BigDecimal realityRepayMoney;
    /** 剩余本金 */
    private java.math.BigDecimal surplusMoney;
    /** 归还标识 */
    private String isRepay;
    /** 备注 */
    private String remark;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private java.util.Date updateDate;
    /** 还款开始日 */
    private java.util.Date repaymentBeginDate;
    /** 删除标志 -2:已删除 */
    private String valid_Sign;
    /**
     * A1 -内部贷款录入 A2 -- 内部贷款发放 B1 -银行借款录入 B2 -- 银行借款发放
     */
    private String internalSign;
    private String relationId;
    /** 到期还款标识 **/
    private String repaySign;
    @Column(name = "REPAY_SIGN")
    public String getRepaySign() {
        return repaySign;
    }
    public void setRepaySign(String repaySign) {
        this.repaySign = repaySign;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return java.lang.String 删除标志
     */
    @Column(name = "VALID_SIGN")
    public String getValid_Sign() {
        return valid_Sign;
    }
    @Column(name = "RELATION_ID")
    public String getRelationId() {
        return relationId;
    }
    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }
    public void setValid_Sign(String valid_Sign) {
        this.valid_Sign = valid_Sign;
    }
    @Column(name = "INTERNAL_SIGN")
    public String getInternalSign() {
        return internalSign;
    }
    public void setInternalSign(String internalSign) {
        this.internalSign = internalSign;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务单据号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             业务单据号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款周期
     */
    @Column(name = "REPAY_CYCEL")
    public String getRepayCycel() {
        return this.repayCycel;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款周期
     */
    public void setRepayCycel(String repayCycel) {
        this.repayCycel = repayCycel;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 预计还款日
     */
    @Column(name = "PREDICE_REPAY_DATE")
    public java.util.Date getPrediceRepayDate() {
        return this.prediceRepayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             预计还款日
     */
    public void setPrediceRepayDate(java.util.Date prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款金额
     */
    @Column(name = "REPAYMENT_MONEY")
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
    @Column(name = "REPAYMENT_IST")
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
    @Column(name = "CURRENT_REPAY_MONEY")
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
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 实际还款日
     */
    @Column(name = "REALITY_REPAY_DATE")
    public java.util.Date getRealityRepayDate() {
        return this.realityRepayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             实际还款日
     */
    public void setRealityRepayDate(java.util.Date realityRepayDate) {
        this.realityRepayDate = realityRepayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 实际还款本金
     */
    @Column(name = "REALITY_REPAY_MONEY")
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
    @Column(name = "SURPLUS_MONEY")
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
    @Column(name = "IS_REPAY")
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
    @Column(name = "REMARK")
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
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人名称
     */
    @Column(name = "CREATE_NAME")
    public String getCreateName() {
        return this.createName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人名称
     */
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 创建人登录名称
     */
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return this.createBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             创建人登录名称
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 创建日期
     */
    @Column(name = "CREATE_DATE")
    public java.util.Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             创建日期
     */
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人名称
     */
    @Column(name = "UPDATE_NAME")
    public String getUpdateName() {
        return this.updateName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人名称
     */
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 更新人登录名称
     */
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return this.updateBy;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             更新人登录名称
     */
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    /**
     * 还款开始日
     * 
     * @return
     */
    @Column(name = "REPAYMENT_BEGIN_DATE")
    public java.util.Date getRepaymentBeginDate() {
        return repaymentBeginDate;
    }
    @Override
    public String toString() {
        return "RepaymentSchedule [billCode=" + billCode + ", repayCycel=" + repayCycel + ", prediceRepayDate="
                + prediceRepayDate + ", repaymentMoney=" + repaymentMoney + ", repaymentIst=" + repaymentIst
                + ", currentRepayMoney=" + currentRepayMoney + ", realityRepayDate=" + realityRepayDate
                + ", realityRepayMoney=" + realityRepayMoney + ", surplusMoney=" + surplusMoney + ", isRepay=" + isRepay
                + ", remark=" + remark + ", createName=" + createName + ", createBy=" + createBy + ", createDate="
                + createDate + ", updateName=" + updateName + ", updateBy=" + updateBy + ", updateDate=" + updateDate
                + ", repaymentBeginDate=" + repaymentBeginDate + "]";
    }
    public void setRepaymentBeginDate(java.util.Date repaymentBeginDate) {
        this.repaymentBeginDate = repaymentBeginDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 更新日期
     */
    @Column(name = "UPDATE_DATE")
    public java.util.Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
}
