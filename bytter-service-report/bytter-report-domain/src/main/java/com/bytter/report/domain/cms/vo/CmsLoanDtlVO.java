package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Currency;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-22 11:24
 * @Version 1.0
 */
public class CmsLoanDtlVO  {

    /**
     *主业务流水号
     */
    private String billCode;
    /**
     *资金中心
     */
    private String netName;

    /**
     *单位
     */
    private String corpName;

    /**
     *合同号
     */
    private String contractNo;

    /**
     *发放流水号
     */
    private String sendBillCode;

    /**
     *发放账户
     */
    private String sendAcc;

    /**
     *币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    /**
     *贷款金额
     */
    private BigDecimal money;

    /**
     *贷款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;

    /**
     * 还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    /**
     *贷款类型
     */
    private String loanGenre;

    /**
     * 期限
     */
    private String loanTerm;

    /**
     * 期限单位
     */
    private String ternUnit;

    /**
     * 发放金额
     */
    private BigDecimal sendMoney;

    /**
     * 已还款金额
     */
    private BigDecimal repayMoney;

    /**
     * 还款在途金额
     */
    private BigDecimal repayingMoney;

    /**
     * 余额
     */
    private BigDecimal leftMoney;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }

    public String getSendAcc() {
        return sendAcc;
    }

    public void setSendAcc(String sendAcc) {
        this.sendAcc = sendAcc;
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

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getLoanGenre() {
        return loanGenre;
    }

    public void setLoanGenre(String loanGenre) {
        this.loanGenre = loanGenre;
    }

    public String getLoanTerm() {
        return loanTerm;
    }

    public void setLoanTerm(String loanTerm) {
        this.loanTerm = loanTerm;
    }

    public String getTernUnit() {
        return ternUnit;
    }

    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public BigDecimal getRepayingMoney() {
        return repayingMoney;
    }

    public void setRepayingMoney(BigDecimal repayingMoney) {
        this.repayingMoney = repayingMoney;
    }

    public BigDecimal getLeftMoney() {
        return leftMoney;
    }

    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }
}
