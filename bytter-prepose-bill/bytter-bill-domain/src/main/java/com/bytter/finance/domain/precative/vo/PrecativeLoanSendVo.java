package com.bytter.finance.domain.precative.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
/**
 * @Description: 委托贷款发放
 *
 */
public class PrecativeLoanSendVo implements java.io.Serializable, IBusinessKey {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private String id;
    @BtCompositeSearch(fieldName = "a.id")
    private String infoId;
    /** 委托贷款录入单号 */
    @BtCompositeSearch(fieldName = "a.bill_code")
    private String infoBillCode;
    /** 委贷单位 */
//    @Corp(inject = "precativeName")
    @BtCompositeSearch(fieldName = "a.precative_Id")
    private String precativeId;
    /** 委托人单位名称 */
    @BtCompositeSearch(fieldName = "a.precative_Id")
    private String precativeName;
    /** 受托人单位 */
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 币别 */
//    @Currency(inject = "curName")
    private String curId;
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curName;
    /** 期限 */
    @BtCompositeSearch(fieldName = "a.term")
    private Integer term;
    /** 贷款金额 */
    @BtCompositeSearch(fieldName = "a.money")
    private java.math.BigDecimal infoMoney;
    /** 贷款录入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.create_Date")
    private Date infoCreateDate;
    /** 担保方式代码 */
    @BtCompositeSearch(fieldName = "a.pl_Type")
    @Dict(dictGroup = "Cms.PlType.C", inject = "plName")
    private String plType;
    private String plName;
    /** 贷款开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.start_Date")
    private Date startDate;
    /** 贷款结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.end_Date")
    private Date endDate;
    /** 借款人 */
//    @Corp(inject = "loanOppName")
    @BtCompositeSearch(fieldName = "a.loan_Opp")
    private String loanOpp;
    @BtCompositeSearch(fieldName = "a.loan_Opp")
    private String loanOppName;
    /** 委贷合同编码 */
    @BtCompositeSearch(fieldName = "a.contract_Code")
    private String infoContractCode;
    /** 期限单位 */
    @BtCompositeSearch(fieldName = "a.tern_Unit")
//    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitStatus")
    private String ternUnit;
    private String ternUnitStatus;
    /** 状态 */
    @BtCompositeSearch(fieldName = "a.status")
    private Integer infoStatus;
    /** 委贷申请编号 */
    private String fatherCode;
    /** 委贷发放编号 */
    @BtCompositeSearch(fieldName = "b.bill_code")
    private String billCode;
    /** 委贷发放合同编码 */
    @BtCompositeSearch(fieldName = "b.contract_Code")
    private String contractCode;
    /** 发放金额 */
    @BtCompositeSearch(fieldName = "a.money")
    private java.math.BigDecimal money;
    @BtCompositeSearch(fieldName = "a.money  - d.sendApproveMoney")
    private java.math.BigDecimal issuedMoney;
    /** 利率 */
    @BtCompositeSearch(fieldName = "b.rate")
    private java.math.BigDecimal rate;
    private java.math.BigDecimal overdueRate;
    /** 发放日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.send_Date")
    private Date sendDate;
    /** 到期日期 */
    @BtCompositeSearch(fieldName = "b.deadline_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deadlineDate;
    /** 还款方式 */
    @BtCompositeSearch(fieldName = "b.repay_Type")
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypes")
    private String repayType;
    private String repayTypes;
    /** 状态 */
    @BtCompositeSearch(fieldName = "b.status")
    private Integer status;
    /** 业务状态 */
    @BtCompositeSearch(fieldName = "b.biz_Status")
    private String bizStatus;
    private String infoBizStatus;
    /** 记账凭证号 */
    @BtCompositeSearch(fieldName = "b.voucher_No")
    private String voucherNo;
    /** 记账流水号 */
    @BtCompositeSearch(fieldName = "b.order_No")
    private String orderNo;
    /** 委贷账号 */
    @BtCompositeSearch(fieldName = "b.repay_Type")
    private String loanAcc;
    /** 委存账号 */
    @BtCompositeSearch(fieldName = "b.deposit_Acc")
    private String depositAcc;
    /** 记账流水号 */
    @BtCompositeSearch(fieldName = "b.order_No2")
    private String orderNo2;
    /** 记账凭证号 */
    @BtCompositeSearch(fieldName = "b.voucher_No2")
    private String voucherNo2;
    /** 发放次数 */
    @BtCompositeSearch(fieldName = "b.send_Time")
    private Integer sendTime;
    /** 上传文件信息 */
    @BtCompositeSearch(fieldName = "b.annex_Name")
    private String annexName;
    /** 浮动方式 */
    @BtCompositeSearch(fieldName = "b.float_Way")
    @Dict(dictGroup = "Cms.FloatWay", inject = "floatName")
    private String floatWay;
    private String floatName;
    /** 浮动二级方式 */
    private String floatWayNext;
    /** 点数或浮动比例 */
    @BtCompositeSearch(fieldName = "b.points_Proportion_Text")
    private java.math.BigDecimal pointsProportionText;
    /** 点数或者比例 */
    @BtCompositeSearch(fieldName = "b.points_Proportion_Select")
    @Dict(dictGroup = "Cms.PointsProportion", inject = "pointsProportionName")
    private String pointsProportionSelect;
    private String pointsProportionName;
    /** 利率调整生效日期 */
    @BtCompositeSearch(fieldName = "b.rate_Entries_Effect_Date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date rateEntriesEffectDate;
    /** 计息方式 */
    @BtCompositeSearch(fieldName = "b.interest_Way")
    @Dict(dictGroup = "Admin.PredrawSign", inject = "interesWays")
    private String interestWay;
    private String interesWays;
    /** 基准利率类型 */
    @BtCompositeSearch(fieldName = "b.reference_Rate_Type")
    private String referenceRateType;
    private String baseTypeName;
    /** 贷款周期 */
    @BtCompositeSearch(fieldName = "b.repay_Cycle")
    private String repayCycle;
    private String periodName;
    /** 利率复核生效日期 */
    @BtCompositeSearch(fieldName = "b.rate_time_of_take_effect")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date ratetimeoftakeeffect;
    /** 贷款录入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "b.create_Date")
    private Date createDate;
    /** 上期展期到期日期 **/
    private Date deferredRepayDate;
    @BtCompositeSearch(fieldName = "d.sendApproveMoney")
    private java.math.BigDecimal sendApproveMoney;
    /** 还款计划Id */
    private String planId;
    @BtCompositeSearch(fieldName = "loan_send_out_type")
    private String loanSendOutType;
    /** 记账标志 */
    @BtCompositeSearch(fieldName = "b.TALLY_SIGN")
    private String tallySign;
    /** 记账员代码 */
    private String tallyCode;
    /** 到期提醒发送标识 */
    private String sendSign;
    /** 贷款用途 */
    private String purpose;
    /** 备注 */
    private String rmk;
    /** 账务表ID */
    private String bookId;
    /** 全局业务ID */
    private String busGlobalId;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    private Date updateDate;
    /** 委托贷款对接的合同id */
    private String conId;
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
    public String getInfoId() {
        return infoId;
    }
    public void setInfoId(String infoId) {
        this.infoId = infoId;
    }
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public String getPrecativeId() {
        return precativeId;
    }
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    public String getCurId() {
        return curId;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public java.math.BigDecimal getInfoMoney() {
        return infoMoney;
    }
    public void setInfoMoney(java.math.BigDecimal infoMoney) {
        this.infoMoney = infoMoney;
    }
    public Date getInfoCreateDate() {
        return infoCreateDate;
    }
    public void setInfoCreateDate(Date infoCreateDate) {
        this.infoCreateDate = infoCreateDate;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
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
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public String getInfoContractCode() {
        return infoContractCode;
    }
    public void setInfoContractCode(String infoContractCode) {
        this.infoContractCode = infoContractCode;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getFatherCode() {
        return fatherCode;
    }
    public void setFatherCode(String fatherCode) {
        this.fatherCode = fatherCode;
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
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
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
    public java.math.BigDecimal getSendApproveMoney() {
        return sendApproveMoney;
    }
    public void setSendApproveMoney(java.math.BigDecimal sendApproveMoney) {
        this.sendApproveMoney = sendApproveMoney;
    }
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getPlName() {
        return plName;
    }
    public void setPlName(String plName) {
        this.plName = plName;
    }
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    public String getTernUnitStatus() {
        return ternUnitStatus;
    }
    public void setTernUnitStatus(String ternUnitStatus) {
        this.ternUnitStatus = ternUnitStatus;
    }
    public Integer getInfoStatus() {
        return infoStatus;
    }
    public void setInfoStatus(Integer infoStatus) {
        this.infoStatus = infoStatus;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getFloatName() {
        return floatName;
    }
    public void setFloatName(String floatName) {
        this.floatName = floatName;
    }
    public String getRepayTypes() {
        return repayTypes;
    }
    public void setRepayTypes(String repayTypes) {
        this.repayTypes = repayTypes;
    }
    public String getInteresWays() {
        return interesWays;
    }
    public void setInteresWays(String interesWays) {
        this.interesWays = interesWays;
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
    public String getPointsProportionName() {
        return pointsProportionName;
    }
    public void setPointsProportionName(String pointsProportionName) {
        this.pointsProportionName = pointsProportionName;
    }
    public String getBusinessKey() {
        return id;
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
    public String getInfoBizStatus() {
        return infoBizStatus;
    }
    public void setInfoBizStatus(String infoBizStatus) {
        this.infoBizStatus = infoBizStatus;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public java.math.BigDecimal getIssuedMoney() {
        return issuedMoney;
    }
    public void setIssuedMoney(java.math.BigDecimal issuedMoney) {
        this.issuedMoney = issuedMoney;
    }
    public String getSendSign() {
        return sendSign;
    }
    public void setSendSign(String sendSign) {
        this.sendSign = sendSign;
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
    public static long getSerialVersionUID() {
        return serialVersionUID;
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
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
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
