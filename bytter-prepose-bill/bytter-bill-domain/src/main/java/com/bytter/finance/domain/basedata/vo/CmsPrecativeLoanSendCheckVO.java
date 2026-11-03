package com.bytter.finance.domain.basedata.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * 信贷委托贷款检查Bean
 * 
 * @author liyh
 *
 */
public class CmsPrecativeLoanSendCheckVO implements Serializable {
    /**
     * 贷款账号
     */
    private String loanAcc;
    /**
     * 委托人
     */
    private String precativeId;
    /**
     * 委托人
     */
    private String loanOpp;
    /**
     * 合同号
     */
    private String contractCode;
    /**
     * 贷款金额
     */
    private BigDecimal money;
    /**
     * 起息日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    /**
     * 到期日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    /**
     * 利率
     */
    private BigDecimal rate;
    /**
     * 币别
     */
    private String curName;
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
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
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    public BigDecimal getMoney() {
        return money;
    }
    public void setMoney(BigDecimal money) {
        this.money = money;
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
    public BigDecimal getRate() {
        return rate;
    }
    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
}
