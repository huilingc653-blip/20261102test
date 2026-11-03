package com.bytter.finance.domain.provide.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2023/10/26<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CmsProvideHistoryVO implements java.io.Serializable {

    @BtCompositeSearch(fieldName="his.id")
    private String id;
    /**报表日期*/
    @BtCompositeSearch(fieldName="his.report_date")
    private Date reportDate;
    /**借款银行*/
    @BtCompositeSearch(fieldName="his.bank_name")
    private String bankName;
    /**申请单位*/
    @BtCompositeSearch(fieldName="his.corp_id")
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /**借款种类*/
    @BtCompositeSearch(fieldName="his.pl_class")
    private String plClass;
    private String plClassName;
    /**借款单号*/
    @BtCompositeSearch(fieldName="his.info_bill_code")
    private String infoBillCode;
    /**发放单号*/
    @BtCompositeSearch(fieldName="his.send_bill_code")
    private String sendBillCode;
    /**发放编号*/
    @BtCompositeSearch(fieldName="his.contract_code")
    private String contractCode;
    /**创建日期*/
    @BtCompositeSearch(fieldName="his.create_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createDate;
    /**借款币种*/
    @BtCompositeSearch(fieldName="his.cur_id")
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /**借款金额*/
    @BtCompositeSearch(fieldName="his.pl_money")
    private BigDecimal plMoney;
    /**发放金额*/
    @BtCompositeSearch(fieldName="his.send_money")
    private BigDecimal sendMoney;
    /**已还款本金*/
    @BtCompositeSearch(fieldName="his.sum_repay_money")
    private BigDecimal sumRepayMoney;
    /**还款在途本金*/
    @BtCompositeSearch(fieldName="his.sum_repay_passage_money")
    private BigDecimal sumRepayPassageMoney;
    /**借款余额*/
    @BtCompositeSearch(fieldName="his.send_balance_money")
    private BigDecimal sendBalanceMoney;
    /**借款利率*/
    @BtCompositeSearch(fieldName="his.pl_rate")
    private BigDecimal plRate;
    /**发放日期*/
    @BtCompositeSearch(fieldName="his.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;
    /**到期日期*/
    @BtCompositeSearch(fieldName="his.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /**展期次数*/
    @BtCompositeSearch(fieldName="his.deferred_count")
    private int deferredCount;
    /**备注*/
    @BtCompositeSearch(fieldName="his.rmk")
    private String rmk;
    /**创建人*/
    @BtCompositeSearch(fieldName="his.create_name")
    private String createName;
    /**状态*/
    @BtCompositeSearch(fieldName="his.biz_status")
    @Dict(dictGroup = "cms.bizStatus", inject = "bizStatusName")
    private String bizStatus;
    private String bizStatusName;
    /**合同id*/
    private String conId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
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

    public String getPlClass() {
        return plClass;
    }

    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }

    public String getPlClassName() {
        return plClassName;
    }

    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }

    public String getInfoBillCode() {
        return infoBillCode;
    }

    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }

    public String getSendBillCode() {
        return sendBillCode;
    }

    public void setSendBillCode(String sendBillCode) {
        this.sendBillCode = sendBillCode;
    }

    public String getContractCode() {
        return contractCode;
    }

    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public BigDecimal getPlMoney() {
        return plMoney;
    }

    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }

    public BigDecimal getSendMoney() {
        return sendMoney;
    }

    public void setSendMoney(BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }

    public BigDecimal getSumRepayMoney() {
        return sumRepayMoney;
    }

    public void setSumRepayMoney(BigDecimal sumRepayMoney) {
        this.sumRepayMoney = sumRepayMoney;
    }

    public BigDecimal getSumRepayPassageMoney() {
        return sumRepayPassageMoney;
    }

    public void setSumRepayPassageMoney(BigDecimal sumRepayPassageMoney) {
        this.sumRepayPassageMoney = sumRepayPassageMoney;
    }

    public BigDecimal getSendBalanceMoney() {
        return sendBalanceMoney;
    }

    public void setSendBalanceMoney(BigDecimal sendBalanceMoney) {
        this.sendBalanceMoney = sendBalanceMoney;
    }

    public BigDecimal getPlRate() {
        return plRate;
    }

    public void setPlRate(BigDecimal plRate) {
        this.plRate = plRate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public int getDeferredCount() {
        return deferredCount;
    }

    public void setDeferredCount(int deferredCount) {
        this.deferredCount = deferredCount;
    }

    public String getRmk() {
        return rmk;
    }

    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getBizStatus() {
        return bizStatus;
    }

    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }

    public String getBizStatusName() {
        return bizStatusName;
    }

    public void setBizStatusName(String bizStatusName) {
        this.bizStatusName = bizStatusName;
    }

    public String getConId() {
        return conId;
    }

    public void setConId(String conId) {
        this.conId = conId;
    }
}
