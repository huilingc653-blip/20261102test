package com.bytter.guarantee.domain.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class GuaContractCmsInlVO implements Serializable {
    /**
     * 主键id
     */
    private String id;
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "gco.status")
    private Integer status;
    /**
     * 业务状态
     */
    private Integer bizStatus;
    /**
     * 是否挂接合同业务
     */
    private String isCon;
    /**
     * 合同类型状态
     */
    private String conTypeId;
    /**
     * 合同id
     */
    private String conId;
    /**
     * 合同挂接类型
     */
    private String conType;
    /**
     * 是否跳过放款申请
     */
    private String isSkipSend;
    /**
     * 拆入单位
     */
    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 放款单号
     */
    private String sendCode;
    /**
     * 放款方式
     */
    private String sendType;
    /**
     * 拆借资金币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     * 拆借金额
     */
    private BigDecimal money;
    /**
     * 拆借日期
     */
//    @BtCompositeSearch(fieldName = "info.start_date")
    private Date startDate;
    /**
     * 到期日期
     */
//    @BtCompositeSearch(fieldName = "info.end_date")
    private Date endDate;
    /**
     * 还款方式
     */
    @Dict(dictGroup = "cms.LoanRepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    /**
     * 计息方式
     */
    private String interestWay;
    /**
     * 计息日
     */
    private Integer expiryDate;
    /**
     * 预提日
     */
    private Integer accruedDailyDate;
    /**
     * 拆借期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "unitName")
    private String unit;
    private String unitName;
    /**
     * 内部拆借期限
     */
    private String termUnit;
    /**
     * 资金拆借利率形式
     */
    @Dict(dictGroup = "Cms.FloatWay", inject = "floatWayName")
    private String floatWay;
    private String floatWayName;
    /**
     * 浮动二级方式
     */
    private String floatWayNext;
    /**
     * 基准利率类型
     */
    private String referenceRateType;
    /**
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /**
     * 点数或者比例值
     */
    private BigDecimal pointsProportionText;
    /**
     * 拆借周期
     */
    private String repayCycle;
    /**
     * 利率调整生效日期
     */
    private String rateEntriesEffectDate;
    /**
     * 利息计算基数
     */
    @Dict(dictGroup = "Cms.RateBase", inject = "rateBaseName")
    private String rateBase;
    private String rateBaseName;
    /**
     * 资金拆借利率
     */
    private BigDecimal rate;
    /**
     * 拆借利息
     */
    private String interest;
    /**
     * 拆借资金用途
     */
    private String purpose;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /**
     * 是否挂授信
     */
    private String isHangCredit;
    /**
     * 额度编号
     */
    private String rationCode;
    /**
     * 占用编码
     */
    private String cpRatCode;
    /**
     * 业务品种
     */
    private String cpRatItemCode;
    private String cpRatItemCodeName;
    /**
     * 是否挂担保合同
     */
    private String isHangGuarantee;
    /**
     * 担保合同号
     */
    private String guaContract;
    /**
     * 担保合Id
     */
    private String guaContractId;
    /**
     * 是否挂资金计划
     */
    private String isBudget;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 附件
     */
    private String attachmentId;
    /**
     * 拆入方收本金账户
     */
    private String borrowInAcc;
    /**
     * 拆入方收本金账户id
     */
    private String borrowInAccId;
    /**
     * 拆出方付本金账户
     */
    private String borrowOutAcc;
    /**
     * 拆出方付本金账户id
     */
    private String borrowOutAccId;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改时间
     */
    private Date updateDate;
    /**
     * 已放款金额
     */
    private BigDecimal sendMoney;
    /**
     * 待放款金额
     */
    private BigDecimal waitSendMoney;
    /**
     * 放款在途金额
     */
    private BigDecimal sendingMoney;
    /**
     * 放款业务 放款状态
     */
    private Integer sendStatus;
    /**
     * 还款计划id
     */
    private String planId;
    /**
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /**
     * 被担保合同使用时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "gco.use_date")
    private Date useDate;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
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
    public String getIsSkipSend() {
        return isSkipSend;
    }
    public void setIsSkipSend(String isSkipSend) {
        this.isSkipSend = isSkipSend;
    }
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public String getSendType() {
        return sendType;
    }
    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
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
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    public String getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(String rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getInterest() {
        return interest;
    }
    public void setInterest(String interest) {
        this.interest = interest;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    public String getCpRatCode() {
        return cpRatCode;
    }
    public void setCpRatCode(String cpRatCode) {
        this.cpRatCode = cpRatCode;
    }
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getBorrowInAcc() {
        return borrowInAcc;
    }
    public void setBorrowInAcc(String borrowInAcc) {
        this.borrowInAcc = borrowInAcc;
    }
    public String getBorrowInAccId() {
        return borrowInAccId;
    }
    public void setBorrowInAccId(String borrowInAccId) {
        this.borrowInAccId = borrowInAccId;
    }
    public String getBorrowOutAcc() {
        return borrowOutAcc;
    }
    public void setBorrowOutAcc(String borrowOutAcc) {
        this.borrowOutAcc = borrowOutAcc;
    }
    public String getBorrowOutAccId() {
        return borrowOutAccId;
    }
    public void setBorrowOutAccId(String borrowOutAccId) {
        this.borrowOutAccId = borrowOutAccId;
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
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }
    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }
    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getCpRatItemCode() {
        return cpRatItemCode;
    }
    public void setCpRatItemCode(String cpRatItemCode) {
        this.cpRatItemCode = cpRatItemCode;
    }
    public String getIsCon() {
        return isCon;
    }
    public void setIsCon(String isCon) {
        this.isCon = isCon;
    }
    public String getGuaContractId() {
        return guaContractId;
    }
    public void setGuaContractId(String guaContractId) {
        this.guaContractId = guaContractId;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getWaitSendMoney() {
        return waitSendMoney;
    }
    public void setWaitSendMoney(BigDecimal waitSendMoney) {
        this.waitSendMoney = waitSendMoney;
    }
    public BigDecimal getSendingMoney() {
        return sendingMoney;
    }
    public void setSendingMoney(BigDecimal sendingMoney) {
        this.sendingMoney = sendingMoney;
    }
    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }
    public String getTermUnit() {
        return this.term + this.getUnitName();
    }
    public String getConType() {
        return conType;
    }
    public void setConType(String conType) {
        this.conType = conType;
    }
    public String getRepayTypeName() {
        return repayTypeName;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public void setTermUnit(String termUnit) {
        this.termUnit = termUnit;
    }
    public String getFloatWayName() {
        return floatWayName;
    }
    public void setFloatWayName(String floatWayName) {
        this.floatWayName = floatWayName;
    }
    public String getRateBaseName() {
        return rateBaseName;
    }
    public void setRateBaseName(String rateBaseName) {
        this.rateBaseName = rateBaseName;
    }
    public String getCpRatItemCodeName() {
        return cpRatItemCodeName;
    }
    public void setCpRatItemCodeName(String cpRatItemCodeName) {
        this.cpRatItemCodeName = cpRatItemCodeName;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
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
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
    }
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    public String getUnitName() {
        return unitName;
    }
    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
    public Date getUseDate() {
        return useDate;
    }
    public void setUseDate(Date useDate) {
        this.useDate = useDate;
    }
}
