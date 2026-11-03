package com.bytter.biz.bdlink.vo;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author onlineGenerator
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK_ACC
 * @date 2017-06-02 17:50:16
 */
public class BtBankAccBalVO implements Serializable {
    private String id;
	/**
	 * 银行账号
	 */
	private String bankAcc;
    /**
     * 币别
     */
	private String curId;
    /**
     * 余额日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date balDate;
    /**
     * 获取时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date getDate;
    /**
     * 余额
     */
    private BigDecimal bal;
    /**
     * 可用余额
     */
    private BigDecimal availBal;
    /**
     * 冻结金额
     */
    private BigDecimal frzBal;
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
    public Date getGetDate() {
        return getDate;
    }
    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public BigDecimal getAvailBal() {
        return availBal;
    }
    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }
    public BigDecimal getFrzBal() {
        return frzBal;
    }
    public void setFrzBal(BigDecimal frzBal) {
        this.frzBal = frzBal;
    }
}
