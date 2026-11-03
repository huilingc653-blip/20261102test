package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Description:
 * Company: 深圳市拜特科技股份有限公司
 * Copyright: Copyright (c) 2024
 *
 * @author ZRS
 * @date 2024/12/30
 */
public class BankLoanWithholdingVO implements Serializable {
    @BtCompositeSearch(fieldName="info.id")
    private String id;
    @BtCompositeSearch(fieldName = "bank.type_name")
    private String bankName;//银行名称
    @BtCompositeSearch(fieldName = "info.bank_type")
    private String bankType;
    @BtCompositeSearch(fieldName="info.corp_id")
    private String corpId;//单位Id
    @BtCompositeSearch(fieldName="c.name")
    private String corpName;//单位名称
    @BtCompositeSearch(fieldName="info.bill_code")
    private String infoBillCode;//申请编号
    @BtCompositeSearch(fieldName="send.bill_code")
    private String sendBillCode;//发放单号
    @BtCompositeSearch(fieldName="info.cur_id")
    @Currency(inject = "curName")
    private String curId;//币别ID
    private String curName;//币别名次
    @BtCompositeSearch(fieldName="info.pl_money")
    private BigDecimal plMoney;//借款金额
    private BigDecimal sendMoney;//本次发放金额
    private BigDecimal sumRepayMoney;//总还款金额（针对某次发放的还款）
    private BigDecimal sumRepayInterest;//总还款利息（针对某次发放的还款）
    private BigDecimal sumRepayPassageMoney;//总还款金额（针对某次发放的还款在途）
    private BigDecimal sendBalanceMoney;//发放余额（sendMoney - sumRepayMoney）
    @BtCompositeSearch(fieldName="send.send_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date sendDate;//发放日期
    @BtCompositeSearch(fieldName="send.repay_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;//到期日期
    @Dict(tableName = "CMS_LOAN_TYPE", valueFieldName = "id", showFiledName = "NAME", inject = "plClassName")
    private String plClassId;
    @BtCompositeSearch(fieldName="clt.name")
    private String plClassName;//借款种类名称
    @BtCompositeSearch(fieldName="info.pl_Class")
    private String plClass;//借款种类名称
    //区间利息
    private BigDecimal sectionInterest;
    // 月度利息
    private BigDecimal[] monthInterest;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
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

    public BigDecimal getSumRepayInterest() {
        return sumRepayInterest;
    }

    public void setSumRepayInterest(BigDecimal sumRepayInterest) {
        this.sumRepayInterest = sumRepayInterest;
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

    public String getPlClassId() {
        return plClassId;
    }

    public void setPlClassId(String plClassId) {
        this.plClassId = plClassId;
    }

    public String getPlClassName() {
        return plClassName;
    }

    public void setPlClassName(String plClassName) {
        this.plClassName = plClassName;
    }

    public String getPlClass() {
        return plClass;
    }

    public void setPlClass(String plClass) {
        this.plClass = plClass;
    }

    public BigDecimal getSectionInterest() {
        return sectionInterest;
    }

    public void setSectionInterest(BigDecimal sectionInterest) {
        this.sectionInterest = sectionInterest;
    }

    public BigDecimal[] getMonthInterest() {
        return monthInterest;
    }

    public void setMonthInterest(BigDecimal[] monthInterest) {
        this.monthInterest = monthInterest;
    }
}
