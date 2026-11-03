package com.bytter.finance.domain.precative.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.bytter.core.annotation.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
public class PrecativeQueryVo implements java.io.Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** 贷款票据号 */
    @BtCompositeSearch(fieldName = "info.bill_Code")
    private String infoBillCode;
    /** 发放编号 */
    private String sendBillCode;
    /** 录入合同编码 */
    @BtCompositeSearch(fieldName = "info.contract_Code")
    private String infoContractCode;
    /** 发放合同编码 */
    @BtCompositeSearch(fieldName = "send.contract_Code")
    private String sendContractCode;
    /** 委托人单位代码 */
//    @Corp(inject = "precativeName")
    @BtCompositeSearch(fieldName = "info.precative_Id")
    private String precativeId;
    @BtCompositeSearch(fieldName = "info.precative_Id")
    private String precativeName;
    /** 贷款对象 */
//    @Corp(inject = "loanOppName")
    @BtCompositeSearch(fieldName = "info.loan_Opp")
    private String loanOpp;
    private String loanOppName;
    /** 贷款币种 */
//    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "info.start_Date")
    private java.util.Date startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date infoStartDate;
    /** 结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @BtCompositeSearch(fieldName = "info.end_Date")
    private java.util.Date endDate;
    /** 贷款期限 */
    private Integer term;
    /** 期限单位 */
    @Dict(dictGroup = "PrecativeLoanInfo.ternUnit", inject = "ternUnitStatus")
    private String ternUnit;
    private String ternUnitStatus;
    /** 发放日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sendDate;
    /** 到期日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date deadlineDate;
    /** 贷款利率 */
    private java.math.BigDecimal rate;
    /** 逾期利率 */
    private java.math.BigDecimal overdueRate;
    /** 贷款金额 */
    private java.math.BigDecimal money;
    /** 录入金额 */
    @BtCompositeSearch(fieldName = "info.money")
    private java.math.BigDecimal infoMoney;
    /** 发放金额 */
    private java.math.BigDecimal sendMoney;
    /** 展期次数 */
    private java.math.BigDecimal counts;
    /** 在途金额 */
    private java.math.BigDecimal passageMoney;
    /** 待还金额 */
    private java.math.BigDecimal repayMoney;
    /** 担保方式 */
    @Dict(dictGroup = "Cms.PlType.C", inject = "plName")
    private String plType;
    private String plName;
    /** 状态 */
    private String status;
    /** 业务状态 */
    private String bizStatus;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private java.util.Date repayDate;
    /** 委托贷款对接的合同信息id **/
    private String conId;
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
    public String getPrecativeId() {
        return precativeId;
    }
    public void setPrecativeId(String precativeId) {
        this.precativeId = precativeId;
    }
    public String getLoanOpp() {
        return loanOpp;
    }
    public void setLoanOpp(String loanOpp) {
        this.loanOpp = loanOpp;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public java.util.Date getStartDate() {
        return startDate;
    }
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }
    public java.util.Date getEndDate() {
        return endDate;
    }
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }
    public Integer getTerm() {
        return term;
    }
    public void setTerm(Integer term) {
        this.term = term;
    }
    public String getTernUnit() {
        return ternUnit;
    }
    public void setTernUnit(String ternUnit) {
        this.ternUnit = ternUnit;
    }
    public java.util.Date getSendDate() {
        return sendDate;
    }
    public void setSendDate(java.util.Date sendDate) {
        this.sendDate = sendDate;
    }
    public java.util.Date getDeadlineDate() {
        return deadlineDate;
    }
    public void setDeadlineDate(java.util.Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }
    public java.math.BigDecimal getRate() {
        return rate;
    }
    public void setRate(java.math.BigDecimal rate) {
        this.rate = rate;
    }
    public java.math.BigDecimal getOverdueRate() {
        return overdueRate;
    }
    public void setOverdueRate(java.math.BigDecimal overdueRate) {
        this.overdueRate = overdueRate;
    }
    public java.math.BigDecimal getMoney() {
        return money;
    }
    public void setMoney(java.math.BigDecimal money) {
        this.money = money;
    }
    public java.math.BigDecimal getInfoMoney() {
        return infoMoney;
    }
    public void setInfoMoney(java.math.BigDecimal infoMoney) {
        this.infoMoney = infoMoney;
    }
    public java.math.BigDecimal getSendMoney() {
        return sendMoney;
    }
    public void setSendMoney(java.math.BigDecimal sendMoney) {
        this.sendMoney = sendMoney;
    }
    public java.math.BigDecimal getPassageMoney() {
        return passageMoney;
    }
    public void setPassageMoney(java.math.BigDecimal passageMoney) {
        this.passageMoney = passageMoney;
    }
    public java.math.BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(java.math.BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public String getPlType() {
        return plType;
    }
    public void setPlType(String plType) {
        this.plType = plType;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public java.util.Date getRepayDate() {
        return repayDate;
    }
    public void setRepayDate(java.util.Date repayDate) {
        this.repayDate = repayDate;
    }
    public java.util.Date getInfoStartDate() {
        return infoStartDate;
    }
    public void setInfoStartDate(java.util.Date infoStartDate) {
        this.infoStartDate = infoStartDate;
    }
    public String getPrecativeName() {
        return precativeName;
    }
    public void setPrecativeName(String precativeName) {
        this.precativeName = precativeName;
    }
    public String getLoanOppName() {
        return loanOppName;
    }
    public void setLoanOppName(String loanOppName) {
        this.loanOppName = loanOppName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getTernUnitStatus() {
        return ternUnitStatus;
    }
    public void setTernUnitStatus(String ternUnitStatus) {
        this.ternUnitStatus = ternUnitStatus;
    }
    public String getPlName() {
        return plName;
    }
    public void setPlName(String plName) {
        this.plName = plName;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public java.math.BigDecimal getCounts() {
        return counts;
    }
    public void setCounts(java.math.BigDecimal counts) {
        this.counts = counts;
    }
    public String getConId() {
        return conId;
    }
    public void setConId(String conId) {
        this.conId = conId;
    }
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }
}
