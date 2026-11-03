package com.bytter.biz.reconcile.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 银行对账单Entity
 *
 * @author huangx 2022-12-01
 */
@Entity
@Table(name = "BIS_BANK_MONTH_STATEMENT_DTL", schema = "")
@SuppressWarnings("serial")
public class BisBankMonthStatementDtl extends IdEntity implements java.io.Serializable {
    /**
     * 客户名称
     */
    private String customerName;
    /**
     * 客户账号
     */
    private String customerAccount;
    /**
     * 账单统计开始日期
     */
    private Date billBenginDate;
    /**
     * 账单统计结束日期
     */
    private Date billEndDate;
    /**
     * 期末余额
     */
    private BigDecimal closingBalance;
    /**
     * 汇总交易笔数
     */
    private Integer totalNumber;
    /**
     * 汇总借方发生额
     */
    private BigDecimal totalDebitAmt;
    /**
     * 汇总贷方发生额
     */
    private BigDecimal totalCreditAmt;
    /**
     * 银行代码ID
     */
    private String bifCodeId;
    /**
     * 币种
     */
    private String curId;
    /**
     * 账单保存类型
     */
    private String saveType;
    /**
     * 账单文件路径
     */
    private String filePath;
    /**
     * 账单文件类型:pdf,img,jpg
     */
    private String fileType;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备用字段1
     */
    private String reserve1;
    /**
     * 备用字段2
     */
    private String reserve2;
    /**
     * 备用字段3
     */
    private String reserve3;
    /**
     * 备用字段4
     */
    private String reserve4;
    /**
     * 备用字段5
     */
    private String reserve5;
    @Column(name ="CUSTOMER_NAME")
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    @Column(name ="CUSTOMER_ACCOUNT")
    public String getCustomerAccount() {
        return customerAccount;
    }
    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }
    @Column(name ="BILL_BENGIN_DATE")
    public Date getBillBenginDate() {
        return billBenginDate;
    }
    public void setBillBenginDate(Date billBenginDate) {
        this.billBenginDate = billBenginDate;
    }
    @Column(name ="BILL_END_DATE")
    public Date getBillEndDate() {
        return billEndDate;
    }
    public void setBillEndDate(Date billEndDate) {
        this.billEndDate = billEndDate;
    }
    @Column(name ="CLOSING_BALANCE")
    public BigDecimal getClosingBalance() {
        return closingBalance;
    }
    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
    @Column(name ="TOTAL_NUMBER")
    public Integer getTotalNumber() {
        return totalNumber;
    }
    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }
    @Column(name ="TOTAL_DEBIT_AMT")
    public BigDecimal getTotalDebitAmt() {
        return totalDebitAmt;
    }
    public void setTotalDebitAmt(BigDecimal totalDebitAmt) {
        this.totalDebitAmt = totalDebitAmt;
    }
    @Column(name ="TOTAL_CREDIT_AMT")
    public BigDecimal getTotalCreditAmt() {
        return totalCreditAmt;
    }
    public void setTotalCreditAmt(BigDecimal totalCreditAmt) {
        this.totalCreditAmt = totalCreditAmt;
    }
    @Column(name ="BIF_CODE_ID")
    public String getBifCodeId() {
        return bifCodeId;
    }
    public void setBifCodeId(String bifCodeId) {
        this.bifCodeId = bifCodeId;
    }
    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    @Column(name ="SAVE_TYPE")
    public String getSaveType() {
        return saveType;
    }
    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
    @Column(name ="FILE_PATH")
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    @Column(name ="FILE_TYPE")
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    @Column(name ="CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    @Column(name ="RESERVE1")
    public String getReserve1() {
        return reserve1;
    }
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }
    @Column(name ="RESERVE2")
    public String getReserve2() {
        return reserve2;
    }
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }
    @Column(name ="RESERVE3")
    public String getReserve3() {
        return reserve3;
    }
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
    @Column(name ="RESERVE4")
    public String getReserve4() {
        return reserve4;
    }
    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }
    @Column(name ="RESERVE5")
    public String getReserve5() {
        return reserve5;
    }
    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }
    public BisBankMonthStatementDtl(String customerName, String customerAccount, Date billBenginDate, Date billEndDate, BigDecimal closingBalance, Integer totalNumber, BigDecimal totalDebitAmt, BigDecimal totalCreditAmt, String bifCodeId, String curId, String saveType, String filePath, String fileType, Date createTime, String reserve1, String reserve2, String reserve3, String reserve4, String reserve5) {
        this.customerName = customerName;
        this.customerAccount = customerAccount;
        this.billBenginDate = billBenginDate;
        this.billEndDate = billEndDate;
        this.closingBalance = closingBalance;
        this.totalNumber = totalNumber;
        this.totalDebitAmt = totalDebitAmt;
        this.totalCreditAmt = totalCreditAmt;
        this.bifCodeId = bifCodeId;
        this.curId = curId;
        this.saveType = saveType;
        this.filePath = filePath;
        this.fileType = fileType;
        this.createTime = createTime;
        this.reserve1 = reserve1;
        this.reserve2 = reserve2;
        this.reserve3 = reserve3;
        this.reserve4 = reserve4;
        this.reserve5 = reserve5;
    }
}
