package com.bytter.biz.capital.entity;
import com.bytter.core.base.entity.IdEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
/**
 * @author shenc
 * @date 2019.3.28
 * 资金备案台账
 */
@Entity
@Table(name = "NIS_CAPITAL_RECORD",schema = "")
public class NisCapitalRecord extends IdEntity implements java.io.Serializable {
    /**付款单号 年年月月日日+6位序列号(从000000开始)*/
    private java.lang.String billNo;
    /**单位ID*/
    private java.lang.String corpId;
    /**收款账号*/
    private java.lang.String payeeAcc;
    /**收款账号名称*/
    private java.lang.String payeeAccName;
    /**收款方开户行行号*/
    private java.lang.String payeeBankCode;
    /**收款开户行名称*/
    private java.lang.String payeeBank;
    /**金额*/
    private java.math.BigDecimal payMoney;
    /**期望支付日期*/
    private java.util.Date wishPayDay;
    /**结算中心Id*/
    private java.lang.String netId;
    /**付款方式*/
    private java.lang.String voucherType;
    /**资金备案类别*/
    private java.lang.String busType;
    /**付款事由*/
    private java.lang.String purpose;
    /**项目名称*/
    private java.lang.String projectName;
    /**合同编号*/
    private java.lang.String contractCode;
    /**合同名称*/
    private java.lang.String contractName;
    /**合同类型*/
    private java.lang.String contractType;
    /**状态*/
    private java.lang.Integer status;
    /**附件Id*/
    private java.lang.String attachmentId;
    /**创建人登录姓名*/
    private java.lang.String createBy;
    /**创建日期*/
    private java.util.Date createDate;
    /**更新人登录名称*/
    private java.lang.String updateBy;
    /**更新日期*/
    private java.util.Date updateDate;
    @Column(name="BILL_NO",length = 12,nullable = false)
    public String getBillNo() {
        return billNo;
    }
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }
    @Column(name="CORP_ID",length = 32,nullable = false)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    @Column(name="PAYEE_ACC",length = 40)
    public String getPayeeAcc() {
        return payeeAcc;
    }
    public void setPayeeAcc(String payeeAcc) {
        this.payeeAcc = payeeAcc;
    }
    @Column(name="PAYEE_ACC_NAME",length = 50)
    public String getPayeeAccName() {
        return payeeAccName;
    }
    public void setPayeeAccName(String payeeAccName) {
        this.payeeAccName = payeeAccName;
    }
    @Column(name="PAYEE_BANK_CODE",length = 20)
    public String getPayeeBankCode() {
        return payeeBankCode;
    }
    public void setPayeeBankCode(String payeeBankCode) {
        this.payeeBankCode = payeeBankCode;
    }
    @Column(name="PAYEE_BANK",length = 150)
    public String getPayeeBank() {
        return payeeBank;
    }
    public void setPayeeBank(String payeeBank) {
        this.payeeBank = payeeBank;
    }
    @Column(name="PAY_MONEY",scale = 2,length = 15,nullable = false)
    public java.math.BigDecimal getPayMoney() {
        return payMoney;
    }
    public void setPayMoney(java.math.BigDecimal payMoney) {
        this.payMoney = payMoney;
    }
    @Column(name="WISH_PAYDAY")
    public Date getWishPayDay() {
        return wishPayDay;
    }
    public void setWishPayDay(Date wishPayDay) {
        this.wishPayDay = wishPayDay;
    }
    @Column(name="NET_ID",length = 32,nullable = false)
    public String getNetId() {
        return netId;
    }
    public void setNetId(String netId) {
        this.netId = netId;
    }
    @Column(name = "VOUCHER_TYPE",length = 30,nullable = false)
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    @Column(name = "BUS_TYPE",length = 30)
    public String getBusType() {
        return busType;
    }
    public void setBusType(String busType) {
        this.busType = busType;
    }
    @Column(name = "PURPOSE",length = 100,nullable = false)
    public String getPurpose() {
        return purpose;
    }
    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
    @Column(name = "PROJECT_NAME",length = 60)
    public String getProjectName() {
        return projectName;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Column(name = "CONTRACT_CODE",length = 30)
    public String getContractCode() {
        return contractCode;
    }
    public void setContractCode(String contractCode) {
        this.contractCode = contractCode;
    }
    @Column(name = "CONTRACT_NAME",length = 60)
    public String getContractName() {
        return contractName;
    }
    public void setContractName(String contractName) {
        this.contractName = contractName;
    }
    @Column(name = "CONTRACT_TYPE",length = 30)
    public String getContractType() {
        return contractType;
    }
    public void setContractType(String contractType) {
        this.contractType = contractType;
    }
    @Column(name = "STATUS",length = 10)
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "ATTACHMENT_ID",length = 32)
    public String getAttachmentId() {
        return attachmentId;
    }
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }
    @Column(name = "CREATE_BY",length = 32)
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    @Column(name = "CREATE_DATE",length = 6)
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    @Column(name = "UPDATE_BY",length = 32)
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    @Column(name = "UPDATE_DATE",length = 6)
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
