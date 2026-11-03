package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;


import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author luxy
 * @Title: CmsLoanStatementVO
 * @ProjectName T3(3.2)
 * @Description: TODO
 * @date 2020/1/13 11:21
 */
public class CmsLoanStatementVO  implements Serializable {

    /*** 单位id*/
    @BtCompositeSearch()
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /*** 币别id*/
    @BtCompositeSearch()
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /*** 融资方式*/
    @BtCompositeSearch()
    @Dict(dictGroup = "Loan.Sign", inject = "loanSignName")
    private String loanSign;
    private String loanSignName;
    /*** 本期合同金额*/
    private BigDecimal curContractMoney;
    /*** 本期发放金额*/
    private BigDecimal curSendMoney;
    /*** 本期还款金额*/
    private BigDecimal curRepayMoney;
    /*** 本期放款占比*/
    private BigDecimal curLoanRate;
    /*** 本期还款占比*/
    private BigDecimal curRepayRate;

    /*** 去年同期合同金额*/
    private BigDecimal lastContractMoney;
    /*** 去年同期发放金额*/
    private BigDecimal lastSendMoney;
    /*** 去年同期还款金额*/
    private BigDecimal lastRepayMoney;
    /*** 去年同期放款占比*/
    private BigDecimal lastLoanRate;
    /*** 去年同期还款占比*/
    private BigDecimal lastRepayRate;

    /*** 放款增长率*/
    private BigDecimal loanGrowthRate;
    /*** 还款增长率*/
    private BigDecimal repayGrowthRate;

    /*** 开始日期*/
    @BtCompositeSearch()
    private Date startDate;
    /*** 结束日期*/
    @BtCompositeSearch()
    private Date endDate;

    /*** 是否折合本位币*/
    @BtCompositeSearch()
    private String isConvertStandCur;
    /*** 统计单位*/
    @BtCompositeSearch()
    private String moneyUnit;

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

    public String getLoanSign() {
        return loanSign;
    }

    public void setLoanSign(String loanSign) {
        this.loanSign = loanSign;
    }

    public String getLoanSignName() {
        return loanSignName;
    }

    public void setLoanSignName(String loanSignName) {
        this.loanSignName = loanSignName;
    }

    public BigDecimal getCurContractMoney() {
        return curContractMoney;
    }

    public void setCurContractMoney(BigDecimal curContractMoney) {
        this.curContractMoney = curContractMoney;
    }

    public BigDecimal getCurSendMoney() {
        return curSendMoney;
    }

    public void setCurSendMoney(BigDecimal curSendMoney) {
        this.curSendMoney = curSendMoney;
    }

    public BigDecimal getCurRepayMoney() {
        return curRepayMoney;
    }

    public void setCurRepayMoney(BigDecimal curRepayMoney) {
        this.curRepayMoney = curRepayMoney;
    }

    public BigDecimal getCurLoanRate() {
        return curLoanRate;
    }

    public void setCurLoanRate(BigDecimal curLoanRate) {
        this.curLoanRate = curLoanRate;
    }

    public BigDecimal getCurRepayRate() {
        return curRepayRate;
    }

    public void setCurRepayRate(BigDecimal curRepayRate) {
        this.curRepayRate = curRepayRate;
    }

    public BigDecimal getLastContractMoney() {
        return lastContractMoney;
    }

    public void setLastContractMoney(BigDecimal lastContractMoney) {
        this.lastContractMoney = lastContractMoney;
    }

    public BigDecimal getLastSendMoney() {
        return lastSendMoney;
    }

    public void setLastSendMoney(BigDecimal lastSendMoney) {
        this.lastSendMoney = lastSendMoney;
    }

    public BigDecimal getLastRepayMoney() {
        return lastRepayMoney;
    }

    public void setLastRepayMoney(BigDecimal lastRepayMoney) {
        this.lastRepayMoney = lastRepayMoney;
    }

    public BigDecimal getLastLoanRate() {
        return lastLoanRate;
    }

    public void setLastLoanRate(BigDecimal lastLoanRate) {
        this.lastLoanRate = lastLoanRate;
    }

    public BigDecimal getLastRepayRate() {
        return lastRepayRate;
    }

    public void setLastRepayRate(BigDecimal lastRepayRate) {
        this.lastRepayRate = lastRepayRate;
    }

    public BigDecimal getLoanGrowthRate() {
        return loanGrowthRate;
    }

    public void setLoanGrowthRate(BigDecimal loanGrowthRate) {
        this.loanGrowthRate = loanGrowthRate;
    }

    public BigDecimal getRepayGrowthRate() {
        return repayGrowthRate;
    }

    public void setRepayGrowthRate(BigDecimal repayGrowthRate) {
        this.repayGrowthRate = repayGrowthRate;
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

    public String getIsConvertStandCur() {
        return isConvertStandCur;
    }

    public void setIsConvertStandCur(String isConvertStandCur) {
        this.isConvertStandCur = isConvertStandCur;
    }

    public String getMoneyUnit() {
        return moneyUnit;
    }

    public void setMoneyUnit(String moneyUnit) {
        this.moneyUnit = moneyUnit;
    }
}
