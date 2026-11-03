package com.bytter.supply.domain.payable.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@ApiModel("凭证兑付记录DTO")
public class ScfVoucherRedemptionDTO implements Serializable {
    @ApiModelProperty("主键ID")
    private String id;
    @ApiModelProperty("应付凭证ID")
    private String voucherId;
    @ApiModelProperty("应付单号")
    private String voucherNo;
    @ApiModelProperty("债权凭证编号")
    private String voucherCode;
    @ApiModelProperty("兑付单号")
    private String cashNo;
    @ApiModelProperty("平台")
    private String platformName;
    @ApiModelProperty("平台id")
    private String platformId;
    @ApiModelProperty("融资产品")
    private String productId;
    @ApiModelProperty("开立单位")
    private String corpId;
    @ApiModelProperty("开立日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date openDate;
    @ApiModelProperty("到期日")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date expireDate;
    @ApiModelProperty("核心企业")
    private String coreCorp;
    @ApiModelProperty("币别")
    private String curId;
    @ApiModelProperty("凭证金额")
    private BigDecimal voucherMoney;
    @ApiModelProperty("供应商")
    private String supplyId;
    @ApiModelProperty("付款账号")
    private String payAccount;
    @ApiModelProperty("兑付金额")
    private BigDecimal cashMoney;
    @ApiModelProperty("供应商名称")
    private String supplyName;
    @ApiModelProperty("付款日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date payDate;
    @ApiModelProperty("收款方名称")
    private String receiveName;
    @ApiModelProperty("收款方账号")
    private String receiveAccount;
    @ApiModelProperty("收款开户行")
    private String receiveBank;
    @ApiModelProperty("保存标志(0=暂存,1=提交)")
    private String saveFlag;

    /** 审批状态 */
    private String approvalStatus;

    public String getSaveFlag() {
        return saveFlag;
    }

    public void setSaveFlag(String saveFlag) {
        this.saveFlag = saveFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }


    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public String getSupplyName() {
        return supplyName;
    }

    public void setSupplyName(String supplyName) {
        this.supplyName = supplyName;
    }

    public String getCashNo() {
        return cashNo;
    }

    public void setCashNo(String cashNo) {
        this.cashNo = cashNo;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCoreCorp() {
        return coreCorp;
    }

    public void setCoreCorp(String coreCorp) {
        this.coreCorp = coreCorp;
    }

    public String getCurId() {
        return curId;
    }

    public void setCurId(String curId) {
        this.curId = curId;
    }

    public BigDecimal getVoucherMoney() {
        return voucherMoney;
    }

    public void setVoucherMoney(BigDecimal voucherMoney) {
        this.voucherMoney = voucherMoney;
    }

    public String getSupplyId() {
        return supplyId;
    }

    public void setSupplyId(String supplyId) {
        this.supplyId = supplyId;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getReceiveName() {
        return receiveName;
    }

    public void setReceiveName(String receiveName) {
        this.receiveName = receiveName;
    }

    public String getReceiveAccount() {
        return receiveAccount;
    }

    public void setReceiveAccount(String receiveAccount) {
        this.receiveAccount = receiveAccount;
    }

    public String getReceiveBank() {
        return receiveBank;
    }

    public void setReceiveBank(String receiveBank) {
        this.receiveBank = receiveBank;
    }

}
