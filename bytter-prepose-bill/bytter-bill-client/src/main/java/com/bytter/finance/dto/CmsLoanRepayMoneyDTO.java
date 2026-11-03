package com.bytter.finance.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
 * <br>
 * <b>功能：</b> 内部贷款户还款信息 <br>
 * <b>作者：</b>liyh<br>
 * <b>日期：</b> 2021/3/12 0012 <br>
 */
public class CmsLoanRepayMoneyDTO implements Serializable {
    // 放款账户
    private String loanAcc;
    private String loanAccName;
    // 贷款金额
    private BigDecimal loanMoney = BigDecimal.ZERO;
    // 本次归还金额
    private BigDecimal repayMoney = BigDecimal.ZERO;
    // 累计还款金额
    private BigDecimal sumRepayMoney = BigDecimal.ZERO;
    // 剩余金额
    private BigDecimal leftMoney = BigDecimal.ZERO;
    // 扣息活期账户
    private String interestAcc;
    private String interestAccName;
    public String getLoanAcc() {
        return loanAcc;
    }
    public void setLoanAcc(String loanAcc) {
        this.loanAcc = loanAcc;
    }
    public String getLoanAccName() {
        return loanAccName;
    }
    public void setLoanAccName(String loanAccName) {
        this.loanAccName = loanAccName;
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
    public BigDecimal getSumRepayMoney() {
        return sumRepayMoney;
    }
    public void setSumRepayMoney(BigDecimal sumRepayMoney) {
        this.sumRepayMoney = sumRepayMoney;
    }
    public BigDecimal getLeftMoney() {
        return leftMoney;
    }
    public void setLeftMoney(BigDecimal leftMoney) {
        this.leftMoney = leftMoney;
    }
    public String getInterestAcc() {
        return interestAcc;
    }
    public void setInterestAcc(String interestAcc) {
        this.interestAcc = interestAcc;
    }
    public String getInterestAccName() {
        return interestAccName;
    }
    public void setInterestAccName(String interestAccName) {
        this.interestAccName = interestAccName;
    }
}
