package com.bytter.finance.dto;
import com.bytter.core.annotation.Corp;
import java.io.Serializable;
public class CmsPrecativeLoanInfoDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /** 委托人单位代码 */
    @Corp(inject = "precativeName")
    private String precativeId;
    private String precativeName;
    /** 受托人单位代码 */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 是否银行办理 */
    private String isBank;
    /** 受托行 */
    private String bankCode;
    /** 委托贷款合同号 */
    private String contractCode;
    /** 委托贷款票据号 */
    private String billCode;
    /** 贷款对象 */
    @Corp(inject = "loanOppName")
    private String loanOpp;
    private String loanOppName;
    /** 贷款币种 */
    private String curId;
    /** 开始日期 */
    private java.util.Date startDate;
    /** 结束日期 */
    private java.util.Date endDate;
    /** 贷款期限 */
    private Integer term;
    /** 期限单位 */
    private String ternUnit;
    /** 贷款利率 */
    private java.math.BigDecimal rate;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 贷款金额 */
    private java.math.BigDecimal money;
    /** 手续费率 */
    private java.math.BigDecimal poundageRate;
    /** 手续费 */
    private java.math.BigDecimal poundage;
    /** 担保方式 */
    private String plType;
    /** 担保说明 */
    private String suretyExplain;
    /** 担保合同编码 */
    private String guaSuretyCode;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
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
    /** 发放方式 */
    private String loanSendOutType;
    /** 是否跳过发放 */
    private String skipLoanSendOut;
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
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
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
    public java.math.BigDecimal getRate() {
        return rate;
    }
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    public java.math.BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(java.math.BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getPoundageRate() {
        return poundageRate;
    }
    public void setPoundageRate(java.math.BigDecimal poundageRate) {
        this.poundageRate = poundageRate;
    }
    public java.math.BigDecimal getPoundage() {
        return poundage;
    }
    public void setPoundage(java.math.BigDecimal poundage) {
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
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
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
    public java.math.BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(java.math.BigDecimal pointsProportionText) {
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
}
