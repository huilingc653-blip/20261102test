package com.bytter.finance.dto;
import java.io.Serializable;
public class CmsPrecativeLoanSendDTO implements Serializable {
    private String id;
    /** 委贷单位 */
    private String precativeId;
    /** 受托单位 */
    private String corpId;
    /** 委贷申请编号 */
    private String fatherCode;
    /** 委贷发放编号 */
    private String billCode;
    /** 委贷发放合同编码 */
    private String contractCode;
    /** 发放金额 */
    private java.math.BigDecimal money;
    /** 利率 */
    private java.math.BigDecimal rate;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 发放日期 */
    private java.util.Date sendDate;
    /** 到期日期 */
    private java.util.Date deadlineDate;
    /** 期限 */
    private Integer term;
    /** 期限单位 */
    private String ternUnit;
    /** 还款方式 */
    private String repayType;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 委贷账号 */
    private String loanAcc;
    /** 委存账号 */
    private String depositAcc;
    /** 记账流水号 */
    private String orderNo2;
    /** 记账凭证号 */
    private String voucherNo2;
    /** 发放次数 */
    private Integer sendTime;
    /** 上传文件信息 */
    private String annexName;
    /** 浮动方式 */
    private String floatWay;
    /** 浮动二级方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    private String pointsProportionText;
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
    /** 记账标志 */
    private String tallySign;
    /** 记账员代码 */
    private String tallyCode;
    /** 贷款用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 账务表ID */
    private String bookId;
    /** 全局业务ID */
    private String busGlobalId;
    /** 展期到期日期 */
    private java.util.Date deferredDate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 录入创建人 */
    private String infoCreateBy;
    /** 录入修改人 */
    private String infoUpdateBy;
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
    public String getFatherCode() {
        return fatherCode;
    }
    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
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
    public java.util.Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(java.util.Date sendDate) {
        this.sendDate = sendDate;
    }
    public java.util.Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(java.util.Date deadlineDate) {
        this.deadlineDate = deadlineDate;
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
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
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
    public String getDepositAcc() {
        return depositAcc;
    }
    public void setDepositAcc(String depositAcc) {
        this.depositAcc = depositAcc;
    }
    public String getOrderNo2() {
        return orderNo2;
    }
    public void setOrderNo2(String orderNo2) {
        this.orderNo2 = orderNo2;
    }
    public String getVoucherNo2() {
        return voucherNo2;
    }
    public void setVoucherNo2(String voucherNo2) {
        this.voucherNo2 = voucherNo2;
    }
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
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
    public String getPointsProportionText() {
        return pointsProportionText;
    }
    public void setPointsProportionText(String pointsProportionText) {
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
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
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
    public String getBookId() {
        return bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public String getBusGlobalId() {
        return busGlobalId;
    }
    public void setBusGlobalId(String busGlobalId) {
        this.busGlobalId = busGlobalId;
    }
    public java.util.Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(java.util.Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    public java.math.BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(java.math.BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
}
