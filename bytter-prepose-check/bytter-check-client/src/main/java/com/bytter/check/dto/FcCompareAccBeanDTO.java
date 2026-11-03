package com.bytter.check.dto;
import com.bytter.check.constant.FinacialConstant;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 中心对账单Bean
 * @author YonJar
 * @date 2023/4/21.
 * @description
 */
public class FcCompareAccBeanDTO implements Serializable {
    private static final long serialVersionUID = 3121751537426844315L;

    private String id;
    /** 对账类型 */
    private String checkType = FinacialConstant.CHECK_TYPE_CEN;
    /** 对账标志 */
    private String compareSign = FinacialConstant.COMPARE_SIGN_NO;
    /** 币别代码 */
    private String curId;
    /** 企业账号 */
    private String compAcc;
    /** 流水号 */
    private String orderNo;
    /** 凭证号 */
    private String voucherNo;
    /** 记账日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;
    /** 起息日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createTime;
    /** 票据号 */
    private String billNo;
    /** 行号 */
    private BigDecimal lineNo;
    /** 摘要 */
    private String summaryContent;
    /** 红蓝标志 */
    private String redblueSign;
    /** 借方发生额 */
    private BigDecimal debitMoney;
    /** 贷方发生额 */
    private BigDecimal creditMoney;
    /** 余额 */
    private BigDecimal balance;
    /** 输入类型 */
    private String importType = FinacialConstant.IMPORT_TYPE_INTERFACE;
    public String getCheckType() {
        return checkType;
    }
    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }
    public String getCompareSign() {
        return compareSign;
    }
    public void setCompareSign(String compareSign) {
        this.compareSign = compareSign;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCompAcc() {
        return compAcc;
    }
    public void setCompAcc(String compAcc) {
        this.compAcc = compAcc;
    }
    public String getOrderNo() {
        return orderNo;
    }
    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
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
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    public BigDecimal getLineNo() {
        return lineNo;
    }
    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }
    public String getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public String getRedblueSign() {
        return redblueSign;
    }
    public void setRedblueSign(String redblueSign) {
        this.redblueSign = redblueSign;
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
    public String getImportType() {
        return importType;
    }
    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
