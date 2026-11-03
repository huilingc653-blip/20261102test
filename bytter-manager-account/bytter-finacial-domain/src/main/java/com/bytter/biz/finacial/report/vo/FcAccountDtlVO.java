package com.bytter.biz.finacial.report.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
/**
 * @author luxy
 * @Title: FcAccountDtlVO
 * @ProjectName T3.3
 * @Description: TODO
 * @date 2020/7/1 18:03
 */

@Data
public class FcAccountDtlVO  {
    /**网点 */
    private java.lang.String netId;
    /**记账日期**/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /**起息日期*/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /**记账凭证号*/
    private java.lang.String voucherNo;
    /**凭证种类*/
    private String voucherType;
    private String voucherName;
    /**单据号*/
    private String billNo;
    /**摘要*/
    private java.lang.String summaryContent;
    /**借方发生额*/
    private java.math.BigDecimal debitMoney;
    /**贷方发生额*/
    private java.math.BigDecimal creditMoney;
    /**余额*/
    private java.math.BigDecimal balance;
    /**账号*/
    private java.lang.String payeeAcc;
    /**
     * 币别代码
     */
    private String  curCode;
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public String getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public BigDecimal getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

}
