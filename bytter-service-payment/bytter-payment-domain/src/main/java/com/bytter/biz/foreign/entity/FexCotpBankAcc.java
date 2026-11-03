package com.bytter.biz.foreign.entity;

import com.bytter.core.base.entity.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "FEX_COTP_BANKACC", schema = "")
public class FexCotpBankAcc extends IdEntity implements java.io.Serializable  {
    /**交易对手id*/
    private String cotpId;
    /**单位id*/
    private String corpId;
    /**交易对手银行账户*/
    private String bankAcc;
    /**Iban号码*/
    private String iBanCode;
    /**账户名称*/
    private String accName;
    /**账户币种*/
    private String curId;
    /**备注*/
    private String rmk;
    /**银行*/
    private String bankId;
    /**BIC编码*/
    private String bicCode;
    /**支付类型*/
    private String payType;
    /**中转行的银行账户*/
    private String tranBankAcc;
    /**银行地址信息*/
    private String bankAddr;
    /**银行信息备注*/
    private String bankRmk;
    /**中转行*/
    private String tranBank;
    /**中转银行BIC编码*/
    private String tranBicCode;
    /**中转银行地址信息*/
    private String tranBankAddr;
    /**交易账户所属银行*/
    private String bankName;
    /**数据状态*/
    private Integer status;
    /**创建人登录名称*/
    private String createBy;
    /**创建日期*/
    private Date createDate;
    /**更新人登录名称*/
    private String updateBy;
    /**更新日期*/
    private Date updateDate;

    @Column(name ="COTP_ID",nullable=false,length=32)
    public String getCotpId() {
        return cotpId;
    }
    public void setCotpId(String cotpId) {
        this.cotpId = cotpId;
    }

    @Column(name ="CORP_ID",nullable=false,length=32)
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    @Column(name ="BANK_ACC",nullable=false,length=100)
    public String getBankAcc() {
        return bankAcc;
    }
    public void setBankAcc(String bankAcc) {
        this.bankAcc = bankAcc;
    }

    @Column(name ="IBAN_CODE",nullable=false,length=100)
    public String getiBanCode() {
        return iBanCode;
    }
    public void setiBanCode(String iBanCode) {
        this.iBanCode = iBanCode;
    }

    @Column(name ="ACC_NAME",nullable=false,length=100)
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }

    @Column(name ="CUR_ID",nullable=false,length=32)
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }

    @Column(name ="RMK",nullable=false,length=200)
    public String getRmk() {
        return rmk;
    }
    public void setRmk(String rmk) {
        this.rmk = rmk;
    }

    @Column(name ="BANK_ID",nullable=false,length=32)
    public String getBankId() {
        return bankId;
    }
    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    @Column(name ="BIC_CODE",nullable=false,length=100)
    public String getBicCode() {
        return bicCode;
    }
    public void setBicCode(String bicCode) {
        this.bicCode = bicCode;
    }

    @Column(name ="PAY_TYPE",nullable=false,length=32)
    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }

    @Column(name ="TRAN_BANK_ACC",nullable=false,length=100)
    public String getTranBankAcc() {
        return tranBankAcc;
    }
    public void setTranBankAcc(String tranBankAcc) {
        this.tranBankAcc = tranBankAcc;
    }

    @Column(name ="BANK_ADDR",nullable=false,length=200)
    public String getBankAddr() {
        return bankAddr;
    }
    public void setBankAddr(String bankAddr) {
        this.bankAddr = bankAddr;
    }

    @Column(name ="BANK_RMK",nullable=false,length=200)
    public String getBankRmk() {
        return bankRmk;
    }
    public void setBankRmk(String bankRmk) {
        this.bankRmk = bankRmk;
    }

    @Column(name ="TRAN_BANK",nullable=false,length=200)
    public String getTranBank() {
        return tranBank;
    }
    public void setTranBank(String tranBank) {
        this.tranBank = tranBank;
    }

    @Column(name ="TRAN_BIC_CODE",nullable=false,length=100)
    public String getTranBicCode() {
        return tranBicCode;
    }
    public void setTranBicCode(String tranBicCode) {
        this.tranBicCode = tranBicCode;
    }

    @Column(name ="TRAN_BANK_ADDR",nullable=false,length=200)
    public String getTranBankAddr() {
        return tranBankAddr;
    }
    public void setTranBankAddr(String tranBankAddr) {
        this.tranBankAddr = tranBankAddr;
    }

    @Column(name ="BANK_NAME",nullable=false,length=255)
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @Column(name ="STATUS")
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name ="CREATE_BY")
    public String getCreateBy() {
        return createBy;
    }
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Column(name ="CREATE_DATE")
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Column(name ="UPDATE_BY")
    public String getUpdateBy() {
        return updateBy;
    }
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    @Column(name ="UPDATE_DATE")
    public Date getUpdateDate() {
        return updateDate;
    }
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
