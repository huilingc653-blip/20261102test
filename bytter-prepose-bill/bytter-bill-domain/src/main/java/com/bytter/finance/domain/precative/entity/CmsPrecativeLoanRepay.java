package com.bytter.finance.domain.precative.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 委托贷款还款信息表
 * @author onlineGenerator
 * @date 2017-08-02 14:05:07
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PRECATIVE_LOAN_REPAY", schema = "")
@SuppressWarnings("serial")
public class CmsPrecativeLoanRepay extends IdEntity implements java.io.Serializable {
    /** 还款单号 */
    private String billCode;
    /** 单位代码 */
    private String corpId;
    /** 贷款合同号 */
    private String contractCode;
    /** 贷款发放单号 */
    private String sendCode;
    /** 还款金额 */
    private java.math.BigDecimal repayMoney;
    /** 还款日期 */
    private java.util.Date repayDate;
    /** 还款利息 */
    private java.math.BigDecimal repayAccrual;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 贷款账号 */
    private String loanAcc;
    /** 还款账号 */
    private String repayAcc;
    /** 支付方式 */
    private String payType;
    /** 附件名称 */
    private String annexName;
    /** 还款计划 */
    private String repaymentScheduleId;
    /** 还款方式 */
    private String repayway;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
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
    /** 还款方式 */
    private String repayType;
    /** 记账标志 */
    private String tallySign;
    /** 记账员代码 */
    private String tallyCode;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 单位代码
     */
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return this.corpId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款合同号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款发放单号
     */
    @Column(name = "SEND_CODE")
    public String getSendCode() {
        return this.sendCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款发放单号
     */
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款金额
     */
    @Column(name = "REPAY_MONEY")
    public java.math.BigDecimal getRepayMoney() {
        return this.repayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款金额
     */
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 还款日期
     */
    @Column(name = "REPAY_DATE")
    public java.util.Date getRepayDate() {
        return this.repayDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             还款日期
     */
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 还款利息
     */
    @Column(name = "REPAY_ACCRUAL")
    public java.math.BigDecimal getRepayAccrual() {
        return this.repayAccrual;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             还款利息
     */
    public void setRepayAccrual(java.math.BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账凭证号
     */
    @Column(name = "VOUCHER_NO")
    public String getVoucherNo() {
        return this.voucherNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账凭证号
     */
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账流水号
     */
    @Column(name = "ORDER_NO")
    public String getOrderNo() {
        return this.orderNo;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款账号
     */
    @Column(name = "LOAN_ACC")
    public String getLoanAcc() {
        return this.loanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款账号
     */
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款账号
     */
    @Column(name = "REPAY_ACC")
    public String getRepayAcc() {
        return this.repayAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款账号
     */
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 支付方式
     */
    @Column(name = "PAY_TYPE")
    public String getPayType() {
        return this.payType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             支付方式
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 附件名称
     */
    @Column(name = "ANNEX_NAME")
    public String getAnnexName() {
        return this.annexName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             附件名称
     */
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款计划
     */
    @Column(name = "REPAYMENT_SCHEDULE_ID")
    public String getRepaymentScheduleId() {
        return this.repaymentScheduleId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款计划
     */
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款方式
     */
    @Column(name = "REPAYWAY")
    public String getRepayway() {
        return this.repayway;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款方式
     */
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 备注
     */
    @Column(name = "RMK")
    public String getRmk() {
        return this.rmk;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 状态
     */
    @Column(name = "STATUS")
    public Integer getStatus() {
        return this.status;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             状态
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 业务状态
     */
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.Integer
     *             业务状态
     */
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
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
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账标志
     */
    @Column(name = "TALLY_SIGN")
    public String getTallySign() {
        return tallySign;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账标志
     */
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账员代码
     */
    @Column(name = "TALLY_CODE")
    public String getTallyCode() {
        return tallyCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账员代码
     */
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
}
