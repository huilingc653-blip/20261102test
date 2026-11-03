package com.bytter.report.domain.cms.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class InterestReportVO implements Serializable {
    /**
     * 银行借款主键
     */
    private String id;
    /**
     *单位
     */
    @BtCompositeSearch(fieldName = "send.CORP_ID")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**
     * 申请银行
     */
    @BtCompositeSearch(fieldName = "bankinfo.BANK_NAME")
    private String bankName;
    /**
     *申请单号
     */
    @BtCompositeSearch(fieldName = "info.BILL_CODE")
    private String infoBillNo;
    /**
     *币种
     */
    @BtCompositeSearch(fieldName = "interest.CUR_ID")
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**
     *发放单号
     */
    @BtCompositeSearch(fieldName = "send.bill_code")
    private String sendBillNo;
    /**
     *发放金额
     */
    private BigDecimal sendMoney;
    /**
     *融资余额
     */
    private BigDecimal financeBalance;
    /**
     *发放日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     *还款日期
     */
    @BtCompositeSearch(fieldName = "interest.SIGN_REPAY_DATE")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**
     *应付本金
     */
    private BigDecimal repayMoney;
    /**
     *应付利息
     */
    private BigDecimal repayInterest;
    /**
     *应付合计
     */
    private BigDecimal repayTotal;

    private Date endDate;
    @BtCompositeSearch(fieldName = "info.pl_class")
    private String plClass;
    private String plClassName;
    private BigDecimal plRate;
    private String ternUnit;
    private BigDecimal plTerm;
    private String plPurpose;

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
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getInfoBillNo() {
        return infoBillNo;
    }
    public void setInfoBillNo(String infoBillNo) {
        this.infoBillNo = infoBillNo;
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
    public String getSendBillNo() {
        return sendBillNo;
    }
    public void setSendBillNo(String sendBillNo) {
        this.sendBillNo = sendBillNo;
    }
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getFinanceBalance() {
        return financeBalance;
    }
    public void setFinanceBalance(BigDecimal financeBalance) {
        this.financeBalance = financeBalance;
    }
    public Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
    public Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRepayInterest() {
        return repayInterest;
    }
    public void setRepayInterest(BigDecimal repayInterest) {
        this.repayInterest = repayInterest;
    }
    public BigDecimal getRepayTotal() {
        return repayTotal;
    }
    public void setRepayTotal(BigDecimal repayTotal) {
        this.repayTotal = repayTotal;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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

    public BigDecimal getPlRate() {
        return plRate;
    }

    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }

    public String getTernUnit() {
        return ternUnit;
    }

    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }

    public BigDecimal getPlTerm() {
        return plTerm;
    }

    public void setPlTerm(BigDecimal plTerm) {
        this.plTerm = plTerm;
    }

    public String getPlPurpose() {
        return plPurpose;
    }

    public void setPlPurpose(String plPurpose) {
        this.plPurpose = plPurpose;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
