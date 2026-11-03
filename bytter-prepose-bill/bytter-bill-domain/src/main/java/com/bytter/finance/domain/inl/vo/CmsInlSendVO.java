package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsInlSendVO implements Serializable, IBusinessKey {
    /**
     * 主键id
     */
    private String id;
    /**
     * 申请表id
     */
    private String inlInfoId;
    /**
     * 申请表业务编号
     */
    @BtCompositeSearch(fieldName = "info.bill_no")
    private String infoBillNo;
    /**
     * 业务编号
     */
    @BtCompositeSearch(fieldName = "send.bill_no")
    private String billNo;
    /**
     * 状态
     */
    @BtCompositeSearch(fieldName = "send.status")
    private Integer status;
    /**
     * 业务状态
     */
    private Integer bizStatus;
    /**
     * 发放编码
     */
    @BtCompositeSearch(fieldName = "send.send_code")
    private String sendCode;
    /**
     * 发放类型，一次性发放，多次发放
     */
    private String sendType;
    /**
     * 拆入单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_in_corp")
//    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_out_corp")
//    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 币种
     */
    @BtCompositeSearch(fieldName = "send.cur_id")
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     *
     */
    private BigDecimal sendMoney;
    /**
     * 发放日期
     */
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "send.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "send.repay_type")
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
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 浮动方式
     */
    private String floatWay;
    /**
     * 利率调整生效日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rateEntriesffectDate;
    /**
     * 基准利率类型
     */
    private String referenceRateType;
    /**
     * 二级浮动方式
     */
    private String floatWayNext;
    /**
     * 拆借周期
     */
    private String repayCycle;
    /**
     * 点数或者比例
     */
    private String pointsProportionSelect;
    /**
     * 点数或者比例值
     */
    private BigDecimal pointsProportionText;
    /**
     * 利息计息基数
     */
    private String rateBase;
    /**
     * 资金拆借利率
     */
    private BigDecimal rate;
    /**
     * 拆借利息
     */
    private BigDecimal interest;
    /**
     * 展期利率
     */
    private BigDecimal deferredRate;
    /**
     * 逾期利率
     */
    private BigDecimal overdueRate;
    /**
     * 拆入方收本金账户
     */
    private String borrowInAcc;
    /**
     * 拆入方账号id
     */
    private String borrowInAccId;
    /**
     * 拆入方账号
     */
    private String borrowOutAcc;
    /**
     * 拆出方账户id
     */
    private String borrowOutAccId;
    /**
     * 附件id
     */
    private String attachmentId;
    /**
     * 备注
     */
    private String rmk;
    /**
     * 是否挂资金计划
     */
    private String isBudget;
    /**
     * 是否展期
     */
    private String isDeferred;
    /**
     * 展期日期到期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredEndDate;
    /**
     * 实际发放十日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date realSendDate;
    /**
     * 创建人
     */
    private String createBy;
    /**
     * 创建日期
     */
    private Date createDate;
    /**
     * 修改人
     */
    private String updateBy;
    /**
     * 修改日期
     */
    private Date updateDate;
    /**
     * 还款计划id
     */
    private String planId;
    /**
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    /** 资金预算 */
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getInlInfoId() {
        return inlInfoId;
    }
    public void setInlInfoId(String inlInfoId) {
        this.inlInfoId = inlInfoId;
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
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
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

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
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
    public Date getRateEntriesffectDate() {
        return rateEntriesffectDate;
    }
    public void setRateEntriesffectDate(Date rateEntriesffectDate) {
        this.rateEntriesffectDate = rateEntriesffectDate;
    }
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
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
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
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
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    public Date getRealSendDate() {
        return realSendDate;
    }
    public void setRealSendDate(Date realSendDate) {
        this.realSendDate = realSendDate;
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
    public String getInfoBillNo() {
        return infoBillNo;
    }
    public void setInfoBillNo(String infoBillNo) {
        this.infoBillNo = infoBillNo;
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
    public String getIsDeferred() {
        return isDeferred;
    }
    public void setIsDeferred(String isDeferred) {
        this.isDeferred = isDeferred;
    }
    public Date getDeferredEndDate() {
        return deferredEndDate;
    }
    public void setDeferredEndDate(Date deferredEndDate) {
        this.deferredEndDate = deferredEndDate;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getPlanId() {
        return planId;
    }
    public void setPlanId(String planId) {
        this.planId = planId;
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
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    @Override
    public String getBusinessKey() {
        return id;
    }
}
