package com.bytter.report.domain.cms.vo;

import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class LoanBalanceAnalysisVO  implements Serializable {
    
    private String billCode;
    
    /**单位*/ 
    @Corp(inject="corpName")
    private String corpId;
    private String corpName;
    /**币别*/
    @Currency(inject="curName")
    private String curId;
    private String curName;
    /**融资种类*/
    @Dict(dictGroup = "Loan.Sign", inject = "loanSignName")
    private String loanSign;
    private String loanSignName;
    /**本期余额*/
    private BigDecimal currentBalance;
    /**对比周期*/
    private String compareSign;
    /**余额*/
    private BigDecimal balanceM;
    /**差额*/
    private BigDecimal differenceM;
    /**比例*/
    private BigDecimal scaleM;

    /**余额*/
    private BigDecimal balanceS;
    /**差额*/
    private BigDecimal differenceS;
    /**比例*/
    private BigDecimal scaleS;

    /**余额*/
    private BigDecimal balanceL;
    /**差额*/
    private BigDecimal differenceL;
    /**比例*/
    private BigDecimal scaleL;

    /**余额*/
    private BigDecimal balanceY;
    /**差额*/
    private BigDecimal differenceY;
    /**比例*/
    private BigDecimal scaleY;
    
    /**是否折合本位币*/
    private String isToCurr;
    /**统计维护*/
    private String typesOfStatistics;
    /**对比周期*/
    private String cycle;
    /**金额单位*/
    private String moneyType;
    /**统计月份*/
    private Date monthDate;
    /**展示方式*/
    private String displayMode;
    /**是否包含在途金额*/
    private String isContain;
    /**贷款金额*/
    private BigDecimal loanMoney;
    /**发放金额*/
    private BigDecimal sendMoney;
    /**已还金额*/   
    private BigDecimal repayMoney;
    /**贷款余额*/
    private BigDecimal stayMoney;
    /**还款在途金额*/
    private BigDecimal repayAwayMoney;
    /**周期*/
    private String periodName;
    private String corpIds;
    
    private String infoBillCode;
    @Corp(inject="netName")
    private String netCode;
    private String netName;
    private String contractCode;
    private String sendBillCode;
    private String loanAcc;
    private Date loanDate;
    private Date repayDate;
    private String repayType;
    private String loanClass;
    
    private String bankName;
    private BigDecimal plMoney;
    private Date sendDate;
    private String plTerm;
    private String ternUnitName;
    private BigDecimal sumRepayMoney;
    private BigDecimal sumRepayPassageMoney;
    private BigDecimal sendBalanceMoney;

    /**委托人单位代码*/
    @Corp(inject="precativeName")
    private String precativeId;
    private String precativeName;
    @Corp(inject="loanOppName")
    private String loanOpp;
    private String loanOppName;
    private String infoContractCode;
    private String sendContractCode;
    private BigDecimal infoMoney;
    private Date endDate;
    private BigDecimal passageMoney;
    /**贷款期限*/
    private String repayCycle;
    
    private List<LoanBalanceAnalysisVO> provideList;
    private List<LoanBalanceAnalysisVO> precativeList;
    private List<LoanBalanceAnalysisVO> loanList;

    /**
     * 名称
     */
    private String name;
    /**
     * 数据
     */
    private Object data;
    /**
     * 横轴
     */
    private String xList;

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
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

    public BigDecimal getCurrentBalance() {
        if(currentBalance == null){
            return BigDecimal.ZERO;
        }
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getCompareSign() {
        return compareSign;
    }

    public void setCompareSign(String compareSign) {
        this.compareSign = compareSign;
    }

    public BigDecimal getBalanceM() {
        if(balanceM == null){
            return BigDecimal.ZERO;
        }
        return balanceM;
    }

    public void setBalanceM(BigDecimal balanceM) {
        this.balanceM = balanceM;
    }

    public BigDecimal getDifferenceM() {
        if(differenceM == null){
            return BigDecimal.ZERO;
        }
        return differenceM;
    }

    public void setDifferenceM(BigDecimal differenceM) {
        this.differenceM = differenceM;
    }

    public BigDecimal getScaleM() {
        return scaleM;
    }

    public void setScaleM(BigDecimal scaleM) {
        this.scaleM = scaleM;
    }

    public BigDecimal getBalanceS() {
        if(balanceS == null){
            return BigDecimal.ZERO;
        }
        return balanceS;
    }

    public void setBalanceS(BigDecimal balanceS) {
        this.balanceS = balanceS;
    }

    public BigDecimal getDifferenceS() {
        return differenceS;
    }

    public void setDifferenceS(BigDecimal differenceS) {
        this.differenceS = differenceS;
    }

    public BigDecimal getScaleS() {
        return scaleS;
    }

    public void setScaleS(BigDecimal scaleS) {
        this.scaleS = scaleS;
    }

    public BigDecimal getBalanceL() {
        if(balanceL == null){
            return BigDecimal.ZERO;
        }
        return balanceL;
    }

    public void setBalanceL(BigDecimal balanceL) {
        this.balanceL = balanceL;
    }

    public BigDecimal getDifferenceL() {
        return differenceL;
    }

    public void setDifferenceL(BigDecimal differenceL) {
        this.differenceL = differenceL;
    }

    public BigDecimal getScaleL() {
        return scaleL;
    }

    public void setScaleL(BigDecimal scaleL) {
        this.scaleL = scaleL;
    }

    public BigDecimal getBalanceY() {
        if(balanceY == null){
            return BigDecimal.ZERO;
        }
        return balanceY;
    }

    public void setBalanceY(BigDecimal balanceY) {
        this.balanceY = balanceY;
    }

    public BigDecimal getDifferenceY() {
        return differenceY;
    }

    public void setDifferenceY(BigDecimal differenceY) {
        this.differenceY = differenceY;
    }

    public BigDecimal getScaleY() {
        return scaleY;
    }

    public void setScaleY(BigDecimal scaleY) {
        this.scaleY = scaleY;
    }

    public String getIsToCurr() {
        return isToCurr;
    }

    public void setIsToCurr(String isToCurr) {
        this.isToCurr = isToCurr;
    }

    public String getTypesOfStatistics() {
        return typesOfStatistics;
    }

    public void setTypesOfStatistics(String typesOfStatistics) {
        this.typesOfStatistics = typesOfStatistics;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String cycle) {
        this.cycle = cycle;
    }

    public String getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(String moneyType) {
        this.moneyType = moneyType;
    }

    public Date getMonthDate() {
        return monthDate;
    }

    public void setMonthDate(Date monthDate) {
        this.monthDate = monthDate;
    }

    public String getDisplayMode() {
        return displayMode;
    }

    public void setDisplayMode(String displayMode) {
        this.displayMode = displayMode;
    }

    public String getIsContain() {
        return isContain;
    }

    public void setIsContain(String isContain) {
        this.isContain = isContain;
    }

    public BigDecimal getLoanMoney() {
        return loanMoney;
    }

    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
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

    public BigDecimal getStayMoney() {
        return stayMoney;
    }

    public void setStayMoney(BigDecimal stayMoney) {
        this.stayMoney = stayMoney;
    }

    public BigDecimal getRepayAwayMoney() {
        return repayAwayMoney;
    }

    public void setRepayAwayMoney(BigDecimal repayAwayMoney) {
        this.repayAwayMoney = repayAwayMoney;
    }
    
    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getCorpIds() {
        return corpIds;
    }

    public void setCorpIds(String corpIds) {
        this.corpIds = corpIds;
    }

    public String getInfoBillCode() {
        return infoBillCode;
    }

    public void setInfoBillCode(String infoBillCode) {
        this.infoBillCode = infoBillCode;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
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

    public String getLoanAcc() {
        return loanAcc;
    }

    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
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

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public String getLoanClass() {
        return loanClass;
    }

    public void setLoanClass(String loanClass) {
        this.loanClass = loanClass;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public BigDecimal getPlMoney() {
        return plMoney;
    }

    public void setPlMoney(BigDecimal plMoney) {
        this.plMoney = plMoney;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public String getPlTerm() {
        return plTerm;
    }

    public void setPlTerm(String plTerm) {
        this.plTerm = plTerm;
    }

    public String getTernUnitName() {
        return ternUnitName;
    }

    public void setTernUnitName(String ternUnitName) {
        this.ternUnitName = ternUnitName;
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

    public String getPrecativeName() {
        return precativeName;
    }

    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }

    public String getLoanOpp() {
        return loanOpp;
    }

    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }

    public String getInfoContractCode() {
        return infoContractCode;
    }

    public void setInfoContractCode(String infoContractCode) {
        this.infoContractCode = infoContractCode;
    }

    public String getSendContractCode() {
        return sendContractCode;
    }

    public void setSendContractCode(String sendContractCode) {
        this.sendContractCode = sendContractCode;
    }

    public BigDecimal getInfoMoney() {
        return infoMoney;
    }

    public void setInfoMoney(BigDecimal infoMoney) {
        this.infoMoney = infoMoney;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPassageMoney() {
        return passageMoney;
    }

    public void setPassageMoney(BigDecimal passageMoney) {
        this.passageMoney = passageMoney;
    }

    public String getNetCode() {
        return netCode;
    }

    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }

    public String getPrecativeId() {
        return precativeId;
    }

    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }

    public String getLoanOppName() {
        return loanOppName;
    }

    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }

    public String getRepayCycle() {
        return repayCycle;
    }

    public void setRepayCycle(String repayCycle) {
        this.repayCycle = repayCycle;
    }

    public List<LoanBalanceAnalysisVO> getProvideList() {
        return provideList;
    }

    public void setProvideList(List<LoanBalanceAnalysisVO> provideList) {
        this.provideList = provideList;
    }

    public List<LoanBalanceAnalysisVO> getPrecativeList() {
        return precativeList;
    }

    public void setPrecativeList(List<LoanBalanceAnalysisVO> precativeList) {
        this.precativeList = precativeList;
    }

    public List<LoanBalanceAnalysisVO> getLoanList() {
        return loanList;
    }

    public void setLoanList(List<LoanBalanceAnalysisVO> loanList) {
        this.loanList = loanList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getxList() {
        return xList;
    }

    public void setxList(String xList) {
        this.xList = xList;
    }

}
