package com.bytter.check.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.poi.annotation.Excel;
import com.bytter.poi.handler.ExcelModel;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: ERP未达账初始化
 * @date 2017-06-20 16:31:49
 */

public class FcCompareAccTempVo extends ExcelModel implements java.io.Serializable, IBaseDataInject {
    /**
     * 对账类型
     */
    private String checkType;
    /**
     * 流水号
     */
    private String orderNo;
    /**
     * 凭证号
     */
    @Excel(name = "*凭证号")
    private String voucherNo;
    /**
     * 记账日期
     */
    @Excel(name = "*发生日期", importFormat = "yyyy/MM/dd")
    @BtCompositeSearch(fieldName = "a.tally_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tallyDate;
    /**
     * 企业账号
     */
    @Excel(name = "银行账号")
    @BtCompositeSearch(fieldName = "a.comp_acc")
    private String compAcc;
    /**
     * 币别代码
     */
    @BtCompositeSearch(fieldName = "a.cur_id")
    private String curCode;
    @Excel(name = "币别")
    private String curName;
    /**
     * 红蓝标志
     */
    @Excel(name = "红蓝标志")
    private String redblueSign;
    /**
     * 借方发生额
     */
    @Excel(name = "*借方金额")
    @BtCompositeSearch(fieldName = "a.debit_money")
    private java.math.BigDecimal debitMoney;
    /**
     * 贷方发生额
     */
    @Excel(name = "*贷方金额")
    @BtCompositeSearch(fieldName = "a.credit_money")
    private java.math.BigDecimal creditMoney;
    /**
     * 余额
     */
    @Excel(name = "*余额")
    private java.math.BigDecimal balance;
    /**
     * 摘要
     */
    @Excel(name = "摘要")
    private String summaryContent;
    /**
     * 行号
     */
    private Integer lineNo;
    /**
     * 对账标志
     */
    @BtCompositeSearch(fieldName = "a.compare_sign")
    private String compareSign;
    /**
     * 序号
     */
    private String bankId;
    /**
     * 票据号
     */
    @BtCompositeSearch(fieldName = "a.bill_no")
    private String billNo;
    /**
     * 起息日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date interestStartDate;
    /**
     * 调节日期
     */
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
     * 数据源
     */
    @Excel(name = "*企业账来源")
    private String dataSource;
    /**
     * 收款账号
     */
    private String payeeAcc;
    /**
     * 导入用户
     */
    private String importUser;
    /**
     * 对账时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date checkDate;
    /**
     * 对账勾对用户
     */
    private String checkUser;
    /**
     * 最后更新用户代码
     */
    private String updateBy;
    /**
     * 最后更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateTime;
    /**
     * 创建用户代码
     */
    private String createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
    /**
     * 单位
     **/
    @BtCompositeSearch(fieldName = "f.corp_id")
    private String corpId;

    @Override
    public String getBeanName() {
        return "fcCompareAccTempService";
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
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

    public java.util.Date getTallyDate() {
        return tallyDate;
    }

    public void setTallyDate(java.util.Date tallyDate) {
        this.tallyDate = tallyDate;
    }

    public String getCompAcc() {
        return compAcc;
    }

    public void setCompAcc(String compAcc) {
        this.compAcc = compAcc;
    }

    public String getCurCode() {
        return curCode;
    }

    public void setCurCode(String curCode) {
        this.curCode = curCode;
    }

    public String getCurName() {
        return curName;
    }

    public void setCurName(String curName) {
        this.curName = curName;
    }

    public String getRedblueSign() {
        return redblueSign;
    }

    public void setRedblueSign(String redblueSign) {
        this.redblueSign = redblueSign;
    }

    public java.math.BigDecimal getDebitMoney() {
        return debitMoney;
    }

    public void setDebitMoney(java.math.BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }

    public java.math.BigDecimal getCreditMoney() {
        return creditMoney;
    }

    public void setCreditMoney(java.math.BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }

    public java.math.BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(java.math.BigDecimal balance) {
        this.balance = balance;
    }

    public String getSummaryContent() {
        return summaryContent;
    }

    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }

    public Integer getLineNo() {
        return lineNo;
    }

    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }

    public String getCompareSign() {
        return compareSign;
    }

    public void setCompareSign(String compareSign) {
        this.compareSign = compareSign;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public java.util.Date getInterestStartDate() {
        return interestStartDate;
    }

    public void setInterestStartDate(java.util.Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    public java.util.Date getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(java.util.Date adjustDate) {
        this.adjustDate = adjustDate;
    }

    public java.util.Date getImportDate() {
        return importDate;
    }

    public void setImportDate(java.util.Date importDate) {
        this.importDate = importDate;
    }

    public String getImportType() {
        return importType;
    }

    public void setImportType(String importType) {
        this.importType = importType;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getPayeeAcc() {
        return payeeAcc;
    }

    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }

    public String getImportUser() {
        return importUser;
    }

    public void setImportUser(String importUser) {
        this.importUser = importUser;
    }

    public java.util.Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(java.util.Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getCheckUser() {
        return checkUser;
    }

    public void setCheckUser(String checkUser) {
        this.checkUser = checkUser;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public java.util.Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(java.util.Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public java.util.Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(java.util.Date createTime) {
        this.createTime = createTime;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

}
