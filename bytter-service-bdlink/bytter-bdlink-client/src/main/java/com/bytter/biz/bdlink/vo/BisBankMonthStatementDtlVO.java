package com.bytter.biz.bdlink.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 银行对账单VO
 *
 * @author huangx 2022-12-01
 */
@SuppressWarnings("serial")
public class BisBankMonthStatementDtlVO implements java.io.Serializable {
    private String id;
    /**
     * 开户单位ID
     */
    @Corp(inject="corpName")
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpId;
    @BtCompositeSearch(fieldName = "t.corp_id")
    private String corpName;
    /**
     * 客户名称
     */
    @BtCompositeSearch(fieldName = "t.customer_Name")
    private String customerName;
    /**
     * 客户账号
     */
    @BtCompositeSearch(fieldName = "t.customer_Account")
    private String customerAccount;
    @BtCompositeSearch(fieldName = "acc.acc_Name")
    private String customerAccountName;
    /**
     * 账单统计开始日期
     */
    @BtCompositeSearch(fieldName = "t.bill_Bengin_Date")
    private Date billBenginDate;
    /**
     * 账单统计结束日期
     */
    @BtCompositeSearch(fieldName = "t.bill_End_Date")
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
    @BtCompositeSearch(fieldName = "t.bif_Code_Id")
    private String bifCodeId;
    private String bifName;
    /**
     * 币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
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
    /**
     * 账单文件大字段信息
     */
    private byte[] statementImg;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
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
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public String getCustomerAccount() {
        return customerAccount;
    }
    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }
    public String getCustomerAccountName() {
        return customerAccountName;
    }
    public void setCustomerAccountName(String customerAccountName) {
        this.customerAccountName = customerAccountName;
    }
    public Date getBillBenginDate() {
        return billBenginDate;
    }
    public void setBillBenginDate(Date billBenginDate) {
        this.billBenginDate = billBenginDate;
    }
    public Date getBillEndDate() {
        return billEndDate;
    }
    public void setBillEndDate(Date billEndDate) {
        this.billEndDate = billEndDate;
    }
    public BigDecimal getClosingBalance() {
        return closingBalance;
    }
    public void setClosingBalance(BigDecimal closingBalance) {
        this.closingBalance = closingBalance;
    }
    public Integer getTotalNumber() {
        return totalNumber;
    }
    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }
    public BigDecimal getTotalDebitAmt() {
        return totalDebitAmt;
    }
    public void setTotalDebitAmt(BigDecimal totalDebitAmt) {
        this.totalDebitAmt = totalDebitAmt;
    }
    public BigDecimal getTotalCreditAmt() {
        return totalCreditAmt;
    }
    public void setTotalCreditAmt(BigDecimal totalCreditAmt) {
        this.totalCreditAmt = totalCreditAmt;
    }
    public String getBifCodeId() {
        return bifCodeId;
    }
    public void setBifCodeId(String bifCodeId) {
        this.bifCodeId = bifCodeId;
    }
    public String getBifName() {
        return bifName;
    }
    public void setBifName(String bifName) {
        this.bifName = bifName;
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
    public String getSaveType() {
        return saveType;
    }
    public void setSaveType(String saveType) {
        this.saveType = saveType;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getReserve1() {
        return reserve1;
    }
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }
    public String getReserve2() {
        return reserve2;
    }
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }
    public String getReserve3() {
        return reserve3;
    }
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }
    public String getReserve4() {
        return reserve4;
    }
    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }
    public String getReserve5() {
        return reserve5;
    }
    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }
    public byte[] getStatementImg() {
        return statementImg;
    }
    public void setStatementImg(byte[] statementImg) {
        this.statementImg = statementImg;
    }
}
