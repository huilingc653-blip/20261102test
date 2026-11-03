package com.bytter.biz.dataexchange.basic.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
/**
 * @author qijh
 */
@Entity
@Table(name = "ERP_WAGE_DETAIL", schema = "")
public class ErpWageDetail extends IdEntity implements java.io.Serializable{
    /**主表外系统ID ERP_PAYINFO（SERIAL_NO_ERP）*/
    private String serialNo;
    /**工号*/
    private String workNo;
    /**姓名*/
    private String userName;
    /**银行账号*/
    private String bankAcc;
    /**户名*/
    private String accName;
    /**实发工资*/
    private BigDecimal wages;
    /**工资用途*/
    private String purpose;
    /**开户行行号*/
    private String bankCode;
    /**开户银行名称*/
    private String bankName;
    /**交易状态*/
    private String voucherStat;
    /**交易信息*/
    private String retrunMsg;
    @Column(name ="SERIAL_NO")
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    @Column(name ="WORK_NO")
    public String getWorkNo() {
        return workNo;
    }
    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
    @Column(name ="USER_NAME")
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Column(name ="BANK_ACC")
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    @Column(name ="ACC_NAME")
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    @Column(name ="WAGES")
    public BigDecimal getWages() {
        return wages;
    }
    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }
    @Column(name ="PURPOSE")
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name ="BANK_CODE")
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    @Column(name ="BANK_NAME")
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    @Column(name ="VOUCHER_STAT")
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    @Column(name ="RETRUN_MSG")
    public String getRetrunMsg() {
        return retrunMsg;
    }
    public void setRetrunMsg(String retrunMsg) {
        this.retrunMsg = retrunMsg;
    }
}
