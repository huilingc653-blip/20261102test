package com.bytter.finance.domain.provide.vo;
import com.bytter.finance.domain.provide.entity.ProvideSyndicatedSend;
import com.bytter.core.annotation.*;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
public class LoanSendVO implements Serializable, IBusinessKey {
    private String id;
    /**
     * 发放单号
     */
    private String billCode;
    /**
     * 借款银行类别
     */
//    @BankType(inject = "bankTypeName")
    @BtCompositeSearch(fieldName = "t.id")
    private String bankType;
    private String bankTypeName;
    /**
     * 借款银行
     */
    private String bankCode;
    private String bankName;
    /**
     * 借款合同编号
     */
    private String contractCode;
    /**
     * 借款单号
     */
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String loanCode;
    /**
     * 单位代码
     */
    @BtCompositeSearch(fieldName = "info.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 借款币种
     */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "info.cur_id")
    private String curId;
    private String curName;
    /**
     * 计息财务季度
     */
    private String fiscalQuarter;
    /**
     * 计息日
     */
    private Integer expiryDate;
    /**
     * 预提日
     */
    private Integer accruedDailyDate;
    /**
     * 贷款发放单号
     **/
    @BtCompositeSearch(fieldName = "s.bill_code")
    private String sendCode;
    /**
     * 还款金额
     **/
    private BigDecimal repayMoney;
    /**
     * 还款账号
     **/
    private String repayAcc;
    /**
     * 还款利息
     **/
    private BigDecimal repayAccrual;
    /**
     * 还款方式
     **/
    private String repayway;
    /**
     * 合同期限
     **/
    private String contractTerm;
    /**
     * 借款金额
     **/
    private BigDecimal plMoney;
    /**
     * 借款银行账号
     **/
    private String plAccount;
    /**
     * 借款日期
     **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;
    /**
     * 合同总金额
     */
    private BigDecimal contractSumMoney;
    /**
     * 待发放金额
     */
    private BigDecimal waitRepayMoney;
    /**
     * 发放金额
     */
    @BtCompositeSearch(fieldName = "send.money")
    private BigDecimal money;
    /**
     * 发放日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "send.send_date")
    private Date sendDate;
    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "send.repay_date")
    private Date repayDate;
    /**
     * 借款账号
     */
    private String loanAcc;
    /**
     * 银行活期账户
     */
    private String blnctrAcc;
    /**
     * 借款币种
     */
    private String curCode;
    /**
     * 借款利率
     */
    private BigDecimal plRate;
    /**
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 展期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredDate;
    /**
     * 税率
     */
    private BigDecimal interestRate;
    /**
     * 状态
     */
    @Dict(dictGroup = "cms.status", inject = "statusName")
    private Integer status;
    private String statusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /**
     * 记账凭证号
     */
    private String voucherNo;
    private String tallySign;
    private String tallyCode;
    /**
     * 记账流水号
     */
    private String orderNo;
    /**
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /**
     * 发放方式
     */
    @Dict(dictGroup = "cms.loan.loanSendOutType", inject = "loanSendOutTypeName")
    private String loanSendOutType;
    private String loanSendOutTypeName;
    /**
     * 还款方式
     */
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    // 计提计息需求，还款方式名称
    private String repayTypeName1;
    /**
     * 浮动方式
     */
    @Dict(dictGroup = "Cms.FloatWay", inject = "floatWayName")
    private String floatWay;
    private String floatWayName;
    /**
     * 二级浮动方式
     */
    private String floatWayNext;
    /**
     * 点数或浮动比例
     */
    private BigDecimal pointsProportionText;
    /**
     * 点数或者比例
     */
    @Dict(dictGroup = "Cms.PointsProportion", inject = "pointsProportionSelectName")
    private String pointsProportionSelect;
    private String pointsProportionSelectName;
    /**
     * 利率调整生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rateEntriesEffectDate;
    /**
     * 计息方式
     */
    @Dict(dictGroup = "Admin.PredrawSign", inject = "interestWayName")
    private String interestWay;
    private String interestWayName;
    /**
     * 基准利率类型
     */
    private String referenceRateType;
    private String referenceRateTypeName;
    /**
     * 是否央行基准利率
     */
    private String isBankBaseRate;
    /**
     * 贷款周期
     */
    private String repayCycle;
    private String repayCycleName;
    /**
     * 利率复核生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ratetimeoftakeeffect;
    /**
     * 预计利息
     */
    private BigDecimal anticInterest;
    /**
     * 发放次数
     */
    private Integer sendTime;
    /**
     * 打回原因
     */
    private String refuseWhy;
    /**
     * 创建人名称
     */
    private String createName;
    /**
     * 创建人登录名称
     */
    private String createBy;
    /**
     * 创建日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    /**
     * 借款期限
     */
    private BigDecimal plTerm;
    /**
     * 期限单位
     */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;
    private String ternUnitName;
    /**
     * 资金预算
     */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    private String uploadId;
    /**
     * 还款计划Id
     */
    private String planId;
    private String planStatus;
    /**
     * 金额
     **/
    private BigDecimal onlineMoney;
    /**
     * 还款单号
     **/
    private String repayBillCode;
    /**
     * 借款合同编号
     **/
    private String sendContractCode;
    /**
     * 实付利息
     **/
    private BigDecimal onlineRealityInterest;
    /**
     * 发放到期日期
     **/
    private Date sendRepayDate;
    /**
     * 还款标志
     **/
    private String onlineSign;
    /**
     * 汇率
     */
    private BigDecimal[] exchangeRate;
    /**
     * 执行金额（折合本身币别）
     */
    private BigDecimal[] processCurMoney;
    /**
     * 录入创建人
     */
    private String infoCreateBy;
    /**
     * 录入修改人
     */
    private String infoUpdateBy;
    /**
     * 银行流水认领功能挂接标记，未认领=0,认领=1
     */
    private String bankFlowSign;
    /**
     * 展期上浮点数
     */
    private BigDecimal deferredPoints;
    /**
     * 逾期上浮比例
     */
    private BigDecimal overdueProportion;
    /** 银团明细信息 */
    private List<ProvideSyndicatedSend> syndicatedSendList;
    /**
     * 计提方式
     */
    private String accrualType;
    /**
     * 计提方式名
     */
    private String accrualTypeName;
    /**
         * 银行ID
     */
    private String bankId;


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

    private String  saveType;

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }

    public String getBankId() {
		return bankId;
	}
	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getLoanCode() {
        return loanCode;
    }
    public void setLoanCode(String loanCode) {
        this.loanCode = loanCode;
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getLoanSendOutTypeName() {
        return loanSendOutTypeName;
    }
    public void setLoanSendOutTypeName(String loanSendOutTypeName) {
        this.loanSendOutTypeName = loanSendOutTypeName;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeName() {
        return StringUtils.isNotEmpty(repayTypeName) ? repayTypeName : repayTypeName1;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public Date getRatetimeoftakeeffect() {
        return ratetimeoftakeeffect;
    }
    public void setRatetimeoftakeeffect(Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    public BigDecimal getAnticInterest() {
        return anticInterest;
    }
    public void setAnticInterest(BigDecimal anticInterest) {
        this.anticInterest = anticInterest;
    }
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
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
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
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
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public BigDecimal getPlTerm() {
        return plTerm;
    }
    public void setPlTerm(BigDecimal plTerm) {
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
    public BigDecimal getContractSumMoney() {
        return contractSumMoney;
    }
    public void setContractSumMoney(BigDecimal contractSumMoney) {
        this.contractSumMoney = contractSumMoney;
    }
    public BigDecimal getWaitRepayMoney() {
        return waitRepayMoney;
    }
    public void setWaitRepayMoney(BigDecimal waitRepayMoney) {
        this.waitRepayMoney = waitRepayMoney;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getFloatWayName() {
        return floatWayName;
    }
    public void setFloatWayName(String floatWayName) {
        this.floatWayName = floatWayName;
    }
    public String getInterestWayName() {
        return interestWayName;
    }
    public void setInterestWayName(String interestWayName) {
        this.interestWayName = interestWayName;
    }
    public String getPointsProportionSelectName() {
        return pointsProportionSelectName;
    }
    public void setPointsProportionSelectName(String pointsProportionSelectName) {
        this.pointsProportionSelectName = pointsProportionSelectName;
    }
    public String getReferenceRateTypeName() {
        return referenceRateTypeName;
    }
    public void setReferenceRateTypeName(String referenceRateTypeName) {
        this.referenceRateTypeName = referenceRateTypeName;
    }
    public String getRepayCycleName() {
        return repayCycleName;
    }
    public void setRepayCycleName(String repayCycleName) {
        this.repayCycleName = repayCycleName;
    }
    public String getBusinessKey() {
        return id;
    }
    public Integer getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Integer expiryDate) {
        this.expiryDate = expiryDate;
    }
    public Integer getAccruedDailyDate() {
        return accruedDailyDate;
    }
    public void setAccruedDailyDate(Integer accruedDailyDate) {
        this.accruedDailyDate = accruedDailyDate;
    }
    public String getFiscalQuarter() {
        return fiscalQuarter;
    }
    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
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
    public String getBankType() {
        return bankType;
    }
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getPlanStatus() {
        return planStatus;
    }
    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }
    public String getBlnctrAcc() {
        return blnctrAcc;
    }
    public void setBlnctrAcc(String blnctrAcc) {
        this.blnctrAcc = blnctrAcc;
    }
    public Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public String getRepayAcc() {
        return repayAcc;
    }
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    public String getContractTerm() {
        return contractTerm;
    }
    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
    }
    public BigDecimal getPlMoney() {
        return plMoney;
    }
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }
    public String getPlAccount() {
        return plAccount;
    }
    public void setPlAccount(String plAccount) {
        this.plAccount = plAccount;
    }
    public Date getPlDate() {
        return plDate;
    }
    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }
    public BigDecimal getOnlineMoney() {
        return onlineMoney;
    }
    public void setOnlineMoney(BigDecimal onlineMoney) {
        this.onlineMoney = onlineMoney;
    }
    public String getRepayBillCode() {
        return repayBillCode;
    }
    public void setRepayBillCode(String repayBillCode) {
        this.repayBillCode = repayBillCode;
    }
    public String getSendContractCode() {
        return sendContractCode;
    }
    public void setSendContractCode(String sendContractCode) {
        this.sendContractCode = sendContractCode;
    }
    public BigDecimal getOnlineRealityInterest() {
        return onlineRealityInterest;
    }
    public void setOnlineRealityInterest(BigDecimal onlineRealityInterest) {
        this.onlineRealityInterest = onlineRealityInterest;
    }
    public String getOnlineSign() {
        return onlineSign;
    }
    public void setOnlineSign(String onlineSign) {
        this.onlineSign = onlineSign;
    }
    public Date getSendRepayDate() {
        return sendRepayDate;
    }
    public void setSendRepayDate(Date sendRepayDate) {
        this.sendRepayDate = sendRepayDate;
    }
    public String getTallySign() {
        return tallySign;
    }
    public void setTallySign(String tallySign) {
        this.tallySign = tallySign;
    }
    public String getTallyCode() {
        return tallyCode;
    }
    public void setTallyCode(String tallyCode) {
        this.tallyCode = tallyCode;
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
    public String getInfoCreateBy() {
        return infoCreateBy;
    }
    public void setInfoCreateBy(String infoCreateBy) {
        this.infoCreateBy = infoCreateBy;
    }
    public String getInfoUpdateBy() {
        return infoUpdateBy;
    }
    public void setInfoUpdateBy(String infoUpdateBy) {
        this.infoUpdateBy = infoUpdateBy;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankFlowSign() {
        return bankFlowSign;
    }
    public void setBankFlowSign(String bankFlowSign) {
        this.bankFlowSign = bankFlowSign;
    }
    public String getIsBankBaseRate() {
        return isBankBaseRate;
    }
    public void setIsBankBaseRate(String isBankBaseRate) {
        this.isBankBaseRate = isBankBaseRate;
    }
    public BigDecimal getDeferredPoints() {
        return deferredPoints;
    }
    public void setDeferredPoints(BigDecimal deferredPoints) {
        this.deferredPoints = deferredPoints;
    }
    public BigDecimal getOverdueProportion() {
        return overdueProportion;
    }
    public void setOverdueProportion(BigDecimal overdueProportion) {
        this.overdueProportion = overdueProportion;
    }
    public List<ProvideSyndicatedSend> getSyndicatedSendList() {
        return syndicatedSendList;
    }
    public void setSyndicatedSendList(List<ProvideSyndicatedSend> syndicatedSendList) {
        this.syndicatedSendList = syndicatedSendList;
    }
    public String getRepayTypeName1() {
        return repayTypeName1;
    }
    public void setRepayTypeName1(String repayTypeName1) {
        this.repayTypeName1 = repayTypeName1;
    }
    public String getAccrualType() {
        return accrualType;
    }
    public void setAccrualType(String accrualType) {
        this.accrualType = accrualType;
    }
    public String getAccrualTypeName() {
        return accrualTypeName;
    }
    public void setAccrualTypeName(String accrualTypeName) {
        this.accrualTypeName = accrualTypeName;
    }

    public String getOnlinePaymentAcc() {
        return onlinePaymentAcc;
    }

    public void setOnlinePaymentAcc(String onlinePaymentAcc) {
        this.onlinePaymentAcc = onlinePaymentAcc;
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
}
