package com.bytter.biz.finacial.api.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * 内部账户每日余额DTO
 *
 * @author liyh
 */
public class FcAccBalanceDTO implements Serializable {
    /**
     * 余额日期
     */
    private java.util.Date balanceDate;
    /**
     * 账号
     */
    private java.lang.String account;
    /**
     * 币别代码
     */
    private java.lang.String curId;
    /**
     * 账户名称
     */
    private java.lang.String accName;
    /**
     * 内转笔数
     */
    private java.lang.Integer trCnt;
    /**
     * 冲正笔数
     */
    private java.lang.Integer rushCnt;
    /**
     * 笔数
     */
    private java.lang.Integer cnt;
    /**
     * 昨日余额
     */
    private java.math.BigDecimal yesterBalance;
    /**
     * 余额
     */
    private java.math.BigDecimal balance;
    /**网点ID*/
    private String netId;

    public Date getBalanceDate() {
        return balanceDate;
    }
    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public Integer getTrCnt() {
        return trCnt;
    }
    public void setTrCnt(Integer trCnt) {
        this.trCnt = trCnt;
    }
    public Integer getRushCnt() {
        return rushCnt;
    }
    public void setRushCnt(Integer rushCnt) {
        this.rushCnt = rushCnt;
    }
    public Integer getCnt() {
        return cnt;
    }
    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }
    public BigDecimal getYesterBalance() {
        return yesterBalance;
    }
    public void setYesterBalance(BigDecimal yesterBalance) {
        this.yesterBalance = yesterBalance;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getNetId() {
        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }
}
