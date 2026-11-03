package com.bytter.finance.domain.loan.vo;
import java.io.Serializable;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsLoanSendTallyQueryVO implements Serializable {
    private String id;
    /** 结算中心代码 */
    @Corp(inject = "netName")
    @BtCompositeSearch(fieldName = "i.net_code")
    private String netCode;
    private String netName;
    /** 单位代码 */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 贷款流水号 */
    @BtCompositeSearch(fieldName = "i.bill_code")
    private String infoBillCode;
    @BtCompositeSearch(fieldName = "s.bill_Code")
    private String sendBillCode;
    /** 放款合同号 */
    @BtCompositeSearch(fieldName = "s.contract_code")
    private String contractCode;
    /** 状态 */
    private Integer status;
    private String bizStatus;
    /** 放款金额 */
    @BtCompositeSearch(fieldName = "s.loan_money")
    private java.math.BigDecimal sendMoney;
    /** 贷款期限 */
    private Integer loanTerm;
    /** 期限单位 */
    private String ternUnit;
    /** 放款币种 */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "i.cur_code")
    private String curCode;
    private String curName;
    /** 放款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date loanDate;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date repayDate;
    /** 支付方式 */
    private String payType;
    /** 放款利率 */
    private java.math.BigDecimal loanRate;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 展期利率 */
    private java.math.BigDecimal deferredRate;
    /** 贷款账号 */
    private String loanAcc;
    /** 还款方式 */
    private String repayType;
    private String repayTypeName;
    /** 贷款类型 */
    private String loanGenre;
    /** 贷款类型名称 */
    private String loanGenreName;
    /** 展期到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date deferredDate;
    /** 用途 */
    private String purpose;
    /** 放款账号 **/
    private String provideLoanAcc;
    /** 计息方式 **/
    private String interestWay;

    public String getRepayTypeName() {
        return repayTypeName;
    }

    public void setRepayTypeName(String repayTypeName) {
        this.repayTypeName = repayTypeName;
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
    public String getInfoBillCode() {
        return infoBillCode;
    }
    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }
    public String getSendBillCode() {
        return sendBillCode;
    }
    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public java.math.BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(java.math.BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
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
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
    public java.math.BigDecimal getLoanRate() {
        return loanRate;
    }
    public void setLoanRate(java.math.BigDecimal loanRate) {
        this.loanRate = loanRate;
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
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
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
    public java.util.Date getDeferredDate() {
        return deferredDate;
    }
    public void setDeferredDate(java.util.Date deferredDate) {
        this.deferredDate = deferredDate;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getProvideLoanAcc() {
        return provideLoanAcc;
    }
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
    }
    public String getInterestWay() {
        return interestWay;
    }
    public void setInterestWay(String interestWay) {
        this.interestWay = interestWay;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
