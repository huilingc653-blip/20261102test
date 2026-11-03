package com.bytter.biz.inneraccount.vo;

import com.bytter.poi.annotation.Excel;
import com.bytter.poi.annotation.ExcelTarget;
import com.bytter.poi.handler.ExcelModel;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 浪潮历史流水导入
 */
@ExcelTarget("BisAccDtlHisVO")
public class BisAccDtlHisVO extends ExcelModel implements java.io.Serializable{

    @Override
    public String getBeanName() {
        return "bisAccDtlHisService";
    }
    /**
     *对应字段
     * 交易日期transDate 交易时间transTime 账户编号bankAcc
     */
    /** 本方账号 */
    @Excel(name = "账户编号")
    private String bankAcc;
    /** 本方账户名 */
    private String accName;
    /** 本方账户开户行 */
    private String bankName;
    /** 接口代码 */
    private String bifCode;
    /** 对方账户 */
    @Excel(name = "对方账户")
    private String oppAccNo;
    /** 对方账户名 */
    @Excel(name = "对方户名")
    private String oppAccName;
    /** 对方账户开户行 */
    @Excel(name = "对方开户行")
    private String oppAccBank;
    /** 借贷标志 0-收 1-支 */
    private String cdSign;
    /** 交易金额 */
    private BigDecimal amt;
    /** 交易日期 */
    @Excel(name = "交易日期")
    private String transDateStr;
    private Date transDate;
    /** 交易时间 */
    @Excel(name = "交易时间")
    private String transTimeStr;
    private Date transTime;
    /** 红蓝标志 1-红 2-蓝 */
    private String rbSign;
    /** 支出金额 */
    @Excel(name = "付款金额",align = HorizontalAlignment.RIGHT)
    private String payMoneyStr;
    private BigDecimal payMoney;
    /** 收入金额 */
    @Excel(name = "收款金额",align = HorizontalAlignment.RIGHT)
    private String incomeMoneyStr;
    private BigDecimal incomeMoney;

    /** 币别ID */
    private String curId;
    /** 币别 */
    @Excel(name = "币种")
    private String curName;
    /** 用途 */
    @Excel(name = "用途")
    private String uses;
    /** 余额 */
    @Excel(name = "当前余额",align = HorizontalAlignment.RIGHT)
    private String balStr;
    private BigDecimal bal;
    /**
     * 银行流水号
     */
    @Excel(name = "银行流水号")
    private String bankSerialNo;

    public String getBankAcc() {
        return bankAcc;
    }

    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    public String getAccName() {
        return accName;
    }

    public void setAccName(String accName) {
        this.accName = accName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBifCode() {
        return bifCode;
    }

    public void setBifCode(String bifCode) {
        this.bifCode = bifCode;
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

    public String getOppAccBank() {
        return oppAccBank;
    }

    public void setOppAccBank(String oppAccBank) {
        this.oppAccBank = oppAccBank;
    }

    public String getCdSign() {
        return cdSign;
    }

    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }

    public String getTransDateStr() {
        return transDateStr;
    }

    public void setTransDateStr(String transDateStr) {
        this.transDateStr = transDateStr;
    }

    public Date getTransDate() {
        return transDate;
    }

    public void setTransDate(Date transDate) {
        this.transDate = transDate;
    }

    public String getTransTimeStr() {
        return transTimeStr;
    }

    public void setTransTimeStr(String transTimeStr) {
        this.transTimeStr = transTimeStr;
    }

    public Date getTransTime() {
        return transTime;
    }

    public void setTransTime(Date transTime) {
        this.transTime = transTime;
    }

    public String getRbSign() {
        return rbSign;
    }

    public void setRbSign(String rbSign) {
        this.rbSign = rbSign;
    }

    public String getPayMoneyStr() {
        return payMoneyStr;
    }

    public void setPayMoneyStr(String payMoneyStr) {
        this.payMoneyStr = payMoneyStr;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public String getIncomeMoneyStr() {
        return incomeMoneyStr;
    }

    public void setIncomeMoneyStr(String incomeMoneyStr) {
        this.incomeMoneyStr = incomeMoneyStr;
    }

    public BigDecimal getIncomeMoney() {
        return incomeMoney;
    }

    public void setIncomeMoney(BigDecimal incomeMoney) {
        this.incomeMoney = incomeMoney;
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

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }

    public String getBalStr() {
        return balStr;
    }

    public void setBalStr(String balStr) {
        this.balStr = balStr;
    }

    public BigDecimal getBal() {
        return bal;
    }

    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }

    public String getBankSerialNo() {
        return bankSerialNo;
    }

    public void setBankSerialNo(String bankSerialNo) {
        this.bankSerialNo = bankSerialNo;
    }
}
