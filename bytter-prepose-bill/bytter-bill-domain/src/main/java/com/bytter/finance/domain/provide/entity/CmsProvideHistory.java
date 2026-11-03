package com.bytter.finance.domain.provide.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2023/10/25<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
@Entity
@Table(name = "CMS_PROVIDE_HISTORY", schema = "")
public class CmsProvideHistory extends IdEntity implements java.io.Serializable {

    /**报表日期*/
    private Date reportDate;
    /**借款银行*/
    private String bankName;
    /**申请单位*/
    private String corpId;
    /**借款种类*/
    private String plClass;
    /**借款单号*/
    private String infoBillCode;
    /**发放单号*/
    private String sendBillCode;
    /**发放编号*/
    private String contractCode;
    /**创建日期*/
    private Date createDate;
    /**借款币种*/
    private String curId;
    /**借款金额*/
    private BigDecimal plMoney;
    /**发放金额*/
    private BigDecimal sendMoney;
    /**已还款本金*/
    private BigDecimal sumRepayMoney;
    /**还款在途本金*/
    private BigDecimal sumRepayPassageMoney;
    /**借款余额*/
    private BigDecimal sendBalanceMoney;
    /**借款利率*/
    private BigDecimal plRate;
    /**发放日期*/
    private Date sendDate;
    /**到期日期*/
    private Date repayDate;
    /**展期次数*/
    private int deferredCount;
    /**备注*/
    private String rmk;
    /**创建人*/
    private String createName;
    /**状态*/
    private String bizStatus;
    /**合同id*/
    private String conId;

    @Column(name ="REPORT_DATE")
    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    @Column(name ="BANK_NAME")
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name ="CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="PL_CLASS")
    public String getPlClass() {
        return plClass;
    }

    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }

    @Column(name ="INFO_BILL_CODE")
    public String getInfoBillCode() {
        return infoBillCode;
    }

    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }

    @Column(name ="SEND_BILL_CODE")
    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }

    @Column(name ="CONTRACT_CODE")
    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="PL_MONEY")
    public BigDecimal getPlMoney() {
        return plMoney;
    }

    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }

    @Column(name ="SEND_MONEY")
    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    @Column(name ="SUM_REPAY_MONEY")
    public BigDecimal getSumRepayMoney() {
        return sumRepayMoney;
    }

    public void setSumRepayMoney(BigDecimal sumRepayMoney) {
        this.sumRepayMoney = sumRepayMoney;
    }

    @Column(name ="SUM_REPAY_PASSAGE_MONEY")
    public BigDecimal getSumRepayPassageMoney() {
        return sumRepayPassageMoney;
    }

    public void setSumRepayPassageMoney(BigDecimal sumRepayPassageMoney) {
        this.sumRepayPassageMoney = sumRepayPassageMoney;
    }

    @Column(name ="SEND_BALANCE_MONEY")
    public BigDecimal getSendBalanceMoney() {
        return sendBalanceMoney;
    }

    public void setSendBalanceMoney(BigDecimal sendBalanceMoney) {
        this.sendBalanceMoney = sendBalanceMoney;
    }

    @Column(name ="PL_RATE")
    public BigDecimal getPlRate() {
        return plRate;
    }

    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }

    @Column(name ="SEND_DATE")
    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    @Column(name ="REPAY_DATE")
    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    @Column(name ="DEFERRED_COUNT")
    public int getDeferredCount() {
        return deferredCount;
    }

    public void setDeferredCount(int deferredCount) {
        this.deferredCount = deferredCount;
    }

    @Column(name ="RMK")
    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="CREATE_NAME")
    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    @Column(name ="BIZ_STATUS")
    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    @Column(name ="CON_ID")
    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }
}
