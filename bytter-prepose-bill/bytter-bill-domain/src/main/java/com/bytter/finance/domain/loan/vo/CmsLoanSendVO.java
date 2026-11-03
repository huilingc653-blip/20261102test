package com.bytter.finance.domain.loan.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.bytter.finance.domain.basedata.vo.RepaymentScheduleVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
public class CmsLoanSendVO implements Serializable, IBusinessKey {
    private String id;
    /** 贷款发放编号 */
    @BtCompositeSearch(fieldName = "a.billCode")
    private String billCode;
    /** 发放合同 */
    private String contractCode;
    /** 贷款信息编号 */
    @BtCompositeSearch(fieldName = "a.loanCode")
    private String loanCode;
    /** 单位代码 */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "a.corpId")
    private String corpId;
    private String corpName;
    /** 资金中心 */
//    @Corp(inject = "netName")
    private String netId;
    private String netName;
    /** 贷款金额 */
    private java.math.BigDecimal loanInfoMoney;
    /** 发放金额 */
    @BtCompositeSearch(fieldName = "a.loanMoney")
    private java.math.BigDecimal loanMoney;
    /** 余额 */
    private java.math.BigDecimal remainMoney;
    /** 已还金额 */
    @BtCompositeSearch(fieldName = "a.repayedMoney")
    private java.math.BigDecimal repayedMoney;
    /** 贷款账号 */
    private String loanAcc;
    /** 发放日期 */
    @BtCompositeSearch(fieldName = "a.loanDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date loanDate;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.repayDate")
    private java.util.Date repayDate;

    private String repayDateStr;

    /** 放款利率 */
    private java.math.BigDecimal loanRate;
    /** 放款账号 */
    private String provideLoanAcc;
    /** 放款币种 */
//    @Currency(inject = "curName")
    private String curCode;
    private String curName;
    /** 贷款种类 */
    private String loanClass;
    /** 担保方式 */
    private String loanType;
    /** 贷款期限 */
    private Integer loanTerm;
    /** 期限单位 */
    private String ternUnit;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 上期贷款展期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lastDeferredDate;
    /** 展期到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date deferredReplayDate;
    /** 展期次数 */
    private Integer deferredNum;
    /** 增值税税率 */
    private java.math.BigDecimal texRate;
    /** 往来账号 */
    private String netAcc;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    private String bizStatus;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 记账跨中心凭证号 */
    private String voucherNo1;
    /** 记账跨中心流水号 */
    private String orderNo1;
    /** 打回原因 */
    private String refuseWhy;
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
    /** 支付方式 */
    private String payType;
    /** 发放次数 */
    private Integer sendTime;
    /** 借款单位 */
    private String borrowCorpId;
    /** 还款方式 */
    private String repayType;
    private String repayTypeName;
    /** 协定费率 */
    private java.math.BigDecimal clbAgreementRate;
    /** 固定费率 */
    private java.math.BigDecimal clbFixedRate;
    /** 借贷关联的借款单号 */
    private String ploadcode;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @BtCompositeSearch(fieldName = "a.createDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    /** 还款中记录数 */
    private Integer repayingCount;
    /** 附件 */
    private String attachmentId;
    /** 浮动二级方式 **/
    private String floatWayNext;
    /* 发放创建人 **/
    private String sendCreateBy;
    /** 录入创建人 */
    private String infoCreateBy;
    /** 录入修改人 */
    private String infoUpdateBy;
    /**
     * 还款计划
     */
    private RepaymentScheduleVO repaymentScheduleVO;
    /**
     * 是否隐藏多选框 1为是
     */
    private String checkboxHide;
    public String getCheckboxHide() {
        if (StringUtils.isEmpty(checkboxHide) && this.repayingCount != null && this.repayingCount != 0) {// 状态不为还款中
            return "1";
        } else {
            return "0";
        }
    }
    public void setCheckboxHide(String checkboxHide) {
        this.checkboxHide = checkboxHide;
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
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
    }
    public RepaymentScheduleVO getRepaymentScheduleVO() {
        return repaymentScheduleVO;
    }
    public void setRepaymentScheduleVO(RepaymentScheduleVO repaymentScheduleVO) {
        this.repaymentScheduleVO = repaymentScheduleVO;
    }
    public java.math.BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(java.math.BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public java.math.BigDecimal getLoanInfoMoney() {
        return loanInfoMoney;
    }
    public void setLoanInfoMoney(java.math.BigDecimal loanInfoMoney) {
        this.loanInfoMoney = loanInfoMoney;
    }
    public java.math.BigDecimal getRemainMoney() {
        return remainMoney;
    }
    public void setRemainMoney(java.math.BigDecimal remainMoney) {
        this.remainMoney = remainMoney;
    }
    public java.math.BigDecimal getRepayedMoney() {
        return repayedMoney;
    }
    public void setRepayedMoney(java.math.BigDecimal repayedMoney) {
        this.repayedMoney = repayedMoney;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
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

    public String getRepayDateStr() {
        return repayDateStr;
    }

    public void setRepayDateStr(String repayDateStr) {
        this.repayDateStr = repayDateStr;
    }

    public java.math.BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(java.math.BigDecimal loanRate) {
        this.loanRate = loanRate;
    }
    public String getProvideLoanAcc() {
        return provideLoanAcc;
    }
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
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
    public String getLoanClass() {
        return loanClass;
    }
    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }
    public String getLoanType() {
        return loanType;
    }
    public void setLoanType(String loanType) {
        this.loanType = loanType;
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
    public java.math.BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(java.math.BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public java.math.BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(java.math.BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }
    public java.util.Date getDeferredReplayDate() {
        return deferredReplayDate;
    }
    public void setDeferredReplayDate(java.util.Date deferredReplayDate) {
        this.deferredReplayDate = deferredReplayDate;
    }
    public java.util.Date getLastDeferredDate() {
        return lastDeferredDate;
    }
    public void setLastDeferredDate(java.util.Date lastDeferredDate) {
        this.lastDeferredDate = lastDeferredDate;
    }
    public Integer getDeferredNum() {
        return deferredNum;
    }
    public void setDeferredNum(Integer deferredNum) {
        this.deferredNum = deferredNum;
    }
    public java.math.BigDecimal getTexRate() {
        return texRate;
    }
    public void setTexRate(java.math.BigDecimal texRate) {
        this.texRate = texRate;
    }
    public String getNetAcc() {
        return netAcc;
    }
    public void setNetAcc(String netAcc) {
        this.netAcc = netAcc;
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
    public String getVoucherNo1() {
        return voucherNo1;
    }
    public void setVoucherNo1(String voucherNo1) {
        this.voucherNo1 = voucherNo1;
    }
    public String getOrderNo1() {
        return orderNo1;
    }
    public void setOrderNo1(String orderNo1) {
        this.orderNo1 = orderNo1;
    }
    public String getRefuseWhy() {
        return refuseWhy;
    }
    public void setRefuseWhy(String refuseWhy) {
        this.refuseWhy = refuseWhy;
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
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public Integer getSendTime() {
        return sendTime;
    }
    public void setSendTime(Integer sendTime) {
        this.sendTime = sendTime;
    }
    public String getBorrowCorpId() {
        return borrowCorpId;
    }
    public void setBorrowCorpId(String borrowCorpId) {
        this.borrowCorpId = borrowCorpId;
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

    public java.math.BigDecimal getClbAgreementRate() {
        return clbAgreementRate;
    }
    public void setClbAgreementRate(java.math.BigDecimal clbAgreementRate) {
        this.clbAgreementRate = clbAgreementRate;
    }
    public java.math.BigDecimal getClbFixedRate() {
        return clbFixedRate;
    }
    public void setClbFixedRate(java.math.BigDecimal clbFixedRate) {
        this.clbFixedRate = clbFixedRate;
    }
    public String getPloadcode() {
        return ploadcode;
    }
    public void setPloadcode(String ploadcode) {
        this.ploadcode = ploadcode;
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
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public Integer getRepayingCount() {
        return repayingCount;
    }
    public void setRepayingCount(Integer repayingCount) {
        this.repayingCount = repayingCount;
    }
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    public String getBusinessKey() {
        return getId();
    }
    public String getFloatWayNext() {
        return floatWayNext;
    }
    public void setFloatWayNext(String floatWayNext) {
        this.floatWayNext = floatWayNext;
    }
    public String getSendCreateBy() {
        return sendCreateBy;
    }
    public void setSendCreateBy(String sendCreateBy) {
        this.sendCreateBy = sendCreateBy;
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
