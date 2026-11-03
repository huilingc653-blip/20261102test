package com.bytter.finance.domain.inl.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Table(name = "CMS_INL_INFO", schema = "")
@SuppressWarnings("serial")
public class CmsInlInfo extends IdEntity implements Serializable {
    /**
     * 单据号
     */
    private String billNo;
    /**
     * 状态
     */
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
     * 合同类型id
     */
    private String conTypeId;
    /**
     * 合同挂接类型
     */
    private String conType;
    /**
     * 合同id
     */
    private String conId;
    /**
     * 是否跳过放款申请
     */
    private String isSkipSend;
    /**
     * 拆入单位
     */
    private String borrowInCorp;
    /**
     * 拆入单位名称
     */
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    private String borrowOutCorp;
    /**
     * 拆出单位名称
     */
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
    private String curId;
    /**
     * 拆借资金币种名称
     */
    private String curName;
    /**
     * 拆借金额
     */
    private BigDecimal money;
    /**
     * 拆借日期
     */
    private Date startDate;
    /**
     * 到期日期
     */
    private Date endDate;
    /**
     * 还款方式
     */
    private String repayType;
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
    private String unit;
    /**
     * 资金拆借利率形式
     */
    private String floatWay;
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
    private Date rateEntriesEffectDate;
    /**
     * 利息计算基数
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
     * 基准利率
     */
    private BigDecimal benchmarkRate;
    @Column(name = "BILL_NO")
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "BIZ_STATUS")
    public Integer getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(Integer bizStatus) {
        this.bizStatus = bizStatus;
    }
    @Column(name = "CON_TYPE_ID")
    public String getConTypeId() {
        return conTypeId;
    }
    public void setConTypeId(String conTypeId) {
        this.conTypeId = conTypeId;
    }
    @Column(name = "CON_ID")
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    @Column(name = "IS_SKIP_SEND")
    public String getIsSkipSend() {
        return isSkipSend;
    }
    public void setIsSkipSend(String isSkipSend) {
        this.isSkipSend = isSkipSend;
    }
    @Column(name = "BORROW_IN_CORP")
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    @Column(name = "BORROW_OUT_CORP")
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    @Column(name = "SEND_CODE")
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    @Column(name = "SEND_TYPE")
    public String getSendType() {
        return sendType;
    }
    public void setSendType(String sendType) {
        this.sendType = sendType;
    }
    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name = "MONEY")
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
    @Column(name = "START_DATE")
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Column(name = "END_DATE")
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    @Column(name = "REPAY_TYPE")
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    @Column(name = "TERM")
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    @Column(name = "UNIT")
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "FLOAT_WAY")
    public String getFloatWay() {
        return floatWay;
    }
    public void setFloatWay(String floatWay) {
        this.floatWay = floatWay;
    }
    @Column(name = "FLOAT_WAY_NEXT")
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    @Column(name = "REFERENCE_RATE_TYPE")
    public String getReferenceRateType() {
        return referenceRateType;
    }
    public void setReferenceRateType(String referenceRateType) {
        this.referenceRateType = referenceRateType;
    }
    @Column(name = "POINTS_PROPORTION_SELECT")
    public String getPointsProportionSelect() {
        return pointsProportionSelect;
    }
    public void setPointsProportionSelect(String pointsProportionSelect) {
        this.pointsProportionSelect = pointsProportionSelect;
    }
    @Column(name = "POINTS_PROPORTION_TEXT")
    public BigDecimal getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(BigDecimal pointsProportionText) {
        this.pointsProportionText = pointsProportionText;
    }
    @Column(name = "REPAY_CYCLE")
    public String getRepayCycle() {
        return repayCycle;
    }
    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }
    @Column(name = "RATE_ENTRIES_EFFECT_DATE")
    public Date getRateEntriesEffectDate() {
        return rateEntriesEffectDate;
    }
    public void setRateEntriesEffectDate(Date rateEntriesEffectDate) {
        this.rateEntriesEffectDate = rateEntriesEffectDate;
    }
    @Column(name = "RATE_BASE")
    public String getRateBase() {
        return rateBase;
    }
    public void setRateBase(String rateBase) {
        this.rateBase = rateBase;
    }
    @Column(name = "RATE")
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    @Column(name = "INTEREST")
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    @Column(name = "PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "DEFERRED_RATE")
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    @Column(name = "OVERDUE_RATE")
    public BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    @Column(name = "IS_HANG_CREDIT")
    public String getIsHangCredit() {
        return isHangCredit;
    }
    public void setIsHangCredit(String isHangCredit) {
        this.isHangCredit = isHangCredit;
    }
    @Column(name = "RATION_CODE")
    public String getRationCode() {
        return rationCode;
    }
    public void setRationCode(String rationCode) {
        this.rationCode = rationCode;
    }
    @Column(name = "CP_RAT_CODE")
    public String getCpRatCode() {
        return cpRatCode;
    }
    public void setCpRatCode(String cpRatCode) {
        this.cpRatCode = cpRatCode;
    }
    @Column(name = "IS_HANG_GUARANTEE")
    public String getIsHangGuarantee() {
        return isHangGuarantee;
    }
    public void setIsHangGuarantee(String isHangGuarantee) {
        this.isHangGuarantee = isHangGuarantee;
    }
    @Column(name = "GUA_CONTRACT")
    public String getGuaContract() {
        return guaContract;
    }
    public void setGuaContract(String guaContract) {
        this.guaContract = guaContract;
    }
    @Column(name = "IS_BUDGET")
    public String getIsBudget() {
        return isBudget;
    }
    public void setIsBudget(String isBudget) {
        this.isBudget = isBudget;
    }
    @Column(name = "RMK")
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    @Column(name = "ATTACHMENT_ID")
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "BORROW_IN_ACC")
    public String getBorrowInAcc() {
        return borrowInAcc;
    }
    public void setBorrowInAcc(String borrowInAcc) {
        this.borrowInAcc = borrowInAcc;
    }
    @Column(name = "BORROW_IN_ACCID")
    public String getBorrowInAccId() {
        return borrowInAccId;
    }
    public void setBorrowInAccId(String borrowInAccId) {
        this.borrowInAccId = borrowInAccId;
    }
    @Column(name = "BORROW_OUT_ACC")
    public String getBorrowOutAcc() {
        return borrowOutAcc;
    }
    public void setBorrowOutAcc(String borrowOutAcc) {
        this.borrowOutAcc = borrowOutAcc;
    }
    @Column(name = "BORROW_OUT_ACCID")
    public String getBorrowOutAccId() {
        return borrowOutAccId;
    }
    public void setBorrowOutAccId(String borrowOutAccId) {
        this.borrowOutAccId = borrowOutAccId;
    }
    @Column(name = "CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    @Column(name = "CP_RAT_ITEM_CODE")
    public String getCpRatItemCode() {
        return cpRatItemCode;
    }
    public void setCpRatItemCode(String cpRatItemCode) {
        this.cpRatItemCode = cpRatItemCode;
    }
    @Column(name = "IS_CON")
    public String getIsCon() {
        return isCon;
    }
    public void setIsCon(String isCon) {
        this.isCon = isCon;
    }
    @Column(name = "GUA_CONTRACT_ID")
    public String getGuaContractId() {
        return guaContractId;
    }
    public void setGuaContractId(String guaContractId) {
        this.guaContractId = guaContractId;
    }
    @Column(name = "CON_TYPE")
    public String getConType() {
        return conType;
    }
    public void setConType(String conType) {
        this.conType = conType;
    }
    @Column(name = "INTEREST_WAY")
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
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
    @Column(name = "BENCHMARK_RATE")
    public BigDecimal getBenchmarkRate() {
        return benchmarkRate;
    }
    public void setBenchmarkRate(BigDecimal benchmarkRate) {
        this.benchmarkRate = benchmarkRate;
    }
    @Column(name = "BORROW_IN_CORP_NAME")
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }
    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }
    @Column(name = "BORROW_OUT_CORP_NAME")
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }
    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
    }
    @Column(name = "CUR_NAME")
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
