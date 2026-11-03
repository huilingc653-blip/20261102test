package com.bytter.biz.inneraccount.vo.v1;

import com.bytter.core.annotation.BtCompositeSearch;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.NumberFormat;

import java.io.Serializable;
import java.math.BigDecimal;

@ApiModel("内部账户明细信息")
public class FcAccDetailVO implements Serializable {
    @ApiModelProperty(value = "主键ID")
    private String id;
    /**账号*/
    @BtCompositeSearch(fieldName = "account")
    @ApiModelProperty(value = "账号")
    private String account;
    /**凭证号*/
    @BtCompositeSearch(fieldName = "voucherNo")
    @ApiModelProperty(value = "凭证号")
    private String voucherNo;
    /**户名*/
    @BtCompositeSearch(fieldName = "accName")
    @ApiModelProperty(value = "户名")
    private String accName;
    /**单位*/
    @BtCompositeSearch(fieldName = "corpId")
    @ApiModelProperty(value = "单位")
    private String corpId;
    /**币别*/
    @BtCompositeSearch(fieldName = "curId")
    @ApiModelProperty(value = "币别")
    private String curId;
    @ApiModelProperty(value = "币别名称")
    private String curName;
    /**记账日期*/
    @BtCompositeSearch(fieldName = "tallyDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "记账日期")
    private java.util.Date tallyDate;

    /**起息日期*/
    @BtCompositeSearch(fieldName = "interestStartDate")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "起息日期")
    private java.util.Date interestStartDate;
    /**摘要*/
    @BtCompositeSearch(fieldName = "summaryContent")
    @ApiModelProperty(value = "摘要")
    private String summaryContent;
    /**借方发生额(支出)*/
    @BtCompositeSearch(fieldName = "debitMoney")
    @NumberFormat(pattern = "#,##0.00")
    @ApiModelProperty(value = "借方发生额(支出)")
    private BigDecimal debitMoney;
    /**贷方发生额(收入)*/
    @BtCompositeSearch(fieldName = "creditMoney")
    @NumberFormat(pattern = "#,##0.00")
    @ApiModelProperty(value = "贷方发生额(收入)")
    private BigDecimal creditMoney;
    /**凭证种类代码(付款方式)*/
    @BtCompositeSearch(fieldName = "voucherType")
    @ApiModelProperty(value = "凭证种类代码(付款方式)")
    private String voucherType;
    @ApiModelProperty(value = "凭证名称")
    private String voucherName;
    /**余额*/
    @BtCompositeSearch(fieldName = "balance")
    @NumberFormat(pattern = "#,##0.00")
    @ApiModelProperty(value = "余额")
    private BigDecimal balance;
    /**外单位名称*/
    @BtCompositeSearch(fieldName = "externalName")
    @ApiModelProperty(value = "外单位名称")
    private String externalName;
    /**外单位账号*/
    @ApiModelProperty(value = "外单位账号")
    private String externalAcc;
    /**原凭证号(付款单号)*/
    @BtCompositeSearch(fieldName = "origVoucherNo")
    @ApiModelProperty(value = "原凭证号(付款单号)")
    private String origVoucherNo;
    @ApiModelProperty(value = "系统凭证代码")
    private String sysVoucherCode;
    /**账户类别*/
    @BtCompositeSearch(fieldName = "accType")
    @ApiModelProperty(value = "账户类别")
    private String accType;
    public String getVoucherName() {
        return voucherName;
    }
    public void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getVoucherNo() {
        return voucherNo;
    }
    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }
    public String getAccName() {
        return accName;
    }
    public void setAccName(String accName) {
        this.accName = accName;
    }
    public String getCorpId() {
        return corpId;
    }
    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }
    public String getCurId() {
        return curId;
    }
    public void setCurId(String curId) {
        this.curId = curId;
    }
    public String getCurName() {
        return curName;
    }
    public void setCurName(String curName) {
        this.curName = curName;
    }
    public java.util.Date getTallyDate() {
        return tallyDate;
    }
    public void setTallyDate(java.util.Date tallyDate) {
        this.tallyDate = tallyDate;
    }
    public java.util.Date getInterestStartDate() {
        return interestStartDate;
    }
    public void setInterestStartDate(java.util.Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }
    public String getSummaryContent() {
        return summaryContent;
    }
    public void setSummaryContent(String summaryContent) {
        this.summaryContent = summaryContent;
    }
    public BigDecimal getDebitMoney() {
        return debitMoney;
    }
    public void setDebitMoney(BigDecimal debitMoney) {
        this.debitMoney = debitMoney;
    }
    public BigDecimal getCreditMoney() {
        return creditMoney;
    }
    public void setCreditMoney(BigDecimal creditMoney) {
        this.creditMoney = creditMoney;
    }
    public String getVoucherType() {
        return voucherType;
    }
    public void setVoucherType(String voucherType) {
        this.voucherType = voucherType;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public String getExternalName() {
        return externalName;
    }
    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
    public String getExternalAcc() {
        return externalAcc;
    }
    public void setExternalAcc(String externalAcc) {
        this.externalAcc = externalAcc;
    }
    public String getOrigVoucherNo() {
        return origVoucherNo;
    }
    public void setOrigVoucherNo(String origVoucherNo) {
        this.origVoucherNo = origVoucherNo;
    }
    public String getSysVoucherCode() {
        return sysVoucherCode;
    }
    public void setSysVoucherCode(String sysVoucherCode) {
        this.sysVoucherCode = sysVoucherCode;
    }
    public String getAccType() {
        return accType;
    }
    public void setAccType(String accType) {
        this.accType = accType;
    }
}
