package com.bytter.biz.creditRating.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 功能介绍 : <br>
 * <b>日期：</b>2024/12/30<br>
 *
 * @param
 * @author tongqr
 * @version 1.0
 */
public class CreditAnalyzeReportVO implements Serializable {
    /**
     * 评价任务编号
     */
    private String taskNo;
    /**
     * 机构编码
     */
    @BtCompositeSearch(fieldName = "ird.Institution_Code")
    private String orgCode;
    /**
     * 评价机构
     */
    private String orgId;

    /**
     * 机构名称
     */
    @BtCompositeSearch(fieldName = "ctoi.org_name")
    private String orgName;
    /**
     * 机构类别
     */
    @BtCompositeSearch(fieldName = "info.org_type")
    @Dict(dictGroup = "INSTITUTIONA_TYPE", inject = "orgTypeName")
    private String orgType;
    private String orgTypeName;
    /**
     * 评价等级
     */
    @BtCompositeSearch(fieldName = "ctoi.ORG_EVALUATE_LEVEL")
    private String grade;
    /**
     * 评价得分（机构平均分）
     */
    @BtCompositeSearch(fieldName = "ctoi.org_avg")
    private BigDecimal orgAvg;
    /**
     * 评价开始日期
     */
    @BtCompositeSearch(fieldName = "info.evaluate_start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date evaluateStartDate;

    /**
     * 评价结束日期
     */
    @BtCompositeSearch(fieldName = "info.evaluate_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date evaluateEndDate;
    /**
     * 银行类型表Id
     */
    private String bankTypeId;

    /**
     *支行开户行行号
     */
    private String bankCode;
    /**
     * 授信总额
     */
    private BigDecimal creditTotal;
    /**
     * 借款余额
     */
    private BigDecimal loanBalance;
    /**
     * 结算总量
     */
    private BigDecimal settlementTotal;
    /**
     * 账户余额
     */
    private BigDecimal accountBalance;
    /**
     * 理财余额
     */
    private BigDecimal financeBalance;

    public String getTaskNo() {
        return taskNo;
    }

    public void setTaskNo(String taskNo) {
        this.taskNo = taskNo;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgTypeName() {
        return orgTypeName;
    }

    public void setOrgTypeName(String orgTypeName) {
        this.orgTypeName = orgTypeName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public BigDecimal getOrgAvg() {
        return orgAvg;
    }

    public void setOrgAvg(BigDecimal orgAvg) {
        this.orgAvg = orgAvg;
    }

    public Date getEvaluateStartDate() {
        return evaluateStartDate;
    }

    public void setEvaluateStartDate(Date evaluateStartDate) {
        this.evaluateStartDate = evaluateStartDate;
    }

    public Date getEvaluateEndDate() {
        return evaluateEndDate;
    }

    public void setEvaluateEndDate(Date evaluateEndDate) {
        this.evaluateEndDate = evaluateEndDate;
    }

    public String getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(String bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal) {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(BigDecimal loanBalance) {
        this.loanBalance = loanBalance;
    }

    public BigDecimal getSettlementTotal() {
        return settlementTotal;
    }

    public void setSettlementTotal(BigDecimal settlementTotal) {
        this.settlementTotal = settlementTotal;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public BigDecimal getFinanceBalance() {
        return financeBalance;
    }

    public void setFinanceBalance(BigDecimal financeBalance) {
        this.financeBalance = financeBalance;
    }
}
