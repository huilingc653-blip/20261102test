package com.bytter.system.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
/**
 * @author qijh
 * @Description 手机账号余额vo
 */
public class BtBankAccMobileBalVO implements Serializable {
    /** 银行账号 */
    private String bankAcc;
    /** 开户行 */
    private String bankName;
    /** 币别 */
    private String curName;
    /** 余额 */
    private BigDecimal bal;
    /** 余额时间 */
    private Date balDate;
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
}
