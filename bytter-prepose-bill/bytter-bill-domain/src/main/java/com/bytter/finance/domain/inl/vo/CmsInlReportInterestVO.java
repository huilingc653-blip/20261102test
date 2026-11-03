package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
public class CmsInlReportInterestVO implements Serializable {
    /**
     * 内部拆借申请单号
     */
    @BtCompositeSearch(fieldName = "send.infoBillNo")
    private String infoBillNo;
    /**
     * 内部拆借放款单号
     */
    private String billNo;
    /**
     * 拆入单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_in_corp")
    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @BtCompositeSearch(fieldName = "send.borrow_out_corp")
    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 拆借资金币种
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "send.cur_id")
    private String curId;
    private String curName;
    /**
     * 拆借金额
     */
    private BigDecimal sendMoney;
    /**
     * 发放编码
     */
    @BtCompositeSearch(fieldName = "send.send_code")
    private String sendCode;
    /**
     * 发放日期
     */
    @BtCompositeSearch(fieldName = "send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "send.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 还款方式
     */
    @BtCompositeSearch(fieldName = "send.repay_type")
    private String repayType;
    private String repayTypeName;
    /**
     * 展期日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date deferredEndDate;
    /**
     * 已还本金合计
     */
    private BigDecimal repayMoneyTotal;
    /**
     * 已还利息合计
     */
    private BigDecimal repayInterestTotal;
    /**
     * 剩余还款本金
     */
    private BigDecimal leftMoney;
    /**
     * 剩余还款本金
     */
    private BigDecimal leftInterest;
    /**
     * 所有利息
     */
    private BigDecimal interestTotal;
    /**
     * 下一次还款日期
     */
    @BtCompositeSearch(fieldName = "send.nextRepayDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date nextRepayDate;
    /**
     * 下一次还款本金
     */
    private BigDecimal nextRepayMoney;
    /**
     * 下次还款利息
     */
    private BigDecimal nextRepayInterest;
    /**
     * 下次还款合计
     */
    private BigDecimal nextRepayTotal;
    public String getInfoBillNo() {
        return infoBillNo;
    }
    public void setInfoBillNo(String infoBillNo) {
        this.infoBillNo = infoBillNo;
    }
    public String getBorrowInCorp() {
        return borrowInCorp;
    }
    public void setBorrowInCorp(String borrowInCorp) {
        this.borrowInCorp = borrowInCorp;
    }
    public String getBorrowInCorpName() {
        return borrowInCorpName;
    }
    public void setBorrowInCorpName(String borrowInCorpName) {
        this.borrowInCorpName = borrowInCorpName;
    }
    public String getBorrowOutCorp() {
        return borrowOutCorp;
    }
    public void setBorrowOutCorp(String borrowOutCorp) {
        this.borrowOutCorp = borrowOutCorp;
    }
    public String getBorrowOutCorpName() {
        return borrowOutCorpName;
    }
    public void setBorrowOutCorpName(String borrowOutCorpName) {
        this.borrowOutCorpName = borrowOutCorpName;
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
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getRepayMoneyTotal() {
        return repayMoneyTotal;
    }
    public void setRepayMoneyTotal(BigDecimal repayMoneyTotal) {
        this.repayMoneyTotal = repayMoneyTotal;
    }
    public BigDecimal getRepayInterestTotal() {
        return repayInterestTotal;
    }
    public void setRepayInterestTotal(BigDecimal repayInterestTotal) {
        this.repayInterestTotal = repayInterestTotal;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public BigDecimal getLeftInterest() {
        return leftInterest;
    }
    public void setLeftInterest(BigDecimal leftInterest) {
        this.leftInterest = leftInterest;
    }
    public Date getNextRepayDate() {
        return nextRepayDate;
    }
    public void setNextRepayDate(Date nextRepayDate) {
        this.nextRepayDate = nextRepayDate;
    }
    public BigDecimal getNextRepayMoney() {
        return nextRepayMoney;
    }
    public void setNextRepayMoney(BigDecimal nextRepayMoney) {
        this.nextRepayMoney = nextRepayMoney;
    }
    public BigDecimal getNextRepayInterest() {
        return nextRepayInterest;
    }
    public void setNextRepayInterest(BigDecimal nextRepayInterest) {
        this.nextRepayInterest = nextRepayInterest;
    }
    public BigDecimal getNextRepayTotal() {
        return nextRepayTotal;
    }
    public void setNextRepayTotal(BigDecimal nextRepayTotal) {
        this.nextRepayTotal = nextRepayTotal;
    }
    public String getSendCode() {
        return sendCode;
    }
    public void setSendCode(String sendCode) {
        this.sendCode = sendCode;
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
    public Date getDeferredEndDate() {
        return deferredEndDate;
    }
    public void setDeferredEndDate(Date deferredEndDate) {
        this.deferredEndDate = deferredEndDate;
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

    public BigDecimal getInterestTotal() {
        return interestTotal;
    }
    public void setInterestTotal(BigDecimal interestTotal) {
        this.interestTotal = interestTotal;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
}
