package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.*;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author luxy
 * @Title: CmsLDRAnalysisVO
 * @ProjectName T3(3.2)
 * @Description: 融资存贷比分析报表查询VO
 * @date 2020/1/16 15:43
 */
public class CmsLDRAnalysisVO implements Serializable {

    /*** 融资单位*/
    @BtCompositeSearch()
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /*** 币别id*/
    @BtCompositeSearch()
    @Currency(inject = "curName")
    private String curId;
    private String curName;

    /*** 统计维度*/
    @BtCompositeSearch()
    private String typesOfStatistics;
    /**
     * 银行类型编码
     */
    @BtCompositeSearch()
    @BankType(inject = "bankTypeName")
    private java.lang.String bankType;
    /**
     * 银行类型
     */
    private java.lang.String bankTypeName;
    /**
     * 银行联行号
     */
    @BtCompositeSearch()
    @InputBankInfo(inject = "bankName")
    private java.lang.String bankCode;
    /**
     * 银行名称
     */
    private java.lang.String bankName;
    /**
     * 结算中心
     */
    @Corp(inject = "netName")
    private java.lang.String netId;
    private java.lang.String netName;
    /*** 是否折合本位币*/
    @BtCompositeSearch()
    private String isConvertStandCur;
    /*** 查询截止日期*/
    @BtCompositeSearch()
    private Date endDate;

    /**对比周期*/
    @BtCompositeSearch()
    private String cycle;

    /**是否包含在途金额*/
    @BtCompositeSearch()
    private String isContain;

    /*** 存款合计*/
    private BigDecimal totalDepositsD;
    /*** 贷款合计*/
    private BigDecimal totalLoansD;
    /*** 存贷比*/
    private BigDecimal depositLoanRateD;

    /*** 存款合计*/
    private BigDecimal totalDepositsM;
    /*** 贷款合计*/
    private BigDecimal totalLoansM;
    /*** 存贷比*/
    private BigDecimal depositLoanRateM;

    /*** 存款合计*/
    private BigDecimal totalDepositsS;
    /*** 贷款合计*/
    private BigDecimal totalLoansS;
    /*** 存贷比*/
    private BigDecimal depositLoanRateS;

    /*** 存款合计*/
    private BigDecimal totalDepositsL;
    /*** 贷款合计*/
    private BigDecimal totalLoansL;
    /*** 存贷比*/
    private BigDecimal depositLoanRateL;

    /*** 存款合计*/
    private BigDecimal totalDepositsY;
    /*** 贷款合计*/
    private BigDecimal totalLoansY;
    /*** 存贷比*/
    private BigDecimal depositLoanRateY;

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

    public String getTypesOfStatistics() {
        return typesOfStatistics;
    }

    public void setTypesOfStatistics(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankTypeName() {
        return bankTypeName;
    }

    public void setBankTypeName(String bankTypeName) {
        this.bankTypeName = bankTypeName;
    }

    public String getIsConvertStandCur() {
        return isConvertStandCur;
    }

    public void setIsConvertStandCur(String isConvertStandCur) {
        this.isConvertStandCur = isConvertStandCur;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getIsContain() {
        return isContain;
    }

    public void setIsContain(String isContain) {
        this.isContain = isContain;
    }

    public BigDecimal getTotalDepositsD() {
        return totalDepositsD;
    }

    public void setTotalDepositsD(BigDecimal totalDepositsD) {
        this.totalDepositsD = totalDepositsD;
    }

    public BigDecimal getTotalLoansD() {
        return totalLoansD;
    }

    public void setTotalLoansD(BigDecimal totalLoansD) {
        this.totalLoansD = totalLoansD;
    }

    public BigDecimal getDepositLoanRateD() {
        return depositLoanRateD;
    }

    public void setDepositLoanRateD(BigDecimal depositLoanRateD) {
        this.depositLoanRateD = depositLoanRateD;
    }

    public BigDecimal getTotalDepositsM() {
        return totalDepositsM;
    }

    public void setTotalDepositsM(BigDecimal totalDepositsM) {
        this.totalDepositsM = totalDepositsM;
    }

    public BigDecimal getTotalLoansM() {
        return totalLoansM;
    }

    public void setTotalLoansM(BigDecimal totalLoansM) {
        this.totalLoansM = totalLoansM;
    }

    public BigDecimal getDepositLoanRateM() {
        return depositLoanRateM;
    }

    public void setDepositLoanRateM(BigDecimal depositLoanRateM) {
        this.depositLoanRateM = depositLoanRateM;
    }

    public BigDecimal getTotalDepositsS() {
        return totalDepositsS;
    }

    public void setTotalDepositsS(BigDecimal totalDepositsS) {
        this.totalDepositsS = totalDepositsS;
    }

    public BigDecimal getTotalLoansS() {
        return totalLoansS;
    }

    public void setTotalLoansS(BigDecimal totalLoansS) {
        this.totalLoansS = totalLoansS;
    }

    public BigDecimal getDepositLoanRateS() {
        return depositLoanRateS;
    }

    public void setDepositLoanRateS(BigDecimal depositLoanRateS) {
        this.depositLoanRateS = depositLoanRateS;
    }

    public BigDecimal getTotalDepositsL() {
        return totalDepositsL;
    }

    public void setTotalDepositsL(BigDecimal totalDepositsL) {
        this.totalDepositsL = totalDepositsL;
    }

    public BigDecimal getTotalLoansL() {
        return totalLoansL;
    }

    public void setTotalLoansL(BigDecimal totalLoansL) {
        this.totalLoansL = totalLoansL;
    }

    public BigDecimal getDepositLoanRateL() {
        return depositLoanRateL;
    }

    public void setDepositLoanRateL(BigDecimal depositLoanRateL) {
        this.depositLoanRateL = depositLoanRateL;
    }

    public BigDecimal getTotalDepositsY() {
        return totalDepositsY;
    }

    public void setTotalDepositsY(BigDecimal totalDepositsY) {
        this.totalDepositsY = totalDepositsY;
    }

    public BigDecimal getTotalLoansY() {
        return totalLoansY;
    }

    public void setTotalLoansY(BigDecimal totalLoansY) {
        this.totalLoansY = totalLoansY;
    }

    public BigDecimal getDepositLoanRateY() {
        return depositLoanRateY;
    }

    public void setDepositLoanRateY(BigDecimal depositLoanRateY) {
        this.depositLoanRateY = depositLoanRateY;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }
}
