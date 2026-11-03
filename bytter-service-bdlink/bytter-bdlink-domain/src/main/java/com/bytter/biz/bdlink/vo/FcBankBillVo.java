package com.bytter.biz.bdlink.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: FC_BANK_BILL
 * @date 2017-06-02 10:04:16
 */
public class FcBankBillVo implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String id;
    /**
     * 记账日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.tally_date")
    private java.util.Date tallyDate;
    /**
     * 账号
     */
    private String account;
    /**
     * 账号币别
     */
    @Currency(inject = "curName")
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curId;
    private String curName;
    /**
     * 银行账号
     */
    @BtCompositeSearch(fieldName = "a.bank_acc")
    private String bankAcc;
    private String bankName;
    @BtCompositeSearch(fieldName = "a.opp_Acc_No")
    private String oppAccNo;
    @BtCompositeSearch(fieldName = "a.opp_Acc_Name")
    private String oppAccName;
    /**
     * 银行账号ID
     */
    @BtCompositeSearch(fieldName = "a.bank_acc.id")
    private String bankAccId;
    /**
     * 借方发生额
     */
    @BtCompositeSearch(fieldName = "a.debit_money")
    private BigDecimal debitMoney;
    /**
     * 贷方发生额
     */
    @BtCompositeSearch(fieldName = "a.credit_money")
    private BigDecimal creditMoney;
    /**
     * 余额
     */
    private BigDecimal balance;
    /**
     * 红蓝标志
     */
    @BtCompositeSearch(fieldName = "a.redblue_sign")
    private String redblueSign;
    /**
     * 对账标志
     */
    @BtCompositeSearch(fieldName = "a.compare_sign")
    private String compareSign;
    /**
     * 单位
     **/
    private String corpId;
    @BtCompositeSearch(fieldName = "corp.name")
    private String corpName;
    @BtCompositeSearch(fieldName = "corp.code")
    private String corpCode;
    private String codeName;
    private String bankTypeName;
    @BtCompositeSearch(fieldName = "bankType.id")
    private String bankTypeId;
    /**
     * 序号
     */
    private String bankId;
    /**
     * 票据号
     */
    private String billNo;
    /**
     * 调节日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date adjustDate;
    /**
     * 输入日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date importDate;
    /**
     * 输入类型
     */
    private String importType;
    /**
     * 行号
     */
    private BigDecimal lineNo;
    /**
     * 摘要
     */
    private String summaryContent;
    /**
     * ERP凭证号
     */
    private String erpVoucherNo;
    /**
     * 凭证号
     */
    @BtCompositeSearch(fieldName = "a.voucher_no")
    private String voucherNo;
    /**
     * 收款账号
     */
    private String payeeAcc;
    /**
     * 交易时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "a.trans_time")
    private java.util.Date transTime;
    /**
     * 结束交易时间
     */
    private java.util.Date ledTransTime;
    /**
     * 起始交易时间
     */
    private java.util.Date gedTransTime;
    /**
     * 交易时间
     */
    public java.util.Date getTransTime() {
        return transTime;
    }
    public void setTransTime(java.util.Date transTime) {
        this.transTime = transTime;
    }
    /**
     * 起始交易时间
     */
    public java.util.Date getLedTransTime() {
        return ledTransTime;
    }
    public void setLedTransTime(java.util.Date ledTransTime) {
        this.ledTransTime = ledTransTime;
    }
    /**
     * 结束交易时间
     */
    public java.util.Date getGedTransTime() {
        return gedTransTime;
    }
    public void setGedTransTime(java.util.Date gedTransTime) {
        this.gedTransTime = gedTransTime;
    }
    /**
     * 凭证号
     */
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    /**
     * 银行账号ID
     */
    public String getBankAccId() {
        return bankAccId;
    }
    public void setBankAccId(String bankAccId) {
        this.bankAccId = bankAccId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public java.util.Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(java.util.Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getBankAcc() {
        return this.bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public BigDecimal getDebitMoney() {
        return this.debitMoney;
    }
    public void setDebitMoney(BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal getCreditMoney() {
        return this.creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public BigDecimal getBalance() {
        return this.balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getRedblueSign() {
        return this.redblueSign;
    }
    public void setRedblueSign(String redblueSign) {
        this.redblueSign = redblueSign;
    }
    public String getCompareSign() {
        return this.compareSign;
    }
    public void setCompareSign(String compareSign) {
        this.compareSign = compareSign;
    }
    public String getBankId() {
        return this.bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }
    public String getBillNo() {
        return this.billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public java.util.Date getAdjustDate() {
        return this.adjustDate;
    }
    public void setAdjustDate(java.util.Date adjustDate) {
        this.adjustDate = adjustDate;
    }
    public java.util.Date getImportDate() {
        return this.importDate;
    }
    public void setImportDate(java.util.Date importDate) {
        this.importDate = importDate;
    }
    public String getImportType() {
        return this.importType;
    }
    public void setImportType(String importType) {
        this.importType = importType;
    }
    public BigDecimal getLineNo() {
        return this.lineNo;
    }
    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }
    public String getSummaryContent() {
        return this.summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public String getErpVoucherNo() {
        return this.erpVoucherNo;
    }
    public void setErpVoucherNo(String erpVoucherNo) {
        this.erpVoucherNo = erpVoucherNo;
    }
    public String getPayeeAcc() {
        return this.payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
    public String getCorpName() {
        return corpName;
    }
    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }
    public String getCorpCode() {
        return corpCode;
    }
    public void setCorpCode(String corpCode) {
        this.corpCode = corpCode;
    }
    public String getCodeName() {
        return codeName;
    }
    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }
    public String getBankTypeName() {
        return bankTypeName;
    }
    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }
    public String getBankTypeId() {
        return bankTypeId;
    }
    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
}
