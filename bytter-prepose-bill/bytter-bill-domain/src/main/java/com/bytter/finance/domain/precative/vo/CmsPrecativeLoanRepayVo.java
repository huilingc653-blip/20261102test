package com.bytter.finance.domain.precative.vo;
import java.math.BigDecimal;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.bytter.core.bpm.IBusinessKey;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @Title: Entity
 * @Description: 委托贷款还款信息表
 * @author onlineGenerator
 * @date 2017-08-02 14:05:07
 * @version 1.0
 *
 */
public class CmsPrecativeLoanRepayVo implements java.io.Serializable, IBusinessKey {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    private String sendId;
    /** 单据编号 */
    @BtCompositeSearch(fieldName = "a.bill_Code")
    private String billCode;
    /** 借款单位代码 */
//    @Corp(inject = "corpName")
    @BtCompositeSearch(fieldName = "b.loan_Opp")
    private String corpId;
    @BtCompositeSearch(fieldName = "b.loan_Opp")
    private String corpName;
    /** 贷款合同号 */
    @BtCompositeSearch(fieldName = "a.contract_Code")
    private String contractCode;
    /** 贷款发放单号 */
    @BtCompositeSearch(fieldName = "a.bill_Code")
    private String sendCode;
    /** 还款金额 */
    private BigDecimal repayMoney;
    /** 在途金额 */
    private BigDecimal passageMoney;
    /** 归还金额(在途和已还) */
    @BtCompositeSearch(fieldName = "a.money - d.sumMoney")
    private BigDecimal sumMoney;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "r.repay_Date")
    private java.util.Date repayDate;
    /** 还款利息 */
    private BigDecimal repayAccrual;
    /** 记账凭证号 */
    private String voucherNo;
    /** 记账流水号 */
    private String orderNo;
    /** 贷款账号 */
    private String loanAcc;
    /** 还款账号 */
    private String repayAcc;
    /** 支付方式 */
    private String payType;
    /** 附件名称 */
    private String annexName;
    /** 还款计划 */
    private String repaymentScheduleId;
    /** 还款方式 */
    @Dict(dictGroup = "Provide.RepayType", inject = "repaywayName")
    @BtCompositeSearch(fieldName = "a.repay_Type")
    private String repayway;
    @BtCompositeSearch(fieldName = "a.repay_Type")
    private String repaywayName;
    /** 备注 */
    private String rmk;
    /** 状态 */
    private Integer status;
    /** 业务状态 */
    @BtCompositeSearch(fieldName = "a.biz_status")
    @Dict(dictGroup = "cms.bizStatus",inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /** 创建人名称 */
    private String createName;
    /** 创建人登录名称 */
    private String createBy;
    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createDate;
    /** 更新人名称 */
    private String updateName;
    /** 更新人登录名称 */
    private String updateBy;
    /** 更新日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;
    /** 展期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date deferredRepayDate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 单据编号 */
    @BtCompositeSearch(fieldName = "b.bill_Code")
    private String infoBillCode;
    /** 期限 */
    private Integer term;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitStatus")
    private String ternUnit;
    private String ternUnitStatus;
    /** 贷款币种 */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 担保方式 */
    @Dict(dictGroup = "Cms.PlType.C", inject = "plName")
    @BtCompositeSearch(fieldName = "b.pl_Type")
    private String plType;
    private String plName;
//    @Corp(inject = "precativeName")
    @BtCompositeSearch(fieldName = "b.precative_Id")
    private String precativeId;
    /** 委托人单位名称 */
    @BtCompositeSearch(fieldName = "b.precative_Id")
    private String precativeName;
    /** 发放日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.send_Date")
    private java.util.Date sendDate;
    /** 到期日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.deadline_Date")
    private java.util.Date deadlineDate;
    /** 借款人 */
//    @Corp(inject = "loanOppName")
    @BtCompositeSearch(fieldName = "b.loan_Opp")
    private String loanOpp;
    @BtCompositeSearch(fieldName = "b.loan_Opp")
    private String loanOppName;
    /** 还款金额 */
    @BtCompositeSearch(fieldName = "a.money")
    private BigDecimal sendMoney;
    @BtCompositeSearch(fieldName = "a.repay_Type")
    private String repayType;
    @BtCompositeSearch(fieldName = "b.loan_Send_Out_Type")
    @Dict(dictGroup = "cms.loan.loanSendOutType",inject = "loanSendOutTypeName")
    private String loanSendOutType;
    private String loanSendOutTypeName;
    @BtCompositeSearch(fieldName = "a.interest_Way")
    @Dict(dictGroup = "Admin.PredrawSign",inject = "interestWayName")
    private String interestWay;
    private String interestWayName;
    /** 记账标志 */
    @BtCompositeSearch(fieldName = "r.tally_sign")
    private String tallySign;
    private BigDecimal total;
    /** 记账员代码 */
    private String tallyCode;
    private String purpose;
    // 资金计划参数
    private String[] itemCode;
    private String[] itemName;
    private BigDecimal[] processMoney;
    /** 汇率 */
    private BigDecimal[] exchangeRate;
    /** 执行金额（折合本身币别） */
    private BigDecimal[] processCurMoney;

    private String  saveType;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
    }
    public BigDecimal getPassageMoney() {
        return passageMoney;
    }
    public void setPassageMoney(BigDecimal passageMoney) {
        this.passageMoney = passageMoney;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
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
    public String getRepayAcc() {
        return repayAcc;
    }
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public String getAnnexName() {
        return annexName;
    }
    public void setAnnexName(String annexName) {
        this.annexName = annexName;
    }
    public String getRepaymentScheduleId() {
        return repaymentScheduleId;
    }
    public void setRepaymentScheduleId(String repaymentScheduleId) {
        this.repaymentScheduleId = repaymentScheduleId;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
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
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
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
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public String getTernUnitStatus() {
        return ternUnitStatus;
    }
    public void setTernUnitStatus(String ternUnitStatus) {
        this.ternUnitStatus = ternUnitStatus;
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
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getPlName() {
        return plName;
    }
    public void setPlName(String plName) {
        this.plName = plName;
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
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getRepaywayName() {
        return repaywayName;
    }
    public void setRepaywayName(String repaywayName) {
        this.repaywayName = repaywayName;
    }
    public String getBusinessKey() {
        return id;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public java.util.Date getDeferredRepayDate() {
        return deferredRepayDate;
    }
    public void setDeferredRepayDate(java.util.Date deferredRepayDate) {
        this.deferredRepayDate = deferredRepayDate;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public String getSendId() {
        return sendId;
    }
    public void setSendId(String sendId) {
        this.sendId = sendId;
    }
    public BigDecimal getSumMoney() {
        return sumMoney;
    }
    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    public BigDecimal getDeferredRate() {
        return deferredRate;
    }
    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
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
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
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
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
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

    public String getBizStatusName() {
        return bizStatusName;
    }

    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }

    public String getLoanSendOutTypeName() {
        return loanSendOutTypeName;
    }

    public void setLoanSendOutTypeName(String loanSendOutTypeName) {
        this.loanSendOutTypeName = loanSendOutTypeName;
    }

    public String getInterestWayName() {
        return interestWayName;
    }

    public void setInterestWayName(String interestWayName) {
        this.interestWayName = interestWayName;
    }

    public String getSaveType() {
        return saveType;
    }

    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
}
