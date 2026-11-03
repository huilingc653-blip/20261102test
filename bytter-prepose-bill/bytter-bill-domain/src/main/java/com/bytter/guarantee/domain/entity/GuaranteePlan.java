package com.bytter.guarantee.domain.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
@Entity
@Table(name = "cme_guarantee_plan", schema = "")
public class GuaranteePlan extends IdEntity implements java.io.Serializable {
    /** 业务单据号 */
    private String billCode;
    /** 批次号 */
    private String ids;
    /** 还款日期 */
    private java.util.Date prediceRepayDate;
    /** 还款金额 */
    private java.math.BigDecimal repaymentMoney;
    /** 还款利息 */
    private java.math.BigDecimal repaymentIst;
    /** 本期还款金额 */
    private java.math.BigDecimal currentRepayMoney;
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
    @Column(name = "ids")
    public String getIds() {
        return ids;
    }
    public void setIds(String ids) {
        this.ids = ids;
    }
}
