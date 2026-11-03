package com.bytter.erp.dto;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
/**
 * @author qijh
 */
public class NisWageDetailDTO implements Serializable {
    private String id;
    /**单据号码*/
    private String wageDtlNo;
    /**代发工资ID*/
    private String wageId;
    /**工号*/
    private String workNo;
    /**姓名*/
    private String userName;
    /**银行账号*/
    private String bankAcc;
    /**账户名*/
    private String accName;
    /**实发工资*/
    private String wagesVerify;
    /**实发工资*/
    private BigDecimal wages;
    /**用途*/
    @NotNull(message = "nis.wage.message.purpose.null")
    private String purpose;
    /**所在省*/
    private String prov;
    private String provName;
    /**所在市*/
    private String city;
    private String cityName;
    /**开户行行号*/
    private String bankCode;
    /**开户银行名称*/
    private String bankName;
    /**跨行标识 replace={"同行_0","他行_1"}*/
    private String difBank;
    /**地域标识, replace={"同城_0","异地_1"}*/
    private String areaSign;
    /**交易状态*/
    private String voucherStat;
    /**返回时间*/
    private Date returnTime;
    /**返回信息（交易前）*/
    private String firstReturnMsg;
    /**返回信息（交易后）*/
    private String nextReturnMsg;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;
    /**指令单号*/
    private String voucherNo;
    /**证件类型*/
    private String idType;
    /**身份证号（部分银行需要身份证信息，已知北京银行、沧州银行）*/
    private String idNo;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getWageDtlNo() {
        return wageDtlNo;
    }
    public void setWageDtlNo(String wageDtlNo) {
        this.wageDtlNo = wageDtlNo;
    }
    public String getWageId() {
        return wageId;
    }
    public void setWageId(String wageId) {
        this.wageId = wageId;
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
    public String getWagesVerify() {
        return wagesVerify;
    }
    public void setWagesVerify(String wagesVerify) {
        this.wagesVerify = wagesVerify;
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
    public String getProv() {
        return prov;
    }
    public void setProv(String prov) {
        this.prov = prov;
    }
    public String getProvName() {
        return provName;
    }
    public void setProvName(String provName) {
        this.provName = provName;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
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
    public String getDifBank() {
        return difBank;
    }
    public void setDifBank(String difBank) {
        this.difBank = difBank;
    }
    public String getAreaSign() {
        return areaSign;
    }
    public void setAreaSign(String areaSign) {
        this.areaSign = areaSign;
    }
    public String getVoucherStat() {
        return voucherStat;
    }
    public void setVoucherStat(String voucherStat) {
        this.voucherStat = voucherStat;
    }
    public Date getReturnTime() {
        return returnTime;
    }
    public void setReturnTime(Date returnTime) {
        this.returnTime = returnTime;
    }
    public String getFirstReturnMsg() {
        return firstReturnMsg;
    }
    public void setFirstReturnMsg(String firstReturnMsg) {
        this.firstReturnMsg = firstReturnMsg;
    }
    public String getNextReturnMsg() {
        return nextReturnMsg;
    }
    public void setNextReturnMsg(String nextReturnMsg) {
        this.nextReturnMsg = nextReturnMsg;
    }
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getIdType() {
        return idType;
    }
    public void setIdType(String idType) {
        this.idType = idType;
    }
    public String getIdNo() {
        return idNo;
    }
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }
}
