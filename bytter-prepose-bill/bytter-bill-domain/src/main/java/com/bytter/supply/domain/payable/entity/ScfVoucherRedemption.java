package com.bytter.supply.domain.payable.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "SCF_VOUCHER_REDEMPTION", schema = "")
@SuppressWarnings("serial")
public class ScfVoucherRedemption extends IdEntity implements Serializable {
    /**
     * 应付凭证ID
     */
    private String voucherId;
    /**
     * 兑付单号
     */
    private String cashNo;
    /**
     * 平台id
     */
    private String platformId;
    /**
     * 融资产品
     */
    private String productId;
    /**
     * 开立单位
     */
    private String corpId;
    /**
     * 开立日期
     */
    private Date openDate;
    /**
     * 到期日
     */
    private Date expireDate;
    /**
     * 核心企业
     */
    private String coreCorp;
    /**
     * 币别
     */
    private String curId;
    /**
     * 凭证金额
     */
    private BigDecimal voucherMoney;
    /**
     * 供应商
     */
    private String supplyId;

    private String supplyName;
    /**
     * 付款账号
     */
    private String payAccount;
    /**
     * 兑付金额
     */
    private BigDecimal cashMoney;
    /**
     * 付款日期
     */
    private Date payDate;
    /**
     * 收款方名称
     */
    private String receiveName;
    /**
     * 收款方账号
     */
    private String receiveAccount;
    /**
     * 收款开户行
     */
    private String receiveBank;

    /** 审批状态 */
    private String approvalStatus;


    @Column(name = "APPROVAL_STATUS")
    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }


    @Column(name = "VOUCHER_ID")
    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    @Column(name = "CASH_NO")
    public String getCashNo() {
        return cashNo;
    }

    public void setCashNo(String cashNo) {
        this.cashNo = cashNo;
    }

    @Column(name = "PLATFORM_ID")
    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Column(name = "PRODUCT_ID")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Column(name = "CORP_ID")
    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name = "OPEN_DATE")
    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    @Column(name = "EXPIRE_DATE")
    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    @Column(name = "CORE_CORP")
    public String getCoreCorp() {
        return coreCorp;
    }

    public void setCoreCorp(String coreCorp) {
        this.coreCorp = coreCorp;
    }

    @Column(name = "CUR_ID")
    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name = "VOUCHER_MONEY")
    public BigDecimal getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(BigDecimal voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    @Column(name = "SUPPLY_ID")
    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    @Column(name = "SUPPLY_NAME")
    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    @Column(name = "PAY_ACCOUNT")
    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    @Column(name = "CASH_MONEY")
    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    @Column(name = "PAY_DATE")
    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    @Column(name = "RECEIVE_NAME")
    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    @Column(name = "RECEIVE_ACCOUNT")
    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    @Column(name = "RECEIVE_BANK")
    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }
}
