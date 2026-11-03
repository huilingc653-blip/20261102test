package com.bytter.finance.domain.loan.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.bytter.core.annotation.BtCompositeSearch;
import com.bytter.core.annotation.Corp;
import com.bytter.core.annotation.Currency;
import com.fasterxml.jackson.annotation.JsonFormat;
public class CmsLoanRepayTallyQueryVO implements Serializable {
    private String id;
    /** 还款单号 */
    @BtCompositeSearch(fieldName = "r.bill_code")
    private String billCode;
    /** 状态 */
    private Integer status;
    /** 显示状态 */
    private String bizStatus;
    /** 网点 */
    @BtCompositeSearch(fieldName = "i.net_code")
    @Corp(inject = "netName")
    private String netCode;
    private String netName;
    /** 单位 */
    @Corp(inject = "corpName")
    private String corpId;
    private String corpName;
    /** 币别 */
    @Currency(inject = "curName")
    private String curId;
    private String curName;
    /** 合同号 */
    @BtCompositeSearch(fieldName = "r.contract_code")
    private String contractCode;
    /** 发放单号 */
    @BtCompositeSearch(fieldName = "s.bill_code")
    private String sendBillCode;
    /** 放款账号 */
    private String provideLoanAcc;
    /** 放款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date loanDate;
    /** 还款日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date repayDate;
    /** 还款账号 **/
    private String repayAcc;
    /** 放款金额 */
    private BigDecimal loanMoney;
    /** 还款金额 */
    private BigDecimal repayMoney;
    /** 还款利息 */
    private BigDecimal repayAccrual;
    /** 还款类型 */
    private String repayType;
    /** 还款方式 */
    private String repayway;
    private String repaywayName;
    /** 还款放款账号 **/
    private String loanAcc;
    /** 起息日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;
    /** 记账日期 **/
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date tallyDate;

    public String getRepaywayName() {
        return repaywayName;
    }

    public void setRepaywayName(String repaywayName) {
        this.repaywayName = repaywayName;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBillCode() {
        return billCode;
    }
    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBizStatus() {
        return bizStatus;
    }
    public void setBizStatus(String bizStatus) {
        this.bizStatus = bizStatus;
    }
    public String getNetCode() {
        return netCode;
    }
    public void setNetCode(String netCode) {
        this.netCode = netCode;
    }
    public String getNetName() {
        return netName;
    }
    public void setNetName(String netName) {
        this.netName = netName;
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
    public String getProvideLoanAcc() {
        return provideLoanAcc;
    }
    public void setProvideLoanAcc(String provideLoanAcc) {
        this.provideLoanAcc = provideLoanAcc;
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
    public BigDecimal getLoanMoney() {
        return loanMoney;
    }
    public void setLoanMoney(BigDecimal loanMoney) {
        this.loanMoney = loanMoney;
    }
    public BigDecimal getRepayMoney() {
        return repayMoney;
    }
    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }
    public BigDecimal getRepayAccrual() {
        return repayAccrual;
    }
    public void setRepayAccrual(BigDecimal repayAccrual) {
        this.repayAccrual = repayAccrual;
    }
    public String getRepayType() {
        return repayType;
    }
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }
    public String getRepayway() {
        return repayway;
    }
    public void setRepayway(String repayway) {
        this.repayway = repayway;
    }
    public String getRepayAcc() {
        return repayAcc;
    }
    public void setRepayAcc(String repayAcc) {
        this.repayAcc = repayAcc;
    }
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }
}
