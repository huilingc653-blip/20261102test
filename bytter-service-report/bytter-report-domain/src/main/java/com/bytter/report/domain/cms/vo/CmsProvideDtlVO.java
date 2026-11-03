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
public class CmsProvideDtlVO  {

    /**
     *借款银行
     */
    private String bankName;

    /**
     *单位
     */
    private String corpName;

    /**
     *主业务编号
     */
    private String billCode;

    /**
     *合同号
     */
    private String contractNo;

    /**
     *发放编号
     */
    private String sendBillCode;

    /**
     *币种
     */
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    /**
     *借款金额
     */
    private BigDecimal money;

    /**
     *借款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date plDate;

    /**
     *还款日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;

    /**
     *合同期限
     */
    private String contractTerm;

    /**
     *发放金额
     */
    private BigDecimal sendMoney;

    /**
     *还款金额
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

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public Date getPlDate() {
        return plDate;
    }

    public void setPlDate(Date plDate) {
        this.plDate = plDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public String getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(String contractTerm) {
        this.contractTerm = contractTerm;
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
