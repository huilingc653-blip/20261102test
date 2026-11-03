package com.bytter.biz.dataexchange.basic.vo;
import com.bytter.core.annotation.BtCompositeSearch;
import java.io.Serializable;
import java.math.BigDecimal;
public class ErpWageDetailVO implements Serializable {
    private String id;
    /**主表外系统ID ERP_PAYINFO（SERIAL_NO_ERP）*/
    @BtCompositeSearch(fieldName = "e.SERIAL_NO")
    private String serialNo;
    /**工号*/
    @BtCompositeSearch(fieldName = "e.WORK_NO")
    private String workNo;
    /**姓名*/
    @BtCompositeSearch(fieldName = "e.USER_NAME")
    private String userName;
    /**银行账号*/
    @BtCompositeSearch(fieldName = "e.BANK_ACC")
    private String bankAcc;
    /**户名*/
    @BtCompositeSearch(fieldName = "e.ACC_NAME")
    private String accName;
    /**实发工资*/
    @BtCompositeSearch(fieldName = "e.WAGES")
    private BigDecimal wages;
    /**工资用途*/
    @BtCompositeSearch(fieldName = "e.PURPOSE")
    private String purpose;
    /**开户行行号*/
    @BtCompositeSearch(fieldName = "e.BANK_CODE")
    private String bankCode;
    /**开户银行名称*/
    @BtCompositeSearch(fieldName = "e.BANK_NAME")
    private String bankName;
    /**交易状态*/
    @BtCompositeSearch(fieldName = "e.VOUCHER_STAT")
    private String voucherStat;
    /**交易信息*/
    @BtCompositeSearch(fieldName = "e.RETRUN_MSG")
    private String retrunMsg;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getSerialNo() {
        return serialNo;
    }
    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }
    public String getWorkNo() {
        return workNo;
    }
    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public BigDecimal getWages() {
        return wages;
    }
    public void setWages(BigDecimal wages) {
        this.wages = wages;
    }
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    public String getBankCode() {
        return bankCode;
    }
    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public String getRetrunMsg() {
        return retrunMsg;
    }
    public void setRetrunMsg(String retrunMsg) {
        this.retrunMsg = retrunMsg;
    }
}
