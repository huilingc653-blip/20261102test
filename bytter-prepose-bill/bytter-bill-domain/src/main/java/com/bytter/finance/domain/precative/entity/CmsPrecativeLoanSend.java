package com.bytter.finance.domain.precative.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import com.bytter.core.base.entity.IdEntity;
/**
 * @Title: Entity
 * @Description: 委托贷款发放表
 * @author onlineGenerator
 * @date 2017-07-29 10:45:29
 * @version 1.0
 *
 */
@Entity
@Table(name = "CMS_PRECATIVE_LOAN_SEND", schema = "")
@SuppressWarnings("serial")
public class CmsPrecativeLoanSend extends IdEntity implements java.io.Serializable {
    /** 委贷单位 */
    private String precativeId;
    /** 委贷申请编号 */
    private String fatherCode;
    /** 委贷发放编号 */
    private String billCode;
    /** 委贷发放合同编码 */
    private String contractCode;
    /** 发放金额 */
    private java.math.BigDecimal money;
    /** 利率 */
    private java.math.BigDecimal rate;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 发放日期 */
    private java.util.Date sendDate;
    /** 到期日期 */
    private java.util.Date deadlineDate;
    /** 期限 */
    private Integer term;
    /** 期限单位 */
    private String ternUnit;
    /** 还款方式 */
    private String repayType;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 委贷账号 */
    private String loanAcc;
    /** 委存账号 */
    private String depositAcc;
    /** 记账流水号 */
    private String orderNo2;
    /** 记账凭证号 */
    private String voucherNo2;
    /** 发放次数 */
    private Integer sendTime;
    /** 上传文件信息 */
    private String annexName;
    /** 浮动方式 */
    private String floatWay;
    /** 浮动二级方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    private java.math.BigDecimal pointsProportionText;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 利率调整生效日期 */
    private java.util.Date rateEntriesEffectDate;
    /** 计息方式 */
    private String interestWay;
    /** 基准利率类型 */
    private String referenceRateType;
    /** 贷款周期 */
    private String repayCycle;
    /** 利率复核生效日期 */
    private java.util.Date ratetimeoftakeeffect;
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
    /** 还款计划Id */
    private String planId;
    /** 展期到期日期 */
    private java.util.Date deferredDate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 到期提醒发送标识 */
    private String sendSign;
    /** 记账标志 */
    private String tallySign;
    /** 记账员代码 */
    private String tallyCode;
    /** 账务表ID */
    private String bookId;
    /** 全局业务ID */
    private String busGlobalId;
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委贷单位
     */
    @Column(name = "PRECATIVE_ID")
    public String getPrecativeId() {
        return this.precativeId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷单位
     */
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 账务表ID
     */
    @Column(name = "BOOK_ID")
    public String getBookId() {
        return this.bookId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷单位
     */
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 全局业务ID
     */
    @Column(name = "BUS_GLOBAL_ID")
    public String getBusGlobalId() {
        return this.busGlobalId;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             全局业务ID
     */
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委贷申请编号
     */
    @Column(name = "FATHER_CODE")
    public String getFatherCode() {
        return this.fatherCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷申请编号
     */
    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委贷发放编号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷发放编号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委贷发放合同编码
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷发放合同编码
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 发放金额
     */
    @Column(name = "MONEY")
    public java.math.BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             发放金额
     */
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 利率
     */
    @Column(name = "RATE")
    public java.math.BigDecimal getRate() {
        return this.rate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     * 
     * @param: java.math.BigDecimal
     *             利率
     */
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 逾期利率
     */
    @Column(name = "OVERDUE_RATE")
    public java.math.BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(java.math.BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 发放日期
     */
    @Column(name = "SEND_DATE")
    public java.util.Date getSendDate() {
        return this.sendDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             发放日期
     */
    public void setSendDate(java.util.Date sendDate) {
        this.sendDate = sendDate;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 到期日期
     */
    @Column(name = "DEADLINE_DATE")
    public java.util.Date getDeadlineDate() {
        return this.deadlineDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             到期日期
     */
    public void setDeadlineDate(java.util.Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 期限
     */
    @Column(name = "TERM")
    public Integer getTerm() {
        return this.term;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             期限
     */
    public void setTerm(Integer term) {
        this.term = term;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 期限单位
     */
    @Column(name = "TERN_UNIT")
    public String getTernUnit() {
        return this.ternUnit;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             期限单位
     */
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 还款方式
     */
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return this.repayType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             还款方式
     */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
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
     * @return: java.lang.String 委贷账号
     */
    @Column(name = "LOAN_ACC")
    public String getLoanAcc() {
        return this.loanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委贷账号
     */
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 委存账号
     */
    @Column(name = "DEPOSIT_ACC")
    public String getDepositAcc() {
        return this.depositAcc;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             委存账号
     */
    public void setDepositAcc(String depositAcc) {
        this.depositAcc = depositAcc;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账流水号
     */
    @Column(name = "ORDER_NO2")
    public String getOrderNo2() {
        return this.orderNo2;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账流水号
     */
    public void setOrderNo2(String orderNo2) {
        this.orderNo2 = orderNo2;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 记账凭证号
     */
    @Column(name = "VOUCHER_NO2")
    public String getVoucherNo2() {
        return this.voucherNo2;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             记账凭证号
     */
    public void setVoucherNo2(String voucherNo2) {
        this.voucherNo2 = voucherNo2;
    }
    /**
     * 方法: 取得java.lang.Integer
     * 
     * @return: java.lang.Integer 发放次数
     */
    @Column(name = "SEND_TIME")
    public Integer getSendTime() {
        return this.sendTime;
    }
    /**
     * 方法: 设置java.lang.Integer
     * 
     * @param: java.lang.Integer
     *             发放次数
     */
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 上传文件信息
     */
    @Column(name = "ANNEX_NAME")
    public String getAnnexName() {
        return this.annexName;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             上传文件信息
     */
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 浮动方式
     */
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return this.floatWay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             浮动方式
     */
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 浮动方式
     */
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 点数或浮动比例
     */
    @Column(name = "POINTS_PROPORTION_TEXT")
    public java.math.BigDecimal getPointsProportionText() {
        return this.pointsProportionText;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             点数或浮动比例
     */
    public void setPointsProportionText(java.math.BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 点数或者比例
     */
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return this.pointsProportionSelect;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             点数或者比例
     */
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 利率调整生效日期
     */
    @Column(name = "RATE_ENTRIES_EFFECT_DATE")
    public java.util.Date getRateEntriesEffectDate() {
        return this.rateEntriesEffectDate;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率调整生效日期
     */
    public void setRateEntriesEffectDate(java.util.Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 计息方式
     */
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return this.interestWay;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             计息方式
     */
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 基准利率类型
     */
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return this.referenceRateType;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             基准利率类型
     */
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 贷款周期
     */
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return this.repayCycle;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             贷款周期
     */
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 利率复核生效日期
     */
    @Column(name = "RATETIMEOFTAKEEFFECT")
    public java.util.Date getRatetimeoftakeeffect() {
        return this.ratetimeoftakeeffect;
    }
    /**
     * 方法: 设置java.util.Date
     * 
     * @param: java.util.Date
     *             利率复核生效日期
     */
    public void setRatetimeoftakeeffect(java.util.Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
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
    @Transient
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    /**
     * 方法: 取得java.util.Date
     * 
     * @return: java.util.Date 展期到期日期
     */
    @Column(name = "DEFERRED_DATE")
    public java.util.Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(java.util.Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     * 
     * @return: java.math.BigDecimal 展期利率
     */
    @Column(name = "DEFERRED_RATE")
    public java.math.BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(java.math.BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    /**
     * 方法: 取得java.lang.String
     * 
     * @return: java.lang.String 发送标识
     */
    @Column(name = "SEND_SIGN")
    public String getSendSign() {
        return this.sendSign;
    }
    /**
     * 方法: 设置java.lang.String
     * 
     * @param: java.lang.String
     *             发送标识
     */
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
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
