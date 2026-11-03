package com.bytter.biz.bdlink.api.dto;
import com.bytter.core.base.entity.IdEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author huangx
 * @version 1.0
 * @Title: Entity
 * @Description: BT_BANK_ACC_BAL
 * @date 2022-05-25 17:50:16
 */
public class BtBankAccBalDTO extends IdEntity implements java.io.Serializable {
    /**
     * 银行账号
     */
    private String bankAcc;
    /**
     * 币种
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
    //@Column(name = "BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    //@Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    //@Column(name = "BAL_DATE")
    public Date getBalDate() {
        return balDate;
    }
    public void setBalDate(Date balDate) {
        this.balDate = balDate;
    }
    //@Column(name = "GET_DATE")
    public Date getGetDate() {
        return getDate;
    }
    public void setGetDate(Date getDate) {
        this.getDate = getDate;
    }
    //@Column(name = "BAL")
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    //@Column(name = "AVAIL_BAL")
    public BigDecimal getAvailBal() {
        return availBal;
    }
    public void setAvailBal(BigDecimal availBal) {
        this.availBal = availBal;
    }
    //@Column(name = "FRZ_BAL")
    public BigDecimal getFrzBal() {
        return frzBal;
    }
    public void setFrzBal(BigDecimal frzBal) {
        this.frzBal = frzBal;
    }
}
