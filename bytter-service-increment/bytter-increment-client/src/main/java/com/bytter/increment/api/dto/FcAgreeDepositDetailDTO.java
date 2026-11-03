package com.bytter.increment.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
/**
* <br>
* <b>功能：</b> 协定存款明细Bean <br>
* <b>作者：</b>liyh<br>
* <b>日期：</b> 2019/5/14 <br>
*/
public class FcAgreeDepositDetailDTO implements Serializable {
    /**
     * 协定账号
     */
    private String account;
    /**
     * 协定类型段
     */
    private Integer lineNo;
    /**
     * 协定存款ID
     */
    private String agreeDepositId;
    /**
     * 下限金额
     */
    private BigDecimal minMoney;
    /**
     * 上限金额
     */
    private BigDecimal maxMoney;
    /**
    * 协定利率
    */
    private BigDecimal agreeRate;
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Integer getLineNo() {
        return lineNo;
    }
    public void setLineNo(Integer lineNo) {
        this.lineNo = lineNo;
    }
    public String getAgreeDepositId() {
        return agreeDepositId;
    }
    public void setAgreeDepositId(String agreeDepositId) {
        this.agreeDepositId = agreeDepositId;
    }
    public BigDecimal getMinMoney() {
        return minMoney;
    }
    public void setMinMoney(BigDecimal minMoney) {
        this.minMoney = minMoney;
    }
    public BigDecimal getMaxMoney() {
        return maxMoney;
    }
    public void setMaxMoney(BigDecimal maxMoney) {
        this.maxMoney = maxMoney;
    }
    public BigDecimal getAgreeRate() {
        return agreeRate;
    }
    public void setAgreeRate(BigDecimal agreeRate) {
        this.agreeRate = agreeRate;
    }
}
