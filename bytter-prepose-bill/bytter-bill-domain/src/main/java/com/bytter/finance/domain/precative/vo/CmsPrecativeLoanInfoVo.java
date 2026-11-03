package com.bytter.finance.domain.precative.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsPrecativeLoanInfoVo implements Serializable, IBusinessKey {
    private String id;
    /** 委托人单位代码 */
//    @Corp(inject = "precativeName")
    @BtCompositeSearch(fieldName = "a.precative_Id")
    private String precativeId;
    /** 委托人单位名称 */
    @BtCompositeSearch(fieldName = "a.precative_Id")
    private String precativeName;
    /** 受托人单位代码 */
//    @Corp(inject = "corpName")
    private String corpId;
    @BtCompositeSearch(fieldName = "a.corp_id")
    private String corpName;
    /** 还款利息 **/
    @BtCompositeSearch(fieldName = "r.repay_accrual")
    private BigDecimal repayAccrual;
    /** 是否银行办理 */
    @Dict(dictGroup = "yn_state", inject = "isBankStatus")
    private String isBank;
    private String isBankStatus;
    /** 受托行 */
    private String bankCode;
    private String bankTypeId;
    private String bankName;
    /** 委托贷款合同号 */
    private String contractCode;
    /** 委托贷款票据号 */
    @BtCompositeSearch(fieldName = "a.bill_code")
    private String billCode;
    /** 贷款对象 */
//    @Corp(inject = "loanOppName")
    @BtCompositeSearch(fieldName = "a.loan_Opp")
    private String loanOpp;
    @BtCompositeSearch(fieldName = "a.loan_Opp")
    private String loanOppName;
    /** 业务状态 */
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    @BtCompositeSearch(fieldName = "a.biz_Status")
    private String bizStatus;
    @BtCompositeSearch(fieldName = "a.biz_Status")
    private String bizStatusName;
    /** 贷款币种 */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curId;
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curName;
    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.start_Date")
    private java.util.Date startDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.end_Date")
    private java.util.Date endDate;
    /** 贷款期限 */
    @BtCompositeSearch(fieldName = "a.term")
    private Integer term;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitStatus")
    private String ternUnit;
    private String ternUnitStatus;
    /** 贷款利率 */
    private BigDecimal rate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 贷款金额 */
    @BtCompositeSearch(fieldName = "a.money")
    private BigDecimal money;
    /** 手续费率 */
    private BigDecimal poundageRate;
    /** 手续费 */
    private BigDecimal poundage;
    /** 担保方式 */
    @Dict(dictGroup = "Cms.PlType.C", inject = "plName")
    @BtCompositeSearch(fieldName = "a.pl_Type")
    private String plType;
    @BtCompositeSearch(fieldName = "a.pl_Type")
    private String plName;
    /** 担保说明 */
    private String suretyExplain;
    /** 担保合同编码 */
    private String guaSuretyCode;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    @BtCompositeSearch(fieldName = "a.status")
    private Integer status;
    /** 贷款用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 打回原因 */
    private String refuseWhy;
    /** 上传文件信息 */
    private String annexName;
    /** 还款方式 */
    private String repayType;
    /** 还款方式名称 */
    private String repayTypeName;
    /** 发放方式 */
    @Dict(dictGroup = "cms.loan.loanSendOutType", inject = "sendOutType")
    @BtCompositeSearch(fieldName = "a.loan_Send_Out_Type")
    private String loanSendOutType;
    private String sendOutType;
    /** 是否跳过发放 */
    @BtCompositeSearch(fieldName = "a.skip_Loan_Send_Out")
    private String skipLoanSendOut;
    /** 浮动方式 */
    @BtCompositeSearch(fieldName = "a.float_Way")
    private String floatWay;
    /** 浮动二级方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    private BigDecimal pointsProportionText;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 利率调整生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date rateEntriesEffectDate;
    /** 计息方式 */
    private String interestWay;
    /** 基准利率类型 */
    private String referenceRateType;
    private String baseTypeName;
    /** 贷款周期 */
    private String repayCycle;
    private String periodName;
    /** 利率复核生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date ratetimeoftakeeffect;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    /** 还款计划Id */
    private String planId;
    /** 发放次数 */
    private Integer sendTime;
    /** 提交标志 **/
    private String saveType;
    /** 核对执行计划 **/
    private BigDecimal total;
    // 资金计划参数
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    /** 汇率 */
    private BigDecimal[] exchangeRate;
    /** 执行金额（折合本身币别） */
    private BigDecimal[] processCurMoney;
    /** 对接的合同类型id */
    private String conTypeId;
    /** 对接的合同id */
    private String conId;
    private String createBy;

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPrecativeId() {
        return precativeId;
    }
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getIsBank() {
        return isBank;
    }
    public void setIsBank(String isBank) {
        this.isBank = isBank;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public BigDecimal getPoundageRate() {
        return poundageRate;
    }
    public void setPoundageRate(BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }
    public BigDecimal getPoundage() {
        return poundage;
    }
    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getSuretyExplain() {
        return suretyExplain;
    }
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    public String getGuaSuretyCode() {
        return guaSuretyCode;
    }
    public void setGuaSuretyCode(String guaSuretyCode) {
        this.guaSuretyCode = guaSuretyCode;
    }
    public String getRepairOp() {
        return repairOp;
    }
    public void setRepairOp(String repairOp) {
        this.repairOp = repairOp;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getSkipLoanSendOut() {
        return skipLoanSendOut;
    }
    public void setSkipLoanSendOut(String skipLoanSendOut) {
        this.skipLoanSendOut = skipLoanSendOut;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
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
    public java.util.Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(java.util.Date rateEntriesEffectDate) {
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
    public java.util.Date getRatetimeoftakeeffect() {
        return ratetimeoftakeeffect;
    }
    public void setRatetimeoftakeeffect(java.util.Date ratetimeoftakeeffect) {
        this.ratetimeoftakeeffect = ratetimeoftakeeffect;
    }
    public java.util.Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(java.util.Date createDate) {
        this.createDate = createDate;
    }
    public java.util.Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(java.util.Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }
    public String getIsBankStatus() {
        return isBankStatus;
    }
    public void setIsBankStatus(String isBankStatus) {
        this.isBankStatus = isBankStatus;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getTernUnitStatus() {
        return ternUnitStatus;
    }
    public void setTernUnitStatus(String ternUnitStatus) {
        this.ternUnitStatus = ternUnitStatus;
    }
    public String getPlName() {
        return plName;
    }
    public void setPlName(String plName) {
        this.plName = plName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    public String getSendOutType() {
        return sendOutType;
    }
    public void setSendOutType(String sendOutType) {
        this.sendOutType = sendOutType;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public String getBaseTypeName() {
        return baseTypeName;
    }
    public void setBaseTypeName(String baseTypeName) {
        this.baseTypeName = baseTypeName;
    }
    public String getPeriodName() {
        return periodName;
    }
    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getSaveType() {
        return saveType;
    }
    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
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
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
