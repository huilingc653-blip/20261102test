package com.bytter.finance.domain.provide.vo;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedDtl;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
public class LoanRepayVO implements Serializable, IBusinessKey {
    private String id;
    private String repayId;
    /**
     * 还款单号
     */
    @BtCompositeSearch(fieldName = "repay.bill_code")
    private String billCode;
    /**
     * 发放单号
     */
    @BtCompositeSearch(fieldName = "send.bill_code")
    private String sendCode;
    /**
     * 借款单号
     */
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String infoBillCode;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "info.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "info.corp_id")
    private String corpName;
    /**
     * 借款合同号
     */
    @BtCompositeSearch(fieldName = "info.contract_no")
    private String contractCode;
    /**
     * 借款银行
     */
    @BtCompositeSearch(fieldName = "info.bank_name")
    private String bankName;
    /**
     * 借款币种
     */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 还款方式
     */
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    // 新计提，计息需求 还款方式
    private String repayTypeName1;
    // 计提方式
    private String accrualTypeName;
    /**
     * 借款种类
     */
    private String plClass;
    private String plClassName;
    /**
     * 担保方式
     */
    // @Dict(dictGroup = "Cms.PlType.K", inject = "plTypeName")
    private String plType;
    private String plTypeName;
    /**
     * 借款期限
     */
    @BtCompositeSearch(fieldName = "send.pl_term")
    private Integer plTerm;
    /**
     * 期限单位
     */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;
    private String ternUnitName;
    /**
     * 发放金额
     */
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal sendMoney;
    /**
     * 借款余额
     */
    private BigDecimal plBalance;
    /**
     * 发放日期
     */
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sendData;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sendDueData;
    /**
     * 还款金额
     */
    @BtCompositeSearch(fieldName = "repay.repay_money")
    private BigDecimal repayMoney;
    /**
     * 还款金额类型
     */
    private String repayMoneyType;
    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "repay.repay_date")
    private java.util.Date repayDate;
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
     * 借款利息预计明细ID
     */
    private String provideInterestId;
    /**
     * 还款方式
     */
    private String repayway;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 打回原因
     */
    private String refuseWhy;
    /**
     * 跳过补录功能
     */
    private String acrossRepair;
    /**
     * 状态
     */
    @Dict(dictGroup = "cms.status", inject = "statusName")
    @BtCompositeSearch(fieldName = "repay.status")
    private Integer status;
    private String statusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /**
     * 创建人名称
     */
    @BtCompositeSearch(fieldName = "info.create_name")
    private String createName;
    /**
     * 还款申请人
     */
    @BtCompositeSearch(fieldName = "repay.create_name")
    private String repayCreateName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "info.create_date")
    private java.util.Date createDate;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    /**
     * 展期开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date deferredStartDate;
    /**
     * 展期到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "send.deferred_date")
    private java.util.Date deferredRepayDate;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    private String uploadId;
    /**
     * 资金预算
     */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    /** 线上还款 */
    /**
     * 线下线上还款标识
     */
    @Dict(dictGroup = "cms.leasepay.online.sign", inject = "onlineSignName")
    private String onlineSign;
    private String onlineSignName;
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
     * 头寸调拨单据号码
     */
    private String billNo;
    /**
     * 头寸调拨单据状态
     */
    private Integer reqStatus;
    /**
     * 临时使用的状态，为满足信贷撤回，还款追踪显示暂存，头寸显示删除做显示准备
     */
    @Dict(dictGroup = "netbank.statusAll", inject = "reqStatusName")
    private Integer reqInterimStatus;
    private String reqStatusName;
    /**
     * 交易状态
     */
    @Dict(dictGroup = "bisExc.voucherStat", inject = "voucherStatName")
    private String voucherStat;
    private String voucherStatName;
    /**
     * 单位类型 是否网点
     */
    private Integer corpType;
    /**
     * 记账员
     **/
    private String tallyCode;
    /**
     * 记账标志
     **/
    private String tallySign;
    /**
     * 借款还款申请/修改操作标志，为“1”是为修改状态，为“0”是为申请状态
     **/
    private String tempOrSubmitSign;
    /**
     * 汇率
     */
    private BigDecimal[] exchangeRate;
    /**
     * 执行金额（折合本身币别）
     */
    private BigDecimal[] processCurMoney;
    /**
     * 调用合同id
     */
    private String conId;
    /**
     * 生成合同类型
     */
    private String conTypeId;
    /**
     * 是否提前还款 字典组：cms.repay.isAdvancedRepay 是 1 否 0
     */
    @BtCompositeSearch(fieldName = "repay.is_advanced_repay")
    private String isAdvancedRepay;

    /**
     * 选择流水id
     */
    private String tickDtlId;

    /**
     * 银行流水认领功能挂接标记，未认领=0,认领=1
     */
    private String bankFlowSign;

    /** 银团明细信息 */
    private List<ProvideSyndicatedDtl> syndicatedDtlList;
    /** 批量线下提交明细集合 */
    private List<LoanRepayBatchVO> loanRepayBatchList = new ArrayList<LoanRepayBatchVO>();

    public String getRepayId() {
        return repayId;
    }
    public void setRepayId(String repayId) {
        this.repayId = repayId;
    }
    public String getTempOrSubmitSign() {
        return tempOrSubmitSign;
    }
    public void setTempOrSubmitSign(String tempOrSubmitSign) {
        this.tempOrSubmitSign = tempOrSubmitSign;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return StringUtils.isNotEmpty(repayTypeName) ? repayTypeName : this.repayTypeName1;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getPlClass() {
        return plClass;
    }
    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }
    public String getPlClassName() {
        return plClassName;
    }
    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getPlTypeName() {
        return plTypeName;
    }
    public void setPlTypeName(String plTypeName) {
        this.plTypeName = plTypeName;
    }
    public Integer getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(Integer plTerm) {
        this.plTerm = plTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getTernUnitName() {
        return ternUnitName;
    }
    public void setTernUnitName(String ternUnitName) {
        this.ternUnitName = ternUnitName;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getRepayAcc() {
        return repayAcc;
    }
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getCreateName() {
        return createName;
    }
    public void setCreateName(String createName) {
        this.createName = createName;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateName() {
        return updateName;
    }
    public void setUpdateName(String updateName) {
        this.updateName = updateName;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getPlBalance() {
        return plBalance;
    }
    public void setPlBalance(BigDecimal plBalance) {
        this.plBalance = plBalance;
    }
    public java.util.Date getSendData() {
        return sendData;
    }
    public void setSendData(java.util.Date sendData) {
        this.sendData = sendData;
    }
    public java.util.Date getSendDueData() {
        return sendDueData;
    }
    public void setSendDueData(java.util.Date sendDueData) {
        this.sendDueData = sendDueData;
    }
    public String getAcrossRepair() {
        return acrossRepair;
    }
    public void setAcrossRepair(String acrossRepair) {
        this.acrossRepair = acrossRepair;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public BigDecimal getRevertAccrual() {
        return revertAccrual;
    }
    public void setRevertAccrual(BigDecimal revertAccrual) {
        this.revertAccrual = revertAccrual;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getProvideInterestId() {
        return provideInterestId;
    }
    public void setProvideInterestId(String provideInterestId) {
        this.provideInterestId = provideInterestId;
    }
    public String getRepayMoneyType() {
        return repayMoneyType;
    }
    public void setRepayMoneyType(String repayMoneyType) {
        this.repayMoneyType = repayMoneyType;
    }
    public String[] getItemCode() {
        return itemCode;
    }
    public void setItemCode(String[] itemCode) {
        this.itemCode = itemCode;
    }
    public String[] getItemName() {
        return itemName;
    }
    public void setItemName(String[] itemName) {
        this.itemName = itemName;
    }
    public BigDecimal[] getProcessMoney() {
        return processMoney;
    }
    public void setProcessMoney(BigDecimal[] processMoney) {
        this.processMoney = processMoney;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getBizStatusName() {
        return bizStatusName;
    }
    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }
    public java.util.Date getDeferredStartDate() {
        return deferredStartDate;
    }
    public void setDeferredStartDate(java.util.Date deferredStartDate) {
        this.deferredStartDate = deferredStartDate;
    }
    public java.util.Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(java.util.Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    public BigDecimal getOnlineRealityInterest() {
        return onlineRealityInterest;
    }
    public void setOnlineRealityInterest(BigDecimal onlineRealityInterest) {
        this.onlineRealityInterest = onlineRealityInterest;
    }
    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }
    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
    }
    public BigDecimal getOnlineMoney() {
        return onlineMoney;
    }
    public void setOnlineMoney(BigDecimal onlineMoney) {
        this.onlineMoney = onlineMoney;
    }
    public String getOnlinePurpose() {
        return onlinePurpose;
    }
    public void setOnlinePurpose(String onlinePurpose) {
        this.onlinePurpose = onlinePurpose;
    }
    public String getOnlineSignName() {
        return onlineSignName;
    }
    public void setOnlineSignName(String onlineSignName) {
        this.onlineSignName = onlineSignName;
    }
    public String getOnlineAccName() {
        return onlineAccName;
    }
    public void setOnlineAccName(String onlineAccName) {
        this.onlineAccName = onlineAccName;
    }
    public String getOnlineBankName() {
        return onlineBankName;
    }
    public void setOnlineBankName(String onlineBankName) {
        this.onlineBankName = onlineBankName;
    }
    public String getOnlinePaymentAccId() {
        return onlinePaymentAccId;
    }
    public void setOnlinePaymentAccId(String onlinePaymentAccId) {
        this.onlinePaymentAccId = onlinePaymentAccId;
    }
    public String getOnlineOtherAccCharge() {
        return onlineOtherAccCharge;
    }
    public void setOnlineOtherAccCharge(String onlineOtherAccCharge) {
        this.onlineOtherAccCharge = onlineOtherAccCharge;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Integer getReqStatus() {
        return reqStatus;
    }
    public void setReqStatus(Integer reqStatus) {
        this.reqStatus = reqStatus;
    }
    public String getReqStatusName() {
        return reqStatusName;
    }
    public void setReqStatusName(String reqStatusName) {
        this.reqStatusName = reqStatusName;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public String getVoucherStatName() {
        return voucherStatName;
    }
    public void setVoucherStatName(String voucherStatName) {
        this.voucherStatName = voucherStatName;
    }
    public Integer getOnlineStatus() {
        return onlineStatus;
    }
    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
    public Integer getReqInterimStatus() {
        return reqInterimStatus;
    }
    public void setReqInterimStatus(Integer reqInterimStatus) {
        this.reqInterimStatus = reqInterimStatus;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    public Integer getCorpType() {
        return corpType;
    }
    public void setCorpType(Integer corpType) {
        this.corpType = corpType;
    }
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
    }
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    public BigDecimal[] getExchangeRate() {
        return exchangeRate;
    }
    public void setExchangeRate(BigDecimal[] exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
    public BigDecimal[] getProcessCurMoney() {
        return processCurMoney;
    }
    public void setProcessCurMoney(BigDecimal[] processCurMoney) {
        this.processCurMoney = processCurMoney;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getIsAdvancedRepay() {
        return isAdvancedRepay;
    }
    public void setIsAdvancedRepay(String isAdvancedRepay) {
        this.isAdvancedRepay = isAdvancedRepay;
    }

    public String getTickDtlId() {
        return tickDtlId;
    }

    public void setTickDtlId(String tickDtlId) {
        this.tickDtlId = tickDtlId;
    }

    public String getBankFlowSign() {
        return bankFlowSign;
    }

    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }

    public List<LoanRepayBatchVO> getLoanRepayBatchList() {
        return loanRepayBatchList;
    }
    public void setLoanRepayBatchList(List<LoanRepayBatchVO> loanRepayBatchList) {
        this.loanRepayBatchList = loanRepayBatchList;
    }
    public String getRepayCreateName() {
        return repayCreateName;
    }
    public void setRepayCreateName(String repayCreateName) {
        this.repayCreateName = repayCreateName;
    }
    public List<ProvideSyndicatedDtl> getSyndicatedDtlList() {
        return syndicatedDtlList;
    }
    public void setSyndicatedDtlList(List<ProvideSyndicatedDtl> syndicatedDtlList) {
        this.syndicatedDtlList = syndicatedDtlList;
    }
    public String getRepayTypeName1() {
        return repayTypeName1;
    }
    public void setRepayTypeName1(String repayTypeName1) {
        this.repayTypeName1 = repayTypeName1;
    }
    public String getAccrualTypeName() {
        return accrualTypeName;
    }
    public void setAccrualTypeName(String accrualTypeName) {
        this.accrualTypeName = accrualTypeName;
    }
}
