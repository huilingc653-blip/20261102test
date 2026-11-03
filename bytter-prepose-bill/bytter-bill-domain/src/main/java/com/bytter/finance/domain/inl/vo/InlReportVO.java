package com.bytter.finance.domain.inl.vo;
import com.bytter.finance.constant.InlConstant;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
public class InlReportVO extends ExcelModel {
    /**
     * 主键id
     */
    private String id;
    /**
     * 内部拆借流水号
     */
    private String billNo;
    /**
     * 拆入单位
     */
    @Corp(inject = "borrowInCorpName")
    private String borrowInCorp;
    @Excel(name = "*拆入单位", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR1)
    private String borrowInCorpName;
    /**
     * 拆出单位
     */
    @Corp(inject = "borrowOutCorpName")
    private String borrowOutCorp;
    @Excel(name = "*拆出单位", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR2)
    private String borrowOutCorpName;
    /**
     * 拆借资金币种
     */
    @Currency(inject = "curName")
    private String curId;
    @Excel(name = "*币种", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR3)
    private String curName;
    /**
     * 总拆借金额
     */
    @Excel(name = "*总拆借金额", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR4)
    private BigDecimal money;
    /**
     * 拆借日期
     */
    @Excel(name = "*拆借日期", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR5)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日期
     */
    @Excel(name = "*协议到期日期", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR6)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 期限
     */
    private Integer term;
    /**
     * 期限单位
     */
    private String unit;
    /**
     * 已放款金额
     */
    @Excel(name = "*已放款金额", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR7)
    private BigDecimal sendMoney;
    /**
     * 待放款金额
     */
    private BigDecimal leftSendMoney;
    /**
     * 放款状态
     */
    private Integer sendStatus;
    /**
     * 已还本金金额
     */
    @Excel(name = "*已还本金金额", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR8)
    private BigDecimal repayMoney;
    /**
     * 已还利息
     */
    @Excel(name = "*已还利息金额", isImportField = "true")
    @NotNull(message = InlConstant.CMS_INL_REPORT_IMPORT_ERROR9)
    private BigDecimal repayInterest;
    /**
     * 还款状态
     */
    private Integer repayStatus;
    /**
     * 剩余还款本金
     */
    private BigDecimal leftRepayMoeny;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
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
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
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
    public BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public BigDecimal getLeftSendMoney() {
        return leftSendMoney;
    }
    public void setLeftSendMoney(BigDecimal leftSendMoney) {
        this.leftSendMoney = leftSendMoney;
    }
    public Integer getSendStatus() {
        return sendStatus;
    }
    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
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
    public Integer getRepayStatus() {
        return repayStatus;
    }
    public void setRepayStatus(Integer repayStatus) {
        this.repayStatus = repayStatus;
    }
    public BigDecimal getLeftRepayMoeny() {
        return leftRepayMoeny;
    }
    public void setLeftRepayMoeny(BigDecimal leftRepayMoeny) {
        this.leftRepayMoeny = leftRepayMoeny;
    }
    @Override
    public String getBeanName() {
        return "InlReportService";
    }
}
