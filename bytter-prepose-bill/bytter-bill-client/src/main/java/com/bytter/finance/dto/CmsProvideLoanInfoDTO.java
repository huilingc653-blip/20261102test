package com.bytter.finance.dto;
import com.bytter.core.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsProvideLoanInfoDTO implements Serializable {
    private String id;
    /** 结算中心代码 */
    private String netId;
    /** 单位代码 */
    @BtCompositeSearch(fieldName = "info.corp_id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 借款流水号 */
    @BtCompositeSearch(fieldName = "info.bill_code")
    private String billCode;
    /** 借款合同号 */
    private String contractNo;
    /** 合同号期限 */
    @Dict(dictGroup = "Provide.ContractTerm", inject = "contractTermName")
    private String contractTerm;
    private String contractTermName;
    /** 借款金额 */
    private BigDecimal plMoney;
    /** 借款期限 */
    private Integer plTerm;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;
    private String ternUnitName;
    /** 借款币种 */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 借款银行类别 */
    @BankType(inject = "bankTypeName")
    private String bankType;
    private String bankTypeName;
    /** 借款银行 */
    private String bankId;
    private String bankName;
    /** 借款银行账号 */
    private String plAccount;
    /** 借款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /** 计息财务季度 */
    private String fiscalQuarter;
    /** 计息日 */
    private Integer expiryDate;
    /** 预提日 */
    private Integer accruedDailyDate;
    /** 还款方式 */
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    /** 还款方式说明 */
    private String repayTypeDesc;
    /** 借款种类 */
    private String plClass;
    private String plClassName;
    /** 贷款类型 */
    private String plGenre;
    /** 担保主业务类型 */
    private String mainPlType;
    /** 担保方式 */
    @Dict(dictGroup = "Cms.PlType.K", inject = "plTypeName")
    private String plType;
    private String plTypeName;
    /** 保证金比例 */
    private BigDecimal bailmoneyRate;
    /** 保证金 */
    private BigDecimal bailmoney;
    private String bailmoneyContract;
    /** 担保说明 */
    private String suretyExplain;
    /** 借款利率 */
    private BigDecimal plRate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    @Dict(dictGroup = "cms.status", inject = "statusName")
    private Integer status;
    private String statusName;
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /** 借款用途 */
    private String plPurpose;
    /** 备注 */
    private String rmk;
    /** 打回原因 */
    private String refuseWhy;
    /** 是否挂授信 */
    private String isHangCredit;
    /** 授信模式 */
    private String ratmodel;
    /** 额度编号 */
    private String rationCode;
    /** 占用授信代码 */
    private String ratItemCode;
    /** 保存当前币种转换汇率 */
    private BigDecimal exchangerate;
    /** 是否折合成本位币 */
    private String isRmbRat;
    /** 使用人 */
    private String users;
    /** 信贷合同号 */
    private String creditContractNo;
    /** 内部合同号 */
    private String insideContractNo;
    /** 项目名称 */
    private String projectName;
    /** 付息方式 */
    @Dict(dictGroup = "provide.payInterest", inject = "cplInterestPaymentName")
    private String cplInterestPayment;
    private String cplInterestPaymentName;
    /** 中间业务费 */
    private BigDecimal interbusexpenses;
    /** 发放方式 */
    @Dict(dictGroup = "cms.loan.loanSendOutType", inject = "loanSendOutTypeName")
    private String loanSendOutType;
    private String loanSendOutTypeName;
    /** 跳过借款发放功能 */
    @Dict(dictGroup = "sf_yn", inject = "acrossSendName")
    private String acrossSend;
    private String acrossSendName;
    /** 浮动方式 */
    @Dict(dictGroup = "Cms.FloatWay", inject = "floatWayName")
    private String floatWay;
    private String floatWayName;
    /** 二级浮动方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    private BigDecimal pointsProportionText;
    /** 点数或者比例 */
    @Dict(dictGroup = "Cms.PointsProportion", inject = "pointsProportionSelectName")
    private String pointsProportionSelect;
    private String pointsProportionSelectName;
    /** 利率调整生效日期 */
    private Date rateEntriesEffectDate;
    @Dict(dictGroup = "Admin.PredrawSign", inject = "interestWayName")
    private String interestWay;
    private String interestWayName;
    /** 基准利率类型 */
    private String referenceRateType;
    private String referenceRateTypeName;
    /** 贷款周期 */
    private String repayCycle;
    private String repayCycleName;
    /** 基准利率 */
    private BigDecimal benchmarkRate;
    /** 利率复核生效日期 */
    private Date ratetimeoftakeeffect;
    /** 预计利息 */
    private BigDecimal anticInterest;
    /** 顾问费利率 */
    private BigDecimal consultantFeeRate;
    /** 顾问费 */
    private BigDecimal consultantFee;
    /** 第三方收益利率 */
    private BigDecimal thirdPartyProfitRate;
    /** 第三方收益 */
    private BigDecimal thirdPartyProfit;
    /** 其他利率 */
    private BigDecimal otherFeeRate;
    /** 其他费用 */
    private BigDecimal otherFee;
    /** 作废单号 */
    private String bolishCode;
    /** 利息测算基数 */
    private String rateBase;
    /** 是否勾选顾问费复选框 */
    private String consultantCb;
    /** 是否勾选第三方收益复选框 */
    private String thirdPartyCb;
    /** 是否勾其他利率复选框 */
    private String otherFeeCb;
    /** 综合成本年化利率 */
    private BigDecimal compositeFeeRate;
    /** 综合成本 */
    private BigDecimal compositeFee;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updateDate;
    private String uploadId;
    /** 资金预算 */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    /** 还款计划Id */
    private String planId;
    private String planStatus;
    /**
     * 是否挂接担保
     */
    private String isHangGuarantee;
    /**
     * 担保备注
     */
    private String guaranteeRmk;
    /**
     * 担保方式
     */
    private String guaWay;
    /**
     * 担保合同Id
     */
    private String guaContract;
    /**
     * 合同类型
     */
    private String conType;
    /** 调用合同id */
    private String conId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
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
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getContractNo() {
        return contractNo;
    }
    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
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
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayTypeDesc() {
        return repayTypeDesc;
    }
    public void setRepayTypeDesc(String repayTypeDesc) {
        this.repayTypeDesc = repayTypeDesc;
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
    public String getPlGenre() {
        return plGenre;
    }
    public void setPlGenre(String plGenre) {
        this.plGenre = plGenre;
    }
    public String getMainPlType() {
        return mainPlType;
    }
    public void setMainPlType(String mainPlType) {
        this.mainPlType = mainPlType;
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
    public BigDecimal getBailmoneyRate() {
        return bailmoneyRate;
    }
    public void setBailmoneyRate(BigDecimal bailmoneyRate) {
        this.bailmoneyRate = bailmoneyRate;
    }
    public BigDecimal getBailmoney() {
        return bailmoney;
    }
    public void setBailmoney(BigDecimal bailmoney) {
        this.bailmoney = bailmoney;
    }
    public String getSuretyExplain() {
        return suretyExplain;
    }
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    public BigDecimal getPlRate() {
        return plRate;
    }
    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
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
    public String getPlPurpose() {
        return plPurpose;
    }
    public void setPlPurpose(String plPurpose) {
        this.plPurpose = plPurpose;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    public String getRatmodel() {
        return ratmodel;
    }
    public void setRatmodel(String ratmodel) {
        this.ratmodel = ratmodel;
    }
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    public BigDecimal getExchangerate() {
        return exchangerate;
    }
    public void setExchangerate(BigDecimal exchangerate) {
        this.exchangerate = exchangerate;
    }
    public String getIsRmbRat() {
        return isRmbRat;
    }
    public void setIsRmbRat(String isRmbRat) {
        this.isRmbRat = isRmbRat;
    }
    public String getUsers() {
        return users;
    }
    public void setUsers(String users) {
        this.users = users;
    }
    public String getCreditContractNo() {
        return creditContractNo;
    }
    public void setCreditContractNo(String creditContractNo) {
        this.creditContractNo = creditContractNo;
    }
    public String getInsideContractNo() {
        return insideContractNo;
    }
    public void setInsideContractNo(String insideContractNo) {
        this.insideContractNo = insideContractNo;
    }
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getCplInterestPayment() {
        return cplInterestPayment;
    }
    public void setCplInterestPayment(String cplInterestPayment) {
        this.cplInterestPayment = cplInterestPayment;
    }
    public BigDecimal getInterbusexpenses() {
        return interbusexpenses;
    }
    public void setInterbusexpenses(BigDecimal interbusexpenses) {
        this.interbusexpenses = interbusexpenses;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    public String getFloatWayName() {
        return floatWayName;
    }
    public void setFloatWayName(String floatWayName) {
        this.floatWayName = floatWayName;
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
    public BigDecimal getConsultantFeeRate() {
        return consultantFeeRate;
    }
    public void setConsultantFeeRate(BigDecimal consultantFeeRate) {
        this.consultantFeeRate = consultantFeeRate;
    }
    public BigDecimal getConsultantFee() {
        return consultantFee;
    }
    public void setConsultantFee(BigDecimal consultantFee) {
        this.consultantFee = consultantFee;
    }
    public BigDecimal getThirdPartyProfitRate() {
        return thirdPartyProfitRate;
    }
    public void setThirdPartyProfitRate(BigDecimal thirdPartyProfitRate) {
        this.thirdPartyProfitRate = thirdPartyProfitRate;
    }
    public BigDecimal getThirdPartyProfit() {
        return thirdPartyProfit;
    }
    public void setThirdPartyProfit(BigDecimal thirdPartyProfit) {
        this.thirdPartyProfit = thirdPartyProfit;
    }
    public BigDecimal getOtherFeeRate() {
        return otherFeeRate;
    }
    public void setOtherFeeRate(BigDecimal otherFeeRate) {
        this.otherFeeRate = otherFeeRate;
    }
    public BigDecimal getOtherFee() {
        return otherFee;
    }
    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }
    public String getBolishCode() {
        return bolishCode;
    }
    public void setBolishCode(String bolishCode) {
        this.bolishCode = bolishCode;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public String getConsultantCb() {
        return consultantCb;
    }
    public void setConsultantCb(String consultantCb) {
        this.consultantCb = consultantCb;
    }
    public String getThirdPartyCb() {
        return thirdPartyCb;
    }
    public void setThirdPartyCb(String thirdPartyCb) {
        this.thirdPartyCb = thirdPartyCb;
    }
    public String getOtherFeeCb() {
        return otherFeeCb;
    }
    public void setOtherFeeCb(String otherFeeCb) {
        this.otherFeeCb = otherFeeCb;
    }
    public BigDecimal getCompositeFeeRate() {
        return compositeFeeRate;
    }
    public void setCompositeFeeRate(BigDecimal compositeFeeRate) {
        this.compositeFeeRate = compositeFeeRate;
    }
    public BigDecimal getCompositeFee() {
        return compositeFee;
    }
    public void setCompositeFee(BigDecimal compositeFee) {
        this.compositeFee = compositeFee;
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
    public String getContractTermName() {
        return contractTermName;
    }
    public void setContractTermName(String contractTermName) {
        this.contractTermName = contractTermName;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getBailmoneyContract() {
        return bailmoneyContract;
    }
    public void setBailmoneyContract(String bailmoneyContract) {
        this.bailmoneyContract = bailmoneyContract;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
    }
    public String getAcrossSend() {
        return acrossSend;
    }
    public void setAcrossSend(String acrossSend) {
        this.acrossSend = acrossSend;
    }
    public String getLoanSendOutTypeName() {
        return loanSendOutTypeName;
    }
    public void setLoanSendOutTypeName(String loanSendOutTypeName) {
        this.loanSendOutTypeName = loanSendOutTypeName;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getUploadId() {
        return uploadId;
    }
    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
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
    public String getCplInterestPaymentName() {
        return cplInterestPaymentName;
    }
    public void setCplInterestPaymentName(String cplInterestPaymentName) {
        this.cplInterestPaymentName = cplInterestPaymentName;
    }
    public String getInterestWayName() {
        return interestWayName;
    }
    public void setInterestWayName(String interestWayName) {
        this.interestWayName = interestWayName;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public String getPlanStatus() {
        return planStatus;
    }
    public void setPlanStatus(String planStatus) {
        this.planStatus = planStatus;
    }
    public String getRatItemCode() {
        return ratItemCode;
    }
    public void setRatItemCode(String ratItemCode) {
        this.ratItemCode = ratItemCode;
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
    public String getFiscalQuarter() {
        return fiscalQuarter;
    }
    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
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
    public String getPointsProportionSelectName() {
        return pointsProportionSelectName;
    }
    public void setPointsProportionSelectName(String pointsProportionSelectName) {
        this.pointsProportionSelectName = pointsProportionSelectName;
    }
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    public String getAcrossSendName() {
        return acrossSendName;
    }
    public void setAcrossSendName(String acrossSendName) {
        this.acrossSendName = acrossSendName;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaranteeRmk() {
        return guaranteeRmk;
    }
    public void setGuaranteeRmk(String guaranteeRmk) {
        this.guaranteeRmk = guaranteeRmk;
    }
    public String getGuaWay() {
        return guaWay;
    }
    public void setGuaWay(String guaWay) {
        this.guaWay = guaWay;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getConType() {
        return conType;
    }
    public void setConType(String conType) {
        this.conType = conType;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
}
