package com.bytter.biz.inneraccount.vo;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 * @author qijh
 * @Description 手机账号明细vo
 */
public class BtBankAccMobileDtlVO implements Serializable {
    /** 币别 */
    private String curName;
    /** 银行账号 */
    private String bankcc;
    /** 对方账号 */
    private String oppAccNo;
    /** 对方账号名称 */
    private String oppAccName;
    /** 摘要 */
    private String remark;
    /** 收支标志 */
    private String cdSign;
    /** 余额 */
    private BigDecimal bal;
    /** 交易金额 */
    private BigDecimal amt;
    /** 交易时间 */
    private Timestamp transTime;
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public String getBankcc() {
        return bankcc;
    }
    public void setBankcc(String bankcc) {
        this.bankcc = bankcc;
    }
    public String getOppAccNo() {
        return oppAccNo;
    }
    public void setOppAccNo(String oppAccNo) {
        this.oppAccNo = oppAccNo;
    }
    public String getOppAccName() {
        return oppAccName;
    }
    public void setOppAccName(String oppAccName) {
        this.oppAccName = oppAccName;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getCdSign() {
        return cdSign;
    }
    public void setCdSign(String cdSign) {
        this.cdSign = cdSign;
    }
    public BigDecimal getBal() {
        return bal;
    }
    public void setBal(BigDecimal bal) {
        this.bal = bal;
    }
    public BigDecimal getAmt() {
        return amt;
    }
    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
    public Timestamp getTransTime() {
        return transTime;
    }
    public void setTransTime(Timestamp transTime) {
        this.transTime = transTime;
    }
}
