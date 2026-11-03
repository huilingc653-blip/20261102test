package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Currency;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class CmsInterestPayVO implements Serializable {
    
    private String corpId;
    /**单位名称**/
    private String corpName;
    /**申请单号**/
    private String billCode;
    /**币别**/
    @Currency(inject = "curName")
    private String curCode;
    /**发放单号**/
    private String sendBillCode;
    /**发放金额**/
    private BigDecimal sendMoney;
    /**发放日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**到期日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**发放次数**/
    private BigDecimal sendTime;
    /**放款利率**/
    private BigDecimal loanRate;
    /**预计还款日**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date prediceRepayDate;
    /**归还标识*/
    private String isRepay;
    /**剩余本金*/
    private BigDecimal surplusMoney;
    /**业务名称**/
    private String cmsType;
    /**应付利息*/
    private BigDecimal yfInterest;
    /**实付利息**/
    private BigDecimal sfInterest;
    /**还款时应付利息**/
    private BigDecimal repaymentIst;
    /**还款时实付利息**/
    private BigDecimal repaymentRelIst;
    /**查询开始日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date searchStartDate;
    /**查询结束日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date searchEndDate;
    /**还款金额**/
    private BigDecimal repayMoney;
    /**展期到期日**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredDate;
    /**展期利率**/
    private BigDecimal deferredRate;
    /**开户行名称**/
    private String bankName;
    /**币别**/
    private String curName;
    /**起息日期--借款**/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date interestStartDate;
    /**止息日期--借款**/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date blnInstDate;
    /**计息金额**/
    private BigDecimal bearingMoney;
    private String sendId;
    /**逾期利率**/
    private BigDecimal overdueRate;
    /**融资类型**/
    private String cmsLoanType;

    public String getCmsLoanType() {
        return cmsLoanType;
    }

    public void setCmsLoanType(String cmsLoanType) {
        this.cmsLoanType = cmsLoanType;
    }

    public BigDecimal getOverdueRate() {
        return overdueRate;
    }

    public void setOverdueRate(BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public BigDecimal getBearingMoney() {
        return bearingMoney;
    }

    public void setBearingMoney(BigDecimal bearingMoney) {
        this.bearingMoney = bearingMoney;
    }

    public Date getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public Date getBlnInstDate() {
        return blnInstDate;
    }

    public void setBlnInstDate(Date blnInstDate) {
        this.blnInstDate = blnInstDate;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Date getDeferredDate() {
        return deferredDate;
    }

    public void setDeferredDate(Date deferredDate) {
        this.deferredDate = deferredDate;
    }

    public BigDecimal getDeferredRate() {
        return deferredRate;
    }

    public void setDeferredRate(BigDecimal deferredRate) {
        this.deferredRate = deferredRate;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public Date getSearchStartDate() {
        return searchStartDate;
    }

    public void setSearchStartDate(Date searchStartDate) {
        this.searchStartDate = searchStartDate;
    }

    public Date getSearchEndDate() {
        return searchEndDate;
    }

    public void setSearchEndDate(Date searchEndDate) {
        this.searchEndDate = searchEndDate;
    }

    public BigDecimal getRepaymentIst() {
        return repaymentIst;
    }
    public void setRepaymentIst(BigDecimal repaymentIst) {
        this.repaymentIst = repaymentIst;
    }
    public BigDecimal getYfInterest() {
        return yfInterest;
    }

    public void setYfInterest(BigDecimal yfInterest) {
        this.yfInterest = yfInterest;
    }

    public BigDecimal getSfInterest() {
        return sfInterest;
    }

    public void setSfInterest(BigDecimal sfInterest) {
        this.sfInterest = sfInterest;
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

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
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

    public BigDecimal getSendTime() {
        return sendTime;
    }

    public void setSendTime(BigDecimal sendTime) {
        this.sendTime = sendTime;
    }

    public BigDecimal getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }

    public Date getPrediceRepayDate() {
        return prediceRepayDate;
    }

    public void setPrediceRepayDate(Date prediceRepayDate) {
        this.prediceRepayDate = prediceRepayDate;
    }

    public String getIsRepay() {
        return isRepay;
    }

    public void setIsRepay(String isRepay) {
        this.isRepay = isRepay;
    }

    public BigDecimal getSurplusMoney() {
        return surplusMoney;
    }

    public void setSurplusMoney(BigDecimal surplusMoney) {
        this.surplusMoney = surplusMoney;
    }

    public String getCmsType() {
        return cmsType;
    }

    public void setCmsType(String cmsType) {
        this.cmsType = cmsType;
    }

    public BigDecimal getRepaymentRelIst() {
        return repaymentRelIst;
    }

    public void setRepaymentRelIst(BigDecimal repaymentRelIst) {
        this.repaymentRelIst = repaymentRelIst;
    }
}
