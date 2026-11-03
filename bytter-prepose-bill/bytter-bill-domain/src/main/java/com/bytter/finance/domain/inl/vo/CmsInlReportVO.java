package com.bytter.finance.domain.inl.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
public class CmsInlReportVO implements Serializable {
    /**
     * 内部拆借申请单号
     */
    @BtCompositeSearch(fieldName = "info.bill_no")
    private String billNo;
    /**
     * 拆入单位
     */
    @BtCompositeSearch(fieldName = "info.borrow_in_corp")
    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @BtCompositeSearch(fieldName = "info.borrow_out_corp")
    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    private String borrowOutCorpName;
    /**
     * 拆借资金币种
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "info.cur_id")
    private String curId;
    private String curName;
    /**
     * 拆借金额
     */
    private String money;
    /**
     * 拆借日期
     */
    @BtCompositeSearch(fieldName = "info.start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @BtCompositeSearch(fieldName = "info.end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 拆借期限
     */
    private String term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 已放款金额
     */
    private String sendMoney;
    /**
     * 待放款金额
     */
    private String waitSendMoney;
    /**
     * 放款在途金额
     */
    @BtCompositeSearch(fieldName = "info.sendStatus")
    private String sendStatus;
    /**
     * 已还本金合计
     */
    private String repayMoneyTotal;
    /**
     * 已还利息合计
     */
    private String repayInterestTotal;
    /**
     * 还款状态
     */
    @BtCompositeSearch(fieldName = "info.repayStatus")
    private String repayStatus;
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
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
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
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
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public String getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(String sendMoney) {
        this.sendMoney = sendMoney;
    }
    public String getWaitSendMoney() {
        return waitSendMoney;
    }
    public void setWaitSendMoney(String waitSendMoney) {
        this.waitSendMoney = waitSendMoney;
    }
    public String getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }
    public String getRepayMoneyTotal() {
        return repayMoneyTotal;
    }
    public void setRepayMoneyTotal(String repayMoneyTotal) {
        this.repayMoneyTotal = repayMoneyTotal;
    }
    public String getRepayInterestTotal() {
        return repayInterestTotal;
    }
    public void setRepayInterestTotal(String repayInterestTotal) {
        this.repayInterestTotal = repayInterestTotal;
    }
    public String getRepayStatus() {
        return repayStatus;
    }
    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }
}
