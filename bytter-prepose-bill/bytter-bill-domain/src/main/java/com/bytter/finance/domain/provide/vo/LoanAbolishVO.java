package com.bytter.finance.domain.provide.vo;
import com.bytter.core.annotation.*;
import com.bytter.core.bpm.IBusinessKey;
import com.bytter.core.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class LoanAbolishVO implements Serializable, IBusinessKey {
    private String id;
    /** 单位代码 */
    @BtCompositeSearch(fieldName = "i.corp_id")
//    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    @BtCompositeSearch(fieldName = "i.bill_code")
    private String billCode;
    /** 借款合同号 */
    private String contractNo;
    /** 合同号期限 */
    @Dict(dictGroup = "Provide.ContractTerm", inject = "contractTermName")
    private String contractTerm;
    private String contractTermName;
    /** 借款金额 */
    @BtCompositeSearch(fieldName = "i.pl_money")
    private BigDecimal plMoney;
    /** 发放借款金额 */
    private BigDecimal money;
    /** 借款期限 */
    private Integer plTerm;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitName")
    private String ternUnit;
    private String ternUnitName;
    /** 借款币种 */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 借款银行类别 */
//    @BankType(inject = "bankTypeName")
    private String bankType;
    private String bankTypeName;
    /** 借款银行 */
    private String bankId;
    private String bankName;
    /** 借款银行账号 */
    private String plAccount;
    /** 借款日期 */
    @BtCompositeSearch(fieldName = "i.pl_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "i.repay_date")
    private Date repayDate;
    /** 还款方式 */
    @Dict(dictGroup = "Provide.RepayType", inject = "repayTypeName")
    private String repayType;
    private String repayTypeName;
    /** 新还款方式 */
    private String repayTypeName1;
    /** 还款方式说明 */
    private String repayTypeDesc;
    /** 借款种类 */
    private String plClass;
    private String plClassName;
    /** 贷款类型 */
    private String plGenre;
    /** 担保方式 */
    @Dict(dictGroup = "Cms.PlType.K", inject = "plTypeName")
    private String plType;
    private String plTypeName;
    /** 借款利率 */
    private BigDecimal plRate;
    /** 展期利率 */
    private BigDecimal deferredRate;
    /** 逾期利率 */
    private BigDecimal overdueRate;
    /** 状态 */
    @Dict(dictGroup = "cms.status", inject = "statusName")
    private Integer status;
    private String statusName;
    @BtCompositeSearch(fieldName = "a.biz_status")
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /** 被作废的单据状态 */
    private Integer abolishedStatus;
    /** 发放方式 */
    @Dict(dictGroup = "cms.loan.loanSendOutType", inject = "loanSendOutTypeName")
    private String loanSendOutType;
    private String loanSendOutTypeName;
    /** 跳过借款发放功能 */
    private String acrossSend;
    /** 浮动方式 */
    @Dict(dictGroup = "Cms.FloatWay", inject = "floatWayName")
    private String floatWay;
    private String floatWayName;
    /** 借款创建日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date infoCreateDate;
    /** 发放金额 **/
    public BigDecimal sendMoney;
    /** 发放币别 **/
//    @Currency(inject = "sendCurName")
    public String sendCurCode;
    public String sendCurName;
    /** 作废申请日期 **/
    @BtCompositeSearch(fieldName = "a.abolished_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    public Date abolishedDate;
    /** 调用合同id */
    private String conId;
    public String getBusinessKey() {
        return id;
    }
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
    public String getContractTermName() {
        return contractTermName;
    }
    public void setContractTermName(String contractTermName) {
        this.contractTermName = contractTermName;
    }
    public BigDecimal getPlMoney() {
        return plMoney;
    }
    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
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
    public String getRepayTypeName() {
        return StringUtils.isNotEmpty(repayTypeName) ? repayTypeName : repayTypeName1;
    }
    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
    }
    public String getRepayTypeName1() {
        return repayTypeName1;
    }
    public void setRepayTypeName1(String repayTypeName1) {
        this.repayTypeName1 = repayTypeName1;
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
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getStatusName() {
        return statusName;
    }
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
    public String getLoanSendOutType() {
        return loanSendOutType;
    }
    public void setLoanSendOutType(String loanSendOutType) {
        this.loanSendOutType = loanSendOutType;
    }
    public String getLoanSendOutTypeName() {
        return loanSendOutTypeName;
    }
    public void setLoanSendOutTypeName(String loanSendOutTypeName) {
        this.loanSendOutTypeName = loanSendOutTypeName;
    }
    public String getAcrossSend() {
        return acrossSend;
    }
    public void setAcrossSend(String acrossSend) {
        this.acrossSend = acrossSend;
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
    public Date getInfoCreateDate() {
        return infoCreateDate;
    }
    public void setInfoCreateDate(Date infoCreateDate) {
        this.infoCreateDate = infoCreateDate;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public String getSendCurCode() {
        return sendCurCode;
    }
    public void setSendCurCode(String sendCurCode) {
        this.sendCurCode = sendCurCode;
    }
    public String getSendCurName() {
        return sendCurName;
    }
    public void setSendCurName(String sendCurName) {
        this.sendCurName = sendCurName;
    }
    public Date getAbolishedDate() {
        return abolishedDate;
    }
    public void setAbolishedDate(Date abolishedDate) {
        this.abolishedDate = abolishedDate;
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
    public Integer getAbolishedStatus() {
        return abolishedStatus;
    }
    public void setAbolishedStatus(Integer abolishedStatus) {
        this.abolishedStatus = abolishedStatus;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
}
