package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/*
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: CMS_PROVIDE_LOAN_SEND
 * @date 2017-07-24 14:13:13
 */
@Entity
@Table(name = "CMS_PROVIDE_LOAN_SEND", schema = "")
@SuppressWarnings("serial")
public class ProvideLoanSend extends IdEntity implements java.io.Serializable {
    /**
     * 票据号
     */
    private String billCode;
    /*
     * 借款合同编号
     */
    private String contractCode;
    /*
     * 借款信息编码
     */
    private String loanCode;
    /*
     * 单位代码
     */
    private String corpId;
    /*
     * 发放金额
     */
    private BigDecimal money;
    /*
     * 发放日期
     */
    private java.util.Date sendDate;
    /*
     * 还款日期
     */
    private java.util.Date repayDate;
    /*
     * 计息财务季度
     */
    private String fiscalQuarter;
    /*
     * 计息日
     */
    private Integer expiryDate;
    /*
     * 预提日
     */
    private Integer accruedDailyDate;
    /*
     * 借款账号
     */
    private String loanAcc;
    /*
     * 银行活期账户
     */
    private String blnctrAcc;
    /*
     * 借款币种
     */
    private String curCode;
    /*
     * 借款利率
     */
    private BigDecimal plRate;
    /*
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /*
     * 展期利率
     */
    private BigDecimal deferredRate;
    /*
     * 展期日期
     */
    private java.util.Date deferredDate;
    /*
     * 税率
     */
    private BigDecimal interestRate;
    /*
     * 状态
     */
    private Integer status;
    /*
     * 记账凭证号
     */
    private String voucherNo;
    /*
     * 期限
     */
    private BigDecimal plTerm;
    /*
     * 记账流水号
     */
    private String orderNo;
    /*
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /*
     * 发放方式
     */
    private String loanSendOutType;
    /*
     * 还款方式
     */
    private String repayType;
    /*
     * 浮动方式
     */
    private String floatWay;
    /*
     * 二级浮动方式
     */
    private String floatWayNext;
    /*
     * 点数或浮动比例
     */
    private BigDecimal pointsProportionText;
    /*
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /*
     * 利率调整生效日期
     */
    private java.util.Date rateEntriesEffectDate;
    /*
     * 计息方式
     */
    private String interestWay;
    /*
     * 基准利率类型
     */
    private String referenceRateType;
    /*
     * 贷款周期
     */
    private String repayCycle;
    /*
     * 利率复核生效日期
     */
    private java.util.Date ratetimeoftakeeffect;
    /*
     * 预计利息
     */
    private BigDecimal anticInterest;
    /*
     * 发放次数
     */
    private Integer sendTime;
    /*
     * 打回原因
     */
    private String refuseWhy;
    /*
     * 创建人名称
     */
    private String createName;
    /*
     * 创建人登录名称
     */
    private String createBy;
    /*
     * 创建日期
     */
    private java.util.Date createDate;
    /*
     * 更新人名称
     */
    private String updateName;
    /*
     * 更新人登录名称
     */
    private String updateBy;
    /*
     * 更新日期
     */
    private java.util.Date updateDate;
    private String uploadId;
    private String bizStatus;
    private String sendSign;
    private String tallySign;
    private String tallyCode;
    /*
     * 银行流水认领功能挂接标记，未认领=0,认领=1
     */
    private String bankFlowSign;
    /*
     * 展期上浮点数
     */
    private BigDecimal deferredPoints;
    /*
     * 逾期上浮比例
     */
    private BigDecimal overdueProportion;
    /*
     * 计提方式
     */
    private String accrualType;
    /*
     * 银行名称
     */
    private String bankName;
    /*
     * 银行类别名称
     */
    private String bankTypeName;
    /*
     * 联行号
     */
    private String bankCode;
    /*
     * 币别名称
     */
    private String curName;
    /*
     * 单位名称
     */
    private String corpName;
    /**
     * 单位编码
     */
    private String corpCode;
    /**
     * 贷款期限名称
     */
    private String repayCycleName;
    /**
     * 还款账号
     */
    private String onlinePaymentAcc;

    /**
     * 还款账号名称
     */
    private String onlineAccName;

    /**
     * 还款开户行名称
     */
    private String onlineBankName;

    /**
     * 还款账号id
     */
    private String onlinePaymentAccId;
    /*
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 票据号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 票据号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款合同编号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款合同编号
     */
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款信息编码
     */
    @Column(name = "LOAN_CODE")
    public String getLoanCode() {
        return this.loanCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款信息编码
     */
    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
    }
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
     * 单位代码
     */
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 发放金额
     */
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return this.money;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 发放金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
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
     * 发放日期
     */
    public void setSendDate(java.util.Date sendDate) {
        this.sendDate = sendDate;
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
     * 还款日期
     */
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款账号
     */
    @Column(name = "LOAN_ACC")
    public String getLoanAcc() {
        return this.loanAcc;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款账号
     */
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款币种
     */
    @Column(name = "CUR_CODE")
    public String getCurCode() {
        return this.curCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款币种
     */
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 借款利率
     */
    @Column(name = "PL_RATE")
    public BigDecimal getPlRate() {
        return this.plRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 借款利率
     */
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 逾期利率
     */
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return this.overdueRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 逾期利率
     */
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 展期利率
     */
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return this.deferredRate;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 展期利率
     */
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
     * 状态
     */
    public void setStatus(Integer status) {
        this.status = status;
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
     * 记账凭证号
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
     * 记账流水号
     */
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 发放方式
     */
    @Column(name = "LOAN_SEND_OUT_TYPE")
    public String getLoanSendOutType() {
        return this.loanSendOutType;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 发放方式
     */
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
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
     * 还款方式
     */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
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
     * 浮动方式
     */
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 点数或浮动比例
     */
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return this.pointsProportionText;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 点数或浮动比例
     */
    public void setPointsProportionText(BigDecimal pointsProportionText) {
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
     * 点数或者比例
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
     * 利率调整生效日期
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
     * 计息方式
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
     * 基准利率类型
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
     * 贷款周期
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
     * 利率复核生效日期
     */
    public void setRatetimeoftakeeffect(java.util.Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 预计利息
     */
    @Column(name = "ANTIC_INTEREST")
    public BigDecimal getAnticInterest() {
        return this.anticInterest;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 预计利息
     */
    public void setAnticInterest(BigDecimal anticInterest) {
        this.anticInterest = anticInterest;
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
     * 发放次数
     */
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 打回原因
     */
    @Column(name = "REFUSE_WHY")
    public String getRefuseWhy() {
        return this.refuseWhy;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 打回原因
     */
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
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
     * 创建人名称
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
     * 创建人登录名称
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
     * 创建日期
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
     * 更新人名称
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
     * 更新人登录名称
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
     * 更新日期
     */
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "PL_TERM")
    public BigDecimal getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(BigDecimal plTerm) {
        this.plTerm = plTerm;
    }
    @Column(name = "FISCAL_QUARTER")
    public String getFiscalQuarter() {
        return fiscalQuarter;
    }
    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }
    @Column(name = "EXPIRY_DATE")
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    @Column(name = "ACCRUED_DAILY_DATE")
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    @Column(name = "BENCHMARK_RATE")
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    @Column(name = "INTEREST_RATE")
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    @Column(name = "UPLOAD_ID")
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    @Column(name = "BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "BLNCTR_ACC")
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    @Column(name = "DEFERRED_DATE")
    public java.util.Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(java.util.Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    @Column(name = "SEND_SIGN")
    public String getSendSign() {
        return sendSign;
    }
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
    }
    @Column(name = "TALLY_SIGN")
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    @Column(name = "TALLY_CODE")
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
    @Column(name = "BANK_FLOW_SIGN")
    public String getBankFlowSign() {
        return bankFlowSign;
    }
    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }
    @Column(name = "DEFERRED_POINTS")
    public BigDecimal getDeferredPoints() {
        return deferredPoints;
    }
    public void setDeferredPoints(BigDecimal deferredPoints) {
        this.deferredPoints = deferredPoints;
    }
    @Column(name = "OVERDUE_PROPORTION")
    public BigDecimal getOverdueProportion() {
        return overdueProportion;
    }
    public void setOverdueProportion(BigDecimal overdueProportion) {
        this.overdueProportion = overdueProportion;
    }
    @Column(name = "ACCRUAL_Type")
    public String getAccrualType() {
        return accrualType;
    }
    public void setAccrualType(String accrualType) {
        this.accrualType = accrualType;
    }
    @Column(name = "BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name = "BANK_TYPE_NAME")
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    @Column(name = "BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    @Column(name = "CORP_NAME")
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    @Column(name = "CORP_CODE")
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    @Column(name = "REPAY_CYCLE_NAME")
    public String getRepayCycleName() {
        return repayCycleName;
    }
    public void setRepayCycleName(String repayCycleName) {
        this.repayCycleName = repayCycleName;
    }
    @Column(name = "ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }


    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }

    @Column(name = "ONLINE_ACC_NAME")
    public String getOnlineAccName() {
        return onlineAccName;
    }

    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }

    @Column(name = "ONLINE_BANK_NAME")
    public String getOnlineBankName() {
        return onlineBankName;
    }

    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }

    @Column(name = "ONLINE_PAYMENT_ACC_ID")
    public String getOnlinePaymentAccId() {
        return onlinePaymentAccId;
    }

    public void setOnlinePaymentAccId(String onlinePaymentAccId) {
        this.onlinePaymentAccId = onlinePaymentAccId;
    }

}
