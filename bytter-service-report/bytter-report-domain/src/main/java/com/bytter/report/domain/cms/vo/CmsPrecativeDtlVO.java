package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Currency;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Title
 * @Description
 * @Author liusy
 * @Date 2020-01-22 11:25
 * @Version 1.0
 */
public class CmsPrecativeDtlVO  {

    /**
     * 主流水号
     */
    private String billCode;

    /**
     * 委托人
     */
    private String precativeName;

    /**
     * 受托人
     */
    private String corpName;

    /**
     * 借款人
     */
    private String loanOppName;

    /**
     * 合同号
     */
    private String contractCode;

    /**
     * 发放流水号
     */
    private String sendBillCode;

    /**
     * 币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    /**
     * 贷款金额
     */
    private BigDecimal money;

    /**
     * 开始日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    /**
     * 结束日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    /**
     *期限
     */
    private String term;

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

    public String getPrecativeName() {
        return precativeName;
    }

    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getLoanOppName() {
        return loanOppName;
    }

    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
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

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
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
