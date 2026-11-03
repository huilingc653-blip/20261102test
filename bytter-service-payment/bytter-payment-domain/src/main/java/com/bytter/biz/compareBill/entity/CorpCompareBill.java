package com.bytter.biz.compareBill.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 单位对账功能
 *
 * @author zhoul 2020-11-03
 * @since 3.4
 */
@Entity
@Table(name = "FC_CORP_COMPARE_BILL")
public class CorpCompareBill extends IdEntity {
    /**
     * 单位ID
     */
    private String corpId;
    /**
     * 账号
     */
    private String account;
    /**
     * 账号余额
     */
    private BigDecimal accountBalance;
    /**
     * 企业账面余额
     */
    private BigDecimal erpBalance;
    /**
     * 核对结果: 1 相符、0 不相符
     */
    private String isCheck;
    /**
     * 中心借方未达帐
     */
    private String accountDebitMoney;
    /**
     * 中心贷方未达帐
     */
    private String accountCreditMoney;
    /**
     * 单位借方未达帐
     */
    private String erpDebitMoney;
    /**
     * 单位贷方未达帐
     */
    private String erpCreditMoney;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 操作人
     */
    private String opCode;
    /**
     * 操作日期
     */
    private Date opDate;
    /**
     * 审批日期
     */
    private Date checkDate;
    /**
     * 打印次数
     */
    private Integer prints;
    /**
     * 账单月份
     */
    private String billMonth;
    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    @Column(name = "ACC_BALANCE")
    public BigDecimal getAccountBalance() {
        return accountBalance;
    }
    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }
    @Column(name = "ERP_BALANCE")
    public BigDecimal getErpBalance() {
        return erpBalance;
    }
    public void setErpBalance(BigDecimal erpBalance) {
        this.erpBalance = erpBalance;
    }
    @Column(name = "IS_CHECK")
    public String getIsCheck() {
        return isCheck;
    }
    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }
    @Column(name = "ACC_DEBIT_MONEY")
    public String getAccountDebitMoney() {
        return accountDebitMoney;
    }
    public void setAccountDebitMoney(String accountDebitMoney) {
        this.accountDebitMoney = accountDebitMoney;
    }
    @Column(name = "ACC_CREDIT_MONEY")
    public String getAccountCreditMoney() {
        return accountCreditMoney;
    }
    public void setAccountCreditMoney(String accountCreditMoney) {
        this.accountCreditMoney = accountCreditMoney;
    }
    @Column(name = "ERP_DEBIT_MONEY")
    public String getErpDebitMoney() {
        return erpDebitMoney;
    }
    public void setErpDebitMoney(String erpDebitMoney) {
        this.erpDebitMoney = erpDebitMoney;
    }
    @Column(name = "ERP_CREDIT_MONEY")
    public String getErpCreditMoney() {
        return erpCreditMoney;
    }
    public void setErpCreditMoney(String erpCreditMoney) {
        this.erpCreditMoney = erpCreditMoney;
    }
    @Column(name = "STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "OP_CODE")
    public String getOpCode() {
        return opCode;
    }
    public void setOpCode(String opCode) {
        this.opCode = opCode;
    }
    @Column(name = "CREATE_DATE")
    public Date getOpDate() {
        return opDate;
    }
    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }
    @Column(name = "CHECK_DATE")
    public Date getCheckDate() {
        return checkDate;
    }
    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }
    @Column(name = "PRINTS")
    public Integer getPrints() {
        return prints;
    }
    public void setPrints(Integer prints) {
        this.prints = prints;
    }
    @Column(name = "BILL_MONTH")
    public String getBillMonth() {
        return billMonth;
    }
    public void setBillMonth(String billMonth) {
        this.billMonth = billMonth;
    }
}
