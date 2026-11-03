package com.bytter.finance.domain.loan.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsLoanQueryVO implements Serializable, IBusinessKey {
    @BtCompositeSearch(fieldName = "a.id")
    private String id;
    /** 结算中心代码 */
//    @Corp(inject = "netName")
    @BtCompositeSearch(fieldName = "a.net_code")
    private String netCode;
    private String netName;
    /** 单位代码 */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "a.corp_id")
    private String corpId;
    private String corpName;
    /** 贷款流水号 */
    @BtCompositeSearch(fieldName = "a.bill_code")
    private String infoBillCode;
    @BtCompositeSearch(fieldName = "s.bill_Code")
    private String sendBillCode;
    /** 借款合同号 */
    @BtCompositeSearch(fieldName = "a.contract_code")
    private String contractCode;
    /** 贷款金额 */
    @BtCompositeSearch(fieldName = "a.loan_money")
    private BigDecimal loanMoney;
    /** 贷款期限 */
    private Integer loanTerm;
    /** 期限单位 */
    private String ternUnit;
    /** 放款币种 */
//    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_code")
    private String curCode;
    private String curName;
    /** 贷款日期 */
    @BtCompositeSearch(fieldName = "a.loan_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date loanDate;
    /** 还款日期 */
    @BtCompositeSearch(fieldName = "a.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date repayDate;
    /** 还款方式 */
    private String repayType;
    private String repayTypeName;
    /** 贷款种类 */
    private String loanClass;
    /** 贷款类型 */
    @BtCompositeSearch(fieldName = "a.loan_Genre")
    private String loanGenre;
    /** 贷款类型名称 */
    private String loanGenreName;
    /** 担保方式 */
    private String loanType;
    /** 担保说明 */
    private String suretyExplain;
    /** 担保品价值 */
    private BigDecimal suretyMoney;
    /** 放款账号 */
    private String provideLoanAcc;
    /** 贷款账号 */
    @BtCompositeSearch(fieldName = "s.loan_acc")
    private String loanAcc;
    /** 放款利率 */
    private BigDecimal loanRate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 展期次数 */
    @BtCompositeSearch(fieldName = "def.deferredNum")
    private Integer deferredNum;
    /** 补录员 */
    private String repairOp;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 借款级次 */
    private String loanGrade;
    /** 用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 打回原因 */
    private String refuseWhy;
    /** 是否挂授信 */
    private String isHangCredit;
    /** 额度编号 */
    private String rationCode;
    /** 授信额度品种代码 */
    private String itemCode;
    /** 贷款发放方式 */
    private String loanSendOutType;
    /** 浮动方式 */
    private String floatWay;
    /** 点数或浮动比例 */
    private String pointsProportionText;
    /** 点数或者比例 */
    private String pointsProportionSelect;
    /** 利率调整生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date rateEntriesEffectDate;
    /** 计息方式 */
    private String interestWay;
    /** 基准利率类型 */
    private String referenceRateType;
    private String referenceRateTypeName;
    /** 贷款周期 */
    private String repayCycle;
    private String repayCycleName;
    /** 利率复核生效日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date ratetimeoftakeeffect;
    /** 发放次数 */
    private Integer sendTime;
    /** 已发放金额 */
    @BtCompositeSearch(fieldName = "s.loan_money")
    private BigDecimal sendMoney;
    @BtCompositeSearch(fieldName = "stayMoney")
    private BigDecimal stayMoney;
    /** 协定费率 */
    private BigDecimal clbAgreementRate;
    /** 借款单位 */
    private String borrowCorpId;
    /** 支付方式 */
    private String payType;
    /** 固定费率 */
    private BigDecimal clbFixedRate;
    /** 借贷关联的借款单号 */
    private String ploadcode;
    /** 行业代码 */
    private String industryCode;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.create_date")
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    @BtCompositeSearch(fieldName = "repayLoan.repayMoney")
    private BigDecimal repayMoney;
    @BtCompositeSearch(fieldName = "repayAwayMoney")
    private BigDecimal repayAwayMoney;
    /**
     * 还款利息
     */
    private BigDecimal repayAccrual;
    /** 对接合同管理的合同id */
    private String conId;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public Integer getLoanTerm() {
        return loanTerm;
    }
    public void setLoanTerm(Integer loanTerm) {
        this.loanTerm = loanTerm;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getCurCode() {
        return curCode;
    }
    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public java.util.Date getLoanDate() {
        return loanDate;
    }
    public void setLoanDate(java.util.Date loanDate) {
        this.loanDate = loanDate;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
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

    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public String getLoanGenre() {
        return loanGenre;
    }
    public void setLoanGenre(String loanGenre) {
        this.loanGenre = loanGenre;
    }
    public String getLoanGenreName() {
        return loanGenreName;
    }
    public void setLoanGenreName(String loanGenreName) {
        this.loanGenreName = loanGenreName;
    }
    public String getLoanType() {
        return loanType;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
    public String getSuretyExplain() {
        return suretyExplain;
    }
    public void setSuretyExplain(String suretyExplain) {
        this.suretyExplain = suretyExplain;
    }
    public BigDecimal getSuretyMoney() {
        return suretyMoney;
    }
    public void setSuretyMoney(BigDecimal suretyMoney) {
        this.suretyMoney = suretyMoney;
    }
    public String getProvideLoanAcc() {
        return provideLoanAcc;
    }
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
    }
    public BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
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
    public Integer getDeferredNum() {
        return deferredNum;
    }
    public void setDeferredNum(Integer deferredNum) {
        this.deferredNum = deferredNum;
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
    public String getLoanGrade() {
        return loanGrade;
    }
    public void setLoanGrade(String loanGrade) {
        this.loanGrade = loanGrade;
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
    public String getItemCode() {
        return itemCode;
    }
    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
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
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getClbAgreementRate() {
        return clbAgreementRate;
    }
    public void setClbAgreementRate(BigDecimal clbAgreementRate) {
        this.clbAgreementRate = clbAgreementRate;
    }
    public String getBorrowCorpId() {
        return borrowCorpId;
    }
    public void setBorrowCorpId(String borrowCorpId) {
        this.borrowCorpId = borrowCorpId;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public BigDecimal getClbFixedRate() {
        return clbFixedRate;
    }
    public void setClbFixedRate(BigDecimal clbFixedRate) {
        this.clbFixedRate = clbFixedRate;
    }
    public String getPloadcode() {
        return ploadcode;
    }
    public void setPloadcode(String ploadcode) {
        this.ploadcode = ploadcode;
    }
    public String getIndustryCode() {
        return industryCode;
    }
    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
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
    public String getBusinessKey() {
        return getId();
    }
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRepayAwayMoney() {
        return repayAwayMoney;
    }
    public void setRepayAwayMoney(BigDecimal repayAwayMoney) {
        this.repayAwayMoney = repayAwayMoney;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public BigDecimal getStayMoney() {
        return stayMoney;
    }
    public void setStayMoney(BigDecimal stayMoney) {
        this.stayMoney = stayMoney;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
}
