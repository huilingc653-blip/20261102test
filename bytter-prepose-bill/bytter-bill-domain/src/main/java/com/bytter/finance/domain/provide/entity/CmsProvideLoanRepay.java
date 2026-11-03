package com.bytter.finance.domain.provide.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: 借款还款
 * @date 2017-08-08 15:15:54
 */
@Entity
@Table(name = "CMS_PROVIDE_LOAN_REPAY", schema = "")
@SuppressWarnings("serial")
public class CmsProvideLoanRepay extends IdEntity implements java.io.Serializable {
    /**
     * 还款单号
     */
    private String billCode;
    /**
     * 单位代码
     */
    private String corpId;
    /**
     * 借款合同号
     */
    private String contractCode;
    /**
     * 贷款发放单号
     */
    private String sendCode;
    /**
     * 还款金额
     */
    private BigDecimal repayMoney;
    /**
     * 还款金额类型
     */
    private String repayMoneyType;
    /**
     * 还款日期
     */
    private Date repayDate;
    /**
     * 归还利息
     */
    private BigDecimal revertAccrual;
    /**
     * 还款利息
     */
    private BigDecimal repayAccrual;
    /**
     * 记账凭证号
     */
    private String voucherNo;
    /**
     * 记账流水号
     */
    private String orderNo;
    /**
     * 借款账号
     */
    private String loanAcc;
    /**
     * 还款账号
     */
    private String repayAcc;
    /**
     * 支付方式
     */
    private String payType;
    /**
     * 附件名称
     */
    private String annexName;
    /**
     * 还款计划
     */
    private String repaymentScheduleId;
    /**
     * 借款利息预计明细ID 用来查询还款日期标记。通过还款日期标记查询这个日期内的全部利息明细
     */
    private String provideInterestId;
    /**
     * 还款方式
     */
    private String repayway;
    /**
     * 打回原因
     */
    private String refuseWhy;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 跳过补录功能
     */
    private String acrossRepair;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 调拨单据号
     */
    private String reqBillNo;
    /**
     * 调拨审批状态
     */
    private Integer reqStatus;
    /**
     * 调拨指令状态
     */
    private String voucherStat;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 更新人名称
     */
    private String updateName;
    /**
     * 更新人登录名称
     */
    private String updateBy;
    /**
     * 更新日期
     */
    private Date updateDate;
    private String uploadId;
    private String bizStatus;
    /**
     * 线下线上还款标识
     */
    private String onlineSign;
    /**
     * 实付利息
     */
    private BigDecimal onlineRealityInterest;
    /**
     * 收款帐号ID
     */
    private String onlinePaymentAccId;
    /**
     * 收款帐号
     */
    private String onlinePaymentAcc;
    /**
     * 收款账户名称
     */
    private String onlineAccName;
    /**
     * 收款账户开户行名称
     */
    private String onlineBankName;
    /**
     * 金额
     */
    private BigDecimal onlineMoney;
    /**
     * 用途
     */
    private String onlinePurpose;
    /**
     * 在线还款记账 其他应付款帐号
     */
    private String onlineOtherAccCharge;
    /**
     * 在线还款状态
     */
    private Integer onlineStatus;
    /**
     * 记账标志
     **/
    private String tallySign;
    /**
     * 记账员代码
     **/
    private String tallyCode;
    /**
     * 是否提前还款 字典组：cms.repay.isAdvancedRepay 是 1 否 0
     */
    private String isAdvancedRepay;
    /**
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 还款单号
     */
    @Column(name = "BILL_CODE")
    public String getBillCode() {
        return this.billCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 还款单号
     */
    public void setBillCode(String billCode) {
        this.billCode = billCode;
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
     * 方法: 取得java.lang.String
     *
     * @return: java.lang.String 借款合同号
     */
    @Column(name = "CONTRACT_CODE")
    public String getContractCode() {
        return this.contractCode;
    }
    /**
     * 方法: 设置java.lang.String
     *
     * @param: java.lang.String
     * 借款合同号
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
     * 贷款发放单号
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
    public BigDecimal getRepayMoney() {
        return this.repayMoney;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 还款金额
     */
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    /**
     * 方法: 取得java.util.Date
     *
     * @return: java.util.Date 还款日期
     */
    @Column(name = "REPAY_DATE")
    public Date getRepayDate() {
        return this.repayDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 还款日期
     */
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    /**
     * 方法: 取得java.math.BigDecimal
     *
     * @return: java.math.BigDecimal 还款利息
     */
    @Column(name = "REPAY_ACCRUAL")
    public BigDecimal getRepayAccrual() {
        return this.repayAccrual;
    }
    /**
     * 方法: 设置java.math.BigDecimal
     *
     * @param: java.math.BigDecimal
     * 还款利息
     */
    public void setRepayAccrual(BigDecimal repayAccrual) {
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
     * 还款账号
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
     * 支付方式
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
     * 附件名称
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
     * 还款计划
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
     * 还款方式
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
     * 备注
     */
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "REFUSE_WHY")
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
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
    public Date getCreateDate() {
        return this.createDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 创建日期
     */
    public void setCreateDate(Date createDate) {
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
    public Date getUpdateDate() {
        return this.updateDate;
    }
    /**
     * 方法: 设置java.util.Date
     *
     * @param: java.util.Date
     * 更新日期
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "ACROSS_REPAIR")
    public String getAcrossRepair() {
        return acrossRepair;
    }
    public void setAcrossRepair(String acrossRepair) {
        this.acrossRepair = acrossRepair;
    }
    @Column(name = "REVERT_ACCRUAL")
    public BigDecimal getRevertAccrual() {
        return revertAccrual;
    }
    public void setRevertAccrual(BigDecimal revertAccrual) {
        this.revertAccrual = revertAccrual;
    }
    @Column(name = "PROVIDE_INTEREST_ID")
    public String getProvideInterestId() {
        return provideInterestId;
    }
    public void setProvideInterestId(String provideInterestId) {
        this.provideInterestId = provideInterestId;
    }
    @Column(name = "REPAY_MONEY_TYPE")
    public String getRepayMoneyType() {
        return repayMoneyType;
    }
    public void setRepayMoneyType(String repayMoneyType) {
        this.repayMoneyType = repayMoneyType;
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
    @Column(name = "ONLINE_SIGN")
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    @Column(name = "ONLINE_REALITY_INTEREST")
    public BigDecimal getOnlineRealityInterest() {
        return onlineRealityInterest;
    }
    public void setOnlineRealityInterest(BigDecimal onlineRealityInterest) {
        this.onlineRealityInterest = onlineRealityInterest;
    }
    @Column(name = "ONLINE_PAYMENT_ACC")
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    @Column(name = "ONLINE_MONEY")
    public BigDecimal getOnlineMoney() {
        return onlineMoney;
    }
    public void setOnlineMoney(BigDecimal onlineMoney) {
        this.onlineMoney = onlineMoney;
    }
    @Column(name = "ONLINE_PURPOSE")
    public String getOnlinePurpose() {
        return onlinePurpose;
    }
    public void setOnlinePurpose(String onlinePurpose) {
        this.onlinePurpose = onlinePurpose;
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
    @Column(name = "ONLINE_OTHER_ACC_CHARGE")
    public String getOnlineOtherAccCharge() {
        return onlineOtherAccCharge;
    }
    public void setOnlineOtherAccCharge(String onlineOtherAccCharge) {
        this.onlineOtherAccCharge = onlineOtherAccCharge;
    }
    @Column(name = "ONLINE_STATUS")
    public Integer getOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
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
    @Column(name = "IS_ADVANCED_REPAY")
    public String getIsAdvancedRepay() {
        return isAdvancedRepay;
    }
    public void setIsAdvancedRepay(String isAdvancedRepay) {
        this.isAdvancedRepay = isAdvancedRepay;
    }
    @Column(name = "REQ_BILL_NO")
    public String getReqBillNo() {
        return reqBillNo;
    }
    public void setReqBillNo(String reqBillNo) {
        this.reqBillNo = reqBillNo;
    }
    @Column(name = "REQ_STATUS")
    public Integer getReqStatus() {
        return reqStatus;
    }
    public void setReqStatus(Integer reqStatus) {
        this.reqStatus = reqStatus;
    }
    @Column(name = "VOUCHER_STAT")
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
}
